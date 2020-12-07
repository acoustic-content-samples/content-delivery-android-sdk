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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Provides access to Acoustic Content Delivery APIs.
 */
public class ContentDeliverySDK {

    private static final boolean LOG_NETWORK = false;

    private final DataEncoder encoder;

    private final DeliverySearch deliverySearch;
    private final NetworkingCallsProvider networkingCallsProvider;
    private final NetworkingCallsExecutor networkingCallsExecutor;
    private final RetrofitFactory retrofitFactory;

    private final SdkCookieManager cookieManager;

    private String currentUserName = null;
    private String encodedCredentials = null;

    private Call<List<LoginResponse>> currentAuthCall;

    public static ContentDeliverySDK create(@NonNull SDKConfig config) {
        return new ContentDeliverySDK(config);
    }

    /**
     * @return {@link DeliverySearch} instance,
     */
    public DeliverySearch deliverySearch() {
        return deliverySearch;
    }

    NetworkingCallsProvider getNetworkingCallsProvider() {
        return networkingCallsProvider;
    }

    private ContentDeliverySDK(@NonNull SDKConfig config) {
        this(config, new DataEncoder());
    }

    ContentDeliverySDK(@NonNull SDKConfig config, DataEncoder dataEncoder) {
        this(config, dataEncoder, new DefaultNetworkingCallsExecutor());
    }

    ContentDeliverySDK(
            @NonNull SDKConfig config,
            DataEncoder dataEncoder,
            NetworkingCallsExecutor networkingCallsExecutor
    ) {
        this.encoder = dataEncoder;
        this.networkingCallsExecutor = networkingCallsExecutor;

        this.cookieManager = new SdkCookieManager();

        retrofitFactory = new RetrofitFactory(
                new GsonBuilder()
                        .registerTypeAdapter(JSONObject.class, new JSONObjectJsonDeserializer())
                        .create(),
                config,
                cookieManager
        );
        deliverySearch = new DeliverySearch(this);
        networkingCallsProvider = new NetworkingCallsProvider(retrofitFactory);
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    /**
     * Logs out current user.
     */
    public void logout() {
        clearCurrentAuthCall();
        cookieManager.clear();
        encodedCredentials = null;
        currentUserName = null;
    }

    /**
     * Checks whether current content delivery SDK instance logged in.
     */
    public boolean isLoggedIn() {
        return  (encodedCredentials != null);
    }

    /**
     * Triggers user login. The login process result will be passed through the listener.
     *
     * @param userName      the user name {@link String}, cannot be null or empty.
     * @param password      the user password {@link String}, cannot be null or empty.
     * @param loginListener the {@link LoginListener} to pass login process result.
     */
    public void login(final String userName, final String password, @Nullable LoginListener loginListener) {
        final String credentials = Validator.checkCondition(userName, "userName cannot be null or empty", name -> !TextUtils.isEmpty(name))
                + ":"
                + Validator.checkCondition(password, "password cannot be null or empty", pass -> !TextUtils.isEmpty(pass));

        final String newEncodedCredentials = encoder.encodeToString(credentials.getBytes());

        if (newEncodedCredentials.equals(this.encodedCredentials)) {
            currentUserName = userName;
            notifyLoginDone(loginListener, null);
            return;
        }

        currentUserName = null;
        clearCurrentAuthCall();

        currentAuthCall = retrofitFactory
                .acousticApiService((LOG_NETWORK ? RetrofitFactory.INTERCEPT_FLAG_LOG : 0 ) | RetrofitFactory.INTERCEPT_FLAG_COOKIE)
                .create(LoginNetworkingService.class)
                .login("Basic " + newEncodedCredentials);

        networkingCallsExecutor.executeCall(
                currentAuthCall,
                new Callback<List<LoginResponse>>() {
                    @Override
                    public void onResponse(Call<List<LoginResponse>> call, Response<List<LoginResponse>> response) {
                        if(!call.isCanceled()) {
                            final Throwable error = processLoginResponse(response);
                            if (null == error) {
                                currentUserName = userName;
                                ContentDeliverySDK.this.encodedCredentials = newEncodedCredentials;
                            } else {
                                cookieManager.clear();
                            }
                            notifyLoginDone(loginListener, error);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<LoginResponse>> call, Throwable error) {
                        if (!call.isCanceled()) {
                            notifyLoginDone(loginListener, error);
                            cookieManager.clear();
                        }
                    }
                }
        );
    }

    SdkCookieManager getCookieManager() {
        return cookieManager;
    }

    private void clearCurrentAuthCall() {
        if (currentAuthCall != null && !currentAuthCall.isCanceled()) {
            currentAuthCall.cancel();
        }
    }

    private void notifyLoginDone(@Nullable LoginListener loginListener, @Nullable Throwable error) {
        if (loginListener != null) {
            loginListener.onLoginDone(error);
        }
    }

    private Throwable processLoginResponse(Response<List<LoginResponse>> response) {
        if (!response.isSuccessful()) {
            final ResponseBody errorResponseBody = response.errorBody();
            try {
                final Converter<ResponseBody, DeliverySearchErrorResponse> errorResponseConverter = networkingCallsProvider.getErrorResponseConverter();
                final DeliverySearchErrorResponse errorResponse = errorResponseConverter.convert(errorResponseBody);
                final DeliverySearchError searchResponseError = errorResponse.errors.get(0);
                String searchErrorMsg = searchResponseError.message;
                if (null != searchResponseError.description) {
                    searchErrorMsg += " " + searchResponseError.description;
                }
                return new RuntimeException(searchErrorMsg);
            } catch (Exception ex) {
                return new RuntimeException("Failed to parse error response", ex);
            }
        }
        return null;
    }

    /**
     * Defines contract for class aware in login process.
     */
    public interface LoginListener {

        /**
         * Called once login process is finished.
         *
         * @param error will be null if login was successful, in other case will provide related {@link Throwable} instance.
         */
        void onLoginDone(@Nullable Throwable error);
    }
}
