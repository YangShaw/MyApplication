package com.example.myapplication.gson;

import com.google.gson.annotations.SerializedName;

public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String cityId;

    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }
}
