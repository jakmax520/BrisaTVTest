package com.example.morais.brisatvtest.View;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.morais.brisatvtest.App;
import com.example.morais.brisatvtest.Model.Channel;
import com.example.morais.brisatvtest.Model.ChannelList;
import com.example.morais.brisatvtest.Model.ChannelSchedule;
import com.example.morais.brisatvtest.Model.Program;
import com.example.morais.brisatvtest.Presenter.ChannelPresenter;
import com.example.morais.brisatvtest.Presenter.ChannelSchedulePresenter;
import com.example.morais.brisatvtest.R;
import com.example.morais.brisatvtest.Utils.TVScheduleCapturer;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity implements IVLCVout.Callback,
        ChannelPresenter.ChannelPresenterListener, ChannelSchedulePresenter.ChannelSchedulePresenterListener{

    public final int timeToSeeBanner = 10000;

    SurfaceView playerView;
    SurfaceHolder playerHolder;
    private LibVLC mLibVLC;
    MediaPlayer mMediaPlayer;
    IVLCVout vout;
    public int mHeight;
    public int mWidth;

    int actualChannel = 0;
    FrameLayout frameLayout;
    boolean viewingTag = false;
    int currentChannelChar = 0;
    int[] typingChannelNumber = new int[3];
    int channelsAmount;


    TextView channelTV, numberTV, programName, hours, nextProgram, newChannelNumberTextView;


    Runnable updateSuperiorTag = new Runnable() {
        @Override
        public void run() {
            toggleSuperiorBanner(false);
        }
    };

    Runnable clearChannelChoosing = new Runnable() {
        @Override
        public void run() {
            newChannelNumberTextView.setVisibility(View.INVISIBLE);
            switch (currentChannelChar){
                case 0:
                    changeSuperiorChannelTag(App.channelList.get(App.channelNumbers[actualChannel]));
                    superiorInterfaceControl();
                    break;
                case 1:
                    chooseChannel((int)typingChannelNumber[0]);
                    break;
                case 2:
                    chooseChannel(typingChannelNumber[0]*10 + typingChannelNumber[1]);
                    break;
            }
            currentChannelChar = 0;
        }
    };

    Runnable setChannelAudio = new Runnable() {
        @Override
        public void run() {
            while(mMediaPlayer.getAudioTracksCount() < 1)
            Log.i("Audio", "" + mMediaPlayer.getAudioTracksCount());

            mMediaPlayer.setAudioTrack(mMediaPlayer.getAudioTracks()[mMediaPlayer.getAudioTracksCount()-1].id);
        }
    };

    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playerView = findViewById(R.id.playerView);

        channelTV = findViewById(R.id.channel_name);
        numberTV = findViewById(R.id.channel_number);
        handler = new Handler();
        setupPlayer();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        programName = findViewById(R.id.program_name);
        hours = findViewById(R.id.hours);
        nextProgram = findViewById(R.id.next_program);
        App.view = this;

        newChannelNumberTextView = findViewById(R.id.new_channel_number);
        newChannelNumberTextView.setVisibility(View.INVISIBLE);
    }

    public void setupPlayer() {
        frameLayout = findViewById(R.id.top_tag);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mHeight = displayMetrics.heightPixels;
        mWidth = displayMetrics.widthPixels;
        ArrayList<String> options = new ArrayList<>();
        options.add("--aout=opensles");
        options.add("--audio-time-stretch"); // time stretching

        mLibVLC = new LibVLC(getApplicationContext(), options);
        mMediaPlayer = new MediaPlayer(mLibVLC);


        vout = mMediaPlayer.getVLCVout();
        vout.setVideoView(playerView);
        vout.setWindowSize(mWidth, mHeight);

        vout.addCallback(this);

        vout.attachViews();

        ChannelPresenter channelPresenter = new ChannelPresenter(this, this);
        channelPresenter.getChannels();
    }

    void increaseChannel(){
        if(actualChannel < channelsAmount-1){
            actualChannel++;
            chooseChannel(App.channelNumbers[actualChannel]);
        }
    }

    void decreaseChannel(){
        if(actualChannel > 0){
            actualChannel--;
            chooseChannel(App.channelNumbers[actualChannel]);
        }
    }

    void chooseChannel(int newChannelNumber){
        superiorInterfaceControl();
        Channel newChannel = App.channelList.get(newChannelNumber);
        if(newChannel != null) {
            actualChannel = findChannelPosition(newChannelNumber, 0, channelsAmount - 1);
            changeSuperiorChannelTag(App.channelList.get(App.channelNumbers[actualChannel]));
            mMediaPlayer.setMedia(App.channelList.get(App.channelNumbers[actualChannel]).getMedia());
            mMediaPlayer.play();
            handler.removeCallbacks(setChannelAudio);
            handler.postDelayed(setChannelAudio, 1000);
        }
    }

    int findChannelPosition(int num, int inf, int sup){
        int test = (inf + sup)/2;

        if (num > App.channelNumbers[test])
            return findChannelPosition(num, test+1, sup);

        else if (num < App.channelNumbers[test])
            return findChannelPosition(num, inf, test-1);

        return test;

    }

    public void superiorInterfaceControl(){
        toggleSuperiorBanner(true);
        handler.removeCallbacks(updateSuperiorTag);
        handler.postDelayed(updateSuperiorTag, timeToSeeBanner);
    }

    void toggleSuperiorBanner(boolean see){
        if (!see) frameLayout.setVisibility(View.INVISIBLE);
        else frameLayout.setVisibility(View.VISIBLE);
    }

    public void changeSuperiorChannelTag(Channel channel){
        channelTV.setText(channel.getName());
        numberTV.setText(channel.getNumber());
        if(App.chargerSchedule) {
            try {
                Program p = App.schedule.get(App.channelNumbers[actualChannel]).getCurrenProgram();
                programName.setText(p.getName());
                nextProgram.setText(App.schedule.get(App.channelNumbers[actualChannel]).getProgram(1).getName());
                hours.setText(p.getHours());
            }catch(Exception e){
                programName.setText("Aguarde");
                nextProgram.setText("Aguarde");
                hours.setText("");
            }
        }
    }

    public void changeSuperiorTag(Channel channel){
        changeSuperiorChannelTag(channel);
    }

    public void typingNewChannel(String number){
        newChannelNumberTextView.setText(number);
        newChannelNumberTextView.setVisibility(View.VISIBLE);
        handler.removeCallbacks(clearChannelChoosing);
        handler.postDelayed(clearChannelChoosing, 5000);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Toast.makeText(this, "" + keyCode, Toast.LENGTH_SHORT).show();

        if(keyCode >= 7 && keyCode <=16){
            typingChannelNumber[currentChannelChar++] =  keyCode - 7;
            switch (currentChannelChar){
                case 1:
                    typingNewChannel("" + typingChannelNumber[0]);
                    break;
                case 2:
                    typingNewChannel("" + typingChannelNumber[0] + "" + typingChannelNumber[1]);
                    break;
                case 3:
                    typingNewChannel("" + typingChannelNumber[0] + "" + typingChannelNumber[1] + "" + typingChannelNumber[2]);
                    handler.removeCallbacks(clearChannelChoosing);
                    handler.post(clearChannelChoosing);
                    break;
            }
        }

        if (currentChannelChar == 3){
            chooseChannel(typingChannelNumber[0]*100 + typingChannelNumber[1]*10 +
                                        typingChannelNumber[2]);
            currentChannelChar = 0;
        }
        else if(keyCode == 23){
            handler.removeCallbacks(clearChannelChoosing);
            handler.post(clearChannelChoosing);
        }

        switch (keyCode) {
            case 166:
                increaseChannel();
                break;
            case 167:
                decreaseChannel();
                break;
            case 183:
                ReplayFragment sd = new ReplayFragment();

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_main, sd)
                        .addToBackStack(null)
                        .commit();
                break;
            case 184:
                getFragmentManager().popBackStack();
                break;

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onSurfacesCreated(IVLCVout ivlcVout) {
        Toast.makeText(this,"Abriiiu!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSurfacesDestroyed(IVLCVout ivlcVout) {
        mMediaPlayer.stop();
        mMediaPlayer.release();
    }

    @Override
    public void channelsReady(ChannelList channels) {

        if ( App.channelList == null){
            App.channelNumbers = new int[channels.getData().size()];
            App.channelIds = new int[channels.getData().size()];
            App.channelList = new HashMap<Integer, Channel>();
        }

        int count = 0;
        for (Channel channel: channels.getData()){
            channel.setMedia(this.mLibVLC);
            App.channelList.put(channel.getNumberInt(), channel);
            App.channelNumbers[count] = channel.getNumberInt();
            App.channelIds[count++] = channel.getId();
        }
        channelsAmount = count;

        channelTV.setText(App.channelList.get(App.channelNumbers[0]).getName());
        numberTV.setText(App.channelList.get(App.channelNumbers[0]).getNumber());

        mMediaPlayer.setMedia(App.channelList.get(App.channelNumbers[0]).getMedia());
        mMediaPlayer.setRate(1.0f);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toggleSuperiorBanner(false);
            }
        }, timeToSeeBanner);
        mMediaPlayer.play();
        App.upgrader.execute();

    }

    @Override
    public void channelsFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void channelScheduleReady(ChannelSchedule channelSchedule) {

    }

    @Override
    public void channelScheduleFailure(String erroMsg) {

    }
}
