package com.example.morais.brisatvtest.Model;

import com.example.morais.brisatvtest.App;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChannelSchedule {

    @SerializedName("data")
    private List<Program> programs;

    public void setPrograms(List<Program> programs) {
        this.programs = programs;
    }

    public List<Program> getPrograms(){
        return programs;
    }

    public Program getProgram(int id){
        return programs.get(id);
    }

    public Program getCurrenProgram(){
        App.currentTime = System.currentTimeMillis();
        while(programs.get(0).getEnd_date() < App.currentTime){
            programs.remove(0);
        }
        return programs.get(0);
    }


}
