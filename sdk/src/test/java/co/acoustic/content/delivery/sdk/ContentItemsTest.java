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

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import okhttp3.Headers;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;
import retrofit2.Callback;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ContentItemsTest {

    private MockWebServer mockWebServer = new MockWebServer();

    @Before
    public void setUp() throws Exception {
        mockWebServer.start();
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    public void testCreationWithoutState() {
        final DeliverySearch deliverySearch = ContentDeliverySDK
                .create(SDKConfig.builder().setApiUrl("https://my7.test.com/").build())
                .deliverySearch();

        ContentItems documents = new ContentItems(deliverySearch, null);
        assertNotNull(documents);
    }

    @Test
    public void testCreationWithState() {
        final DeliverySearch deliverySearch = mock(DeliverySearch.class);
        final Documents.State mockState = mock(Documents.State.class);

        ContentItems documents = new ContentItems(deliverySearch, mockState);

        verify(mockState).getDeliverySearchQueryBuilder();
        verify(mockState).isIncludeDraft();
        verify(mockState).isIncludeProtectedContent();
        verify(mockState).isRetrieveCompleteContentContext();
        verify(mockState).isIncludeAllFields();
        verify(mockState).isIncludeRetired();
        assertNotNull(documents);
    }

    @Test
    public void testCreationWithInjectedQueryBuilder() {
        final DeliverySearch deliverySearch = mock(DeliverySearch.class);
        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);

        ContentItems documents = new ContentItems(deliverySearch, null, queryBuilder);

        assertNotNull(documents);
    }

    @Test
    public void testCreationWithInjectedQueryBuilderAndState() {
        final DeliverySearch deliverySearch = mock(DeliverySearch.class);
        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        final Documents.State mockState = mock(Documents.State.class);

        ContentItems documents = new ContentItems(deliverySearch, mockState, queryBuilder);
        verify(mockState).isIncludeDraft();
        verify(mockState).isIncludeProtectedContent();
        verify(mockState).isRetrieveCompleteContentContext();
        verify(mockState).isIncludeAllFields();
        verify(mockState).isIncludeRetired();
        assertNotNull(documents);
    }

    @Test
    public void testCreateInstance() {
        final DeliverySearch deliverySearch = mock(DeliverySearch.class);
        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);

        ContentItems documents = new ContentItems(deliverySearch, null, queryBuilder);
        assertNotNull(documents.createInstance(mock(DeliverySearch.class), mock(DeliverySearchQueryBuilder.class)));
    }

    @Test
    public void testCreateDocument() throws JSONException {
        final DeliverySearch deliverySearch = mock(DeliverySearch.class);
        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);

        ContentItems documents = new ContentItems(deliverySearch, null, queryBuilder);
        assertNotNull(documents.createDocument(createFullRawDoc()));
    }

    @Test
    public void testGetTargetDocumentClassification() {
        final DeliverySearch deliverySearch = mock(DeliverySearch.class);
        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);

        ContentItems documents = new ContentItems(deliverySearch, null, queryBuilder);
        assertEquals("content", documents.getTargetDocumentClassification());
    }

    @Test
    public void testSortBy() {
        final String field = "category";
        final boolean asc = false;

        final DeliverySearch deliverySearch = mock(DeliverySearch.class);
        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);

        ContentItems documents = new ContentItems(deliverySearch, null, queryBuilder);
        documents.sortBy(field, asc);
        verify(queryBuilder).sort(field, asc);
    }

    @Test(expected = NullPointerException.class)
    public void testSortWithNullField() {
        final String field = null;
        final boolean asc = false;

        final DeliverySearch deliverySearch = mock(DeliverySearch.class);
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();

        ContentItems documents = new ContentItems(deliverySearch, null, queryBuilder);
        documents.sortBy(field, asc);
    }

    @Test
    public void testFilterBy() {
        final String field = "name";
        final String value = "n*";

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);

        ContentItems documents = new ContentItems(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterBy(field, value);
        verify(queryBuilder).filterQuery(field, value);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterByWithNullField() {
        final String field = null;
        final String value = "n*";

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();

        ContentItems documents = new ContentItems(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterBy(field, value);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterByWithNullValue() {
        final String field = "name";
        final String value = null;

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();

        ContentItems documents = new ContentItems(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterBy(field, value);
    }

    @Test
    public void testFilterQuery() {
        final String query = "popularity : [10 TO *]";

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        ContentItems documents = new ContentItems(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterQuery(query);
        verify(queryBuilder).filterQuery(query);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterQueryWithNullQuery() {
        final String query = null;

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();

        ContentItems documents = new ContentItems(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterQuery(query);
    }

    @Test
    public void testSearchByText() {
        final String text = "classification:asset";

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        ContentItems documents = new ContentItems(mock(DeliverySearch.class), null, queryBuilder);
        documents.searchByText(text);
        verify(queryBuilder).query(text);
    }

    @Test(expected = NullPointerException.class)
    public void testSearchByTextWithNull() {
        final String text = null;

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();

        ContentItems documents = new ContentItems(mock(DeliverySearch.class), null, queryBuilder);
        documents.searchByText(text);
    }

    @Test
    public void testRows() {
        final int rows = 15;

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        ContentItems documents = new ContentItems(mock(DeliverySearch.class), null, queryBuilder);
        documents.rows(rows);
        verify(queryBuilder).rows(rows);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRowsLessThen1() {
        final int rows = 0;

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        ContentItems documents = new ContentItems(mock(DeliverySearch.class), null, queryBuilder);
        documents.rows(rows);
    }

    @Test
    public void testStart() {
        final int start = 15;

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        ContentItems documents = new ContentItems(mock(DeliverySearch.class), null, queryBuilder);
        documents.start(start);
        verify(queryBuilder).start(start);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartLessThen0() {
        final int start = -1;

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        ContentItems documents = new ContentItems(mock(DeliverySearch.class), null, queryBuilder);
        documents.rows(start);
    }

    @Test
    public void TestFilterByName() {
        final String value = "footerConfig";

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        ContentItems documents = new ContentItems(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterByName(value);
        verify(queryBuilder).filterQuery("name", value);
    }

    @Test
    public void TestFilterById() {
        final String id = "ae72d304-ad18-4bf3-b213-4a79c829e458";

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        ContentItems documents = new ContentItems(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterById(id);
        verify(queryBuilder).filterQuery("id", id);
    }

    @Test
    public void TestFilterByCategories() {
        final String value = "\"Dynamic list criteria/Type/Design page\", \"List ordering options/By date descending\"";

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        ContentItems documents = new ContentItems(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterByCategories(value);
        verify(queryBuilder).filterQuery("categories", "(" + value + ")");
    }

    @Test(expected = NullPointerException.class)
    public void TestFilterByCategoriesWithNull() {
        final String categories = null;

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        ContentItems documents = new ContentItems(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterByCategories(categories);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestFilterByCategoriesWithEmpty() {
        final String categories = "";

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        ContentItems documents = new ContentItems(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterByCategories(categories);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestFilterByTagsWithNull() {
        final String[] value = new String[]{"website template", null};

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        ContentItems documents = new ContentItems(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterByTags(value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestFilterByTagsWithEmpty() {
        final String[] value = new String[]{"website template", ""};

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        ContentItems documents = new ContentItems(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterByTags(value);
    }

    @Test
    public void TestFilterByTags() {
        final String[] value = new String[]{"website template", "IBM sample"};

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        ContentItems documents = new ContentItems(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterByTags(value);
        verify(queryBuilder).filterQuery("tags", "(website template OR IBM sample)");
    }

    @Test
    public void TestFilterByTagsWithMoreThen2Tags() {
        final String[] value = new String[]{"website template", "IBM sample", "Tag1", "Tag4"};

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        ContentItems documents = new ContentItems(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterByTags(value);
        verify(queryBuilder).filterQuery("tags", "(website template OR IBM sample OR Tag1 OR Tag4)");
    }

    @Test
    public void TestIncludeDraftTrue() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        ContentItems documents = new ContentItems(mock(DeliverySearch.class), null, queryBuilder);
        documents.includeDraft(true);

        Documents.State state = documents.getState();
        assertTrue(state.isIncludeDraft());
    }

    @Test
    public void TestIncludeDraftFalse() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        ContentItems documents = new ContentItems(mock(DeliverySearch.class), null, queryBuilder);
        documents.includeDraft(false);

        Documents.State state = documents.getState();
        assertFalse(state.isIncludeDraft());
    }

    @Test
    public void TestIncludeProtectedContentTrue() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        ContentItems documents = new ContentItems(mock(DeliverySearch.class), null, queryBuilder);
        documents.protectedContent(true);

        Documents.State state = documents.getState();
        assertTrue(state.isIncludeProtectedContent());
    }

    @Test
    public void TestIncludeProtectedContentFalse() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        ContentItems documents = new ContentItems(mock(DeliverySearch.class), null, queryBuilder);
        documents.protectedContent(false);

        Documents.State state = documents.getState();
        assertFalse(state.isIncludeProtectedContent());
    }

    @Test
    public void TestRetrieveCompleteContentContextTrue() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        ContentItems documents = new ContentItems(mock(DeliverySearch.class), null, queryBuilder);
        documents.completeContentContext(true);

        Documents.State state = documents.getState();
        assertTrue(state.isRetrieveCompleteContentContext());
    }

    @Test
    public void TestRetrieveCompleteContentContextFalse() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        ContentItems documents = new ContentItems(mock(DeliverySearch.class), null, queryBuilder);
        documents.completeContentContext(false);

        Documents.State state = documents.getState();
        assertFalse(state.isRetrieveCompleteContentContext());
    }

    @Test
    public void TestIncludeAllFieldsTrue() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        ContentItems documents = new ContentItems(mock(DeliverySearch.class), null, queryBuilder);
        documents.includeAllFields(true);

        Documents.State state = documents.getState();
        assertTrue(state.isIncludeAllFields());
    }

    @Test
    public void TestIncludeAllFieldsFalse() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        ContentItems documents = new ContentItems(mock(DeliverySearch.class), null, queryBuilder);
        documents.includeAllFields(false);

        Documents.State state = documents.getState();
        assertFalse(state.isIncludeAllFields());
    }

    @Test
    public void TestIncludeRetiredTrue() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        ContentItems documents = new ContentItems(mock(DeliverySearch.class), null, queryBuilder);
        documents.includeRetired(true);

        Documents.State state = documents.getState();
        assertTrue(state.isIncludeRetired());
    }

    @Test
    public void TestIncludeRetiredFalse() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        ContentItems documents = new ContentItems(mock(DeliverySearch.class), null, queryBuilder);
        documents.includeRetired(false);

        Documents.State state = documents.getState();
        assertFalse(state.isIncludeRetired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGet() {
        /*
         * Test verifies that get method trigger call.
         */
        class NetworkingCallsExecutorImpl implements NetworkingCallsExecutor {

            @Override
            public <ResponseType> void executeCall(Call<ResponseType> call, Callback<ResponseType> callback) {
                //Letting test know that executeCall was triggered.
                throw new IllegalArgumentException("Call execution successfully");
            }
        }

        final DeliverySearch deliverySearch = ContentDeliverySDK
                .create(SDKConfig.builder().setApiUrl("https://my7.test.com/").build())
                .deliverySearch();
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        new ContentItems(deliverySearch, null, queryBuilder, new NetworkingCallsExecutorImpl())
                .get();
    }

    @Test
    public void testThen() {
        final MockResponse successfulAssetsResponse = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setHeaders(
                        new Headers.Builder()
                                .add("cache-control", "public, max-age=30, s-maxage=30")
                                .add("content-security-policy", "frame-ancestors 'self' https://www.digitalexperience.ibm.com https://www.customer-engagement.ibm.com https://commerceinsights.ibmcloud.com https://my7-preview.content-cms.com; report-uri https://www.digitalexperience.ibm.com/api/csp/report")
                                .add("content-type", "application/json; charset=utf-8")
                                .add("date", "Fri, 20 Mar 2020 11:53:57 GMT")
                                .add("status", "200")
                                .add("strict-transport-security", "max-age=31536000; includeSubDomains")
                                .add("vary", "Accept-Encoding")
                                .add("x-content-type-options", "nosniff")
                                .add("x-ibm-dx-request-id", "74789069ddb364a45b85bd05f84b21c2")
                                .add("x-newrelic-app-data", "PxQGV1JTCwAIR1RWAAMDUVMIFB9AMQYAZBBZDEtZV0ZaClc9HjdWEBBOa04CBlRaRgETG2seQVc4HkVWAxQAChBKfydsERYeA0sJTQFPA1NXBgFRU1QBHx1VTUBVVwRWVQNQAAFeAFoEWw5XGhRSU18WXDw=")
                                .add("x-response-time", "46.031ms")
                                .add("x-xss-protection", "1; mode=block")
                                .build()
                )
                .setBody(ResourceReader.read("content_items_default_success_response.json"));

        mockWebServer.enqueue(successfulAssetsResponse);

        final SDKConfig sdkConfig = SDKConfig.builder().setApiUrl(mockWebServer.url("/").url().toString()).build();

        final ContentDeliverySDK sdk = new ContentDeliverySDK(sdkConfig, new DataEncoder());


        final DeliverySearch deliverySearch = sdk.deliverySearch();
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        final ContentItems documents = new ContentItems(deliverySearch, null, queryBuilder, new BlockingNetworkingCallsExecutor());

        documents
                .get()
                .then(result -> assertNotNull("Get result should not be null", result)
                )
                .error(error -> fail("Code shouldn't came to this point"));
    }

    @Test
    public void testError() {
        final MockResponse successfulAssetsResponse = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_FORBIDDEN)
                .setHeaders(
                        new Headers.Builder()
                                .add("cache-control", "no-store")
                                .add("content-type", "application/json; charset=utf-8")
                                .add("date", "Fri, 20 Mar 2020 12:35:53 GMT")
                                .add("etag", "W/\"587-HP9v3pQdNoD3JHcFwFmsAzec10A\"")
                                .add("status", "403")
                                .add("strict-transport-security", "max-age=31536000; includeSubDomains")
                                .add("vary", "Accept")
                                .add("x-content-type-options", "nosniff")
                                .add("x-ibm-dx-request-id", "278cb4fb951d37b46d26560b7b8e03ce")
                                .add("x-response-time", "22.700ms")
                                .add("x-xss-protection", "1; mode=block")
                                .build()
                )
                .setBody(ResourceReader.read("access_controll_error_response.json"));

        mockWebServer.enqueue(successfulAssetsResponse);

        final SDKConfig sdkConfig = SDKConfig.builder().setApiUrl(mockWebServer.url("/").url().toString()).build();

        final ContentDeliverySDK sdk = new ContentDeliverySDK(sdkConfig, new DataEncoder());


        final DeliverySearch deliverySearch = sdk.deliverySearch();
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        final ContentItems documents = new ContentItems(deliverySearch, null, queryBuilder, new BlockingNetworkingCallsExecutor());

        documents
                .get()
                .then(result -> fail("Code shouldn't came to this point"))
                .error(error -> assertNotNull("Get result should not be null", error));
    }

    private DeliverySearchResponseDocument createFullRawDoc() throws JSONException {
        final ArrayList<Boolean> boolean1 = new ArrayList<>();
        boolean1.add(true);
        boolean1.add(false);
        boolean1.add(true);
        final ArrayList<Boolean> boolean2 = new ArrayList<>();
        boolean2.add(false);
        boolean2.add(false);
        boolean2.add(true);
        final ArrayList<String> categories = new ArrayList<>();
        categories.add("Dynamic list criteria/Date/Future dates (Events)");
        categories.add("Dynamic list criteria/Type/Event");
        categories.add("List ordering options/By date ascending");
        final ArrayList<String> categoryLeaves = new ArrayList<>();
        categoryLeaves.add("Future dates (Events)");
        categoryLeaves.add("Event");
        categoryLeaves.add("By date ascending");
        final ArrayList<String> date1 = new ArrayList<>();
        date1.add("2029-09-21T04:00:00Z");
        date1.add("2029-09-22T04:00:00Z");
        date1.add("2029-09-23T04:00:00Z");
        date1.add("2029-09-24T04:00:00Z");
        final ArrayList<String> date2 = new ArrayList<>();
        date2.add("2029-09-21T04:00:00Z");
        date2.add("2029-09-22T04:00:00Z");
        date2.add("2029-09-23T04:00:00Z");
        date2.add("2029-09-24T04:00:00Z");
        final ArrayList<String> generatedFiles = new ArrayList<>();
        generatedFiles.add("test1/api/ae6a1610-fd30-4b81-8871-0f7f11f95426/delivery/v1/resources/09884b0e-a98d-4614-aebf-5388563c8edf?fit=inside%7C220:145");
        generatedFiles.add("test2/api/ae6a1610-fd30-4b81-8871-0f7f11f95426/delivery/v1/resources/09884b0e-a98d-4614-aebf-5388563c8edf?fit=inside%7C220:145");
        generatedFiles.add("test3/api/ae6a1610-fd30-4b81-8871-0f7f11f95426/delivery/v1/resources/09884b0e-a98d-4614-aebf-5388563c8edf?fit=inside%7C220:145");
        final ArrayList<String> location1 = new ArrayList<>();
        location1.add("/dxdam/3a/3a71b9cf-f6da-4b66-85f7-421c5a693d1e");
        location1.add("/dxdam/6f/6f38a240-7fb7-4be2-9304-5198e4350ee1");
        location1.add("/dxdam/6b/6bf5f2fa-03e8-4054-af2b-f5d78d3d6f55");
        final ArrayList<String> locations = new ArrayList<>();
        locations.add("/dxdam/3a/3a71b9cf-f6da-4b66-85f7-421c5a693d1e");
        locations.add("/dxdam/6f/6f38a240-7fb7-4be2-9304-5198e4350ee1");
        locations.add("/dxdam/6b/6bf5f2fa-03e8-4054-af2b-f5d78d3d6f55");
        final ArrayList<Double> number1 = new ArrayList<>();
        number1.add(10.65);
        number1.add(192.34);
        number1.add(23455.22);
        final ArrayList<Double> number2 = new ArrayList<>();
        number2.add(10.65);
        number2.add(192.34);
        number2.add(23455.22);
        final ArrayList<String> string1 = new ArrayList<>();
        string1.add("test_string1");
        string1.add("test_string2");
        string1.add("test_string3");
        string1.add("test_string4");
        final ArrayList<String> string2 = new ArrayList<>();
        string2.add("test_string1");
        string2.add("test_string2");
        string2.add("test_string3");
        string2.add("test_string4");
        final ArrayList<String> string3 = new ArrayList<>();
        string3.add("test_string1");
        string3.add("test_string2");
        string3.add("test_string3");
        string3.add("test_string4");
        final ArrayList<String> string4 = new ArrayList<>();
        string4.add("test_string1");
        string4.add("test_string2");
        string4.add("test_string3");
        string4.add("test_string4");
        final ArrayList<String> text = new ArrayList<>();
        text.add("copyright Oslo");
        text.add("salesNumber +1 (888) 777-5555");
        text.add("labelForCustomerService Customer service number");
        text.add("emailAddress customer-service@example.com");
        text.add("customerServiceContactNumber +1 (888) 777-5555");
        text.add("labelForSales Sales number");
        text.add("Logo");
        final ArrayList<String> testKeywords = new ArrayList<>();
        testKeywords.add("keyword1");
        testKeywords.add("keyword2");
        testKeywords.add("keyword3");
        testKeywords.add("keyword4");

        final ArrayList<String> testTags = new ArrayList<>();
        testTags.add("travel site sample");
        testTags.add("Sights");

        final DeliverySearchResponseDocument raw = new DeliverySearchResponseDocument();
        raw.boolean1 = boolean1;
        raw.boolean2 = boolean2;
        raw.categories = categories;
        raw.categoryLeaves = categoryLeaves;
        raw.date1 = date1;
        raw.date2 = date2;
        raw.document = createDocument();
        raw.generatedFiles = generatedFiles;
        raw.isManaged = true;
        raw.location1 = location1;
        raw.locations = locations;
        raw.number1 = number1;
        raw.number2 = number2;
        raw.status = "ready";
        raw.string1 = string1;
        raw.string2 = string2;
        raw.string3 = string3;
        raw.string4 = string4;
        raw.sortableDate1 = "2029-09-21T04:00:00Z";
        raw.sortableDate2 = "2029-09-21T04:00:00Z";
        raw.sortableNumber1 = 10.65;
        raw.sortableNumber2 = 192.34;
        raw.sortableString1 = "test_string1";
        raw.sortableString2 = "test_string2";
        raw.sortableString3 = "test_string3";
        raw.sortableString4 = "test_string4";
        raw.text = text;
        raw.type = "Footer";
        raw.typeId = "cbde3c3b-d03f-484b-8aa2-0c35227db10c";

        raw.classification = "ContentItem";
        raw.created = "2020-02-06T19:39:33.605Z";
        raw.creatorId = "30834b6f-0a47-4ef1-ac68-61ee5a2b7fbc";
        raw.description = "testDescription";
        raw.id = "r=126be044-ddaf-4c9c-bb07-156b1c97c592&a=6132847c-ecc6-41b7-83e0-20c29ace3d17";
        raw.keywords = testKeywords;
        raw.lastModified = "2020-02-06T19:39:58.416Z";
        raw.lastModifierId = "30834b6f-0a47-4ef1-ac68-61ee5a2b7fbc";
        raw.locale = "en:US";
        raw.name = "Canada Gallery Image";
        raw.restricted = true;
        raw.tags = testTags;

        return raw;
    }

    private JSONObject createDocument() throws JSONException {
        String obj = "{\n" +
                "    \"rev\" : \"1-2cdd9cf0f273c8a32e14046043071638\",\n" +
                "    \"thumbnail\" : {\n" +
                "      \"id\" : \"9c6440b5-512c-4cfc-9941-b1ad759f77f5\",\n" +
                "      \"url\" : \"/api/ae6a1610-fd30-4b81-8871-0f7f11f95426/delivery/v1/resources/b05c661d5e891602477ea9714733a975\"\n" +
                "    },\n" +
                "    \"keywords\" : [ ],\n" +
                "    \"kind\" : [ ],\n" +
                "    \"created\" : \"2020-01-07T17:52:31.610Z\",\n" +
                "    \"creatorId\" : \"00000000-0000-0000-0000-000000000009\",\n" +
                "    \"description\" : \"\",\n" +
                "    \"classification\" : \"content\",\n" +
                "    \"type\" : \"Event\",\n" +
                "    \"locale\" : \"en\",\n" +
                "    \"tags\" : [ \"oslo\", \"IBM sample\" ],\n" +
                "    \"selectedLayouts\" : [ {\n" +
                "      \"layout\" : {\n" +
                "        \"id\" : \"event-layout\"\n" +
                "      }\n" +
                "    } ],\n" +
                "    \"elements\" : {\n" +
                "      \"heading\" : {\n" +
                "        \"elementType\" : \"text\",\n" +
                "        \"value\" : \"Lecture: Decorating for twins\"\n" +
                "      },\n" +
                "      \"body\" : {\n" +
                "        \"elementType\" : \"formattedtext\",\n" +
                "        \"value\" : \"<p>Lorem ipsum dolor sit amet, cu dictas antiopam pri, mei augue iuvaret ea, eu vis sonet postulant. Viderer liberavisse qui at. Ad mel vitae recusabo, ius docendi tibique apeirian no, ea latine oporteat aliquando per. Etiam viris intellegam eum id, ad dico fabulas posidonium cum. Mea mutat legimus vulputate ut. Ignota consetetur necessitatibus te per, in nonumy definitionem sea.</p>\\n\"\n" +
                "      },\n" +
                "      \"eventDetails\" : {\n" +
                "        \"elementType\" : \"formattedtext\",\n" +
                "        \"value\" : \"<p>Voluptate tempora, consequuntur, et quisquam. Occaecat tempora but sequi nemo for magni. Nisi aperiam odit veritatis for duis or culpa. Qui architecto dolorem yet inventore. Suscipit vitae for corporis. Officia quam, or proident dolore yet.</p>\\n\"\n" +
                "      },\n" +
                "      \"date\" : {\n" +
                "        \"elementType\" : \"datetime\",\n" +
                "        \"value\" : \"2029-09-21T04:00:00Z\"\n" +
                "      },\n" +
                "      \"eventLocation\" : {\n" +
                "        \"elementType\" : \"formattedtext\",\n" +
                "        \"value\" : \"<p>555 Main St.<br />\\nAnywhere, USA</p>\\n\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"name\" : \"Ideas for Twins\",\n" +
                "    \"lastModifierId\" : \"00000000-0000-0000-0000-000000000009\",\n" +
                "    \"typeId\" : \"10ed9f3f-ab41-45a9-ba24-d988974affa7\",\n" +
                "    \"links\" : {\n" +
                "      \"thumbnail\" : {\n" +
                "        \"href\" : \"/authoring/v1/resources/b05c661d5e891602477ea9714733a975\"\n" +
                "      },\n" +
                "      \"createDraft\" : {\n" +
                "        \"href\" : \"/authoring/v1/content/b718161b-4219-4dc3-90d4-a663594b41e4/create-draft\"\n" +
                "      },\n" +
                "      \"retire\" : {\n" +
                "        \"href\" : \"/authoring/v1/changes/content/b718161b-4219-4dc3-90d4-a663594b41e4/status/retire\"\n" +
                "      },\n" +
                "      \"self\" : {\n" +
                "        \"href\" : \"/authoring/v1/content/b718161b-4219-4dc3-90d4-a663594b41e4\"\n" +
                "      },\n" +
                "      \"type\" : {\n" +
                "        \"href\" : \"/authoring/v1/types/10ed9f3f-ab41-45a9-ba24-d988974affa7\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"id\" : \"b718161b-4219-4dc3-90d4-a663594b41e4\",\n" +
                "    \"lastModified\" : \"2020-01-07T17:52:31.610Z\",\n" +
                "    \"systemModified\" : \"2020-01-07T17:52:31.873Z\",\n" +
                "    \"status\" : \"ready\"\n" +
                "  }";
        return new JSONObject(obj);
    }

}