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


import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

class DialogUtils {

    interface ValueChangedCallback {
        void onValueChanged(int newValue);
    }

    /**
     * Shows {@link AlertDialog} which allows to change int value. Provides result trough the callback method.
     *
     * @param context      current screen context.
     * @param title        the dialog title.
     * @param initialValue value that should be changed.
     * @param callback     the instance of {@link ValueChangedCallback}, used to send updated int value back to client once change will be done.
     */
    static void showChangeNumericValueDialog(Context context, @NonNull String title, final int initialValue, @NonNull ValueChangedCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        final View customView = inflater.inflate(R.layout.dialog_update_numeric, null);

        final EditText edit = customView.findViewById(R.id.edit_value);
        edit.setHint(Integer.toString(initialValue));
        builder.setTitle(title);

        builder.setView(customView)
                .setPositiveButton(android.R.string.ok, (dialog, id) -> {
                    if (!TextUtils.isEmpty(edit.getText())) {
                        int selectedValue = Integer.valueOf(edit.getText().toString());
                        if (selectedValue != initialValue) {
                            callback.onValueChanged(selectedValue);
                        }
                    }
                    dialog.dismiss();
                })
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> dialog.dismiss());

        builder.show();
    }

    interface SortByReadyCallback {
        void onSortByReady(String fieldName, boolean ascending);
    }

    /**
     * Shows {@link AlertDialog} which allows to add sort by query parameter. Provides result trough the callback method.
     *
     * @param context      current screen context.
     * @param callback     the instance of {@link SortByReadyCallback}, used to send sort by parameter back to client once change will be done.
     */
    static void showSortByDialog(Context context, @NonNull SortByReadyCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        final View customView = inflater.inflate(R.layout.dialog_add_sort, null);

        final EditText filedName = customView.findViewById(R.id.sort_filed_name);
        final Spinner sortOrder = customView.findViewById(R.id.sort_order);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.sort_orders_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortOrder.setAdapter(adapter);

        builder.setTitle(context.getString(R.string.activity_query_builder_sort_template, ""));
        builder.setView(customView)
                // Add action buttons
                .setPositiveButton(android.R.string.ok, (dialog, id) -> {

                    if (!TextUtils.isEmpty(filedName.getText())) {
                        callback.onSortByReady(
                                filedName.getText().toString(),
                                sortOrder.getSelectedItemPosition() == 0);
                    }
                    dialog.dismiss();
                })
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> dialog.dismiss());

        builder.show();
    }

    interface FilterByReadyCallback {
        void onFilterByReady(String fieldName, String value);
    }

    /**
     * Shows {@link AlertDialog} which allows to add filter by query parameter. Provides result trough the callback method.
     *
     * @param context      current screen context.
     * @param callback     the instance of {@link FilterByReadyCallback}, used to send filter by parameter back to client once change will be done.
     */
    static void showFilterByDialog(Context context, @NonNull FilterByReadyCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);

        final View customView = inflater.inflate(R.layout.dialog_filter_by, null);
        final EditText filedName = customView.findViewById(R.id.field_name);
        final EditText filedValue = customView.findViewById(R.id.field_value);
        builder.setTitle(context.getString(R.string.activity_query_builder_filter_by_template, ""));
        builder.setView(customView)
                // Add action buttons
                .setPositiveButton(android.R.string.ok, (dialog, id) -> {
                    if (!TextUtils.isEmpty(filedName.getText())
                            && !TextUtils.isEmpty(filedValue.getText())) {
                        callback.onFilterByReady(filedName.getText().toString(), filedValue.getText().toString());
                    }
                    dialog.dismiss();
                })
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> dialog.dismiss());

        builder.show();
    }

    interface FilterQueryReadyCallback {
        void onFilterQueryReady(String query);
    }

    /**
     * Shows {@link AlertDialog} which allows to add filter query parameter. Provides result trough the callback method.
     *
     * @param context      current screen context.
     * @param callback     the instance of {@link FilterByReadyCallback}, used to send filter query parameter back to client once change will be done.
     */
    static void showFilterQueryDialog(@NonNull Context context, @NonNull FilterQueryReadyCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        final View customView = inflater.inflate(R.layout.dialog_filter_query, null);
        final EditText filedName = customView.findViewById(R.id.query);

        builder.setTitle(context.getString(R.string.activity_query_builder_filter_query_template, ""));
        builder.setView(customView)
                // Add action buttons
                .setPositiveButton(android.R.string.ok, (dialog, id) -> {
                    if (!TextUtils.isEmpty(filedName.getText())) {
                        callback.onFilterQueryReady(filedName.getText().toString());
                    }
                    dialog.dismiss();
                })
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> dialog.dismiss());

        builder.show();
    }


    interface OnUpdateClickListener {
        void onUpdateClick();
    }

    static void showQueryDetailsDialog(@NonNull Context context, @NonNull String queryDetails, @NonNull String title, @NonNull OnUpdateClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        final View customView = inflater.inflate(R.layout.dialog_query_details, null);
        ((TextView) customView.findViewById(R.id.query_details_label)).setText(queryDetails);
        builder.setTitle(title);
        builder.setView(customView)
                .setPositiveButton(R.string.action_update_query, (dialog, id) -> {
                    listener.onUpdateClick();
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.action_close,  (dialog, id) -> dialog.dismiss());
        builder.show();
    }

    static void showErrorDialog(@NonNull Context context, @NonNull String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View customView = inflater.inflate(R.layout.dialog_query_details, null);
        ((TextView) customView.findViewById(R.id.query_details_label)).setText(error);
        builder.setTitle(context.getString(R.string.error_title));
        builder.setView(customView)
                .setPositiveButton(android.R.string.ok, (dialog, id) -> dialog.dismiss());
        builder.show();
    }

}