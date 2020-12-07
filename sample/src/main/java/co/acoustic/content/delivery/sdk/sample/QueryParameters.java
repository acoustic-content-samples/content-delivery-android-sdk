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

package co.acoustic.content.delivery.sdk.sample;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Contains all common query parameters, that could be used for making SDK call. Prepare for transferring between screens.
 */
class QueryParameters implements Parcelable {

    private int start;
    private int rows;
    private Map<String, Boolean> sortBy = new LinkedHashMap<>();
    private Map<String, String> filterBy = new LinkedHashMap<>();
    private List<String> filterQueries = new LinkedList<>();

    private boolean includeDraft;
    private boolean includeRetired;
    private boolean protectedContent;

    QueryParameters() {
    }

    int getStart() {
        return start;
    }

    void setStart(int start) {
        this.start = start;
    }

    int getRows() {
        return rows;
    }

    void setRows(int rows) {
        this.rows = rows;
    }

    void addSortBy(String field, boolean ascending) {
        sortBy.put(field, ascending);
    }

    void removeSortBy() {
        if (sortBy.isEmpty()) {
            return;
        }
        Iterator<String> keysIterator = sortBy.keySet().iterator();
        String lastKey = null;
        while (keysIterator.hasNext()) {
            lastKey = keysIterator.next();
        }
        sortBy.remove(lastKey);
    }

    @NonNull
    Map<String, Boolean> getSortBy() {
        return sortBy;
    }

    void addFilterBy(String field, String value) {
        filterBy.put(field, value);
    }

    void removeFilterBy() {
        if (filterBy.isEmpty()) {
            return;
        }
        Iterator<String> keysIterator = filterBy.keySet().iterator();
        String lastKey = null;
        while (keysIterator.hasNext()) {
            lastKey = keysIterator.next();
        }
        filterBy.remove(lastKey);
    }

    Map<String, String> getFilterBy() {
        return filterBy;
    }

    void addFilterQuery(String query) {
        filterQueries.add(query);
    }

    void removeFilterQuery() {
        if (filterQueries.isEmpty()) {
            return;
        }
        filterQueries.remove(filterQueries.size() - 1);
    }

    List<String> getFilterQueries() {
        return filterQueries;
    }

    boolean isIncludeDraft() {
        return includeDraft;
    }

    void setIncludeDraft(boolean includeDraft) {
        this.includeDraft = includeDraft;
    }

    boolean isIncludeRetired() {
        return includeRetired;
    }

    void setIncludeRetired(boolean includeRetired) {
        this.includeRetired = includeRetired;
    }

    boolean isProtectedContent() {
        return protectedContent;
    }

    void setProtectedContent(boolean protectedContent) {
        this.protectedContent = protectedContent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("QueryParameters:");
        sb.append("\n\tstart=").append(start);
        sb.append("\n\trows=").append(rows);
        if (!sortBy.isEmpty()) {
            sb.append("\n\tSort By:");
            for (String key : sortBy.keySet()) {
                sb.append("\n\t\t").append(key).append(":").append(sortBy.get(key));
            }
        }
        if (!filterBy.isEmpty()) {
            sb.append("\n\tFilter By:");
            for (String key : filterBy.keySet()) {
                sb.append("\n\t\t").append(key).append(":").append(filterBy.get(key));
            }
        }
        if (!filterQueries.isEmpty()) {
            sb.append("\n\tFilter Query:");
            for (String query : filterQueries) {
                sb.append("\n\t\t").append(query);
            }
        }
        sb.append("\n\tincludeDraft=").append(includeDraft);
        sb.append("\n\tincludeRetired=").append(includeRetired);
        sb.append("\n\tprotectedContent=").append(protectedContent);

        return sb.toString();
    }

    //Parcelable implementation
    public static Creator<QueryParameters> getCREATOR() {
        return CREATOR;
    }

    protected QueryParameters(Parcel in) {
        start = in.readInt();
        rows = in.readInt();


        //Reading sortBy map
        int sortSize = in.readInt();
        String[] sortKeys = new String[sortSize];
        in.readStringArray(sortKeys);
        boolean[] sortValues = new boolean[sortSize];
        in.readBooleanArray(sortValues);
        for (int i = 0; i < sortSize; i++) {
            sortBy.put(sortKeys[i], sortValues[i]);
        }
        //Reading filterBy map
        int filterBySize = in.readInt();
        String[] filterByKeys = new String[filterBySize];
        in.readStringArray(filterByKeys);
        String[] filterByValues = new String[filterBySize];
        in.readStringArray(filterByValues);
        for (int i = 0; i < filterBySize; i++) {
            filterBy.put(filterByKeys[i], filterByValues[i]);
        }

        //Writing filterQueries list
        int filterQueriesSize = in.readInt();
        String[] filterQueries = new String[filterQueriesSize];
        in.readStringArray(filterQueries);
        for (int i = 0; i < filterQueriesSize; i++) {
            this.filterQueries.add(filterQueries[i]);
        }

        includeDraft = in.readInt() == 1;
        includeRetired = in.readInt() == 1;
        protectedContent = in.readInt() == 1;
    }

    public static final Creator<QueryParameters> CREATOR = new Creator<QueryParameters>() {
        @Override
        public QueryParameters createFromParcel(Parcel in) {
            return new QueryParameters(in);
        }

        @Override
        public QueryParameters[] newArray(int size) {
            return new QueryParameters[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(start);
        parcel.writeInt(rows);
        //Writing sortBy map
        parcel.writeInt(sortBy.size());
        parcel.writeStringArray(sortBy.keySet().toArray(new String[0]));
        parcel.writeBooleanArray(convertToPrimitiveArray(sortBy.values()));

        //Writing filterBy map
        parcel.writeInt(filterBy.size());
        parcel.writeStringArray(filterBy.keySet().toArray(new String[0]));
        parcel.writeStringArray(filterBy.values().toArray(new String[0]));

        //Writing filterQueries list
        parcel.writeInt(filterQueries.size());
        parcel.writeStringArray(filterQueries.toArray(new String[0]));

        parcel.writeInt((includeDraft) ? 1 : 0);
        parcel.writeInt((includeRetired) ? 1 : 0);
        parcel.writeInt((protectedContent) ? 1 : 0);
    }

    private boolean[] convertToPrimitiveArray(Collection<Boolean> source) {
        int valuesArrayIndex = 0;
        boolean[] values = new boolean[source.size()];
        for (Boolean value : source) {
            values[valuesArrayIndex++] = value;
        }
        return values;
    }
}