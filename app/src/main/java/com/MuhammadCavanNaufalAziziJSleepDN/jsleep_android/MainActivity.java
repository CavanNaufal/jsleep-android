package com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.model.Account;
import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.model.Payment;
import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.model.Renter;
import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.model.Room;
import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.request.BaseApiService;
import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.request.UtilsApi;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static Account loginAccount;
    public static Account registerAccount;
    public static Payment paymentAccount;

    //Room room;

    String name;
    static ArrayList<Room> roomList = new ArrayList<Room>();

    BaseApiService mApiService;
    Context mContext;
    List<String> nameStr;
    public static List<Room> getRoom ;
    List<Room> acc ;
    ListView lv;
    Button next, prev, go;
    int currPage = 1, pageSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mApiService = UtilsApi.getApiService();
        mContext = this;
        next = findViewById(R.id.MainNextButton);
        prev = findViewById(R.id.MainPrevButton);
        go = findViewById(R.id.MainGoButton);
        lv = findViewById(R.id.view_list);

        lv.setOnItemClickListener(this::onItemClick);
        getAllRoom();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getRoom.size()>currPage){
                    currPage=1;
                    return;
                }
                currPage++;
                try {
                    getAllRoom();
                    Toast.makeText(mContext, "page " + currPage, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currPage <= 1){
                    currPage = 1;
                    Toast.makeText(mContext, "this is the first page", Toast.LENGTH_SHORT).show();
                    return;
                }
                currPage--;
                try {
                    getAllRoom();
                    Toast.makeText(mContext, "page " + currPage, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    protected void getAllRoom() {
        mApiService.getAllRoom (currPage - 1, pageSize).enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if(response.isSuccessful()){
                    getRoom = (ArrayList<Room>) response.body();
                    ArrayList<String> list = new ArrayList<>();

                    assert getRoom != null;
                    for (Room room : getRoom){
                        list.add(room.name);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, list);
                    lv.setAdapter(adapter);

                }
            }
            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menuresource, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.person_button:
                Intent intent = new Intent(MainActivity.this, AboutMeActivity.class);
                startActivity(intent);
        }

        switch (item.getItemId()){
            case R.id.add_box_button:
                Intent intent = new Intent(MainActivity.this, CreateRoomActivity.class);
                startActivity(intent);
        }
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem register = menu.findItem(R.id.add_box_button);
        if(loginAccount.renter == null){
            register.setVisible(false);
        } else {
            register.setVisible(true);
        }
        return true;
    }

    public static ArrayList<String> getName(List<Room> list) {
        ArrayList<String> ret = new ArrayList<String>();
        int i;
        for (i = 0; i < list.size(); i++) {
            ret.add(list.get(i).name);
        }
        return ret;
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Log.i("HelloListView", "You clicked Item: " + id + " at position:" + position);
        Intent intent = new Intent();
        intent.setClass(this, DetailRoomActivity.class);
        DetailRoomActivity.room = getRoom.get(position);
        intent.putExtra("position", position);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
