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
 * Delivery search query builder to receive {@link ContentType}s.
 */
public class ContentTypes extends Documents<ContentType> {

    ContentTypes(@NonNull DeliverySearch deliverySearch, @Nullable State state) {
        super(deliverySearch, state);
    }

    ContentTypes(@NonNull DeliverySearch deliverySearch, @Nullable State state, @NonNull DeliverySearchQueryBuilder queryBuilder) {
        super(deliverySearch, state, queryBuilder);
    }

    @VisibleForTesting
    ContentTypes(@NonNull DeliverySearch deliverySearch, @Nullable State state, @NonNull DeliverySearchQueryBuilder deliverySearchQueryBuilder, @NonNull NetworkingCallsExecutor callsExecutor) {
        super(deliverySearch, state, deliverySearchQueryBuilder, callsExecutor);
    }

    @Override
    public ContentTypes sortBy(@NonNull String field, boolean ascending) {
        return (ContentTypes) super.sortBy(field, ascending);
    }

    @Override
    public ContentTypes filterBy(@NonNull String field, @NonNull String value) {
        return (ContentTypes) super.filterBy(field, value);
    }

    @Override
    public ContentTypes filterQuery(@NonNull String query) {
        return (ContentTypes) super.filterQuery(query);
    }

    @Override
    public ContentTypes searchByText(@NonNull String text) {
        return (ContentTypes) super.searchByText(text);
    }

    @Override
    public ContentTypes rows(@IntRange(from = 1) int rows) {
        return (ContentTypes) super.rows(rows);
    }

    @Override
    public ContentTypes start(@IntRange(from = 0) int start) {
        return (ContentTypes) super.start(start);
    }

    @Override
    public ContentTypes get() {
        return (ContentTypes) super.get();
    }

    @Override
    public ContentTypes then(@NonNull DeliverySearchResultListener<ContentType> listener) {
        return (ContentTypes) super.then(listener);
    }

    @Override
    public ContentTypes error(@NonNull DeliverySearchErrorListener listener) {
        return (ContentTypes) super.error(listener);
    }

    @Override
    public ContentTypes filterByName(@NonNull String name) {
        return (ContentTypes) super.filterByName(name);
    }

    @Override
    public ContentTypes filterById(@NonNull String id) {
        return (ContentTypes) super.filterById(id);
    }

    @NonNull
    @Override
    String getTargetDocumentClassification() {
        return "content-type";
    }

    @NonNull
    @Override
    ContentType createDocument(@NonNull DeliverySearchResponseDocument rawDoc) {
        return new ContentType(rawDoc);
    }

    @NonNull
    @Override
    ContentTypes createInstance(@NonNull DeliverySearch deliverySearch, @NonNull DeliverySearchQueryBuilder queryBuilder) {
        return new ContentTypes(deliverySearch, null, queryBuilder);
    }
}
