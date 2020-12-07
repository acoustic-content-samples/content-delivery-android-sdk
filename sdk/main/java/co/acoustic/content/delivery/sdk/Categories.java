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
 * Delivery search query builder to receive {@link Category}s.
 */
public class Categories extends Documents<Category> {

    Categories(@NonNull DeliverySearch deliverySearch, @Nullable State state) {
        super(deliverySearch, state);
    }

    Categories(@NonNull DeliverySearch deliverySearch, @Nullable State state, @NonNull DeliverySearchQueryBuilder queryBuilder) {
        super(deliverySearch, state, queryBuilder);
    }

    @VisibleForTesting
    Categories(@NonNull DeliverySearch deliverySearch, @Nullable State state, @NonNull DeliverySearchQueryBuilder deliverySearchQueryBuilder, @NonNull NetworkingCallsExecutor callsExecutor) {
        super(deliverySearch, state, deliverySearchQueryBuilder, callsExecutor);
    }

    @Override
    public Categories sortBy(@NonNull String field, boolean ascending) {
        return (Categories) super.sortBy(field, ascending);
    }

    @Override
    public Categories filterBy(@NonNull String field, @NonNull String value) {
        return (Categories) super.filterBy(field, value);
    }

    @Override
    public Categories filterQuery(@NonNull String query) {
        return (Categories) super.filterQuery(query);
    }

    @Override
    public Categories searchByText(@NonNull String text) {
        return (Categories) super.searchByText(text);
    }

    @Override
    public Categories rows(@IntRange(from = 1) int rows) {
        return (Categories) super.rows(rows);
    }

    @Override
    public Categories start(@IntRange(from = 0) int start) {
        return (Categories) super.start(start);
    }

    @Override
    public Categories get() {
        return (Categories) super.get();
    }

    @Override
    public Categories then(@NonNull DeliverySearchResultListener<Category> listener) {
        return (Categories) super.then(listener);
    }

    @Override
    public Categories error(@NonNull DeliverySearchErrorListener listener) {
        return (Categories) super.error(listener);
    }

    @Override
    public Categories filterByName(@NonNull String name) {
        return (Categories) super.filterByName(name);
    }

    @Override
    public Categories filterById(@NonNull String id) {
        return (Categories) super.filterById(id);
    }

    @NonNull
    @Override
    String getTargetDocumentClassification() {
        return "category";
    }

    @NonNull
    @Override
    Category createDocument(@NonNull DeliverySearchResponseDocument rawDoc) {
        return new Category(rawDoc);
    }

    @NonNull
    @Override
    Categories createInstance(@NonNull DeliverySearch deliverySearch, @NonNull DeliverySearchQueryBuilder queryBuilder) {
        return new Categories(deliverySearch, null, queryBuilder);
    }
}
