package com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.textfield.TextInputLayout;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.model.Facility;
import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.model.Room;
import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.model.Payment;
import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.request.BaseApiService;
import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.request.UtilsApi;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailRoomActivity extends AppCompatActivity {

    TextView roomName, roomPrice, roomSize, roomAddress, roomBedtype, from, to, bookingDate;
    CheckBox ac, refri, wifi, bathub, balcony, restaurant, pool, fitness;
    Button makeBookingButton, btnTo, btnFrom, cancel, booking;
    ConstraintLayout makeBooking;


    BaseApiService mApiService;
    Context mContext;
    public static Room room;

    public static String toDate = "0000-00-00";
    public static String fromDate = "0000-00-00";
    //public static Room tempRoom;
    private DatePickerDialog datePickerDialog, datePickerDialog1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_room);

        roomName = findViewById(R.id.RoomName);
        roomPrice = findViewById(R.id.price);
        roomSize = findViewById(R.id.size);
        roomAddress = findViewById(R.id.address);
        roomBedtype = findViewById(R.id.BedType);
        mApiService = UtilsApi.getApiService();
        mContext = this;
        initDatePicker1();
        initDatePicker();

        ac = findViewById(R.id.AC);
        refri = findViewById(R.id.Refri);
        wifi = findViewById(R.id.WiFi);
        bathub = findViewById(R.id.bathTub);
        balcony = findViewById(R.id.balcony);
        restaurant = findViewById(R.id.restaurant);
        pool = findViewById(R.id.swimmingpool);
        fitness = findViewById(R.id.fitness);
        makeBookingButton = findViewById(R.id.MakeBookingButton);
        makeBooking = findViewById(R.id.MakeBooking);
        btnFrom = findViewById(R.id.btnFrom);
        btnTo = findViewById(R.id.btnTo);
        btnTo.setText(getTodaysDate());
        btnFrom.setText(getTodaysDate());
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

        // Hide Action Bar
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        makeBookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeBookingButton.setVisibility(View.INVISIBLE);
                makeBooking.setVisibility(View.VISIBLE);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeBookingButton.setVisibility(View.VISIBLE);
                makeBooking.setVisibility(View.INVISIBLE);
            }
        });

        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent move = new Intent(DetailRoomActivity.this, PaymentActivity.class);
                startActivity(move);
            }
        });
//        booking.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                create();
//            }
//        });

//        for (int i = 0; i < tempRoom.facility.size(); i++) {
//            if (tempRoom.facility.get(i).equals(Facility.AC)) {
//                ac.setChecked(true);
//            } else if (tempRoom.facility.get(i).equals(Facility.Refrigerator)) {
//                refri.setChecked(true);
//            } else if (tempRoom.facility.get(i).equals(Facility.WiFi)) {
//                wifi.setChecked(true);
//            } else if (tempRoom.facility.get(i).equals(Facility.Bathtub)) {
//                bathub.setChecked(true);
//            } else if (tempRoom.facility.get(i).equals(Facility.Balcony)) {
//                balcony.setChecked(true);
//            } else if (tempRoom.facility.get(i).equals(Facility.Restaurant)) {
//                restaurant.setChecked(true);
//            } else if (tempRoom.facility.get(i).equals(Facility.SwimmingPool)) {
//                pool.setChecked(true);
//            } else if (tempRoom.facility.get(i).equals(Facility.FitnessCenter)) {
//                fitness.setChecked(true);
//            }
//        }
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
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month,int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                btnFrom.setText(date);
                fromDate = year + "-" + month + "-" + day;
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_DEVICE_DEFAULT_DARK;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);

    }

    private void initDatePicker1() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                btnTo.setText(date);
                toDate = year + "-" + month + "-" +day;
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_DEVICE_DEFAULT_DARK;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog1 = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        return day + " " + getMonthFormat(month) + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }
    public void openDatePicker1(View view)
    {
        datePickerDialog1.show();
    }

    protected Payment create(){
//        System.out.println(MainActivity.loginAccount.id);
        System.out.println(MainActivity.loginAccount.renter.id);
//        System.out.println(room.id);
        System.out.println(fromDate);
        System.out.println(toDate);

        mApiService.getPayment(MainActivity.loginAccount.id, MainActivity.loginAccount.renter.id, room.id, fromDate.toString(),
                toDate.toString()).enqueue(new Callback<Payment>() {
            @Override
            public void onResponse(Call<Payment> call, Response<Payment> response) {
                if(response.isSuccessful()){
                    MainActivity.paymentAccount = response.body();
                    System.out.println("Berhasil");
                    Intent move = new Intent(DetailRoomActivity.this, PaymentActivity.class);
                    startActivity(move);
                    Toast toast = Toast.makeText(getApplicationContext(), "Booking Succesfull", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<Payment> call, Throwable t) {
                System.out.println(t.toString());
                Toast.makeText(mContext, "Booking Failed", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }
}