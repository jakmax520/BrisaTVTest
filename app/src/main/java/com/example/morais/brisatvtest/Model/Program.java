package com.example.morais.brisatvtest.Model;

import com.google.gson.annotations.SerializedName;

public class Program {

    @SerializedName("name")
    private String name;

    @SerializedName("parental_rating")
    private int parentalRating;

    @SerializedName("start_date_format")
    private String startDate;

    @SerializedName("end_date_format")
    private String endDate;

    @SerializedName("end_date")
    private long end_date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentalRating() {
        return parentalRating;
    }

    public void setParentalRating(int parentalRating) {
        this.parentalRating = parentalRating;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getHours(){
        return startDate.split(" ")[1] + " - " + endDate.split(" ")[1];
    }

    public long getEnd_date() {
        return end_date;
    }

    public void setEnd_date(long end_date) {
        this.end_date = end_date;
    }
}
