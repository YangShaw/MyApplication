package com.example.myapplication.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

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
    private static final String TAG = "HeWeatherUtil";

    public static void heWeatherInit(){
        //  和风天气api初始化
        Log.i(TAG, "heWeatherInit: 正在初始化");
//        HeConfig.init("HE1901311309381615", "d2ae781d61744d65a2ef2156eef2cb64");
        HeConfig.init("HE2001130052131565", "967ae624f31d45ed90f991feb063cba3");

        //  免费接口要转换成免费节点
        HeConfig.switchToFreeServerNode();
    }

    public static void requestWeather(final Context context, String cityId,
                                      final Handler handler, final int[] messageSymbols){

        requestWeatherNowBySDK(context, cityId, handler, messageSymbols[0]);
        requestWeatherDailyBySDK(context, cityId, handler, messageSymbols[1]);
    }

    //  通过SDK读取即时天气
    public static void requestWeatherNowBySDK(final Context context, String cityId,
                                              final Handler handler, final int messageSymbol){

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

                            NowWeather nowWeather = new NowWeather();

                            nowWeather.setCondCode(now.getCond_code());
                            nowWeather.setCondTxt(now.getCond_txt());
                            nowWeather.setTemp(now.getTmp());

                            //  获取更新时间
                            nowWeather.setLoc(update.getLoc());
                            Log.i(TAG, "onSuccess: "+nowWeather.getCondCode());
                            Log.i(TAG, "onSuccess: "+nowWeather.getTemp());

                            //  通过Message和Handler来传递信息
                            Message message = handler.obtainMessage();
                            message.obj = nowWeather;
                            message.what = messageSymbol;
                            handler.sendMessage(message);

                        } else {
                            Toast.makeText(context, "读取天气数据不存在", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                });
    }

    //  通过SDK读取近日天气
    public static void requestWeatherDailyBySDK(final Context context, String cityId,
                                                final Handler handler, final int messageSymbol){

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
                            DailyWeather dailyWeather = new DailyWeather();

                            dailyWeather.setCondTxtD(today.getCond_txt_d());
                            dailyWeather.setCondCodeD(today.getCond_code_d());
                            dailyWeather.setTmpMax(today.getTmp_max());
                            dailyWeather.setTmpMin(today.getTmp_min());
                            dailyWeather.setWindDir(today.getWind_dir());
                            dailyWeather.setWindSc(today.getWind_sc());

                            dailyWeather.generateTemp();
                            dailyWeather.generateWind();

                            //  后续应该补充白天和晚上的区别
                            Message message = handler.obtainMessage();
                            message.obj = dailyWeather;
                            message.what = messageSymbol;
                            handler.sendMessage(message);

                        } else {
                            Toast.makeText(context, "读取天气数据不存在", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                });
    }

    //  通过api获取近日天气

}
