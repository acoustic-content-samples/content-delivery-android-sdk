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

import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Provides functionality for making blocking retrofit calls.
 */
class RetrofitBlockingCall {
    /**
     * Makes blocked request to retrofit service.
     *
     * @param dataLoaderFactory factory that creates {@link Call} instance for particular ResponseType.
     * @param <ResponseT> the expected response type.
     *
     * @return the instance of {@link Result}.
     */
    static <ResponseT> Result<ResponseT> blockingGet(@NonNull CallFactory<ResponseT> dataLoaderFactory) {
        final CountDownLatch airlockLatch = new CountDownLatch(1);
        final Result<ResponseT> result = new Result<>();

        dataLoaderFactory.createCall().enqueue(new Callback<ResponseT>() {
            @Override
            public void onResponse(Call<ResponseT> call, Response<ResponseT> response) {
                result.rawResponse = response;
                if (response.body() != null) {
                    result.response = response.body();
                    result.error = null;
                } else {
                    result.response = null;
                    result.error = new NullPointerException("response is null");
                }
                airlockLatch.countDown();
            }
            @Override
            public void onFailure(Call<ResponseT> call, Throwable error) {
                result.response = null;
                result.error = new NullPointerException("response is null");
                airlockLatch.countDown();
            }
        });

        try {
            airlockLatch.await();
        } catch (InterruptedException e) {
        }

        return result;
    }

    static class Result<ResponseT> {
        private Response<ResponseT> rawResponse;
        private ResponseT response;
        private Throwable error;

        public ResponseT getResponse() {
            return response;
        }

        public Response<ResponseT> getRawResponse() {
            return rawResponse;
        }

        public Throwable getError() {
            return error;
        }
    }

    interface CallFactory<ResponseT> {
        Call<ResponseT> createCall();
    }
}