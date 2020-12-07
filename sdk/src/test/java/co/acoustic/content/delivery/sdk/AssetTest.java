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

public class AssetTest {

    @Test
    public void testCreation() {
        assertNotNull("Constructor should create not null instance.", createAssetToTest());
    }

    @Test
    public void testGetAssetType() throws JSONException {
        final String testValue = "image";
        assertEquals(testValue, createAssetToTest(rawDoc -> rawDoc.assetType = testValue).getAssetType());
    }

    @Test
    public void testGetAssetTypeNull() {
        assertNull(createAssetToTest().getAssetType());
    }

    @Test
    public void testGetCategories() throws JSONException {
        final ArrayList<String> testValue = new ArrayList<>();
        testValue.add("Locations/North America/Canada");
        testValue.add("Locations/North America/US");
        assertEquals(testValue, createAssetToTest(rawDoc -> rawDoc.categories = testValue).getCategories());
    }

    @Test
    public void testGetCategoriesNull() {
        assertNull(createAssetToTest().getCategories());
    }

    @Test
    public void testGetCategoryLeaves() throws JSONException {
        final ArrayList<String> testValue = new ArrayList<>();
        testValue.add("Canada");
        testValue.add("Locations");
        assertEquals(testValue, createAssetToTest(rawDoc -> rawDoc.categoryLeaves = testValue).getCategoryLeaves());
    }

    @Test
    public void testGetCategoryLeavesNull() {
        assertNull(createAssetToTest().getCategoryLeaves());
    }

    @Test
    public void testGetDocument() throws JSONException {
        final JSONObject testValue = createDocument();
        assertEquals(testValue, createAssetToTest(rawDoc -> rawDoc.document = testValue).getDocument());
    }

    @Test
    public void testGetDocumentNull() {
        assertNull(createAssetToTest().getDocument());
    }

    @Test
    public void testGetFileSize() throws JSONException {
        final long testValue = 1130797L;
        assertEquals(testValue, createAssetToTest(rawDoc -> rawDoc.fileSize = testValue).getFileSize());
    }

    @Test
    public void testIsManaged() throws JSONException {
        final boolean testValue = true;
        assertEquals(testValue, createAssetToTest(rawDoc -> rawDoc.isManaged = testValue).isManaged());
    }

    @Test
    public void testIsManagedDefault() {
        assertFalse(createAssetToTest().isManaged());
    }

    @Test
    public void testGetLocation() throws JSONException {
        final String testValue = "/dxdam/3a/3a71b9cf-f6da-4b66-85f7-421c5a693d1e";
        assertEquals(testValue, createAssetToTest(rawDoc -> rawDoc.location = testValue).getLocation());
    }

    @Test
    public void testGetLocationNull() {
        assertNull(createAssetToTest().getLocation());
    }

    @Test
    public void testGetLocationPath() throws JSONException {
        final String testValue = "/dxdam/3a/3a71b9cf-f6da-4b66-85f7-421c5a693d1e";
        assertEquals(testValue, createAssetToTest(rawDoc -> rawDoc.locationPaths = testValue).getLocationPaths());
    }

    @Test
    public void testGetLocationPathNull() {
        assertNull(createAssetToTest().getLocationPaths());
    }

    @Test
    public void testGetMedia() throws JSONException {
        final String testValue = "/delivery/v1/resources/53b94276-9619-4f7a-bce8-7d663117bd51.png";
        assertEquals(testValue, createAssetToTest(rawDoc -> rawDoc.media = testValue).getMedia());
    }

    @Test
    public void testGetMediaNull() {
        assertNull(createAssetToTest().getMedia());
    }

    @Test
    public void testGetMediaType() throws JSONException {
        final String testValue = "image/png";
        assertEquals(testValue, createAssetToTest(rawDoc -> rawDoc.mediaType = testValue).getMediaType());
    }

    @Test
    public void testGetMediaTypeNull() {
        assertNull(createAssetToTest().getMediaType());
    }

    @Test
    public void testGetPath() throws JSONException {
        final String testValue = "/dxdam/6f/6f38a240-7fb7-4be2-9304-5198e4350ee1/image.png";
        assertEquals(testValue, createAssetToTest(rawDoc -> rawDoc.path = testValue).getPath());
    }

    @Test
    public void testGetPathNull() {
        assertNull(createAssetToTest().getPath());
    }

    @Test
    public void testGetResource() throws JSONException {
        final String testValue = "126be044-ddaf-4c9c-bb07-156b1c97c592";
        assertEquals(testValue, createAssetToTest(rawDoc -> rawDoc.resource = testValue).getResource());
    }

    @Test
    public void testGetResourcesNull() {
        assertNull(createAssetToTest().getResource());
    }

    @Test
    public void testGetStatus() throws JSONException {
        final String testValue = "complete";
        assertEquals(testValue, createAssetToTest(rawDoc -> rawDoc.status = testValue).getStatus());
    }

    @Test
    public void testGetStatusNull() {
        assertNull(createAssetToTest().getStatus());
    }

    @Test
    public void testGetThumbnail() throws JSONException {
        final String testValue = "/delivery/v1/resources/126be044-ddaf-4c9c-bb07-156b1c97c592?fit=inside%7C220:145";
        assertEquals(testValue, createAssetToTest(rawDoc -> rawDoc.thumbnail = testValue).getThumbnail());
    }

    @Test
    public void testGetThumbnailNull() {
        assertNull(createAssetToTest().getThumbnail());
    }

    @Test
    public void testGetUrl() throws JSONException {
        final String testValue = "/api/ae6a1610-fd30-4b81-8871-0f7f11f95426/delivery/v1/resources/df1678a1-7ac4-4aab-a590-5e22411a8904?fit=inside%7C220:145";
        assertEquals(testValue, createAssetToTest(rawDoc -> rawDoc.url = testValue).getUrl());
    }

    @Test
    public void testGetUrlNull() {
        assertNull(createAssetToTest().getUrl());
    }

    @Test
    public void testToString() throws JSONException {
        Asset document = new Asset(createFullRawDoc());
        assertEquals(createExpectedToString(document), document.toString());
    }

    @Test
    public void testEquals() throws JSONException {
        /*
         * Test fully equals objects
         */
        assertEquals(new Asset(createFullRawDoc()), new Asset(createFullRawDoc()));

        assertNotEquals(
                "Two objects with different assetType, should not be equals",
                createAssetToTest(rawDoc -> rawDoc.assetType = "image"),
                createAssetToTest(rawDoc -> rawDoc.assetType = "video")
        );

        final ArrayList<String> categories1 = new ArrayList<>();
        categories1.add("Locations/North America/Canada");
        categories1.add("Locations/North America/US");
        final ArrayList<String> categories2 = new ArrayList<>();
        categories2.add("Locations/North America/Canada");

        assertNotEquals(
                "Two objects with different categories, should not be equals",
                createAssetToTest(rawDoc -> rawDoc.categories = categories1),
                createAssetToTest(rawDoc -> rawDoc.categories = categories2)
        );

        final ArrayList<String> categoryLeaves1 = new ArrayList<>();
        categoryLeaves1.add("Canada");
        categoryLeaves1.add("Locations");

        final ArrayList<String> categoryLeaves2 = new ArrayList<>();
        categoryLeaves1.add("Locations");
        assertNotEquals(
                "Two objects with different categoryLeaves, should not be equals",
                createAssetToTest(rawDoc -> rawDoc.categoryLeaves = categoryLeaves1),
                createAssetToTest(rawDoc -> rawDoc.categoryLeaves = categoryLeaves2)
        );
        assertNotEquals(
                "Two objects with different document, should not be equals",
                createAssetToTest(rawDoc -> rawDoc.document = createDocument()),
                createAssetToTest(rawDoc -> rawDoc.document = new JSONObject("{ \"classification\" : \"content\" }"))
        );
        assertNotEquals(
                "Two objects with different fileSize, should not be equals",
                createAssetToTest(rawDoc -> rawDoc.fileSize = 1130797L),
                createAssetToTest(rawDoc -> rawDoc.fileSize = 1130807L)
        );
        assertNotEquals(
                "Two objects with different isManaged, should not be equals",
                createAssetToTest(rawDoc -> rawDoc.isManaged = true),
                createAssetToTest(rawDoc -> rawDoc.isManaged = false)
        );
        assertNotEquals(
                "Two objects with different location, should not be equals",
                createAssetToTest(rawDoc -> rawDoc.location = "/dxdam/3a/3a71b9cf-f6da-4b66-85f7-421c5a693d1e"),
                createAssetToTest(rawDoc -> rawDoc.location = "/dxdam-test/3a/3a71b9cf-f6da-4b66-85f7-421c5a693d1e")
        );
        assertNotEquals(
                "Two objects with different locationPaths, should not be equals",
                createAssetToTest(rawDoc -> rawDoc.locationPaths = "/dxdam/3a/3a71b9cf-f6da-4b66-85f7-421c5a693d1e"),
                createAssetToTest(rawDoc -> rawDoc.locationPaths = "/dxdam-test/3a/3a71b9cf-f6da-4b66-85f7-421c5a693d1e")
        );
        assertNotEquals(
                "Two objects with different media, should not be equals",
                createAssetToTest(rawDoc -> rawDoc.media = "/delivery/v1/resources/53b94276-9619-4f7a-bce8-7d663117bd51.png"),
                createAssetToTest(rawDoc -> rawDoc.media = "/delivery-test/v1/resources/53b94276-9619-4f7a-bce8-7d663117bd51.png")
        );
        assertNotEquals(
                "Two objects with different mediaType, should not be equals",
                createAssetToTest(rawDoc -> rawDoc.mediaType = "image/png"),
                createAssetToTest(rawDoc -> rawDoc.mediaType = "image/jpg")
        );
        assertNotEquals(
                "Two objects with different path, should not be equals",
                createAssetToTest(rawDoc -> rawDoc.path = "/dxdam/6f/6f38a240-7fb7-4be2-9304-5198e4350ee1/image.png"),
                createAssetToTest(rawDoc -> rawDoc.path = "/dxdam-test/6f/6f38a240-7fb7-4be2-9304-5198e4350ee1/image.png")
        );
        assertNotEquals(
                "Two objects with different resource, should not be equals",
                createAssetToTest(rawDoc -> rawDoc.resource = "126be044-ddaf-4c9c-bb07-156b1c97c592"),
                createAssetToTest(rawDoc -> rawDoc.resource = "TEST-126be044-ddaf-4c9c-bb07-156b1c97c592")
        );
        assertNotEquals(
                "Two objects with different status, should not be equals",
                createAssetToTest(rawDoc -> rawDoc.status = "complete"),
                createAssetToTest(rawDoc -> rawDoc.status = "in progress")
        );
        assertNotEquals(
                "Two objects with different thumbnail, should not be equals",
                createAssetToTest(rawDoc -> rawDoc.thumbnail = "/delivery/v1/resources/126be044-ddaf-4c9c-bb07-156b1c97c592?fit=inside%7C220:145"),
                createAssetToTest(rawDoc -> rawDoc.thumbnail = "/delivery-test/v1/resources/126be044-ddaf-4c9c-bb07-156b1c97c592?fit=inside%7C220:145")
        );
        assertNotEquals(
                "Two objects with different url, should not be equals",
                createAssetToTest(rawDoc -> rawDoc.url = "/api/ae6a1610-fd30-4b81-8871-0f7f11f95426/delivery/v1/resources/df1678a1-7ac4-4aab-a590-5e22411a8904?fit=inside%7C220:145"),
                createAssetToTest(rawDoc -> rawDoc.url = "/api-test/ae6a1610-fd30-4b81-8871-0f7f11f95426/delivery/v1/resources/df1678a1-7ac4-4aab-a590-5e22411a8904?fit=inside%7C220:145")
        );
    }

    @Test
    public void testHashCode() throws JSONException {
        assertEquals("hashCode() same for equal objects.", new Asset(createFullRawDoc()).hashCode(), new Asset(createFullRawDoc()).hashCode());

        assertNotEquals(
                "hashCode() should be different for objects with different assetType.",
                createAssetToTest(rawDoc -> rawDoc.assetType = "image").hashCode(),
                createAssetToTest(rawDoc -> rawDoc.assetType = "video").hashCode()
        );

        final ArrayList<String> categories1 = new ArrayList<>();
        categories1.add("Locations/North America/Canada");
        categories1.add("Locations/North America/US");
        final ArrayList<String> categories2 = new ArrayList<>();
        categories2.add("Locations/North America/Canada");

        assertNotEquals(
                "hashCode() should be different for objects with different categories.",
                createAssetToTest(rawDoc -> rawDoc.categories = categories1).hashCode(),
                createAssetToTest(rawDoc -> rawDoc.categories = categories2).hashCode()
        );

        final ArrayList<String> categoryLeaves1 = new ArrayList<>();
        categoryLeaves1.add("Canada");
        categoryLeaves1.add("Locations");

        final ArrayList<String> categoryLeaves2 = new ArrayList<>();
        categoryLeaves1.add("Locations");
        assertNotEquals(
                "hashCode() should be different for objects with different categoryLeaves.",
                createAssetToTest(rawDoc -> rawDoc.categoryLeaves = categoryLeaves1).hashCode(),
                createAssetToTest(rawDoc -> rawDoc.categoryLeaves = categoryLeaves2).hashCode()
        );
        assertNotEquals(
                "hashCode() should be different for objects with different document.",
                createAssetToTest(rawDoc -> rawDoc.document = createDocument()).hashCode(),
                createAssetToTest(rawDoc -> rawDoc.document = new JSONObject("{ \"classification\" : \"content\" }")).hashCode()
        );
        assertNotEquals(
                "hashCode() should be different for objects with different fileSize.",
                createAssetToTest(rawDoc -> rawDoc.fileSize = 1130797L).hashCode(),
                createAssetToTest(rawDoc -> rawDoc.fileSize = 1130807L).hashCode()
        );
        assertNotEquals(
                "hashCode() should be different for objects with different isManaged.",
                createAssetToTest(rawDoc -> rawDoc.isManaged = true).hashCode(),
                createAssetToTest(rawDoc -> rawDoc.isManaged = false).hashCode()
        );
        assertNotEquals(
                "hashCode() should be different for objects with different location.",
                createAssetToTest(rawDoc -> rawDoc.location = "/dxdam/3a/3a71b9cf-f6da-4b66-85f7-421c5a693d1e").hashCode(),
                createAssetToTest(rawDoc -> rawDoc.location = "/dxdam-test/3a/3a71b9cf-f6da-4b66-85f7-421c5a693d1e").hashCode()
        );
        assertNotEquals(
                "hashCode() should be different for objects with different locationPaths.",
                createAssetToTest(rawDoc -> rawDoc.locationPaths = "/dxdam/3a/3a71b9cf-f6da-4b66-85f7-421c5a693d1e").hashCode(),
                createAssetToTest(rawDoc -> rawDoc.locationPaths = "/dxdam-test/3a/3a71b9cf-f6da-4b66-85f7-421c5a693d1e").hashCode()
        );
        assertNotEquals(
                "hashCode() should be different for objects with different media.",
                createAssetToTest(rawDoc -> rawDoc.media = "/delivery/v1/resources/53b94276-9619-4f7a-bce8-7d663117bd51.png").hashCode(),
                createAssetToTest(rawDoc -> rawDoc.media = "/delivery-test/v1/resources/53b94276-9619-4f7a-bce8-7d663117bd51.png").hashCode()
        );
        assertNotEquals(
                "hashCode() should be different for objects with different mediaType.",
                createAssetToTest(rawDoc -> rawDoc.mediaType = "image/png").hashCode(),
                createAssetToTest(rawDoc -> rawDoc.mediaType = "image/jpg").hashCode()
        );
        assertNotEquals(
                "hashCode() should be different for objects with different path.",
                createAssetToTest(rawDoc -> rawDoc.path = "/dxdam/6f/6f38a240-7fb7-4be2-9304-5198e4350ee1/image.png").hashCode(),
                createAssetToTest(rawDoc -> rawDoc.path = "/dxdam-test/6f/6f38a240-7fb7-4be2-9304-5198e4350ee1/image.png").hashCode()
        );
        assertNotEquals(
                "hashCode() should be different for objects with different resource.",
                createAssetToTest(rawDoc -> rawDoc.resource = "126be044-ddaf-4c9c-bb07-156b1c97c592").hashCode(),
                createAssetToTest(rawDoc -> rawDoc.resource = "TEST-126be044-ddaf-4c9c-bb07-156b1c97c592").hashCode()
        );
        assertNotEquals(
                "hashCode() should be different for objects with different status.",
                createAssetToTest(rawDoc -> rawDoc.status = "complete").hashCode(),
                createAssetToTest(rawDoc -> rawDoc.status = "in progress").hashCode()
        );
        assertNotEquals(
                "hashCode() should be different for objects with different thumbnail.",
                createAssetToTest(rawDoc -> rawDoc.thumbnail = "/delivery/v1/resources/126be044-ddaf-4c9c-bb07-156b1c97c592?fit=inside%7C220:145").hashCode(),
                createAssetToTest(rawDoc -> rawDoc.thumbnail = "/delivery-test/v1/resources/126be044-ddaf-4c9c-bb07-156b1c97c592?fit=inside%7C220:145").hashCode()
        );
        assertNotEquals(
                "hashCode() should be different for objects with different url.",
                createAssetToTest(rawDoc -> rawDoc.url = "/api/ae6a1610-fd30-4b81-8871-0f7f11f95426/delivery/v1/resources/df1678a1-7ac4-4aab-a590-5e22411a8904?fit=inside%7C220:145").hashCode(),
                createAssetToTest(rawDoc -> rawDoc.url = "/api-test/ae6a1610-fd30-4b81-8871-0f7f11f95426/delivery/v1/resources/df1678a1-7ac4-4aab-a590-5e22411a8904?fit=inside%7C220:145").hashCode()
        );
    }

    @Test
    public void testParcelable() throws JSONException {
        Asset source = new Asset(createFullRawDoc());

        Parcel parcel = MockParcel.obtain();
        source.writeToParcel(parcel, source.describeContents());
        parcel.setDataPosition(0);

        Asset createdFromParcel = Asset.CREATOR.createFromParcel(parcel);

        assertEquals("Builders should builder equals Asset instances ", source, createdFromParcel);
        final int testArraySize = 2;
        Asset[] testArray = Asset.CREATOR.newArray(testArraySize);
        assertNotNull(testArray);
        assertEquals(testArraySize, testArray.length);
    }

    private DeliverySearchResponseDocument createFullRawDoc() throws JSONException {
        final ArrayList<String> testKeywords = new ArrayList<>();
        testKeywords.add("keyword1");
        testKeywords.add("keyword2");
        testKeywords.add("keyword3");
        testKeywords.add("keyword4");

        final ArrayList<String> testTags = new ArrayList<>();
        testTags.add("travel site sample");
        testTags.add("Sights");

        final ArrayList<String> categories = new ArrayList<>();
        categories.add("Locations/North America/Canada");
        categories.add("Locations/North America/US");

        final ArrayList<String> categoryLeaves = new ArrayList<>();
        categoryLeaves.add("Canada");
        categoryLeaves.add("Locations");


        final DeliverySearchResponseDocument raw = new DeliverySearchResponseDocument();
        raw.assetType = "image";
        raw.categories = categories;
        raw.categoryLeaves = categoryLeaves;
        raw.document = createDocument();
        raw.fileSize = 1130797L;
        raw.isManaged = true;
        raw.location = "/dxdam/3a/3a71b9cf-f6da-4b66-85f7-421c5a693d1e";
        raw.locationPaths = "/dxdam/3a/3a71b9cf-f6da-4b66-85f7-421c5a693d1e";
        raw.media = "/delivery/v1/resources/53b94276-9619-4f7a-bce8-7d663117bd51.png";
        raw.mediaType = "image/png";
        raw.path = "/dxdam/6f/6f38a240-7fb7-4be2-9304-5198e4350ee1/image.png";
        raw.resource = "126be044-ddaf-4c9c-bb07-156b1c97c592";
        raw.status = "complete";
        raw.thumbnail = "/delivery/v1/resources/126be044-ddaf-4c9c-bb07-156b1c97c592?fit=inside%7C220:145";
        raw.url = "/api/ae6a1610-fd30-4b81-8871-0f7f11f95426/delivery/v1/resources/df1678a1-7ac4-4aab-a590-5e22411a8904?fit=inside%7C220:145";

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

    private String createExpectedToString(Asset asset) {
        return "Asset{" +
                "assetType='" + asset.getAssetType() + '\'' +
                ", categories=" + asset.getCategories() +
                ", categoryLeaves=" + asset.getCategoryLeaves() +
                ", document=" + asset.getDocument() +
                ", fileSize=" + asset.getFileSize() +
                ", isManaged=" + asset.isManaged() +
                ", location='" + asset.getLocation() + '\'' +
                ", locationPaths='" + asset.getLocationPaths() + '\'' +
                ", media='" + asset.getMedia() + '\'' +
                ", mediaType='" + asset.getMediaType() + '\'' +
                ", path='" + asset.getPath() + '\'' +
                ", resource='" + asset.getResource() + '\'' +
                ", status='" + asset.getStatus() + '\'' +
                ", thumbnail='" + asset.getThumbnail() + '\'' +
                ", url='" + asset.getUrl() + '\'' +
                "} " + "Document{" +
                "classification='" + asset.getClassification() + '\'' +
                ", created=" + asset.getCreated() +
                ", creatorId='" + asset.getCreatorId() + '\'' +
                ", description='" + asset.getDescription() + '\'' +
                ", id='" + asset.getId() + '\'' +
                ", keywords=" + asset.getKeywords() +
                ", lastModified=" + asset.getLastModified() +
                ", lastModifierId='" + asset.getLastModifierId() + '\'' +
                ", locale='" + asset.getLocale() + '\'' +
                ", name='" + asset.getName() + '\'' +
                ", restricted=" + asset.isRestricted() +
                ", tags=" + asset.getTags() +
                '}';
    }

    private Asset createAssetToTest(InitFields callback) throws JSONException {
        final DeliverySearchResponseDocument responseDocument = new DeliverySearchResponseDocument();
        callback.initFields(responseDocument);
        return new Asset(responseDocument);
    }

    private Asset createAssetToTest() {
        DeliverySearchResponseDocument responseDocument = new DeliverySearchResponseDocument();
        return new Asset(responseDocument);
    }

    private JSONObject createDocument() throws JSONException {
        String obj = "{\n" +
                "    \"rev\" : \"1-0108c7db7e623d25495ec96ec2518c14\",\n" +
                "    \"thumbnail\" : {\n" +
                "      \"id\" : \"a7b2cf43-badd-4530-a3b3-0d834883b692\",\n" +
                "      \"url\" : \"/api/ae6a1610-fd30-4b81-8871-0f7f11f95426/delivery/v1/resources/df1678a1-7ac4-4aab-a590-5e22411a8904?fit=inside%7C220:145\"\n" +
                "    },\n" +
                "    \"keywords\" : [ ],\n" +
                "    \"kind\" : [ ],\n" +
                "    \"created\" : \"2020-02-06T19:39:58.416Z\",\n" +
                "    \"libraryId\" : \"b066778e-7f87-4584-893a-a3f7c0e30232\",\n" +
                "    \"creatorId\" : \"30834b6f-0a47-4ef1-ac68-61ee5a2b7fbc\",\n" +
                "    \"description\" : \"\",\n" +
                "    \"classification\" : \"content\",\n" +
                "    \"type\" : \"Travel Article\",\n" +
                "    \"locale\" : \"en\",\n" +
                "    \"tags\" : [ \"travel site sample\" ],\n" +
                "    \"elements\" : {\n" +
                "      \"countryOfTravelArticle\" : {\n" +
                "        \"categoryIds\" : [ \"77abce3f-7a13-45ef-b1ce-11dbd11b61ff\" ],\n" +
                "        \"categories\" : [ \"Locations/North America/Canada\" ],\n" +
                "        \"elementType\" : \"category\"\n" +
                "      },\n" +
                "      \"travelArticleTitle\" : {\n" +
                "        \"elementType\" : \"text\",\n" +
                "        \"value\" : \"The 'Must Do' Guide for Canada\"\n" +
                "      },\n" +
                "      \"travelArticleImage\" : {\n" +
                "        \"mode\" : \"shared\",\n" +
                "        \"profiles\" : [ \"59414bcf-8ccb-4809-9117-47b7a64864a0\" ],\n" +
                "        \"renditions\" : {\n" +
                "          \"card\" : {\n" +
                "            \"source\" : \"/delivery/v1/resources/df1678a1-7ac4-4aab-a590-5e22411a8904?resize=830px:250px&crop=250:250;529,0\",\n" +
                "            \"width\" : 250,\n" +
                "            \"height\" : 250,\n" +
                "            \"transform\" : {\n" +
                "              \"scale\" : 0.43252595155709345,\n" +
                "              \"crop\" : {\n" +
                "                \"x\" : 529,\n" +
                "                \"y\" : 0,\n" +
                "                \"width\" : 250,\n" +
                "                \"height\" : 250\n" +
                "              }\n" +
                "            },\n" +
                "            \"url\" : \"/ae6a1610-fd30-4b81-8871-0f7f11f95426/dxresources/df16/df1678a1-7ac4-4aab-a590-5e22411a8904.png?resize=830px%3A250px&crop=250%3A250%3B529%2C0\"\n" +
                "          },\n" +
                "          \"default\" : {\n" +
                "            \"width\" : 1920,\n" +
                "            \"source\" : \"/delivery/v1/resources/df1678a1-7ac4-4aab-a590-5e22411a8904\",\n" +
                "            \"height\" : 578,\n" +
                "            \"url\" : \"/ae6a1610-fd30-4b81-8871-0f7f11f95426/dxresources/df16/df1678a1-7ac4-4aab-a590-5e22411a8904.png\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"asset\" : {\n" +
                "          \"fileName\" : \"Canada-Lead-Image.png\",\n" +
                "          \"altText\" : \"The landscape of Canada with a river, forest, and snowy mountains.\",\n" +
                "          \"fileSize\" : 617185,\n" +
                "          \"width\" : 1920,\n" +
                "          \"mediaType\" : \"image/png\",\n" +
                "          \"id\" : \"a7b2cf43-badd-4530-a3b3-0d834883b692\",\n" +
                "          \"resourceUri\" : \"/delivery/v1/resources/df1678a1-7ac4-4aab-a590-5e22411a8904\",\n" +
                "          \"height\" : 578\n" +
                "        },\n" +
                "        \"elementType\" : \"image\",\n" +
                "        \"url\" : \"/ae6a1610-fd30-4b81-8871-0f7f11f95426/dxresources/df16/df1678a1-7ac4-4aab-a590-5e22411a8904.png\"\n" +
                "      },\n" +
                "      \"articleAuthor\" : {\n" +
                "        \"elementType\" : \"text\",\n" +
                "        \"value\" : \"John Smith\"\n" +
                "      },\n" +
                "      \"travelArticleText\" : {\n" +
                "        \"elementType\" : \"formattedtext\",\n" +
                "        \"value\" : \"<p><em>We hope you have enough time to spare. Because the most important thing to do in Canada is to see ALL of Canada. One of the largest countries in the world by the area that has 10% of the world's forest, the longest coastline, incredible mountains, and waterfalls - all this hints that in the first place you need to get acquainted with the nature of this beautiful country.</em></p> \\n<h2>Do canoeing</h2> \\n<div style=\\\"text-align:center;clear:both;padding:0px 0px 5px\\\">\\n <img data-wch-asset-id=\\\"8bb42682-08dd-4b1f-8cc0-0deef30bfa0c\\\" height=\\\"500\\\" src=\\\"/ae6a1610-fd30-4b81-8871-0f7f11f95426/dxresources/53b9/53b94276-9619-4f7a-bce8-7d663117bd51.png\\\" width=\\\"750\\\">\\n</div> \\n<p>One of the best ways to see Canada is by taking a trip on a canoe.</p> \\n<p>There are hundreds of canoe routes across Canada, where you can enjoy seeing picturesque lakes, rivers, mountains, and wildlife, but make sure you add the following routes to your list:</p> \\n<ul>\\n <li>Johnstone Strait in British Columbia, where among typical Canadian wildlife (eagles, deer, otters, and seals) you can also see orca whales that feed on salmon from mid-summer to early autumn;</li>\\n <li> <p>Quetico Provincial Park in Ontario. The park area of 460 thousand hectares includes pine and spruce forests and more than 2,000 lakes. It operates from the mid-May to mid-October;</p> </li>\\n <li>Rideau Canal - a UNESCO World Heritage Site that connects the capital of Canada, Ottawa, with Kingston, Ontario.</li>\\n</ul> \\n<p>&nbsp;</p> \\n<h2>Visit Niagara Falls</h2> \\n<p><img data-wch-asset-id=\\\"09a014ea-1018-4b8e-9245-e7a8a0b5b9f0\\\" height=\\\"250\\\" src=\\\"/ae6a1610-fd30-4b81-8871-0f7f11f95426/dxresources/e328/e328e3a0-2c9c-4cfc-8969-2513cbde15f4.png\\\" style=\\\"float:right;padding:5px 0px 0px 10px\\\" width=\\\"375\\\">Niagara Falls is one of the world’s most famous waterfalls and the most visited place in Canada.</p> \\n<p>The Falls is not just one wide waterfall, but 3 connected waterfalls:</p> \\n<ol>\\n <li><strong>American Falls</strong> - named so, since it flows mainly over the border of US;</li>\\n <li><strong>Bridal Veil Falls</strong>&nbsp;that resembles&nbsp;- <em>surprise</em> - a bridal veil;</li>\\n <li><strong>Horseshoe Falls</strong>, named because of its horseshoe shape, and it is also known as the Canadian Falls due to being mostly in Ontario.</li>\\n</ol> \\n<p>&nbsp;</p> \\n<h2>Go skiing in Whistler</h2> \\n<p><img data-wch-asset-id=\\\"6132847c-ecc6-41b7-83e0-20c29ace3d17\\\" height=\\\"250\\\" src=\\\"/ae6a1610-fd30-4b81-8871-0f7f11f95426/dxresources/126b/126be044-ddaf-4c9c-bb07-156b1c97c592.png\\\" style=\\\"float:left;padding:0px 10px 0px 0px\\\" width=\\\"375\\\"></p> \\n<p>Canadian ski resorts are located on both east and west coasts, but the west coast is a homeland&nbsp;to the most famous resorts like Banff, Lake Louise, and Whistler.</p> \\n<p>Whistler is a 'flagship' resort in the country.&nbsp;It is conveniently located in the two-hour drive from Vancouver and has the biggest ski area in North America.</p> \\n<p>The popularity of this place certainly has its drawbacks. In high season, it's getting too crowded, thus lift queues are not unusual,&nbsp;and too expensive, making you consider other resorts.</p>\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"name\" : \"Canada Travel Article\",\n" +
                "    \"lastModifierId\" : \"30834b6f-0a47-4ef1-ac68-61ee5a2b7fbc\",\n" +
                "    \"typeId\" : \"020bc107-4d2a-4513-8988-bc0cd455bb98\",\n" +
                "    \"links\" : {\n" +
                "      \"thumbnail\" : {\n" +
                "        \"href\" : \"/authoring/v1/resources/df1678a1-7ac4-4aab-a590-5e22411a8904?fit=inside%7C220:145\"\n" +
                "      },\n" +
                "      \"createDraft\" : {\n" +
                "        \"href\" : \"/authoring/v1/content/f1492227-943b-4f8e-81a6-79bc939e101b/create-draft\"\n" +
                "      },\n" +
                "      \"retire\" : {\n" +
                "        \"href\" : \"/authoring/v1/changes/content/f1492227-943b-4f8e-81a6-79bc939e101b/status/retire\"\n" +
                "      },\n" +
                "      \"self\" : {\n" +
                "        \"href\" : \"/authoring/v1/content/f1492227-943b-4f8e-81a6-79bc939e101b\"\n" +
                "      },\n" +
                "      \"type\" : {\n" +
                "        \"href\" : \"/authoring/v1/types/020bc107-4d2a-4513-8988-bc0cd455bb98\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"id\" : \"f1492227-943b-4f8e-81a6-79bc939e101b\",\n" +
                "    \"lastModified\" : \"2020-02-06T19:39:58.416Z\",\n" +
                "    \"systemModified\" : \"2020-02-06T19:39:58.580Z\",\n" +
                "    \"status\" : \"ready\"\n" +
                "  }";
        return new JSONObject(obj);
    }

    private interface InitFields {
        void initFields(DeliverySearchResponseDocument responseDocument) throws JSONException;
    }
}