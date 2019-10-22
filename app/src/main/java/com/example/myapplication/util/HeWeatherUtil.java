package com.example.myapplication.util;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.gson.Basic;
import com.example.myapplication.model.DailyWeather;
import com.example.myapplication.model.NowWeather;
import com.google.gson.Gson;

import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.basic.Update;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

public class HeWeatherUtil {

    public static NowWeather nowWeather;
    public static DailyWeather dailyWeather;
    public static void heWeatherInit(){
        //  和风天气api初始化
        HeConfig.init("HE1909200100361711", "69f2cb642a1646379bdb680390c377c7");
        //  免费接口要转换成免费节点
        HeConfig.switchToFreeServerNode();
    }

    //  通过SDK读取即时天气
    private NowWeather requestWeatherNowBySDK(final Context context, String cityId){

        /**
         * 实况天气
         * 实况天气即为当前时间点的天气状况以及温湿风压等气象指数，具体包含的数据：体感温度、
         * 实测温度、天气状况、风力、风速、风向、相对湿度、大气压强、降水量、能见度等。
         * @param context  上下文
         * @param location 地址详解
         * @param lang       多语言，默认为简体中文
         * @param unit        单位选择，公制（m）或英制（i），默认为公制单位
         * @param listener  网络访问回调接口
         */

        nowWeather = new NowWeather();

        HeWeather.getWeatherNow(context, cityId, Lang.CHINESE_SIMPLIFIED,
                Unit.METRIC, new HeWeather.OnResultWeatherNowBeanListener() {

                    public static final String TAG = "WeatherNow";
                    @Override
                    public void onError(Throwable throwable) {
                        Log.v(TAG, "on Error: ", throwable);
                    }

                    @Override
                    public void onSuccess(Now dataObject) {
                        Log.v(TAG, "Weather Now Success: "+new Gson().toJson(dataObject));

                        if(dataObject.getStatus().equals("ok")){
                            NowBase now = dataObject.getNow();
                            Update update = dataObject.getUpdate();

                            nowWeather.setCondCode(now.getCond_code());
                            nowWeather.setCondTxt(now.getCond_txt());
                            nowWeather.setTemp(now.getTmp());

                            //  获取更新时间
                            nowWeather.setLoc(update.getLoc());

                        } else {
                            Toast.makeText(context, "读取天气数据不存在", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                });
        return nowWeather;
    }

    //  通过SDK读取近日天气
    private DailyWeather requestWeatherDailyBySDK(final Context context, String cityId){

        dailyWeather = new DailyWeather();
        HeWeather.getWeatherForecast(context, cityId, Lang.CHINESE_SIMPLIFIED,
                Unit.METRIC, new HeWeather.OnResultWeatherForecastBeanListener() {

                    public static final String TAG = "WeatherDaily";
                    @Override
                    public void onError(Throwable throwable) {
                        Log.v(TAG, "on Error: ", throwable);
                        System.out.println("Weather Daily Error: "+new Gson());
                    }

                    @Override
                    public void onSuccess(Forecast dataObject) {
                        Log.v(TAG, "Weather Now Success: "+new Gson().toJson(dataObject));
                        if(dataObject.getStatus().equals("ok")){
                            //  mini版本，只读取当日天气，近一周的天气信息后续再补充
                            ForecastBase today = dataObject.getDaily_forecast().get(0);

                            dailyWeather.setCondTxtD(today.getCond_txt_d());
                            dailyWeather.setCondCodeD(today.getCond_code_d());
                            dailyWeather.setTmpMax(today.getTmp_max());
                            dailyWeather.setTmpMin(today.getTmp_min());
                            dailyWeather.setWindDir(today.getWind_dir());
                            dailyWeather.setWindSc(today.getWind_sc());

                            dailyWeather.generateTemp();
                            dailyWeather.generateWind();

                            //  后续应该补充白天和晚上的区别

                        } else {
                            Toast.makeText(context, "读取天气数据不存在", Toast.LENGTH_LONG).show();
                            return;
                        }

                    }
                });
        return dailyWeather;
    }
}
