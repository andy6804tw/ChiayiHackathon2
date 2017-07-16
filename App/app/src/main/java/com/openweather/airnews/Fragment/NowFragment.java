package com.openweather.airnews.Fragment;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andy6804tw.trendchartviewlib.HorizontalScrollChartParentView;
import com.andy6804tw.trendchartviewlib.ITrendData;
import com.andy6804tw.trendchartviewlib.TrendChartView;
import com.andy6804tw.trendchartviewlib.TrendYAxisView;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.github.pwittchen.weathericonview.WeatherIconView;
import com.openweather.airnews.DataBase.DBAccess;
import com.openweather.airnews.DataModel.TrendHourBean;
import com.openweather.airnews.R;
import com.openweather.airnews.Util.Utils;
import com.openweather.airnews.View.AqiDialogFragment;
import com.openweather.airnews.View.TemperatureView;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class NowFragment extends Fragment {

    private DBAccess mAccess;
    private View mView;
    private SharedPreferences settings;
    //condition目前狀況
    private TextView tvLocation,tv_temp,tv_low,tv_high;
    private TemperatureView temperatureView;
    //Icon
    private WeatherIconView weatherIconView;
    //AQI
    private ArcProgress arc_progress;
    private int mIndex=0;
    private RelativeLayout AQIrelativeLayout;
    private TextView tvStr,tvDes,tvNormalsuggest,tvSiteName,tvPublishtime;
    //AQI VIew
    private TrendYAxisView trendYAxis;
    private HorizontalScrollChartParentView svContainer;
    private TrendChartView trendChartView;
    private int aqi[]={50,54,56,57,54,56,53,45,44,41,49,50,54,54,53,53,47,44,43,54,55,58,54,57,
            26,28,29,35,29,34,28,31,32,36,39,45,48,52,54,56,54,60,61,54,54,51,51,50,
            49,45,35,35,29,34,28,31,32,42,49,55,58,62,66,63,52,51,51,51,51,51,51,51};

    public NowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_now, container, false);
        mView=view;
        mAccess = new DBAccess(getContext(), "weather", null,1);
        settings=getActivity().getSharedPreferences("Data",MODE_PRIVATE);

        //左邊天氣
        weatherIconView = (WeatherIconView) view.findViewById(R.id.my_weather_icon);
        tv_temp = (TextView) view.findViewById(R.id.tv_temp);
        tvLocation = (TextView) view.findViewById(R.id.tvLocation);
        //右邊AQI
        arc_progress=(ArcProgress)view.findViewById(R.id.arc_progress);
        AQIrelativeLayout=(RelativeLayout)view.findViewById(R.id.AQIrelativeLayout);
        tvStr=(TextView)view.findViewById(R.id.tvStr);
        //底部測站、更新時間
        tvSiteName=(TextView)view.findViewById(R.id.tvSiteName);
        tvPublishtime=(TextView)view.findViewById(R.id.tvPublishtime);

        initAQIView();
        initWeather();
        initAQI();

        return view;
    }
    private void initWeather() {
        Cursor cl2 = mAccess.getData("Condition", null, null);
        cl2.moveToFirst();
        weatherIconView.setIconSize(90);
        weatherIconView.setIconColor(Color.WHITE);
        //天氣圖示
        weatherIconView.setIconResource(weatherIcon(cl2.getShort(6)));
        //設定氣溫與位置
        //設定位置與溫度
        Cursor c = mAccess.getData("Location", null, null);
        c.moveToFirst();
        final Cursor c2 = mAccess.getData("Condition", null, null);
        c2.moveToFirst();

        tvLocation.setText(c.getString(2)+"/"+c.getString(3));
        if(settings.getString("Temperature","").equals("°C")||settings.getString("Temperature","").equals("")) {
            tv_temp.setText(Math.round((c2.getShort(5)-32)*5/9.)+" °C");
        }else{
            tv_temp.setText(c2.getString(5)+" °F");
        }
    }
    private void initAQI() {
        Cursor cl2 = mAccess.getData("AIR", null, null);
        cl2.moveToFirst();
        Cursor cl3 = mAccess.getData("AQI", null, null);
        cl3.moveToFirst();

        if(cl2.getShort(3)>=0&&cl2.getShort(3)<=50){
            mIndex=1;
            AQIrelativeLayout.setBackgroundResource(R.drawable.round_box_air1);
        }
        else if(cl2.getShort(3)>=51&&cl2.getShort(3)<=100){
            mIndex=2;
            AQIrelativeLayout.setBackgroundResource(R.drawable.round_box_air2);
        }
        else if(cl2.getShort(3)>=101&&cl2.getShort(3)<=150){
            mIndex=3;
            AQIrelativeLayout.setBackgroundResource(R.drawable.round_box_air3);
        }
        else if(cl2.getShort(3)>=151&&cl2.getShort(3)<=200){
            mIndex=4;
            AQIrelativeLayout.setBackgroundResource(R.drawable.round_box_air4);
        }
        else if(cl2.getShort(3)>=201&&cl2.getShort(3)<=300){
            mIndex=5;
            AQIrelativeLayout.setBackgroundResource(R.drawable.round_box_air5);
        }
        else if(cl2.getShort(3)>=301&&cl2.getShort(3)<=500){
            mIndex=6;
            AQIrelativeLayout.setBackgroundResource(R.drawable.round_box_air6);
        }
        arc_progress.setProgress(cl2.getShort(3));
        cl3.moveToPosition(mIndex-1);
        tvStr.setText(cl3.getString(1));
        tvSiteName.setText("測站: "+cl2.getString(2));
        tvPublishtime.setText("最後更新時間: "+cl2.getString(1));

        //跳出AQI建議dialog
        arc_progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AqiDialogFragment fragment
                        = AqiDialogFragment.newInstance(
                        8,
                        4,
                        false,
                        false
                );
                fragment.show(getActivity().getFragmentManager(), "blur_sample");
            }
        });

    }
    public String weatherIcon(int code){
        //天氣圖示
        if(code==0)
            return getActivity().getString(R.string.wi_yahoo_0);
        else if(code==1)
            return getActivity().getString(R.string.wi_yahoo_1);
        else if(code==2)
            return getActivity().getString(R.string.wi_yahoo_2);
        else if(code==3)
            return getActivity().getString(R.string.wi_yahoo_3);
        else if(code==4)
            return getActivity().getString(R.string.wi_yahoo_4);
        else if(code==5)
            return getActivity().getString(R.string.wi_yahoo_5);
        else if(code==6)
            return getActivity().getString(R.string.wi_yahoo_6);
        else if(code==7)
            return getActivity().getString(R.string.wi_yahoo_7);
        else if(code==8)
            return getActivity().getString(R.string.wi_yahoo_8);
        else if(code==9)
            return getActivity().getString(R.string.wi_yahoo_9);
        else if(code==10)
            return getActivity().getString(R.string.wi_yahoo_10);
        else if(code==11)
            return getActivity().getString(R.string.wi_yahoo_11);
        else if(code==12)
            return getActivity().getString(R.string.wi_yahoo_12);
        else if(code==13)
            return getActivity().getString(R.string.wi_yahoo_13);
        else if(code==14)
            return getActivity().getString(R.string.wi_yahoo_14);
        else if(code==15)
            return getActivity().getString(R.string.wi_yahoo_15);
        else if(code==16)
            return getActivity().getString(R.string.wi_yahoo_16);
        else if(code==17)
            return getActivity().getString(R.string.wi_yahoo_17);
        else if(code==18)
            return getActivity().getString(R.string.wi_yahoo_18);
        else if(code==19)
            return getActivity().getString(R.string.wi_yahoo_19);
        else if(code==20)
            return getActivity().getString(R.string.wi_yahoo_20);
        else if(code==21)
            return getActivity().getString(R.string.wi_yahoo_21);
        else if(code==22)
            return getActivity().getString(R.string.wi_yahoo_22);
        else if(code==23)
            return getActivity().getString(R.string.wi_yahoo_23);
        else if(code==24)
            return getActivity().getString(R.string.wi_yahoo_24);
        else if(code==25)
            return getActivity().getString(R.string.wi_yahoo_25);
        else if(code==26)
            return getActivity().getString(R.string.wi_yahoo_26);
        else if(code==27)
            return getActivity().getString(R.string.wi_yahoo_27);
        else if(code==28)
            return getActivity().getString(R.string.wi_yahoo_28);
        else if(code==29)
            return getActivity().getString(R.string.wi_yahoo_29);
        else if(code==30)
            return getActivity().getString(R.string.wi_yahoo_30);
        else if(code==31)
            return getActivity().getString(R.string.wi_yahoo_31);
        else if(code==32)
            return getActivity().getString(R.string.wi_yahoo_32);
        else if(code==33)
            return getActivity().getString(R.string.wi_yahoo_33);
        else if(code==34)
            return getActivity().getString(R.string.wi_yahoo_34);
        else if(code==35)
            return getActivity().getString(R.string.wi_yahoo_35);
        else if(code==36)
            return getActivity().getString(R.string.wi_yahoo_36);
        else if(code==37)
            return getActivity().getString(R.string.wi_yahoo_37);
        else if(code==38)
            return getActivity().getString(R.string.wi_yahoo_38);
        else if(code==39)
            return getActivity().getString(R.string.wi_yahoo_39);
        else if(code==40)
            return getActivity().getString(R.string.wi_yahoo_40);
        else if(code==41)
            return getActivity().getString(R.string.wi_yahoo_41);
        else if(code==42)
            return getActivity().getString(R.string.wi_yahoo_42);
        else if(code==43)
            return getActivity().getString(R.string.wi_yahoo_43);
        else if(code==44)
            return getActivity().getString(R.string.wi_yahoo_44);
        else if(code==45)
            return getActivity().getString(R.string.wi_yahoo_45);
        else if(code==46)
            return getActivity().getString(R.string.wi_yahoo_46);
        else if(code==47)
            return getActivity().getString(R.string.wi_yahoo_47);
        else
            return getActivity().getString(R.string.wi_yahoo_3200);
    }
    //以下是AQI View
    private void initAQIView(){
        trendYAxis = (TrendYAxisView) mView.findViewById(R.id.trend_y_axis);
        svContainer = (HorizontalScrollChartParentView) mView.findViewById(R.id.sv_container);
        trendChartView = (TrendChartView) mView.findViewById(R.id.trend_chart_view);
        trendYAxis.setChartView(trendChartView);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) trendYAxis.getLayoutParams();
        params.width = trendChartView.getLeftMarginSize();
        params.height = trendChartView.getChartHeight();
        trendYAxis.setLayoutParams(params);

        svContainer.setOnScrollListener(new HorizontalScrollChartParentView.OnScrollListener() {
            @Override
            public void onScrollChanged(int x, int y, int oldX, int oldY) {
                trendChartView.onTrendCharScrollChanged(x, y, oldX, oldY);
            }
        });

        trendChartView.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                trendChartView.fillData(formatDataList(), mockDayList(), getForecastDaysCount());
            }
        });
        trendChartView.postDelayed(new Runnable() {
            @Override
            public void run() {
                svContainer.smoothScrollTo(trendChartView.getOffsetByPosition(1),0);
            }
        },1000);

    }
    private int getForecastDaysCount() {
        return 3;
    }

    private List<String> mockDayList() {
        List<String> list = new ArrayList<>(getForecastDaysCount());
        list.add("昨天");
        list.add("今天");
        list.add("明天");
        return list;
    }
    List<ITrendData> formatDataList() {
        List<ITrendData> dataList = new ArrayList<>();
        long todayTime = getDayBegin().getTime();
        long yesterday = todayTime - 24 * 60 * 60 * 1000;
        Random random = new Random();
        for (int i = 0; i < getForecastDaysCount() * 24; i++) {
            int baseValue = 300;
            if (i / 24 == 0) {
                baseValue = 40;
            }else if(i / 24 == 1){
                baseValue = 50;
            }else if(i / 24 == 2){
                baseValue = 55;
            }else if(i / 24 == 3){
                baseValue = 52;
            }else if(i / 24 == 4){
                baseValue = 65;
            }else if(i / 24 == 5){
                baseValue = 100;
            }else if(i / 24 == 6){
                baseValue = 20;
            }
            int aqiValue = baseValue+i*2;
            Log.d("=======", "baseValue "+baseValue+" aqiValue "+aqiValue);
            //int aqiLevel = Utils.getAqiIndex(aqiValue);
            int aqiLevel = Utils.getAqiIndex(aqi[i]);
            String aqiDesc = getString(Utils.getIndexDescription(aqiLevel));
            @ColorInt int color = Utils.getColor(getContext(), Utils.getIndexColor(aqiLevel));
            dataList.add(new TrendHourBean(yesterday + (60 * 60 * 1000 * i), aqi[i], aqiLevel, color, aqi[i], aqiDesc));
        }
        return dataList;
    }
    public Timestamp getDayBegin() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 001);
        return new Timestamp(cal.getTimeInMillis());
    }
}
