package com.example.morais.brisatvtest.Model;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;

import java.util.ArrayList;


public class Channel {

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private int id;

    @SerializedName("url")
    private String url;

    @SerializedName("number")
    private int number;

    private Media media = null;

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNumber() {
        return "" + number;
    }

    public int getNumberInt() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public void setMedia(LibVLC mLibVLC){
        media = new Media(mLibVLC, Uri.parse("udp://@" + url));
        media.addOption(":network-caching=200");
        media.addOption(":file-caching=500");
        media.setHWDecoderEnabled(true, true);
    }

    public Media getMedia(){
        return media;
    }

    public static void setMediaList(LibVLC mLibVLC, ArrayList<Channel> channels){
        for (Channel channel: channels) channel.setMedia(mLibVLC);
    }
}
