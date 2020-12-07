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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

class DeliverySearchQueryBuilder implements Parcelable {

    static final String DEFAULT_QUERY_PARAM = "*:*";

    private static final String ASC_VALUE = "asc";
    private static final String DESC_VALUE = "desc";

    private static final String ALL_FIELDS_FIELD_LIST = "*";
    private static final String JSON_DOCUMENT_FIELD_LIST = "document:[json]";

    private static final String INCLUDE_DRAFT_FILTER_QUERY = "status:ready OR status:draft OR draftStatus:*";
    private static final String INCLUDE_RETIRED_FILTER_QUERY = "status:ready OR status:retired";
    private static final String INCLUDE_DRAFT_AND_RETIRED_FILTER_QUERY = "status:ready OR status:draft OR draftStatus:* OR status:retired";

    private String q = DEFAULT_QUERY_PARAM;
    private Set<String> fieldList = new LinkedHashSet<>(2);

    private List<String> filterQueryList = new LinkedList<>();

    //Using LinkedHashMap in order to support sort rules overlapping and ordering. 
    private Map<String, Boolean> sortRules = new LinkedHashMap<>(16, 0.75f, true);

    private Integer start;
    private Integer rows;

    private boolean includeDraft;
    private boolean includeAllFields = true;
    private boolean includeRetired;

    DeliverySearchQueryBuilder() {
    }

    DeliverySearchQueryBuilder(@NonNull DeliverySearchQueryBuilder builder) {
        q = builder.q;
        filterQueryList.addAll(builder.filterQueryList);
        sortRules.putAll(builder.sortRules);
        start = builder.start;
        rows = builder.rows;
        includeDraft = builder.includeDraft;
        includeAllFields = builder.includeAllFields;
        includeRetired = builder.includeRetired;
    }

    DeliverySearchQueryBuilder filterQuery(String field, String value) {
        Validator.checkNotNull(field);
        Validator.checkNotNull(value);
        return filterQuery(field + ":" + value);
    }

    DeliverySearchQueryBuilder filterQuery(String filterQuery) {
        Validator.checkNotNull(filterQuery, "filterQuery cannot be null.");
        filterQueryList.add(filterQuery);
        return this;
    }

    @Nullable
    List<String> getFilterQuery() {
        final List<String> retFilterQuery = new ArrayList<>(filterQueryList);
        if (includeDraft && includeRetired) {
            retFilterQuery.add(INCLUDE_DRAFT_AND_RETIRED_FILTER_QUERY);
        } else if (includeDraft) {
            retFilterQuery.add(INCLUDE_DRAFT_FILTER_QUERY);
        } else if (includeRetired) {
            retFilterQuery.add(INCLUDE_RETIRED_FILTER_QUERY);
        }
        return retFilterQuery;
    }

    DeliverySearchQueryBuilder fieldList(String fieldList) {
        Validator.checkNotNull(fieldList, "fieldQuery cannot be null.");
        this.fieldList.add(fieldList);
        return this;
    }

    List<String> getFieldList() {
        final List<String> retFieldsList = new ArrayList<>(fieldList);
        if (includeAllFields) {
            retFieldsList.add(ALL_FIELDS_FIELD_LIST);
            retFieldsList.add(JSON_DOCUMENT_FIELD_LIST);
        }
        return retFieldsList;
    }

    DeliverySearchQueryBuilder sort(@NonNull String field, boolean asc) {
        Validator.checkNotNull(field);
        sortRules.put(field, asc);
        return this;
    }

    private String getSortOrder(boolean asc) {
        return (asc) ? ASC_VALUE : DESC_VALUE;
    }

    @Nullable
    String getSortString() {
        if (sortRules.isEmpty()) {
            return null;
        }

        StringBuilder sortBuilder = new StringBuilder();
        for (Map.Entry<String, Boolean> rule : sortRules.entrySet()) {
            if (sortBuilder.length() > 0) {
                sortBuilder.append(",");
            }
            sortBuilder.append(rule.getKey())
                    .append(" ")
                    .append(getSortOrder(rule.getValue()));
        }
        return sortBuilder.toString();
    }

    DeliverySearchQueryBuilder query(String query) {
        this.q = Validator.checkNotNull(query);
        return this;
    }

    @NonNull
    String getQString() {
        return q;
    }

    DeliverySearchQueryBuilder start(int start) {
        this.start = Validator.checkCondition(start, "start cannot be less then 0", val -> (val >= 0));
        return this;
    }

    @Nullable
    Integer getStart() {
        return start;
    }

    DeliverySearchQueryBuilder rows(int rows) {
        this.rows = Validator.checkCondition(rows, "rows cannot be less then 1", val -> (val >= 1));
        return this;
    }

    @Nullable
    Integer getRows() {
        return rows;
    }

    DeliverySearchQueryBuilder setIncludeDraft(boolean includeDraft) {
        this.includeDraft = includeDraft;
        return this;
    }

    DeliverySearchQueryBuilder setIncludeAllFields(boolean includeAllFields) {
        this.includeAllFields = includeAllFields;
        return this;
    }

    DeliverySearchQueryBuilder setIncludeRetired(boolean includeRetired) {
        this.includeRetired = includeRetired;
        return this;
    }

    DeliverySearchQuery build() {
        return new DeliverySearchQuery(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeliverySearchQueryBuilder builder = (DeliverySearchQueryBuilder) o;

        if (includeDraft != builder.includeDraft) return false;
        if (includeAllFields != builder.includeAllFields) return false;
        if (includeRetired != builder.includeRetired) return false;
        if (!q.equals(builder.q)) return false;
        if (!fieldList.equals(builder.fieldList)) return false;
        if (!filterQueryList.equals(builder.filterQueryList)) return false;
        if (!sortRules.equals(builder.sortRules)) return false;
        if (start != null ? !start.equals(builder.start) : builder.start != null) return false;
        return rows != null ? rows.equals(builder.rows) : builder.rows == null;
    }

    @Override
    public int hashCode() {
        int result = q.hashCode();
        result = 31 * result + fieldList.hashCode();
        result = 31 * result + filterQueryList.hashCode();
        result = 31 * result + sortRules.hashCode();
        result = 31 * result + (start != null ? start.hashCode() : 0);
        result = 31 * result + (rows != null ? rows.hashCode() : 0);
        result = 31 * result + (includeDraft ? 1 : 0);
        result = 31 * result + (includeAllFields ? 1 : 0);
        result = 31 * result + (includeRetired ? 1 : 0);
        return result;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(q);
        ParcelableUtils.writeArrayListOfStrings(new ArrayList<>(fieldList), dest);
        ParcelableUtils.writeArrayListOfStrings(new ArrayList<>(filterQueryList), dest);

        final int sortRulesSize = sortRules.size();
        dest.writeInt(sortRulesSize);
        for (Map.Entry<String, Boolean> sortRule : sortRules.entrySet()) {
            dest.writeString(sortRule.getKey());
            dest.writeInt(sortRule.getValue() ? 1 : 0);
        }

        dest.writeSerializable(start);
        dest.writeSerializable(rows);

        dest.writeInt(includeDraft ? 1 : 0);
        dest.writeInt(includeAllFields ? 1 : 0);
        dest.writeInt(includeRetired ? 1 : 0);
    }

    private DeliverySearchQueryBuilder(Parcel source) {
        q = source.readString();

        final ArrayList<String> sourceFieldList = ParcelableUtils.readArrayListOfStrings(source);
        if (null != sourceFieldList) {
            fieldList.addAll(sourceFieldList);
        }

        final ArrayList<String> sourceFilterQueryList = ParcelableUtils.readArrayListOfStrings(source);
        if (null != sourceFilterQueryList) {
            filterQueryList.addAll(sourceFilterQueryList);
        }

        final int sourceSortRulesSize = source.readInt();
        for (int i = 0; i < sourceSortRulesSize; i++) {
            sortRules.put(source.readString(), source.readInt() == 1);
        }

        start = (Integer) source.readSerializable();
        rows = (Integer) source.readSerializable();

        includeDraft = source.readInt() == 1;
        includeAllFields = source.readInt() == 1;
        includeRetired = source.readInt() == 1;
    }

    public static final Creator<DeliverySearchQueryBuilder> CREATOR = new Creator<DeliverySearchQueryBuilder>() {
        @Override
        public DeliverySearchQueryBuilder createFromParcel(Parcel source) {
            return new DeliverySearchQueryBuilder(source);
        }

        @Override
        public DeliverySearchQueryBuilder[] newArray(int size) {
            return new DeliverySearchQueryBuilder[size];
        }
    };
}