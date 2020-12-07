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

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static co.acoustic.content.delivery.sdk.DeliverySearchQueryBuilder.DEFAULT_QUERY_PARAM;
import static org.junit.Assert.*;

public class DeliverySearchQueryTest {
    private static final String ALL_FIELDS_FIELD_LIST = "*";
    private static final String JSON_DOCUMENT_FIELD_LIST = "document:[json]";

    private final List<String> defaultFlToCompare = new ArrayList<>(2);
    {
        defaultFlToCompare.add(ALL_FIELDS_FIELD_LIST);
        defaultFlToCompare.add(JSON_DOCUMENT_FIELD_LIST);
    }


    private final String testQuery = "classification:asset";

    private final List<String> testFQ = new ArrayList<>();
    private final String[] testFQToCompare = new String[] {"popularity:[10 TO *]", "section:0"};

    private final Map<String, Boolean> testSort = new LinkedHashMap<>();
    private final String testSortToCompare = "name asc,classification desc";

    private final int testStart = 10;
    private final int testRows = 5;

    private int hashCodeToCompare;

    @Before
    public void setup() {
        testFQ.add("popularity:[10 TO *]");
        testFQ.add("section:0");

        testSort.put("name", true);
        testSort.put("classification", false);

        hashCodeToCompare = testQuery.hashCode();
        hashCodeToCompare = 31 * hashCodeToCompare + testFQ.hashCode();
        hashCodeToCompare = 31 * hashCodeToCompare + defaultFlToCompare.hashCode();
        hashCodeToCompare = 31 * hashCodeToCompare + testSortToCompare.hashCode();
        hashCodeToCompare = 31 * hashCodeToCompare + Integer.valueOf(testStart).hashCode();
        hashCodeToCompare = 31 * hashCodeToCompare + Integer.valueOf(testRows).hashCode();
    }

    @Test
    public void testBuilder() {
        DeliverySearchQueryBuilder builder = DeliverySearchQuery.builder();
        assertNotNull(builder);
    }

    @Test
    public void testBuilderConstructorWithNulls() {
        DeliverySearchQuery query = DeliverySearchQuery.builder().build();
        assertEquals(DEFAULT_QUERY_PARAM, query.q);
        assertArrayEquals(new String[] {}, query.fq.toArray(new String[0]));
        assertEquals(defaultFlToCompare, query.fl);
        assertNull("sort field should be null", query.sort);
        assertNull("start field should be null", query.start);
        assertNull("rows field should be null", query.rows);
    }

    @Test
    public void testBuilderConstructorWithAppropriateData() {
        final DeliverySearchQuery query = createBuilderWithTestData(testQuery, testFQ, testSort, testStart, testRows).build();

        assertEquals(testQuery, query.q);
        assertArrayEquals(testFQToCompare, query.fq.toArray(new String[0]));
        assertEquals(defaultFlToCompare, query.fl);
        assertEquals(testSortToCompare, query.sort);
        assertEquals(Integer.valueOf(testStart), query.start);
        assertEquals(Integer.valueOf(testRows), query.rows);
    }

    @Test
    public void testToString() {
        final String toCompare1 = "DeliverySearchQuery{" +
                "q='" + testQuery + '\'' +
                ", fq=" + testFQ +
                ", fl=" + defaultFlToCompare +
                ", sort=" + testSortToCompare +
                ", start=" + testStart +
                ", rows=" + testRows +
                '}';

        assertEquals(toCompare1,
                createBuilderWithTestData(testQuery, testFQ, testSort, testStart, testRows).build().toString());

        final String toCompare2 = "DeliverySearchQuery{" +
                "q='" + DEFAULT_QUERY_PARAM + '\'' +
                ", fq=" + "[]" +
                ", fl=" + defaultFlToCompare +
                ", sort=" + null +
                ", start=" + null +
                ", rows=" + null +
                '}';

        assertEquals(toCompare2, DeliverySearchQuery.builder().build().toString());
    }

    @Test
    public void testEquals() {
        final DeliverySearchQuery query01 = DeliverySearchQuery.builder().build();
        final DeliverySearchQuery query02 = DeliverySearchQuery.builder().build();
        assertEquals(query01, query02);

        final DeliverySearchQuery query11 =
                createBuilderWithTestData(testQuery, testFQ, testSort, testStart, testRows).build();
        final DeliverySearchQuery query12 =
                createBuilderWithTestData(testQuery, testFQ, testSort, testStart, testRows).build();
        assertEquals(query11, query12);

        final DeliverySearchQuery query21 =
                createBuilderWithTestData(testQuery, testFQ, testSort, testStart, testRows).build();
        final DeliverySearchQuery query22 =
                createBuilderWithTestData(null, testFQ, testSort, testStart, testRows).build();
        assertNotEquals(query21, query22);

        final DeliverySearchQuery query31 =
                createBuilderWithTestData(testQuery, testFQ, testSort, testStart, testRows).build();
        final DeliverySearchQuery query32 =
                createBuilderWithTestData(testQuery, null, testSort, testStart, testRows).build();
        assertNotEquals(query31, query32);


        final DeliverySearchQuery query41 =
                createBuilderWithTestData(testQuery, testFQ, testSort, testStart, testRows).build();
        final DeliverySearchQuery query42 =
                createBuilderWithTestData(testQuery, testFQ,  null, testStart, testRows).build();
        assertNotEquals(query41, query42);

        final DeliverySearchQuery query51 =
                createBuilderWithTestData(testQuery, testFQ, testSort, testStart, testRows).build();
        final DeliverySearchQuery query52 =
                createBuilderWithTestData(testQuery, testFQ, testSort, null, testRows).build();
        assertNotEquals(query51, query52);

        final DeliverySearchQuery query61 =
                createBuilderWithTestData(testQuery, testFQ, testSort, testStart, testRows).build();
        final DeliverySearchQuery query62 =
                createBuilderWithTestData(testQuery, testFQ, testSort, testStart, null).build();
        assertNotEquals(query61, query62);
    }

    @Test
    public void testHashCodeEquals() {
        DeliverySearchQuery query = createBuilderWithTestData(testQuery, testFQ, testSort, testStart, testRows).build();
        assertEquals("hash codes should be equals", hashCodeToCompare, query.hashCode());
    }

    @Test
    public void testHashCodeNotEquals() {
        DeliverySearchQuery query = createBuilderWithTestData(testQuery, testFQ, testSort, testStart, null).build();
        assertNotEquals("hash codes should be equals", hashCodeToCompare, query.hashCode());
    }

    private DeliverySearchQueryBuilder createBuilderWithTestData(String testQuery, List<String> testFQ, Map<String, Boolean> testSort, Integer testStart, Integer testRows) {
        final DeliverySearchQueryBuilder builder = DeliverySearchQuery.builder();
        if (testQuery != null) {
            builder.query(testQuery);
        }
        if (testStart != null) {
            builder.start(testStart);
        }
        if (testRows != null) {
            builder.rows(testRows);
        }
        if (testFQ != null) {
            for (String fq : testFQ) {
                builder.filterQuery(fq);
            }
        }
        if (testSort != null) {
            for (Map.Entry<String, Boolean> entry : testSort.entrySet()) {
                builder.sort(entry.getKey(), entry.getValue());
            }
        }
        return builder;
    }
}