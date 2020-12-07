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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CategoryTest {

    @Test
    public void testCreation() {
        Category category = new Category(new DeliverySearchResponseDocument());
        assertNotNull("Constructor should create not null instance.", category);
    }

    @Test
    public void testToString() {
        Category document = new Category(createFullRawDoc());
        assertEquals(createExpectedToString(document), document.toString());
    }

    @Test
    public void testParcelable() {
        Category source = new Category(createFullRawDoc());

        Parcel parcel = MockParcel.obtain();
        source.writeToParcel(parcel, source.describeContents());
        parcel.setDataPosition(0);

        Category createdFromParcel = Category.CREATOR.createFromParcel(parcel);

        assertEquals("Builders should builder equals TestDocumentImpl instances ", source, createdFromParcel);
        final int testArraySize = 2;
        Category[] testArray = Category.CREATOR.newArray(testArraySize);
        assertNotNull(testArray);
        assertEquals(testArraySize, testArray.length);
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
        return "Category{} " + "Document{" +
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
}