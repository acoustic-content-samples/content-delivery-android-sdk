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

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import co.acoustic.content.delivery.sdk.ContentDeliverySDK;
import co.acoustic.content.delivery.sdk.DeliverySearchResult;
import co.acoustic.content.delivery.sdk.Documents;

import static co.acoustic.content.delivery.sdk.sample.BaseQueryBuilderActivity.RESULT_KEY_SEARCH_PARAMETERS;
import static co.acoustic.content.delivery.sdk.sample.BaseQueryBuilderActivity.RESULT_KEY_SEARCH_PARAMETERS_INSTANCE;
import static co.acoustic.content.delivery.sdk.sample.BaseQueryBuilderActivity.RESULT_KEY_SEARCH_PARAMETERS_STRING;
import static co.acoustic.content.delivery.sdk.sample.BaseQueryBuilderActivity.RESULT_KEY_SEARCH_START;

/**
 * Contains common boilerplate code responsible for managing UI components.
 * All SDK related code could be found in particular implementation.
 */
abstract class BaseSampleActivity extends AppCompatActivity {
    protected final String tag = getClass().getSimpleName();

    private static final String DEFAULT_SEARCH_PARAMETERS_LABEL = "API default";

    private static final int DEFAULT_QUERY_START = 0;
    private static final int DEFAULT_QUERY_ROWS = 10;

    private static final QueryParameters DEFAULT_QUERY_PARAMETERS = new QueryParameters();
    static {
        DEFAULT_QUERY_PARAMETERS.setRows(DEFAULT_QUERY_ROWS);
        DEFAULT_QUERY_PARAMETERS.setStart(DEFAULT_QUERY_START);
    }

    private static final int REQUEST_CODE_QUERY_FILTER = 1;

    private TextView tvTotalItemsCount;
    private TextView tvStartPosition;
    private TextView tvLastPosition;

    private SwipeRefreshLayout refreshLayout;
    private LinearLayoutManager layoutManager;
    private RecyclerView rvSearchResults;

    private MenuItem loginMenuItem;
    private MenuItem accountMenuItem;

    private String searchParameters = DEFAULT_SEARCH_PARAMETERS_LABEL;
    private QueryParameters queryParameters = DEFAULT_QUERY_PARAMETERS;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infinite_search_sample);

        tvTotalItemsCount = findViewById(R.id.tvTotalItemsCount);
        tvStartPosition = findViewById(R.id.tvStartPosition);
        tvLastPosition = findViewById(R.id.tvLastPosition);

        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(true);
            loadPrevPage();
        });


        findViewById(R.id.btn_query_details).setOnClickListener(v ->
            DialogUtils.showQueryDetailsDialog(this, searchParameters, getSampleTitle(), this::launchQueryBuilder));
        findViewById(R.id.btn_call).setOnClickListener(v -> {
            clear();
            launchInitialQuery();
        });

        rvSearchResults = findViewById(R.id.rvSearchResults);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvSearchResults.setLayoutManager(layoutManager);
        rvSearchResults.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));

        rvSearchResults.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (refreshLayout.isRefreshing()) {
                    return;
                }

                if (layoutManager.findLastCompletelyVisibleItemPosition() == getSource().size() - 1) {
                    refreshLayout.setRefreshing(true);
                    loadNextPage();
                }
            }
        });

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getSampleTitle());
        }
    }

    protected void handleError(Throwable error) {
        Log.e(tag, "Failed to get initial content", error);
        DialogUtils.showErrorDialog(this, error.getMessage());
        refreshLayout.setRefreshing(false);
    }

    /**
     * Refreshes Views that displays current delivery search state.
     *
     * @param firstItem the index of first loaded item.
     * @param lastItem  the index of last loaded item.
     * @param numFound  the number of all found items on the backend.
     */
    protected final void updateLoadedItemIndexes(int firstItem, int lastItem, int numFound) {
        tvStartPosition.setText("First loaded item " + firstItem);
        tvLastPosition.setText("Last loaded item " + lastItem);
        tvTotalItemsCount.setText("Total items count: " + numFound);
    }

    /**
     * Clear current sample screen state. Called before launch new initial query.
     */
    protected void clear() {
        rvSearchResults.setAdapter(null);
        tvStartPosition.setText("");
        tvLastPosition.setText("");
        tvTotalItemsCount.setText("");
    }

    private void updateSearchParameters(@NonNull String searchParameters, @NonNull QueryParameters queryParameters, int searchStartPosition, Documents.State state) {
        this.searchParameters = searchParameters;
        this.queryParameters = queryParameters;
        onQueryBuilderResultReady(searchStartPosition, state);
    }

    /**
     * @return actual instance of {@link ContentDeliverySDK}.
     */
    @NonNull
    abstract protected ContentDeliverySDK getContentDeliverySDK();

    /**
     * Gets sample screen title, to display in the toolbar.
     */
    abstract protected String getSampleTitle();

    /**
     * Triggers loading previous page, if such exists. Called when the list view already displays first loaded page, and user tries to scroll up.
     * <p>
     * The implementation should use {@link DeliverySearchResult#previousPage()} here.
     */
    abstract protected void loadPrevPage();

    /**
     * Triggers loading next page. Called when the list view already displays last loaded page, and user tries to scroll down.
     * <p>
     * The implementation should use {@link DeliverySearchResult#nextPage()} here.
     */
    abstract protected void loadNextPage();

    /**
     * Triggers initial query, based on actual {@link QueryParameters} instance. The successful response should fill the list view with initial set of data.
     * After that opportunities to load additional pages will be opened.
     */
    abstract protected void launchInitialQuery();

    /**
     * Returns list of loaded data converted to format understandable for {@link ResultsAdapter}.
     *
     * @return the list of {@link ResultItem}.
     */
    @NonNull
    abstract protected List<ResultItem> getSource();

    /**
     * Returns class for launch custom query builder screen.
     *
     * @return the custom query builder {@link Class} instance.
     */
    @NonNull
    abstract protected Class<?> getQueryBuilderScreenClass();

    /**
     * Notifies child that Query builder screen successfully finished, and provide result.
     *
     * @param searchStartPosition the start position for the initial search query.
     * @param state the {@link Documents.State} which represents search query.
     */
    abstract protected void onQueryBuilderResultReady(int searchStartPosition, Documents.State state);

    @Override
    protected void onStart() {
        super.onStart();
        setAccountMenuItem(getContentDeliverySDK().isLoggedIn());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account_menu, menu);
        loginMenuItem = menu.findItem(R.id.action_login);
        accountMenuItem = menu.findItem(R.id.action_account);
        setAccountMenuItem(getContentDeliverySDK().isLoggedIn());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_login: {
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            }
            case R.id.action_logout: {
                getContentDeliverySDK().logout();
                setAccountMenuItem(getContentDeliverySDK().isLoggedIn());
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_QUERY_FILTER && resultCode == RESULT_OK && data != null) {

            final String searchParameters = data.getStringExtra(RESULT_KEY_SEARCH_PARAMETERS_STRING);
            final QueryParameters queryParameters = data.getParcelableExtra(RESULT_KEY_SEARCH_PARAMETERS_INSTANCE);
            final Documents.State state = data.getParcelableExtra(RESULT_KEY_SEARCH_PARAMETERS);
            final int searchStartPosition = data.getIntExtra(RESULT_KEY_SEARCH_START, 0);

            updateSearchParameters(searchParameters, queryParameters, searchStartPosition, state);
        }
    }

    protected void launchQueryBuilder() {
        Intent intent = new Intent(this, getQueryBuilderScreenClass());
        intent.putExtra(BaseQueryBuilderActivity.TITLE_KEY, getSampleTitle());
        intent.putExtra(BaseQueryBuilderActivity.SEARCH_PARAMETERS_KEY, queryParameters);

        startActivityForResult(intent, REQUEST_CODE_QUERY_FILTER);
    }

    protected void setAccountMenuItem(boolean isLoggedIn) {
        if (loginMenuItem != null) {
            loginMenuItem.setVisible(!isLoggedIn);
        }
        if (accountMenuItem != null) {
            accountMenuItem.setVisible(isLoggedIn);
            accountMenuItem.setTitle(getContentDeliverySDK().getCurrentUserName());
        }
    }

    protected void notifyDataUpdated() {
        resetAdapter();
        refreshLayout.setRefreshing(false);
    }

    private void resetAdapter() {
        if (rvSearchResults.getAdapter() == null) {
            rvSearchResults.setAdapter(new ResultsAdapter(getSource()));
        } else {
            new Handler(Looper.getMainLooper()).post(() -> rvSearchResults.getAdapter().notifyDataSetChanged());
        }
    }

    static class ResultItem {
        final String title;
        final String documentDetails;

        ResultItem(@NonNull String title, @NonNull String documentDetails) {
            this.title = title;
            this.documentDetails = documentDetails;
        }
    }

    private static class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.SampleViewHolder> {
        private final List<ResultItem> sampleItems;

        private ResultsAdapter(List<ResultItem> sampleItems) {
            this.sampleItems = sampleItems;
        }

        @NonNull
        @Override
        public SampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final View itemView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            final TextView title = itemView.findViewById(android.R.id.text1);
            return new SampleViewHolder(itemView, title);
        }

        @Override
        public void onBindViewHolder(@NonNull SampleViewHolder holder, int position) {
            final ResultItem item = sampleItems.get(position);
            holder.title.setText(item.title);
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), DocumentDetailsActivity.class);
                intent.putExtra(DocumentDetailsActivity.EXTRA_DOCUMENT_DETAILS, item.documentDetails);
                v.getContext().startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return sampleItems.size();
        }

        private static class SampleViewHolder extends RecyclerView.ViewHolder {
            TextView title;

            private SampleViewHolder(@NonNull View itemView, @NonNull TextView title) {
                super(itemView);
                this.title = title;
            }
        }
    }
}