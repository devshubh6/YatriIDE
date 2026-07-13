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

public class EditorActivity extends AppCompatActivity {

    private EditText codeEditor;
    private Button compileBtn, saveBtn, myCodesBtn, logoutBtn;
    private TextView outputView, statusView;
    private SharedPrefsHelper prefsHelper;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        prefsHelper = new SharedPrefsHelper(this);
        apiService = ApiClient.getClient().create(ApiService.class);

        // Check authentication
        if (!prefsHelper.isLoggedIn()) {
            startActivity(new Intent(EditorActivity.this, LoginActivity.class));
            finish();
            return;
        }

        ApiClient.setAuthToken(prefsHelper.getAuthToken());

        initViews();
        setupListeners();
    }

    private void initViews() {
        codeEditor = findViewById(R.id.codeEditor);
        compileBtn = findViewById(R.id.compileBtn);
        saveBtn = findViewById(R.id.saveBtn);
        myCodesBtn = findViewById(R.id.myCodesBtn);
        logoutBtn = findViewById(R.id.logoutBtn);
        outputView = findViewById(R.id.outputView);
        statusView = findViewById(R.id.statusView);
    }

    private void setupListeners() {
        compileBtn.setOnClickListener(v -> handleCompile());
        saveBtn.setOnClickListener(v -> handleSave());
        myCodesBtn.setOnClickListener(v -> startActivity(new Intent(EditorActivity.this, MyCodesActivity.class)));
        logoutBtn.setOnClickListener(v -> handleLogout());
    }

    private void handleCompile() {
        String code = codeEditor.getText().toString().trim();

        if (code.isEmpty()) {
            ToastHelper.showShort(this, "❌ Please enter code");
            return;
        }

        compileBtn.setEnabled(false);
        compileBtn.setText("Compiling...");
        statusView.setText("⏳ Compiling...");

        JsonObject body = new JsonObject();
        body.addProperty("code", code);
        body.addProperty("language", "java");

        apiService.compileCode(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject responseBody = response.body();
                    boolean success = responseBody.get("success").getAsBoolean();
                    String output = responseBody.get("output").getAsString();
                    String status = responseBody.get("status").getAsString();

                    outputView.setText(output);
                    statusView.setText("Status: " + status);

                    if (success) {
                        ToastHelper.showShort(EditorActivity.this, "✅ Compilation successful!");
                    } else {
                        ToastHelper.showShort(EditorActivity.this, "❌ Compilation error!");
                    }
                } else {
                    outputView.setText("❌ Compilation failed");
                    ToastHelper.showShort(EditorActivity.this, "❌ Server error");
                }

                compileBtn.setEnabled(true);
                compileBtn.setText("Compile");
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                outputView.setText("❌ Error: " + t.getMessage());
                statusView.setText("Error");
                ToastHelper.showShort(EditorActivity.this, "❌ Network error");
                compileBtn.setEnabled(true);
                compileBtn.setText("Compile");
            }
        });
    }

    private void handleSave() {
        String code = codeEditor.getText().toString().trim();

        if (code.isEmpty()) {
            ToastHelper.showShort(this, "❌ Please enter code");
            return;
        }

        saveBtn.setEnabled(false);
        saveBtn.setText("Saving...");

        String title = "Code_" + System.currentTimeMillis();
        JsonObject body = new JsonObject();
        body.addProperty("title", title);
        body.addProperty("code", code);
        body.addProperty("language", "java");

        apiService.saveCode(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    ToastHelper.showShort(EditorActivity.this, "✅ Code saved successfully!");
                } else {
                    ToastHelper.showShort(EditorActivity.this, "❌ Failed to save code");
                }
                saveBtn.setEnabled(true);
                saveBtn.setText("Save");
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                ToastHelper.showShort(EditorActivity.this, "❌ Error: " + t.getMessage());
                saveBtn.setEnabled(true);
                saveBtn.setText("Save");
            }
        });
    }

    private void handleLogout() {
        prefsHelper.clearAll();
        ApiClient.clearAuthToken();
        startActivity(new Intent(EditorActivity.this, LoginActivity.class));
        finish();
    }
}
