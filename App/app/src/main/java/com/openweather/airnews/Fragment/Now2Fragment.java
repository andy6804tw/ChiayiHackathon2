package com.openweather.airnews.Fragment;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.github.pwittchen.weathericonview.WeatherIconView;
import com.openweather.airnews.DataBase.DBAccess;
import com.openweather.airnews.R;
import com.openweather.airnews.View.TemperatureView;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Now2Fragment extends Fragment {

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


    public Now2Fragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_now2, container, false);
        mView=view;
        mAccess = new DBAccess(getContext(), "weather", null,1);
        settings=getActivity().getSharedPreferences("Data",MODE_PRIVATE);

        tv_low = (TextView) view.findViewById(R.id.tv_low);
        tv_high = (TextView) view.findViewById(R.id.tv_high);
        tv_temp = (TextView) view.findViewById(R.id.tv_temp);
        temperatureView = (TemperatureView) view.findViewById(R.id.temperatureView);
        tvLocation = (TextView) view.findViewById(R.id.tvLocation);
        weatherIconView = (WeatherIconView) view.findViewById(R.id.my_weather_icon);

        arc_progress=(ArcProgress)view.findViewById(R.id.arc_progress);
        AQIrelativeLayout=(RelativeLayout)view.findViewById(R.id.AQIrelativeLayout);
        tvSiteName=(TextView)view.findViewById(R.id.tvSiteName);
        tvPublishtime=(TextView)view.findViewById(R.id.tvPublishtime);
        tvStr=(TextView)view.findViewById(R.id.tvStr);

        initView();
        initWeatherIcon();
        initAQI();

        Cursor c = mAccess.getData("Location", null, null);
        c.moveToFirst();
        Log.e("Loc",c.getString(0)+" "+c.getString(1)+" "+c.getString(2)+" "+c.getString(3)+" "+c.getString(4)+" "+c.getString(5)+" "+c.getString(6));

        Cursor cl2 = mAccess.getData("Condition", null, null);
        cl2.moveToFirst();
        Log.e("Condition",cl2.getString(0)+" "+cl2.getString(1)+" "+cl2.getString(2)+" "+cl2.getString(3)+" "+cl2.getString(4)+" "+cl2.getString(5)+" "+cl2.getString(6)+" "+cl2.getString(7));

        Cursor cl3 = mAccess.getData("Forecast", null, null);
        //cl3.move(5);
//        Log.e("Forecast",cl3.getString(0)+" "+cl3.getString(1)+" "+cl3.getString(2)+" "+cl3.getString(3)+" "+cl3.getString(4)+" "+cl3.getString(5));


        return view;
    }

    private void initAQI() {
            Cursor cl2 = mAccess.getData("AIR", null, null);
            cl2.moveToFirst();
            Cursor cl3 = mAccess.getData("AQI", null, null);
            cl3.moveToFirst();

            if(cl2.getShort(2)>=0&&cl2.getShort(2)<=50){
                mIndex=1;
                AQIrelativeLayout.setBackgroundResource(R.drawable.round_box_air1);
            }
            else if(cl2.getShort(2)>=51&&cl2.getShort(2)<=100){
                mIndex=2;
                AQIrelativeLayout.setBackgroundResource(R.drawable.round_box_air2);
            }
            else if(cl2.getShort(2)>=101&&cl2.getShort(2)<=150){
                mIndex=3;
                AQIrelativeLayout.setBackgroundResource(R.drawable.round_box_air3);
            }
            else if(cl2.getShort(2)>=151&&cl2.getShort(2)<=200){
                mIndex=4;
                AQIrelativeLayout.setBackgroundResource(R.drawable.round_box_air4);
            }
            else if(cl2.getShort(2)>=201&&cl2.getShort(2)<=300){
                mIndex=5;
                AQIrelativeLayout.setBackgroundResource(R.drawable.round_box_air5);
            }
            else if(cl2.getShort(2)>=301&&cl2.getShort(2)<=500){
                mIndex=6;
                AQIrelativeLayout.setBackgroundResource(R.drawable.round_box_air6);
            }
            arc_progress.setProgress(cl2.getShort(3));
            cl3.moveToPosition(mIndex-1);
            tvStr.setText(cl3.getString(1));
            /*tvDes.setText(cl3.getString(3));
            tvNormalsuggest.setText(cl3.getString(2));*/
            tvSiteName.setText("測站: "+cl2.getString(2));
            tvPublishtime.setText("最後更新時間: "+cl2.getString(1));
    }

    private void initWeatherIcon() {
        Cursor cl2 = mAccess.getData("Condition", null, null);
        cl2.moveToFirst();
        weatherIconView.setIconSize(85);
        weatherIconView.setIconColor(Color.WHITE);
        //天氣圖示
        weatherIconView.setIconResource(weatherIcon(cl2.getShort(6)));
    }

    private void initView() {
        Cursor c = mAccess.getData("Location", null, null);
        c.moveToFirst();
        final Cursor c2 = mAccess.getData("Condition", null, null);
        c2.moveToFirst();
        long temp=Math.round((c2.getShort(5)-32)*5/9.);
        if(temp<-9)
            temperatureView.start((temp-16));
        else if(temp<5)
            temperatureView.start(temp-12);
        else if(temp<11)
            temperatureView.start((temp-7));
        else if(temp<16)
            temperatureView.start((temp-4));
        else if(temp<20)
            temperatureView.start(temp-2);
        else if(temp<30&&temp>=25)
            temperatureView.start(temp+2);
        else if(temp>=35)
            temperatureView.start(temp+6);
        else if(temp>=30)
            temperatureView.start(temp+4);
        else
            temperatureView.start(temp);
        tvLocation.setText(c.getString(2));
        /*if(settings.getString("Temperature","").equals("°C")||settings.getString("Temperature","").equals("")) {
            tvChill.setText(getActivity().getString(R.string.tvChill)+" "+Math.round((c3.getShort(1)-32)*5/9.)+"°C");
        }else{
            tvChill.setText(getActivity().getString(R.string.tvChill)+" "+c3.getString(1)+"°F");
        }*/
        if(settings.getString("Temperature","").equals("°C")||settings.getString("Temperature","").equals("")) {
            tv_high.setText(Math.round((c2.getShort(3)-32)*5/9.)+"°");
            tv_low.setText(Math.round((c2.getShort(4)-32)*5/9.)+"°");
            tv_temp.setText(Math.round((c2.getShort(5)-32)*5/9.)+"°");
        }else{
            tv_high.setText(c2.getString(3)+"°");
            tv_low.setText(c2.getString(4)+"°");
            tv_temp.setText(c2.getString(5)+"°");
        }
        temperatureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long temp=Math.round((c2.getShort(5)-32)*5/9.);
                if(temp<-9)
                    temperatureView.start((temp-16));
                else if(temp<5)
                    temperatureView.start(temp-12);
                else if(temp<11)
                    temperatureView.start((temp-7));
                else if(temp<16)
                    temperatureView.start((temp-4));
                else if(temp<20)
                    temperatureView.start(temp-2);
                else if(temp<30&&temp>=25)
                    temperatureView.start(temp+2);
                else if(temp>=35)
                    temperatureView.start(temp+6);
                else if(temp>=30)
                    temperatureView.start(temp+4);
                else
                    temperatureView.start(temp);
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
}
