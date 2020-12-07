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

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

/**
 * Delivery search query builder to receive {@link Asset}s.
 */
public class Assets extends Documents<Asset> {

    Assets(@NonNull DeliverySearch deliverySearch, @Nullable State state) {
        super(deliverySearch, state);
    }

    Assets(@NonNull DeliverySearch deliverySearch, @Nullable State state, @NonNull DeliverySearchQueryBuilder queryBuilder) {
        super(deliverySearch, state, queryBuilder);
    }

    @VisibleForTesting
    Assets(@NonNull DeliverySearch deliverySearch, @Nullable State state, @NonNull DeliverySearchQueryBuilder deliverySearchQueryBuilder, @NonNull NetworkingCallsExecutor callsExecutor) {
        super(deliverySearch, state, deliverySearchQueryBuilder, callsExecutor);
    }

    @Override
    public Assets sortBy(@NonNull String field, boolean ascending) {
        return (Assets) super.sortBy(field, ascending);
    }

    @Override
    public Assets filterBy(@NonNull String field, @NonNull String value) {
        return (Assets) super.filterBy(field, value);
    }

    @Override
    public Assets filterQuery(@NonNull String query) {
        return (Assets) super.filterQuery(query);
    }

    @Override
    public Assets searchByText(@NonNull String text) {
        return (Assets) super.searchByText(text);
    }

    @Override
    public Assets rows(@IntRange(from = 1) int rows) {
        return (Assets) super.rows(rows);
    }

    @Override
    public Assets start(@IntRange(from = 0) int start) {
        return (Assets) super.start(start);
    }

    @Override
    public Assets get() {
        return (Assets) super.get();
    }

    @Override
    public Assets then(@NonNull DeliverySearchResultListener<Asset> listener) {
        return (Assets) super.then(listener);
    }

    @Override
    public Assets error(@NonNull DeliverySearchErrorListener listener) {
        return (Assets) super.error(listener);
    }

    @Override
    public Assets filterByName(@NonNull String name) {
        return (Assets) super.filterByName(name);
    }

    @Override
    public Assets filterById(@NonNull String id) {
        return (Assets) super.filterById(id);
    }

    @Override
    public Assets filterByTags(@NonNull String... tags) {
        return (Assets) super.filterByTags(tags);
    }

    @Override
    public Assets filterByCategories(@NonNull String categories) {
        return (Assets) super.filterByCategories(categories);
    }

    /**
     * Call to retrieve draft content in addition to published content.
     * <p/>
     * <b>NOTE:</b> requires authenticated user, in order to login use {@link ContentDeliverySDK#login(String, String, ContentDeliverySDK.LoginListener)}.
     *
     * @param include {@code true} - include drafts, {@code false} - don't include drafts
     * @return this
     */
    public Assets includeDraft(boolean include) {
        setIncludeDraft(include);
        return this;
    }

    /**
     * Call to include retired assets.
     * <p/>
     * <b>NOTE:</b> requires authenticated user, in order to login use {@link ContentDeliverySDK#login(String, String, ContentDeliverySDK.LoginListener)}.
     *
     * @param include {@code true} - include retired assets, {@code false} - don't include retired assets
     * @return this
     */
    public Assets includeRetired(boolean include) {
        setIncludeRetired(include);
        return this;
    }

    /**
     * Call to include all fields for each asset, set to {@code true} by default.
     *
     * @param includeAllFields {@code true} - include all fields, {@code false} - do not include all fields
     * @return this
     */
    public Assets includeAllFields(boolean includeAllFields) {
        setIncludeAllFields(includeAllFields);
        return this;
    }

    @NonNull
    @Override
    String getTargetDocumentClassification() {
        return "asset";
    }

    @NonNull
    @Override
    Asset createDocument(@NonNull DeliverySearchResponseDocument rawDoc) {
        return new Asset(rawDoc);
    }

    @NonNull
    @Override
    Assets createInstance(@NonNull DeliverySearch deliverySearch, @NonNull DeliverySearchQueryBuilder queryBuilder) {
        return new Assets(deliverySearch, null, queryBuilder);
    }
}
