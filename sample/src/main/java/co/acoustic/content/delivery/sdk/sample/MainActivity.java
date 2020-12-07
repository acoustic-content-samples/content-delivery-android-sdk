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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

import co.acoustic.content.delivery.sdk.ContentDeliverySDK;


public class MainActivity extends AppCompatActivity {

    private ContentDeliverySDK sdk;
    private MenuItem loginMenuItem;
    private MenuItem accountMenuItem;

    private List<SamplesListAdapter.SampleItem> samples = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sdk = ((SampleApplication) getApplication()).getSDK();

        initSampleItems();

        final RecyclerView recyclerViewSamples = findViewById(R.id.recyclerViewSamples);
        recyclerViewSamples.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerViewSamples.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));
        recyclerViewSamples.setAdapter(new SamplesListAdapter(samples));

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
        }
    }

    private void initSampleItems() {
        samples.add(
                new SamplesListAdapter.SampleItem(
                        getString(R.string.activity_sample_assets),
                        AssetsSampleActivity.class)
        );
        samples.add(
                new SamplesListAdapter.SampleItem(
                        getString(R.string.activity_sample_categories),
                        CategoriesSampleActivity.class)
        );
        samples.add(
                new SamplesListAdapter.SampleItem(
                        getString(R.string.activity_sample_content_types),
                        ContentTypesSampleActivity.class)
        );
        samples.add(
                new SamplesListAdapter.SampleItem(
                        getString(R.string.activity_sample_content_items),
                        ContentItemsSampleActivity.class)
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        setAccountMenuItem(sdk.isLoggedIn());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account_menu, menu);
        loginMenuItem = menu.findItem(R.id.action_login);
        accountMenuItem = menu.findItem(R.id.action_account);
        setAccountMenuItem(sdk.isLoggedIn());
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
                triggerLogout();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void triggerLogout() {
        sdk.logout();
        setAccountMenuItem(sdk.isLoggedIn());
    }

    private void setAccountMenuItem(boolean isLoggedIn) {
        if (loginMenuItem != null) {
            loginMenuItem.setVisible(!isLoggedIn);
        }
        if (accountMenuItem != null) {
            accountMenuItem.setVisible(isLoggedIn);
            accountMenuItem.setTitle(sdk.getCurrentUserName());
        }
    }

    private static class SamplesListAdapter extends RecyclerView.Adapter<SamplesListAdapter.SampleViewHolder> {
        private final List<SampleItem> sampleItems;

        private SamplesListAdapter(List<SampleItem> sampleItems) {
            this.sampleItems = sampleItems;
        }

        @NonNull
        @Override
        public SamplesListAdapter.SampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final View itemView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            final TextView title = itemView.findViewById(android.R.id.text1);
            return new SamplesListAdapter.SampleViewHolder(itemView, title);
        }

        @Override
        public void onBindViewHolder(@NonNull SamplesListAdapter.SampleViewHolder holder, int position) {
            final SampleItem item = sampleItems.get(position);
            holder.title.setText(item.sampleTitle);
            holder.itemView.setOnClickListener(v -> v.getContext().startActivity(new Intent(v.getContext(), item.sampleActivityClass)));
        }

        @Override
        public int getItemCount() {
            return sampleItems.size();
        }

        private static class SampleItem {
            final String sampleTitle;
            final Class<? extends Activity> sampleActivityClass;

            private SampleItem(@NonNull String sampleTitle, @NonNull Class<? extends Activity> sampleActivityClass) {
                this.sampleTitle = sampleTitle;
                this.sampleActivityClass = sampleActivityClass;
            }
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