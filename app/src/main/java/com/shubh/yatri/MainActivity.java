package com.shubh.yatri;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.shubh.yatri.activities.LoginActivity;
import com.shubh.yatri.activities.EditorActivity;
import com.shubh.yatri.utils.SharedPrefsHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPrefsHelper prefsHelper = new SharedPrefsHelper(this);

        // Splash delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent;
            if (prefsHelper.isLoggedIn()) {
                intent = new Intent(MainActivity.this, EditorActivity.class);
            } else {
                intent = new Intent(MainActivity.this, LoginActivity.class);
            }
            startActivity(intent);
            finish();
        }, 2000);
    }
}
