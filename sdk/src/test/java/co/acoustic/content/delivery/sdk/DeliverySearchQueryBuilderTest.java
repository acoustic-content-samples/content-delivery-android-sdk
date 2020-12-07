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

import org.junit.Test;

import static org.junit.Assert.*;

public class DeliverySearchQueryBuilderTest {
    private final String testQuery = "classification:asset";
    private final String testSortToCompare = "name asc,classification desc";

    @Test
    public void testEmptyConstructor() {
        DeliverySearchQueryBuilder builder = new DeliverySearchQueryBuilder();
        assertNotNull(builder);
    }

    @Test
    public void testCopyConstructor() {
        DeliverySearchQueryBuilder source = DeliverySearchQuery.builder()
                .start(5)
                .rows(10)
                .sort("name", true)
                .filterQuery("popularity:[10 TO *]")
                .filterQuery("section", "0")
                .sort("classification", false);

        DeliverySearchQueryBuilder builder = new DeliverySearchQueryBuilder(source);
        assertNotNull(builder);
        assertEquals("Builders should builder equals DeliverySearchQuery instances ", source.build(), builder.build());
    }

    @Test
    public void testFilterQuery() {
        DeliverySearchQueryBuilder builder = new DeliverySearchQueryBuilder();

        builder.filterQuery("popularity:[10 TO *]");
        assertArrayEquals(new String[]{"popularity:[10 TO *]"}, builder.getFilterQuery().toArray(new String[0]));
        builder.filterQuery("section:0");
        assertArrayEquals(new String[]{"popularity:[10 TO *]", "section:0"}, builder.getFilterQuery().toArray(new String[0]));

        DeliverySearchQueryBuilder builder2 = new DeliverySearchQueryBuilder();

        builder2.filterQuery("popularity", "[10 TO *]");
        assertArrayEquals(new String[]{"popularity:[10 TO *]"}, builder2.getFilterQuery().toArray(new String[0]));
        builder2.filterQuery("section", "0");
        assertArrayEquals(new String[]{"popularity:[10 TO *]", "section:0"}, builder2.getFilterQuery().toArray(new String[0]));
    }

    @Test(expected = NullPointerException.class)
    public void testFilterQueryWithNullFiled() {
        DeliverySearchQueryBuilder builder = new DeliverySearchQueryBuilder();
        builder.filterQuery(null, "[10 TO *]");
    }

    @Test(expected = NullPointerException.class)
    public void testFilterQueryWithNullValue() {
        DeliverySearchQueryBuilder builder = new DeliverySearchQueryBuilder();
        builder.filterQuery("popularity", null);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterQueryWithNullFqParam() {
        DeliverySearchQueryBuilder builder = new DeliverySearchQueryBuilder();
        builder.filterQuery(null);
    }

    @Test
    public void testQuery() {
        DeliverySearchQueryBuilder builder = new DeliverySearchQueryBuilder();

        assertEquals(DeliverySearchQueryBuilder.DEFAULT_QUERY_PARAM, builder.getQString());

        builder.query(testQuery);
        assertEquals(testQuery, builder.getQString());
    }

    @Test(expected = NullPointerException.class)
    public void testQueryWithNull() {
        DeliverySearchQueryBuilder builder = new DeliverySearchQueryBuilder();
        builder.query(null);
    }

    @Test
    public void testFieldList() {
        DeliverySearchQueryBuilder builder = new DeliverySearchQueryBuilder();

        builder.fieldList("tags");
        builder.fieldList("name");
        builder.fieldList("lastModified");


        assertArrayEquals(new String[]{"tags", "name", "lastModified", "*", "document:[json]"}, builder.getFieldList().toArray(new String[0]));
    }

    @Test(expected = NullPointerException.class)
    public void testFieldListWithNullValue() {
        DeliverySearchQueryBuilder builder = new DeliverySearchQueryBuilder();

        builder.fieldList("tags");
        builder.fieldList(null);
        builder.fieldList("lastModified");

        assertArrayEquals(new String[]{"tags", "name", "lastModified", "*", "document:[json]"}, builder.getFieldList().toArray(new String[0]));
    }


    @Test
    public void testSort() {
        DeliverySearchQueryBuilder builder = new DeliverySearchQueryBuilder();

        assertNull(builder.getSortString());

        builder
                .sort("name", true);
        assertEquals("name asc", builder.getSortString());
        builder
                .sort("classification", false);
        assertEquals(testSortToCompare, builder.getSortString());
    }

    @Test
    public void testSortRulesOrdering() {
        DeliverySearchQueryBuilder builder = new DeliverySearchQueryBuilder();

        builder
                .sort("name", true);
        builder
                .sort("classification", false);
        builder
                .sort("name", false);
        assertEquals("classification desc,name desc", builder.getSortString());
        builder
                .sort("classification", true);
        assertEquals("name desc,classification asc", builder.getSortString());
    }

    @Test(expected = NullPointerException.class)
    public void testSortWithNull() {
        DeliverySearchQueryBuilder builder = new DeliverySearchQueryBuilder();

        assertNull(builder.getSortString());

        builder
                .sort(null, true);
    }

    @Test
    public void testStart() {
        DeliverySearchQueryBuilder builder = new DeliverySearchQueryBuilder();

        assertNull(builder.getStart());

        builder.start(5);
        assertEquals(5L, builder.getStart().longValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartWithWrongData() {
        DeliverySearchQueryBuilder builder = new DeliverySearchQueryBuilder();
        builder.start(-1);
    }

    @Test
    public void testRows() {
        DeliverySearchQueryBuilder builder = new DeliverySearchQueryBuilder();

        assertNull(builder.getRows());

        builder.rows(5);
        assertEquals(5L, builder.getRows().longValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRowsWithWrongData() {
        DeliverySearchQueryBuilder builder = new DeliverySearchQueryBuilder();
        builder.rows(-1);
    }

    @Test
    public void testIncludeDraft() {
        final String INCLUDE_DRAFT_FILTER_QUERY = "status:ready OR status:draft OR draftStatus:*";
        final String INCLUDE_DRAFT_AND_RETIRED_FILTER_QUERY = "status:ready OR status:draft OR draftStatus:* OR status:retired";

        DeliverySearchQueryBuilder builder = new DeliverySearchQueryBuilder()
                .filterQuery("popularity", "[10 TO *]")
                .filterQuery("section", "0")
                .setIncludeDraft(false);
        assertArrayEquals(new String[]{"popularity:[10 TO *]", "section:0"}, builder.getFilterQuery().toArray(new String[0]));

        builder.setIncludeDraft(true);
        assertArrayEquals(new String[]{"popularity:[10 TO *]", "section:0", INCLUDE_DRAFT_FILTER_QUERY}, builder.getFilterQuery().toArray(new String[0]));
        builder.setIncludeRetired(true);
        assertArrayEquals(new String[]{"popularity:[10 TO *]", "section:0", INCLUDE_DRAFT_AND_RETIRED_FILTER_QUERY}, builder.getFilterQuery().toArray(new String[0]));
    }

    @Test
    public void testIncludeRetired() {
        final String INCLUDE_RETIRED_FILTER_QUERY = "status:ready OR status:retired";
        final String INCLUDE_DRAFT_AND_RETIRED_FILTER_QUERY = "status:ready OR status:draft OR draftStatus:* OR status:retired";

        DeliverySearchQueryBuilder builder = new DeliverySearchQueryBuilder()
                .filterQuery("popularity", "[10 TO *]")
                .filterQuery("section", "0")
                .setIncludeRetired(false);
        assertArrayEquals(new String[]{"popularity:[10 TO *]", "section:0"}, builder.getFilterQuery().toArray(new String[0]));

        builder.setIncludeRetired(true);
        assertArrayEquals(new String[]{"popularity:[10 TO *]", "section:0", INCLUDE_RETIRED_FILTER_QUERY}, builder.getFilterQuery().toArray(new String[0]));
        builder.setIncludeDraft(true);
        assertArrayEquals(new String[]{"popularity:[10 TO *]", "section:0", INCLUDE_DRAFT_AND_RETIRED_FILTER_QUERY}, builder.getFilterQuery().toArray(new String[0]));
    }

    @Test
    public void testIncludeAllField() {
        DeliverySearchQueryBuilder builder = new DeliverySearchQueryBuilder();
        assertArrayEquals("Default includeAllFields should be true ", new String[]{"*", "document:[json]"}, builder.getFieldList().toArray(new String[0]));

        builder.setIncludeAllFields(false);
        assertArrayEquals(new String[]{}, builder.getFieldList().toArray(new String[0]));

        builder.setIncludeAllFields(true);
        assertArrayEquals("includeAllFields is true ", new String[]{"*", "document:[json]"}, builder.getFieldList().toArray(new String[0]));
    }

    @Test
    public void testParcelableDescribeContents() {
        DeliverySearchQueryBuilder builder = new DeliverySearchQueryBuilder();
        assertEquals(0, builder.describeContents());
    }

    @Test
    public void testParcelable() {
        DeliverySearchQueryBuilder source = new DeliverySearchQueryBuilder()
                .filterQuery("popularity", "[10 TO *]")
                .filterQuery("section", "0")
                .setIncludeDraft(true)
                .setIncludeAllFields(true)
                .setIncludeRetired(true)
                .start(5)
                .rows(10)
                .sort("name", false)
                .sort("category", true);

        Parcel parcel = MockParcel.obtain();
        source.writeToParcel(parcel, source.describeContents());
        parcel.setDataPosition(0);

        DeliverySearchQueryBuilder createdFromParcel = DeliverySearchQueryBuilder.CREATOR.createFromParcel(parcel);
        assertEquals("Builders should builder equals DeliverySearchQuery instances ", source.build(), createdFromParcel.build());

        final int testArraySize = 2;
        DeliverySearchQueryBuilder[] testArray = DeliverySearchQueryBuilder.CREATOR.newArray(testArraySize);
        assertNotNull(testArray);
        assertEquals(testArraySize, testArray.length);
    }

    @Test
    public void testEquals() {
        /*
         * Test two equal objects
         */
        DeliverySearchQueryBuilder builder01 = new DeliverySearchQueryBuilder()
                .filterQuery("popularity", "[10 TO *]")
                .filterQuery("section", "0")
                .setIncludeDraft(true)
                .setIncludeAllFields(true)
                .setIncludeRetired(true)
                .start(5)
                .rows(10)
                .sort("name", false)
                .sort("category", true);

        DeliverySearchQueryBuilder builder02 = new DeliverySearchQueryBuilder(builder01);
        assertTrue(builder01.equals(builder02));

        /*
         * Test two objects with different query
         */
        DeliverySearchQueryBuilder builder11 = new DeliverySearchQueryBuilder()
                .filterQuery("popularity", "[10 TO *]")
                .filterQuery("section", "0")
                .setIncludeDraft(true)
                .setIncludeAllFields(true)
                .setIncludeRetired(true)
                .start(5)
                .rows(10)
                .sort("name", false)
                .sort("category", true);

        DeliverySearchQueryBuilder builder12 = new DeliverySearchQueryBuilder(builder11)
                .query("test");
        assertFalse(builder11.equals(builder12));

        /*
         * Test two objects with different query filter
         */
        DeliverySearchQueryBuilder builder21 = new DeliverySearchQueryBuilder()
                .filterQuery("popularity", "[10 TO *]")
                .setIncludeDraft(true)
                .setIncludeAllFields(true)
                .setIncludeRetired(true)
                .start(5)
                .rows(10)
                .sort("name", false)
                .sort("category", true);

        DeliverySearchQueryBuilder builder22 = new DeliverySearchQueryBuilder(builder21)
                .filterQuery("section", "0");
        assertFalse(builder21.equals(builder22));

        /*
         * Test two objects with different field list
         */
        DeliverySearchQueryBuilder builder31 = new DeliverySearchQueryBuilder()
                .filterQuery("popularity", "[10 TO *]")
                .filterQuery("section", "0")
                .setIncludeDraft(true)
                .setIncludeAllFields(true)
                .setIncludeRetired(true)
                .start(5)
                .rows(10)
                .sort("name", false)
                .sort("category", true);

        DeliverySearchQueryBuilder builder32 = new DeliverySearchQueryBuilder(builder31)
                .fieldList("tags");
        assertFalse(builder31.equals(builder32));

        /*
         * Test two objects with different sort rules
         */
        DeliverySearchQueryBuilder builder41 = new DeliverySearchQueryBuilder()
                .filterQuery("popularity", "[10 TO *]")
                .filterQuery("section", "0")
                .setIncludeDraft(true)
                .setIncludeAllFields(true)
                .setIncludeRetired(true)
                .start(5)
                .rows(10)
                .sort("category", true);

        DeliverySearchQueryBuilder builder42 = new DeliverySearchQueryBuilder(builder41)
                .sort("name", false);
        assertFalse(builder41.equals(builder42));

        /*
         * Test two objects with different start
         */
        DeliverySearchQueryBuilder builder51 = new DeliverySearchQueryBuilder()
                .filterQuery("popularity", "[10 TO *]")
                .filterQuery("section", "0")
                .setIncludeDraft(true)
                .setIncludeAllFields(true)
                .setIncludeRetired(true)
                .start(5)
                .rows(10)
                .sort("category", true);

        DeliverySearchQueryBuilder builder52 = new DeliverySearchQueryBuilder(builder51)
                .start(0);
        assertFalse(builder51.equals(builder52));

        /*
         * Test two objects with different rows
         */
        DeliverySearchQueryBuilder builder61 = new DeliverySearchQueryBuilder()
                .filterQuery("popularity", "[10 TO *]")
                .filterQuery("section", "0")
                .setIncludeDraft(true)
                .setIncludeAllFields(true)
                .setIncludeRetired(true)
                .start(5)
                .rows(10)
                .sort("category", true);

        DeliverySearchQueryBuilder builder62 = new DeliverySearchQueryBuilder(builder61)
                .rows(25);
        assertFalse(builder61.equals(builder62));

        /*
         * Test two objects with different include draft
         */
        DeliverySearchQueryBuilder builder71 = new DeliverySearchQueryBuilder()
                .filterQuery("popularity", "[10 TO *]")
                .filterQuery("section", "0")
                .setIncludeDraft(true)
                .setIncludeAllFields(true)
                .setIncludeRetired(true)
                .start(5)
                .rows(10)
                .sort("category", true);

        DeliverySearchQueryBuilder builder72 = new DeliverySearchQueryBuilder(builder71)
                .setIncludeDraft(false);
        assertFalse(builder71.equals(builder72));

        /*
         * Test two objects with different include all fields
         */
        DeliverySearchQueryBuilder builder81 = new DeliverySearchQueryBuilder()
                .filterQuery("popularity", "[10 TO *]")
                .filterQuery("section", "0")
                .setIncludeDraft(true)
                .setIncludeAllFields(true)
                .setIncludeRetired(true)
                .start(5)
                .rows(10)
                .sort("category", true);

        DeliverySearchQueryBuilder builder82 = new DeliverySearchQueryBuilder(builder81)
                .setIncludeAllFields(false);
        assertFalse(builder81.equals(builder82));

        /*
         * Test two objects with different include retired
         */
        DeliverySearchQueryBuilder builder91 = new DeliverySearchQueryBuilder()
                .filterQuery("popularity", "[10 TO *]")
                .filterQuery("section", "0")
                .setIncludeDraft(true)
                .setIncludeAllFields(true)
                .setIncludeRetired(true)
                .start(5)
                .rows(10)
                .sort("category", true);

        DeliverySearchQueryBuilder builder92 = new DeliverySearchQueryBuilder(builder91)
                .setIncludeRetired(false);
        assertFalse(builder91.equals(builder92));
    }

    @Test
    public void testHashCode() {
        /*
         * Test hashCode() same for equal objects.
         */
        DeliverySearchQueryBuilder builder01 = new DeliverySearchQueryBuilder()
                .filterQuery("popularity", "[10 TO *]")
                .filterQuery("section", "0")
                .setIncludeDraft(true)
                .setIncludeAllFields(true)
                .setIncludeRetired(true)
                .start(5)
                .rows(10)
                .sort("name", false)
                .sort("category", true);

        DeliverySearchQueryBuilder builder02 = new DeliverySearchQueryBuilder(builder01);
        assertEquals(builder01.hashCode(), builder02.hashCode());

        /*
         * Test hashCode() different for objects with different query
         */
        DeliverySearchQueryBuilder builder11 = new DeliverySearchQueryBuilder()
                .filterQuery("popularity", "[10 TO *]")
                .filterQuery("section", "0")
                .setIncludeDraft(true)
                .setIncludeAllFields(true)
                .setIncludeRetired(true)
                .start(5)
                .rows(10)
                .sort("name", false)
                .sort("category", true);

        DeliverySearchQueryBuilder builder12 = new DeliverySearchQueryBuilder(builder11)
                .query("test");
        assertNotEquals(builder11.hashCode(), builder12.hashCode());

        /*
         * Test hashCode() different for objects with different query filter
         */
        DeliverySearchQueryBuilder builder21 = new DeliverySearchQueryBuilder()
                .filterQuery("popularity", "[10 TO *]")
                .setIncludeDraft(true)
                .setIncludeAllFields(true)
                .setIncludeRetired(true)
                .start(5)
                .rows(10)
                .sort("name", false)
                .sort("category", true);

        DeliverySearchQueryBuilder builder22 = new DeliverySearchQueryBuilder(builder21)
                .filterQuery("section", "0");
        assertNotEquals(builder21.hashCode(), builder22.hashCode());

        /*
         * Test hashCode() different for objects with different field list
         */
        DeliverySearchQueryBuilder builder31 = new DeliverySearchQueryBuilder()
                .filterQuery("popularity", "[10 TO *]")
                .filterQuery("section", "0")
                .setIncludeDraft(true)
                .setIncludeAllFields(true)
                .setIncludeRetired(true)
                .start(5)
                .rows(10)
                .sort("name", false)
                .sort("category", true);

        DeliverySearchQueryBuilder builder32 = new DeliverySearchQueryBuilder(builder31)
                .fieldList("tags");
        assertNotEquals(builder31.hashCode(), builder32.hashCode());

        /*
         * Test hashCode() different for objects with different sort rules
         */
        DeliverySearchQueryBuilder builder41 = new DeliverySearchQueryBuilder()
                .filterQuery("popularity", "[10 TO *]")
                .filterQuery("section", "0")
                .setIncludeDraft(true)
                .setIncludeAllFields(true)
                .setIncludeRetired(true)
                .start(5)
                .rows(10)
                .sort("category", true);

        DeliverySearchQueryBuilder builder42 = new DeliverySearchQueryBuilder(builder41)
                .sort("name", false);
        assertNotEquals(builder41.hashCode(), builder42.hashCode());

        /*
         * Test hashCode() different for objects with different start
         */
        DeliverySearchQueryBuilder builder51 = new DeliverySearchQueryBuilder()
                .filterQuery("popularity", "[10 TO *]")
                .filterQuery("section", "0")
                .setIncludeDraft(true)
                .setIncludeAllFields(true)
                .setIncludeRetired(true)
                .start(5)
                .rows(10)
                .sort("category", true);

        DeliverySearchQueryBuilder builder52 = new DeliverySearchQueryBuilder(builder51)
                .start(0);
        assertNotEquals(builder51.hashCode(), builder52.hashCode());

        /*
         * Test hashCode() different for objects with different rows
         */
        DeliverySearchQueryBuilder builder61 = new DeliverySearchQueryBuilder()
                .filterQuery("popularity", "[10 TO *]")
                .filterQuery("section", "0")
                .setIncludeDraft(true)
                .setIncludeAllFields(true)
                .setIncludeRetired(true)
                .start(5)
                .rows(10)
                .sort("category", true);

        DeliverySearchQueryBuilder builder62 = new DeliverySearchQueryBuilder(builder61)
                .rows(25);
        assertNotEquals(builder61.hashCode(), builder62.hashCode());

        /*
         * Test hashCode() different for objects with different draft
         */
        DeliverySearchQueryBuilder builder71 = new DeliverySearchQueryBuilder()
                .filterQuery("popularity", "[10 TO *]")
                .filterQuery("section", "0")
                .setIncludeDraft(true)
                .setIncludeAllFields(true)
                .setIncludeRetired(true)
                .start(5)
                .rows(10)
                .sort("category", true);

        DeliverySearchQueryBuilder builder72 = new DeliverySearchQueryBuilder(builder71)
                .setIncludeDraft(false);
        assertNotEquals(builder71.hashCode(), builder72.hashCode());

        /*
         * Test hashCode() different for objects with different include all fields
         */
        DeliverySearchQueryBuilder builder81 = new DeliverySearchQueryBuilder()
                .filterQuery("popularity", "[10 TO *]")
                .filterQuery("section", "0")
                .setIncludeDraft(true)
                .setIncludeAllFields(true)
                .setIncludeRetired(true)
                .start(5)
                .rows(10)
                .sort("category", true);

        DeliverySearchQueryBuilder builder82 = new DeliverySearchQueryBuilder(builder81)
                .setIncludeAllFields(false);
        assertNotEquals(builder81.hashCode(), builder82.hashCode());

        /*
         * Test hashCode() different for objects with different include retired
         */
        DeliverySearchQueryBuilder builder91 = new DeliverySearchQueryBuilder()
                .filterQuery("popularity", "[10 TO *]")
                .filterQuery("section", "0")
                .setIncludeDraft(true)
                .setIncludeAllFields(true)
                .setIncludeRetired(true)
                .start(5)
                .rows(10)
                .sort("category", true);

        DeliverySearchQueryBuilder builder92 = new DeliverySearchQueryBuilder(builder91)
                .setIncludeRetired(false);
        assertFalse(builder91.equals(builder92));
        assertNotEquals(builder91.hashCode(), builder92.hashCode());
    }
}