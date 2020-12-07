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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DocumentsTest extends BaseDocumentsTest{

    @Test
    public void testCreationWithoutState() {
        final DeliverySearch deliverySearch = ContentDeliverySDK
                .create(SDKConfig.builder().setApiUrl("https://my7.test.com/").build())
                .deliverySearch();

        DocumentsTestImpl documents = new DocumentsTestImpl(deliverySearch, null);
        assertNotNull(documents);
    }

    @Test
    public void testCreationWithState() {
        final DeliverySearch deliverySearch = mock(DeliverySearch.class);
        final Documents.State mockState = mock(Documents.State.class);

        DocumentsTestImpl documents = new DocumentsTestImpl(deliverySearch, mockState);

        verify(mockState).getDeliverySearchQueryBuilder();
        verify(mockState).isIncludeDraft();
        verify(mockState).isIncludeProtectedContent();
        verify(mockState).isRetrieveCompleteContentContext();
        verify(mockState).isIncludeAllFields();
        verify(mockState).isIncludeRetired();
        assertNotNull(documents);
    }

    @Test
    public void testCreationWithInjectedQueryBuilder() {
        final DeliverySearch deliverySearch = mock(DeliverySearch.class);
        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);

        DocumentsTestImpl documents = new DocumentsTestImpl(deliverySearch, null, queryBuilder);

        assertNotNull(documents);
    }

    @Test
    public void testCreationWithInjectedQueryBuilderAndState() {
        final DeliverySearch deliverySearch = mock(DeliverySearch.class);
        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        final Documents.State mockState = mock(Documents.State.class);

        DocumentsTestImpl documents = new DocumentsTestImpl(deliverySearch, mockState, queryBuilder);
        verify(mockState).isIncludeDraft();
        verify(mockState).isIncludeProtectedContent();
        verify(mockState).isRetrieveCompleteContentContext();
        verify(mockState).isIncludeAllFields();
        verify(mockState).isIncludeRetired();
        assertNotNull(documents);
    }

    @Test
    public void testSortBy() {
        final String field = "category";
        final boolean asc = false;

        final DeliverySearch deliverySearch = mock(DeliverySearch.class);
        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);

        DocumentsTestImpl documents = new DocumentsTestImpl(deliverySearch, null, queryBuilder);
        documents.sortBy(field, asc);
        verify(queryBuilder).sort(field, asc);
    }

    @Test(expected = NullPointerException.class)
    public void testSortWithNullField() {
        final String field = null;
        final boolean asc = false;

        final DeliverySearch deliverySearch = mock(DeliverySearch.class);
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();

        DocumentsTestImpl documents = new DocumentsTestImpl(deliverySearch, null, queryBuilder);
        documents.sortBy(field, asc);
    }

    @Test
    public void testFilterBy() {
        final String field = "name";
        final String value = "n*";

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);

        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterBy(field, value);
        verify(queryBuilder).filterQuery(field, value);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterByWithNullField() {
        final String field = null;
        final String value = "n*";

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();

        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterBy(field, value);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterByWithNullValue() {
        final String field = "name";
        final String value = null;

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();

        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterBy(field, value);
    }

    @Test
    public void testFilterQuery() {
        final String query = "popularity : [10 TO *]";

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterQuery(query);
        verify(queryBuilder).filterQuery(query);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterQueryWithNullQuery() {
        final String query = null;

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();

        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterQuery(query);
    }

    @Test
    public void testSearchByText() {
        final String text = "classification:asset";

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.searchByText(text);
        verify(queryBuilder).query(text);
    }

    @Test(expected = NullPointerException.class)
    public void testSearchByTextWithNull() {
        final String text = null;

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();

        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.searchByText(text);
    }

    @Test
    public void testRows() {
        final int rows = 15;

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.rows(rows);
        verify(queryBuilder).rows(rows);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRowsLessThen1() {
        final int rows = 0;

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.rows(rows);
    }

    @Test
    public void testStart() {
        final int start = 15;

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.start(start);
        verify(queryBuilder).start(start);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartLessThen0() {
        final int start = -1;

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.rows(start);
    }

    @Test
    public void TestFilterByName() {
        final String value = "footerConfig";

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterByName(value);
        verify(queryBuilder).filterQuery("name", value);
    }

    @Test
    public void TestFilterById() {
        final String id = "ae72d304-ad18-4bf3-b213-4a79c829e458";

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterById(id);
        verify(queryBuilder).filterQuery("id", id);
    }

    @Test
    public void TestFilterByCategories() {
        final String value = "\"Dynamic list criteria/Type/Design page\", \"List ordering options/By date descending\"";

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterByCategories(value);
        verify(queryBuilder).filterQuery("categories", "("+value+")");
    }

    @Test (expected = NullPointerException.class)
    public void TestFilterByCategoriesWithNull() {
        final String categories = null;

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterByCategories(categories);
    }

    @Test (expected = IllegalArgumentException.class)
    public void TestFilterByCategoriesWithEmpty() {
        final String categories = "";

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterByCategories(categories);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestFilterByTagsWithNull() {
        final String[] value = new String[] { "website template", null };

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterByTags(value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestFilterByTagsWithEmpty() {
        final String[] value = new String[] { "website template", "" };

        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterByTags(value);
    }

    @Test
    public void TestFilterByTags() {
        final String[] value = new String[] { "website template", "IBM sample" };

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterByTags(value);
        verify(queryBuilder).filterQuery("tags", "(website template OR IBM sample)");
    }

    @Test
    public void TestFilterByTagsWithMoreThen2Tags() {
        final String[] value = new String[] { "website template", "IBM sample", "Tag1", "Tag4" };

        final DeliverySearchQueryBuilder queryBuilder = mock(DeliverySearchQueryBuilder.class);
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.filterByTags(value);
        verify(queryBuilder).filterQuery("tags", "(website template OR IBM sample OR Tag1 OR Tag4)");
    }

    @Test
    public void TestGetState() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        Documents.State state = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder).getState();
        assertNotNull(state);
    }

    @Test
    public void TestIncludeDraftTrue() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.setIncludeDraft(true);

        Documents.State state = documents.getState();
        assertTrue(state.isIncludeDraft());
    }

    @Test
    public void TestIncludeDraftFalse() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.setIncludeDraft(false);

        Documents.State state = documents.getState();
        assertFalse(state.isIncludeDraft());
    }

    @Test
    public void TestIncludeProtectedContentTrue() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.setIncludeProtectedContent(true);

        Documents.State state = documents.getState();
        assertTrue(state.isIncludeProtectedContent());
    }

    @Test
    public void TestIncludeProtectedContentFalse() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.setIncludeProtectedContent(false);

        Documents.State state = documents.getState();
        assertFalse(state.isIncludeProtectedContent());
    }

    @Test
    public void TestRetrieveCompleteContentContextTrue() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.setRetrieveCompleteContentContext(true);

        Documents.State state = documents.getState();
        assertTrue(state.isRetrieveCompleteContentContext());
    }

    @Test
    public void TestRetrieveCompleteContentContextFalse() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.setRetrieveCompleteContentContext(false);

        Documents.State state = documents.getState();
        assertFalse(state.isRetrieveCompleteContentContext());
    }

    @Test
    public void TestIncludeAllFieldsTrue() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.setIncludeAllFields(true);

        Documents.State state = documents.getState();
        assertTrue(state.isIncludeAllFields());
    }

    @Test
    public void TestIncludeAllFieldsFalse() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.setIncludeAllFields(false);

        Documents.State state = documents.getState();
        assertFalse(state.isIncludeAllFields());
    }

    @Test
    public void TestIncludeRetiredTrue() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.setIncludeRetired(true);

        Documents.State state = documents.getState();
        assertTrue(state.isIncludeRetired());
    }

    @Test
    public void TestIncludeRetiredFalse() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.setIncludeRetired(false);

        Documents.State state = documents.getState();
        assertFalse(state.isIncludeRetired());
    }

    @Test
    public void testCreatePrevPageInstance() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.start(10);
        documents.rows(5);
        DocumentsTestImpl prevPage = (DocumentsTestImpl) documents.createPrevPageInstance();
        assertNotNull("previousPage should not be null", prevPage);

        Documents.State state = prevPage.getState();
        Integer prevPageStart = state.getDeliverySearchQueryBuilder().getStart();
        assertNotNull("prev page start should not be null", prevPageStart);
        assertEquals("prev page start should be 5", 5, prevPageStart.intValue());
    }

    @Test
    public void testCreatePrevPageForStart0() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.start(0);
        DocumentsTestImpl prevPage = (DocumentsTestImpl) documents.createPrevPageInstance();
        assertNull("previousPage for Documents that starts from 0 should be null", prevPage);
    }

    @Test
    public void testCreatePrevPageForStartNull() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        DocumentsTestImpl prevPage = (DocumentsTestImpl) documents.createPrevPageInstance();
        assertNull("previousPage for Documents that start is null should be null", prevPage);
    }

    @Test
    public void testCreatePrevPageForRowsNull() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.start(5);
        DocumentsTestImpl prevPage = (DocumentsTestImpl) documents.createPrevPageInstance();

        Documents.State state = prevPage.getState();
        Integer prevPageRows = state.getDeliverySearchQueryBuilder().getRows();
        assertNotNull("prev page rows should not be null", prevPageRows);
        assertEquals("prev page rows should be default - 10", 10, prevPageRows.intValue());
    }

    @Test
    public void testCreateNextPageInstance() {
        final int testStart = 10;
        final int testRows = 5;
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.start(testStart);
        documents.rows(testRows);
        DocumentsTestImpl nextPage = (DocumentsTestImpl) documents.createNextPageInstance();
        assertNotNull("nextPage should not be null", nextPage);

        Documents.State state = nextPage.getState();
        Integer nextPageStart = state.getDeliverySearchQueryBuilder().getStart();
        assertNotNull("next Page start should not be null", nextPageStart);
        assertEquals("next Page start should be equal to source.start + source.rows ", testStart + testRows, nextPageStart.intValue());
    }

    @Test
    public void testCreateNextPageForStartNull() {
        final int testRows = 15;
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.rows(testRows);

        DocumentsTestImpl nextPage = (DocumentsTestImpl) documents.createNextPageInstance();
        assertNotNull("nextPage should not be null", nextPage);

        Documents.State state = nextPage.getState();
        Integer nextPageStart = state.getDeliverySearchQueryBuilder().getStart();
        assertEquals("next Page start should be equal to source rows ", testRows, nextPageStart.intValue());
    }

    @Test
    public void testCreateNextPageForRowsNull() {
        final int testStart = 15;
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.start(testStart);

        DocumentsTestImpl nextPage = (DocumentsTestImpl) documents.createNextPageInstance();
        assertNotNull("nextPage should not be null", nextPage);

        Documents.State state = nextPage.getState();
        Integer nextPageStart = state.getDeliverySearchQueryBuilder().getStart();
        assertEquals("next Page start should be equal to source start + defaultRows(10) ", testStart + 10, nextPageStart.intValue());
    }

    @Test
    public void TestStateGetDeliverySearchQueryBuilder() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        Documents.State state = documents.getState();
        assertEquals(queryBuilder, state.getDeliverySearchQueryBuilder());
    }

    @Test
    public void TestStateParcelable() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        DocumentsTestImpl documents = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents.setIncludeRetired(true);
        documents.setIncludeDraft(true);
        documents.setIncludeAllFields(true);
        documents.setIncludeProtectedContent(true);
        documents.setRetrieveCompleteContentContext(true);
        Documents.State source = documents.getState();


        Parcel parcel = MockParcel.obtain();
        source.writeToParcel(parcel, source.describeContents());
        parcel.setDataPosition(0);

        Documents.State createdFromParcel = Documents.State.CREATOR.createFromParcel(parcel);

        assertEquals("Builders should builder equals Documents.State instances ", source, createdFromParcel);
        final int testArraySize = 2;
        Documents.State[] testArray = Documents.State.CREATOR.newArray(testArraySize);
        assertNotNull(testArray);
        assertEquals(testArraySize, testArray.length);
    }

    @Test
    public void TestStateEquals() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        DocumentsTestImpl documents1 = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents1.setIncludeRetired(true);
        documents1.setIncludeDraft(true);
        documents1.setIncludeAllFields(true);
        documents1.setIncludeProtectedContent(true);
        documents1.setRetrieveCompleteContentContext(true);
        Documents.State state1 = documents1.getState();

        DocumentsTestImpl documents2 = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents2.setIncludeRetired(true);
        documents2.setIncludeDraft(true);
        documents2.setIncludeAllFields(true);
        documents2.setIncludeProtectedContent(true);
        documents2.setRetrieveCompleteContentContext(true);
        Documents.State state2 = documents2.getState();
        assertEquals("two states for the same documents should be equals", state1, state2);

        DocumentsTestImpl documents1_1 = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents1_1.setIncludeRetired(true);
        Documents.State state1_1 = documents1_1.getState();

        DocumentsTestImpl documents1_2 = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents1_2.setIncludeRetired(false);
        Documents.State state1_2 = documents1_2.getState();
        assertNotEquals("two states with different isIncludeRetired, should not be equals", state1_1, state1_2);

        DocumentsTestImpl documents2_1 = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents2_1.setIncludeDraft(true);
        Documents.State state2_1 = documents2_1.getState();

        DocumentsTestImpl documents2_2 = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents2_2.setIncludeDraft(false);
        Documents.State state2_2 = documents2_2.getState();
        assertNotEquals("two states with different isIncludeDraft, should not be equals", state2_1, state2_2);

        DocumentsTestImpl documents3_1 = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents3_1.setIncludeAllFields(true);
        Documents.State state3_1 = documents3_1.getState();

        DocumentsTestImpl documents3_2 = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents3_2.setIncludeAllFields(false);
        Documents.State state3_2 = documents3_2.getState();
        assertNotEquals("two states with different isIncludeAllFields, should not be equals", state3_1, state3_2);

        DocumentsTestImpl documents4_1 = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents4_1.setIncludeProtectedContent(true);
        Documents.State state4_1 = documents4_1.getState();

        DocumentsTestImpl documents4_2 = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents4_2.setIncludeProtectedContent(false);
        Documents.State state4_2 = documents4_2.getState();
        assertNotEquals("two states with different isIncludeProtectedContent, should not be equals", state4_1, state4_2);

        DocumentsTestImpl documents5_1 = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents5_1.setRetrieveCompleteContentContext(true);
        Documents.State state5_1 = documents5_1.getState();

        DocumentsTestImpl documents5_2 = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents5_2.setRetrieveCompleteContentContext(false);
        Documents.State state5_2 = documents5_2.getState();
        assertNotEquals("two states with different isRetrieveCompleteContentContext, should not be equals", state5_1, state5_2);


        DocumentsTestImpl documents6_1 = new DocumentsTestImpl(mock(DeliverySearch.class), null, new DeliverySearchQueryBuilder());
        documents6_1.start(5);
        Documents.State state6_1 = documents6_1.getState();

        DocumentsTestImpl documents6_2 = new DocumentsTestImpl(mock(DeliverySearch.class), null, new DeliverySearchQueryBuilder());
        documents6_2.rows(25);
        Documents.State state6_2 = documents6_2.getState();
        assertNotEquals("two states with different queryBuilders, should not be equals", state6_1, state6_2);
    }

    @Test
    public void TestStateHashCode() {
        final DeliverySearchQueryBuilder queryBuilder = new DeliverySearchQueryBuilder();
        DocumentsTestImpl documents1 = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents1.setIncludeRetired(true);
        documents1.setIncludeDraft(true);
        documents1.setIncludeAllFields(true);
        documents1.setIncludeProtectedContent(true);
        documents1.setRetrieveCompleteContentContext(true);
        Documents.State state1 = documents1.getState();

        DocumentsTestImpl documents2 = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents2.setIncludeRetired(true);
        documents2.setIncludeDraft(true);
        documents2.setIncludeAllFields(true);
        documents2.setIncludeProtectedContent(true);
        documents2.setRetrieveCompleteContentContext(true);
        Documents.State state2 = documents2.getState();

        assertEquals("hashCode() same for equal objects.", state1.hashCode(), state2.hashCode());

        DocumentsTestImpl documents1_1 = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents1_1.setIncludeRetired(true);
        Documents.State state1_1 = documents1_1.getState();

        DocumentsTestImpl documents1_2 = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents1_2.setIncludeRetired(false);
        Documents.State state1_2 = documents1_2.getState();
        assertNotEquals("hashCode() should be different for objects with different isIncludeRetired.", state1_1.hashCode(), state1_2.hashCode());

        DocumentsTestImpl documents2_1 = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents2_1.setIncludeDraft(true);
        Documents.State state2_1 = documents2_1.getState();

        DocumentsTestImpl documents2_2 = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents2_2.setIncludeDraft(false);
        Documents.State state2_2 = documents2_2.getState();
        assertNotEquals("hashCode() should be different for objects with different isIncludeDraft.", state2_1.hashCode(), state2_2.hashCode());

        DocumentsTestImpl documents3_1 = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents3_1.setIncludeAllFields(true);
        Documents.State state3_1 = documents3_1.getState();

        DocumentsTestImpl documents3_2 = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents3_2.setIncludeAllFields(false);
        Documents.State state3_2 = documents3_2.getState();
        assertNotEquals("hashCode() should be different for objects with different isIncludeAllFields.", state3_1.hashCode(), state3_2.hashCode());

        DocumentsTestImpl documents4_1 = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents4_1.setIncludeProtectedContent(true);
        Documents.State state4_1 = documents4_1.getState();

        DocumentsTestImpl documents4_2 = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents4_2.setIncludeProtectedContent(false);
        Documents.State state4_2 = documents4_2.getState();
        assertNotEquals("hashCode() should be different for objects with different isIncludeProtectedContent.", state4_1.hashCode(), state4_2.hashCode());

        DocumentsTestImpl documents5_1 = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents5_1.setRetrieveCompleteContentContext(true);
        Documents.State state5_1 = documents5_1.getState();

        DocumentsTestImpl documents5_2 = new DocumentsTestImpl(mock(DeliverySearch.class), null, queryBuilder);
        documents5_2.setRetrieveCompleteContentContext(false);
        Documents.State state5_2 = documents5_2.getState();
        assertNotEquals("hashCode() should be different for objects with different isRetrieveCompleteContentContext.", state5_1.hashCode(), state5_2.hashCode());


        DocumentsTestImpl documents6_1 = new DocumentsTestImpl(mock(DeliverySearch.class), null, new DeliverySearchQueryBuilder());
        documents6_1.start(5);
        Documents.State state6_1 = documents6_1.getState();

        DocumentsTestImpl documents6_2 = new DocumentsTestImpl(mock(DeliverySearch.class), null, new DeliverySearchQueryBuilder());
        documents6_2.rows(25);
        Documents.State state6_2 = documents6_2.getState();
        assertNotEquals("hashCode() should be different for objects with different queryBuilders.", state6_1.hashCode(), state6_2.hashCode());
    }

}