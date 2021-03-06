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

import java.util.List;
import java.util.Map;

import co.acoustic.content.delivery.sdk.ContentTypes;
import co.acoustic.content.delivery.sdk.DeliverySearch;
import co.acoustic.content.delivery.sdk.Documents;

/**
 * The {@link BaseQueryBuilderActivity} implementation responsible for showing {@link ContentTypes} query builder.
 *
 * @see BaseQueryBuilderActivity for UI implementation details.
 */
public class ContentTypesQueryBuilderActivity extends BaseQueryBuilderActivity {

    @Override
    Documents createDocuments(DeliverySearch deliverySearch, QueryParameters searchParameters) {
        /*
         * Filling ContentTypes instance with all required search query parameters, and pass it as a result.
         * For more details check Delivery Search API docs.
         */
        ContentTypes contentTypes = deliverySearch.contentTypes();

        final Map<String, Boolean> sortBy = searchParameters.getSortBy();
        for (String field: sortBy.keySet()) {
            contentTypes.sortBy(field, sortBy.get(field));
        }

        final Map<String, String> filterBy = searchParameters.getFilterBy();
        for (String field: filterBy.keySet()) {
            contentTypes.filterBy(field, filterBy.get(field));
        }

        final List<String> filterQueries = searchParameters.getFilterQueries();
        for (String query: filterQueries) {
            contentTypes.filterQuery(query);
        }
        contentTypes
                .rows(searchParameters.getRows())
                .start(searchParameters.getStart());
        return contentTypes;
    }
}