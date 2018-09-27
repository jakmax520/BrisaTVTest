package com.example.morais.brisatvtest.Service;

import com.example.morais.brisatvtest.Model.ChannelList;
import com.example.morais.brisatvtest.Utils.Constants;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class ChannelService {
    private static String BASE_URL = Constants.base_url;

    public interface ChannelAPI{
        @GET("channel/1")
        Call<ChannelList> getResults();
    }


    public ChannelAPI getAPI(){
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ChannelAPI.class);

    }
}
