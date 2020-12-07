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

import java.util.ArrayList;

import static org.junit.Assert.*;


public class DocumentTest {

    @Test
    public void testCreation() {
        assertNotNull("Constructor should create not null instance.", createDocumentToTest());
    }

    @Test
    public void testGetClassification() {
        final String testValue = "asset";
        assertEquals(testValue, createDocumentToTest(rawDoc -> rawDoc.classification = testValue).getClassification());
    }

    @Test
    public void testGetClassificationNull() {
        assertNull(createDocumentToTest().getClassification());
    }

    @Test
    public void testGetCreated() {
        final String testValue = "2020-02-06T19:39:33.605Z";
        assertEquals(testValue, createDocumentToTest(rawDoc -> rawDoc.created = testValue).getCreated());
    }

    @Test
    public void testGetCreatedNull() {
        assertNull(createDocumentToTest().getCreated());
    }

    @Test
    public void testGetCreatorId() {
        final String testValue = "30834b6f-0a47-4ef1-ac68-61ee5a2b7fbc";
        assertEquals(testValue, createDocumentToTest(rawDoc -> rawDoc.creatorId = testValue).getCreatorId());
    }

    @Test
    public void testGetCreatorIdNull() {
        assertNull(createDocumentToTest().getCreatorId());
    }

    @Test
    public void testGetDescription() {
        final String testValue = "testDescription";
        assertEquals(testValue, createDocumentToTest(rawDoc -> rawDoc.description = testValue).getDescription());
    }

    @Test
    public void testGetDescriptionNull() {
        assertNull(createDocumentToTest().getDescription());
    }

    @Test
    public void testGetId() {
        final String testValue = "r=126be044-ddaf-4c9c-bb07-156b1c97c592&a=6132847c-ecc6-41b7-83e0-20c29ace3d17";
        assertEquals(testValue, createDocumentToTest(rawDoc -> rawDoc.id = testValue).getId());
    }

    @Test
    public void testGetIdNull() {
        assertNull(createDocumentToTest().getId());
    }

    @Test
    public void testKeywords() {
        ArrayList<String> testKeywords = new ArrayList<>();
        testKeywords.add("testKeyword1");
        testKeywords.add("testKeyword2");
        testKeywords.add("testKeyword3");
        assertEquals(testKeywords, createDocumentToTest(rawDoc -> rawDoc.keywords = new ArrayList<>(testKeywords)).getKeywords());
    }

    @Test
    public void testKeywordsEmpty() {
        assertEquals(new ArrayList<>(), createDocumentToTest(rawDoc -> rawDoc.keywords = new ArrayList<>()).getKeywords());
    }

    @Test
    public void testKeywordsNull() {
        assertNull(createDocumentToTest().getKeywords());
    }

    @Test
    public void testGetLastModified() {
        final String testValue = "2020-02-06T19:39:58.416Z";
        assertEquals(testValue, createDocumentToTest(rawDoc -> rawDoc.lastModified = testValue).getLastModified());
    }

    @Test
    public void testGetLastModifiedNull() {
        assertNull(createDocumentToTest().getLastModified());
    }

    @Test
    public void testGetLastModifierId() {
        final String testValue = "30834b6f-0a47-4ef1-ac68-61ee5a2b7fbc";
        assertEquals(testValue, createDocumentToTest(rawDoc -> rawDoc.lastModifierId = testValue).getLastModifierId());
    }

    @Test
    public void testGetLastModifierIdIdNull() {
        assertNull(createDocumentToTest().getLastModifierId());
    }

    @Test
    public void testGetLocale() {
        final String testValue = "en:US";
        assertEquals(testValue, createDocumentToTest(rawDoc -> rawDoc.locale = testValue).getLocale());
    }

    @Test
    public void testGetLocaleNull() {
        assertNull(createDocumentToTest().getLocale());
    }

    @Test
    public void testGetName() {
        final String testValue = "Canada Gallery Image";
        assertEquals(testValue, createDocumentToTest(rawDoc -> rawDoc.name = testValue).getName());
    }

    @Test
    public void testGetNameNull() {
        assertNull(createDocumentToTest().getName());
    }

    @Test
    public void testGetRestricted() {
        assertTrue(createDocumentToTest(rawDoc -> rawDoc.restricted = true).isRestricted());
    }

    @Test
    public void testGetRestrictedDefaults() {
        assertFalse(createDocumentToTest().isRestricted());
    }

    @Test
    public void testTags() {
        final ArrayList<String> testTags = new ArrayList<>();
        testTags.add("travel site sample");
        testTags.add("Sights");
        assertEquals(testTags, createDocumentToTest(rawDoc -> rawDoc.tags = new ArrayList<>(testTags)).getTags());
    }

    @Test
    public void testTagsEmpty() {
        assertEquals(new ArrayList<>(), createDocumentToTest(rawDoc -> rawDoc.tags = new ArrayList<>()).getTags());
    }

    @Test
    public void testTagsNull() {
        assertNull(createDocumentToTest().getTags());
    }

    @Test
    public void testEquals() {
        /*
         * Test fully equals objects
         */
        TestDocumentImpl testDocument001 = new TestDocumentImpl(createFullRawDoc());
        TestDocumentImpl testDocument002 = new TestDocumentImpl(createFullRawDoc());

        assertEquals(testDocument001, testDocument002);

        assertNotEquals(
                "Two objects with different created, should not be equals",
                createDocumentToTest(rawDoc -> rawDoc.classification = "asset"),
                createDocumentToTest(rawDoc -> rawDoc.classification = "contentItem")
        );
        assertNotEquals(
                "Two objects with different classification, should not be equals",
                createDocumentToTest(rawDoc -> rawDoc.created = "2020-02-06T19:39:33.605Z"),
                createDocumentToTest(rawDoc -> rawDoc.created = "2020-03-06T19:39:33.605Z")
        );
        assertNotEquals(
                "Two objects with different creatorId, should not be equals",
                createDocumentToTest(rawDoc -> rawDoc.creatorId = "30834b6f-0a47-4ef1-ac68-61ee5a2b7fbc"),
                createDocumentToTest(rawDoc -> rawDoc.creatorId = "test-30834b6f-0a47-4ef1-ac68-61ee5a2b7fbc")
        );
        assertNotEquals(
                "Two objects with different description, should not be equals",
                createDocumentToTest(rawDoc -> rawDoc.description = "testDescription01"),
                createDocumentToTest(rawDoc -> rawDoc.description = "testDescription02")
        );
        assertNotEquals(
                "Two objects with different id, should not be equals",
                createDocumentToTest(rawDoc -> rawDoc.id = "r=126be044-ddaf-4c9c-bb07-156b1c97c592&a=6132847c-ecc6-41b7-83e0-20c29ace3d17"),
                createDocumentToTest(rawDoc -> rawDoc.id = "test-r=126be044-ddaf-4c9c-bb07-156b1c97c592&a=6132847c-ecc6-41b7-83e0-20c29ace3d17")
        );

        final ArrayList<String> testKeywords = new ArrayList<>();
        testKeywords.add("keyword1");
        testKeywords.add("keyword2");
        testKeywords.add("keyword3");
        testKeywords.add("keyword4");
        assertNotEquals(
                "Two objects with different keywords, should not be equals",
                createDocumentToTest(rawDoc -> rawDoc.keywords = testKeywords),
                createDocumentToTest(rawDoc -> rawDoc.keywords = new ArrayList<>())
        );

        assertNotEquals(
                "Two objects with different lastModified, should not be equals",
                createDocumentToTest(rawDoc -> rawDoc.lastModified = "2020-02-06T19:39:58.416Z"),
                createDocumentToTest(rawDoc -> rawDoc.lastModified = "2020-03-06T19:39:58.416Z")
        );
        assertNotEquals(
                "Two objects with different lastModifierId, should not be equals",
                createDocumentToTest(rawDoc -> rawDoc.lastModifierId = "30834b6f-0a47-4ef1-ac68-61ee5a2b7fbc"),
                createDocumentToTest(rawDoc -> rawDoc.lastModifierId = "test-30834b6f-0a47-4ef1-ac68-61ee5a2b7fbc")
        );
        assertNotEquals(
                "Two objects with different locale, should not be equals",
                createDocumentToTest(rawDoc -> rawDoc.locale = "en:US"),
                createDocumentToTest(rawDoc -> rawDoc.locale = "en")
        );
        assertNotEquals(
                "Two objects with different name, should not be equals",
                createDocumentToTest(rawDoc -> rawDoc.name = "Canada Gallery Image"),
                createDocumentToTest(rawDoc -> rawDoc.name = "Canada Gallery Video")
        );
        assertNotEquals(
                "Two objects with different restricted, should not be equals",
                createDocumentToTest(rawDoc -> rawDoc.restricted = true),
                createDocumentToTest(rawDoc -> rawDoc.restricted = false)
        );

        final ArrayList<String> testTags = new ArrayList<>();
        testTags.add("travel site sample");
        testTags.add("Sights");
        assertNotEquals(
                "Two objects with different tags, should not be equals",
                createDocumentToTest(rawDoc -> rawDoc.tags = testTags),
                createDocumentToTest(rawDoc -> rawDoc.tags = new ArrayList<>())
        );
    }

    @Test
    public void testHashCode() {
        TestDocumentImpl testDocument001 = new TestDocumentImpl(createFullRawDoc());
        TestDocumentImpl testDocument002 = new TestDocumentImpl(createFullRawDoc());

        assertEquals("hashCode() same for equal objects.", testDocument001.hashCode(), testDocument002.hashCode());

        assertNotEquals(
                "hashCode() should be different for objects with different classification.",
                createDocumentToTest(rawDoc -> rawDoc.classification = "asset").hashCode(),
                createDocumentToTest(rawDoc -> rawDoc.classification = "contentItem").hashCode()
        );
        assertNotEquals(
                "hashCode() should be different for objects with different created.",
                createDocumentToTest(rawDoc -> rawDoc.created = "2020-02-06T19:39:33.605Z").hashCode(),
                createDocumentToTest(rawDoc -> rawDoc.created = "2020-03-06T19:39:33.605Z").hashCode()
        );
        assertNotEquals(
                "hashCode() should be different for objects with different creatorId.",
                createDocumentToTest(rawDoc -> rawDoc.creatorId = "30834b6f-0a47-4ef1-ac68-61ee5a2b7fbc").hashCode(),
                createDocumentToTest(rawDoc -> rawDoc.creatorId = "test-30834b6f-0a47-4ef1-ac68-61ee5a2b7fbc").hashCode()
        );
        assertNotEquals(
                "hashCode() should be different for objects with different description.",
                createDocumentToTest(rawDoc -> rawDoc.description = "testDescription01").hashCode(),
                createDocumentToTest(rawDoc -> rawDoc.description = "testDescription02").hashCode()
        );
        assertNotEquals(
                "hashCode() should be different for objects with different id.",
                createDocumentToTest(rawDoc -> rawDoc.id = "r=126be044-ddaf-4c9c-bb07-156b1c97c592&a=6132847c-ecc6-41b7-83e0-20c29ace3d17").hashCode(),
                createDocumentToTest(rawDoc -> rawDoc.id = "test-r=126be044-ddaf-4c9c-bb07-156b1c97c592&a=6132847c-ecc6-41b7-83e0-20c29ace3d17").hashCode()
        );

        final ArrayList<String> testKeywords = new ArrayList<>();
        testKeywords.add("keyword1");
        testKeywords.add("keyword2");
        testKeywords.add("keyword3");
        testKeywords.add("keyword4");

        assertNotEquals(
                "hashCode() should be different for objects with different keywords.",
                createDocumentToTest(rawDoc -> rawDoc.keywords = testKeywords).hashCode(),
                createDocumentToTest(rawDoc -> rawDoc.keywords = new ArrayList<>()).hashCode()
        );
        assertNotEquals(
                "hashCode() should be different for objects with different lastModified.",
                createDocumentToTest(rawDoc -> rawDoc.lastModified = "2020-02-06T19:39:58.416Z").hashCode(),
                createDocumentToTest(rawDoc -> rawDoc.lastModified = "2020-03-06T19:39:58.416Z").hashCode()
        );
        assertNotEquals(
                "hashCode() should be different for objects with different lastModifierId.",
                createDocumentToTest(rawDoc -> rawDoc.lastModifierId = "30834b6f-0a47-4ef1-ac68-61ee5a2b7fbc").hashCode(),
                createDocumentToTest(rawDoc -> rawDoc.lastModifierId = "test-30834b6f-0a47-4ef1-ac68-61ee5a2b7fbc").hashCode()
        );
        assertNotEquals(
                "hashCode() should be different for objects with different locale.",
                createDocumentToTest(rawDoc -> rawDoc.locale = "en:US").hashCode(),
                createDocumentToTest(rawDoc -> rawDoc.locale = "en").hashCode()
        );
        assertNotEquals(
                "hashCode() should be different for objects with different name.",
                createDocumentToTest(rawDoc -> rawDoc.name = "Canada Gallery Image").hashCode(),
                createDocumentToTest(rawDoc -> rawDoc.name = "Canada Gallery Video").hashCode()
        );
        assertNotEquals(
                "hashCode() should be different for objects with different restricted.",
                createDocumentToTest(rawDoc -> rawDoc.restricted = true).hashCode(),
                createDocumentToTest(rawDoc -> rawDoc.restricted = false).hashCode()
        );


        final ArrayList<String> testTags = new ArrayList<>();
        testTags.add("travel site sample");
        testTags.add("Sights");

        assertNotEquals(
                "hashCode() should be different for objects with different tags.",
                createDocumentToTest(rawDoc -> rawDoc.tags = testTags).hashCode(),
                createDocumentToTest(rawDoc -> rawDoc.tags = new ArrayList<>()).hashCode()
        );
    }

    @Test
    public void testToString() {
        TestDocumentImpl document = new TestDocumentImpl(createFullRawDoc());
        assertEquals(createExpectedToString(document), document.toString());
    }

    @Test
    public void testParcelable() {
        TestDocumentImpl source = new TestDocumentImpl(createFullRawDoc());

        Parcel parcel = MockParcel.obtain();
        source.writeToParcel(parcel, source.describeContents());
        parcel.setDataPosition(0);

        TestDocumentImpl createdFromParcel = new TestDocumentImpl(parcel);

        assertEquals("Builders should builder equals TestDocumentImpl instances ", source, createdFromParcel);
    }

    private DeliverySearchResponseDocument createFullRawDoc() {
        final ArrayList<String> testKeywords = new ArrayList<>();
        testKeywords.add("keyword1");
        testKeywords.add("keyword2");
        testKeywords.add("keyword3");
        testKeywords.add("keyword4");

        final ArrayList<String> testTags = new ArrayList<>();
        testTags.add("travel site sample");
        testTags.add("Sights");

        final DeliverySearchResponseDocument raw = new DeliverySearchResponseDocument();
        raw.classification = "asset";
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


    private String createExpectedToString(Document document) {
        return "Document{" +
                "classification='" + document.getClassification() + '\'' +
                ", created=" + document.getCreated() +
                ", creatorId='" + document.getCreatorId() + '\'' +
                ", description='" + document.getDescription() + '\'' +
                ", id='" + document.getId() + '\'' +
                ", keywords=" + document.getKeywords() +
                ", lastModified=" + document.getLastModified() +
                ", lastModifierId='" + document.getLastModifierId() + '\'' +
                ", locale='" + document.getLocale() + '\'' +
                ", name='" + document.getName() + '\'' +
                ", restricted=" + document.isRestricted() +
                ", tags=" + document.getTags() +
                '}';
    }


    private TestDocumentImpl createDocumentToTest(InitFields callback) {
        final DeliverySearchResponseDocument responseDocument = new DeliverySearchResponseDocument();
        callback.initFields(responseDocument);
        return new TestDocumentImpl(responseDocument);
    }

    private TestDocumentImpl createDocumentToTest() {
        DeliverySearchResponseDocument responseDocument = new DeliverySearchResponseDocument();
        return new TestDocumentImpl(responseDocument);
    }

    private interface InitFields {
        void initFields(DeliverySearchResponseDocument responseDocument);
    }

    private static class TestDocumentImpl extends Document {

        TestDocumentImpl(DeliverySearchResponseDocument rawDoc) {
            super(rawDoc);
        }

        TestDocumentImpl(Parcel source) {
            super(source);
        }
    }
}