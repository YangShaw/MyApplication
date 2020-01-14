package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;


import com.example.myapplication.R;
import com.example.myapplication.activity.MainActivity;
import com.example.myapplication.gson.Forecast;
import com.example.myapplication.util.Utility;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ListRcvAdapter extends RecyclerView.Adapter<ListRcvAdapter.GroupViewHolder> {

    private static final String TAG = "ListRcvAdapter";

    private Context context;
    private List<Forecast> infoList;

    public ListRcvAdapter(Context context, List<Forecast> infoList) {
        this.context = context;
        this.infoList = infoList;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.forecast_item, parent, false);

        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Forecast forecast = infoList.get(position);

        String date = forecast.date.substring(5);
        holder.dayTv.setText(date);

        String temp = forecast.min+"°~"+forecast.max+"°";
        holder.tempTv.setText(temp);
        holder.weatherIv.setImageResource(Utility.getResIdByWeatherCode(forecast.code,
                context));


    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

//    //  添加数据
//    public void addData(GroupCreate createdGroup, int position){
//        infoList.add(position, createdGroup);
//        Log.i(TAG, "addData: new item");
//        notifyItemInserted(position);
//    }

//    public void addData(int position){
//        infoList.add(position, new GroupCreate("te", "te"));
//        Log.i(TAG, "addData: new item");
//        notifyItemInserted(position);
//    }


    public static class GroupViewHolder extends RecyclerView.ViewHolder{
        TextView dayTv;
        TextView tempTv;
        ImageView weatherIv;
        View itemView;

        public GroupViewHolder(View itemView){
            super(itemView);
            this.itemView = itemView;
            dayTv =itemView.findViewById(R.id.tv_item_day);
            tempTv = itemView.findViewById(R.id.tv_item_temperature);
            weatherIv = itemView.findViewById(R.id.iv_item_weather);

        }
    }

}
