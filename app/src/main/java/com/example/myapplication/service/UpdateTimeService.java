package com.example.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

public class UpdateTimeService extends Service {
    public UpdateTimeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();

        //  创建一个定时器
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //  发送广播仍然使用Intent对象
                Intent timeIntent = new Intent();
                //  设置动作名称，用于接收
                timeIntent.setAction("TIME_CHANGED_ACTION");
                sendBroadcast(timeIntent);
            }
            //  设置时间间隔
        }, 0, 30*60*1000);
    }
}
