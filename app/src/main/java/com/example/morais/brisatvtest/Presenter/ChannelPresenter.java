package com.example.morais.brisatvtest.Presenter;

import android.content.Context;

import com.example.morais.brisatvtest.Model.ChannelList;
import com.example.morais.brisatvtest.Service.ChannelService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChannelPresenter {

    private final Context context;
    private final ChannelPresenterListener cListener;
    private final ChannelService channelService;

    public interface ChannelPresenterListener{
        void channelsReady(ChannelList channels);
        void channelsFailure(String message);
    }

    public ChannelPresenter(Context context, ChannelPresenterListener cListener) {
        this.context = context;
        this.cListener = cListener;
        this.channelService = new ChannelService();
    }

    public void getChannels(){
        channelService
                .getAPI()
                .getResults()
                .enqueue(new Callback<ChannelList>() {
                    @Override
                    public void onResponse(Call<ChannelList> call, Response<ChannelList> response) {

                        ChannelList result = response.body();

                        if(result != null)
                            cListener.channelsReady(result);
                    }

                    @Override
                    public void onFailure(Call<ChannelList> call, Throwable t) {
                        try {
                            cListener.channelsFailure(t.getMessage());
                            throw new InterruptedException("Erro na comunicação com o servidor!");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
