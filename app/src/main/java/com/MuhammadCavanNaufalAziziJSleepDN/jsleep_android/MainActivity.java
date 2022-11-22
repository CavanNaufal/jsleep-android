package com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.model.Account;
import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.model.Room;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static Account loginAccount;
    public static Account registerAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String json = null;
        try {
            InputStream is = getAssets().open("randomRoomList.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Gson gson = new Gson();
        Room[] roomList = gson.fromJson(json, Room[].class);
        ArrayList<String> names = new ArrayList<>();

        for (Room r : roomList) {
            Log.d("Name JSON", r.name);
            names.add(r.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
        ListView listView = (ListView) findViewById(R.id.view_list);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menuresource, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.person_button) {
            Intent intent = new Intent(MainActivity.this, AboutMeActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
