package com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.model.Facility;
import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.model.Payment;
import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.model.Room;
import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.request.BaseApiService;
import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This class represents an activity that displays the details of a payment
 * and allows the user to accept or cancel the payment.
 */
public class PaymentActivity extends AppCompatActivity {
    TextView roomName, roomPrice, roomSize, roomAddress, roomBedtype, from, to, bookingDate;
    CheckBox ac, refri, wifi, bathub, balcony, restaurant, pool, fitness;
    Button cancel, accept;

    BaseApiService mApiService;
    Context mContext;
    public static Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        room = MainActivity.getRoom.get(MainActivity.roomPosition);

        roomName = findViewById(R.id.RoomName1);
        roomPrice = findViewById(R.id.price1);
        roomSize = findViewById(R.id.size1);
        roomAddress = findViewById(R.id.address1);
        roomBedtype = findViewById(R.id.BedType1);
        mApiService = UtilsApi.getApiService();
        mContext = this;

        ac = findViewById(R.id.AC1);
        refri = findViewById(R.id.Refri1);
        wifi = findViewById(R.id.WiFi1);
        bathub = findViewById(R.id.bathTub1);
        balcony = findViewById(R.id.balcony1);
        restaurant = findViewById(R.id.restaurant1);
        pool = findViewById(R.id.swimmingpool1);
        fitness = findViewById(R.id.fitness1);

        accept = findViewById(R.id.AcceptPayment);
        cancel = findViewById(R.id.CancelPayment);

        roomName.setText(room.name);
        System.out.println(room);
        //String.valueOf(room.price.price)
        String balance = String.valueOf(room.price.price);
        roomPrice.setText("Rp " + balance);
        roomSize.setText(String.valueOf(room.size));
        roomAddress.setText(room.address);
        roomBedtype.setText(room.bedType.toString());

        // Hide Action Bar
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        for (int i = 0; i < room.facility.size(); i++) {
            if (room.facility.get(i).equals(Facility.AC)) {
                ac.setChecked(true);
            } else if (room.facility.get(i).equals(Facility.Refrigerator)) {
                refri.setChecked(true);
            } else if (room.facility.get(i).equals(Facility.WiFi)) {
                wifi.setChecked(true);
            } else if (room.facility.get(i).equals(Facility.Bathtub)) {
                bathub.setChecked(true);
            } else if (room.facility.get(i).equals(Facility.Balcony)) {
                balcony.setChecked(true);
            } else if (room.facility.get(i).equals(Facility.Restaurant)) {
                restaurant.setChecked(true);
            } else if (room.facility.get(i).equals(Facility.SwimmingPool)) {
                pool.setChecked(true);
            } else if (room.facility.get(i).equals(Facility.FitnessCenter)) {
                fitness.setChecked(true);
            }
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentCancel();
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentAcc();
            }
        });
    }

    /**
     * Accepts the payment using the specified payment account.
     *
     * @param paymentAccount The payment account to use for accepting the payment
     * @return The payment details, or null if the payment was not accepted
     * @throws IOException if there was a problem communicating with the server
     */
    protected Payment paymentAcc(){
        mApiService.accept(MainActivity.paymentAccount.id).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    Toast toast = Toast.makeText(getApplicationContext(), "Accept Successful", Toast.LENGTH_SHORT);
                    toast.show();
                    Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t){
                System.out.println(t.toString());
                Toast.makeText(mContext, "Accept Not Success", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }

    /**
     * Cancels a payment and updates the login account balance.
     *
     * @param paymentAccount the payment account to cancel
     * @param room the room associated with the payment
     * @return the Payment that was canceled, or null if the operation failed
     * @throws PaymentException if the payment could not be canceled
     */
    protected Payment paymentCancel(){
        mApiService.cancel(MainActivity.paymentAccount.id).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    MainActivity.loginAccount.balance += room.price.price;
                    Toast toast = Toast.makeText(getApplicationContext(), "Cancel Successful", Toast.LENGTH_SHORT);
                    toast.show();
                    Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t){
                System.out.println(t.toString());
                Toast.makeText(mContext, "Cancel Not Success", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }
}