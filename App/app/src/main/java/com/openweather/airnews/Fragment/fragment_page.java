package com.openweather.airnews.Fragment;


import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.openweather.airnews.DataBase.DBAccess;
import com.openweather.airnews.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_page extends Fragment {

    private DBAccess mAccess;
    private TextView tvArea,tvMajorPollutant,tvContent,tvForecastDate;
    private RelativeLayout relativeLayout[]=new RelativeLayout[10];

    public fragment_page() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_fragment_page, container, false);
        mAccess = new DBAccess(getContext(), "weather", null,1);

        tvArea=(TextView) view.findViewById(R.id.tvArea);
        //tvMajorPollutant=(TextView) view.findViewById(R.id.tvMajorPollutant);
        tvContent=(TextView) view.findViewById(R.id.tvContent);
        tvForecastDate=(TextView) view.findViewById(R.id.tvForecastDate);
        relativeLayout[0]=(RelativeLayout)view.findViewById(R.id.rlRegion1);
        relativeLayout[1]=(RelativeLayout)view.findViewById(R.id.rlRegion2);
        relativeLayout[2]=(RelativeLayout)view.findViewById(R.id.rlRegion3);
        relativeLayout[3]=(RelativeLayout)view.findViewById(R.id.rlRegion4);
        relativeLayout[4]=(RelativeLayout)view.findViewById(R.id.rlRegion5);
        relativeLayout[5]=(RelativeLayout)view.findViewById(R.id.rlRegion6);
        relativeLayout[6]=(RelativeLayout)view.findViewById(R.id.rlRegion7);
        relativeLayout[7]=(RelativeLayout)view.findViewById(R.id.rlRegion8);
        relativeLayout[8]=(RelativeLayout)view.findViewById(R.id.rlRegion9);
        relativeLayout[9]=(RelativeLayout)view.findViewById(R.id.rlRegion10);

        initForecast();
        initForecastView();

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initForecastView() {
        Cursor cl7 = mAccess.getData("PMForecast", null, null);
        cl7.moveToFirst();
        for(int i=0;i<cl7.getCount();i++){
            if(cl7.getShort(2)>0&&cl7.getShort(2)<=50){
                relativeLayout[i].setBackground(getResources().getDrawable(R.drawable.circle_air1));
            }else if(cl7.getShort(2)>=51&&cl7.getShort(2)<=100){
                relativeLayout[i].setBackground(getResources().getDrawable(R.drawable.circle_air2));
            }else if(cl7.getShort(2)>=101&&cl7.getShort(2)<=150){
                relativeLayout[i].setBackground(getResources().getDrawable(R.drawable.circle_air3));
            }else if(cl7.getShort(2)>=151&&cl7.getShort(2)<=200){
                relativeLayout[i].setBackground(getResources().getDrawable(R.drawable.circle_air4));
            }else if(cl7.getShort(2)>=201&&cl7.getShort(2)<=300){
                relativeLayout[i].setBackground(getResources().getDrawable(R.drawable.circle_air5));
            }else if(cl7.getShort(2)>=301){
                relativeLayout[i].setBackground(getResources().getDrawable(R.drawable.circle_air6));
            }
            cl7.moveToNext();
        }
    }

    private void initForecast(){
        Cursor cl7 = mAccess.getData("PMForecast", null, null);
        cl7.moveToFirst();
        Cursor c = mAccess.getData("Location", null, null);
        c.moveToFirst();
        Log.e("AQIF",cl7.getString(0)+" "+cl7.getString(1)+" "+cl7.getString(2)+" "+cl7.getString(3)+" "+cl7.getString(4)+" "+cl7.getString(5)+" "+cl7.getCount());
        int index=0;
        if(c.getString(2).contains("基隆")||c.getString(2).contains("北")){
            index=0;
        }else if(c.getString(2).contains("竹")||c.getString(2).contains("苗")){
            index=1;
        }else if(c.getString(2).contains("中")||c.getString(2).contains("彰")||c.getString(2).contains("投")){
            index=2;
        }else if(c.getString(2).contains("雲")||c.getString(2).contains("嘉")||c.getString(2).contains("南")){
            index=3;
        }else if(c.getString(2).contains("高")||c.getString(2).contains("屏")){
            index=4;
        }else if(c.getString(2).contains("宜蘭")){
            index=5;
        }else if(c.getString(2).contains("花")||c.getString(2).contains("東")){
            index=6;
        }else if(c.getString(2).contains("馬祖")){
            index=7;
        }else if(c.getString(2).contains("金門")){
            index=8;
        }else if(c.getString(2).contains("澎湖")){
            index=9;
        }
        cl7.moveToPosition(index);
        tvArea.setText("空品區: "+cl7.getString(1));
        //tvMajorPollutant.setText("指標汙染物: "+cl7.getString(3));
        tvForecastDate.setText("預報日期: "+cl7.getString(5));
        String strForecast="";
        for(int i=0;i<cl7.getString(4).split("\r").length;i++){
            strForecast+=cl7.getString(4).split("\r")[i]+"\r\n";
        }
        tvContent.setText(strForecast);

    }


}
