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

import java.lang.annotation.Annotation;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Retrofit;

import static co.acoustic.content.delivery.sdk.DeliverySearchNetworkServiceConstants.TYPE_DELIVERY_SEARCH;
import static co.acoustic.content.delivery.sdk.DeliverySearchNetworkServiceConstants.TYPE_MY_DELIVERY_SEARCH;

/**
 * Provides delivery search {@link Call}s.
 */
class NetworkingCallsProvider {

    private static final boolean LOG_NETWORK = false;

    private RetrofitFactory retrofitFactory;

    NetworkingCallsProvider(
            @NonNull RetrofitFactory retrofitFactory
    ) {
        this.retrofitFactory = Validator.checkNotNull(retrofitFactory);
    }

    Call<DeliverySearchResponse> getDeliverySearchCall(
            @NonNull DeliverySearchQuery query,
            boolean includeDraft,
            boolean includeRetired,
            boolean includeProtectedContent,
            boolean retrieveCompleteContentContext
    ) {
        final boolean previewApiService = includeDraft || includeRetired;
        final DeliverySearchNetworkService networkService = getRetrofitInstance(
                previewApiService,
                (LOG_NETWORK ? RetrofitFactory.INTERCEPT_FLAG_LOG : 0) | RetrofitFactory.INTERCEPT_FLAG_COOKIE
        ).create(DeliverySearchNetworkService.class);

        Call<DeliverySearchResponse> call;
        if (retrieveCompleteContentContext) {
            call = networkService.renderingSearch(
                    getDeliverySearchType(includeProtectedContent),
                    query.q,
                    query.fq,
                    query.sort,
                    query.fl,
                    query.start,
                    query.rows
            );
        } else {
            call = networkService.search(
                    getDeliverySearchType(includeProtectedContent),
                    query.q,
                    query.fq,
                    query.sort,
                    query.fl,
                    query.start,
                    query.rows
            );
        }
        return call;
    }

    Call<List<LoginResponse>> getLoginCall(@NonNull String authorization) {
        return getRetrofitInstance(
                false,
                (LOG_NETWORK ? RetrofitFactory.INTERCEPT_FLAG_LOG : 0) | RetrofitFactory.INTERCEPT_FLAG_COOKIE
        ).create(LoginNetworkingService.class).login(authorization);
    }

    Retrofit getRetrofitInstance(boolean previewApiService, int interceptFlags) {
        return (previewApiService)
                ? retrofitFactory.acousticPreviewApiService(interceptFlags)
                : retrofitFactory.acousticApiService(interceptFlags);
    }

    @DeliverySearchNetworkServiceConstants.DeliveryTypes
    String getDeliverySearchType(boolean includeProtectedContent) {
        return includeProtectedContent ? TYPE_MY_DELIVERY_SEARCH : TYPE_DELIVERY_SEARCH;
    }

    Converter<ResponseBody, DeliverySearchErrorResponse> getErrorResponseConverter() {
        return getRetrofitInstance(false, 0)
                .responseBodyConverter(DeliverySearchErrorResponse.class, new Annotation[0]);
    }
}