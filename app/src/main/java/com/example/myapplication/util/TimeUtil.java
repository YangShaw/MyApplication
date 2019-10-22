package com.example.myapplication.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;
public class TimeUtil {

    public static Map<String, Integer> getSystemCalender(){
        Calendar calendar = Calendar.getInstance(); //  单例模式

        Map<String, Integer> myCalender = new HashMap<>();

        myCalender.put("year", calendar.get(Calendar.YEAR));
        myCalender.put("month", calendar.get(Calendar.MONTH));
        myCalender.put("day", calendar.get(Calendar.DAY_OF_MONTH));
        myCalender.put("weekday", calendar.get(Calendar.DAY_OF_WEEK));
        myCalender.put("hour", calendar.get(Calendar.HOUR));
        myCalender.put("minute", calendar.get(Calendar.MINUTE));
        myCalender.put("second", calendar.get(Calendar.SECOND));

        return myCalender;
    }
}
