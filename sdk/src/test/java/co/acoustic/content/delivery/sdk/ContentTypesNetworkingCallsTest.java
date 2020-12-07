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

public class ContentTypesNetworkingCallsTest {
    private ContentDeliverySDK sdk;
    private MockWebServer mockWebServer = new MockWebServer();

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
        mockWebServer.start();

        sdk = new ContentDeliverySDK(SDKConfig.builder().setApiUrl(apiUrl).build(), new TestDataEncoder(), new BlockingNetworkingCallsExecutor());
    }

    @After
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void testDefaultCall() {
        final ContentDeliverySDK mockSdk = new ContentDeliverySDK(
                SDKConfig.builder()
                        .setApiUrl(mockWebServer.url("/").url().toString())
                        .build(),
                new DataEncoder(),
                new BlockingNetworkingCallsExecutor());

        ContentTypes liveContentTypes = new ContentTypes(sdk.deliverySearch(), null, new DeliverySearchQueryBuilder(), new BlockingNetworkingCallsExecutor());
        liveContentTypes.sortBy("name", true);
        liveContentTypes.filterBy("classification", liveContentTypes.getTargetDocumentClassification());
        liveContentTypes.start(0);
        liveContentTypes.rows(DEFAULT_PAGE_SIZE);
        liveContentTypes.setIncludeAllFields(false);
        ContentTypes mockContentTypes = new ContentTypes(mockSdk.deliverySearch(), null, new DeliverySearchQueryBuilder(), new BlockingNetworkingCallsExecutor());

        new APICallResultTester<>(
                "testDefaultCall",
                mockWebServer,
                liveContentTypes,
                mockContentTypes,
                "content_types_sorted_by_name_default_success_response.json",
                document -> {
                    assertTrue("document should be instance of ContentType", document instanceof ContentType);
                    assertEquals("document classification should be content-type.","content-type", document.getClassification());
                }
        ).apiCallTest();
    }

    private void testExpected(ContentType expectedDocument, ContentType documentToTest) {
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getName() + " classifications  fields: ",
                expectedDocument.getClassification(),
                documentToTest.getClassification()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getName() + " descriptions  fields: ",
                expectedDocument.getDescription(),
                documentToTest.getDescription()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getName() + " ids  fields: ",
                expectedDocument.getId(),
                documentToTest.getId()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getName() + " keywords  fields: ",
                expectedDocument.getKeywords(),
                documentToTest.getKeywords()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getName() + " locales  fields: ",
                expectedDocument.getLocale(),
                documentToTest.getLocale()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getName() + " names  fields: ",
                expectedDocument.getName(),
                documentToTest.getName()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getName() + " restricteds  fields: ",
                expectedDocument.isRestricted(),
                documentToTest.isRestricted()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getName() + " tags  fields: ",
                expectedDocument.getTags(),
                documentToTest.getTags()
        );


    }
}