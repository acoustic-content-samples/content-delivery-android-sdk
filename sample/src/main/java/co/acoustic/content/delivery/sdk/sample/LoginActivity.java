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
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import co.acoustic.content.delivery.sdk.ContentDeliverySDK;


public class LoginActivity extends AppCompatActivity {
    private EditText etUserName;
    private EditText etPassword;

    private Button buttonLogin;

    private TextView tvErrorText;

    private ContentDeliverySDK sdk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Use a lenient CookieManager
//        final CookieManager cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
//        CookieHandler.setDefault(cookieManager);

        this.sdk = ((SampleApplication) getApplication()).getSDK();

        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        tvErrorText = findViewById(R.id.tvErrorText);

        setResult(RESULT_OK);

        buttonLogin.setOnClickListener(v -> triggerLogin());
    }

    private void triggerLogin() {
        hideKeyboard(LoginActivity.this);
        tvErrorText.setText("");
        tvErrorText.setVisibility(View.GONE);

        buttonLogin.setClickable(false);

        //check if credentials are entered
        if (etUserName.getText().toString().trim().length() == 0) {
            showLoginError("No username supplied!");
        } else if (etPassword.getText().toString().trim().length() == 0) {
            showLoginError("No password supplied!");
        } else {
            sdk.login(
                    etUserName.getText().toString().trim(),
                    etPassword.getText().toString().trim(),
                    this::handleLoginResponse
            );
        }
    }

    private void handleLoginResponse(@Nullable Throwable error) {
        if (error == null) {
            onBackPressed();
        } else {
            showLoginError(error.getMessage());
            buttonLogin.setClickable(true);
        }
    }

    private void showLoginError(String error) {
        tvErrorText.setText(error);
        tvErrorText.setVisibility(View.VISIBLE);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}