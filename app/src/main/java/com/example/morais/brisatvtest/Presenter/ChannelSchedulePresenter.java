package com.example.morais.brisatvtest.Presenter;

import android.content.Context;

import com.example.morais.brisatvtest.Model.ChannelSchedule;
import com.example.morais.brisatvtest.Service.ChannelScheduleService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChannelSchedulePresenter {
    private final Context context;
    private final ChannelSchedulePresenterListener cListener;
    private final ChannelScheduleService channelScheduleService;

    public interface ChannelSchedulePresenterListener{
        void channelScheduleReady(ChannelSchedule channelSchedule);
        void channelScheduleFailure(String erroMsg);
    }

    public ChannelSchedulePresenter(Context context, ChannelSchedulePresenterListener cListener) {
        this.context = context;
        this.cListener = cListener;
        channelScheduleService = new ChannelScheduleService();
    }

    public void getChannelSchedule(int pk, String time1, String time2){
        channelScheduleService
                .getAPI()
                .retrieve("" + pk, time1, time2)
                .enqueue(new Callback<ChannelSchedule>() {
                    @Override
                    public void onResponse(Call<ChannelSchedule> call, Response<ChannelSchedule> response) {
                        ChannelSchedule result = response.body();

                        if(result != null)
                            cListener.channelScheduleReady(result);
                    }

                    @Override
                    public void onFailure(Call<ChannelSchedule> call, Throwable t) {
                        cListener.channelScheduleFailure(t.getMessage());
                    }
                });
    }
}
