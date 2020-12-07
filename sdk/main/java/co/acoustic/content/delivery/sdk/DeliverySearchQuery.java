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

import java.util.List;

class DeliverySearchQuery {

    final String q;
    final List<String> fq;
    final List<String> fl;
    final String sort;
    final Integer start;
    final Integer rows;

    DeliverySearchQuery(DeliverySearchQueryBuilder builder) {
        this.q = builder.getQString();
        this.fq = builder.getFilterQuery();
        this.fl = builder.getFieldList();
        this.sort = builder.getSortString();
        this.start = builder.getStart();
        this.rows = builder.getRows();
    }

    /**
     * @return the instance of {@link DeliverySearchQueryBuilder} to build query.
     */
    static DeliverySearchQueryBuilder builder() {
        return new DeliverySearchQueryBuilder();
    }

    @Override
    public String toString() {
        return "DeliverySearchQuery{" +
                "q='" + q + '\'' +
                ", fq=" + fq +
                ", fl=" + fl +
                ", sort=" + sort +
                ", start=" + start +
                ", rows=" + rows +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeliverySearchQuery that = (DeliverySearchQuery) o;

        if (start != null ? !start.equals(that.start) : that.start != null) return false;
        if (rows != null ? !rows.equals(that.rows) : that.rows != null) return false;
        if (q != null ? !q.equals(that.q) : that.q != null) return false;
        if (fq != null ? !fq.equals(that.fq) : that.fq != null) return false;
        if (fl != null ? !fl.equals(that.fl) : that.fl != null) return false;
        return sort != null ? sort.equals(that.sort) : that.sort == null;
    }

    @Override
    public int hashCode() {
        int result = q != null ? q.hashCode() : 0;
        result = 31 * result + (fq != null ? fq.hashCode() : 0);
        result = 31 * result + (fl != null ? fl.hashCode() : 0);
        result = 31 * result + (sort != null ? sort.hashCode() : 0);
        result = 31 * result + (start != null ? start.hashCode() : 0);
        result = 31 * result + (rows != null ? rows.hashCode() : 0);
        return result;
    }
}
