/*
 * Copyright 2020 Acoustic, L.P.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Apache License, Version 2.0
 * www.apache.org
 * Home page of The Apache Software Foundation
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package co.acoustic.content.delivery.sdk;

import android.text.TextUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import okhttp3.mockwebserver.MockWebServer;

import static co.acoustic.content.delivery.sdk.Documents.DEFAULT_PAGE_SIZE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CategoriesNetworkingCallsTest {

    private MockWebServer mockWebServer = new MockWebServer();
    private SDKConfig config;

    @Before
    public void setup() throws IOException {
        final String scheme = SystemPropertiesUtils.getScheme();

        String apiUrl;
        if (TestSystemProperties.TestScheme.LIVE.equals(scheme)) {
            apiUrl = SystemPropertiesUtils.getApiUrl();

            if (TextUtils.isEmpty(apiUrl)) {
                throw new IllegalArgumentException("API_URL not specified.");
            }
        } else {
            apiUrl = "https://test";
        }

        this.config = SDKConfig.builder().setApiUrl(apiUrl).build();

        mockWebServer.start();
    }

    @After
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void testDefaultCall() {
        final ContentDeliverySDK sdk = new ContentDeliverySDK(config, new DataEncoder(), new BlockingNetworkingCallsExecutor());
        final ContentDeliverySDK mockSdk = new ContentDeliverySDK(
                SDKConfig.builder()
                        .setApiUrl(mockWebServer.url("/").url().toString())
                        .build(),
                new DataEncoder(),
                new BlockingNetworkingCallsExecutor());

        Categories liveCategories = new Categories(sdk.deliverySearch(), null, new DeliverySearchQueryBuilder(), new BlockingNetworkingCallsExecutor());
        liveCategories.sortBy("name", true);
        liveCategories.filterBy("classification", liveCategories.getTargetDocumentClassification());
        liveCategories.start(0);
        liveCategories.rows(DEFAULT_PAGE_SIZE);
        Categories mockCategories = new Categories(mockSdk.deliverySearch(), null, new DeliverySearchQueryBuilder(), new BlockingNetworkingCallsExecutor());

        new APICallResultTester<>(
                "testDefaultCall",
                mockWebServer,
                liveCategories,
                mockCategories,
                "categories_sorted_by_name_default_success_response.json",
                document -> {
                    assertTrue("document should be instance of Category", document instanceof Category);
                    assertEquals("document classification should be.","category", document.getClassification());
                }
        ).apiCallTest();
    }

    private void testExpected(Category expectedCategory, Category categoryToTest) {
        assertEquals(
                "Compare " + expectedCategory.getName() + " AND " + categoryToTest.getName() + " classifications  fields: ",
                expectedCategory.getClassification(),
                categoryToTest.getClassification()
        );
        assertEquals(
                "Compare " + expectedCategory.getName() + " AND " + categoryToTest.getName() + " descriptions  fields: ",
                expectedCategory.getDescription(),
                categoryToTest.getDescription()
        );
        assertEquals(
                "Compare " + expectedCategory.getName() + " AND " + categoryToTest.getName() + " ids  fields: ",
                expectedCategory.getId(),
                categoryToTest.getId()
        );
        assertEquals(
                "Compare " + expectedCategory.getName() + " AND " + categoryToTest.getName() + " keywords  fields: ",
                expectedCategory.getKeywords(),
                categoryToTest.getKeywords()
        );
        assertEquals(
                "Compare " + expectedCategory.getName() + " AND " + categoryToTest.getName() + " locales  fields: ",
                expectedCategory.getLocale(),
                categoryToTest.getLocale()
        );
        assertEquals(
                "Compare " + expectedCategory.getName() + " AND " + categoryToTest.getName() + " names  fields: ",
                expectedCategory.getName(),
                categoryToTest.getName()
        );
        assertEquals(
                "Compare " + expectedCategory.getName() + " AND " + categoryToTest.getName() + " restricteds  fields: ",
                expectedCategory.isRestricted(),
                categoryToTest.isRestricted()
        );
        assertEquals(
                "Compare " + expectedCategory.getName() + " AND " + categoryToTest.getName() + " tags  fields: ",
                expectedCategory.getTags(),
                categoryToTest.getTags()
        );
    }
}