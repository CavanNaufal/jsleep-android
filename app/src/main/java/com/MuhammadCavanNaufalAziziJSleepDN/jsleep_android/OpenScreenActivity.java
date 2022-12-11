package com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


/**
 * The `OpenScreenActivity` class extends the `AppCompatActivity` class
 * and is used to show the open screen for the application. It initializes
 * the view and hides the status bar and action bar. It also sets a timer
 * that starts the `LoginActivity` after a specified delay.
 */
public class OpenScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_screen);

        // inisialisasi view
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        // Hide ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        // timer
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(OpenScreenActivity.this, LoginActivity.class));
                finish();
            }
        }, 2000);
    }
}