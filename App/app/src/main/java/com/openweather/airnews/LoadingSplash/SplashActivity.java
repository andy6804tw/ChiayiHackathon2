package com.openweather.airnews.LoadingSplash;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.openweather.airnews.DataBase.DBAccess;
import com.openweather.airnews.DataModel.DataModel;
import com.openweather.airnews.MainActivity;
import com.openweather.airnews.R;
import com.openweather.airnews.Util.ExitApplication;
import com.openweather.airnews.Util.GPSTracker;
import com.victor.loading.newton.NewtonCradleLoading;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import static android.os.Build.VERSION_CODES.M;

public class SplashActivity extends AppCompatActivity {

    public static ArrayList<DataModel> list;
    private static Document document;
    private  NewtonCradleLoading loadingView;
    private GPSTracker mGps;
    private double mLatitude=0.0,mLongitude=0.0;
    private String mLanguage="en",mCity,mCountry,mDistrict,mVillage;
    private DBAccess mAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ExitApplication.getInstance().addActivity(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAccess = new DBAccess(this, "weather", null,1);
        list=new ArrayList<DataModel>();

        loadingView = (NewtonCradleLoading)findViewById(R.id.loadingView);
        loadingView.start();
        //loadingView.setLoadingColor(R.color.colorPrimary);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initYahooData();
        initAirForecast();
        GPSPremessionCheck();

    }

    private void initAirForecast() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://opendata.epa.gov.tw/webapi/api/rest/datastore/355000000I-000022?sort=PublishTime&offset=0&limit=1000";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //mTextView.setText("Response is: "+ response.substring(0,500));
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int index[]={0,3,6,9,12,15,18,21,22,23};
                            Cursor cl7 = mAccess.getData("PMForecast", null, null);
                            cl7.moveToFirst();
                            for(int i=0;i<index.length;i++){
                                String Area=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index[i]).getString("Area");
                                String Content=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index[i]).getString("Content");
                                String MajorPollutant=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index[i]).getString("MajorPollutant");
                                String AQI=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index[i]).getString("AQI");
                                String ForecastDate=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index[i]).getString("ForecastDate");
                                String PublishTime=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index[i]).getString("PublishTime");
                                //Log.e("Air_Forecast",Area+" "+Content.split("\r")[1]+" "+MajorPollutant+" "+AQI+" "+ForecastDate+" "+PublishTime);
                                if(cl7.getCount()!=10){
                                    mAccess.add(i+"",Area,Integer.parseInt(AQI),MajorPollutant,Content,ForecastDate);
                                }else{
                                    mAccess.update(i+"",Area,Integer.parseInt(AQI),MajorPollutant,Content,ForecastDate,"pmforecast_id ="+i);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SplashActivity.this, "無法連接網路!", Toast.LENGTH_SHORT).show();

            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public static void initYahooData(){
        //Yahoo天氣新聞
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    document = Jsoup.connect("https://video-weatherrisk-yahoopartner.tumblr.com/")
                            .timeout(3000)
                            .get();
                    Elements noteList = document.select("div.post-content");
                    Log.e("Data",noteList.select("h2.title").select("a").text().split(" ")[0]);
                    Log.e("Data",noteList.select("p").text().split(" ")[0]);
                    String url="https://tw.video.yahoo.com/weather/";
                    String img="https://s.yimg.com/dh/ap/default/130909/y_200_a.png";
                    list.add(0,new DataModel(noteList.select("h2.title").select("a").text().split(" ")[0],"Yahoo奇摩新聞氣象(報氣象)",noteList.select("p").text().split(" ")[0].substring(0,26)+"...",img,url));

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("TAG", "run: " + e.getMessage());
                }

            }
        }).start();
        initAirData();
    }
    public static void initAirData(){

        //環境新聞
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    document = Jsoup.connect("http://e-info.org.tw/taxonomy/term/258/allall?page0")
                            .timeout(3000)
                            .get();
                    Elements noteList = document.select("div").select("#block-system-main").select("div.views-row");
                    //Log.e("Data",noteList.toString());
                    int c=0;
                    //Log.e("Name"+c++,Image.attr("abs:src"));
                    for (Element element:noteList){
                        Elements title = element.select("div.views-field.views-field-title");
                        Elements url =element.select("div.views-field.views-field-title").select("a");
                        Elements Image = element.select("div.views-field.views-field-field-image").select("img");
                        Elements detail = element.select("div.views-field.views-field-body");
                        Elements time = element.select("span.views-field.views-field-created");
                        //Log.e("Title"+c++,title.text()+" "+Image.attr("abs:src")+" "+detail.text()+" "+time.text()+" "+url.attr("abs:href"));
                        list.add(new DataModel(title.text(),time.text(),detail.text().substring(0,25)+"...",Image.attr("abs:src"),url.attr("abs:href")));
                    }
                    for(int i =0;i<list.size();i++){
                       /* Log.e("Title"+i,list.get(i).getTitle());
                        Log.e("time"+i,list.get(i).getTime());
                        Log.e("detail"+i,list.get(i).getDetail());
                        Log.e("Image"+i,list.get(i).getImage());
                        Log.e("url"+i,list.get(i).getUrl());*/
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("TAG", "run: " + e.getMessage());
                }

            }
        }).start();

    }
    private void GPSPremessionCheck() {
        /**偵測權限**/
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //如果沒有授權使用定位就會跳出來這個
            // TODO: Consider calling
            //Log.e("Data6", "進入!");
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            // 如果裝置版本是6.0（包含）以上
            if (Build.VERSION.SDK_INT >= M) {
                // 取得授權狀態，參數是請求授權的名稱
                int hasPermission = checkSelfPermission(
                        Manifest.permission.ACCESS_FINE_LOCATION);
                // 如果未授權
                if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                    // 請求授權
                    //     第一個參數是請求授權的名稱
                    //     第二個參數是請求代碼
                    //Log.e("Data3", "失敗!");
                    requestPermissions(
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            10);
                }
                //Log.e("Data4", "成功!");
            }
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity( new Intent(SplashActivity.this, MainActivity.class));
                }
            }, 4500);
            init_GPS();
            init_Weather();
            initAirLoc();
        }

    }
    private void init_GPS() {
        mGps = new GPSTracker(this);
        if (mGps.canGetLocation && mGps.getLatitude() != (0.0) && mGps.getLongtitude() != (0.0)) {
            mLatitude = mGps.getLatitude();
            mLongitude =mGps.getLongtitude();
            if((mLatitude>=20&&mLatitude<=27)&&(mLongitude>=118&&mLongitude<=124))
                mLanguage="zh-TW";

            //Toast.makeText(getApplicationContext(), "Your Location is->\nLat: " + latitude + "\nLong: " + longtitude, Toast.LENGTH_LONG).show();
            ///**撈取時間資料START**///
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + mLatitude + "," + mLongitude + "&language="+mLanguage+"&sensor=true&key=AIzaSyDHA4UDKuJ_hZafj8Xn6m3mMzOsQnbTZ_w&lafhdfhfdhfdhrh";

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            //mTextView.setText("Response is: "+ response.substring(0,500));
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response);
                                int count = jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("address_components").length();
                                mCountry = jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("address_components").getJSONObject(count - 2).getString("long_name");
                                mCity = jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("address_components").getJSONObject(count - 3).getString("long_name");
                                mDistrict = jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("address_components").getJSONObject(count - 4).getString("short_name");
                                mVillage = jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("address_components").getJSONObject(count - 5).getString("short_name");
                                String str5 = jsonObject.getJSONArray("results").getJSONObject(0).getString("formatted_address");

                                //寫入Location資料表
                                Cursor c = mAccess.getData("Location", null, null);
                                c.moveToFirst();
                                if(c.getCount()==0){
                                    mAccess.add("1",mCountry,mCity,mDistrict,mVillage,mLatitude+"",mLongitude+"");

                                }else if(c.getDouble(5)!=mLatitude||c.getDouble(6)!=mLongitude){
                                    //Toast.makeText(SplashActivity.this,"更新位置->\nLat: " + latitude + "\nLong: " + longtitude,Toast.LENGTH_SHORT).show();
                                    mAccess.update("1",mCountry,mCity,mDistrict,mVillage,Double.toString(mLatitude),Double.toString(mLongitude),null);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }else{
            final LocationManager locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
            if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
                alertDialog.setTitle("Gps is settings");
                alertDialog.setMessage("Gps is not enabled.");
                alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        }
    }
    private void init_Weather() {
        ///**撈取資料END**///
        ///**撈取天氣資料START**///
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(SELECT%20woeid%20FROM%20geo.places%20WHERE%20text%3D\"("+mLatitude+","+mLongitude+")\")&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //mTextView.setText("Response is: "+ response.substring(0,500));
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            //位置 Location
                            if(!(mLatitude>=20&&mLatitude<=27)&&!(mLongitude>=118&&mLongitude<=124)){
                                mCountry = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("location").getString("country");
                                mCity = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("location").getString("city");
                                //寫入 Location 資料表
                                mAccess.update("1",mCountry,mCity,mDistrict,mVillage,Double.toString(mLatitude),Double.toString(mLongitude),null);
                            }
                           //狀態 Condition
                            String date = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(0).getString("date");
                            String day = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(0).getString("day");
                            String high = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(0).getString("high");
                            String low = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(0).getString("low");
                            String temp = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONObject("condition").getString("temp");
                            String code = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONObject("condition").getString("code");
                            String pushTime = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getString("pubDate");
                            String publish_time = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getString("lastBuildDate");
                            Cursor c = mAccess.getData("Condition", null, null);
                            c.moveToFirst();
                            if(c.getCount()==0) {
                                mAccess.add();
                                //寫入 Condition資料表
                                mAccess.add("1", date, day, Double.parseDouble(high), Double.parseDouble(low), Double.parseDouble(temp), Integer.parseInt(code),publish_time);
                                //寫入 Forecast
                               /* for(int i=0;i<10;i++){
                                    //預報Forecast
                                    String forecast_date = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(i).getString("date");
                                    String forecast_day = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(i).getString("day");
                                    String forecast_high = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(i).getString("high");
                                    String forecast_low = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(i).getString("low");
                                    String forecast_code = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(i).getString("code");
                                    mAccess.add(i+"", forecast_date, forecast_day,Double.parseDouble(forecast_high),Double.parseDouble(forecast_low),forecast_code);
                                }*/

                            }else{
                                //Toast.makeText(SplashActivity.this,publish_time,Toast.LENGTH_SHORT).show();

                                //寫入 Condition資料表
                                mAccess.update("1", date, day, Double.parseDouble(high), Double.parseDouble(low), Double.parseDouble(temp), Integer.parseInt(code),publish_time,null);
                                //寫入 Forecast
                               /* for(int i=0;i<10;i++){
                                    //預報Forecast
                                    String forecast_date = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(i).getString("date");
                                    String forecast_day = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(i).getString("day");
                                    String forecast_high = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(i).getString("high");
                                    String forecast_low = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(i).getString("low");
                                    String forecast_code = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(i).getString("code");
                                    mAccess.update(i+"", forecast_date, forecast_day,Double.parseDouble(forecast_high),Double.parseDouble(forecast_low),forecast_code,"forecast_id ="+i);
                                }*/
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SplashActivity.this, "無法連接網路!", Toast.LENGTH_SHORT).show();

            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    //計算兩點距離
    public  double calLocation(double wd1, double jd1, double wd2, double jd2) {
        double x, y, out;
        double PI = 3.14159265;
        double R = 6.371229 * 1e6;

        x = (jd2 - jd1) * PI * R * Math.cos(((wd1 + wd2) / 2) * PI / 180) / 180;
        y = (wd2 - wd1) * PI * R / 180;
        out = Math.hypot(x, y);
        return out / 1000;
    }
    public void initAirLoc() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://opendata.epa.gov.tw/webapi/api/rest/datastore/355000000I-000006?sort=SiteName&offset=0&limit=1000";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response);
                            //Log.e("jsonObject",jsonObject.toString());
                            //String SiteName=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(0).getString("SiteName");
                            Double min=Double.MAX_VALUE;
                            String siteName="";
                            int index=0;
                            for(int i=0;i<jsonObject.getJSONObject("result").getJSONArray("records").length();i++){
                                String SiteName=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(i).getString("SiteName");
                                String latitude=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(i).getString("TWD97Lat");
                                String longitude=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(i).getString("TWD97Lon");
                                Double loc=calLocation(mLatitude,mLongitude,Double.parseDouble(latitude),Double.parseDouble(longitude));
                                if(min>loc) {
                                    index=i;
                                    min = loc;
                                    siteName=SiteName;
                                }
                                //Log.e("Data"+i,"測站:"+SiteName+"    經度:"+latitude+"    緯度:"+longitude+"   "+loc+"km");
                            }
                            //Log.e("Informatin","Your Location is: "+mLatitude+","+mLongitude);
                            //Log.e("Air Result","The AirSite closest to you is "+siteName+"測站  distance->"+min+" km"+" "+index);
                            initAir(index);//找出距離最近測站擷取空氣品質OpenData

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "無法連接網路!", Toast.LENGTH_SHORT).show();
            }

        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    public void initAir(int Airindex) {
        final int index=Airindex;
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://opendata.epa.gov.tw/webapi/api/rest/datastore/355000000I-000259?sort=SiteName&offset=0&limit=1000";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response);
                            String SiteName=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("SiteName");
                            String PublishTime=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("PublishTime");
                            String AQI=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("AQI");
                            String SO2=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("SO2");
                            String CO=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("CO");
                            String O3=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("O3");
                            String PM10=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("PM10");
                            String PM25=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("PM2.5");
                            String NO2=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("NO2");
                            String NOx=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("NOx");
                            String NO1=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("NO");
                            /*Log.e("Air info","SiteName:"+SiteName+"   PublishTime:"+PublishTime+"   AQI:"+AQI+"   SO2:"+SO2+"   CO:"+CO+"   O3:"+O3+"  PM10:"+PM10+"   PM25:"
                                    +PM25+"   NO2:"+NO2+"   NOX:"+NOx+"  NO:"+NO1);*/
                            if(PM25.equals(""))
                                PM25="0";
                            Cursor cl2 = mAccess.getData("AIR", null, null);
                            cl2.moveToFirst();
                            if(cl2.getCount()==0){
                                mAccess.add("1", PublishTime, SiteName, AQI, SO2, CO, O3, PM10, PM25, NO2, NOx, NO1);
                            }else{
                                mAccess.update("1", PublishTime, SiteName, AQI, SO2, CO, O3, PM10, PM25, NO2, NOx, NO1,null);
                            }
                            Log.e("AIR",SiteName+" PM:"+PM25+" AQI:"+AQI+" "+PublishTime);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "無法連接網路!", Toast.LENGTH_SHORT).show();
                Cursor cl2 = mAccess.getData("AIR", null, null);
                cl2.moveToFirst();
                if (cl2.getCount() == 0)
                    mAccess.add("1", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
