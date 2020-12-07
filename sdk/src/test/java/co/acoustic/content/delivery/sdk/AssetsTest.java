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

import net.bytebuddy.build.ToStringPlugin;

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
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AssetsTest {

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

        Assets documents = new Assets(deliverySearch, null);
        assertNotNull(documents);
    }

    @Test
    public void testCreationWithState() {
        final DeliverySearch deliverySearch = mock(DeliverySearch.class);
        final Documents.State mockState = mock(Documents.State.class);

        Assets documents = new Assets(deliverySearch, mockState);

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

        Assets documents = new Assets(deliverySearch, null, queryBuilder);

        assertNotNull(documents);
    }

    @Test
    public void testCreationWithInjectedQueryBuilderAndState() {
        final DeliverySearch deliverySearch = mock(DeliverySearch.class);
        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        final Documents.State mockState = mock(Documents.State.class);

        Assets documents = new Assets(deliverySearch, mockState, queryBuilder);
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

        Assets documents = new Assets(deliverySearch, null, queryBuilder);
        assertNotNull(documents.createInstance(mock(DeliverySearch.class), mock(DeliverySearchQueryBuilder.class)));
    }

    @Test
    public void testCreateDocument() throws JSONException {
        final DeliverySearch deliverySearch = mock(DeliverySearch.class);
        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);

        Assets documents = new Assets(deliverySearch, null, queryBuilder);
        assertNotNull(documents.createDocument(createFullRawDoc()));
    }

    @Test
    public void testGetTargetDocumentClassification() {
        final DeliverySearch deliverySearch = mock(DeliverySearch.class);
        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);

        Assets documents = new Assets(deliverySearch, null, queryBuilder);
        assertEquals("asset", documents.getTargetDocumentClassification());
    }

    @Test
    public void testSortBy() {
        final String field = "category";
        final boolean asc = false;

        final DeliverySearch deliverySearch = mock(DeliverySearch.class);
        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);

        Assets documents = new Assets(deliverySearch, null, queryBuilder);
        documents.sortBy(field, asc);
        verify(queryBuilder).sort(field, asc);
    }

    @Test(expected = NullPointerException.class)
    public void testSortWithNullField() {
        final String field = null;
        final boolean asc = false;

        final DeliverySearch deliverySearch = mock(DeliverySearch.class);
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();

        Assets documents = new Assets(deliverySearch, null, queryBuilder);
        documents.sortBy(field, asc);
    }

    @Test
    public void testFilterBy() {
        final String field = "name";
        final String value = "n*";

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);

        Assets documents = new Assets(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterBy(field, value);
        verify(queryBuilder).filterQuery(field, value);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterByWithNullField() {
        final String field = null;
        final String value = "n*";

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();

        Assets documents = new Assets(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterBy(field, value);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterByWithNullValue() {
        final String field = "name";
        final String value = null;

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();

        Assets documents = new Assets(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterBy(field, value);
    }

    @Test
    public void testFilterQuery() {
        final String query = "popularity : [10 TO *]";

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        Assets documents = new Assets(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterQuery(query);
        verify(queryBuilder).filterQuery(query);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterQueryWithNullQuery() {
        final String query = null;

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();

        Assets documents = new Assets(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterQuery(query);
    }

    @Test
    public void testSearchByText() {
        final String text = "classification:asset";

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        Assets documents = new Assets(mock(DeliverySearch.class), null, queryBuilder);
        documents.searchByText(text);
        verify(queryBuilder).query(text);
    }

    @Test(expected = NullPointerException.class)
    public void testSearchByTextWithNull() {
        final String text = null;

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();

        Assets documents = new Assets(mock(DeliverySearch.class), null, queryBuilder);
        documents.searchByText(text);
    }

    @Test
    public void testRows() {
        final int rows = 15;

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        Assets documents = new Assets(mock(DeliverySearch.class), null, queryBuilder);
        documents.rows(rows);
        verify(queryBuilder).rows(rows);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRowsLessThen1() {
        final int rows = 0;

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        Assets documents = new Assets(mock(DeliverySearch.class), null, queryBuilder);
        documents.rows(rows);
    }

    @Test
    public void testStart() {
        final int start = 15;

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        Assets documents = new Assets(mock(DeliverySearch.class), null, queryBuilder);
        documents.start(start);
        verify(queryBuilder).start(start);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartLessThen0() {
        final int start = -1;

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        Assets documents = new Assets(mock(DeliverySearch.class), null, queryBuilder);
        documents.rows(start);
    }

    @Test
    public void TestFilterByName() {
        final String value = "footerConfig";

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        Assets documents = new Assets(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterByName(value);
        verify(queryBuilder).filterQuery("name", value);
    }

    @Test
    public void TestFilterById() {
        final String id = "ae72d304-ad18-4bf3-b213-4a79c829e458";

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        Assets documents = new Assets(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterById(id);
        verify(queryBuilder).filterQuery("id", id);
    }

    @Test
    public void TestFilterByCategories() {
        final String value = "\"Dynamic list criteria/Type/Design page\", \"List ordering options/By date descending\"";

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        Assets documents = new Assets(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterByCategories(value);
        verify(queryBuilder).filterQuery("categories", "("+value+")");
    }

    @Test (expected = NullPointerException.class)
    public void TestFilterByCategoriesWithNull() {
        final String categories = null;

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        Assets documents = new Assets(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterByCategories(categories);
    }

    @Test (expected = IllegalArgumentException.class)
    public void TestFilterByCategoriesWithEmpty() {
        final String categories = "";

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        Assets documents = new Assets(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterByCategories(categories);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestFilterByTagsWithNull() {
        final String[] value = new String[] { "website template", null };

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        Assets documents = new Assets(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterByTags(value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestFilterByTagsWithEmpty() {
        final String[] value = new String[] { "website template", "" };

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        Assets documents = new Assets(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterByTags(value);
    }

    @Test
    public void TestFilterByTags() {
        final String[] value = new String[] { "website template", "IBM sample" };

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        Assets documents = new Assets(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterByTags(value);
        verify(queryBuilder).filterQuery("tags", "(website template OR IBM sample)");
    }

    @Test
    public void TestFilterByTagsWithMoreThen2Tags() {
        final String[] value = new String[] { "website template", "IBM sample", "Tag1", "Tag4" };

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        Assets documents = new Assets(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterByTags(value);
        verify(queryBuilder).filterQuery("tags", "(website template OR IBM sample OR Tag1 OR Tag4)");
    }

    @Test
    public void TestIncludeDraftTrue() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        Assets documents = new Assets(mock(DeliverySearch.class), null, queryBuilder);
        documents.includeDraft(true);

        Documents.State state = documents.getState();
        assertTrue(state.isIncludeDraft());
    }

    @Test
    public void TestIncludeDraftFalse() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        Assets documents = new Assets(mock(DeliverySearch.class), null, queryBuilder);
        documents.includeDraft(false);

        Documents.State state = documents.getState();
        assertFalse(state.isIncludeDraft());
    }

    @Test
    public void TestIncludeProtectedContentTrue() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        Assets documents = new Assets(mock(DeliverySearch.class), null, queryBuilder);
        documents.setIncludeProtectedContent(true);

        Documents.State state = documents.getState();
        assertTrue(state.isIncludeProtectedContent());
    }

    @Test
    public void TestIncludeProtectedContentFalse() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        Assets documents = new Assets(mock(DeliverySearch.class), null, queryBuilder);
        documents.setIncludeProtectedContent(false);

        Documents.State state = documents.getState();
        assertFalse(state.isIncludeProtectedContent());
    }

    @Test
    public void TestRetrieveCompleteContentContextTrue() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        Assets documents = new Assets(mock(DeliverySearch.class), null, queryBuilder);
        documents.setRetrieveCompleteContentContext(true);

        Documents.State state = documents.getState();
        assertTrue(state.isRetrieveCompleteContentContext());
    }

    @Test
    public void TestRetrieveCompleteContentContextFalse() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        Assets documents = new Assets(mock(DeliverySearch.class), null, queryBuilder);
        documents.setRetrieveCompleteContentContext(false);

        Documents.State state = documents.getState();
        assertFalse(state.isRetrieveCompleteContentContext());
    }

    @Test
    public void TestIncludeAllFieldsTrue() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        Assets documents = new Assets(mock(DeliverySearch.class), null, queryBuilder);
        documents.includeAllFields(true);

        Documents.State state = documents.getState();
        assertTrue(state.isIncludeAllFields());
    }

    @Test
    public void TestIncludeAllFieldsFalse() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        Assets documents = new Assets(mock(DeliverySearch.class), null, queryBuilder);
        documents.includeAllFields(false);

        Documents.State state = documents.getState();
        assertFalse(state.isIncludeAllFields());
    }

    @Test
    public void TestIncludeRetiredTrue() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        Assets documents = new Assets(mock(DeliverySearch.class), null, queryBuilder);
        documents.includeRetired(true);

        Documents.State state = documents.getState();
        assertTrue(state.isIncludeRetired());
    }

    @Test
    public void TestIncludeRetiredFalse() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        Assets documents = new Assets(mock(DeliverySearch.class), null, queryBuilder);
        documents.includeRetired(false);

        Documents.State state = documents.getState();
        assertFalse(state.isIncludeRetired());
    }

//TEST CALLS



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
        new Assets(deliverySearch, null, queryBuilder, new NetworkingCallsExecutorImpl())
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
                .setBody(ResourceReader.read("assets_default_success_response.json"));

        mockWebServer.enqueue(successfulAssetsResponse);

        final SDKConfig sdkConfig = SDKConfig.builder().setApiUrl(mockWebServer.url("/").url().toString()).build();

        final ContentDeliverySDK sdk = new ContentDeliverySDK(sdkConfig, new DataEncoder());


        final DeliverySearch deliverySearch = sdk.deliverySearch();
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        final Assets documents = new Assets(deliverySearch, null, queryBuilder, new BlockingNetworkingCallsExecutor());

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
        final Assets documents = new Assets(deliverySearch, null, queryBuilder, new BlockingNetworkingCallsExecutor());

        documents
                .get()
                .then(result -> fail("Code shouldn't came to this point"))
                .error(error -> assertNotNull("Get result should not be null", error));
    }

    private DeliverySearchResponseDocument createFullRawDoc() throws JSONException {
        final ArrayList<String> testKeywords = new ArrayList<>();
        testKeywords.add("keyword1");
        testKeywords.add("keyword2");
        testKeywords.add("keyword3");
        testKeywords.add("keyword4");

        final ArrayList<String> testTags = new ArrayList<>();
        testTags.add("travel site sample");
        testTags.add("Sights");

        final ArrayList<String> categories = new ArrayList<>();
        categories.add("Locations/North America/Canada");
        categories.add("Locations/North America/US");

        final ArrayList<String> categoryLeaves = new ArrayList<>();
        categoryLeaves.add("Canada");
        categoryLeaves.add("Locations");


        final DeliverySearchResponseDocument raw = new DeliverySearchResponseDocument();
        raw.assetType = "image";
        raw.categories = categories;
        raw.categoryLeaves = categoryLeaves;
        raw.document = createDocument();
        raw.fileSize = 1130797L;
        raw.isManaged = true;
        raw.location = "/dxdam/3a/3a71b9cf-f6da-4b66-85f7-421c5a693d1e";
        raw.locationPaths = "/dxdam/3a/3a71b9cf-f6da-4b66-85f7-421c5a693d1e";
        raw.media = "/delivery/v1/resources/53b94276-9619-4f7a-bce8-7d663117bd51.png";
        raw.mediaType = "image/png";
        raw.path = "/dxdam/6f/6f38a240-7fb7-4be2-9304-5198e4350ee1/image.png";
        raw.resource = "126be044-ddaf-4c9c-bb07-156b1c97c592";
        raw.status = "complete";
        raw.thumbnail = "/delivery/v1/resources/126be044-ddaf-4c9c-bb07-156b1c97c592?fit=inside%7C220:145";
        raw.url = "/api/ae6a1610-fd30-4b81-8871-0f7f11f95426/delivery/v1/resources/df1678a1-7ac4-4aab-a590-5e22411a8904?fit=inside%7C220:145";

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

    private JSONObject createDocument() throws JSONException {
        String obj = "{\n" +
                "    \"rev\" : \"1-0108c7db7e623d25495ec96ec2518c14\",\n" +
                "    \"thumbnail\" : {\n" +
                "      \"id\" : \"a7b2cf43-badd-4530-a3b3-0d834883b692\",\n" +
                "      \"url\" : \"/api/ae6a1610-fd30-4b81-8871-0f7f11f95426/delivery/v1/resources/df1678a1-7ac4-4aab-a590-5e22411a8904?fit=inside%7C220:145\"\n" +
                "    },\n" +
                "    \"keywords\" : [ ],\n" +
                "    \"kind\" : [ ],\n" +
                "    \"created\" : \"2020-02-06T19:39:58.416Z\",\n" +
                "    \"libraryId\" : \"b066778e-7f87-4584-893a-a3f7c0e30232\",\n" +
                "    \"creatorId\" : \"30834b6f-0a47-4ef1-ac68-61ee5a2b7fbc\",\n" +
                "    \"description\" : \"\",\n" +
                "    \"classification\" : \"content\",\n" +
                "    \"type\" : \"Travel Article\",\n" +
                "    \"locale\" : \"en\",\n" +
                "    \"tags\" : [ \"travel site sample\" ],\n" +
                "    \"elements\" : {\n" +
                "      \"countryOfTravelArticle\" : {\n" +
                "        \"categoryIds\" : [ \"77abce3f-7a13-45ef-b1ce-11dbd11b61ff\" ],\n" +
                "        \"categories\" : [ \"Locations/North America/Canada\" ],\n" +
                "        \"elementType\" : \"category\"\n" +
                "      },\n" +
                "      \"travelArticleTitle\" : {\n" +
                "        \"elementType\" : \"text\",\n" +
                "        \"value\" : \"The 'Must Do' Guide for Canada\"\n" +
                "      },\n" +
                "      \"travelArticleImage\" : {\n" +
                "        \"mode\" : \"shared\",\n" +
                "        \"profiles\" : [ \"59414bcf-8ccb-4809-9117-47b7a64864a0\" ],\n" +
                "        \"renditions\" : {\n" +
                "          \"card\" : {\n" +
                "            \"source\" : \"/delivery/v1/resources/df1678a1-7ac4-4aab-a590-5e22411a8904?resize=830px:250px&crop=250:250;529,0\",\n" +
                "            \"width\" : 250,\n" +
                "            \"height\" : 250,\n" +
                "            \"transform\" : {\n" +
                "              \"scale\" : 0.43252595155709345,\n" +
                "              \"crop\" : {\n" +
                "                \"x\" : 529,\n" +
                "                \"y\" : 0,\n" +
                "                \"width\" : 250,\n" +
                "                \"height\" : 250\n" +
                "              }\n" +
                "            },\n" +
                "            \"url\" : \"/ae6a1610-fd30-4b81-8871-0f7f11f95426/dxresources/df16/df1678a1-7ac4-4aab-a590-5e22411a8904.png?resize=830px%3A250px&crop=250%3A250%3B529%2C0\"\n" +
                "          },\n" +
                "          \"default\" : {\n" +
                "            \"width\" : 1920,\n" +
                "            \"source\" : \"/delivery/v1/resources/df1678a1-7ac4-4aab-a590-5e22411a8904\",\n" +
                "            \"height\" : 578,\n" +
                "            \"url\" : \"/ae6a1610-fd30-4b81-8871-0f7f11f95426/dxresources/df16/df1678a1-7ac4-4aab-a590-5e22411a8904.png\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"asset\" : {\n" +
                "          \"fileName\" : \"Canada-Lead-Image.png\",\n" +
                "          \"altText\" : \"The landscape of Canada with a river, forest, and snowy mountains.\",\n" +
                "          \"fileSize\" : 617185,\n" +
                "          \"width\" : 1920,\n" +
                "          \"mediaType\" : \"image/png\",\n" +
                "          \"id\" : \"a7b2cf43-badd-4530-a3b3-0d834883b692\",\n" +
                "          \"resourceUri\" : \"/delivery/v1/resources/df1678a1-7ac4-4aab-a590-5e22411a8904\",\n" +
                "          \"height\" : 578\n" +
                "        },\n" +
                "        \"elementType\" : \"image\",\n" +
                "        \"url\" : \"/ae6a1610-fd30-4b81-8871-0f7f11f95426/dxresources/df16/df1678a1-7ac4-4aab-a590-5e22411a8904.png\"\n" +
                "      },\n" +
                "      \"articleAuthor\" : {\n" +
                "        \"elementType\" : \"text\",\n" +
                "        \"value\" : \"John Smith\"\n" +
                "      },\n" +
                "      \"travelArticleText\" : {\n" +
                "        \"elementType\" : \"formattedtext\",\n" +
                "        \"value\" : \"<p><em>We hope you have enough time to spare. Because the most important thing to do in Canada is to see ALL of Canada. One of the largest countries in the world by the area that has 10% of the world's forest, the longest coastline, incredible mountains, and waterfalls - all this hints that in the first place you need to get acquainted with the nature of this beautiful country.</em></p> \\n<h2>Do canoeing</h2> \\n<div style=\\\"text-align:center;clear:both;padding:0px 0px 5px\\\">\\n <img data-wch-asset-id=\\\"8bb42682-08dd-4b1f-8cc0-0deef30bfa0c\\\" height=\\\"500\\\" src=\\\"/ae6a1610-fd30-4b81-8871-0f7f11f95426/dxresources/53b9/53b94276-9619-4f7a-bce8-7d663117bd51.png\\\" width=\\\"750\\\">\\n</div> \\n<p>One of the best ways to see Canada is by taking a trip on a canoe.</p> \\n<p>There are hundreds of canoe routes across Canada, where you can enjoy seeing picturesque lakes, rivers, mountains, and wildlife, but make sure you add the following routes to your list:</p> \\n<ul>\\n <li>Johnstone Strait in British Columbia, where among typical Canadian wildlife (eagles, deer, otters, and seals) you can also see orca whales that feed on salmon from mid-summer to early autumn;</li>\\n <li> <p>Quetico Provincial Park in Ontario. The park area of 460 thousand hectares includes pine and spruce forests and more than 2,000 lakes. It operates from the mid-May to mid-October;</p> </li>\\n <li>Rideau Canal - a UNESCO World Heritage Site that connects the capital of Canada, Ottawa, with Kingston, Ontario.</li>\\n</ul> \\n<p>&nbsp;</p> \\n<h2>Visit Niagara Falls</h2> \\n<p><img data-wch-asset-id=\\\"09a014ea-1018-4b8e-9245-e7a8a0b5b9f0\\\" height=\\\"250\\\" src=\\\"/ae6a1610-fd30-4b81-8871-0f7f11f95426/dxresources/e328/e328e3a0-2c9c-4cfc-8969-2513cbde15f4.png\\\" style=\\\"float:right;padding:5px 0px 0px 10px\\\" width=\\\"375\\\">Niagara Falls is one of the world’s most famous waterfalls and the most visited place in Canada.</p> \\n<p>The Falls is not just one wide waterfall, but 3 connected waterfalls:</p> \\n<ol>\\n <li><strong>American Falls</strong> - named so, since it flows mainly over the border of US;</li>\\n <li><strong>Bridal Veil Falls</strong>&nbsp;that resembles&nbsp;- <em>surprise</em> - a bridal veil;</li>\\n <li><strong>Horseshoe Falls</strong>, named because of its horseshoe shape, and it is also known as the Canadian Falls due to being mostly in Ontario.</li>\\n</ol> \\n<p>&nbsp;</p> \\n<h2>Go skiing in Whistler</h2> \\n<p><img data-wch-asset-id=\\\"6132847c-ecc6-41b7-83e0-20c29ace3d17\\\" height=\\\"250\\\" src=\\\"/ae6a1610-fd30-4b81-8871-0f7f11f95426/dxresources/126b/126be044-ddaf-4c9c-bb07-156b1c97c592.png\\\" style=\\\"float:left;padding:0px 10px 0px 0px\\\" width=\\\"375\\\"></p> \\n<p>Canadian ski resorts are located on both east and west coasts, but the west coast is a homeland&nbsp;to the most famous resorts like Banff, Lake Louise, and Whistler.</p> \\n<p>Whistler is a 'flagship' resort in the country.&nbsp;It is conveniently located in the two-hour drive from Vancouver and has the biggest ski area in North America.</p> \\n<p>The popularity of this place certainly has its drawbacks. In high season, it's getting too crowded, thus lift queues are not unusual,&nbsp;and too expensive, making you consider other resorts.</p>\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"name\" : \"Canada Travel Article\",\n" +
                "    \"lastModifierId\" : \"30834b6f-0a47-4ef1-ac68-61ee5a2b7fbc\",\n" +
                "    \"typeId\" : \"020bc107-4d2a-4513-8988-bc0cd455bb98\",\n" +
                "    \"links\" : {\n" +
                "      \"thumbnail\" : {\n" +
                "        \"href\" : \"/authoring/v1/resources/df1678a1-7ac4-4aab-a590-5e22411a8904?fit=inside%7C220:145\"\n" +
                "      },\n" +
                "      \"createDraft\" : {\n" +
                "        \"href\" : \"/authoring/v1/content/f1492227-943b-4f8e-81a6-79bc939e101b/create-draft\"\n" +
                "      },\n" +
                "      \"retire\" : {\n" +
                "        \"href\" : \"/authoring/v1/changes/content/f1492227-943b-4f8e-81a6-79bc939e101b/status/retire\"\n" +
                "      },\n" +
                "      \"self\" : {\n" +
                "        \"href\" : \"/authoring/v1/content/f1492227-943b-4f8e-81a6-79bc939e101b\"\n" +
                "      },\n" +
                "      \"type\" : {\n" +
                "        \"href\" : \"/authoring/v1/types/020bc107-4d2a-4513-8988-bc0cd455bb98\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"id\" : \"f1492227-943b-4f8e-81a6-79bc939e101b\",\n" +
                "    \"lastModified\" : \"2020-02-06T19:39:58.416Z\",\n" +
                "    \"systemModified\" : \"2020-02-06T19:39:58.580Z\",\n" +
                "    \"status\" : \"ready\"\n" +
                "  }";
        return new JSONObject(obj);
    }
}