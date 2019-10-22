package com.example.myapplication.model;

public class NowWeather {

    private String condCode;
    private String condTxt;
    private String temp;



    private String loc;

    public NowWeather(){

    }
    public NowWeather(String condCode, String condTxt, String temp, String loc) {
        this.condCode = condCode;
        this.condTxt = condTxt;
        this.temp = temp;
        this.loc = loc;
    }


    public String getCondCode() {
        return condCode;
    }

    public void setCondCode(String condCode) {
        this.condCode = condCode;
    }

    public String getCondTxt() {
        return condTxt;
    }

    public void setCondTxt(String condTxt) {
        this.condTxt = condTxt;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }
}
