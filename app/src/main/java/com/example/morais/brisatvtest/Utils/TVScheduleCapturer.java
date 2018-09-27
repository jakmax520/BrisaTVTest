package com.example.morais.brisatvtest.Utils;

import android.os.AsyncTask;
import android.provider.Settings;

import com.example.morais.brisatvtest.App;
import com.example.morais.brisatvtest.Model.Channel;
import com.example.morais.brisatvtest.Model.ChannelSchedule;
import com.example.morais.brisatvtest.Presenter.ChannelSchedulePresenter;
import com.example.morais.brisatvtest.View.MainActivity;

import java.sql.Timestamp;

public class TVScheduleCapturer extends AsyncTask<Void, Void, Void> implements ChannelSchedulePresenter.ChannelSchedulePresenterListener{

    private long time = System.currentTimeMillis();// + 10800000;
    private long time2 = time + 43200000;
    @Override
    protected Void doInBackground(Void... voids) {
        int count = App.channelList.size();
        App.upgrading = 0;

        App.channelSchedulePresenter.getChannelSchedule(
                App.channelList.get(App.channelNumbers[App.upgrading]).getId(),
                "" + time,
                "" + time2);


        return null;
    }

    @Override
    public void channelScheduleReady(ChannelSchedule channelSchedule) {
        App.schedule.put(App.channelNumbers[App.upgrading++], channelSchedule);
        if(App.upgrading < App.channelList.size())
            App.channelSchedulePresenter.getChannelSchedule(
                    App.channelList.get(App.channelNumbers[App.upgrading]).getId(),
                    "" + time,
                    "" + time2);
        else {
            App.chargerSchedule = true;
            App.lastScheduleRechargeTime = System.currentTimeMillis();
            App.handler.postDelayed(App.runnableTest, 3600000);
        }


    }


    @Override
    public void channelScheduleFailure(String erroMsg) {

    }
}
