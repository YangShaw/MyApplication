package com.example.myapplication.util;

import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.db.City;
import com.example.myapplication.db.County;
import com.example.myapplication.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {

//    public static int findResIdByWeatherCode(String weatherCode){
//
//        String imgName = "ic_weather_"+weatherCode;
//        int resId = getResources().getIdentifier(imgName, "drawable", "com.example.myapplication");
//
//    }

    //  这里默认response是获得到的数据，但是如何获得还要再写别的方法

    //  解析和处理处理器返回的省级数据
    public static boolean handleProvinceResponse(String response){
        //  解析前一定要确保成功获得了数据
        if(!TextUtils.isEmpty(response)){
            //  对返回的数据response进行解析
            try{
                //  jsonArray是json对象的数组
                JSONArray allProvinces = new JSONArray(response);

                for(int i=0;i<allProvinces.length();++i){
                    //  遍历jsonArray中的每个json对象
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    //  利用json对象封装的方法，通过键值对的形式找到我们需要的内容
                    province.setPrivinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    //  调用继承自DataSupport类的方法，将数据存储到数据库中
                    province.save();
                }
                return true;
            } catch(JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    //  解析和处理服务器返回的市级数据
    public static boolean handleCityResponse(String response, int provinceId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCities = new JSONArray(response);
                for(int i=0;i<allCities.length();++i){

                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityCode(cityObject.getInt("id"));
                    city.setCityName(cityObject.getString("name"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    //  县级数据
    public static boolean handleCountyResponse(String response, int cityId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCounties = new JSONArray(response);

                for(int i=0;i<allCounties.length();++i){
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setCityId(cityId);
                    //  我们的目的是存储不同城市的weatherid，以便于在点击城市按钮的时候找到天气
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.save();
                }

           } catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
}
