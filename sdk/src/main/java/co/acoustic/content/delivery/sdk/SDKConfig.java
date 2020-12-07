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
import androidx.annotation.VisibleForTesting;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Provides configuration for {@link ContentDeliverySDK} instance.
 */
public class SDKConfig {
    private final URL apiUrl;
    private final URL previewApiUrl;

    private SDKConfig(Builder builder) {
        Validator.checkCondition(builder, "Builder should have api url. builder = " + builder, sdkBuilder -> builder.getApiUrl() != null);

        apiUrl = builder.getApiUrl();
        previewApiUrl = builder.getPreviewApiUrl();
    }

    public URL getApiUrl() {
        return apiUrl;
    }

    public URL getPreviewApiUrl() {
        return previewApiUrl;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * The builder implementation for constructing SDKConfig.
     */
    public static class Builder {
        private URL apiUrl = null;
        private URL previewApiUrl = null;

        @VisibleForTesting
        Builder() {
        }

        /**
         * Sets API URL for the config that will be build.
         *
         * @param apiUrl the {@link String} that represents acoustic api url. The default to {@code null}.
         *               Cannot be {@code null} or empty string.
         * @return the current {@link Builder} instance, to continue building.
         * @see #getApiUrl()
         * @see #getPreviewApiUrl()
         */
        public Builder setApiUrl(@NonNull String apiUrl) {
            Validator.checkCondition(apiUrl, "API URL cannot be null or empty", url -> !TextUtils.isEmpty(url));
            try {
                String apiUrlToSet = apiUrl;
                if (!apiUrl.endsWith("/")) {
                    apiUrlToSet += "/";
                }
                this.apiUrl = new URL(apiUrlToSet);
                this.previewApiUrl = createPreviewURL(this.apiUrl);

            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("Invalid API URL", e);
            }
            return this;
        }

        /**
         * Get's API URL for SDK config that will be build.
         *
         * @see #setApiUrl(String)
         */
        public URL getApiUrl() {
            return apiUrl;
        }

        /**
         * Get's Preview API URL for SDK config that will be build.
         *
         * @see #setApiUrl(String)
         */
        public URL getPreviewApiUrl() {
            return previewApiUrl;
        }

        /**
         * Creates new instance of {@link SDKConfig}.
         *
         * @throws IllegalArgumentException if config doesn't contain API url
         */
        @NonNull
        public SDKConfig build() {
            return new SDKConfig(this);
        }

        private URL createPreviewURL(URL apiUrl) throws  MalformedURLException {
            final String[] apiUrlAuthorityParts = apiUrl.getAuthority().split("\\.");
            return new URL(
                    apiUrl.getProtocol(),
                    apiUrl.getAuthority().replace(
                            apiUrlAuthorityParts[0], apiUrlAuthorityParts[0] + "-preview"
                    ),
                    apiUrl.getPath());
        }
    }
}