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

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ContentItemTest {

    @Test
    public void testCreation() {
        assertNotNull("Constructor should create not null instance.", createContentItemToTest());
    }

    @Test
    public void testGetBoolean1() throws JSONException {
        final ArrayList<Boolean> testValue = new ArrayList<>();
        testValue.add(true);
        testValue.add(false);
        testValue.add(true);
        assertEquals(testValue, createContentItemToTest(rawDoc -> rawDoc.boolean1 = testValue).getBoolean1());
    }

    @Test
    public void testGetBoolean1Null() {
        assertNull(createContentItemToTest().getBoolean1());
    }

    @Test
    public void testGetBoolean2() throws JSONException {
        final ArrayList<Boolean> testValue = new ArrayList<>();
        testValue.add(false);
        testValue.add(false);
        testValue.add(true);
        assertEquals(testValue, createContentItemToTest(rawDoc -> rawDoc.boolean2 = testValue).getBoolean2());
    }

    @Test
    public void testGetBoolean2Null() {
        assertNull(createContentItemToTest().getBoolean1());
    }

    @Test
    public void testGetCategories() throws JSONException {
        final ArrayList<String> testValue = new ArrayList<>();
        testValue.add("Dynamic list criteria/Date/Future dates (Events)");
        testValue.add("Dynamic list criteria/Type/Event");
        testValue.add("List ordering options/By date ascending");
        assertEquals(testValue, createContentItemToTest(rawDoc -> rawDoc.categories = testValue).getCategories());
    }

    @Test
    public void testGetCategoriesNull() {
        assertNull(createContentItemToTest().getCategories());
    }

    @Test
    public void testGetCategoryLeaves() throws JSONException {
        final ArrayList<String> testValue = new ArrayList<>();
        testValue.add("Future dates (Events)");
        testValue.add("Event");
        testValue.add("By date ascending");
        assertEquals(testValue, createContentItemToTest(rawDoc -> rawDoc.categoryLeaves = testValue).getCategoryLeaves());
    }

    @Test
    public void testGetCategoryLeavesNull() {
        assertNull(createContentItemToTest().getCategoryLeaves());
    }

    @Test
    public void testGetDate1() throws JSONException {
        final ArrayList<String> testValue = new ArrayList<>();
        testValue.add("2029-09-21T04:00:00Z");
        testValue.add("2029-09-22T04:00:00Z");
        testValue.add("2029-09-23T04:00:00Z");
        testValue.add("2029-09-24T04:00:00Z");
        assertEquals(testValue, createContentItemToTest(rawDoc -> rawDoc.date1 = testValue).getDate1());
    }

    @Test
    public void testGetDate1Null() {
        assertNull(createContentItemToTest().getDate1());
    }

    @Test
    public void testGetDate2() throws JSONException {
        final ArrayList<String> testValue = new ArrayList<>();
        testValue.add("2029-09-21T04:00:00Z");
        testValue.add("2029-09-22T04:00:00Z");
        testValue.add("2029-09-23T04:00:00Z");
        testValue.add("2029-09-24T04:00:00Z");
        assertEquals(testValue, createContentItemToTest(rawDoc -> rawDoc.date2 = testValue).getDate2());
    }

    @Test
    public void testGetDate2Null() {
        assertNull(createContentItemToTest().getDate2());
    }

    @Test
    public void testGetDocument() throws JSONException {
        final JSONObject testValue = createDocument();
        assertEquals(testValue, createContentItemToTest(rawDoc -> rawDoc.document = testValue).getDocument());
    }

    @Test
    public void testGetDocumentNull() {
        assertNull(createContentItemToTest().getDocument());
    }

    @Test
    public void testGetGeneratedFiles() throws JSONException {
        final ArrayList<String> testValue = new ArrayList<>();
        testValue.add("test1/api/ae6a1610-fd30-4b81-8871-0f7f11f95426/delivery/v1/resources/09884b0e-a98d-4614-aebf-5388563c8edf?fit=inside%7C220:145");
        testValue.add("test2/api/ae6a1610-fd30-4b81-8871-0f7f11f95426/delivery/v1/resources/09884b0e-a98d-4614-aebf-5388563c8edf?fit=inside%7C220:145");
        testValue.add("test3/api/ae6a1610-fd30-4b81-8871-0f7f11f95426/delivery/v1/resources/09884b0e-a98d-4614-aebf-5388563c8edf?fit=inside%7C220:145");
        assertEquals(testValue, createContentItemToTest(rawDoc -> rawDoc.generatedFiles = testValue).getGeneratedFiles());
    }

    @Test
    public void testGetGeneratedFilesNull() {
        assertNull(createContentItemToTest().getGeneratedFiles());
    }

    @Test
    public void testGetIsManaged() throws JSONException {
        final boolean testValue = true;
        assertEquals(testValue, createContentItemToTest(rawDoc -> rawDoc.isManaged = testValue).isManaged());
    }

    @Test
    public void testIsManagedDefault() {
        assertFalse(createContentItemToTest().isManaged());
    }

    @Test
    public void testGetLocation1() throws JSONException {
        final ArrayList<String> testValue = new ArrayList<>();
        testValue.add("/dxdam/3a/3a71b9cf-f6da-4b66-85f7-421c5a693d1e");
        testValue.add("/dxdam/6f/6f38a240-7fb7-4be2-9304-5198e4350ee1");
        testValue.add("/dxdam/6b/6bf5f2fa-03e8-4054-af2b-f5d78d3d6f55");
        assertEquals(testValue, createContentItemToTest(rawDoc -> rawDoc.location1 = testValue).getLocation1());
    }

    @Test
    public void testGetLocation1Null() {
        assertNull(createContentItemToTest().getLocation1());
    }

    @Test
    public void testGetLocations() throws JSONException {
        final ArrayList<String> testValue = new ArrayList<>();
        testValue.add("/dxdam/3a/3a71b9cf-f6da-4b66-85f7-421c5a693d1e");
        testValue.add("/dxdam/6f/6f38a240-7fb7-4be2-9304-5198e4350ee1");
        testValue.add("/dxdam/6b/6bf5f2fa-03e8-4054-af2b-f5d78d3d6f55");
        assertEquals(testValue, createContentItemToTest(rawDoc -> rawDoc.locations = testValue).getLocations());
    }

    @Test
    public void testGetLocationsNull() {
        assertNull(createContentItemToTest().getLocations());
    }

    @Test
    public void testGetNumber1() throws JSONException {
        final ArrayList<Double> testValue = new ArrayList<>();
        testValue.add(10.65);
        testValue.add(192.34);
        testValue.add(23455.22);
        assertEquals(testValue, createContentItemToTest(rawDoc -> rawDoc.number1 = testValue).getNumber1());
    }

    @Test
    public void testGetNumber1Null() {
        assertNull(createContentItemToTest().getNumber1());
    }

    @Test
    public void testGetNumber2() throws JSONException {
        final ArrayList<Double> testValue = new ArrayList<>();
        testValue.add(10.65);
        testValue.add(192.34);
        testValue.add(23455.22);
        assertEquals(testValue, createContentItemToTest(rawDoc -> rawDoc.number2 = testValue).getNumber2());
    }

    @Test
    public void testGetNumber2Null() {
        assertNull(createContentItemToTest().getNumber2());
    }

    @Test
    public void testGetStatus() throws JSONException {
        final String testValue = "ready";
        assertEquals(testValue, createContentItemToTest(rawDoc -> rawDoc.status = testValue).getStatus());
    }

    @Test
    public void testGetStatusNull() {
        assertNull(createContentItemToTest().getStatus());
    }

    @Test
    public void testGetString1() throws JSONException {
        final ArrayList<String> testValue = new ArrayList<>();
        testValue.add("test_string1");
        testValue.add("test_string2");
        testValue.add("test_string3");
        testValue.add("test_string4");
        assertEquals(testValue, createContentItemToTest(rawDoc -> rawDoc.string1 = testValue).getString1());
    }

    @Test
    public void testGetString1Null() {
        assertNull(createContentItemToTest().getString1());
    }


    @Test
    public void testGetString2() throws JSONException {
        final ArrayList<String> testValue = new ArrayList<>();
        testValue.add("test_string1");
        testValue.add("test_string2");
        testValue.add("test_string3");
        testValue.add("test_string4");
        assertEquals(testValue, createContentItemToTest(rawDoc -> rawDoc.string2 = testValue).getString2());
    }

    @Test
    public void testGetString2Null() {
        assertNull(createContentItemToTest().getString2());
    }

    @Test
    public void testGetString3() throws JSONException {
        final ArrayList<String> testValue = new ArrayList<>();
        testValue.add("test_string1");
        testValue.add("test_string2");
        testValue.add("test_string3");
        testValue.add("test_string4");
        assertEquals(testValue, createContentItemToTest(rawDoc -> rawDoc.string3 = testValue).getString3());
    }

    @Test
    public void testGetString3Null() {
        assertNull(createContentItemToTest().getString3());
    }

    @Test
    public void testGetString4() throws JSONException {
        final ArrayList<String> testValue = new ArrayList<>();
        testValue.add("test_string1");
        testValue.add("test_string2");
        testValue.add("test_string3");
        testValue.add("test_string4");
        assertEquals(testValue, createContentItemToTest(rawDoc -> rawDoc.string4 = testValue).getString4());
    }

    @Test
    public void testGetString4Null() {
        assertNull(createContentItemToTest().getString4());
    }

    @Test
    public void testGetSortableDate1() throws JSONException {
        final String testValue = "2029-09-21T04:00:00Z";
        assertEquals(testValue, createContentItemToTest(rawDoc -> rawDoc.sortableDate1 = testValue).getSortableDate1());
    }

    @Test
    public void testGetSortableDate1Null() {
        assertNull(createContentItemToTest().getSortableDate1());
    }

    @Test
    public void testGetSortableDate2() throws JSONException {
        final String testValue = "2029-09-21T04:00:00Z";
        assertEquals(testValue, createContentItemToTest(rawDoc -> rawDoc.sortableDate2 = testValue).getSortableDate2());
    }

    @Test
    public void testGetSortableDate2Null() {
        assertNull(createContentItemToTest().getSortableDate2());
    }

    @Test
    public void testGetSortableNumber1() throws JSONException {
        final Double testValue = 10.65;
        assertEquals(testValue, createContentItemToTest(rawDoc -> rawDoc.sortableNumber1 = testValue).getSortableNumber1());
    }

    @Test
    public void testGetSortableNumber1Null() {
        assertNull(createContentItemToTest().getSortableNumber1());
    }

    @Test
    public void testGetSortableNumber2() throws JSONException {
        final Double testValue = 192.34;
        assertEquals(testValue, createContentItemToTest(rawDoc -> rawDoc.sortableNumber2 = testValue).getSortableNumber2());
    }

    @Test
    public void testGetSortableNumber2Null() {
        assertNull(createContentItemToTest().getSortableNumber2());
    }

    @Test
    public void testGetSortableString1() throws JSONException {
        final String testValue = "test_string1";
        assertEquals(testValue, createContentItemToTest(rawDoc -> rawDoc.sortableString1 = testValue).getSortableString1());
    }

    @Test
    public void testGetSortableString1Null() {
        assertNull(createContentItemToTest().getSortableString1());
    }

    @Test
    public void testGetSortableString2() throws JSONException {
        final String testValue = "test_string2";
        assertEquals(testValue, createContentItemToTest(rawDoc -> rawDoc.sortableString2 = testValue).getSortableString2());
    }

    @Test
    public void testGetSortableString2Null() {
        assertNull(createContentItemToTest().getSortableString2());
    }

    @Test
    public void testGetSortableString3() throws JSONException {
        final String testValue = "test_string3";
        assertEquals(testValue, createContentItemToTest(rawDoc -> rawDoc.sortableString3 = testValue).getSortableString3());
    }

    @Test
    public void testGetSortableString3Null() {
        assertNull(createContentItemToTest().getSortableString3());
    }

    @Test
    public void testGetSortableString4() throws JSONException {
        final String testValue = "test_string4";
        assertEquals(testValue, createContentItemToTest(rawDoc -> rawDoc.sortableString4 = testValue).getSortableString4());
    }

    @Test
    public void testGetSortableString4Null() {
        assertNull(createContentItemToTest().getSortableString4());
    }

    @Test
    public void testGetType() throws JSONException {
        final String testValue = "Footer";
        assertEquals(testValue, createContentItemToTest(rawDoc -> rawDoc.type = testValue).getType());
    }

    @Test
    public void testGetTypeNull() {
        assertNull(createContentItemToTest().getType());
    }

    @Test
    public void testGetTypeId() throws JSONException {
        final String testValue = "cbde3c3b-d03f-484b-8aa2-0c35227db10c";
        assertEquals(testValue, createContentItemToTest(rawDoc -> rawDoc.typeId = testValue).getTypeId());
    }

    @Test
    public void testGetTypeIdNull() {
        assertNull(createContentItemToTest().getTypeId());
    }

    @Test
    public void testGetText() throws JSONException {
        final ArrayList<String> testValue = new ArrayList<>();
        testValue.add("copyright Oslo");
        testValue.add("salesNumber +1 (888) 777-5555");
        testValue.add("labelForCustomerService Customer service number");
        testValue.add("emailAddress customer-service@example.com");
        testValue.add("customerServiceContactNumber +1 (888) 777-5555");
        testValue.add("labelForSales Sales number");
        testValue.add("Logo");
        assertEquals(testValue, createContentItemToTest(rawDoc -> rawDoc.text = testValue).getText());
    }

    @Test
    public void testGetTextNull() {
        assertNull(createContentItemToTest().getText());
    }

    @Test
    public void testEquals() throws JSONException {
        /*
         * Test fully equals objects
         */
        assertEquals(new Asset(createFullRawDoc()), new Asset(createFullRawDoc()));

        final ArrayList<Boolean> boolean1_1 = new ArrayList<>();
        boolean1_1.add(true);
        boolean1_1.add(false);
        boolean1_1.add(true);
        final ArrayList<Boolean> boolean1_2 = new ArrayList<>();
        boolean1_2.add(true);
        boolean1_2.add(false);
        boolean1_2.add(false);
        boolean1_2.add(true);
        assertNotEquals(
                "Two objects with different boolean1, should not be equals",
                createContentItemToTest(rawDoc -> rawDoc.boolean1 = boolean1_1),
                createContentItemToTest(rawDoc -> rawDoc.boolean1 = boolean1_2)
        );

        final ArrayList<Boolean> boolean2_1 = new ArrayList<>();
        boolean2_1.add(false);
        boolean2_1.add(false);
        boolean2_1.add(true);
        final ArrayList<Boolean> boolean2_2 = new ArrayList<>();
        assertNotEquals(
                "Two objects with different boolean2, should not be equals",
                createContentItemToTest(rawDoc -> rawDoc.boolean2 = boolean2_1),
                createContentItemToTest(rawDoc -> rawDoc.boolean2 = boolean2_2)
        );

        final ArrayList<String> categories1 = new ArrayList<>();
        categories1.add("Dynamic list criteria/Date/Future dates (Events)");
        categories1.add("Dynamic list criteria/Type/Event");
        categories1.add("List ordering options/By date ascending");
        final ArrayList<String> categories2 = new ArrayList<>();
        categories2.add("Dynamic list criteria/Date/Future dates (Events)");
        assertNotEquals(
                "Two objects with different categories, should not be equals",
                createContentItemToTest(rawDoc -> rawDoc.categories = categories1),
                createContentItemToTest(rawDoc -> rawDoc.categories = categories2)
        );

        final ArrayList<String> categoryLeaves1 = new ArrayList<>();
        categoryLeaves1.add("Future dates (Events)");
        categoryLeaves1.add("Event");
        categoryLeaves1.add("By date ascending");
        final ArrayList<String> categoryLeaves2 = new ArrayList<>();
        categoryLeaves2.add("Future dates (Events)");
        categoryLeaves2.add("Event");
        assertNotEquals(
                "Two objects with different categoryLeaves, should not be equals",
                createContentItemToTest(rawDoc -> rawDoc.categoryLeaves = categoryLeaves1),
                createContentItemToTest(rawDoc -> rawDoc.categoryLeaves = categoryLeaves2)
        );

        final ArrayList<String> date1_1 = new ArrayList<>();
        date1_1.add("2029-09-21T04:00:00Z");
        date1_1.add("2029-09-22T04:00:00Z");
        date1_1.add("2029-09-23T04:00:00Z");
        date1_1.add("2029-09-24T04:00:00Z");
        final ArrayList<String> date1_2 = new ArrayList<>();
        date1_2.add("2029-09-21T04:00:00Z");
        date1_2.add("2029-09-24T04:00:00Z");
        assertNotEquals(
                "Two objects with different date1, should not be equals",
                createContentItemToTest(rawDoc -> rawDoc.date1 = date1_1),
                createContentItemToTest(rawDoc -> rawDoc.date1 = date1_2)
        );

        final ArrayList<String> date2_1 = new ArrayList<>();
        date2_1.add("2029-09-21T04:00:00Z");
        date2_1.add("2029-09-22T04:00:00Z");
        date2_1.add("2029-09-23T04:00:00Z");
        date2_1.add("2029-09-24T04:00:00Z");
        final ArrayList<String> date2_2 = new ArrayList<>();
        date2_2.add("2029-09-21T04:00:00Z");
        date2_2.add("2029-09-24T04:00:00Z");
        assertNotEquals(
                "Two objects with different date2, should not be equals",
                createContentItemToTest(rawDoc -> rawDoc.date2 = date2_1),
                createContentItemToTest(rawDoc -> rawDoc.date2 = date2_2)
        );

        assertNotEquals(
                "Two objects with different document, should not be equals",
                createContentItemToTest(rawDoc -> rawDoc.document = createDocument()),
                createContentItemToTest(rawDoc -> rawDoc.document = new JSONObject("{ \"classification\" : \"content\" }"))
        );

        final ArrayList<String> generatedFiles = new ArrayList<>();
        generatedFiles.add("test1/api/ae6a1610-fd30-4b81-8871-0f7f11f95426/delivery/v1/resources/09884b0e-a98d-4614-aebf-5388563c8edf?fit=inside%7C220:145");
        generatedFiles.add("test2/api/ae6a1610-fd30-4b81-8871-0f7f11f95426/delivery/v1/resources/09884b0e-a98d-4614-aebf-5388563c8edf?fit=inside%7C220:145");
        generatedFiles.add("test3/api/ae6a1610-fd30-4b81-8871-0f7f11f95426/delivery/v1/resources/09884b0e-a98d-4614-aebf-5388563c8edf?fit=inside%7C220:145");
        assertNotEquals(
                "Two objects with different generatedFiles, should not be equals",
                createContentItemToTest(rawDoc -> rawDoc.generatedFiles = generatedFiles),
                createContentItemToTest(rawDoc -> rawDoc.generatedFiles = new ArrayList<>())
        );

        assertNotEquals(
                "Two objects with different isManaged, should not be equals",
                createContentItemToTest(rawDoc -> rawDoc.isManaged = false),
                createContentItemToTest(rawDoc -> rawDoc.isManaged = true)
        );

        final ArrayList<String> location1_1 = new ArrayList<>();
        location1_1.add("/dxdam/3a/3a71b9cf-f6da-4b66-85f7-421c5a693d1e");
        location1_1.add("/dxdam/6f/6f38a240-7fb7-4be2-9304-5198e4350ee1");
        location1_1.add("/dxdam/6b/6bf5f2fa-03e8-4054-af2b-f5d78d3d6f55");
        final ArrayList<String> location1_2 = new ArrayList<>();
        location1_2.add("test/dxdam/3a/3a71b9cf-f6da-4b66-85f7-421c5a693d1e");
        location1_2.add("test/dxdam/6f/6f38a240-7fb7-4be2-9304-5198e4350ee1");
        location1_2.add("test/dxdam/6b/6bf5f2fa-03e8-4054-af2b-f5d78d3d6f55");
        assertNotEquals(
                "Two objects with different location1, should not be equals",
                createContentItemToTest(rawDoc -> rawDoc.location1 = location1_1),
                createContentItemToTest(rawDoc -> rawDoc.location1 = location1_2)
        );


        final ArrayList<String> locations_1 = new ArrayList<>();
        locations_1.add("/dxdam/3a/3a71b9cf-f6da-4b66-85f7-421c5a693d1e");
        locations_1.add("/dxdam/6f/6f38a240-7fb7-4be2-9304-5198e4350ee1");
        locations_1.add("/dxdam/6b/6bf5f2fa-03e8-4054-af2b-f5d78d3d6f55");
        final ArrayList<String> locations_2 = new ArrayList<>();
        locations_2.add("test/dxdam/3a/3a71b9cf-f6da-4b66-85f7-421c5a693d1e");
        locations_2.add("test/dxdam/6f/6f38a240-7fb7-4be2-9304-5198e4350ee1");
        locations_2.add("test/dxdam/6b/6bf5f2fa-03e8-4054-af2b-f5d78d3d6f55");
        assertNotEquals(
                "Two objects with different locations, should not be equals",
                createContentItemToTest(rawDoc -> rawDoc.locations = locations_1),
                createContentItemToTest(rawDoc -> rawDoc.locations = locations_2)
        );

        final ArrayList<Double> number1_1 = new ArrayList<>();
        number1_1.add(10.65);
        number1_1.add(192.34);
        number1_1.add(23455.22);
        final ArrayList<Double> number1_2 = new ArrayList<>();
        number1_2.add(23455.22);
        assertNotEquals(
                "Two objects with different number1, should not be equals",
                createContentItemToTest(rawDoc -> rawDoc.number1 = number1_1),
                createContentItemToTest(rawDoc -> rawDoc.number1 = number1_2)
        );

        final ArrayList<Double> number2_1 = new ArrayList<>();
        number2_1.add(10.65);
        number2_1.add(192.34);
        number2_1.add(23455.22);
        final ArrayList<Double> number2_2 = new ArrayList<>();
        number2_2.add(10.65);
        number2_2.add(192.34);
        assertNotEquals(
                "Two objects with different number2, should not be equals",
                createContentItemToTest(rawDoc -> rawDoc.number2 = number2_1),
                createContentItemToTest(rawDoc -> rawDoc.number2 = number2_2)
        );

        assertNotEquals(
                "Two objects with different status, should not be equals",
                createContentItemToTest(rawDoc -> rawDoc.status = "ready"),
                createContentItemToTest(rawDoc -> rawDoc.status = "in progress")
        );

        final ArrayList<String> string1_1 = new ArrayList<>();
        string1_1.add("test_string1");
        string1_1.add("test_string2");
        string1_1.add("test_string3");
        string1_1.add("test_string4");
        final ArrayList<String> string1_2 = new ArrayList<>();
        string1_2.add("test_string1");
        string1_2.add("test_string3");
        assertNotEquals(
                "Two objects with different string1, should not be equals",
                createContentItemToTest(rawDoc -> rawDoc.string1 = string1_1),
                createContentItemToTest(rawDoc -> rawDoc.string1 = string1_2)
        );

        final ArrayList<String> string2_1 = new ArrayList<>();
        string2_1.add("test_string1");
        string2_1.add("test_string2");
        string2_1.add("test_string3");
        string2_1.add("test_string4");
        final ArrayList<String> string2_2 = new ArrayList<>();
        string2_2.add("test_string1");
        string2_2.add("test_string2");
        string2_2.add("test_string3");
        assertNotEquals(
                "Two objects with different string2, should not be equals",
                createContentItemToTest(rawDoc -> rawDoc.string2 = string2_1),
                createContentItemToTest(rawDoc -> rawDoc.string2 = string2_2)
        );

        final ArrayList<String> string3_1 = new ArrayList<>();
        string3_1.add("test_string1");
        string3_1.add("test_string2");
        string3_1.add("test_string3");
        string3_1.add("test_string4");
        final ArrayList<String> string3_2 = new ArrayList<>();
        string3_2.add("test_string4");
        string3_2.add("test_string2");
        string3_2.add("test_string3");
        assertNotEquals(
                "Two objects with different string3, should not be equals",
                createContentItemToTest(rawDoc -> rawDoc.string3 = string3_1),
                createContentItemToTest(rawDoc -> rawDoc.string3 = string3_2)
        );

        final ArrayList<String> string4_1 = new ArrayList<>();
        string4_1.add("test_string1");
        string4_1.add("test_string2");
        string4_1.add("test_string3");
        string4_1.add("test_string4");
        final ArrayList<String> string4_2 = new ArrayList<>();
        string4_2.add("test_string4");
        string4_2.add("test_string2");
        string4_2.add("test_string3");
        string4_2.add("test_string371");
        assertNotEquals(
                "Two objects with different string4, should not be equals",
                createContentItemToTest(rawDoc -> rawDoc.string4 = string4_1),
                createContentItemToTest(rawDoc -> rawDoc.string4 = string4_2)
        );

        assertNotEquals(
                "Two objects with different sortableDate1, should not be equals",
                createContentItemToTest(rawDoc -> rawDoc.sortableDate1 = "2029-09-21T04:00:00Z"),
                createContentItemToTest(rawDoc -> rawDoc.sortableDate1 = "2029-10-21T04:00:00Z")
        );

        assertNotEquals(
                "Two objects with different sortableDate2, should not be equals",
                createContentItemToTest(rawDoc -> rawDoc.sortableDate2 = "2029-09-21T04:00:00Z"),
                createContentItemToTest(rawDoc -> rawDoc.sortableDate2 = "2029-10-21T04:00:00Z")
        );

        assertNotEquals(
                "Two objects with different sortableNumber1, should not be equals",
                createContentItemToTest(rawDoc -> rawDoc.sortableNumber1 = 10.65),
                createContentItemToTest(rawDoc -> rawDoc.sortableNumber1 = 1270.65)
        );

        assertNotEquals(
                "Two objects with different sortableNumber2, should not be equals",
                createContentItemToTest(rawDoc -> rawDoc.sortableNumber2 = 192.34),
                createContentItemToTest(rawDoc -> rawDoc.sortableNumber2 = -10.22)
        );

        assertNotEquals(
                "Two objects with different sortableString1, should not be equals",
                createContentItemToTest(rawDoc -> rawDoc.sortableString1 = "test_string1_1"),
                createContentItemToTest(rawDoc -> rawDoc.sortableString1 = "test_string1_2")
        );

        assertNotEquals(
                "Two objects with different sortableString2, should not be equals",
                createContentItemToTest(rawDoc -> rawDoc.sortableString2 = "test_string2_1"),
                createContentItemToTest(rawDoc -> rawDoc.sortableString2 = "test_string2_2")
        );

        assertNotEquals(
                "Two objects with different sortableString3, should not be equals",
                createContentItemToTest(rawDoc -> rawDoc.sortableString3 = "test_string3_1"),
                createContentItemToTest(rawDoc -> rawDoc.sortableString3 = "test_string3_2")
        );

        assertNotEquals(
                "Two objects with different sortableString4, should not be equals",
                createContentItemToTest(rawDoc -> rawDoc.sortableString4 = "test_string4_1"),
                createContentItemToTest(rawDoc -> rawDoc.sortableString4 = "test_string4_2")
        );

        final ArrayList<String> text = new ArrayList<>();
        text.add("copyright Oslo");
        text.add("salesNumber +1 (888) 777-5555");
        text.add("labelForCustomerService Customer service number");
        text.add("emailAddress customer-service@example.com");
        text.add("customerServiceContactNumber +1 (888) 777-5555");
        text.add("labelForSales Sales number");
        text.add("Logo");
        assertNotEquals(
                "Two objects with different text, should not be equals",
                createContentItemToTest(rawDoc -> rawDoc.text = text),
                createContentItemToTest(rawDoc -> rawDoc.text = new ArrayList<>())
        );

        assertNotEquals(
                "Two objects with different type, should not be equals",
                createContentItemToTest(rawDoc -> rawDoc.type = "Footer"),
                createContentItemToTest(rawDoc -> rawDoc.type = "Header")
        );

        assertNotEquals(
                "Two objects with different typeId, should not be equals",
                createContentItemToTest(rawDoc -> rawDoc.typeId = "cbde3c3b-d03f-484b-8aa2-0c35227db10c"),
                createContentItemToTest(rawDoc -> rawDoc.typeId = "test-cbde3c3b-d03f-484b-8aa2-0c35227db10c")
        );
    }

    @Test
    public void testHashCode() throws JSONException {
        assertEquals("hashCode() same for equal objects.", new ContentItem(createFullRawDoc()).hashCode(), new ContentItem(createFullRawDoc()).hashCode());

        final ArrayList<Boolean> boolean1_1 = new ArrayList<>();
        boolean1_1.add(true);
        boolean1_1.add(false);
        boolean1_1.add(true);
        final ArrayList<Boolean> boolean1_2 = new ArrayList<>();
        boolean1_2.add(true);
        boolean1_2.add(false);
        boolean1_2.add(false);
        boolean1_2.add(true);
        assertNotEquals(
                "hashCode() should be different for objects with different boolean1.",
                createContentItemToTest(rawDoc -> rawDoc.boolean1 = boolean1_1),
                createContentItemToTest(rawDoc -> rawDoc.boolean1 = boolean1_2)
        );

        final ArrayList<Boolean> boolean2_1 = new ArrayList<>();
        boolean2_1.add(false);
        boolean2_1.add(false);
        boolean2_1.add(true);
        final ArrayList<Boolean> boolean2_2 = new ArrayList<>();
        assertNotEquals(
                "hashCode() should be different for objects with different boolean2.",
                createContentItemToTest(rawDoc -> rawDoc.boolean2 = boolean2_1),
                createContentItemToTest(rawDoc -> rawDoc.boolean2 = boolean2_2)
        );

        final ArrayList<String> categories1 = new ArrayList<>();
        categories1.add("Dynamic list criteria/Date/Future dates (Events)");
        categories1.add("Dynamic list criteria/Type/Event");
        categories1.add("List ordering options/By date ascending");
        final ArrayList<String> categories2 = new ArrayList<>();
        categories2.add("Dynamic list criteria/Date/Future dates (Events)");
        assertNotEquals(
                "hashCode() should be different for objects with different categories.",
                createContentItemToTest(rawDoc -> rawDoc.categories = categories1),
                createContentItemToTest(rawDoc -> rawDoc.categories = categories2)
        );

        final ArrayList<String> categoryLeaves1 = new ArrayList<>();
        categoryLeaves1.add("Future dates (Events)");
        categoryLeaves1.add("Event");
        categoryLeaves1.add("By date ascending");
        final ArrayList<String> categoryLeaves2 = new ArrayList<>();
        categoryLeaves2.add("Future dates (Events)");
        categoryLeaves2.add("Event");
        assertNotEquals(
                "hashCode() should be different for objects with different categoryLeaves.",
                createContentItemToTest(rawDoc -> rawDoc.categoryLeaves = categoryLeaves1),
                createContentItemToTest(rawDoc -> rawDoc.categoryLeaves = categoryLeaves2)
        );

        final ArrayList<String> date1_1 = new ArrayList<>();
        date1_1.add("2029-09-21T04:00:00Z");
        date1_1.add("2029-09-22T04:00:00Z");
        date1_1.add("2029-09-23T04:00:00Z");
        date1_1.add("2029-09-24T04:00:00Z");
        final ArrayList<String> date1_2 = new ArrayList<>();
        date1_2.add("2029-09-21T04:00:00Z");
        date1_2.add("2029-09-24T04:00:00Z");
        assertNotEquals(
                "hashCode() should be different for objects with different date1.",
                createContentItemToTest(rawDoc -> rawDoc.date1 = date1_1),
                createContentItemToTest(rawDoc -> rawDoc.date1 = date1_2)
        );

        final ArrayList<String> date2_1 = new ArrayList<>();
        date2_1.add("2029-09-21T04:00:00Z");
        date2_1.add("2029-09-22T04:00:00Z");
        date2_1.add("2029-09-23T04:00:00Z");
        date2_1.add("2029-09-24T04:00:00Z");
        final ArrayList<String> date2_2 = new ArrayList<>();
        date2_2.add("2029-09-21T04:00:00Z");
        date2_2.add("2029-09-24T04:00:00Z");
        assertNotEquals(
                "hashCode() should be different for objects with different date2.",
                createContentItemToTest(rawDoc -> rawDoc.date2 = date2_1),
                createContentItemToTest(rawDoc -> rawDoc.date2 = date2_2)
        );

        assertNotEquals(
                "hashCode() should be different for objects with different document.",
                createContentItemToTest(rawDoc -> rawDoc.document = createDocument()),
                createContentItemToTest(rawDoc -> rawDoc.document = new JSONObject("{ \"classification\" : \"content\" }"))
        );

        final ArrayList<String> generatedFiles = new ArrayList<>();
        generatedFiles.add("test1/api/ae6a1610-fd30-4b81-8871-0f7f11f95426/delivery/v1/resources/09884b0e-a98d-4614-aebf-5388563c8edf?fit=inside%7C220:145");
        generatedFiles.add("test2/api/ae6a1610-fd30-4b81-8871-0f7f11f95426/delivery/v1/resources/09884b0e-a98d-4614-aebf-5388563c8edf?fit=inside%7C220:145");
        generatedFiles.add("test3/api/ae6a1610-fd30-4b81-8871-0f7f11f95426/delivery/v1/resources/09884b0e-a98d-4614-aebf-5388563c8edf?fit=inside%7C220:145");
        assertNotEquals(
                "hashCode() should be different for objects with different generatedFiles.",
                createContentItemToTest(rawDoc -> rawDoc.generatedFiles = generatedFiles),
                createContentItemToTest(rawDoc -> rawDoc.generatedFiles = new ArrayList<>())
        );

        assertNotEquals(
                "hashCode() should be different for objects with different isManaged.",
                createContentItemToTest(rawDoc -> rawDoc.isManaged = false),
                createContentItemToTest(rawDoc -> rawDoc.isManaged = true)
        );

        final ArrayList<String> location1_1 = new ArrayList<>();
        location1_1.add("/dxdam/3a/3a71b9cf-f6da-4b66-85f7-421c5a693d1e");
        location1_1.add("/dxdam/6f/6f38a240-7fb7-4be2-9304-5198e4350ee1");
        location1_1.add("/dxdam/6b/6bf5f2fa-03e8-4054-af2b-f5d78d3d6f55");
        final ArrayList<String> location1_2 = new ArrayList<>();
        location1_2.add("test/dxdam/3a/3a71b9cf-f6da-4b66-85f7-421c5a693d1e");
        location1_2.add("test/dxdam/6f/6f38a240-7fb7-4be2-9304-5198e4350ee1");
        location1_2.add("test/dxdam/6b/6bf5f2fa-03e8-4054-af2b-f5d78d3d6f55");
        assertNotEquals(
                "hashCode() should be different for objects with different location1.",
                createContentItemToTest(rawDoc -> rawDoc.location1 = location1_1),
                createContentItemToTest(rawDoc -> rawDoc.location1 = location1_2)
        );


        final ArrayList<String> locations_1 = new ArrayList<>();
        locations_1.add("/dxdam/3a/3a71b9cf-f6da-4b66-85f7-421c5a693d1e");
        locations_1.add("/dxdam/6f/6f38a240-7fb7-4be2-9304-5198e4350ee1");
        locations_1.add("/dxdam/6b/6bf5f2fa-03e8-4054-af2b-f5d78d3d6f55");
        final ArrayList<String> locations_2 = new ArrayList<>();
        locations_2.add("test/dxdam/3a/3a71b9cf-f6da-4b66-85f7-421c5a693d1e");
        locations_2.add("test/dxdam/6f/6f38a240-7fb7-4be2-9304-5198e4350ee1");
        locations_2.add("test/dxdam/6b/6bf5f2fa-03e8-4054-af2b-f5d78d3d6f55");
        assertNotEquals(
                "hashCode() should be different for objects with different locations.",
                createContentItemToTest(rawDoc -> rawDoc.locations = locations_1),
                createContentItemToTest(rawDoc -> rawDoc.locations = locations_2)
        );

        final ArrayList<Double> number1_1 = new ArrayList<>();
        number1_1.add(10.65);
        number1_1.add(192.34);
        number1_1.add(23455.22);
        final ArrayList<Double> number1_2 = new ArrayList<>();
        number1_2.add(23455.22);
        assertNotEquals(
                "hashCode() should be different for objects with different number1.",
                createContentItemToTest(rawDoc -> rawDoc.number1 = number1_1),
                createContentItemToTest(rawDoc -> rawDoc.number1 = number1_2)
        );

        final ArrayList<Double> number2_1 = new ArrayList<>();
        number2_1.add(10.65);
        number2_1.add(192.34);
        number2_1.add(23455.22);
        final ArrayList<Double> number2_2 = new ArrayList<>();
        number2_2.add(10.65);
        number2_2.add(192.34);
        assertNotEquals(
                "hashCode() should be different for objects with different number2.",
                createContentItemToTest(rawDoc -> rawDoc.number2 = number2_1),
                createContentItemToTest(rawDoc -> rawDoc.number2 = number2_2)
        );

        assertNotEquals(
                "hashCode() should be different for objects with different status.",
                createContentItemToTest(rawDoc -> rawDoc.status = "ready"),
                createContentItemToTest(rawDoc -> rawDoc.status = "in progress")
        );

        final ArrayList<String> string1_1 = new ArrayList<>();
        string1_1.add("test_string1");
        string1_1.add("test_string2");
        string1_1.add("test_string3");
        string1_1.add("test_string4");
        final ArrayList<String> string1_2 = new ArrayList<>();
        string1_2.add("test_string1");
        string1_2.add("test_string3");
        assertNotEquals(
                "hashCode() should be different for objects with different string1.",
                createContentItemToTest(rawDoc -> rawDoc.string1 = string1_1),
                createContentItemToTest(rawDoc -> rawDoc.string1 = string1_2)
        );

        final ArrayList<String> string2_1 = new ArrayList<>();
        string2_1.add("test_string1");
        string2_1.add("test_string2");
        string2_1.add("test_string3");
        string2_1.add("test_string4");
        final ArrayList<String> string2_2 = new ArrayList<>();
        string2_2.add("test_string1");
        string2_2.add("test_string2");
        string2_2.add("test_string3");
        assertNotEquals(
                "hashCode() should be different for objects with different string2.",
                createContentItemToTest(rawDoc -> rawDoc.string2 = string2_1),
                createContentItemToTest(rawDoc -> rawDoc.string2 = string2_2)
        );

        final ArrayList<String> string3_1 = new ArrayList<>();
        string3_1.add("test_string1");
        string3_1.add("test_string2");
        string3_1.add("test_string3");
        string3_1.add("test_string4");
        final ArrayList<String> string3_2 = new ArrayList<>();
        string3_2.add("test_string4");
        string3_2.add("test_string2");
        string3_2.add("test_string3");
        assertNotEquals(
                "hashCode() should be different for objects with different string3.",
                createContentItemToTest(rawDoc -> rawDoc.string3 = string3_1),
                createContentItemToTest(rawDoc -> rawDoc.string3 = string3_2)
        );

        final ArrayList<String> string4_1 = new ArrayList<>();
        string4_1.add("test_string1");
        string4_1.add("test_string2");
        string4_1.add("test_string3");
        string4_1.add("test_string4");
        final ArrayList<String> string4_2 = new ArrayList<>();
        string4_2.add("test_string4");
        string4_2.add("test_string2");
        string4_2.add("test_string3");
        string4_2.add("test_string371");
        assertNotEquals(
                "hashCode() should be different for objects with different string4.",
                createContentItemToTest(rawDoc -> rawDoc.string4 = string4_1),
                createContentItemToTest(rawDoc -> rawDoc.string4 = string4_2)
        );

        assertNotEquals(
                "hashCode() should be different for objects with different sortableDate1.",
                createContentItemToTest(rawDoc -> rawDoc.sortableDate1 = "2029-09-21T04:00:00Z"),
                createContentItemToTest(rawDoc -> rawDoc.sortableDate1 = "2029-10-21T04:00:00Z")
        );

        assertNotEquals(
                "hashCode() should be different for objects with different sortableDate2.",
                createContentItemToTest(rawDoc -> rawDoc.sortableDate2 = "2029-09-21T04:00:00Z"),
                createContentItemToTest(rawDoc -> rawDoc.sortableDate2 = "2029-10-21T04:00:00Z")
        );

        assertNotEquals(
                "hashCode() should be different for objects with different sortableNumber1.",
                createContentItemToTest(rawDoc -> rawDoc.sortableNumber1 = 10.65),
                createContentItemToTest(rawDoc -> rawDoc.sortableNumber1 = 1270.65)
        );

        assertNotEquals(
                "hashCode() should be different for objects with different sortableNumber2.",
                createContentItemToTest(rawDoc -> rawDoc.sortableNumber2 = 192.34),
                createContentItemToTest(rawDoc -> rawDoc.sortableNumber2 = -10.22)
        );

        assertNotEquals(
                "hashCode() should be different for objects with different sortableString1.",
                createContentItemToTest(rawDoc -> rawDoc.sortableString1 = "test_string1_1"),
                createContentItemToTest(rawDoc -> rawDoc.sortableString1 = "test_string1_2")
        );

        assertNotEquals(
                "hashCode() should be different for objects with different sortableString2.",
                createContentItemToTest(rawDoc -> rawDoc.sortableString2 = "test_string2_1"),
                createContentItemToTest(rawDoc -> rawDoc.sortableString2 = "test_string2_2")
        );

        assertNotEquals(
                "hashCode() should be different for objects with different sortableString3.",
                createContentItemToTest(rawDoc -> rawDoc.sortableString3 = "test_string3_1"),
                createContentItemToTest(rawDoc -> rawDoc.sortableString3 = "test_string3_2")
        );

        assertNotEquals(
                "hashCode() should be different for objects with different sortableString4.",
                createContentItemToTest(rawDoc -> rawDoc.sortableString4 = "test_string4_1"),
                createContentItemToTest(rawDoc -> rawDoc.sortableString4 = "test_string4_2")
        );

        final ArrayList<String> text = new ArrayList<>();
        text.add("copyright Oslo");
        text.add("salesNumber +1 (888) 777-5555");
        text.add("labelForCustomerService Customer service number");
        text.add("emailAddress customer-service@example.com");
        text.add("customerServiceContactNumber +1 (888) 777-5555");
        text.add("labelForSales Sales number");
        text.add("Logo");
        assertNotEquals(
                "hashCode() should be different for objects with different text.",
                createContentItemToTest(rawDoc -> rawDoc.text = text),
                createContentItemToTest(rawDoc -> rawDoc.text = new ArrayList<>())
        );

        assertNotEquals(
                "hashCode() should be different for objects with different type.",
                createContentItemToTest(rawDoc -> rawDoc.type = "Footer"),
                createContentItemToTest(rawDoc -> rawDoc.type = "Header")
        );

        assertNotEquals(
                "hashCode() should be different for objects with different typeId.",
                createContentItemToTest(rawDoc -> rawDoc.typeId = "cbde3c3b-d03f-484b-8aa2-0c35227db10c"),
                createContentItemToTest(rawDoc -> rawDoc.typeId = "test-cbde3c3b-d03f-484b-8aa2-0c35227db10c")
        );
    }


    @Test
    public void testToString() throws JSONException {
        ContentItem document = new ContentItem(createFullRawDoc());
        assertEquals(createExpectedToString(document), document.toString());
    }

    @Test
    public void testParcelable() throws JSONException {
        ContentItem source = new ContentItem(createFullRawDoc());

        Parcel parcel = MockParcel.obtain();
        source.writeToParcel(parcel, source.describeContents());
        parcel.setDataPosition(0);

        ContentItem createdFromParcel = ContentItem.CREATOR.createFromParcel(parcel);

        assertEquals("Builders should builder equals ContentItem instances ", source, createdFromParcel);
        final int testArraySize = 2;
        ContentItem[] testArray = ContentItem.CREATOR.newArray(testArraySize);
        assertNotNull(testArray);
        assertEquals(testArraySize, testArray.length);
    }

    @Test
    public void testParcelableDouble() throws JSONException {
        DeliverySearchResponseDocument raw = createFullRawDoc();
        raw.sortableNumber1 = null;
        raw.sortableNumber2 = null;
        ContentItem source = new ContentItem(createFullRawDoc());

        Parcel parcel = MockParcel.obtain();
        source.writeToParcel(parcel, source.describeContents());
        parcel.setDataPosition(0);

        ContentItem createdFromParcel = ContentItem.CREATOR.createFromParcel(parcel);

        assertEquals("Builders should builder equals ContentItem instances ", source, createdFromParcel);
        final int testArraySize = 2;
        ContentItem[] testArray = ContentItem.CREATOR.newArray(testArraySize);
        assertNotNull(testArray);
        assertEquals(testArraySize, testArray.length);
    }

    private DeliverySearchResponseDocument createFullRawDoc() throws JSONException {
        final ArrayList<Boolean> boolean1 = new ArrayList<>();
        boolean1.add(true);
        boolean1.add(false);
        boolean1.add(true);
        final ArrayList<Boolean> boolean2 = new ArrayList<>();
        boolean2.add(false);
        boolean2.add(false);
        boolean2.add(true);
        final ArrayList<String> categories = new ArrayList<>();
        categories.add("Dynamic list criteria/Date/Future dates (Events)");
        categories.add("Dynamic list criteria/Type/Event");
        categories.add("List ordering options/By date ascending");
        final ArrayList<String> categoryLeaves = new ArrayList<>();
        categoryLeaves.add("Future dates (Events)");
        categoryLeaves.add("Event");
        categoryLeaves.add("By date ascending");
        final ArrayList<String> date1 = new ArrayList<>();
        date1.add("2029-09-21T04:00:00Z");
        date1.add("2029-09-22T04:00:00Z");
        date1.add("2029-09-23T04:00:00Z");
        date1.add("2029-09-24T04:00:00Z");
        final ArrayList<String> date2 = new ArrayList<>();
        date2.add("2029-09-21T04:00:00Z");
        date2.add("2029-09-22T04:00:00Z");
        date2.add("2029-09-23T04:00:00Z");
        date2.add("2029-09-24T04:00:00Z");
        final ArrayList<String> generatedFiles = new ArrayList<>();
        generatedFiles.add("test1/api/ae6a1610-fd30-4b81-8871-0f7f11f95426/delivery/v1/resources/09884b0e-a98d-4614-aebf-5388563c8edf?fit=inside%7C220:145");
        generatedFiles.add("test2/api/ae6a1610-fd30-4b81-8871-0f7f11f95426/delivery/v1/resources/09884b0e-a98d-4614-aebf-5388563c8edf?fit=inside%7C220:145");
        generatedFiles.add("test3/api/ae6a1610-fd30-4b81-8871-0f7f11f95426/delivery/v1/resources/09884b0e-a98d-4614-aebf-5388563c8edf?fit=inside%7C220:145");
        final ArrayList<String> location1 = new ArrayList<>();
        location1.add("/dxdam/3a/3a71b9cf-f6da-4b66-85f7-421c5a693d1e");
        location1.add("/dxdam/6f/6f38a240-7fb7-4be2-9304-5198e4350ee1");
        location1.add("/dxdam/6b/6bf5f2fa-03e8-4054-af2b-f5d78d3d6f55");
        final ArrayList<String> locations = new ArrayList<>();
        locations.add("/dxdam/3a/3a71b9cf-f6da-4b66-85f7-421c5a693d1e");
        locations.add("/dxdam/6f/6f38a240-7fb7-4be2-9304-5198e4350ee1");
        locations.add("/dxdam/6b/6bf5f2fa-03e8-4054-af2b-f5d78d3d6f55");
        final ArrayList<Double> number1 = new ArrayList<>();
        number1.add(10.65);
        number1.add(192.34);
        number1.add(23455.22);
        final ArrayList<Double> number2 = new ArrayList<>();
        number2.add(10.65);
        number2.add(192.34);
        number2.add(23455.22);
        final ArrayList<String> string1 = new ArrayList<>();
        string1.add("test_string1");
        string1.add("test_string2");
        string1.add("test_string3");
        string1.add("test_string4");
        final ArrayList<String> string2 = new ArrayList<>();
        string2.add("test_string1");
        string2.add("test_string2");
        string2.add("test_string3");
        string2.add("test_string4");
        final ArrayList<String> string3 = new ArrayList<>();
        string3.add("test_string1");
        string3.add("test_string2");
        string3.add("test_string3");
        string3.add("test_string4");
        final ArrayList<String> string4 = new ArrayList<>();
        string4.add("test_string1");
        string4.add("test_string2");
        string4.add("test_string3");
        string4.add("test_string4");
        final ArrayList<String> text = new ArrayList<>();
        text.add("copyright Oslo");
        text.add("salesNumber +1 (888) 777-5555");
        text.add("labelForCustomerService Customer service number");
        text.add("emailAddress customer-service@example.com");
        text.add("customerServiceContactNumber +1 (888) 777-5555");
        text.add("labelForSales Sales number");
        text.add("Logo");
        final ArrayList<String> testKeywords = new ArrayList<>();
        testKeywords.add("keyword1");
        testKeywords.add("keyword2");
        testKeywords.add("keyword3");
        testKeywords.add("keyword4");

        final ArrayList<String> testTags = new ArrayList<>();
        testTags.add("travel site sample");
        testTags.add("Sights");

        final DeliverySearchResponseDocument raw = new DeliverySearchResponseDocument();
        raw.boolean1 = boolean1;
        raw.boolean2 = boolean2;
        raw.categories = categories;
        raw.categoryLeaves = categoryLeaves;
        raw.date1 = date1;
        raw.date2 = date2;
        raw.document = createDocument();
        raw.generatedFiles = generatedFiles;
        raw.isManaged = true;
        raw.location1 = location1;
        raw.locations = locations;
        raw.number1 = number1;
        raw.number2 = number2;
        raw.status = "ready";
        raw.string1 = string1;
        raw.string2 = string2;
        raw.string3 = string3;
        raw.string4 = string4;
        raw.sortableDate1 = "2029-09-21T04:00:00Z";
        raw.sortableDate2 = "2029-09-21T04:00:00Z";
        raw.sortableNumber1 = 10.65;
        raw.sortableNumber2 = 192.34;
        raw.sortableString1 = "test_string1";
        raw.sortableString2 = "test_string2";
        raw.sortableString3 = "test_string3";
        raw.sortableString4 = "test_string4";
        raw.text = text;
        raw.type = "Footer";
        raw.typeId = "cbde3c3b-d03f-484b-8aa2-0c35227db10c";

        raw.classification = "ContentItem";
        raw.created = "2020-02-06T19:39:33.605Z";
        raw.creatorId = "30834b6f-0a47-4ef1-ac68-61ee5a2b7fbc";
        raw.description = "testDescription";
        raw.id = "r=126be044-ddaf-4c9c-bb07-156b1c97c592&a=6132847c-ecc6-41b7-83e0-20c29ace3d17";
        raw.keywords = testKeywords;
        raw.lastModified = "2020-02-06T19:39:58.416Z";
        raw.lastModifierId = "30834b6f-0a47-4ef1-ac68-61ee5a2b7fbc";
        raw.locale = "en:US";
        raw.name = "Canada Gallery Image";
        raw.restricted = true;
        raw.tags = testTags;

        return raw;
    }

    private JSONObject createDocument() throws JSONException {
        String obj = "{\n" +
                "    \"rev\" : \"1-2cdd9cf0f273c8a32e14046043071638\",\n" +
                "    \"thumbnail\" : {\n" +
                "      \"id\" : \"9c6440b5-512c-4cfc-9941-b1ad759f77f5\",\n" +
                "      \"url\" : \"/api/ae6a1610-fd30-4b81-8871-0f7f11f95426/delivery/v1/resources/b05c661d5e891602477ea9714733a975\"\n" +
                "    },\n" +
                "    \"keywords\" : [ ],\n" +
                "    \"kind\" : [ ],\n" +
                "    \"created\" : \"2020-01-07T17:52:31.610Z\",\n" +
                "    \"creatorId\" : \"00000000-0000-0000-0000-000000000009\",\n" +
                "    \"description\" : \"\",\n" +
                "    \"classification\" : \"content\",\n" +
                "    \"type\" : \"Event\",\n" +
                "    \"locale\" : \"en\",\n" +
                "    \"tags\" : [ \"oslo\", \"IBM sample\" ],\n" +
                "    \"selectedLayouts\" : [ {\n" +
                "      \"layout\" : {\n" +
                "        \"id\" : \"event-layout\"\n" +
                "      }\n" +
                "    } ],\n" +
                "    \"elements\" : {\n" +
                "      \"heading\" : {\n" +
                "        \"elementType\" : \"text\",\n" +
                "        \"value\" : \"Lecture: Decorating for twins\"\n" +
                "      },\n" +
                "      \"body\" : {\n" +
                "        \"elementType\" : \"formattedtext\",\n" +
                "        \"value\" : \"<p>Lorem ipsum dolor sit amet, cu dictas antiopam pri, mei augue iuvaret ea, eu vis sonet postulant. Viderer liberavisse qui at. Ad mel vitae recusabo, ius docendi tibique apeirian no, ea latine oporteat aliquando per. Etiam viris intellegam eum id, ad dico fabulas posidonium cum. Mea mutat legimus vulputate ut. Ignota consetetur necessitatibus te per, in nonumy definitionem sea.</p>\\n\"\n" +
                "      },\n" +
                "      \"eventDetails\" : {\n" +
                "        \"elementType\" : \"formattedtext\",\n" +
                "        \"value\" : \"<p>Voluptate tempora, consequuntur, et quisquam. Occaecat tempora but sequi nemo for magni. Nisi aperiam odit veritatis for duis or culpa. Qui architecto dolorem yet inventore. Suscipit vitae for corporis. Officia quam, or proident dolore yet.</p>\\n\"\n" +
                "      },\n" +
                "      \"date\" : {\n" +
                "        \"elementType\" : \"datetime\",\n" +
                "        \"value\" : \"2029-09-21T04:00:00Z\"\n" +
                "      },\n" +
                "      \"eventLocation\" : {\n" +
                "        \"elementType\" : \"formattedtext\",\n" +
                "        \"value\" : \"<p>555 Main St.<br />\\nAnywhere, USA</p>\\n\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"name\" : \"Ideas for Twins\",\n" +
                "    \"lastModifierId\" : \"00000000-0000-0000-0000-000000000009\",\n" +
                "    \"typeId\" : \"10ed9f3f-ab41-45a9-ba24-d988974affa7\",\n" +
                "    \"links\" : {\n" +
                "      \"thumbnail\" : {\n" +
                "        \"href\" : \"/authoring/v1/resources/b05c661d5e891602477ea9714733a975\"\n" +
                "      },\n" +
                "      \"createDraft\" : {\n" +
                "        \"href\" : \"/authoring/v1/content/b718161b-4219-4dc3-90d4-a663594b41e4/create-draft\"\n" +
                "      },\n" +
                "      \"retire\" : {\n" +
                "        \"href\" : \"/authoring/v1/changes/content/b718161b-4219-4dc3-90d4-a663594b41e4/status/retire\"\n" +
                "      },\n" +
                "      \"self\" : {\n" +
                "        \"href\" : \"/authoring/v1/content/b718161b-4219-4dc3-90d4-a663594b41e4\"\n" +
                "      },\n" +
                "      \"type\" : {\n" +
                "        \"href\" : \"/authoring/v1/types/10ed9f3f-ab41-45a9-ba24-d988974affa7\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"id\" : \"b718161b-4219-4dc3-90d4-a663594b41e4\",\n" +
                "    \"lastModified\" : \"2020-01-07T17:52:31.610Z\",\n" +
                "    \"systemModified\" : \"2020-01-07T17:52:31.873Z\",\n" +
                "    \"status\" : \"ready\"\n" +
                "  }";
        return new JSONObject(obj);
    }

    private String createExpectedToString(ContentItem contentItem) {
        return "ContentItem{" +
                "boolean1=" + contentItem.getBoolean1() +
                ", boolean2=" + contentItem.getBoolean2() +
                ", categories=" + contentItem.getCategories() +
                ", categoryLeaves=" + contentItem.getCategoryLeaves() +
                ", date1=" + contentItem.getDate1() +
                ", date2=" + contentItem.getDate2() +
                ", document=" + contentItem.getDocument() +
                ", generatedFiles=" + contentItem.getGeneratedFiles() +
                ", isManaged=" + contentItem.isManaged() +
                ", location1=" + contentItem.getLocation1() +
                ", locations=" + contentItem.getLocations() +
                ", number1=" + contentItem.getNumber1() +
                ", number2=" + contentItem.getNumber2() +
                ", status='" + contentItem.getStatus() + '\'' +
                ", string1=" + contentItem.getString1() +
                ", string2=" + contentItem.getString2() +
                ", string3=" + contentItem.getString3() +
                ", string4=" + contentItem.getString4() +
                ", sortableDate1='" + contentItem.getSortableDate1() + '\'' +
                ", sortableDate2='" + contentItem.getSortableDate2() + '\'' +
                ", sortableNumber1='" + contentItem.getSortableNumber1() + '\'' +
                ", sortableNumber2='" + contentItem.getSortableNumber2() + '\'' +
                ", sortableString1='" + contentItem.getSortableString1() + '\'' +
                ", sortableString2='" + contentItem.getSortableString2() + '\'' +
                ", sortableString3='" + contentItem.getSortableString3() + '\'' +
                ", sortableString4='" + contentItem.getSortableString4() + '\'' +
                ", text=" + contentItem.getText() +
                ", type='" + contentItem.getType() + '\'' +
                ", typeId='" + contentItem.getTypeId() + '\'' +
                "} " + "Document{" +
                "classification='" + contentItem.getClassification() + '\'' +
                ", created=" + contentItem.getCreated() +
                ", creatorId='" + contentItem.getCreatorId() + '\'' +
                ", description='" + contentItem.getDescription() + '\'' +
                ", id='" + contentItem.getId() + '\'' +
                ", keywords=" + contentItem.getKeywords() +
                ", lastModified=" + contentItem.getLastModified() +
                ", lastModifierId='" + contentItem.getLastModifierId() + '\'' +
                ", locale='" + contentItem.getLocale() + '\'' +
                ", name='" + contentItem.getName() + '\'' +
                ", restricted=" + contentItem.isRestricted() +
                ", tags=" + contentItem.getTags() +
                '}';
    }

    private ContentItem createContentItemToTest(InitFields callback) throws JSONException {
        final DeliverySearchResponseDocument responseDocument = new DeliverySearchResponseDocument();
        callback.initFields(responseDocument);
        return new ContentItem(responseDocument);
    }

    private ContentItem createContentItemToTest() {
        DeliverySearchResponseDocument responseDocument = new DeliverySearchResponseDocument();
        return new ContentItem(responseDocument);
    }

    private interface InitFields {
        void initFields(DeliverySearchResponseDocument responseDocument) throws JSONException;
    }

}