package com.example.myapplication.gson;

import com.google.gson.annotations.SerializedName;

public class Forecast {

    public String date;
    public String max;
    public String min;
    public String code;

    @Override
    public String toString() {
        return "Forecast{" +
                "date='" + date + '\'' +
                ", max='" + max + '\'' +
                ", min='" + min + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
