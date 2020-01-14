package com.example.myapplication.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {

    //  封装一个用来访问网络的工具类

    public static void sendOkHttpRequest(String url, okhttp3.Callback callback){

        OkHttpClient okHttpClient = new OkHttpClient();

        //  连接网络
        Request request = new Request.Builder().url(url).build();

        //  发送请求
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void getOkHttpRequest(String address, okhttp3.Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        okHttpClient.newCall(request).enqueue(callback);

    }
}
