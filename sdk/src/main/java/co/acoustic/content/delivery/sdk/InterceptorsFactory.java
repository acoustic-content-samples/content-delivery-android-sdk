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

import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * The factory pattern implementation for creating {@link okhttp3.Interceptor} instances.
 */
class InterceptorsFactory {

    /**
     * Creates logging interceptor.
     *
     * @param level the instance of {@link HttpLoggingInterceptor.Level}, represents logging level.
     * @return new {@link Interceptor} instance.
     */
    Interceptor createLoggingInterceptor(@NonNull HttpLoggingInterceptor.Level level) {
        return new HttpLoggingInterceptor().setLevel(level);
    }
}