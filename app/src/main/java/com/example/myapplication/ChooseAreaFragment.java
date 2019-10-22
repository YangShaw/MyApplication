package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.db.City;
import com.example.myapplication.db.County;
import com.example.myapplication.db.Province;
import com.example.myapplication.util.HttpUtil;
import com.example.myapplication.util.Utility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.*;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.internal.Util;


public class ChooseAreaFragment extends Fragment {

    private static final String TAG = "ChooseAreaFragment";
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;

    private TextView titleTv;
    private ListView citiesLv;
    private FloatingActionButton backFab;

    //  存储省市县信息的列表
    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;

    //  在当前item中被选中的省市县
    private Province selectedProvince;
    private City selectedCity;
    private County selectedCounty;

    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();

    //  当前选中的级别
    private int currentLevel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_area, container, false);
        titleTv = view.findViewById(R.id.tv_citylist_title);
        citiesLv = view.findViewById(R.id.lv_citylist_cities);
        backFab = view.findViewById(R.id.fab_citylist_back);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
        citiesLv.setAdapter(adapter);
        return view;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        getView().setFocusableInTouchMode(true);
//        getView().requestFocus();
//        getView().setOnKeyListener(new View.OnKeyListener(){
//
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK){
//                    //  返回上级查询目录
//                    if(currentLevel == LEVEL_COUNTY){
//                        queryCities();
//                    } else if(currentLevel == LEVEL_CITY){
//                        queryProvinces();
//                    } else if(currentLevel == LEVEL_PROVINCE){
//                        //  关闭滑动窗口
//                        MainActivity mainActivity = (MainActivity)getActivity();
//                        mainActivity.citiesDL.closeDrawers();
//                    }
//                    return true;
//                }
//                return false;
//            }
//        });
//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //  给列表中的每个item创建点击监听事件
        citiesLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //  由于是列表中的每个item的点击事件，所以有一个参数position
                if(currentLevel == LEVEL_PROVINCE){
                    //  省级目录，选中省份名称列表中相应位置的省份对象
                    selectedProvince = provinceList.get(position);
                    //  进一步查询市级目录。参数用全局变量selectedProvince传递，所以这几个查询方法都没有参数
                    queryCities();
                } else if(currentLevel == LEVEL_CITY){
                    //  市级目录
                    selectedCity = cityList.get(position);
                    queryCounties();
                } else if(currentLevel == LEVEL_COUNTY){

                    //  县级目录，这时候就要返回主界面并显示信息了
                    selectedCounty = countyList.get(position);
                    String weatherId = selectedCounty.getWeatherId();

                    //  fragment本身就在主活动中，只需要判断一下
                    if(getActivity() instanceof MainActivity){
                        MainActivity mainActivity = (MainActivity)getActivity();
                        mainActivity.citiesDL.closeDrawers();
                        mainActivity.refreshSrl.setRefreshing(true);
                        mainActivity.currentWeatherId = weatherId;
                        mainActivity.currentCity = selectedCity.getCityName();
                        mainActivity.currentCounty = selectedCounty.getCountyName();
                        mainActivity.updateWeather(weatherId);
//                        Intent intent = new Intent(getActivity(), MainActivity.class);
//                        intent.putExtra("weather_id", weatherId);
//                        startActivity(intent);
//                        getActivity().finish();
                    }

                    Log.i(TAG, "onItemClick: getweather:" + weatherId);


                }
            }
        });


        backFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  返回上级查询目录
                if(currentLevel == LEVEL_COUNTY){
                    queryCities();
                } else if(currentLevel == LEVEL_CITY){
                    queryProvinces();
                } else if(currentLevel == LEVEL_PROVINCE){
                    //  关闭滑动窗口
                    MainActivity mainActivity = (MainActivity)getActivity();
                    mainActivity.citiesDL.closeDrawers();
                }
            }
        });

        queryProvinces();


    }

    //  在数据库中查询省份信息。如果没有查到再去服务器中查询
    private void queryProvinces(){
        titleTv.setText("中国");

        //  从数据库中查找Province.class的内容，没有条件约束，直接findAll
        provinceList = DataSupport.findAll(Province.class);
        if(provinceList.size()>0){
            dataList.clear();
            for(Province province : provinceList){
                //  列表中只存储名字就可以
                dataList.add(province.getPrivinceName());
            }
            //  记住列表划到的位置
            adapter.notifyDataSetChanged();
            citiesLv.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        } else {
            String addr = "http://guolin.tech/api/china";
            queryFromServer(addr, "province");
        }

    }

    private void queryCities(){
        titleTv.setText(selectedProvince.getPrivinceName());
        //  进入次级目录后，返回按钮可以调用
        //  利用sql查询的思路，从City.class中find找到符合where要求的结果
        cityList = DataSupport.where("provinceid = ?",
                String.valueOf(selectedProvince.getId())).find(City.class);

        if(cityList.size()>0){
            dataList.clear();
            for(City city : cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            citiesLv.setSelection(0);
            currentLevel = LEVEL_CITY;
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            //  原来记录省市代码的原因是为了补全在服务器上进行查询的url
            String addr = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(addr, "city");
        }

    }

    private void queryCounties(){
        titleTv.setText(selectedCity.getCityName());
        countyList = DataSupport.where("cityid=?",
                String.valueOf(selectedCity.getId())).find(County.class);

        if(countyList.size()>0){
            dataList.clear();
            for(County county: countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            citiesLv.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String addr = "http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
            queryFromServer(addr, "county");
        }
    }

    //  从服务器上查询数据。初始化当数据库中没有的时候，需要从这里查找。
    private void queryFromServer(String address, final String type){
        showProgressDialog();
        //  调用写好的发送http请求的方法。这个方法依赖于okhttp库的函数，由自定义的方法封装，接收地址，
        //  形成回调函数。
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                //  回到主线程
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //  获取http返回信息的主体部分
                String responseText = response.body().string();
                boolean result = false;

                //  通过type参数来确定需要查询的是哪一层级的信息
                if("province".equals(type)){
                    //  调用封装好的处理省信息的函数，将相关的name，id，code等信息构成Province的一个实例，
                    //  然后将这个实例存入数据库中（利用litepal封装好的save函数）
                    result = Utility.handleProvinceResponse(responseText);
                } else if("city".equals(type)){
                    result = Utility.handleCityResponse(responseText, selectedProvince.getId());
                } else if("county".equals(type)){
                    result = Utility.handleCountyResponse(responseText, selectedCity.getId());
                }

                //  如果成功读取了数据，在主线程中进行操作
                if(result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type)){
                                //  这时候存到了数据库中，就可以回调query的一系列方法在数据库中查找了
                                queryProvinces();
                            } else if("city".equals(type)){
                                queryCities();
                            } else if("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "请重试！", Toast.LENGTH_SHORT).show();
                            closeProgressDialog();
                        }
                    });
//
                }
            }

        });

    }

    //  显示进度条
    private void showProgressDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    //  关闭进度条
    private void closeProgressDialog(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }




}
