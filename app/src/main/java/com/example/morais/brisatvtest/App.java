package com.example.morais.brisatvtest;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.morais.brisatvtest.Model.Channel;
import com.example.morais.brisatvtest.Model.ChannelSchedule;
import com.example.morais.brisatvtest.Model.ReplayChannel;
import com.example.morais.brisatvtest.Presenter.ChannelSchedulePresenter;
import com.example.morais.brisatvtest.Utils.TVScheduleCapturer;
import com.example.morais.brisatvtest.View.MainActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.LogRecord;

public class App extends Application {
    public final static String TAG = "BRISANET/SetupBox";
    public final static String ACTION_MEDIALIBRARY_READY = "BRISANET/SetupBox";

    private static volatile App instance;
    private static SimpleArrayMap<String, WeakReference<Object>> sDataMap = new SimpleArrayMap<>();

    private static boolean sTV;
    private static SharedPreferences sSettings;
    private static int sDialogCounter = 0;

    public static HashMap<Integer, ChannelSchedule> schedule;
    public static volatile HashMap<Integer, Channel> channelList;
    public static int [] channelNumbers;
    public static int [] channelIds;
    public static int upgrading = 0;
    public static TVScheduleCapturer upgrader ;
    public static ChannelSchedulePresenter channelSchedulePresenter;
    public static boolean chargerSchedule = false;
    public static long currentTime, lastScheduleRechargeTime;



    public static MainActivity view;

    public static Runnable runnableTest = new Runnable() {
        @Override
        public void run() {
            Log.i("Schedule", "Begin schedule upgrade");
            handler.postDelayed(this, 3600000);
        }
    };

    public static Handler handler;

    public App() {
        super();
        instance = this;
        upgrader = new TVScheduleCapturer();
        channelSchedulePresenter = new ChannelSchedulePresenter(getAppContext(), upgrader);
        schedule = new HashMap<Integer, ChannelSchedule>();
        handler = new Handler();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Called when the overall system is running low on memory
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Toast.makeText(this, "Memória baixa, mermão!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.w(TAG, "onTrimMemory, level: " + level);
    }

    public static Context getAppContext() {
        return instance;
    }

    /**
     * @return the main resources from the Application
     */
    public static Resources getAppResources() {
        return instance.getResources();
    }

    public static void setLocale() {
        if (sSettings == null) PreferenceManager.getDefaultSharedPreferences(instance);
        // Are we using advanced debugging - locale?
        String p = sSettings.getString("set_locale", "");
        if (!p.equals("")) {
            Locale locale;
            // workaround due to region code
            if (p.equals("zh-TW")) {
                locale = Locale.TRADITIONAL_CHINESE;
            } else if (p.startsWith("zh")) {
                locale = Locale.CHINA;
            } else if (p.equals("pt-BR")) {
                locale = new Locale("pt", "BR");
            } else if (p.equals("bn-IN") || p.startsWith("bn")) {
                locale = new Locale("bn", "IN");
            } else {
                /**
                 * Avoid a crash of
                 * java.lang.AssertionError: couldn't initialize LocaleData for locale
                 * if the user enters nonsensical region codes.
                 */
                if (p.contains("-"))
                    p = p.substring(0, p.indexOf('-'));
                locale = new Locale(p);
            }
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getAppResources().updateConfiguration(config,
                    getAppResources().getDisplayMetrics());
        }
    }



}
