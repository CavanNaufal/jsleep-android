package com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.request.BaseApiService;

public class AboutMeActivity extends AppCompatActivity {
    BaseApiService mApiService;
    TextView name, email, balance;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        name = findViewById(R.id.NameAccount);
        email = findViewById(R.id.emailAccount);
        balance = findViewById(R.id.balanceAccount);

        name.setText(MainActivity.loginAccount.name);
        email.setText(MainActivity.loginAccount.email);
        balance.setText(String.valueOf(MainActivity.loginAccount.balance));
    }
}