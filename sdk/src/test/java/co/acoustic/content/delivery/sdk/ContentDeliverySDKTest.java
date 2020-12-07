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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.HttpURLConnection;

import okhttp3.Headers;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ContentDeliverySDKTest {

    private static final String SUCCESSFUL_LOGIN_MOCK_RESPONSE_FILE_NAME = "successful_login_mock_response.json";
    private static final String FAILED_LOGIN_MOCK_RESPONSE_FILE_NAME = "failed_to_login_wrong_credentials_error_mock_response.json";

    private static MockWebServer mockWebServer = new MockWebServer();

    @BeforeClass
    public static void setUp() throws Exception {
        mockWebServer.start();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test(expected = NullPointerException.class)
    public void testCreateWithNullConfig() {
        ContentDeliverySDK.create(null);
    }

    @Test()
    public void testCreateWithNotNullConfig() {
        final ContentDeliverySDK sdk = ContentDeliverySDK.create(SDKConfig.builder().setApiUrl("http://test.blah.com/").build());
        assertNotNull("SDK create method should never return null", sdk);
    }

    @Test
    public void testDeliverySearchCreation() {
        final ContentDeliverySDK sdk = ContentDeliverySDK.create(SDKConfig.builder().setApiUrl("http://test.blah.com/").build());
        final DeliverySearch deliverySearch = sdk.deliverySearch();
        assertNotNull("DeliverySearch instance should never be null", deliverySearch);
    }

    @Test
    public void testGetNetworkingCallsProvider() {
        final ContentDeliverySDK sdk = ContentDeliverySDK.create(SDKConfig.builder().setApiUrl("http://test.blah.com/").build());
        final NetworkingCallsProvider networkingCallsProvider = sdk.getNetworkingCallsProvider();
        assertNotNull("NetworkingCallsProvider should never be null", networkingCallsProvider);
    }

    @Test
    public void testGetCookieManager() {
        final ContentDeliverySDK sdk = ContentDeliverySDK.create(SDKConfig.builder().setApiUrl("http://test.blah.com/").build());
        RetrofitFactory.CookieManager cookieManager = sdk.getCookieManager();
        assertNotNull("CookieManager should never be null", cookieManager);
    }

    @Test
    public void testLogin() {
        final SDKConfig sdkConfig = SDKConfig.builder().setApiUrl(mockWebServer.url("/").url().toString()).build();

        final String userName = "user123";
        final String password = "password123";

        final DataEncoder dataEncoder = mock(DataEncoder.class);
        when(dataEncoder.encodeToString(any(byte[].class))).thenReturn("asdfkahlsdfklj234234");

        final ContentDeliverySDK sdk = new ContentDeliverySDK(sdkConfig, dataEncoder, new BlockingNetworkingCallsExecutor());
        final SdkCookieManager cookieManager = sdk.getCookieManager();

        assertTrue("CookieManager should be empty before login", cookieManager.isEmpty());
        assertFalse("isLoggedIn should return false before login", sdk.isLoggedIn());
        assertNull("getCurrentUserName should return null before login", sdk.getCurrentUserName());

        mockWebServer.enqueue(createSuccessfulLoginMockResponse());

        sdk.login(userName, password, error -> {
            assertNull("Error should be null in case of successful login", error);
        });

        assertTrue("isLoggedIn should return true after successful login", sdk.isLoggedIn());
        assertEquals("getCurrentUserName should return logged in user name after successful login", userName, sdk.getCurrentUserName());
        assertTrue("Cookie manager should have 'x-ibm-dx-tenant-id' cookie", cookieManager.hasCookie("x-ibm-dx-tenant-id"));
        assertTrue("Cookie manager should have 'x-ibm-dx-user-auth' cookie", cookieManager.hasCookie("x-ibm-dx-user-auth"));

        sdk.login(userName, password, error -> {
            assertNull("Error should be null in case of successful login", error);
        });

        assertTrue("isLoggedIn should return true after successful login", sdk.isLoggedIn());
        assertEquals("getCurrentUserName should return logged in user name after successful login", userName, sdk.getCurrentUserName());
        assertTrue("Cookie manager should have 'x-ibm-dx-tenant-id' cookie", cookieManager.hasCookie("x-ibm-dx-tenant-id"));
        assertTrue("Cookie manager should have 'x-ibm-dx-user-auth' cookie", cookieManager.hasCookie("x-ibm-dx-user-auth"));

        sdk.logout();

        assertTrue("CookieManager should be empty after logout", cookieManager.isEmpty());
        assertFalse("isLoggedIn should return false after logout", sdk.isLoggedIn());
        assertNull("getCurrentUserName should return null after logout", sdk.getCurrentUserName());

        mockWebServer.enqueue(createFailedToLoginMockResponse());

        sdk.login(userName, password, error -> {
            assertNotNull("Error should not be null in case of failed login", error);
            assertEquals("For failed login RuntimeException is expected", error.getClass(), RuntimeException.class);
        });

        assertTrue("CookieManager should be empty after failed login", cookieManager.isEmpty());
        assertFalse("isLoggedIn should return false after failed login", sdk.isLoggedIn());
        assertNull("getCurrentUserName should return null after failed login", sdk.getCurrentUserName());

        final MockResponse successfulLoginEmptyBodyResponse = createSuccessfulLoginMockResponse();
        successfulLoginEmptyBodyResponse.setBody("");
        mockWebServer.enqueue(successfulLoginEmptyBodyResponse);

        sdk.login(userName, password, error -> assertNotNull("Error should not be null in case of failed login", error));

        assertTrue("CookieManager should be empty after failed login", cookieManager.isEmpty());
        assertFalse("isLoggedIn should return false after failed login", sdk.isLoggedIn());
        assertNull("getCurrentUserName should return null after failed login", sdk.getCurrentUserName());

        final MockResponse failedLoginEmptyBodyResponse = createFailedToLoginMockResponse();
        failedLoginEmptyBodyResponse.setBody("");
        mockWebServer.enqueue(failedLoginEmptyBodyResponse);

        sdk.login(userName, password, error -> {
            assertNotNull("Error should not be null in case of failed login", error);
            assertEquals("For empty response body for failed login response RuntimeException is expected", error.getClass(), RuntimeException.class);
        });

        assertTrue("CookieManager should be empty after failed login", cookieManager.isEmpty());
        assertFalse("isLoggedIn should return false after failed login", sdk.isLoggedIn());
        assertNull("getCurrentUserName should return null after failed login", sdk.getCurrentUserName());
    }

    private MockResponse createSuccessfulLoginMockResponse() {
        return new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setHeaders(
                        new Headers.Builder()
                                .add("x-ibm-dx-request-id", "6807c3c68fe2de2e2d3ef2cd70073bcc")
                                .add("content-type", "application/json; charset=UTF-8")
                                .add("x-ibm-dx-tenant-id", "ae6a1610-fd30-4b81-8871-0f7f11f95426")
                                .add("x-ibm-dx-tenant-base-url", "https://my7.content-cms.com/api/")
                                .add("x-newrelic-app-data", "PxQGV1JTCwAIR1RWAAMDU1AJFB9AMQYAZBBZDEtZV0ZaClc9HjZWADdTRRcKBkxjURAJPhgbFUpUHwYfU0hSWwRWC1MLFBkDH0cBWABTAQNQA1ICUlcICwAFQ05RUFsVAWw=")
                                .add("content-language", "en-US")
                                .add("vary", "authorization, origin")
                                .add("content-security-policy", "frame-ancestors 'self' https://www.digitalexperience.ibm.com https://www.customer-engagement.ibm.com https://commerceinsights.ibmcloud.com; report-uri https://www.digitalexperience.ibm.com/api/csp/report")
                                .add("x-response-time", "1226.281ms")
                                .add("content-length", "117")
                                .add("cache-control", "private, no-cache")
                                .add("expires", "Mon, 16 Mar 2020 20:42:33 GMT")
                                .add("set-cookie", "x-ibm-dx-tenant-id=ae6a1610-fd30-4b81-8871-0f7f11f95426; Path=/api; Secure; HttpOnly")
                                .add("set-cookie", "x-ibm-dx-user-auth=eyJraWQiOiJZS2UzV2JBVDBxY1M1aGU1SmNVViIsInR5cCI6IkpXVCIsImFsZyI6IlJTMjU2In0.eyJ0b2tlbl90eXBlIjoiQmVhcmVyIiwic3ViIjoidnNsdXBrb0Bzb2Z0c2VydmVpbmMuY29tIiwiZXh0ZW5kU2Vzc2lvbiI6ZmFsc2UsImFwaVVybCI6Imh0dHBzOi8vbXk3LmNvbnRlbnQtY21zLmNvbS9hcGkiLCJhdXRob3JpbmdVcmwiOiJodHRwczovL3d3dy5kaWdpdGFsZXhwZXJpZW5jZS5pYm0uY29tIiwidGVuYW50SWQiOiJhZTZhMTYxMC1mZDMwLTRiODEtODg3MS0wZjdmMTFmOTU0MjYiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ2c2x1cGtvQHNvZnRzZXJ2ZWluYy5jb20iLCJlbWFpbCI6InZzbHVwa29Ac29mdHNlcnZlaW5jLmNvbSIsImlzcyI6Imh0dHA6Ly93d3cuZGlnaXRhbGV4cGVyaWVuY2UuaWJtLmNvbS9pc3N1ZXIiLCJleHAiOjE1ODQ0MzQ1NTMsImlhdCI6MTU4NDM5MTM1M30.NO_CyllrwU6eRR1II-RmqIgoOLFaFXNpYDazO56Is0pLCkYb1LRrdjd0VIhmA24hCZISzDz2btONiO4Uar_uvgijLLX9ZsEVuDMReVR46Efja8X4U5IhD3JXfwLsypLqN-FO6ppXNcfGpFYd9qA11jSXcoY8FK9B46qLuvRRAVow4XL4_EgRBEV1TYDhXbn_YnQnunx1h1wf1LZD7I3I7b7Hx_8XHA0tHNwFkWRNIgdI2q-YjKlvfJP42xSXzIQmTO7oaRX2HaUjqiBmho_XYWKudpYaeMPD-KbH_V-BlXPewU7Z4V2RsO9n-NiGpbEbzo_OyHoKHnDAGRTe9oKD5A; Expires=Tue, 17 Mar 2020 08:42:33 GMT; Path=/api; Secure; HttpOnly")
                                .add("strict-transport-security", "max-age=31536000; includeSubDomains")
                                .add("x-content-type-options", "nosniff")
                                .add("x-xss-protection", "1; mode=block")
                                .build()
                )
                .setBody(ResourceReader.read(SUCCESSFUL_LOGIN_MOCK_RESPONSE_FILE_NAME));
    }

    private MockResponse createFailedToLoginMockResponse() {
        return new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .setHeaders(
                        new Headers.Builder()
                                .add("x-ibm-dx-request-id", "e23378856ea4defe1a480e6fe30db4a0")
                                .add("content-type", "application/json; charset=UTF-8")
                                .add("x-newrelic-app-data", "PxQGV1JTCwAIR1RWAAMDU1AJFB9AMQYAZBBZDEtZV0ZaClc9HjZWADdTRRcKBkxjURAJPhgbFUpUHwYfUkhVUgJbD1QBFBkDH0ddVgFYBFRWVVZQUwIJBFVTQ05RUFsVAWw=")
                                .add("content-language", "en-US")
                                .add("vary", "authorization, origin")
                                .add("content-security-policy", "frame-ancestors 'self' https://www.digitalexperience.ibm.com https://www.customer-engagement.ibm.com https://commerceinsights.ibmcloud.com; report-uri https://www.digitalexperience.ibm.com/api/csp/report")
                                .add("x-response-time", "631.944ms")
                                .add("content-length", "327")
                                .add("cache-control", "private, max-age=0")
                                .add("date", "Mon, 16 Mar 2020 20:40:33 GMT")
                                .add("strict-transport-security", "max-age=31536000; includeSubDomains")
                                .add("x-content-type-options", "nosniff")
                                .add("x-xss-protection", "1; mode=block")
                                .build()
                )
                .setBody(ResourceReader.read(FAILED_LOGIN_MOCK_RESPONSE_FILE_NAME));
    }
}