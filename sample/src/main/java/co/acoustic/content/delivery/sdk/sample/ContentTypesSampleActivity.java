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

package co.acoustic.content.delivery.sdk.sample;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import co.acoustic.content.delivery.sdk.ContentDeliverySDK;
import co.acoustic.content.delivery.sdk.ContentType;
import co.acoustic.content.delivery.sdk.ContentTypes;
import co.acoustic.content.delivery.sdk.DeliverySearch;
import co.acoustic.content.delivery.sdk.DeliverySearchResult;
import co.acoustic.content.delivery.sdk.Documents;

import static java.lang.Math.max;

/**
 * The {@link BaseSampleActivity} implementation, demonstrates using  {@link ContentDeliverySDK} for working with ContentTypes.
 */
public class ContentTypesSampleActivity extends BaseSampleActivity {
    /*
     * The global SDK instance.
     */
    private ContentDeliverySDK sdk;
    private DeliverySearch deliverySearch;

    /*
     * ContentTypes instance which holds search parameters for initial query.
     * The could be updated on QueryBuilder Screen. See onQueryBuilderResultReady() method.
     */
    private ContentTypes initialSearch;

    /*
     * The start position of the initial search query.
     * The could be updated on QueryBuilder Screen. See onQueryBuilderResultReady() method.
     */
    private int searchStartPosition = 0;
    /*
     * Represents index of the first found ContentTypes element. Used for displaying this index on Sample Screen.
     */
    private int firstLoadedItem = -1;
    /*
     * Represents index of the last found ContentTypes element. Used for displaying this index on Sample Screen.
     */
    private int lastLoadedItem = -1;

    /*
     * Holds successfully loaded DeliverySearchResults, each DeliverySearchResult represents a delivery search page. For more details check API docs.
     */
    private List<DeliverySearchResult<ContentType>> loadedPages = new LinkedList<>();
    /*
     * Represents ContentTypes results, converted to format understandable for RecyclerView.Adapter.
     */
    private List<ResultItem> resultItems = new LinkedList<>();

    @NonNull
    @Override
    protected ContentDeliverySDK getContentDeliverySDK() {
        if (sdk == null) {
            /*
             * Initialize sdk, delivery search and default initialSearch instances.
             */
            sdk = ((SampleApplication) getApplication()).getSDK();
            deliverySearch = sdk.deliverySearch();
            initialSearch = deliverySearch.contentTypes();
        }
        return sdk;
    }

    @Override
    protected String getSampleTitle() {
        return getString(R.string.activity_sample_content_types);
    }

    @NonNull
    @Override
    protected Class<?> getQueryBuilderScreenClass() {
        return ContentTypesQueryBuilderActivity.class;
    }

    @Override
    protected void onQueryBuilderResultReady(int searchStartPosition, Documents.State state) {
        this.searchStartPosition = searchStartPosition;
        initialSearch = deliverySearch.createDocuments(state);
    }

    @Override
    protected void launchInitialQuery() {
        /*
         * Making initial search Query call.
         * For more details check Delivery Search API docs.
         */
        initialSearch
                .get()
                .then(this::handleNextPageResult)
                .error(this::handleError);
    }

    @Override
    protected void loadPrevPage() {
        /*
         * Triggering previous page loading, for a first already loaded page, if the first loaded page isn't first page in the search
         * (isn't starts from 0 which is index of the first element in the overall search).
         * In other case just notify parent to refresh the list view.
         */
        ContentTypes prevPage = loadedPages.get(0).previousPage();
        if (prevPage != null) {
            prevPage
                    .get()
                    .then(this::handlePrevPageResult)
                    .error(this::handleError);
        } else {
            notifyDataUpdated();
        }
    }

    @Override
    protected void loadNextPage() {
        /*
         * Triggering next page loading, for the last already loaded page.
         */
        ContentTypes nextPage = loadedPages.get(loadedPages.size() - 1).nextPage();
        if (nextPage != null) {
            nextPage
                    .get()
                    .then(this::handleNextPageResult)
                    .error(this::handleError);
        } else {
            notifyDataUpdated();
        }
    }

    @Override
    protected List<ResultItem> getSource() {
        return resultItems;
    }



    @Override
    protected void clear() {
        super.clear();

        /*
         * Clearing all loaded data including pagination state.
         */
        firstLoadedItem = -1;
        lastLoadedItem = -1;
        loadedPages.clear();
        resultItems.clear();
    }

    /**
     * Called when previous page request successfully finished, and return appropriate result.
     *
     * @param result the {@link DeliverySearchResult<ContentType>} instance.
     */
    private void handlePrevPageResult(DeliverySearchResult<ContentType> result) {
        /*
         * Adding the result as first loaded page.
         */
        loadedPages.add(0, result);

        final List<ContentType> items = result.getDocuments();

        /*
         * Updating index of the first loaded item.
         * if it wasn't initialized before (result obtained from initial call) than initialize it with searchStartPosition,
         * in other case it's calculating as current firstLoadedItem minus number of documents in the result, but it can't be less then 0.
         */
        final int newValue = (firstLoadedItem == -1)
                ? searchStartPosition
                : max(firstLoadedItem - items.size(), 0);
        firstLoadedItem = max(newValue, 0);

        /*
         * Updating index of the last loaded item. That makes sense only if lastLoadedItem wasn't initialized, that can happen only if result instance was obtained from initial call.
         * Normally that value should be managed by handleNextPageResult() method.
         *
         * lastLoadedItem index calculating as first loaded item index plus number of documents in the result.
         */
        if (lastLoadedItem == -1) {
            lastLoadedItem = firstLoadedItem + items.size();
        }

        /*
         * Parsing all documents from the result to ResultItem's and add them all to begin of the list for displaying. Then refreshing UI. For more details see methods JavaDocs.
         */
        this.resultItems.addAll(0, parseContentTypes(firstLoadedItem, items));
        updateLoadedItemIndexes(firstLoadedItem, lastLoadedItem, result.getNumFound());
        notifyDataUpdated();
    }

    /**
     * Called when initial or next page request successfully finished, and return appropriate result.
     *
     * @param result the {@link DeliverySearchResult<ContentType>} instance.
     */
    private void handleNextPageResult(DeliverySearchResult<ContentType> result) {
        /*
         * Adding the result as last loaded page.
         */
        loadedPages.add(result);

        /*
         * Calculating index of the first document in the result index, based on previous search results.
         *
         * if lastLoadedItem wasn't initialized before (result obtained from initial call) than startIndex initialized with searchStartPosition,
         * in other case it's calculating based on lastLoadedItem.
         *
         * NOTE: startIndex - is value that make sense only in scope of the sample, it is used only for adding indexes to ResultItem's. For more details see parseContentTypes() method.
         */
        final int startIndex = (lastLoadedItem == -1)
                ? searchStartPosition
                : (lastLoadedItem + 1);

        /*
         * Parsing all documents from the result to ResultItem's and add them all to the end list for displaying.
         */
        resultItems.addAll(
                parseContentTypes(
                        startIndex,
                        result.getDocuments()));

        /*
         * Updating index of the first loaded item. That makes sense only if firstLoadedItem wasn't initialized, that can happen only if result instance was obtained from initial call.
         * Normally that value should be managed by handlePrevPageResult() method.
         */
        if (firstLoadedItem == -1) {
            firstLoadedItem = searchStartPosition;
        }

        /*
         * Updating index of the last loaded item. If there are no loaded data just set lastLoadedItem to 0.
         */
        lastLoadedItem = (resultItems.isEmpty())
                ? 0
                : firstLoadedItem + (resultItems.size() - 1);

        /*
         * Refreshing UI. For more details see methods JavaDocs.
         */
        updateLoadedItemIndexes(firstLoadedItem, lastLoadedItem, result.getNumFound());
        notifyDataUpdated();
    }

    /**
     * Parses {@link ContentType} instances to {@link ResultItem}. For displaying in List.
     *
     * @param startIndex   start position for items numbering.
     * @param source the source list of {@link ContentType}, that should be parsed.
     * @return the list of {@link ResultItem}.
     */
    private List<ResultItem> parseContentTypes(int startIndex, List<ContentType> source) {
        int itemIndex = startIndex;
        final List<ResultItem> resultItems = new ArrayList<>(source.size());
        for (ContentType item : source) {
            resultItems.add(new ResultItem(itemIndex++ + ". " + item.getName() + "\n"
                    + item.getId(), getContentTypeDetailsFormatterString(item)));
        }
        return resultItems;
    }

    private static String getContentTypeDetailsFormatterString(@NonNull ContentType item) {
        return new StringBuilder()
                .append("classification=").append(item.getClassification()).append("\n")
                .append("created=").append(item.getCreated()).append("\n")
                .append("creatorId=").append(item.getCreatorId()).append("\n")
                .append("description=").append(item.getDescription()).append("\n")
                .append("id=").append(item.getId()).append("\n")
                .append("keywords=").append(item.getKeywords()).append("\n")
                .append("lastModified=").append(item.getLastModified()).append("\n")
                .append("lastModifierId=").append(item.getLastModifierId()).append("\n")
                .append("locale=").append(item.getLocale()).append("\n")
                .append("name=").append(item.getName()).append("\n")
                .append("restricted=").append(item.isRestricted()).append("\n")
                .append("tags=").append(item.getTags()).append("\n")
                .toString();
    }
}