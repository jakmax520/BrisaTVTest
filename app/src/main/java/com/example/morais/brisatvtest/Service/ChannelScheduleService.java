package com.example.morais.brisatvtest.Service;

import com.example.morais.brisatvtest.Model.ChannelSchedule;
import com.example.morais.brisatvtest.Utils.Constants;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class ChannelScheduleService {
    private static final String BASE_URL = Constants.base_url;

    public interface ChannelScheduleAPI{
        @GET("gracenote/channel/{pk}/program")
        Call<ChannelSchedule> retrieve(@Path(value = "pk", encoded = true) String pk,
                                       @Query("start_date") String start_date,
                                       @Query("end_date") String end_date);
    }

    public ChannelScheduleAPI getAPI(){
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ChannelScheduleAPI.class);

    }


}
