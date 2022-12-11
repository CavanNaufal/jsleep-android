package com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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


/**
 * The `LoginActivity` class is an `Activity` that provides a login form
 * for the user to input their username and password.
 *
 * It also provides a button that navigates the user to the `RegisterActivity`
 * when clicked, allowing them to create a new account.
 *
 * When the login button is clicked, the entered username and password
 * are sent to the server for verification. If the login is successful,
 * the user is redirected to the `MainActivity`.
 */
public class LoginActivity extends AppCompatActivity {
    BaseApiService mApiService;
    EditText username, password;
    CheckBox loginRemember;
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
        loginRemember = findViewById(R.id.rememberLogin);


        if (getSupportActionBar() != null) getSupportActionBar().hide();

        SharedPreferences sharedPreferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkbox = sharedPreferences.getString("loginRemember", "");

        if (checkbox.equals("true")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (checkbox.equals("false")) {
            Toast.makeText(this, "Sign In!", Toast.LENGTH_SHORT).show();
        }

        loginRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(compoundButton.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "true");
                    editor.apply();
                    Toast.makeText(LoginActivity.this, "Checked", Toast.LENGTH_SHORT).show();
                }else if (!compoundButton.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                    Toast.makeText(LoginActivity.this, "Unchecked", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

    /**
     * This method sends a request to the server to retrieve the account
     * with the specified ID.
     *
     * If the request is successful, the account details are printed to the console.
     *
     * @return The `Account` object returned by the server, or `null` if the request failed.
     */
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

    /**
     * This method sends a login request to the server using the username and password
     * entered in the login form.
     *
     * If the login is successful, the user is redirected to the `MainActivity` and
     * the logged in account details are stored in the `loginAccount` variable.
     *
     * @return The `Account` object returned by the server, or `null` if the request failed.
     */
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

