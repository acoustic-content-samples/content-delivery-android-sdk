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

import org.junit.Test;

import java.net.URL;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Converter;

import static co.acoustic.content.delivery.sdk.DeliverySearchNetworkServiceConstants.TYPE_DELIVERY_SEARCH;
import static co.acoustic.content.delivery.sdk.DeliverySearchNetworkServiceConstants.TYPE_MY_DELIVERY_SEARCH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NetworkingCallsProviderTest {

    @Test
    public void testCreation() {
        NetworkingCallsProvider callsProvider = new NetworkingCallsProvider(mock(RetrofitFactory.class));
        assertNotNull(callsProvider);
    }

    @Test(expected = NullPointerException.class)
    public void testCreationWithNull() {
        new NetworkingCallsProvider(null);
    }

    @Test
    public void testGetRetrofitInstance() {
        RetrofitFactory factory = mock(RetrofitFactory.class);
        NetworkingCallsProvider callsProvider = new NetworkingCallsProvider(factory);

        callsProvider.getRetrofitInstance(true, RetrofitFactory.INTERCEPT_FLAG_NONE);
        verify(factory).acousticPreviewApiService(anyInt());
        callsProvider.getRetrofitInstance(false, RetrofitFactory.INTERCEPT_FLAG_NONE);
        verify(factory).acousticApiService(anyInt());
    }

    @Test
    public void testGetDeliverySearchType() {
        NetworkingCallsProvider callsProvider = new NetworkingCallsProvider(mock(RetrofitFactory.class));
        assertEquals(TYPE_MY_DELIVERY_SEARCH, callsProvider.getDeliverySearchType(true));
        assertEquals(TYPE_DELIVERY_SEARCH, callsProvider.getDeliverySearchType(false));
    }

    @Test
    public void testGetErrorResponseConverter() {
        ContentDeliverySDK sdk = ContentDeliverySDK
                .create(SDKConfig.builder().setApiUrl("https://my7.test.com/").build());

        NetworkingCallsProvider callsProvider = sdk.getNetworkingCallsProvider();
        Converter<ResponseBody, DeliverySearchErrorResponse> converter = callsProvider.getErrorResponseConverter();

        assertNotNull(converter);
    }

    @Test
    public void testGetLoginCall() {
        ContentDeliverySDK sdk = ContentDeliverySDK
                .create(SDKConfig.builder().setApiUrl("https://my7.test.com/").build());

        Call<List<LoginResponse>> call = sdk.getNetworkingCallsProvider().getLoginCall("test_auth");
        assertNotNull(call);
    }

    @Test
    public void testDeliverySearchCall() {
        NetworkingCallsProvider callsProvider = ContentDeliverySDK
                .create(SDKConfig.builder().setApiUrl("https://my7.test.com/").build())
                .getNetworkingCallsProvider();

        URL url = callsProvider.getDeliverySearchCall(mock(DeliverySearchQuery.class), true, true, false, false).request().url().url();
        String authority = url.getAuthority();
        String path = url.getPath();
        assertEquals("IF includeDraft OR includeRetired authority should include \"-preview\"", "my7-preview.test.com", authority);
        assertEquals("IF NOT retrieveCompleteContentContext should use path for search API", "/delivery/v1/search", path);

        url = callsProvider.getDeliverySearchCall(mock(DeliverySearchQuery.class), true, false, false, true).request().url().url();
        authority = url.getAuthority();
        path = url.getPath();
        assertEquals("IF includeDraft OR includeRetired authority should include \"-preview\"", "my7-preview.test.com", authority);
        assertEquals("IF retrieveCompleteContentContext should use path for complete content context API", "/delivery/v1/rendering/search", path);

        url = callsProvider.getDeliverySearchCall(mock(DeliverySearchQuery.class), false, true, false, true).request().url().url();
        authority = url.getAuthority();
        path = url.getPath();
        assertEquals("IF includeDraft OR includeRetired authority should include \"-preview\"", "my7-preview.test.com", authority);
        assertEquals("IF retrieveCompleteContentContext should use path for complete content context API", "/delivery/v1/rendering/search", path);

        url = callsProvider.getDeliverySearchCall(mock(DeliverySearchQuery.class), false, false, false, false).request().url().url();
        authority = url.getAuthority();
        path = url.getPath();
        assertEquals("IF NOT includeDraft AND NOT includeRetired authority shouldn't include \"-preview\"", "my7.test.com", authority);
        assertEquals("IF NOT retrieveCompleteContentContext should use path for complete content context API", "/delivery/v1/search", path);
    }
}