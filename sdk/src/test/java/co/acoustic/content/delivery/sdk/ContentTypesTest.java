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

public class ContentTypesTest {
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

        ContentTypes documents = new ContentTypes(deliverySearch, null);
        assertNotNull(documents);
    }

    @Test
    public void testCreationWithState() {
        final DeliverySearch deliverySearch = mock(DeliverySearch.class);
        final Documents.State mockState = mock(Documents.State.class);

        ContentTypes documents = new ContentTypes(deliverySearch, mockState);

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

        ContentTypes documents = new ContentTypes(deliverySearch, null, queryBuilder);

        assertNotNull(documents);
    }

    @Test
    public void testCreationWithInjectedQueryBuilderAndState() {
        final DeliverySearch deliverySearch = mock(DeliverySearch.class);
        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        final Documents.State mockState = mock(Documents.State.class);

        ContentTypes documents = new ContentTypes(deliverySearch, mockState, queryBuilder);
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

        ContentTypes documents = new ContentTypes(deliverySearch, null, queryBuilder);
        assertNotNull(documents.createInstance(mock(DeliverySearch.class), mock(DeliverySearchQueryBuilder.class)));
    }

    @Test
    public void testCreateDocument() throws JSONException {
        final DeliverySearch deliverySearch = mock(DeliverySearch.class);
        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);

        ContentTypes documents = new ContentTypes(deliverySearch, null, queryBuilder);
        assertNotNull(documents.createDocument(createFullRawDoc()));
    }

    @Test
    public void testGetTargetDocumentClassification() {
        final DeliverySearch deliverySearch = mock(DeliverySearch.class);
        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);

        ContentTypes documents = new ContentTypes(deliverySearch, null, queryBuilder);
        assertEquals("content-type", documents.getTargetDocumentClassification());
    }

    @Test
    public void testSortBy() {
        final String field = "category";
        final boolean asc = false;

        final DeliverySearch deliverySearch = mock(DeliverySearch.class);
        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);

        ContentTypes documents = new ContentTypes(deliverySearch, null, queryBuilder);
        documents.sortBy(field, asc);
        verify(queryBuilder).sort(field, asc);
    }

    @Test(expected = NullPointerException.class)
    public void testSortWithNullField() {
        final String field = null;
        final boolean asc = false;

        final DeliverySearch deliverySearch = mock(DeliverySearch.class);
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();

        ContentTypes documents = new ContentTypes(deliverySearch, null, queryBuilder);
        documents.sortBy(field, asc);
    }

    @Test
    public void testFilterBy() {
        final String field = "name";
        final String value = "n*";

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);

        ContentTypes documents = new ContentTypes(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterBy(field, value);
        verify(queryBuilder).filterQuery(field, value);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterByWithNullField() {
        final String field = null;
        final String value = "n*";

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();

        ContentTypes documents = new ContentTypes(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterBy(field, value);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterByWithNullValue() {
        final String field = "name";
        final String value = null;

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();

        ContentTypes documents = new ContentTypes(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterBy(field, value);
    }

    @Test
    public void testFilterQuery() {
        final String query = "popularity : [10 TO *]";

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        ContentTypes documents = new ContentTypes(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterQuery(query);
        verify(queryBuilder).filterQuery(query);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterQueryWithNullQuery() {
        final String query = null;

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();

        ContentTypes documents = new ContentTypes(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterQuery(query);
    }

    @Test
    public void testSearchByText() {
        final String text = "classification:asset";

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        ContentTypes documents = new ContentTypes(mock(DeliverySearch.class), null, queryBuilder);
        documents.searchByText(text);
        verify(queryBuilder).query(text);
    }

    @Test(expected = NullPointerException.class)
    public void testSearchByTextWithNull() {
        final String text = null;

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();

        ContentTypes documents = new ContentTypes(mock(DeliverySearch.class), null, queryBuilder);
        documents.searchByText(text);
    }

    @Test
    public void testRows() {
        final int rows = 15;

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        ContentTypes documents = new ContentTypes(mock(DeliverySearch.class), null, queryBuilder);
        documents.rows(rows);
        verify(queryBuilder).rows(rows);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRowsLessThen1() {
        final int rows = 0;

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        ContentTypes documents = new ContentTypes(mock(DeliverySearch.class), null, queryBuilder);
        documents.rows(rows);
    }

    @Test
    public void testStart() {
        final int start = 15;

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        ContentTypes documents = new ContentTypes(mock(DeliverySearch.class), null, queryBuilder);
        documents.start(start);
        verify(queryBuilder).start(start);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartLessThen0() {
        final int start = -1;

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        ContentTypes documents = new ContentTypes(mock(DeliverySearch.class), null, queryBuilder);
        documents.rows(start);
    }

    @Test
    public void TestFilterByName() {
        final String value = "footerConfig";

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        ContentTypes documents = new ContentTypes(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterByName(value);
        verify(queryBuilder).filterQuery("name", value);
    }

    @Test
    public void TestFilterById() {
        final String id = "ae72d304-ad18-4bf3-b213-4a79c829e458";

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        ContentTypes documents = new ContentTypes(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterById(id);
        verify(queryBuilder).filterQuery("id", id);
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
        new ContentTypes(deliverySearch, null, queryBuilder, new NetworkingCallsExecutorImpl())
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
                                .add("x-ibm-dx-request-id", "8591ec630faccdc1311150ff379b5567")
                                .add("x-newrelic-app-data", "PxQGV1JTCwAIR1RWAAMDUVMIFB9AMQYAZBBZDEtZV0ZaClc9HjdWEBBOa04CBlRaRgETG2seQVc4HkVWAxQAChBKfydsERYeA0sJTQFPA1NXBgFRU1QBHx1VTUBVVwRWVQNQAAFeAFoEWw5XGhRSU18WXDw=")
                                .add("x-response-time", "46.031ms")
                                .add("x-xss-protection", "1; mode=block")
                                .build()
                )
                .setBody(ResourceReader.read("content_types_default_success_response.json"));

        mockWebServer.enqueue(successfulAssetsResponse);

        final SDKConfig sdkConfig = SDKConfig.builder().setApiUrl(mockWebServer.url("/").url().toString()).build();

        final ContentDeliverySDK sdk = new ContentDeliverySDK(sdkConfig, new DataEncoder());


        final DeliverySearch deliverySearch = sdk.deliverySearch();
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        final ContentTypes documents = new ContentTypes(deliverySearch, null, queryBuilder, new BlockingNetworkingCallsExecutor());

        documents
                .get()
                .then(result -> {
                    assertNotNull("Get result should not be null", result);
                })
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
        final ContentTypes documents = new ContentTypes(deliverySearch, null, queryBuilder, new BlockingNetworkingCallsExecutor());

        documents
                .get()
                .then(result -> fail("Code shouldn't came to this point"))
                .error(error -> assertNotNull("Get result should not be null", error));
    }
    
    private DeliverySearchResponseDocument createFullRawDoc() {
        final ArrayList<String> testKeywords = new ArrayList<>();
        testKeywords.add("keyword1");
        testKeywords.add("keyword2");
        testKeywords.add("keyword3");
        testKeywords.add("keyword4");

        final ArrayList<String> testTags = new ArrayList<>();
        testTags.add("travel site sample");
        testTags.add("Sights");

        final DeliverySearchResponseDocument raw = new DeliverySearchResponseDocument();
        raw.classification = "asset";
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

}