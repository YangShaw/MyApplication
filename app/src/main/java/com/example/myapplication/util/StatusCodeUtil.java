package com.example.myapplication.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class StatusCodeUtil {

    public static boolean isTokenError(int status){
        if(status==403 || status==401){
            //  之后应该重新加载一次token
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNormalStatus(int  status){
        if(status==1){
            return true;
        } return false;
    }

    public static JsonObject isNormalResponse(String responseBody){
        try {
            JsonObject jsonObject = (JsonObject) new JsonParser().parse(responseBody);
            return jsonObject;
        } catch(Exception e){
            return null;
        }
    }
}
