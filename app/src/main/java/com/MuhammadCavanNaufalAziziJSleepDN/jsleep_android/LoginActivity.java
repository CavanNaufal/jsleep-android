package com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.model.Account;
import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.request.BaseApiService;
import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.request.UtilsApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    BaseApiService mApiService;
    EditText username, password;
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        TextView register = findViewById(R.id.RegisterNow);
        Button login = findViewById(R.id.Login);
        mApiService = UtilsApi.getApiService();
        mContext = this;
        username = findViewById(R.id.UsernameLogin);
        password = findViewById(R.id.PasswordLogin);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent move = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(move);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Account account = requestAccount();
                Account login = requestLogin();
            }
        });
    }

    protected Account requestAccount(){
        mApiService.getAccount(0).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if(response.isSuccessful()){
                    Account account;
                    account = response.body();
                    System.out.println(account.toString());
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t){
                System.out.println(t.toString());
                Toast.makeText(mContext, "no Account id=0", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }

    protected Account requestLogin() {
        String username = this.username.getText().toString();
        String password = this.password.getText().toString();
        mApiService.loginRequest(username, password).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.isSuccessful()) {
                    MainActivity.loginAccount = response.body();
                    Intent move = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(move);
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Toast.makeText(mContext, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }
}

