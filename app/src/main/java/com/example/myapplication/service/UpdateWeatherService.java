package com.example.myapplication.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import interfaces.heweather.com.interfacesmodule.bean.alarm.Alarm;

public class UpdateWeatherService extends Service {

    private static final String TAG = "UpdateWeatherService";
    private static int count = 0;
    public UpdateWeatherService() {
        
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //  服务创建时
    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate: ");
        super.onCreate();
    }

    //  每次服务启动时
    //  长时间在后台定时启动
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: "+count);
//        Toast.makeText(UpdateWeatherService.this, "调用"+count, Toast.LENGTH_LONG);
        count++;
        updateWeather();



        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        //  设置每10m触发。下面的数值单位是毫秒
        int tenSecond = 10*1000;
        long triggerAtTime = SystemClock.elapsedRealtime()+tenSecond;

        Intent i = new Intent(this, UpdateWeatherService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        alarmManager.cancel(pi);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }

    //  更新天气
    private void updateWeather(){

    }
}
