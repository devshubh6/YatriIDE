package com.shubh.yatri.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.gson.JsonObject;
import com.shubh.yatri.R;
import com.shubh.yatri.api.ApiClient;
import com.shubh.yatri.api.ApiService;
import com.shubh.yatri.utils.SharedPrefsHelper;
import com.shubh.yatri.utils.ToastHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginBtn;
    private TextView registerLink;
    private SharedPrefsHelper prefsHelper;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefsHelper = new SharedPrefsHelper(this);
        apiService = ApiClient.getClient().create(ApiService.class);

        // Check if already logged in
        if (prefsHelper.isLoggedIn()) {
            startActivity(new Intent(LoginActivity.this, EditorActivity.class));
            finish();
            return;
        }

        initViews();
        setupListeners();
    }

    private void initViews() {
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginBtn = findViewById(R.id.loginBtn);
        registerLink = findViewById(R.id.registerLink);
    }

    private void setupListeners() {
        loginBtn.setOnClickListener(v -> handleLogin());
        registerLink.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });
    }

    private void handleLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            ToastHelper.showShort(this, "❌ Please fill all fields");
            return;
        }

        loginBtn.setEnabled(false);
        loginBtn.setText("Logging in...");

        JsonObject body = new JsonObject();
        body.addProperty("email", email);
        body.addProperty("password", password);

        apiService.login(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject responseBody = response.body();
                    String token = responseBody.get("token").getAsString();
                    String userId = responseBody.getAsJsonObject("user").get("id").getAsString();
                    String username = responseBody.getAsJsonObject("user").get("username").getAsString();
                    String userEmail = responseBody.getAsJsonObject("user").get("email").getAsString();

                    ApiClient.setAuthToken(token);
                    prefsHelper.saveAuthToken(token);
                    prefsHelper.saveUserInfo(userId, username, userEmail);

                    ToastHelper.showShort(LoginActivity.this, "✅ Login successful!");
                    startActivity(new Intent(LoginActivity.this, EditorActivity.class));
                    finish();
                } else {
                    ToastHelper.showShort(LoginActivity.this, "❌ Invalid credentials");
                    loginBtn.setEnabled(true);
                    loginBtn.setText("Login");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                ToastHelper.showShort(LoginActivity.this, "❌ Error: " + t.getMessage());
                loginBtn.setEnabled(true);
                loginBtn.setText("Login");
            }
        });
    }
}
