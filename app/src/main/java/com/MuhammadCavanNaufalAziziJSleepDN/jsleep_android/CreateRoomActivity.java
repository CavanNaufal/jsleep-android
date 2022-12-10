package com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.model.BedType;
import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.model.City;
import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.model.Facility;
import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.model.Room;
import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.request.BaseApiService;
import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.request.UtilsApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRoomActivity extends AppCompatActivity {

    EditText roomName, roomPrice, roomSize, roomAddress;
    Spinner bedSpin, citySpin;
    Button submitRoom, cancelRoom;
    CheckBox ac, refrig, wifi, bathub, balcony, restaurant, pool, fitness;
    ArrayList<Facility> facility = new ArrayList<Facility>();
    BedType bedType;
    City city;

    BaseApiService mApiService;
    Context mContext;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);

        mApiService = UtilsApi.getApiService();
        mContext = this;

        bedSpin = (Spinner) findViewById(R.id.bedTypespinner);
        citySpin = (Spinner) findViewById(R.id.citySpinner);
        ac = findViewById(R.id.AC);
        refrig = findViewById(R.id.Refri);
        wifi = findViewById(R.id.WiFi);
        bathub = findViewById(R.id.bathTub);
        balcony = findViewById(R.id.balcony);
        restaurant = findViewById(R.id.restaurant);
        pool = findViewById(R.id.swimmingpool);
        fitness = findViewById(R.id.fitness);
        roomName = findViewById(R.id.inputName);
        roomPrice = findViewById(R.id.inputPrice);
        roomAddress = findViewById(R.id.inputAddress);
        roomSize = findViewById(R.id.inputSize);
        submitRoom = findViewById(R.id.createRoomButton);
        cancelRoom = findViewById(R.id.cancelRoomButton);

        bedSpin.setAdapter(new ArrayAdapter<BedType>(this, android.R.layout.simple_spinner_item, BedType.values()));
        citySpin.setAdapter(new ArrayAdapter<City>(this, android.R.layout.simple_spinner_item, City.values()));

        // Hide Action Bar
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        submitRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ac.isChecked()) {
                    facility.add(Facility.AC);
                }
                if (refrig.isChecked()) {
                    facility.add(Facility.Refrigerator);
                }
                if (wifi.isChecked()) {
                    facility.add(Facility.WiFi);
                }
                if (bathub.isChecked()) {
                    facility.add(Facility.Bathtub);
                }
                if (balcony.isChecked()) {
                    facility.add(Facility.Balcony);
                }
                if (restaurant.isChecked()) {
                    facility.add(Facility.Restaurant);
                }
                if (pool.isChecked()) {
                    facility.add(Facility.SwimmingPool);
                }
                if (fitness.isChecked()) {
                    facility.add(Facility.FitnessCenter);
                }
                String bed = bedSpin.getSelectedItem().toString();
                String cityStr = citySpin.getSelectedItem().toString();
                bedType = BedType.valueOf(bed);
                city = City.valueOf(cityStr);

                Integer priceObj = new Integer(roomPrice.getText().toString());
                Integer sizeObj = new Integer(roomSize.getText().toString());

                int priceInt = priceObj.parseInt(roomPrice.getText().toString());
                int sizeInt = sizeObj.parseInt(roomSize.getText().toString());
                Room room = requestRoom(MainActivity.loginAccount.id, roomName.getText().toString(), sizeInt, priceInt, facility, city, roomAddress.getText().toString(), bedType);
            }
        });

        cancelRoom.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent move = new Intent(CreateRoomActivity.this, MainActivity.class);
                startActivity(move);
            }
        });
    }

    protected Room requestRoom(int accountId, String name, int size, int price, ArrayList<Facility> facility, City city, String address, BedType bedType) {
        mApiService.getRoom(accountId, name, size, price, facility, city, address, bedType).enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {
                if (response.isSuccessful()) {
                    Intent move = new Intent(CreateRoomActivity.this, MainActivity.class);
                    startActivity(move);
                    Toast.makeText(mContext, "Create Room Successfull", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Room> call, Throwable t) {
                System.out.println(t.toString());
                Toast.makeText(mContext, "Failed!", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }
}