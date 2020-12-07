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
 * Delivery search query builder to receive {@link ContentItem}s.
 */
public class ContentItems extends Documents<ContentItem> {

    ContentItems(@NonNull DeliverySearch deliverySearch, @Nullable State state) {
        super(deliverySearch, state);
    }

    ContentItems(@NonNull DeliverySearch deliverySearch, @Nullable State state, @NonNull DeliverySearchQueryBuilder queryBuilder) {
        super(deliverySearch, state, queryBuilder);
    }

    @VisibleForTesting
    ContentItems(@NonNull DeliverySearch deliverySearch, @Nullable State state, @NonNull DeliverySearchQueryBuilder deliverySearchQueryBuilder, @NonNull NetworkingCallsExecutor callsExecutor) {
        super(deliverySearch, state, deliverySearchQueryBuilder, callsExecutor);
    }

    @Override
    public ContentItems sortBy(@NonNull String field, boolean ascending) {
        return (ContentItems) super.sortBy(field, ascending);
    }

    @Override
    public ContentItems filterBy(@NonNull String field, @NonNull String value) {
        return (ContentItems) super.filterBy(field, value);
    }

    @Override
    public ContentItems filterQuery(@NonNull String query) {
        return (ContentItems) super.filterQuery(query);
    }

    @Override
    public ContentItems searchByText(@NonNull String text) {
        return (ContentItems) super.searchByText(text);
    }

    @Override
    public ContentItems rows(@IntRange(from = 1) int rows) {
        return (ContentItems) super.rows(rows);
    }

    @Override
    public ContentItems start(@IntRange(from = 0) int start) {
        return (ContentItems) super.start(start);
    }

    @Override
    public ContentItems get() {
        return (ContentItems) super.get();
    }

    @Override
    public ContentItems then(@NonNull DeliverySearchResultListener<ContentItem> listener) {
        return (ContentItems) super.then(listener);
    }

    @Override
    public ContentItems error(@NonNull DeliverySearchErrorListener listener) {
        return (ContentItems) super.error(listener);
    }

    @Override
    public ContentItems filterByName(@NonNull String name) {
        return (ContentItems) super.filterByName(name);
    }

    @Override
    public ContentItems filterById(@NonNull String id) {
        return (ContentItems) super.filterById(id);
    }

    @Override
    public ContentItems filterByTags(@NonNull String... tags) {
        return (ContentItems) super.filterByTags(tags);
    }

    @Override
    public ContentItems filterByCategories(@NonNull String categories) {
        return (ContentItems) super.filterByCategories(categories);
    }

    /**
     * Call to retrieve protected content.
     * <p/>
     * <b>NOTE:</b> requires authenticated user, in order to login use {@link ContentDeliverySDK#login(String, String, ContentDeliverySDK.LoginListener)}.
     *
     * @param protectedContent {@code true} - retrieve protected content, {@code false} - don't retrieve protected content
     * @return this
     */
    public ContentItems protectedContent(boolean protectedContent) {
        setIncludeProtectedContent(protectedContent);
        return this;
    }

    /**
     * Call to retrieve complete content context.
     * <p/>
     * <b>NOTE:</b>
     * <ul>
     *     <li>if set to {@code true} only {@code document} field is returned by server.</li>
     *     <li>requires authenticated user, in order to login use {@link ContentDeliverySDK#login(String, String, ContentDeliverySDK.LoginListener)}.</li>
     * </ul>
     *
     * @param completeContentContext {@code true} - retrieve complete content context, {@code false} - don't retrieve complete content context
     * @return this
     */
    public ContentItems completeContentContext(boolean completeContentContext) {
        setRetrieveCompleteContentContext(completeContentContext);
        return this;
    }

    /**
     * Call to retrieve draft content in addition to published content.
     * <p/>
     * <b>NOTE:</b> requires authenticated user, in order to login use {@link ContentDeliverySDK#login(String, String, ContentDeliverySDK.LoginListener)}.
     *
     * @param include {@code true} - include drafts, {@code false} - don't include drafts
     * @return this
     */
    public ContentItems includeDraft(boolean include) {
        setIncludeDraft(include);
        return this;
    }

    /**
     * Call to include retired content.
     * <p/>
     * <b>NOTE:</b> requires authenticated user, in order to login use {@link ContentDeliverySDK#login(String, String, ContentDeliverySDK.LoginListener)}.
     *
     * @param include {@code true} - include retired content, {@code false} - don't include retired content
     * @return this
     */
    public ContentItems includeRetired(boolean include) {
        setIncludeRetired(include);
        return this;
    }

    /**
     * Call to include all fields for each content item, set to {@code true} by default.
     *
     * @param includeAllFields {@code true} - include all fields, {@code false} - do not include all fields
     * @return this
     */
    public ContentItems includeAllFields(boolean includeAllFields) {
        setIncludeAllFields(includeAllFields);
        return this;
    }

    @NonNull
    @Override
    String getTargetDocumentClassification() {
        return "content";
    }

    @NonNull
    @Override
    ContentItem createDocument(@NonNull DeliverySearchResponseDocument rawDoc) {
        return new ContentItem(rawDoc);
    }

    @NonNull
    @Override
    ContentItems createInstance(@NonNull DeliverySearch deliverySearch, @NonNull DeliverySearchQueryBuilder queryBuilder) {
        return new ContentItems(deliverySearch, null, queryBuilder);
    }
}
