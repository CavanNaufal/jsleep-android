package com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.model.Account;
import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.model.Renter;
import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.request.BaseApiService;
import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * AboutMeActivity is an activity that displays information about the logged in user,
 * such as their name, email, and balance. It also allows the user to register as a renter
 * by entering their name, address, and phone number.
 */
public class AboutMeActivity extends AppCompatActivity {
    BaseApiService mApiService;
    Context mContext;
    Button topUpButton, registerRenterButton, registerRenterAcceptButton, registerRenterCancelButton;
    EditText balanceInput, renterNameInput, renterAddressInput, renterPhoneNumberInput;
    TextView name, email, balance;
    TextView renterName, renterAddress, renterPhoneNumber;
    ConstraintLayout renterLayout, registerRenterLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        mApiService = UtilsApi.getApiService();
        mContext = this;
        name = findViewById(R.id.NameAccount);
        email = findViewById(R.id.emailAccount);
        balance = findViewById(R.id.balanceAccount);
        topUpButton = findViewById(R.id.topUpButton);
        balanceInput = findViewById(R.id.balanceInput);

        registerRenterButton = findViewById(R.id.registerRenterButton);
        renterNameInput = findViewById(R.id.renter_register_name);
        renterAddressInput = findViewById(R.id.renter_register_address);
        renterPhoneNumberInput = findViewById(R.id.renter_register_phoneNumber);
        registerRenterAcceptButton = findViewById(R.id.register_renter_button_accept);
        registerRenterCancelButton = findViewById(R.id.register_renter_button_cancel);

        renterName = findViewById(R.id.account_renter_name);
        renterAddress = findViewById(R.id.renter_account_address);
        renterPhoneNumber = findViewById(R.id.account_renter_phone_number);

        renterLayout = findViewById(R.id.renter_layout);
        registerRenterLayout = findViewById(R.id.renter_register_layout);

        name.setText(MainActivity.loginAccount.name);
        email.setText(MainActivity.loginAccount.email);
        String balanceDetails = "Rp. " + MainActivity.loginAccount.balance;
        balance.setText(balanceDetails);

        /**
         * Called when the "Top Up" button is clicked. This method sends a request to the API
         * to top up the user's balance.
         */
        topUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topUpRequest();
            }
        });

        if (MainActivity.loginAccount.renter == null){
            registerRenterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    registerRenterButton.setVisibility(View.GONE);
                    registerRenterLayout.setVisibility(View.VISIBLE);

                    registerRenterCancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            registerRenterLayout.setVisibility(View.INVISIBLE);
                            registerRenterButton.setVisibility(View.VISIBLE);

                        }
                    });
                    registerRenterAcceptButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (renterNameInput.getText().toString().isEmpty()
                                    && renterAddressInput.getText().toString().isEmpty()
                                    && renterPhoneNumberInput.getText().toString().isEmpty()){
                                Toast.makeText(mContext, "Invalid name, address, or phone number", Toast.LENGTH_SHORT).show();
                            } else{
                                MainActivity.loginAccount.renter = requestRenter();
                            }
                        }
                    });
                }
            });
        } else {
            renterName.setText(MainActivity.loginAccount.renter.username);
            renterAddress.setText(MainActivity.loginAccount.renter.address);
            renterPhoneNumber.setText(MainActivity.loginAccount.renter.phoneNumber);

            registerRenterButton.setVisibility(View.GONE);
            renterLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Attempts to top up the balance of the user's account.
     *
     * @return {@code true} if the top-up request was successful, {@code false} otherwise.
     */
    protected Boolean topUpRequest(){
        Double topUpAmount = Double.parseDouble(balanceInput.getText().toString());
        mApiService.topUp(MainActivity.loginAccount.id, Double.parseDouble(balanceInput.getText().toString())).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(mContext, "Top Up Success", Toast.LENGTH_SHORT).show();
                    MainActivity.loginAccount.balance += topUpAmount;
                    String balanceDetails = "Rp. " + MainActivity.loginAccount.balance;
                    balance.setText(balanceDetails);
                } else {
                    Toast.makeText(mContext, "Top Up Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(mContext, "Top Up Failed", Toast.LENGTH_SHORT).show();
            }
        });
        return false;
    }

    /**
     * Registers a renter with the specified information.
     * @return the registered renter
     */
    protected Renter requestRenter(){
        mApiService.registerRenter(MainActivity.loginAccount.id,
                renterNameInput.getText().toString(),
                renterAddressInput.getText().toString(),
                renterPhoneNumberInput.getText().toString()
        ).enqueue(new Callback<Renter>() {
            @Override
            public void onResponse(Call<Renter> call, Response<Renter> response) {
                if(response.isSuccessful()){
                    Renter renter = response.body();
                    MainActivity.loginAccount.renter = renter;
                    Intent move = new Intent(AboutMeActivity.this, MainActivity.class);
                    startActivity(move);
                    Toast toast = Toast.makeText(getApplicationContext(), "Renter register Successful", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<Renter> call, Throwable t) {
                System.out.println(t.toString());
                Toast.makeText(mContext, "Renter already registered", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }
}