package com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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

public class PaymentActivity extends AppCompatActivity {

    TextView roomName, roomPrice, roomSize, roomAddress, roomBedtype, from, to, bookingDate;
    CheckBox ac, refri, wifi, bathub, balcony, restaurant, pool, fitness;
    Button makeBookingButton, btnTo, btnFrom, cancel, booking;

    BaseApiService mApiService;
    Context mContext;
    private DatePickerDialog datePickerDialog, datePickerDialog1;

    public static Room room;
    Payment payment;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState the saved instance state (if any)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set the layout, initialize the API service, and get the context
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.payment_room);
        mApiService = UtilsApi.getApiService();
        mContext = this;


        // Get the buttons and the room object
        Button btnAcc = (Button) findViewById(R.id.btnCancel);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        //room = MainActivity.roomList.get(MainActivity.detailRoom);

        // Get the text views and check boxes
        roomName = findViewById(R.id.RoomName);
        roomPrice = findViewById(R.id.price);
        roomSize = findViewById(R.id.size);
        roomAddress = findViewById(R.id.address);
        roomBedtype = findViewById(R.id.BedType);
        //TextView fromDate = (TextView) findViewById(R.id.roomDetailFromDate);
        //TextView toDate = (TextView) findViewById(R.id.roomDetailToDate);

        ac = findViewById(R.id.AC);
        refri = findViewById(R.id.Refri);
        wifi = findViewById(R.id.WiFi);
        bathub = findViewById(R.id.bathTub);
        balcony = findViewById(R.id.balcony);
        restaurant = findViewById(R.id.restaurant);
        pool = findViewById(R.id.swimmingpool);
        fitness = findViewById(R.id.fitness);
        makeBookingButton = findViewById(R.id.MakeBookingButton);
        btnFrom = findViewById(R.id.btnFrom);
        btnTo = findViewById(R.id.btnTo);
        from = findViewById(R.id.textFrom);
        to = findViewById(R.id.textTo);
        bookingDate = findViewById(R.id.BookingDate);
        booking = findViewById(R.id.paymentButton);
        cancel = findViewById(R.id.cancelButton);

        roomName.setText(room.name);
        roomPrice.setText(String.valueOf(room.price.price));
        roomSize.setText(String.valueOf(room.size));
        roomAddress.setText(room.address);
        roomBedtype.setText(room.bedType.toString());

        // Set the values of the text views and check the facilities
        //roomName.setText("" + room.name);
        //cityRoom.setText("" + room.city.toString().substring(0, 1) + room.city.toString().substring(1).toLowerCase());
        //roomBedtype.setText("" + room.bedType.toString().substring(0, 1) + room.bedType.toString().substring(1).toLowerCase());
        //roomSize.setText("" + room.size);
        //roomPrice.setText("Rp " + MainActivity.paymentAccount.totalPrice);
        //roomPrice.setText("" + room.address);
//        fromDate.setText("" + BookRoom.fromDate);
//        toDate.setText("" + BookRoom.toDate);

        /**
         * Check the facilities of the room and disable them.
         */
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

        /**
         * Called when the accept button is clicked.
         * Starts the main activity and makes the payment.
         */
        btnAcc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent move = new Intent(PaymentActivity.this, MainActivity.class);
                startActivity(move);
                paymentAcc();
            }
        });

        /**
         * Called when the cam button is clicked.
         * Starts the main activity and makes the payment.
         */
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent move = new Intent(PaymentActivity.this, MainActivity.class);
                startActivity(move);
                paymentCancel();
            }
        });
    }
    /**
     * Makes the Accept payment and shows a toast message.
     *
     * @return the payment object (null in this case)
     */
    protected Payment paymentAcc(){
        mApiService.accept(MainActivity.loginAccount.id).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    Toast toast = Toast.makeText(getApplicationContext(), "Accept Successful", Toast.LENGTH_SHORT);
                    toast.show();
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

    protected Payment paymentCancel(){
        mApiService.cancel(MainActivity.loginAccount.id).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    Toast toast = Toast.makeText(getApplicationContext(), "Cancel Successful", Toast.LENGTH_SHORT);
                    toast.show();
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