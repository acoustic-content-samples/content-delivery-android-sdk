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
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DeliverySearchResultTest {

    @Test
    public void testDefaultConstructor() {
        final int numParameters = 120;
        final List<Asset> documents = new ArrayList<>();
        final Assets assets = mock(Assets.class);
        documents.add(createTestAsset("Canada-Article-Image", "image"));
        documents.add(createTestAsset("Canada-Country-Image.png", "image"));
        DeliverySearchResult<Asset> deliverySearchResult = new DeliverySearchResult<>(numParameters, assets, documents);

        assertNotNull(deliverySearchResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNumFoundLessThen0() {
        final int numParameters = -12;
        final List<Asset> documents = new ArrayList<>();
        final Assets assets = mock(Assets.class);
        new DeliverySearchResult<>(numParameters, assets, documents);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullRequest() {
        final int numParameters = 120;
        final List<Asset> documents = new ArrayList<>();
        final Assets assets = null;
        new DeliverySearchResult<>(numParameters, assets, documents);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullDocuments() {
        final int numParameters = 120;
        final List<Asset> documents = null;
        final Assets assets = mock(Assets.class);
        new DeliverySearchResult<>(numParameters, assets, documents);
    }

    @Test
    public void testRestoreConstructor() {
        /*
         * Creating initial (source) DeliverySearchResult state.
         */
        final int numParameters = 120;
        final List<Asset> documents = new ArrayList<>();
        final Assets assets = mock(Assets.class);
        documents.add(createTestAsset("Canada-Article-Image", "image"));
        documents.add(createTestAsset("Canada-Country-Image.png", "image"));
        final DeliverySearchResult<Asset> source = new DeliverySearchResult<>(numParameters, assets, documents);

        final DeliverySearchResult.State sourceState = source.getState();

        /*
         * Creating restored DeliverySearchResult.
         */
        final DeliverySearch deliverySearch = mock(DeliverySearch.class);
        when(deliverySearch.createDocuments(any(Documents.State.class))).thenReturn(assets);

        final DeliverySearchResult<Asset> restored = new DeliverySearchResult<>(deliverySearch, sourceState);

        assertNotNull(restored);
    }

    @Test(expected = NullPointerException.class)
    public void testRestoreConstructorWithNullState() {
        final Assets assets = mock(Assets.class);
        final DeliverySearch deliverySearch = mock(DeliverySearch.class);
        when(deliverySearch.createDocuments(any(Documents.State.class))).thenReturn(assets);

        new DeliverySearchResult<>(deliverySearch, null);
    }

    @Test(expected = NullPointerException.class)
    public void testRestoreConstructorWithNullDeliverySearch() {
        /*
         * Creating initial (source) DeliverySearchResult state.
         */
        final int numParameters = 120;
        final List<Asset> documents = new ArrayList<>();
        final Assets assets = mock(Assets.class);
        documents.add(createTestAsset("Canada-Article-Image", "image"));
        documents.add(createTestAsset("Canada-Country-Image.png", "image"));
        final DeliverySearchResult<Asset> source = new DeliverySearchResult<>(numParameters, assets, documents);

        final DeliverySearchResult.State sourceState = source.getState();

        /*
         * Creating restored DeliverySearchResult.
         */
        new DeliverySearchResult<>(null, sourceState);
    }

    @Test
    public void testGetNumFound() {
        final int numParameters = 120;
        final List<Asset> documents = new ArrayList<>();
        final Assets assets = mock(Assets.class);
        DeliverySearchResult<Asset> deliverySearchResult = new DeliverySearchResult<>(numParameters, assets, documents);
        assertEquals(numParameters, deliverySearchResult.getNumFound());
    }

    @Test
    public void testGetDocuments() {
        final int numParameters = 120;
        final List<Asset> documents = new ArrayList<>();
        documents.add(createTestAsset("Canada-Article-Image", "image"));
        documents.add(createTestAsset("Canada-Country-Image.png", "image"));
        final Assets assets = mock(Assets.class);
        DeliverySearchResult<Asset> deliverySearchResult = new DeliverySearchResult<>(numParameters, assets, documents);
        assertEquals(documents, deliverySearchResult.getDocuments());
    }

    @Test
    public void testGetState() {
        final int numParameters = 120;
        final List<Asset> documents = new ArrayList<>();
        documents.add(createTestAsset("Canada-Article-Image", "image"));
        documents.add(createTestAsset("Canada-Country-Image.png", "image"));
        final Assets assets = mock(Assets.class);
        DeliverySearchResult<Asset> deliverySearchResult = new DeliverySearchResult<>(numParameters, assets, documents);
        assertNotNull(deliverySearchResult.getState());
    }

    @Test
    public void testNextPage() {
        Assets assets = ContentDeliverySDK
                .create(SDKConfig.builder().setApiUrl("https://my7.test.com/").build())
                .deliverySearch()
                .assets();

        final int numParameters = 120;
        final List<Asset> documents = new ArrayList<>();
        documents.add(createTestAsset("Canada-Article-Image", "image"));
        documents.add(createTestAsset("Canada-Country-Image.png", "image"));

        DeliverySearchResult<Asset> deliverySearchResult = new DeliverySearchResult<>(numParameters, assets, documents);

        Documents nextPage = deliverySearchResult.nextPage();
        assertNotNull(nextPage);
        assertTrue(nextPage instanceof Assets);
    }

    @Test
    public void testPreviousPage() {
        Assets assets = ContentDeliverySDK
                .create(SDKConfig.builder().setApiUrl("https://my7.test.com/").build())
                .deliverySearch()
                .assets()
                .start(10);

        final int numParameters = 120;
        final List<Asset> documents = new ArrayList<>();
        documents.add(createTestAsset("Canada-Article-Image", "image"));
        documents.add(createTestAsset("Canada-Country-Image.png", "image"));

        DeliverySearchResult<Asset> deliverySearchResult = new DeliverySearchResult<>(numParameters, assets, documents);

        Documents previousPage = deliverySearchResult.previousPage();
        assertNotNull(previousPage);
        assertTrue(previousPage instanceof Assets);
    }

    @Test
    public void testPreviousPageWith0Start() {
        Assets assets = ContentDeliverySDK
                .create(SDKConfig.builder().setApiUrl("https://my7.test.com/").build())
                .deliverySearch()
                .assets()
                .start(0);

        final int numParameters = 120;
        final List<Asset> documents = new ArrayList<>();
        documents.add(createTestAsset("Canada-Article-Image", "image"));
        documents.add(createTestAsset("Canada-Country-Image.png", "image"));

        DeliverySearchResult<Asset> deliverySearchResult = new DeliverySearchResult<>(numParameters, assets, documents);

        Documents previousPage = deliverySearchResult.previousPage();
        assertNull(previousPage);
    }

    @Test
    public void testEquals() {
        /*
         * Test two equals objects
         */
        Assets assets = ContentDeliverySDK
                .create(SDKConfig.builder().setApiUrl("https://my7.test.com/").build())
                .deliverySearch()
                .assets()
                .start(0);

        final int numParameters = 120;
        final List<Asset> documents = new ArrayList<>();
        documents.add(createTestAsset("Canada-Article-Image", "image"));
        documents.add(createTestAsset("Canada-Country-Image.png", "image"));

        DeliverySearchResult<Asset> deliverySearchResult01 = new DeliverySearchResult<>(numParameters, assets, documents);
        DeliverySearchResult<Asset> deliverySearchResult02 = new DeliverySearchResult<>(numParameters, assets, documents);

        assertTrue(deliverySearchResult01.equals(deliverySearchResult02));

        /*
         * Test two objects with different request
         */
        Assets assets2 = ContentDeliverySDK
                .create(SDKConfig.builder().setApiUrl("https://my7.test.com/").build())
                .deliverySearch()
                .assets()
                .start(10);

        DeliverySearchResult<Asset> deliverySearchResult11 = new DeliverySearchResult<>(numParameters, assets, documents);
        DeliverySearchResult<Asset> deliverySearchResult12 = new DeliverySearchResult<>(numParameters, assets2, documents);
        assertFalse(deliverySearchResult11.equals(deliverySearchResult12));

        /*
         * Test two objects with different numFound
         */
        DeliverySearchResult<Asset> deliverySearchResult21 = new DeliverySearchResult<>(numParameters, assets, documents);
        DeliverySearchResult<Asset> deliverySearchResult22 = new DeliverySearchResult<>(111, assets, documents);

        assertFalse(deliverySearchResult21.equals(deliverySearchResult22));

        /*
         * Test two objects with different documents
         */
        DeliverySearchResult<Asset> deliverySearchResult31 = new DeliverySearchResult<>(numParameters, assets, documents);
        DeliverySearchResult<Asset> deliverySearchResult32 = new DeliverySearchResult<>(numParameters, assets, new ArrayList<>());

        assertFalse(deliverySearchResult31.equals(deliverySearchResult32));
    }

    @Test
    public void testHashCode() {
        /*
         * Test hashCode() same for equal objects.
         */
        Assets assets = ContentDeliverySDK
                .create(SDKConfig.builder().setApiUrl("https://my7.test.com/").build())
                .deliverySearch()
                .assets()
                .start(0);

        final int numParameters = 120;
        final List<Asset> documents = new ArrayList<>();
        documents.add(createTestAsset("Canada-Article-Image", "image"));
        documents.add(createTestAsset("Canada-Country-Image.png", "image"));

        DeliverySearchResult<Asset> deliverySearchResult01 = new DeliverySearchResult<>(numParameters, assets, documents);
        DeliverySearchResult<Asset> deliverySearchResult02 = new DeliverySearchResult<>(numParameters, assets, documents);
        assertEquals(deliverySearchResult01.hashCode(), deliverySearchResult02.hashCode());

        /*
         * Test hashCode() different for objects with different request.
         */
        Assets assets2 = ContentDeliverySDK
                .create(SDKConfig.builder().setApiUrl("https://my7.test.com/").build())
                .deliverySearch()
                .assets()
                .start(10);

        DeliverySearchResult<Asset> deliverySearchResult11 = new DeliverySearchResult<>(numParameters, assets, documents);
        DeliverySearchResult<Asset> deliverySearchResult12 = new DeliverySearchResult<>(numParameters, assets2, documents);
        assertNotEquals(deliverySearchResult11.hashCode(), deliverySearchResult12.hashCode());

        /*
         * Test hashCode() different for objects with different numFound.
         */
        DeliverySearchResult<Asset> deliverySearchResult21 = new DeliverySearchResult<>(numParameters, assets, documents);
        DeliverySearchResult<Asset> deliverySearchResult22 = new DeliverySearchResult<>(111, assets, documents);

        assertNotEquals(deliverySearchResult21.hashCode(), deliverySearchResult22.hashCode());

        /*
         * Test hashCode() different for objects with different documents.
         */
        DeliverySearchResult<Asset> deliverySearchResult31 = new DeliverySearchResult<>(numParameters, assets, documents);
        DeliverySearchResult<Asset> deliverySearchResult32 = new DeliverySearchResult<>(numParameters, assets, new ArrayList<>());

        assertNotEquals(deliverySearchResult31.hashCode(), deliverySearchResult32.hashCode());
    }

    @Test
    public void testStateParcelable() {
        Assets assets = ContentDeliverySDK
                .create(SDKConfig.builder().setApiUrl("https://my7.test.com/").build())
                .deliverySearch()
                .assets();

        final int numParameters = 120;
        final List<Asset> documents = new ArrayList<>();
        documents.add(createTestAsset("Canada-Article-Image", "image"));
        documents.add(createTestAsset("Canada-Country-Image.png", "image"));

        DeliverySearchResult.State source = new DeliverySearchResult<>(numParameters, assets, documents).getState();

        Parcel parcel = MockParcel.obtain();
        source.writeToParcel(parcel, source.describeContents());
        parcel.setDataPosition(0);

        DeliverySearchResult.State createdFromParcel = DeliverySearchResult.State.CREATOR.createFromParcel(parcel);

        assertEquals("Builders should builder equals DeliverySearchQuery instances ", source, createdFromParcel);

        final int testArraySize = 2;
        DeliverySearchResult.State[] testArray = DeliverySearchResult.State.CREATOR.newArray(testArraySize);
        assertNotNull(testArray);
        assertEquals(testArraySize, testArray.length);
    }

    @Test
    public void testStateEquals() {
        /*
         * Test two equals objects
         */
        Assets assets = ContentDeliverySDK
                .create(SDKConfig.builder().setApiUrl("https://my7.test.com/").build())
                .deliverySearch()
                .assets()
                .start(0);

        final int numParameters = 120;
        final List<Asset> documents = new ArrayList<>();
        documents.add(createTestAsset("Canada-Article-Image", "image"));
        documents.add(createTestAsset("Canada-Country-Image.png", "image"));

        DeliverySearchResult.State state01 = new DeliverySearchResult<>(numParameters, assets, documents).getState();
        DeliverySearchResult.State state02 = new DeliverySearchResult<>(numParameters, assets, documents).getState();

        assertTrue(state01.equals(state02));

        /*
         * Test two objects with different request
         */
        Assets assets2 = ContentDeliverySDK
                .create(SDKConfig.builder().setApiUrl("https://my7.test.com/").build())
                .deliverySearch()
                .assets()
                .start(10);

        DeliverySearchResult.State state11 = new DeliverySearchResult<>(numParameters, assets, documents).getState();
        DeliverySearchResult.State state12 = new DeliverySearchResult<>(numParameters, assets2, documents).getState();
        assertFalse(state11.equals(state12));

        /*
         * Test two objects with different numFound
         */
        DeliverySearchResult.State state21 = new DeliverySearchResult<>(numParameters, assets, documents).getState();
        DeliverySearchResult.State state22 = new DeliverySearchResult<>(111, assets, documents).getState();

        assertFalse(state21.equals(state22));

        /*
         * Test two objects with different documents
         */
        DeliverySearchResult.State state31 = new DeliverySearchResult<>(numParameters, assets, documents).getState();
        DeliverySearchResult.State state32 = new DeliverySearchResult<>(numParameters, assets, new ArrayList<>()).getState();

        assertFalse(state31.equals(state32));
    }

    @Test
    public void testStateHashCode() {
        /*
         * Test hashCode() same for equal objects.
         */
        Assets assets = ContentDeliverySDK
                .create(SDKConfig.builder().setApiUrl("https://my7.test.com/").build())
                .deliverySearch()
                .assets()
                .start(0);

        final int numParameters = 120;
        final List<Asset> documents = new ArrayList<>();
        documents.add(createTestAsset("Canada-Article-Image", "image"));
        documents.add(createTestAsset("Canada-Country-Image.png", "image"));

        DeliverySearchResult.State state01 = new DeliverySearchResult<>(numParameters, assets, documents).getState();
        DeliverySearchResult.State state02 = new DeliverySearchResult<>(numParameters, assets, documents).getState();
        assertEquals(state01.hashCode(), state02.hashCode());

        /*
         * Test hashCode() different for objects with different request.
         */
        Assets assets2 = ContentDeliverySDK
                .create(SDKConfig.builder().setApiUrl("https://my7.test.com/").build())
                .deliverySearch()
                .assets()
                .start(10);

        DeliverySearchResult.State state11 = new DeliverySearchResult<>(numParameters, assets, documents).getState();
        DeliverySearchResult.State state12 = new DeliverySearchResult<>(numParameters, assets2, documents).getState();
        assertNotEquals(state11.hashCode(), state12.hashCode());

        /*
         * Test hashCode() different for objects with different numFound.
         */
        DeliverySearchResult.State state21 = new DeliverySearchResult<>(numParameters, assets, documents).getState();
        DeliverySearchResult.State state22 = new DeliverySearchResult<>(111, assets, documents).getState();

        assertNotEquals(state21.hashCode(), state22.hashCode());

        /*
         * Test hashCode() different for objects with different documents.
         */
        DeliverySearchResult.State state31 = new DeliverySearchResult<>(numParameters, assets, documents).getState();
        DeliverySearchResult.State state32 = new DeliverySearchResult<>(numParameters, assets, new ArrayList<>()).getState();

        assertNotEquals(state31.hashCode(), state32.hashCode());
    }

    private Asset createTestAsset(String name, String assetType) {
        DeliverySearchResponseDocument responseDocument = new DeliverySearchResponseDocument();
        responseDocument.assetType = assetType;
        responseDocument.name = name;
        return new Asset(responseDocument);
    }
}