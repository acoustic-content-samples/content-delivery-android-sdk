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

import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RetrofitFactoryTest {
    private static final String DEFAULT_TEST_URL = "https://my7.test.com/";

    private final RetrofitFactory.CookieManager cookieManager = mock(RetrofitFactory.CookieManager.class);

    private SDKConfig config;

    @Before
    public void setup() {
        String apiUrl = DEFAULT_TEST_URL;

        this.config = SDKConfig.builder()
                .setApiUrl(apiUrl)
                .build();
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullGsonException() {
        new RetrofitFactory(null, config, cookieManager);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullConfigException() {
        new RetrofitFactory(new GsonBuilder().create(), null, cookieManager);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullCookieManager() {
        new RetrofitFactory(new GsonBuilder().create(), config, null);
    }

    @Test
    public void testCreateAcousticApiServiceNotNull() {
        Retrofit service = new RetrofitFactory(new GsonBuilder().create(), config, cookieManager).acousticApiService(RetrofitFactory.INTERCEPT_FLAG_NONE);
        assertNotNull("acousticApiService shouldn't return null", service);
    }

    @Test
    public void testCreateAcousticApiServiceWithLoggingInterceptor() {
        //Mock InterceptorsFactory and it's behavior
        final InterceptorsFactory mockInterceptorsFactory = mock(InterceptorsFactory.class);
        when(mockInterceptorsFactory.createLoggingInterceptor(any(HttpLoggingInterceptor.Level.class))).thenReturn(new HttpLoggingInterceptor().setLevel(BODY));

        new RetrofitFactory(new GsonBuilder().create(), config, cookieManager, mockInterceptorsFactory).acousticApiService(RetrofitFactory.INTERCEPT_FLAG_LOG);
        verify(mockInterceptorsFactory).createLoggingInterceptor(any(HttpLoggingInterceptor.Level.class));
    }

    @Test
    public void testCreateAcousticApiServiceWithoutInterceptor() {
        //Mock InterceptorsFactory and it's behavior
        final InterceptorsFactory mockInterceptorsFactory = mock(InterceptorsFactory.class);
        when(mockInterceptorsFactory.createLoggingInterceptor(any(HttpLoggingInterceptor.Level.class))).thenReturn(new HttpLoggingInterceptor().setLevel(BODY));

        new RetrofitFactory(new GsonBuilder().create(), config, cookieManager, mockInterceptorsFactory).acousticApiService(RetrofitFactory.INTERCEPT_FLAG_NONE);
        verifyNoMoreInteractions(mockInterceptorsFactory);
    }

    @Test
    public void testCreateAcousticPreviewApiServiceNotNull() {
        Retrofit service = new RetrofitFactory(new GsonBuilder().create(), config, cookieManager).acousticPreviewApiService(RetrofitFactory.INTERCEPT_FLAG_NONE);
        assertNotNull("acousticPreviewApiService shouldn't return null", service);
    }

    @Test
    public void testCreateAcousticPreviewApiServiceWithLoggingInterceptor() {
        //Mock InterceptorsFactory and it's behavior
        final InterceptorsFactory mockInterceptorsFactory = mock(InterceptorsFactory.class);
        when(mockInterceptorsFactory.createLoggingInterceptor(any(HttpLoggingInterceptor.Level.class))).thenReturn(new HttpLoggingInterceptor().setLevel(BODY));

        new RetrofitFactory(new GsonBuilder().create(), config, cookieManager, mockInterceptorsFactory).acousticPreviewApiService(RetrofitFactory.INTERCEPT_FLAG_LOG);
        verify(mockInterceptorsFactory).createLoggingInterceptor(any(HttpLoggingInterceptor.Level.class));
    }

    @Test
    public void testCreateAcousticPreviewApiServiceWithoutInterceptor() {
        //Mock InterceptorsFactory and it's behavior
        final InterceptorsFactory mockInterceptorsFactory = mock(InterceptorsFactory.class);
        when(mockInterceptorsFactory.createLoggingInterceptor(any(HttpLoggingInterceptor.Level.class))).thenReturn(new HttpLoggingInterceptor().setLevel(BODY));

        new RetrofitFactory(new GsonBuilder().create(), config, cookieManager, mockInterceptorsFactory).acousticPreviewApiService(RetrofitFactory.INTERCEPT_FLAG_NONE);
        verifyNoMoreInteractions(mockInterceptorsFactory);
    }

    @Test
    public void testCallCreation() {
        Call<SearchResponse> testCall = new RetrofitFactory(
                new GsonBuilder().create(), config, cookieManager)
                .acousticApiService(RetrofitFactory.INTERCEPT_FLAG_LOG)
                .create(SearchService.class)
                .getData("*:*");

        assertNotNull("testCall should not be null", testCall);
    }
}