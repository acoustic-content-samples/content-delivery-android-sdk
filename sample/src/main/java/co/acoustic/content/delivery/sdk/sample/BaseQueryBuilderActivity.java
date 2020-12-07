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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Map;

import co.acoustic.content.delivery.sdk.DeliverySearch;
import co.acoustic.content.delivery.sdk.Documents;

/**
 * Base query builder activity, allows build delivery search parameters. Contains common boilerplate code responsible for managing UI components.
 * All SDK related code could be found in particular implementation.
 */
abstract class BaseQueryBuilderActivity extends AppCompatActivity {
    static final String TITLE_KEY = "QueryBuilderActivity.TITLE_KEY";
    static final String SEARCH_PARAMETERS_KEY = "QueryBuilderActivity.SEARCH_PARAMETERS_KEY";

    static final String RESULT_KEY_SEARCH_START = "QueryBuilderActivity.RESULT_KEY_SEARCH_START";
    static final String RESULT_KEY_SEARCH_PARAMETERS = "QueryBuilderActivity.RESULT_KEY_SEARCH_PARAMETERS";
    static final String RESULT_KEY_SEARCH_PARAMETERS_STRING = "QueryBuilderActivity.RESULT_KEY_QUERY_PARAMETERS_STRING";
    static final String RESULT_KEY_SEARCH_PARAMETERS_INSTANCE = "QueryBuilderActivity.RESULT_KEY_QUERY_PARAMETERS";

    private static final int DEFAULT_QUERY_START = 0;
    private static final int DEFAULT_QUERY_ROWS = 10;

    private DeliverySearch deliverySearch;

    private TextView startLabel;
    private TextView rowsLabel;
    private TextView sortLabel;
    private TextView filterByLabel;
    private TextView filterQueryLabel;

    private CheckBox includeDraftCheckBox;
    private CheckBox includeRetiredCheckBox;
    private CheckBox protectedContentCheckBox;

    private QueryParameters queryParameters;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_builder);

        /*
         * Initialize delivery search instances.
         */
        deliverySearch = ((SampleApplication) getApplication())
                .getSDK()
                .deliverySearch();

        if (getIntent().getExtras() != null) {
            queryParameters = getIntent().getExtras().getParcelable(SEARCH_PARAMETERS_KEY);
        } else {
            setDefaultQueryParameters();
        }

        startLabel = findViewById(R.id.start_label);
        rowsLabel = findViewById(R.id.rows_label);
        sortLabel = findViewById(R.id.sort_label);
        filterByLabel = findViewById(R.id.filter_by_label);
        filterQueryLabel = findViewById(R.id.filter_query_label);

        /*
         * Adding OnClickListener that will show dialog for editing start parameter.
         */
        findViewById(R.id.btn_start_update).setOnClickListener(v ->
                DialogUtils.showChangeNumericValueDialog(this, "Start", queryParameters.getStart(),
                        newValue -> {
                            queryParameters.setStart(newValue);
                            displayStartLabel();
                        }));

        /*
         * Adding OnClickListener that will show dialog for editing rows parameter.
         */
        findViewById(R.id.btn_rows_update).setOnClickListener(v ->
                DialogUtils.showChangeNumericValueDialog(this, "Rows", queryParameters.getRows(),
                        newValue -> {
                            queryParameters.setRows(newValue);
                            displayRowsLabel();
                        }));

        /*
         * Adding OnClickListener that will show dialog for adding sort by parameter.
         */
        findViewById(R.id.btn_sort_add).setOnClickListener(v ->
                DialogUtils.showSortByDialog(this,
                        (fieldName, ascending) -> {
                            queryParameters.addSortBy(fieldName, ascending);
                            displaySortLabel();
                        })
        );

        /*
         * Adding OnClickListener that will remove last sort by parameter.
         */
        findViewById(R.id.btn_sort_remove).setOnClickListener(v -> {
            queryParameters.removeSortBy();
            displaySortLabel();
        });

        /*
         * Adding OnClickListener that will show dialog for adding filter by parameter.
         */
        findViewById(R.id.btn_filter_by_add).setOnClickListener(v ->
                DialogUtils.showFilterByDialog(this,
                        (field, value) -> {
                            queryParameters.addFilterBy(field, value);
                            displayFilterByLabel();
                        })
        );

        /*
         * Adding OnClickListener that will remove last filter by parameter.
         */
        findViewById(R.id.btn_filter_by_remove).setOnClickListener(v -> {
            queryParameters.removeFilterBy();
            displayFilterByLabel();
        });

        /*
         * Adding OnClickListener that will show dialog for adding filter query parameter.
         */
        findViewById(R.id.btn_filter_query_add).setOnClickListener(v ->
                DialogUtils.showFilterQueryDialog(this,
                        query -> {
                            queryParameters.addFilterQuery(query);
                            displayFilterQueryLabel();
                        })
        );

        /*
         * Adding OnClickListener that will remove last filter query parameter.
         */
        findViewById(R.id.btn_filter_query_remove).setOnClickListener(v -> {
            queryParameters.removeFilterQuery();
            displayFilterQueryLabel();
        });

        /*
         * Adding listeners that listens to boolean request parameters (includeDraft, includeRetired, protectedContent).
         */
        includeDraftCheckBox = findViewById(R.id.include_draft);
        includeDraftCheckBox.setOnCheckedChangeListener((compoundButton, b) -> queryParameters.setIncludeDraft(b));
        includeRetiredCheckBox = findViewById(R.id.include_retired);
        includeRetiredCheckBox.setOnCheckedChangeListener((compoundButton, b) -> queryParameters.setIncludeRetired(b));
        protectedContentCheckBox = findViewById(R.id.protected_content);
        protectedContentCheckBox.setOnCheckedChangeListener((compoundButton, b) -> queryParameters.setProtectedContent(b));

        final String title;
        if (getIntent().getExtras() != null) {
            title = getIntent().getExtras().getString(TITLE_KEY, "");
        } else {
            title = "";
        }

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }

        displayCurrentQueryParameters();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.query_builder_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_close) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_defaults) {
            setDefaultQueryParameters();
            displayCurrentQueryParameters();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Shows save dialog.
     */
    private void showSaveDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.action_save)
                .setPositiveButton(android.R.string.ok, (dialog, whichButton) -> {
                    Intent data = new Intent();
                    data.putExtra(RESULT_KEY_SEARCH_PARAMETERS, createDocuments(deliverySearch, queryParameters).getState());
                    data.putExtra(RESULT_KEY_SEARCH_PARAMETERS_STRING, queryParameters.toString());
                    data.putExtra(RESULT_KEY_SEARCH_PARAMETERS_INSTANCE, queryParameters);
                    data.putExtra(RESULT_KEY_SEARCH_START, queryParameters.getStart());
                    setResult(RESULT_OK, data);
                    finish();
                })
                .setNegativeButton(android.R.string.cancel, (dialog, whichButton) -> {
                    setResult(RESULT_CANCELED);
                    finish();
                })
                .show();
    }

    /**
     * Converts QueryParameters to SDK Documents instance, for sending as a result.
     */
    abstract Documents createDocuments(DeliverySearch deliverySearch, QueryParameters searchParameters);

    @Override
    public void onBackPressed() {
        showSaveDialog();
    }

    /**
     * Shows on the screen start position of the search.
     */
    private void displayStartLabel() {
        startLabel.setText(getString(R.string.activity_query_builder_start_template, Integer.toString(queryParameters.getStart())));
    }

    /**
     * Shows on the screen rows count on one page of the search.
     */
    private void displayRowsLabel() {
        rowsLabel.setText(getString(R.string.activity_query_builder_rows_template, Integer.toString(queryParameters.getRows())));
    }

    /**
     * Shows on the screen list of added sortBy parameters.
     */
    private void displaySortLabel() {
        final Map<String, Boolean> sortBy = queryParameters.getSortBy();
        final String ascString = getString(R.string.activity_query_builder_sort_asc_label);
        final String descString = getString(R.string.activity_query_builder_sort_desc_label);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Boolean> entry : sortBy.entrySet()) {
            sb
                    .append("\n")
                    .append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue() ? ascString : descString);
        }
        sortLabel.setText(getString(R.string.activity_query_builder_sort_template, sb.toString()));
    }

    /**
     * Shows on the screen list of added filterBy parameters.
     */
    private void displayFilterByLabel() {
        final Map<String, String> filterBy = queryParameters.getFilterBy();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : filterBy.entrySet()) {
            sb
                    .append("\n")
                    .append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue());
        }
        filterByLabel.setText(getString(R.string.activity_query_builder_filter_by_template, sb.toString()));
    }

    /**
     * Shows on the screen list of added filterQuery parameters.
     */
    private void displayFilterQueryLabel() {
        final List<String> filterQueries = queryParameters.getFilterQueries();
        StringBuilder sb = new StringBuilder();
        for (String item : filterQueries) {
            sb
                    .append("\n")
                    .append(item);
        }
        filterQueryLabel.setText(getString(R.string.activity_query_builder_filter_query_template, sb.toString()));
    }

    private void displayIncludeDraftParam() {
        includeDraftCheckBox.setChecked(queryParameters.isIncludeDraft());
    }

    private void displayIncludeRetiredParam() {
        includeRetiredCheckBox.setChecked(queryParameters.isIncludeRetired());
    }

    private void displayProtectedContentParam() {
        protectedContentCheckBox.setChecked(queryParameters.isProtectedContent());
    }

    private void setDefaultQueryParameters() {
        queryParameters = new QueryParameters();
        queryParameters.setRows(DEFAULT_QUERY_ROWS);
        queryParameters.setStart(DEFAULT_QUERY_START);
    }

    private void displayCurrentQueryParameters() {
        displayStartLabel();
        displayRowsLabel();
        displaySortLabel();
        displayFilterByLabel();
        displayFilterQueryLabel();
        displayIncludeDraftParam();
        displayIncludeRetiredParam();
        displayProtectedContentParam();
    }
}