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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
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

/**
 * MainActivity is an activity that displays a list of rooms and allows the user
 * to browse through the pages of the list. The user can also go to a specific
 * page by entering the page number in a text field.
 *
 * The activity provides a menu with two options: "About Me" and "Create Room".
 * The "Create Room" option is only visible to users who are logged in as renters.
 * Clicking on a room in the list takes the user to the DetailRoomActivity, where
 * they can see more information about the selected room.
 */
public class MainActivity extends AppCompatActivity {

    public static Account loginAccount;
    public static Account registerAccount;
    public static Payment paymentAccount;

    BaseApiService mApiService;
    Context mContext;
    EditText pageNum;
    Button prevPage, nextPage, goPage;
    ListView listView;
    MenuItem addBut, persBut;

    private int currentPg = 1;
    private final int pSize = 20;
    public static int roomPosition;
    public static Room listRoom;
    public static List<Room> getRoom;
    public static ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApiService = UtilsApi.getApiService();
        mContext = this;
        setContentView(R.layout.activity_main);
        pageNum = findViewById(R.id.MainPage);
        prevPage = findViewById(R.id.MainPrevButton);
        nextPage = findViewById(R.id.MainNextButton);
        goPage = findViewById(R.id.MainGoButton);
        listView = findViewById(R.id.view_list);

        getAllRoom(currentPg);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                roomPosition = i;
                //+ ((currentPg - 1) * pSize);
                System.out.println(getRoom.get(roomPosition));
                Intent move = new Intent(MainActivity.this, DetailRoomActivity.class);
                startActivity(move);
            }
        });

        prevPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentPg - 1 < 1) {
                    getAllRoom(currentPg);
                } else {
                    getAllRoom(currentPg - 1);
                }
            }
        });
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllRoom(currentPg+1);
            }
        });
        goPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int inpPage = Integer.parseInt(pageNum.getText().toString());
                if(inpPage < 1)
                    getAllRoom(currentPg);
                getAllRoom(inpPage);
            }
        });
    }

    /**
     * onCreateOptionsMenu is a method that inflates the menu resource and sets up a search view.
     *
     * @param menu the menu to be inflated
     * @return true if the menu was successfully created
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menuresource, menu);
        addBut = menu.findItem(R.id.add_box_button);
        persBut = menu.findItem(R.id.person_button);
        addBut.setVisible(MainActivity.loginAccount.renter != null);

        MenuItem menuItem = menu.findItem(R.id.search_button);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This method is called when an option in the menu is selected.
     *
     * @param item The menu item that was selected.
     * @return True if the event was handled, false otherwise.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.person_button:
                Intent intent = new Intent(MainActivity.this, AboutMeActivity.class);
                startActivity(intent);
                break;
            case R.id.add_box_button:
                Intent intent1 = new Intent(MainActivity.this, CreateRoomActivity.class);
                startActivity(intent1);
                break;
        }
        return true;
    }

    /**
     * This method fetches the list of rooms from the API and displays them in
     * the list view. The page number is specified by the `page` parameter.
     *
     * @param page The page number to fetch.
     * @return The list of rooms.
     */
    protected Room getAllRoom(int page){
        mApiService.getAllRoom(page-1, pSize).enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if(response.isSuccessful()){
                    getRoom = (ArrayList<Room>)response.body();
                    System.out.println(getRoom.toString());
                    ArrayList<String> names = new ArrayList<>();

                    for (Room r : getRoom) {
                        names.add(r.name);
                    }
                    if(names.isEmpty()) {
                        Toast.makeText(mContext, "All available rooms are already displayed", Toast.LENGTH_SHORT).show();
                        if (page > 1) {
                            getAllRoom(page - 1);
                        }
                        return;
                    }
                    adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, names);
                    ListView listView = (ListView) findViewById(R.id.view_list);
                    listView.setAdapter(adapter);
                    pageNum.setText(String.valueOf(page));
                    currentPg = page;
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                System.out.println(page);
                System.out.println(t);
            }
        });
        return null;
    }
}
