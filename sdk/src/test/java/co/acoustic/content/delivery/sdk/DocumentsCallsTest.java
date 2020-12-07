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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;

import okhttp3.Headers;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;
import retrofit2.Callback;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

public class DocumentsCallsTest extends BaseDocumentsTest {
    private static final String SUCCESSFUL_ASSETS_GET_RESPONSE_FILE_NAME = "assets_default_success_response.json";
    private static final String SUCCESSFUL_GET_WITHOUT_CLASSIFICATION_RESPONSE_FILE_NAME = "assets_without_classification_success_response.json";
    private static final String ACCESS_CONTROL_ERROR_RESPONSE_FILE_NAME = "access_controll_error_response.json";
    private static final String ACCESS_CONTROL_ERROR_WITHOUT_MESSAGE_RESPONSE_FILE_NAME = "access_controll_error_response.json";



    private MockWebServer mockWebServer = new MockWebServer();

    @Before
    public void setUp() throws Exception {
        mockWebServer.start();
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
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
        new DocumentsTestImpl(deliverySearch, null, queryBuilder, new NetworkingCallsExecutorImpl())
                .get();
    }

    @Test
    public void testGetPassCorrectValuesToQueryBuilderAndCallsProvider() {
        /*
         * Test verifies that get method passes appropriate data to QueryBuilder and CallsProvider.
         */
        class NetworkingCallsExecutorImpl implements NetworkingCallsExecutor {

            @Override
            public <ResponseType> void executeCall(Call<ResponseType> call, Callback<ResponseType> callback) {
                //Mocking long call, to allow  cancel previous call.
            }
        }

        Call mockCall = mock(Call.class);

        NetworkingCallsProvider mockCallsProvider = mock(NetworkingCallsProvider.class);
        when(mockCallsProvider.getDeliverySearchCall(any(DeliverySearchQuery.class), anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean())).thenReturn(mockCall);

        ContentDeliverySDK mockSDK = mock(ContentDeliverySDK.class);
        when(mockSDK.getNetworkingCallsProvider()).thenReturn(mockCallsProvider);

        DeliverySearch mockDeliverySearch = new DeliverySearch(mockSDK);

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        when(queryBuilder.setIncludeAllFields(anyBoolean())).thenReturn(queryBuilder);
        when(queryBuilder.setIncludeDraft(anyBoolean())).thenReturn(queryBuilder);
        when(queryBuilder.setIncludeRetired(anyBoolean())).thenReturn(queryBuilder);
        when(queryBuilder.build()).thenReturn(DeliverySearchQuery.builder().build());

        DocumentsTestImpl documents = new DocumentsTestImpl(mockDeliverySearch, null, queryBuilder, new NetworkingCallsExecutorImpl());
        documents.setIncludeAllFields(true);
        documents.setIncludeDraft(true);
        documents.setIncludeRetired(true);
        documents.setIncludeProtectedContent(true);
        documents.setRetrieveCompleteContentContext(true);

        documents.get();
        verify(queryBuilder).setIncludeAllFields(true);
        verify(queryBuilder).setIncludeDraft(true);
        verify(queryBuilder).setIncludeRetired(true);
        verify(queryBuilder).build();

        verify(mockCallsProvider).getDeliverySearchCall(any(DeliverySearchQuery.class), eq(true), eq(true), eq(true), eq(true));
    }

    @Test
    public void testGetClearingOldCall() {
        /*
         * Test verifies that if make new call before the previous one wasn't finished, the previous will be canceled.
         */
        class NetworkingCallsExecutorImpl implements NetworkingCallsExecutor {

            @Override
            public <ResponseType> void executeCall(Call<ResponseType> call, Callback<ResponseType> callback) {
                //Mocking long call, to allow  cancel previous call.
            }
        }

        Call mockCall = mock(Call.class);

        NetworkingCallsProvider mockCallsProvider = mock(NetworkingCallsProvider.class);
        when(mockCallsProvider.getDeliverySearchCall(any(DeliverySearchQuery.class), anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean())).thenReturn(mockCall);

        ContentDeliverySDK mockSDK = mock(ContentDeliverySDK.class);
        when(mockSDK.getNetworkingCallsProvider()).thenReturn(mockCallsProvider);

        DeliverySearch mockDeliverySearch = new DeliverySearch(mockSDK);
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();

        DocumentsTestImpl documents = new DocumentsTestImpl(mockDeliverySearch, null, queryBuilder, new NetworkingCallsExecutorImpl());

        documents.get();
        documents.get();
        verify(mockCall).cancel();
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
                .setBody(ResourceReader.read(SUCCESSFUL_ASSETS_GET_RESPONSE_FILE_NAME));

        mockWebServer.enqueue(successfulAssetsResponse);

        final SDKConfig sdkConfig = SDKConfig.builder().setApiUrl(mockWebServer.url("/").url().toString()).build();

        final ContentDeliverySDK sdk = new ContentDeliverySDK(sdkConfig, new DataEncoder());


        final DeliverySearch deliverySearch = sdk.deliverySearch();
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        final DocumentsTestImpl documents = new DocumentsTestImpl(deliverySearch, null, queryBuilder, new BlockingNetworkingCallsExecutor());

        documents
                .get()
                .then(result -> {
                    assertNotNull("Get result should not be null", result);
                })
                .error(error -> fail("Code shouldn't came to this point"));
    }

    @Test
    public void tesParseDocumentWithoutClassification() {
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
                .setBody(ResourceReader.read(SUCCESSFUL_GET_WITHOUT_CLASSIFICATION_RESPONSE_FILE_NAME));

        mockWebServer.enqueue(successfulAssetsResponse);

        final SDKConfig sdkConfig = SDKConfig.builder().setApiUrl(mockWebServer.url("/").url().toString()).build();

        final ContentDeliverySDK sdk = new ContentDeliverySDK(sdkConfig, new DataEncoder());


        final DeliverySearch deliverySearch = sdk.deliverySearch();
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        final DocumentsTestImpl documents = new DocumentsTestImpl(deliverySearch, null, queryBuilder, new BlockingNetworkingCallsExecutor());

        documents
                .get()
                .then(result -> {
                    assertNotNull("Get result should not be null", result);
                })
                .error(error -> fail("Code shouldn't came to this point"));
    }


    @Test
    public void testReceiveCallException() {
        final String testErrorMessage = "Test Message";
        class NetworkingCallsExecutorImpl implements NetworkingCallsExecutor {

            @Override
            public <ResponseType> void executeCall(Call<ResponseType> call, Callback<ResponseType> callback) {
                //Mocking exception behavior
                callback.onFailure(call, new SocketTimeoutException(testErrorMessage));
            }
        }

        final DeliverySearch deliverySearch = ContentDeliverySDK
                .create(SDKConfig.builder().setApiUrl("https://my7.test.com/").build())
                .deliverySearch();
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        new DocumentsTestImpl(deliverySearch, null, queryBuilder, new NetworkingCallsExecutorImpl())
                .get()
                .then(result -> fail("Code shouldn't came to this point"))
                .error(error -> {
                    assertNotNull("Get result should not be null", error);
                });
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
                .setBody(ResourceReader.read(ACCESS_CONTROL_ERROR_RESPONSE_FILE_NAME));

        mockWebServer.enqueue(successfulAssetsResponse);

        final SDKConfig sdkConfig = SDKConfig.builder().setApiUrl(mockWebServer.url("/").url().toString()).build();

        final ContentDeliverySDK sdk = new ContentDeliverySDK(sdkConfig, new DataEncoder());


        final DeliverySearch deliverySearch = sdk.deliverySearch();
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        final DocumentsTestImpl documents = new DocumentsTestImpl(deliverySearch, null, queryBuilder, new BlockingNetworkingCallsExecutor());

        documents
                .get()
                .then(result -> fail("Code shouldn't came to this point"))
                .error(error -> assertNotNull("Get result should not be null", error));
    }

    @Test
    public void testParseErrorWithoutMessage() {
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
                .setBody(ResourceReader.read(ACCESS_CONTROL_ERROR_WITHOUT_MESSAGE_RESPONSE_FILE_NAME));

        mockWebServer.enqueue(successfulAssetsResponse);

        final SDKConfig sdkConfig = SDKConfig.builder().setApiUrl(mockWebServer.url("/").url().toString()).build();

        final ContentDeliverySDK sdk = new ContentDeliverySDK(sdkConfig, new DataEncoder());


        final DeliverySearch deliverySearch = sdk.deliverySearch();
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        final DocumentsTestImpl documents = new DocumentsTestImpl(deliverySearch, null, queryBuilder, new BlockingNetworkingCallsExecutor());

        documents
                .get()
                .then(result -> fail("Code shouldn't came to this point"))
                .error(error -> assertNotNull("Get result should not be null", error));
    }

    @Test
    public void testErrorParseException() {
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
                .setBody(ResourceReader.read(ACCESS_CONTROL_ERROR_RESPONSE_FILE_NAME));

        mockWebServer.enqueue(successfulAssetsResponse);

        final SDKConfig sdkConfig = SDKConfig.builder().setApiUrl(mockWebServer.url("/").url().toString()).build();

        final ContentDeliverySDK tempSdk = new ContentDeliverySDK(sdkConfig, new DataEncoder());
        final NetworkingCallsProvider spyCallsProvider = spy(tempSdk.getNetworkingCallsProvider());
        when(spyCallsProvider.getErrorResponseConverter()).thenReturn(value -> {
            throw new IllegalStateException("Fake Exception");
        });

        final ContentDeliverySDK sdk = spy(new ContentDeliverySDK(sdkConfig, new DataEncoder()));
        when(sdk.getNetworkingCallsProvider()).thenReturn(spyCallsProvider);


        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        final DocumentsTestImpl documents = new DocumentsTestImpl(new DeliverySearch(sdk), null, queryBuilder, new BlockingNetworkingCallsExecutor());

        documents
                .get()
                .then(result -> fail("Code shouldn't came to this point"))
                .error(error -> assertTrue("In this case error should be instance of RuntimeException", error instanceof RuntimeException));
    }

}
