package com.example.morais.brisatvtest.Utils;

import android.os.AsyncTask;

import com.example.morais.brisatvtest.App;
import com.example.morais.brisatvtest.Model.Program;

public class TvUtils {




    public class CurrentClass extends Thread{

        

        protected Void doInBackground(Integer... integers) {
            App.currentTime = System.currentTimeMillis();
            for(Program program: App.schedule.get(App.channelNumbers[integers[0]]).getPrograms()){
                if(program.getEnd_date() <= App.currentTime)
                    App.schedule.get(App.channelNumbers[integers[0]]).getPrograms().remove(program);
                else
                    return null;
            }
            return null;
        }
    }
}
