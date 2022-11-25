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
        balance.setText(Double.toString(MainActivity.loginAccount.balance));

//        topUpButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!balanceInput.getText().toString().isEmpty()){
//                    requestTopUp();
//                }
//                else{
//                    Toast.makeText(mContext, "Please input number of top up", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        if (MainActivity.loginAccount.renter == null){
            registerRenterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    registerRenterButton.setVisibility(View.GONE);
                    registerRenterLayout.setVisibility(View.VISIBLE);

                    registerRenterCancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            registerRenterLayout.setVisibility(View.GONE);
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

    protected Renter requestRenter(){
        mApiService.registerRenter(MainActivity.loginAccount.id,
                renterNameInput.getText().toString(),
                renterAddressInput.getText().toString(),
                renterPhoneNumberInput.getText().toString()
                ).enqueue(new Callback<Renter>() {
            @Override
            public void onResponse(Call<Renter> call, Response<Renter> response) {
                if (response.isSuccessful()){
                    MainActivity.loginAccount.renter = response.body();
                    Toast.makeText(mContext, "Register Renter Success!!", Toast.LENGTH_SHORT).show();
                    System.out.println(response.body());
                    registerRenterLayout.setVisibility(View.GONE);
                    renterLayout.setVisibility(View.VISIBLE);


                    Intent refresh = new Intent(mContext, AboutMeActivity.class);
                    refresh.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                    startActivity(refresh);

                    renterName.setText(MainActivity.loginAccount.renter.username);
                    renterAddress.setText(MainActivity.loginAccount.renter.address);
                    renterPhoneNumber.setText(MainActivity.loginAccount.renter.phoneNumber);
                }
            }
            @Override
            public void onFailure(Call<Renter> call, Throwable t) {
                Toast.makeText(mContext, "Register Renter Failed!!", Toast.LENGTH_SHORT).show();
                System.out.println("id="+MainActivity.loginAccount.id);
            }
        });
        return null;
    }

    //protected boolean requestTopUp(){

//        mApiService.topUp(MainActivity.loginAccount.id,
//                Double.parseDouble(balanceInput.getText().toString())).enqueue(new Callback<Boolean>() {
//            @Override
//            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
//                if (response.isSuccessful()){
//                    Toast.makeText(mContext, "TopUp Success!!", Toast.LENGTH_SHORT).show();
//                    if (Boolean.TRUE.equals(response.body())){
//                        MainActivity.loginAccount.balance += Double.parseDouble(balanceInput.getText().toString());
//                    }
//                    System.out.println(response.body());
//                    System.out.println(MainActivity.loginAccount.toString());
//                    Intent refresh = new Intent(mContext, AboutMeActivity.class);
//                    refresh.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//                    startActivity(refresh);
//                }
//            }
//            @Override
//            public void onFailure(Call<Boolean> call, Throwable t) {
//                Toast.makeText(mContext, "TopUp Failed!!", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        return true;
//    }
}