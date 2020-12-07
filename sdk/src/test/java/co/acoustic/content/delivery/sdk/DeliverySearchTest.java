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

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class DeliverySearchTest {

    @Test(expected = NullPointerException.class)
    public void testConstructorNullSDK() {
        new DeliverySearch(null);
    }

    @Test
    public void testConstructor() {
        DeliverySearch deliverySearch = new DeliverySearch(ContentDeliverySDK.create(SDKConfig.builder().setApiUrl("http://test.blah.com/").build()));
        assertNotNull("DeliverySearch instance should not be null", deliverySearch);
    }

    @Test
    public void testCreateAssets() {
        final ContentDeliverySDK sdk = ContentDeliverySDK.create(SDKConfig.builder().setApiUrl("http://test.blah.com/").build());

        final Assets assets1 = sdk.deliverySearch().assets();
        assertNotNull("DeliverySearch should never return null Assets instance", assets1);

        final Assets assets2 = sdk.deliverySearch().assets();
        assertNotEquals("DeliverySearch should always return new Assets instance", assets1, assets2);
    }

    @Test
    public void testCreateCategories() {
        final ContentDeliverySDK sdk = ContentDeliverySDK.create(SDKConfig.builder().setApiUrl("http://test.blah.com/").build());

        final Categories categories1 = sdk.deliverySearch().categories();
        assertNotNull("DeliverySearch should never return null Categories instance", categories1);

        final Categories categories2 = sdk.deliverySearch().categories();
        assertNotEquals("DeliverySearch should always return new Categories instance", categories1, categories2);
    }

    @Test
    public void testCreateContentItems() {
        final ContentDeliverySDK sdk = ContentDeliverySDK.create(SDKConfig.builder().setApiUrl("http://test.blah.com/").build());

        final ContentItems contentItems1 = sdk.deliverySearch().contentItems();
        assertNotNull("DeliverySearch should never return null ContentItems instance", contentItems1);

        final ContentItems contentItems2 = sdk.deliverySearch().contentItems();
        assertNotEquals("DeliverySearch should always return new Assets instance", contentItems1, contentItems2);
    }

    @Test
    public void testCreateContentTypes() {
        final ContentDeliverySDK sdk = ContentDeliverySDK.create(SDKConfig.builder().setApiUrl("http://test.blah.com/").build());

        final ContentTypes contentTypes1 = sdk.deliverySearch().contentTypes();
        assertNotNull("DeliverySearch should never return null ContentTypes instance", contentTypes1);

        final ContentTypes contentTypes2 = sdk.deliverySearch().contentTypes();
        assertNotEquals("DeliverySearch should always return new ContentTypes instance", contentTypes1, contentTypes2);
    }

    @Test
    public void testCreateAssetsFromState() {
        final ContentDeliverySDK sdk = ContentDeliverySDK.create(SDKConfig.builder().setApiUrl("http://test.blah.com/").build());
        final Assets assets1 = sdk.deliverySearch().assets();
        assets1.filterBy("blah", "blahVal");
        assets1.sortBy("blahSort", true);
        assets1.includeDraft(true);
        assets1.includeRetired(true);

        final Documents.State assets1State = assets1.getState();

        final Assets assets2 = sdk.deliverySearch().createDocuments(assets1State);
        final Documents.State assets2State = assets2.getState();

        assertEquals("Original Assets instance state and state of created instance should be equal", assets1State, assets2State);
    }

    @Test
    public void testCreateContentItemsFromState() {
        final ContentDeliverySDK sdk = ContentDeliverySDK.create(SDKConfig.builder().setApiUrl("http://test.blah.com/").build());
        final ContentItems contentItems1 = sdk.deliverySearch().contentItems();
        contentItems1.filterBy("blah", "blahVal");
        contentItems1.sortBy("blahSort", true);
        contentItems1.includeDraft(true);
        contentItems1.includeRetired(true);
        contentItems1.completeContentContext(true);
        contentItems1.protectedContent(true);

        final Documents.State contentItems1State = contentItems1.getState();

        final ContentItems contentItems2 = sdk.deliverySearch().createDocuments(contentItems1State);
        final Documents.State contentItems2State = contentItems2.getState();

        assertEquals("Original ContentItems instance state and state of created instance should be equal", contentItems1State, contentItems2State);
    }

    @Test
    public void testCreateCategoriesFromState() {
        final ContentDeliverySDK sdk = ContentDeliverySDK.create(SDKConfig.builder().setApiUrl("http://test.blah.com/").build());
        final Categories categories1 = sdk.deliverySearch().categories();
        categories1.filterBy("blah", "blahVal");
        categories1.sortBy("blahSort", true);

        final Documents.State categories1State = categories1.getState();

        final Categories categories2 = sdk.deliverySearch().createDocuments(categories1State);
        final Documents.State categories2State = categories2.getState();

        assertEquals("Original Categories instance state and state of created instance should be equal", categories1State, categories2State);
    }

    @Test
    public void testCreateContentTypesFromState() {
        final ContentDeliverySDK sdk = ContentDeliverySDK.create(SDKConfig.builder().setApiUrl("http://test.blah.com/").build());
        final ContentTypes contentTypes1 = sdk.deliverySearch().contentTypes();
        contentTypes1.filterBy("blah", "blahVal");
        contentTypes1.sortBy("blahSort", true);

        final Documents.State contentTypes1State = contentTypes1.getState();

        final ContentTypes contentTypes2 = sdk.deliverySearch().createDocuments(contentTypes1State);
        final Documents.State contentTypes2State = contentTypes2.getState();

        assertEquals("Original ContentTypes instance state and state of created instance should be equal", contentTypes1State, contentTypes2State);
    }

    @Test
    public void testCreateDeliverySearchResult() {
        final ContentDeliverySDK sdk = ContentDeliverySDK.create(SDKConfig.builder().setApiUrl("http://test.blah.com/").build());
        DeliverySearchResponseDocument mockDeliverySearchResponseDocument = new DeliverySearchResponseDocument();
        mockDeliverySearchResponseDocument.classification = "asset";
        mockDeliverySearchResponseDocument.id = "assetId";
        final Asset mockAsset = new Asset(mockDeliverySearchResponseDocument);
        final List<Asset> mockDeliverySearchResultDocuments = new ArrayList<>();
        mockDeliverySearchResultDocuments.add(mockAsset);

        final DeliverySearchResult<Asset> deliverySearchResult1 = new DeliverySearchResult<>(
                1,
                sdk.deliverySearch().assets(),
                mockDeliverySearchResultDocuments
        );

        final DeliverySearchResult.State deliverySearchResult1State = deliverySearchResult1.getState();

        final DeliverySearchResult<Asset> deliverySearchResult2 = sdk.deliverySearch().createDeliverySearchResult(deliverySearchResult1State);
        final DeliverySearchResult.State deliverySearchResult2State = deliverySearchResult2.getState();

        assertEquals("Original DeliverySearchResult instance state and state of created instance should be equal", deliverySearchResult1State, deliverySearchResult2State);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDocumentsWrongClass() {
        final ContentDeliverySDK sdk = ContentDeliverySDK.create(SDKConfig.builder().setApiUrl("http://test.blah.com/").build());
        sdk.deliverySearch().createDocuments(
                new Documents.State(
                        DeliverySearchTest.class,
                        new DeliverySearchQueryBuilder(),
                        false,
                        false,
                        false,
                        true,
                        false
                )
        );
    }
}