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

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The factory pattern implementation for creating {@link Retrofit} instances,
 * that setup for working with Acoustic API.
 */
class RetrofitFactory {
    private static final int CONNECT_TIMEOUT_SECONDS = 20;
    private static final int READ_TIMEOUT_SECONDS = 20;
    private static final int WRITE_TIMEOUT_SECONDS = 20;

    public static final int INTERCEPT_FLAG_NONE = 0;
    public static final int INTERCEPT_FLAG_LOG = 1;
    public static final int INTERCEPT_FLAG_COOKIE = 2;

    private final Gson gson;

    private final SDKConfig acousticConfig;
    private final InterceptorsFactory interceptorsFactory;

    private final CookieManager cookieJar;


    RetrofitFactory(Gson gson, SDKConfig acousticConfig, CookieManager cookieManager) {
        this(gson, acousticConfig, cookieManager, new InterceptorsFactory());
    }

    @VisibleForTesting
    RetrofitFactory(
            @NonNull Gson gson,
            @NonNull SDKConfig acousticConfig,
            @NonNull CookieManager cookieJar,
            @NonNull InterceptorsFactory interceptorsFactory) {
        this.gson = Validator.checkNotNull(gson, "Gson, cannot be null");
        this.acousticConfig = Validator.checkNotNull(acousticConfig, "acousticConfig, cannot be null");
        this.cookieJar = Validator.checkNotNull(cookieJar, "cookieJar, cannot be null");
        this.interceptorsFactory = Validator.checkNotNull(interceptorsFactory, "interceptorsFactory, cannot be null");
    }

    /**
     * Creates new instance of {@link Retrofit}, that setup for working with Acoustic API.
     *
     * @param interceptFlags the set of flags for intercepting url and providing required modifications
     *                       in it.
     * @return new instance {@link Retrofit}
     */
    Retrofit acousticApiService(int interceptFlags) {
        return new Retrofit
                .Builder()
                .baseUrl(acousticConfig.getApiUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(createOkHttpClient(interceptFlags))
                .build();
    }

    /**
     * Creates new instance of {@link Retrofit}, that setup for working with Acoustic Preview API.
     *
     * @param interceptFlags the set of flags for intercepting url and providing required modifications
     *                       in it.
     * @return new instance {@link Retrofit}
     */
    Retrofit acousticPreviewApiService(int interceptFlags) {
        return new Retrofit
                .Builder()
                .baseUrl(acousticConfig.getPreviewApiUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(createOkHttpClient(interceptFlags))
                .build();
    }

    /**
     * Creates new instance of {@link OkHttpClient}.
     *
     * @param interceptFlags the set of flags for intercepting url and providing required modifications
     *                       in it.
     * @return new instance {@link OkHttpClient}
     */
    private OkHttpClient createOkHttpClient(int interceptFlags) {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        if ((interceptFlags | INTERCEPT_FLAG_LOG) == interceptFlags) {
            builder.addInterceptor(interceptorsFactory.createLoggingInterceptor(HttpLoggingInterceptor.Level.BODY));
        }

        if ((interceptFlags | INTERCEPT_FLAG_COOKIE) == interceptFlags) {
            builder.cookieJar(cookieJar);
        }

        return builder.build();
    }

    /**
     * Extending {@link CookieJar} interface, adds an opportunity to clear cookie.
     */
    interface CookieManager extends CookieJar {
        void clear();
    }
}