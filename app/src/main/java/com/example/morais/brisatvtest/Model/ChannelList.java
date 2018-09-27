package com.example.morais.brisatvtest.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChannelList {

    @SerializedName("data")
    private List<Channel> data;

    public List<Channel> getData() {
        return data;
    }

    public void setData(List<Channel> data) {
        this.data = data;
    }
}
