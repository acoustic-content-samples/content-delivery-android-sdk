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

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents delivery search query results.
 *
 * @param <T> - type of retrieved {@link Document}(s)
 */
public class DeliverySearchResult<T extends Document> {

    private final int numFound;
    private final List<T> documents;
    private final Documents<T> request;

    DeliverySearchResult(@IntRange(from = 0) int numFound, @NonNull Documents<T> request, @NonNull List<T> documents) {
        this.numFound = Validator.checkCondition(numFound, "numFound cannot be less then 0", value -> (value >= 0));
        this.request = Validator.checkNotNull(request, "request cannot be null");
        this.documents = Validator.checkNotNull(documents, "documents cannot be null");
    }

    DeliverySearchResult(@NonNull DeliverySearch deliverySearch, @NonNull State state) {
        Validator.checkNotNull(state, "state cannot be null");
        Validator.checkNotNull(deliverySearch, "deliverySearch cannot be null");
        numFound = state.numFound;
        documents = (List<T>) state.documents;
        request = deliverySearch.createDocuments(state.requestState);
    }

    /**
     * @return number of found documents by delivery search query.
     */
    public int getNumFound() {
        return numFound;
    }

    /**
     * @return documents that were found by delivery search query
     */
    public List<T> getDocuments() {
        return new ArrayList<>(documents);
    }

    /**
     * @return delivery search query builder configured to retrieve next page of delivery search query that returned this {@link DeliverySearchResult}.
     */
    public <E extends Documents<T>> E nextPage() {
        return (E) request.createNextPageInstance();
    }

    /**
     * @return delivery search query builder configured to retrieve previous page of delivery search query that returned this {@link DeliverySearchResult},
     * if current {@link DeliverySearchResult} instance represents the first page, returns null.
     */
    @Nullable
    public <E extends Documents<T>> E previousPage() {
        Documents<T> page = request.createPrevPageInstance();
        return (page != null) ? (E) page : null;
    }

    /**
     * Creates new {@link State} instance that holds state of this instance.
     *
     * @return {@link State} instance that holds state of this instance
     */
    @NonNull
    public State getState() {
        return new State(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeliverySearchResult<?> that = (DeliverySearchResult<?>) o;

        if (numFound != that.numFound) return false;
        if (documents != null ? !documents.equals(that.documents) : that.documents != null)
            return false;
        return request != null ? request.equals(that.request) : that.request == null;
    }

    @Override
    public int hashCode() {
        int result = numFound;
        result = 31 * result + (documents != null ? documents.hashCode() : 0);
        result = 31 * result + (request != null ? request.hashCode() : 0);
        return result;
    }

    public static class State implements Parcelable {

        private final int numFound;
        private final List<? extends Document> documents;

        private final Documents.State requestState;

        private State(@NonNull DeliverySearchResult deliverySearchResult) {
            this.numFound = deliverySearchResult.numFound;
            this.documents = deliverySearchResult.documents;
            this.requestState = deliverySearchResult.request.getState();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(numFound);

            final int documentsSize = documents.size();
            dest.writeInt(documentsSize);
            for (int i = 0; i < documentsSize; i++) {
                dest.writeParcelable(documents.get(i), flags);
            }

            dest.writeParcelable(requestState, flags);
        }

        private State(Parcel source) {
            numFound = source.readInt();

            final int documentsSize = source.readInt();
            documents = new ArrayList<>(documentsSize);
            for (int i = 0; i < documentsSize; i++) {
                documents.add(source.readParcelable(getClass().getClassLoader()));
            }

            requestState = source.readParcelable(Documents.State.class.getClassLoader());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            State state = (State) o;

            if (numFound != state.numFound) return false;
            if (documents != null ? !documents.equals(state.documents) : state.documents != null)
                return false;
            return requestState != null ? requestState.equals(state.requestState) : state.requestState == null;
        }

        @Override
        public int hashCode() {
            int result = numFound;
            result = 31 * result + (documents != null ? documents.hashCode() : 0);
            result = 31 * result + (requestState != null ? requestState.hashCode() : 0);
            return result;
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
    }

}
