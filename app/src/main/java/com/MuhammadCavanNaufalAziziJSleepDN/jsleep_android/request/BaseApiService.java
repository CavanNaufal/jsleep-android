package com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.request;

import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.model.Account;
import com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.model.Room;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BaseApiService {
    @GET("account/{id}")
    Call<Account> getAccount (@Path("id") int id);

    @GET("room/{id}")
    Call<Room> getRoom (@Path("id") int id);

    @POST("account/login")
    Call<Account> loginRequest(@Query("email") String email, @Query("password") String password);

    @POST("account/register")
    Call<Account> registerRequest(@Query("name") String name, @Query("email") String email, @Query("password") String password);
}
