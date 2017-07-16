package com.openweather.airnews.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by andy6804tw on 2017/7/8.
 */

public class DBAccess extends SQLiteOpenHelper {
    private Context mContext;
    public DBAccess(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql1 = "create table Location("
                +"loc_id integer not null primary key,"
                +"country string(10),"
                +"city char(10),"
                +"district varchar(10),"
                +"village char(10),"
                +"latitude string,"
                +"longitude string"
                +")";
        Log.e("SQLDB",sql1);
        db.execSQL(sql1);

        String sql2 = "create table AIR("
                +"loc_id integer not null primary key,"
                +"PublishTime string(20),"
                +"SiteName string(10),"
                +"AQI int(5),"
                +"SO2 int(3),"
                +"CO int(3),"
                +"O3 int(4),"
                +"PM10 int(4),"
                +"PM25 int(4),"
                +"NO2 int(4),"
                +"NOX int(4),"
                +"NO1 int(4)"
                +")";
        Log.e("SQLDB",sql2);
        db.execSQL(sql2);

        String sql3 = "create table AQI("
                +"AQI_index integer not null primary key,"
                +"AQI_suggest varchar(35),"
                +"AQI_normalsuggest varchar(35),"
                +"AQI_des varchar(40),"
                +"foreign key (AQI_index) references Air(AQI) "
                +")";
        Log.e("SQLDB",sql3);
        db.execSQL(sql3);

        String sql4 = "create table PM25("
                +"PM25_index integer not null primary key,"
                +"PM25_sort varchar(4),"
                +"PM25_level int(3),"
                +"PM25_normalsuggest varchar(100),"
                +"PM25_allergysuggest varchar(100),"
                +"foreign key(PM25_index)references AQI(PM25)"
                +")";
        Log.e("SQLDB",sql4);
        db.execSQL(sql4);

        String sql5 = "create table Condition("
                +"loc_id int(10) not null primary key,"
                +"date string(20),"
                +"day string(5),"
                +"high int(5),"
                +"low int(5),"
                +"temp int(5),"
                +"code int(4),"
                +"publish_time string(20)"
                +")";
        Log.e("SQLDB",sql5);
        db.execSQL(sql5);


        String sql6 = "create table Forecast("
                + "forecast_id integer not null primary key,"
                + "date varchar(15),"
                + "day varchar(5),"
                + "high int(5),"
                + "low int(5),"
                + "text varchar(20)"
                + ")";
        Log.e("SQLDB",sql6);
        db.execSQL(sql6);

        String sql7 = "create table PMForecast("
                + "pmforecast_id integer not null primary key,"
                + "Area char(10),"
                + "AQI int(5),"
                + "MajorPollutant char(5),"
                + "Content varchar(100),"
                + "ForecastDate varchar(15)"
                + ")";
        Log.e("SQLDB",sql7);
        db.execSQL(sql7);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Location");
        db.execSQL("DROP TABLE IF EXISTS Air");
        db.execSQL("DROP TABLE IF EXISTS AQI");
        db.execSQL("DROP TABLE IF EXISTS PM25");
        db.execSQL("DROP TABLE IF EXISTS Condition");
        db.execSQL("DROP TABLE IF EXISTS Forecast");
        db.execSQL("DROP TABLE IF EXISTS PMForecast");
        onCreate(db);
    }

    public long add(){

        SQLiteDatabase db = getWritableDatabase();
/*
        //Location
        ContentValues va1 = new ContentValues();
        va1.put("loc_id", 1);
        va1.put("country","台灣");
        va1.put("city","台北市");
        va1.put("district","士林區");
        va1.put("village","百齡里");
        va1.put("latitude",25.084874123);
        va1.put("longitude",121.515486552);
        db.insert("Location", null, va1);
        //Air
        ContentValues va2 = new ContentValues();
        va2.put("loc_id",1);
        va2.put("PublishTime","2017/03/10 15:30");
        va2.put("SiteName","台南");
        va2.put("AQI",0);
        va2.put("SO2",1);
        va2.put("CO",2);
        va2.put("O3",3);
        va2.put("PM10",4);
        va2.put("PM25",5);
        va2.put("NO2",6);
        va2.put("NOX",7);
        va2.put("NO1",8);
        db.insert("AIR",null,va2);*/
        //AQI
        for(int i = 1;i<=6;i++){
            ContentValues va3 = new ContentValues();
            switch (i){
                case 1:
                    va3.put("AQI_index",1);
                    va3.put("AQI_suggest","良好");
                    va3.put("AQI_normalsuggest","一般民眾活動建議:\n\n正常戶外活動。");
                    va3.put("AQI_des","人體健康影響: \n\n空氣品質為良好，污染程度低或無污染。");
                    db.insert("AQI",null,va3);
                    break;
                case 2:
                    va3.put("AQI_index",2);
                    va3.put("AQI_suggest","普通");
                    va3.put("AQI_normalsuggest","一般民眾活動建議:\n\n正常戶外活動。");
                    va3.put("AQI_des","人體健康影響: \n\n空氣品質普通；但對非常少數之極敏感族群產生輕微影響。");
                    db.insert("AQI",null,va3);
                    break;
                case 3:
                    va3.put("AQI_index",3);
                    va3.put("AQI_suggest","對敏感群不良");
                    va3.put("AQI_normalsuggest","一般民眾活動建議:\n\n1.一般民眾如果有不適，如眼痛，咳嗽或喉嚨痛等，應該考慮減少戶外活動。\n" +
                            "2.學生仍可進行戶外活動，但建議減少長時間劇烈運動。");
                    va3.put("AQI_des","人體健康影響: \n\n空氣污染物可能會對敏感族群的健康造成影響，但是對一般大眾的影響不明顯。");
                    db.insert("AQI",null,va3);
                    break;
                case 4:
                    va3.put("AQI_index",4);
                    va3.put("AQI_suggest","對所有群族不良");
                    va3.put("AQI_normalsuggest","0一般民眾活動建議:\n\n1.一般民眾如果有不適，如眼痛，咳嗽或喉嚨痛等，應減少體力消耗，特別是減少戶外活動。\n" +
                            "2.學生應避免長時間劇烈運動，進行其他戶外活動時應增加休息時間。");
                    va3.put("AQI_des","人體健康影響: \n\n對所有人的健康開始產生影響，對於敏感族群可能產生較嚴重的健康影響。");
                    db.insert("AQI",null,va3);
                    break;
                case 5:
                    va3.put("AQI_index",5);
                    va3.put("AQI_suggest","非常不良");
                    va3.put("AQI_normalsuggest","一般民眾活動建議:\n\n1.一般民眾應減少戶外活動。\n" +
                            "2.學生應立即停止戶外活動，並將課程調整於室內進行。");
                    va3.put("AQI_des","人體健康影響: \n\n健康警報：所有人都可能產生較嚴重的健康影響。");
                    db.insert("AQI",null,va3);
                    break;
                case 6:
                    va3.put("AQI_index",6);
                    va3.put("AQI_suggest","有害");
                    va3.put("AQI_normalsuggest","一般民眾活動建議:\n\n1.一般民眾應避免戶外活動，室內應緊閉門窗，必要外出應配戴口罩等防護用具。\n" +
                            "2.學生應立即停止戶外活動，並將課程調整於室內進行。");
                    va3.put("AQI_des","人體健康影響: \n\n健康威脅達到緊急，所有人都可能受到影響。");
                    db.insert("AQI",null,va3);
                    break;
            }
        }
        //PM25
        for(int j =1;j<=10;j++){
            ContentValues va4 = new ContentValues();
            switch (j){
                case 1:
                    va4.put("PM25_index",1);
                    va4.put("PM25_sort","低");
                    va4.put("PM25_level",1);
                    va4.put("PM25_normalsuggest","一般民眾活動建議:\n\n正常戶外活動");
                    va4.put("PM25_allergysuggest","敏感性族群活動建議:\n\n正常戶外活動");
                    db.insert("PM25",null,va4);
                    break;
                case 2:
                    va4.put("PM25_index",2);
                    va4.put("PM25_sort","低");
                    va4.put("PM25_level",2);
                    va4.put("PM25_normalsuggest","一般民眾活動建議:\n\n正常戶外活動");
                    va4.put("PM25_allergysuggest","敏感性族群活動建議:\n\n正常戶外活動");
                    db.insert("PM25",null,va4);
                    break;
                case 3:
                    va4.put("PM25_index",3);
                    va4.put("PM25_sort","低");
                    va4.put("PM25_level",3);
                    va4.put("PM25_normalsuggest","一般民眾活動建議:\n\n正常戶外活動");
                    va4.put("PM25_allergysuggest","敏感性族群活動建議:\n\n正常戶外活動");
                    db.insert("PM25",null,va4);
                    break;
                case 4:
                    va4.put("PM25_index",4);
                    va4.put("PM25_sort","中");
                    va4.put("PM25_level",4);
                    va4.put("PM25_normalsuggest","一般民眾活動建議:\n\n正常戶外活動");
                    va4.put("PM25_allergysuggest","敏感性族群活動建議:\n\n有心臟、呼吸道及心血管疾病的成人與孩童感受到癥狀時，應考慮減少體力消耗，特別是減少戶外活動。");
                    db.insert("PM25",null,va4);
                    break;
                case 5:
                    va4.put("PM25_index",5);
                    va4.put("PM25_sort","中");
                    va4.put("PM25_level",5);
                    va4.put("PM25_normalsuggest","一般民眾活動建議:\n\n正常戶外活動");
                    va4.put("PM25_allergysuggest","敏感性族群活動建議:\n\n有心臟、呼吸道及心血管疾病的成人與孩童感受到癥狀時，應考慮減少體力消耗，特別是減少戶外活動。");
                    db.insert("PM25",null,va4);
                    break;
                case 6:
                    va4.put("PM25_index",6);
                    va4.put("PM25_sort","中");
                    va4.put("PM25_level",6);
                    va4.put("PM25_normalsuggest","一般民眾活動建議:\n\n正常戶外活動");
                    va4.put("PM25_allergysuggest", "敏感性族群活動建議:\n\n有心臟、呼吸道及心血管疾病的成人與孩童感受到癥狀時，應考慮減少體力消耗，特別是減少戶外活動。");
                    db.insert("PM25",null,va4);
                    break;
                case 7:
                    va4.put("PM25_index",7);
                    va4.put("PM25_sort","高");
                    va4.put("PM25_level",7);
                    va4.put("PM25_normalsuggest","一般民眾活動建議:\n\n任何人如果有不適，如眼痛，咳嗽或喉嚨痛等，應該考慮減少戶外活動。");
                    va4.put("PM25_allergysuggest","敏感性族群活動建議:\n\n1. 有心臟、呼吸道及心血管疾病的成人與孩童，應減少體力消耗，特別是減少戶外活動。\n" +
                            "2. 老年人應減少體力消耗。\n" +
                            "3. 具有氣喘的人可能需增加使用吸入劑的頻率。 \t\n" );
                    db.insert("PM25",null,va4);
                    break;
                case 8:
                    va4.put("PM25_index",8);
                    va4.put("PM25_sort","高");
                    va4.put("PM25_level",8);
                    va4.put("PM25_normalsuggest","一般民眾活動建議:\n\n任何人如果有不適，如眼痛，咳嗽或喉嚨痛等，應該考慮減少戶外活動。");
                    va4.put("PM25_allergysuggest","敏感性族群活動建議:\n\n1. 有心臟、呼吸道及心血管疾病的成人與孩童，應減少體力消耗，特別是減少戶外活動。\n" +
                            "2. 老年人應減少體力消耗。\n" +
                            "3. 具有氣喘的人可能需增加使用吸入劑的頻率。 \t\n");
                    db.insert("PM25",null,va4);
                    break;
                case 9:
                    va4.put("PM25_index",9);
                    va4.put("PM25_sort","高");
                    va4.put("PM25_level",9);
                    va4.put("PM25_normalsuggest","一般民眾活動建議:\n\n任何人如果有不適，如眼痛，咳嗽或喉嚨痛等，應該考慮減少戶外活動。");
                    va4.put("PM25_allergysuggest","敏感性族群活動建議:\n\n1. 有心臟、呼吸道及心血管疾病的成人與孩童，應減少體力消耗，特別是減少戶外活動。\n" +
                            "2. 老年人應減少體力消耗。\n" +
                            "3. 具有氣喘的人可能需增加使用吸入劑的頻率。 \t\n");
                    break;
                case 10:
                    va4.put("PM25_index",10);
                    va4.put("PM25_sort","高");
                    va4.put("PM25_level",10);
                    va4.put("PM25_normalsuggest","一般民眾活動建議:\n\n任何人如果有不適，如眼痛，咳嗽或喉嚨痛等，應該考慮減少戶外活動。");
                    va4.put("PM25_allergysuggest","敏感性族群活動建議:\n\n1. 有心臟、呼吸道及心血管疾病的成人與孩童，應減少體力消耗，特別是減少戶外活動。\n" +
                            "2. 老年人應減少體力消耗。\n" +
                            "3. 具有氣喘的人可能需增加使用吸入劑的頻率。 \t\n");
                    db.insert("PM25",null,va4);
                    break;


            }
        }

        db.close();
        return 1;
    }
    public Cursor getData(String NAME, String whereStr, String orderbyStr) {
        SQLiteDatabase db = getReadableDatabase();
        switch (NAME) {
            case "Location": {
                return db.query(NAME, new String[]{"loc_id", "country", "city", "district"
                                , "village", "latitude", "longitude"}
                        , whereStr, null, null, null, orderbyStr);
            }
            case "AIR":{
                return db.query(NAME, new String[]{"loc_id", "PublishTime","SiteName", "AQI", "SO2"
                                , "CO", "O3", "PM10","PM25","NO2","NOX","NO1"}
                        , whereStr, null, null, null, orderbyStr);
            }
            case "AQI":{
                return db.query(NAME, new String[]{"AQI_index", "AQI_suggest", "AQI_normalsuggest", "AQI_des"}
                        , whereStr, null, null, null, orderbyStr);
            }
            case "PM25":{
                return db.query(NAME, new String[]{"PM25_index", "PM25_sort", "PM25_level", "PM25_normalsuggest"
                                , "PM25_allergysuggest"}
                        , whereStr, null, null, null, orderbyStr);
            }
            case "Condition":{
                return db.query(NAME, new String[]{"loc_id", "date", "day", "high"
                                , "low","temp","code","publish_time"}
                        , whereStr, null, null, null, orderbyStr);
            }
            case "Forecast":{
                return db.query(NAME, new String[]{"forecast_id","date","day","high","low","text"}
                        , whereStr, null, null, null, orderbyStr);
            }case "PMForecast":{
                return db.query(NAME, new String[]{"pmforecast_id","Area","AQI","MajorPollutant","Content","ForecastDate"}
                        , whereStr, null, null, null, orderbyStr);
            }
            default: {
                return null;
            }
        }
    }

    //Location
    public long add(String loc_id,String country,String city,String district
            ,String village,String latitude,String longitude){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put("loc_id",loc_id);
        values.put("country",country);
        values.put("city",city);
        values.put("district",district);
        values.put("village",village);
        values.put("latitude",Double.parseDouble(latitude));
        values.put("longitude",Double.parseDouble(longitude));
        return db.insert("Location",null,values);
    }
    public long update(String loc_id,String country,String city,String district
            ,String village,String latitude,String longitude,String whereClause){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put("loc_id",loc_id);
        values.put("country",country);
        values.put("city",city);
        values.put("district",district);
        values.put("village",village);
        values.put("latitude",latitude);
        values.put("longitude",longitude);
        long result=db.update("Location", values, whereClause, null);
        db.close();
        return result;//回傳更新資料筆數
    }

    //AIR
    public long add(String loc_id,String PublishTime,String SiteName,String AQI,String SO2,String CO,String O3,String PM10,String PM25,String NO2,String NOX,String NO1){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put("loc_id",loc_id);
        values.put("PublishTime",PublishTime);
        values.put("SiteName",SiteName);
        values.put("AQI",AQI);
        values.put("SO2",SO2);
        values.put("CO",CO);
        values.put("O3",O3);
        values.put("PM10",PM10);
        values.put("PM25",PM25);
        values.put("NO2",NO2);
        values.put("NOX",NOX);
        values.put("NO1",NO1);
        return db.insert("AIR",null,values);
    }
    public long update(String loc_id,String PublishTime,String SiteName,String AQI,String SO2,String CO,String O3,String PM10,String PM25,String NO2,String NOX,String NO1,String whereClause){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put("loc_id",loc_id);
        values.put("PublishTime",PublishTime);
        values.put("SiteName",SiteName);
        values.put("AQI",AQI);
        values.put("SO2",SO2);
        values.put("CO",CO);
        values.put("O3",O3);
        values.put("PM10",PM10);
        values.put("PM25",PM25);
        values.put("NO2",NO2);
        values.put("NOX",NOX);
        values.put("NO1",NO1);
        long result=db.update("AIR", values, whereClause, null);
        db.close();
        return result;//回傳更新資料筆數
    }
    //Condition
    public long add(String loc_id,String date,String day,Double high,Double low,Double temp,int code,String publish_time){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put("loc_id",loc_id);
        values.put("date",date);
        values.put("day",day);
        values.put("high",high);
        values.put("low",low);
        values.put("temp",temp);
        values.put("code",code+"");
        values.put("publish_time",publish_time);
        return db.insert("Condition", null,values);
    }
    public long update(String loc_id,String date,String day,Double high,Double low,Double temp,int code,String publish_time,String whereClause){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put("loc_id",loc_id);
        values.put("date",date);
        values.put("day",day);
        values.put("high",high);
        values.put("low",low);
        values.put("temp",temp);
        values.put("code",code);
        values.put("publish_time",publish_time);
        long result=db.update("Condition", values, whereClause, null);
        db.close();
        return result;//回傳更新資料筆數
    }
    //Forecast
    public long add(String forecast_id,String date,String day,Double high,Double low,String text){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("forecast_id",forecast_id);
        values.put("date",date);
        values.put("day",day);
        values.put("high",high);
        values.put("low",low);
        values.put("text",text);
        return db.insert("Forecast",null,values);
    }
    public long update(String forecast_id,String date,String day,Double high,Double low,String text,String whereClause){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values =new ContentValues();
        //values.put("forecast_id",forecast_id);
        values.put("date",date);
        values.put("day",day);
        values.put("high",high);
        values.put("low",low);
        values.put("text",text);
        long result=db.update("Forecast", values, whereClause, null);
        db.close();
        return result;//回傳更新資料筆數
    }
    //PMForecast
    public long add(String pmforecast_id,String Area,int AQI,String MajorPollutant,String Content,String ForecastDate){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pmforecast_id",pmforecast_id);
        values.put("Area",Area);
        values.put("AQI",AQI);
        values.put("MajorPollutant",MajorPollutant);
        values.put("Content",Content);
        values.put("ForecastDate",ForecastDate);
        return db.insert("PMForecast",null,values);
    }
    public long update(String pmforecast_id,String Area,int AQI,String MajorPollutant,String Content,String ForecastDate,String whereClause){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values =new ContentValues();
        //values.put("forecast_id",forecast_id);
        //values.put("pmforecast_id",pmforecast_id);
        values.put("Area",Area);
        values.put("AQI",AQI);
        values.put("MajorPollutant",MajorPollutant);
        values.put("Content",Content);
        values.put("ForecastDate",ForecastDate);
        long result=db.update("PMForecast", values, whereClause, null);
        db.close();
        return result;//回傳更新資料筆數
    }
}