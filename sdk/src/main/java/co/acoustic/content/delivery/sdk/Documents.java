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

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * General delivery search query builder that provides general functionality to receive various types of {@link Document}s.
 *
 * @param <T> - type of document that is provided by this delivery search query builder
 */
public abstract class Documents<T extends Document> {

    public static final int DEFAULT_PAGE_SIZE = 10;

    private final DeliverySearch deliverySearch;
    private final DeliverySearchQueryBuilder deliverySearchQueryBuilder;
    private final NetworkingCallsExecutor callsExecutor;

    @Nullable
    private DeliverySearchResultListener<T> deliverySearchResultListener;

    @Nullable
    private DeliverySearchErrorListener deliverySearchErrorListener;

    private DeliverySearchQuery onGoingDeliverySearchQuery;
    private Call<DeliverySearchResponse> onGoingDeliverySearchCall;

    private DeliverySearchResult<T> pendingDeliverySearchResult;
    private Throwable pendingDeliverySearchError;

    private boolean includeDraft;
    private boolean includeProtectedContent;
    private boolean retrieveCompleteContentContext;
    private boolean includeAllFields = true;
    private boolean includeRetired;

    private final Callback<DeliverySearchResponse> onGoingDeliverySearchCallCallback = new Callback<DeliverySearchResponse>() {
        @Override
        public void onResponse(Call<DeliverySearchResponse> call, Response<DeliverySearchResponse> response) {
            onDeliverySearchQueryResponse(call, response, null);
        }

        @Override
        public void onFailure(Call<DeliverySearchResponse> call, Throwable error) {
            onDeliverySearchQueryResponse(call, null, error);
        }
    };

    Documents(@NonNull DeliverySearch deliverySearch, @Nullable State state) {
        this.deliverySearch = deliverySearch;
        this.callsExecutor = new DefaultNetworkingCallsExecutor();

        if (null == state) {
            this.deliverySearchQueryBuilder = new DeliverySearchQueryBuilder();
            filterBy("classification", getTargetDocumentClassification());
            start(0);
            rows(DEFAULT_PAGE_SIZE);
        } else {
            deliverySearchQueryBuilder = state.getDeliverySearchQueryBuilder();
            includeDraft = state.isIncludeDraft();
            includeProtectedContent = state.isIncludeProtectedContent();
            retrieveCompleteContentContext = state.isRetrieveCompleteContentContext();
            includeAllFields = state.isIncludeAllFields();
            includeRetired = state.isIncludeRetired();
        }
    }


    Documents(@NonNull DeliverySearch deliverySearch, @Nullable State state, @NonNull DeliverySearchQueryBuilder deliverySearchQueryBuilder) {
        this(deliverySearch, state, deliverySearchQueryBuilder, new DefaultNetworkingCallsExecutor());
    }

    @VisibleForTesting
    Documents(@NonNull DeliverySearch deliverySearch,
              @Nullable State state,
              @NonNull DeliverySearchQueryBuilder deliverySearchQueryBuilder,
              @NonNull NetworkingCallsExecutor callsExecutor) {
        this.deliverySearch = deliverySearch;
        this.deliverySearchQueryBuilder = deliverySearchQueryBuilder;
        this.callsExecutor = callsExecutor;

        if (null != state) {
            includeDraft = state.isIncludeDraft();
            includeProtectedContent = state.isIncludeProtectedContent();
            retrieveCompleteContentContext = state.isRetrieveCompleteContentContext();
            includeAllFields = state.isIncludeAllFields();
            includeRetired = state.isIncludeRetired();
        }
    }

    /**
     * Adds sort by parameter.
     *
     * @param field     target field, cannot be {@code null} or empty string
     * @param ascending {@code true} - ascending order, {@code false} - descending order
     * @return this
     */
    public Documents sortBy(@NonNull String field, boolean ascending) {
        deliverySearchQueryBuilder.sort(field, ascending);
        return this;
    }

    /**
     * Adds filter by parameter.
     *
     * @param field target field, cannot be {@code null} or empty string
     * @param value target field value, cannot be {@code null} or empty string
     * @return this
     */
    public Documents filterBy(@NonNull String field, @NonNull String value) {
        deliverySearchQueryBuilder.filterQuery(field, value);
        return this;
    }

    /**
     * Adds filter query.
     *
     * @param query Solr filter query "{@code fq}" string, cannot be {@code null} or empty string
     * @return this
     */
    public Documents filterQuery(@NonNull String query) {
        deliverySearchQueryBuilder.filterQuery(query);
        return this;
    }

    /**
     * Text search: search within the complete “text” index field
     *
     * @param text Solr query "{@code query}" string, cannot be {@code null} or empty string
     * @return this
     */
    public Documents searchByText(@NonNull String text) {
        deliverySearchQueryBuilder.query(text);
        return this;
    }

    /**
     * Alters query result rows count, set to {@value #DEFAULT_PAGE_SIZE} by default.
     *
     * @param rows number of rows that should be returned per delivery search response, must be greater or equal 1
     * @return this
     */
    public Documents rows(@IntRange(from = 1) int rows) {
        deliverySearchQueryBuilder.rows(rows);
        return this;
    }

    /**
     * Determines query result start page.
     *
     * @param start number of starting row, must be greater or equal {@code 0}
     * @return this
     */
    public Documents start(@IntRange(from = 0) int start) {
        deliverySearchQueryBuilder.start(start);
        return this;
    }

    /**
     * Adds filter by name parameter.
     *
     * @param name target name, cannot be {@code null} or empty string
     * @return this
     */
    public Documents filterByName(@NonNull String name) {
        return filterBy("name", name);
    }

    /**
     * Adds filter by ID parameter.
     *
     * @param id target ID, cannot be {@code null} or empty string
     * @return this
     */
    public Documents filterById(@NonNull String id) {
        return filterBy("id", id);
    }

    /**
     * Adds filter by tags parameter.
     *
     * @param tags target tag(s), cannot be {@code null} or empty string(s)
     * @return this
     */
    Documents filterByTags(@NonNull String... tags) {
        Validator.checkNotNull(tags);
        final String[] tagsToFilterBy = Validator.checkCondition(tags, "Provided tags cannot contain null or empty string", tagsToCheck -> {
            for (String tag : tagsToCheck) {
                if (TextUtils.isEmpty(tag)) {
                    return false;
                }
            }
            return true;
        });

        final StringBuilder tagsFilterQueryBuilder = new StringBuilder("(").append(tagsToFilterBy[0]);

        for (int i = 1; i < tagsToFilterBy.length; i++) {
            tagsFilterQueryBuilder
                    .append(" OR ")
                    .append(tagsToFilterBy[i]);
        }
        tagsFilterQueryBuilder.append(")");
        return filterBy("tags", tagsFilterQueryBuilder.toString());
    }

    /**
     * Adds filter by categories parameter.
     *
     * @param categories target categories, cannot be {@code null} or empty string
     * @return this
     */
    Documents filterByCategories(@NonNull String categories) {
        Validator.checkNotNull(categories);
        Validator.checkCondition(categories, "Categories cannot be empty", value -> !TextUtils.isEmpty(value));
        return filterBy("categories", "(" + categories + ")");
    }

    /**
     * Initiates delivery search query.
     *
     * @return this
     */
    public Documents get() {
        final NetworkingCallsProvider callsProvider = deliverySearch.sdk.getNetworkingCallsProvider();
        if (null != onGoingDeliverySearchCall) {
            onGoingDeliverySearchCall.cancel();
            onGoingDeliverySearchCall = null;
            onGoingDeliverySearchQuery = null;
        }

        pendingDeliverySearchResult = null;
        pendingDeliverySearchError = null;

        onGoingDeliverySearchQuery = deliverySearchQueryBuilder
                .setIncludeAllFields(includeAllFields)
                .setIncludeDraft(includeDraft)
                .setIncludeRetired(includeRetired)
                .build();

        onGoingDeliverySearchCall = callsProvider.getDeliverySearchCall(
                onGoingDeliverySearchQuery,
                includeDraft,
                includeRetired,
                includeProtectedContent,
                retrieveCompleteContentContext
        );

        callsExecutor.executeCall(onGoingDeliverySearchCall, onGoingDeliverySearchCallCallback);
        return this;
    }

    /**
     * Alters {@link DeliverySearchResultListener} that should get result of this delivery search query.
     *
     * @param listener result listener
     * @return this
     */
    public Documents then(@NonNull DeliverySearchResultListener<T> listener) {
        deliverySearchResultListener = listener;
        notifyPendingDeliverySearchQueryResult();
        return this;
    }

    /**
     * Alters {@link DeliverySearchErrorListener} that should get error that caused delivery search query failure, if any.
     *
     * @param listener error listener
     * @return this
     */
    public Documents error(@NonNull DeliverySearchErrorListener listener) {
        deliverySearchErrorListener = listener;
        notifyPendingDeliverySearchQueryResult();
        return this;
    }

    /**
     * Creates new {@link State} instance that holds state of this instance.
     *
     * @return {@link State} instance that holds state of this instance
     */
    public State getState() {
        return new State(this);
    }

    /**
     * Receives {@link DeliverySearchResult}.
     *
     * @param <E>
     */
    public interface DeliverySearchResultListener<E extends Document> {

        /**
         * Gets {@link DeliverySearchResult} of corresponding query.
         *
         * @param result result of delivery search query
         */
        void onThen(DeliverySearchResult<E> result);
    }

    /**
     * Receives delivery search query errors if any.
     */
    public interface DeliverySearchErrorListener {

        /**
         * Called if there was an error while executing corresponding delivery search query.
         *
         * @param error error that caused deliver search query failure
         */
        void onError(@NonNull Throwable error);
    }

    Documents<T> createNextPageInstance() {
        final Documents<T> newInstance = createInstance(deliverySearch, new DeliverySearchQueryBuilder(deliverySearchQueryBuilder));

        Integer start = newInstance.deliverySearchQueryBuilder.getStart();
        if (null == start) {
            start = 0;
        }

        Integer rows = newInstance.deliverySearchQueryBuilder.getRows();
        if (null == rows) {
            rows = DEFAULT_PAGE_SIZE;
            newInstance.deliverySearchQueryBuilder.rows(rows);
        }

        final int nextPageStart = start + rows;
        newInstance.deliverySearchQueryBuilder.start(nextPageStart);
        return newInstance;
    }

    @Nullable
    Documents<T> createPrevPageInstance() {
        final Documents<T> newInstance = createInstance(deliverySearch, new DeliverySearchQueryBuilder(deliverySearchQueryBuilder));

        Integer start = newInstance.deliverySearchQueryBuilder.getStart();
        if (null == start) {
            start = 0;
        }

        if (start == 0) {
            //we already on first page so can't return previous page.
            return null;
        }

        Integer rows = newInstance.deliverySearchQueryBuilder.getRows();
        if (null == rows) {
            rows = DEFAULT_PAGE_SIZE;
            newInstance.deliverySearchQueryBuilder.rows(rows);
        }

        final int prevPageStart = start - rows;
        newInstance.deliverySearchQueryBuilder.start(prevPageStart < 0 ? 0 : prevPageStart);
        return newInstance;
    }

    void setIncludeDraft(boolean includeDraft) {
        this.includeDraft = includeDraft;
    }

    void setIncludeProtectedContent(boolean includeProtectedContent) {
        this.includeProtectedContent = includeProtectedContent;
    }

    void setRetrieveCompleteContentContext(boolean retrieveCompleteContentContext) {
        this.retrieveCompleteContentContext = retrieveCompleteContentContext;
    }

    void setIncludeAllFields(boolean includeAllFields) {
        this.includeAllFields = includeAllFields;
    }

    void setIncludeRetired(boolean includeRetired) {
        this.includeRetired = includeRetired;
    }

    @NonNull
    abstract String getTargetDocumentClassification();

    @NonNull
    abstract T createDocument(@NonNull DeliverySearchResponseDocument rawDoc);

    @NonNull
    abstract Documents<T> createInstance(@NonNull DeliverySearch deliverySearch, @NonNull DeliverySearchQueryBuilder queryBuilder);

    private void onDeliverySearchQueryResponse(Call<DeliverySearchResponse> call, Response<DeliverySearchResponse> response, Throwable error) {
        if (call.equals(onGoingDeliverySearchCall)) {
            onGoingDeliverySearchQuery = null;
            onGoingDeliverySearchCall = null;

            if (null != error) {
                pendingDeliverySearchError = error;
                notifyPendingDeliverySearchQueryResult();
                return;
            }

            if (response.isSuccessful()) {
                processSuccessfulDeliverySearchQueryResponse(response);
            } else {
                processErrorDeliverySearchQueryResponse(response);
            }
            notifyPendingDeliverySearchQueryResult();
        }
    }

    private void processSuccessfulDeliverySearchQueryResponse(Response<DeliverySearchResponse> response) {
        final DeliverySearchResponse deliverySearchResponse = response.body();
        if (null == deliverySearchResponse) {
            pendingDeliverySearchError = new NullPointerException("Empty response from server");
        } else {
            pendingDeliverySearchResult = new DeliverySearchResult<>(
                    deliverySearchResponse.numFound,
                    this,
                    parseDocuments(deliverySearchResponse.documents)
            );
        }
    }

    private void processErrorDeliverySearchQueryResponse(Response<DeliverySearchResponse> response) {
        final ResponseBody errorResponseBody = response.errorBody();
        if (null == errorResponseBody) {
            pendingDeliverySearchError = new NullPointerException("Empty response from server");
        } else {
            try {
                final NetworkingCallsProvider callsProvider = deliverySearch.sdk
                        .getNetworkingCallsProvider();
                final Converter<ResponseBody, DeliverySearchErrorResponse> errorResponseConverter = callsProvider.getErrorResponseConverter();
                final DeliverySearchErrorResponse errorResponse = errorResponseConverter.convert(errorResponseBody);
                final DeliverySearchError searchResponseError = errorResponse.errors.get(0);
                String searchErrorMsg = searchResponseError.message;
                if (null != searchResponseError.description) {
                    searchErrorMsg += " " + searchResponseError.description;
                }
                pendingDeliverySearchError = new RuntimeException(searchErrorMsg);
            } catch (Exception ex) {
                pendingDeliverySearchError = new RuntimeException("Failed to parse error response", ex);
            }
        }
    }

    @NonNull
    private List<T> parseDocuments(@Nullable List<DeliverySearchResponseDocument> rawDocs) {
        final String targetDocumentClassification = getTargetDocumentClassification();
        final List<T> parsedDocuments = new ArrayList<>();
        if (null != rawDocs) {
            for (DeliverySearchResponseDocument rawDoc : rawDocs) {
                if (null == rawDoc.classification) {
                    rawDoc.classification = targetDocumentClassification;
                }
                if (targetDocumentClassification.equalsIgnoreCase(rawDoc.classification)) {
                    parsedDocuments.add(createDocument(rawDoc));
                }
            }
        }
        return parsedDocuments;
    }

    private void notifyPendingDeliverySearchQueryResult() {
        if (null != deliverySearchResultListener && null != pendingDeliverySearchResult) {
            final DeliverySearchResult<T> deliverySearchResultToNotify = pendingDeliverySearchResult;
            final DeliverySearchResultListener<T> listenerToNotify = deliverySearchResultListener;

            pendingDeliverySearchResult = null;
            deliverySearchResultListener = null;

            listenerToNotify.onThen(deliverySearchResultToNotify);

        } else if (null != deliverySearchErrorListener && null != pendingDeliverySearchError) {
            final Throwable errorToNotify = pendingDeliverySearchError;
            final DeliverySearchErrorListener listenerToNotify = deliverySearchErrorListener;

            pendingDeliverySearchError = null;
            deliverySearchErrorListener = null;

            listenerToNotify.onError(errorToNotify);
        }
    }

    /**
     * Holds state of {@link Documents} instance, intended to be used to serialize / deserialize {@link Documents} instance state.
     */
    public static class State implements Parcelable {

        final Class<?> clazz;

        private final DeliverySearchQueryBuilder deliverySearchQueryBuilder;

        private final boolean includeDraft;
        private final boolean includeProtectedContent;
        private final boolean retrieveCompleteContentContext;
        private final boolean includeAllFields;
        private final boolean includeRetired;

        @VisibleForTesting
        State(
                Class<?> clazz,
                DeliverySearchQueryBuilder deliverySearchQueryBuilder,
                boolean includeDraft,
                boolean includeProtectedContent,
                boolean retrieveCompleteContentContext,
                boolean includeAllFields,
                boolean includeRetired) {
            this.clazz = clazz;
            this.deliverySearchQueryBuilder = deliverySearchQueryBuilder;
            this.includeDraft = includeDraft;
            this.includeProtectedContent = includeProtectedContent;
            this.retrieveCompleteContentContext = retrieveCompleteContentContext;
            this.includeAllFields = includeAllFields;
            this.includeRetired = includeRetired;
        }

        private State(@NonNull Documents<? extends Document> documents) {
            this(
                    documents.getClass(),
                    new DeliverySearchQueryBuilder(documents.deliverySearchQueryBuilder),
                    documents.includeDraft,
                    documents.includeProtectedContent,
                    documents.retrieveCompleteContentContext,
                    documents.includeAllFields,
                    documents.includeRetired
            );
        }

        private State(Parcel source) {
            clazz = (Class<?>) source.readSerializable();
            deliverySearchQueryBuilder = source.readParcelable(DeliverySearchQueryBuilder.class.getClassLoader());
            includeDraft = 1 == source.readInt();
            includeProtectedContent = 1 == source.readInt();
            retrieveCompleteContentContext = 1 == source.readInt();
            includeAllFields = 1 == source.readInt();
            includeRetired = 1 == source.readInt();
        }

        @VisibleForTesting
        DeliverySearchQueryBuilder getDeliverySearchQueryBuilder() {
            return deliverySearchQueryBuilder;
        }

        @VisibleForTesting
        boolean isIncludeDraft() {
            return includeDraft;
        }

        @VisibleForTesting
        boolean isIncludeProtectedContent() {
            return includeProtectedContent;
        }

        @VisibleForTesting
        boolean isRetrieveCompleteContentContext() {
            return retrieveCompleteContentContext;
        }

        @VisibleForTesting
        boolean isIncludeAllFields() {
            return includeAllFields;
        }

        @VisibleForTesting
        boolean isIncludeRetired() {
            return includeRetired;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeSerializable(clazz);
            dest.writeParcelable(deliverySearchQueryBuilder, flags);
            dest.writeInt(includeDraft ? 1 : 0);
            dest.writeInt(includeProtectedContent ? 1 : 0);
            dest.writeInt(retrieveCompleteContentContext ? 1 : 0);
            dest.writeInt(includeAllFields ? 1 : 0);
            dest.writeInt(includeRetired ? 1 : 0);
        }

        public static final Creator<State> CREATOR = new Creator<State>() {
            @Override
            public State createFromParcel(Parcel source) {
                return new State(source);
            }

            @Override
            public State[] newArray(int size) {
                return new State[size];
            }
        };

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            State state = (State) o;

            if (includeDraft != state.includeDraft) return false;
            if (includeProtectedContent != state.includeProtectedContent) return false;
            if (retrieveCompleteContentContext != state.retrieveCompleteContentContext)
                return false;
            if (includeAllFields != state.includeAllFields) return false;
            if (includeRetired != state.includeRetired) return false;
            if (clazz != null ? !clazz.equals(state.clazz) : state.clazz != null) return false;
            return deliverySearchQueryBuilder != null ? deliverySearchQueryBuilder.equals(state.deliverySearchQueryBuilder) : state.deliverySearchQueryBuilder == null;
        }

        @Override
        public int hashCode() {
            int result = clazz != null ? clazz.hashCode() : 0;
            result = 31 * result + (deliverySearchQueryBuilder != null ? deliverySearchQueryBuilder.hashCode() : 0);
            result = 31 * result + (includeDraft ? 1 : 0);
            result = 31 * result + (includeProtectedContent ? 1 : 0);
            result = 31 * result + (retrieveCompleteContentContext ? 1 : 0);
            result = 31 * result + (includeAllFields ? 1 : 0);
            result = 31 * result + (includeRetired ? 1 : 0);
            return result;
        }
    }
}
