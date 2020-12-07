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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import okhttp3.mockwebserver.MockWebServer;

import static co.acoustic.content.delivery.sdk.Documents.DEFAULT_PAGE_SIZE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ContentItemsNetworkingCallsTest {
    private static final Set<String> POSSIBLE_DRAFT_STATES = new HashSet<>();
    private static final Set<String> POSSIBLE_RETIRED_STATES = new HashSet<>();
    static {
        POSSIBLE_DRAFT_STATES.add("ready");
        POSSIBLE_DRAFT_STATES.add("draft");

        POSSIBLE_RETIRED_STATES.add("ready");
        POSSIBLE_RETIRED_STATES.add("draft");
        POSSIBLE_RETIRED_STATES.add("retired");
    }

    private static String apiUrl;
    private static ContentDeliverySDK sdk;
    private static MockWebServer mockWebServer = new MockWebServer();

    @BeforeClass
    public static void setup() throws IOException {
        final String scheme = SystemPropertiesUtils.getScheme();
        final String userName;
        final String password;
        if (TestSystemProperties.TestScheme.LIVE.equals(scheme)) {
            apiUrl = SystemPropertiesUtils.getApiUrl();

            if (TextUtils.isEmpty(apiUrl)) {
                throw new IllegalArgumentException("API_URL not specified.");
            }

            userName = SystemPropertiesUtils.getUserName();
            password = SystemPropertiesUtils.getPassword();
            if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
                throw new IllegalArgumentException("USERNAME or PASSWORD not specified.");
            }
            sdk = new ContentDeliverySDK(SDKConfig.builder().setApiUrl(apiUrl).build(), new TestDataEncoder(), new BlockingNetworkingCallsExecutor());
            sdk.login(userName, password, error -> { if (error != null) throw new IllegalArgumentException("Unable to login wrong credentials."); });
        } else {
            apiUrl = "http://test.blah.com/";
            sdk = new ContentDeliverySDK(SDKConfig.builder().setApiUrl(apiUrl).build(), new TestDataEncoder(), new BlockingNetworkingCallsExecutor());
        }
        mockWebServer.start();
    }

    @AfterClass
    public static void tearDown() throws IOException {
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

        ContentItems liveContentItems = new ContentItems(sdk.deliverySearch(), null, new DeliverySearchQueryBuilder(), new BlockingNetworkingCallsExecutor());
        liveContentItems.sortBy("name", true);
        liveContentItems.filterBy("classification", liveContentItems.getTargetDocumentClassification());
        liveContentItems.start(0);
        liveContentItems.rows(DEFAULT_PAGE_SIZE);
        ContentItems mockContentItems = new ContentItems(mockSdk.deliverySearch(), null, new DeliverySearchQueryBuilder(), new BlockingNetworkingCallsExecutor());

        new APICallResultTester<>(
                "testDefaultCall",
                mockWebServer,
                liveContentItems,
                mockContentItems,
                "content_items_sorted_by_name_default_success_response.json",
                document -> {
                    assertTrue("document should be instance of ContentItem", document instanceof ContentItem);
                    assertEquals("document classification should be.","content", document.getClassification());
                }
        ).apiCallTest();
    }

    @Test
    public void testNotIncludeAllFieldsCall() {
        final ContentDeliverySDK mockSdk = new ContentDeliverySDK(
                SDKConfig.builder()
                        .setApiUrl(mockWebServer.url("/").url().toString())
                        .build(),
                new DataEncoder(),
                new BlockingNetworkingCallsExecutor());

        ContentItems liveContentItems = new ContentItems(sdk.deliverySearch(), null, new DeliverySearchQueryBuilder(), new BlockingNetworkingCallsExecutor());
        liveContentItems.sortBy("name", true);
        liveContentItems.filterBy("classification", liveContentItems.getTargetDocumentClassification());
        liveContentItems.start(0);
        liveContentItems.rows(DEFAULT_PAGE_SIZE);
        liveContentItems.includeAllFields(false);
        ContentItems mockContentItems = new ContentItems(mockSdk.deliverySearch(), null, new DeliverySearchQueryBuilder(), new BlockingNetworkingCallsExecutor());

        new APICallResultTester<>(
                "testNotIncludeAllFieldsCall",
                mockWebServer,
                liveContentItems,
                mockContentItems,
                "content_items_sorted_by_name_not_include_all_fields_success_response.json",
                document -> {
                    assertTrue("document should be instance of ContentItem", document instanceof ContentItem);
                    assertEquals("document classification should be.","content", document.getClassification());
                    assertNull(document.getDocument());
                    assertNull(document.getStatus());
                }
        ).apiCallTest();
    }

    @Test
    public void testDraftCall() {
        final ContentDeliverySDK mockSdk = new ContentDeliverySDK(
                SDKConfig.builder()
                        .setApiUrl(mockWebServer.url("/").url().toString())
                        .build(),
                new DataEncoder(),
                new BlockingNetworkingCallsExecutor());

        ContentItems liveContentItems = new ContentItems(sdk.deliverySearch(), null, new DeliverySearchQueryBuilder(), new BlockingNetworkingCallsExecutor());
        liveContentItems.sortBy("name", true);
        liveContentItems.filterBy("classification", liveContentItems.getTargetDocumentClassification());
        liveContentItems.start(0);
        liveContentItems.rows(DEFAULT_PAGE_SIZE);
        liveContentItems.includeDraft(true);
        ContentItems mockAssets = new ContentItems(mockSdk.deliverySearch(), null, new DeliverySearchQueryBuilder(), new BlockingNetworkingCallsExecutor());

        new APICallResultTester<>(
                "testDraftCall",
                mockWebServer,
                liveContentItems,
                mockAssets,
                "content_items_sorted_by_name_include_draft_success_response.json",
                document -> {
                    assertTrue("document should be instance of ContentItem", document instanceof ContentItem);
                    assertEquals("document classification should be.","content", document.getClassification());
                    assertTrue("document status should be ready or draft", POSSIBLE_DRAFT_STATES.contains(document.getStatus()));
                }
        ).apiCallTest();
    }

    @Test (expected = RuntimeException.class)
    public void testNotAuthorizedDraftCall() {
        ContentDeliverySDK sdk = new ContentDeliverySDK(SDKConfig.builder().setApiUrl(apiUrl).build(), new TestDataEncoder(), new BlockingNetworkingCallsExecutor());
        ContentItems liveContentItems = new ContentItems(sdk.deliverySearch(), null, new DeliverySearchQueryBuilder(), new BlockingNetworkingCallsExecutor());
        liveContentItems.sortBy("name", true);
        liveContentItems.filterBy("classification", liveContentItems.getTargetDocumentClassification());
        liveContentItems.start(0);
        liveContentItems.rows(DEFAULT_PAGE_SIZE);
        liveContentItems.includeDraft(true);

        liveContentItems
                .get()
                .then(result -> fail())
                .error(error -> { throw new RuntimeException(error.getMessage()); });
    }

    @Test
    public void testRetiredCall() {
        final ContentDeliverySDK mockSdk = new ContentDeliverySDK(
                SDKConfig.builder()
                        .setApiUrl(mockWebServer.url("/").url().toString())
                        .build(),
                new DataEncoder(),
                new BlockingNetworkingCallsExecutor());

        ContentItems liveContentItems = new ContentItems(sdk.deliverySearch(), null, new DeliverySearchQueryBuilder(), new BlockingNetworkingCallsExecutor());
        liveContentItems.sortBy("name", true);
        liveContentItems.filterBy("classification", liveContentItems.getTargetDocumentClassification());
        liveContentItems.start(0);
        liveContentItems.rows(DEFAULT_PAGE_SIZE);
        liveContentItems.includeRetired(true);
        ContentItems mockAssets = new ContentItems(mockSdk.deliverySearch(), null, new DeliverySearchQueryBuilder(), new BlockingNetworkingCallsExecutor());

        new APICallResultTester<>(
                "testRetiredCall",
                mockWebServer,
                liveContentItems,
                mockAssets,
                "content_items_sorted_by_name_include_retired_success_response.json",
                document -> {
                    assertTrue("document should be instance of ContentItem", document instanceof ContentItem);
                    assertEquals("document classification should be.","content", document.getClassification());
                    assertTrue("document status should be ready or draft", POSSIBLE_RETIRED_STATES.contains(document.getStatus()));
                }
        ).apiCallTest();
    }

    @Test (expected = RuntimeException.class)
    public void testNotAuthorizedRetiredCall() {
        ContentDeliverySDK sdk = new ContentDeliverySDK(SDKConfig.builder().setApiUrl(apiUrl).build(), new TestDataEncoder(), new BlockingNetworkingCallsExecutor());
        ContentItems liveContentItems = new ContentItems(sdk.deliverySearch(), null, new DeliverySearchQueryBuilder(), new BlockingNetworkingCallsExecutor());
        liveContentItems.sortBy("name", true);
        liveContentItems.filterBy("classification", liveContentItems.getTargetDocumentClassification());
        liveContentItems.start(0);
        liveContentItems.rows(DEFAULT_PAGE_SIZE);
        liveContentItems.includeRetired(true);

        liveContentItems
                .get()
                .then(result -> fail())
                .error(error -> { throw new RuntimeException(error.getMessage()); });
    }

    @Test
    public void testDraftNotIncludeAllFieldsCall() {
        final ContentDeliverySDK mockSdk = new ContentDeliverySDK(
                SDKConfig.builder()
                        .setApiUrl(mockWebServer.url("/").url().toString())
                        .build(),
                new DataEncoder(),
                new BlockingNetworkingCallsExecutor());

        ContentItems liveContentItems = new ContentItems(sdk.deliverySearch(), null, new DeliverySearchQueryBuilder(), new BlockingNetworkingCallsExecutor());
        liveContentItems.sortBy("name", true);
        liveContentItems.filterBy("classification", liveContentItems.getTargetDocumentClassification());
        liveContentItems.start(0);
        liveContentItems.rows(DEFAULT_PAGE_SIZE);
        liveContentItems.includeDraft(true);
        liveContentItems.includeAllFields(false);
        ContentItems mockAssets = new ContentItems(mockSdk.deliverySearch(), null, new DeliverySearchQueryBuilder(), new BlockingNetworkingCallsExecutor());

        new APICallResultTester<>(
                "testDraftNotIncludeAllFieldsCall",
                mockWebServer,
                liveContentItems,
                mockAssets,
                "content_items_sorted_by_name_draft_not_include_all_fields_success_response.json",
                document -> {
                    assertTrue("document should be instance of ContentItem", document instanceof ContentItem);
                    assertEquals("document classification should be.","content", document.getClassification());
                    assertNull(document.getDocument());
                    assertNull(document.getStatus());
                }
        ).apiCallTest();
    }

    @Test
    public void testRetiredNotIncludeAllFieldsCall() {
        final ContentDeliverySDK mockSdk = new ContentDeliverySDK(
                SDKConfig.builder()
                        .setApiUrl(mockWebServer.url("/").url().toString())
                        .build(),
                new DataEncoder(),
                new BlockingNetworkingCallsExecutor());

        ContentItems liveContentItems = new ContentItems(sdk.deliverySearch(), null, new DeliverySearchQueryBuilder(), new BlockingNetworkingCallsExecutor());
        liveContentItems.sortBy("name", true);
        liveContentItems.filterBy("classification", liveContentItems.getTargetDocumentClassification());
        liveContentItems.start(0);
        liveContentItems.rows(DEFAULT_PAGE_SIZE);
        liveContentItems.includeRetired(true);
        liveContentItems.includeAllFields(false);
        ContentItems mockAssets = new ContentItems(mockSdk.deliverySearch(), null, new DeliverySearchQueryBuilder(), new BlockingNetworkingCallsExecutor());

        new APICallResultTester<>(
                "testRetiredNotIncludeAllFieldsCall",
                mockWebServer,
                liveContentItems,
                mockAssets,
                "content_items_sorted_by_name_retired_not_include_all_fields_success_response.json",
                document -> {
                    assertTrue("document should be instance of ContentItem", document instanceof ContentItem);
                    assertEquals("document classification should be.","content", document.getClassification());
                    assertNull(document.getDocument());
                    assertNull(document.getStatus());
                }
        ).apiCallTest();
    }

    @Test
    public void testIncludeProtectedContent() {
        final ContentDeliverySDK mockSdk = new ContentDeliverySDK(
                SDKConfig.builder()
                        .setApiUrl(mockWebServer.url("/").url().toString())
                        .build(),
                new DataEncoder(),
                new BlockingNetworkingCallsExecutor());

        ContentItems liveContentItems = new ContentItems(sdk.deliverySearch(), null, new DeliverySearchQueryBuilder(), new BlockingNetworkingCallsExecutor());
        liveContentItems.sortBy("name", true);
        liveContentItems.filterBy("classification", liveContentItems.getTargetDocumentClassification());
        liveContentItems.start(0);
        liveContentItems.rows(DEFAULT_PAGE_SIZE);
        liveContentItems.protectedContent(true);
        ContentItems mockAssets = new ContentItems(mockSdk.deliverySearch(), null, new DeliverySearchQueryBuilder(), new BlockingNetworkingCallsExecutor());

        new APICallResultTester<>(
                "testIncludeProtectedContent",
                mockWebServer,
                liveContentItems,
                mockAssets,
                "content_items_protected_content_success_response.json",
                document -> {
                    assertTrue("document should be instance of ContentItem", document instanceof ContentItem);
                    assertEquals("document classification should be.","content", document.getClassification());
                }
        ).apiCallTest();
    }

    @Test (expected = RuntimeException.class)
    public void testNotAuthorizedProtectedContentCall() {
        ContentDeliverySDK sdk = new ContentDeliverySDK(SDKConfig.builder().setApiUrl(apiUrl).build(), new TestDataEncoder(), new BlockingNetworkingCallsExecutor());
        ContentItems liveContentItems = new ContentItems(sdk.deliverySearch(), null, new DeliverySearchQueryBuilder(), new BlockingNetworkingCallsExecutor());
        liveContentItems.sortBy("name", true);
        liveContentItems.filterBy("classification", liveContentItems.getTargetDocumentClassification());
        liveContentItems.start(0);
        liveContentItems.rows(DEFAULT_PAGE_SIZE);
        liveContentItems.protectedContent(true);

        liveContentItems
                .get()
                .then(result -> fail())
                .error(error -> { throw new RuntimeException(error.getMessage()); });
    }

    @Test
    public void testCompleteContentContext() {
        final ContentDeliverySDK mockSdk = new ContentDeliverySDK(
                SDKConfig.builder()
                        .setApiUrl(mockWebServer.url("/").url().toString())
                        .build(),
                new DataEncoder(),
                new BlockingNetworkingCallsExecutor());

        ContentItems liveContentItems = new ContentItems(sdk.deliverySearch(), null, new DeliverySearchQueryBuilder(), new BlockingNetworkingCallsExecutor());
        liveContentItems.sortBy("name", true);
        liveContentItems.filterBy("classification", liveContentItems.getTargetDocumentClassification());
        liveContentItems.start(0);
        liveContentItems.rows(DEFAULT_PAGE_SIZE);
        liveContentItems.completeContentContext(true);
        ContentItems mockAssets = new ContentItems(mockSdk.deliverySearch(), null, new DeliverySearchQueryBuilder(), new BlockingNetworkingCallsExecutor());

        new APICallResultTester<>(
                "testCompleteContentContext",
                mockWebServer,
                liveContentItems,
                mockAssets,
                "content_items_complete_content_context_success_response.json",
                document -> {
                    assertTrue("document should be instance of ContentItem", document instanceof ContentItem);
                    assertEquals("document classification should be.","content", document.getClassification());
                }
        ).apiCallTest();
    }

    private void testExpected(ContentItem expectedDocument, ContentItem documentToTest) {
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
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getName() + " restricted  fields: ",
                expectedDocument.isRestricted(),
                documentToTest.isRestricted()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getName() + " tags  fields: ",
                expectedDocument.getTags(),
                documentToTest.getTags()
        );

        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getName() + " boolean1  fields: ",
                expectedDocument.getBoolean1(),
                documentToTest.getBoolean1()
        );

        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getName() + " boolean2  fields: ",
                expectedDocument.getBoolean2(),
                documentToTest.getBoolean2()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getName() + " categories  fields: ",
                expectedDocument.getCategories(),
                documentToTest.getCategories()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getName() + " categoryLeaves  fields: ",
                expectedDocument.getCategoryLeaves(),
                documentToTest.getCategoryLeaves()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getName() + " date1  fields: ",
                expectedDocument.getDate1(),
                documentToTest.getDate1()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getName() + " date2  fields: ",
                expectedDocument.getDate2(),
                documentToTest.getDate2()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getName() + " documents  fields: ",
                (expectedDocument.getDocument() != null) ? expectedDocument.getDocument().toString() : null,
                (documentToTest.getDocument() != null) ? documentToTest.getDocument().toString() : null
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getName() + " generatedFiles  fields: ",
                expectedDocument.getGeneratedFiles(),
                documentToTest.getGeneratedFiles()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getName() + " isManaged  fields: ",
                expectedDocument.isManaged(),
                documentToTest.isManaged()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getName() + " location1  fields: ",
                expectedDocument.getLocation1(),
                documentToTest.getLocation1()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getName() + " locations  fields: ",
                expectedDocument.getLocations(),
                documentToTest.getLocations()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getName() + " number1  fields: ",
                expectedDocument.getNumber1(),
                documentToTest.getNumber1()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getName() + " number2  fields: ",
                expectedDocument.getNumber2(),
                documentToTest.getNumber2()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getStatus() + " statuses  fields: ",
                expectedDocument.getStatus(),
                documentToTest.getStatus()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getStatus() + " string1  fields: ",
                expectedDocument.getString1(),
                documentToTest.getString1()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getStatus() + " string2  fields: ",
                expectedDocument.getString2(),
                documentToTest.getString2()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getStatus() + " string3  fields: ",
                expectedDocument.getString3(),
                documentToTest.getString3()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getStatus() + " string4  fields: ",
                expectedDocument.getString4(),
                documentToTest.getString4()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getStatus() + " sortableDate1  fields: ",
                expectedDocument.getSortableDate1(),
                documentToTest.getSortableDate1()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getStatus() + " sortableDate2  fields: ",
                expectedDocument.getSortableDate2(),
                documentToTest.getSortableDate2()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getStatus() + " sortableNumber1  fields: ",
                expectedDocument.getSortableNumber1(),
                documentToTest.getSortableNumber1()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getStatus() + " sortableNumber2  fields: ",
                expectedDocument.getSortableNumber2(),
                documentToTest.getSortableNumber2()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getStatus() + " sortableString1  fields: ",
                expectedDocument.getString1(),
                documentToTest.getString1()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getStatus() + " sortableString2  fields: ",
                expectedDocument.getString2(),
                documentToTest.getString2()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getStatus() + " sortableString3  fields: ",
                expectedDocument.getString3(),
                documentToTest.getString3()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getStatus() + " sortableString4  fields: ",
                expectedDocument.getString4(),
                documentToTest.getString4()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getStatus() + " text  fields: ",
                expectedDocument.getText(),
                documentToTest.getText()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getStatus() + " type  fields: ",
                expectedDocument.getType(),
                documentToTest.getType()
        );
        assertEquals(
                "Compare " + expectedDocument.getName() + " AND " + documentToTest.getStatus() + " typeId  fields: ",
                expectedDocument.getTypeId(),
                documentToTest.getTypeId()
        );
    }
}