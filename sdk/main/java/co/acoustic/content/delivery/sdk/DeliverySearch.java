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

/**
 * Provides access to delivery search APIs.
 */
public class DeliverySearch {

    final ContentDeliverySDK sdk;

    DeliverySearch(@NonNull ContentDeliverySDK sdk) {
        this.sdk = Validator.checkNotNull(sdk, "SDK cannot be null");
    }

    /**
     * @return delivery search query builder to obtain {@link Asset}s
     */
    public Assets assets() {
        return new Assets(this, null);
    }

    /**
     * @return delivery search query builder to obtain {@link Category}s
     */
    public Categories categories() {
        return new Categories(this, null);
    }

    /**
     * @return delivery search query builder to obtain {@link ContentItem}s
     */
    public ContentItems contentItems() {
        return new ContentItems(this, null);
    }

    /**
     * @return delivery search query builder to obtain {@link ContentType}s
     */
    public ContentTypes contentTypes() {
        return new ContentTypes(this, null);
    }

    /**
     * Creates {@link Documents} instance using given {@link co.acoustic.content.delivery.sdk.Documents.State}.
     *
     * @param state {@link Documents} state, cannot be {@code null}
     * @param <T> - expected {@link Document} type for returned {@link Documents} instance
     * @param <E> - expected {@link Documents} type
     * @return new {@link Documents} instance initialize with given state
     */
    @NonNull
    public <T extends Document, E extends Documents<T>> E createDocuments (@NonNull Documents.State state) {
        Validator.checkNotNull(state, "State cannot be null");
        if (Assets.class.equals(state.clazz)) {
            return (E) new Assets(this, state);
        } else if (Categories.class.equals(state.clazz)) {
            return (E) new Categories(this, state);
        } else if (ContentItems.class.equals(state.clazz)) {
            return (E) new ContentItems(this, state);
        } else if (ContentTypes.class.equals(state.clazz)) {
            return (E) new ContentTypes(this, state);
        }
        throw new IllegalArgumentException("Cannot create instance using given state");
    }

    /**
     * Creates {@link DeliverySearchResult} instance using given {@link DeliverySearchResult.State}.
     *
     * @param state {@link DeliverySearchResult} state, cannot be {@code null}
     * @param <T> - expected {@link Document} type for returned {@link DeliverySearchResult} instance
     * @return new {@link DeliverySearchResult} instance initialize with given state
     */
    @NonNull
    public <T extends Document> DeliverySearchResult<T> createDeliverySearchResult(@NonNull DeliverySearchResult.State state) {
        Validator.checkNotNull(state, "State cannot be null");
        return new DeliverySearchResult<>(this, state);
    }
}