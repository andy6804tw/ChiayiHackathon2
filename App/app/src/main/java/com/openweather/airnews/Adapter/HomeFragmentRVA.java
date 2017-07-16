package com.openweather.airnews.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.openweather.airnews.DataModel.DataModel;
import com.openweather.airnews.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by andy6804tw on 2017/7/6.
 */

public class HomeFragmentRVA extends RecyclerView.Adapter<HomeFragmentRVA.ViewHolder> {


    private final Context mContext;
    public ArrayList<DataModel> list;
    private int page=1;
    private String mDate="";
    private Boolean mCheck=true;
    private Boolean mStatus=true;
    private ViewHolder mViewHolder;

    public HomeFragmentRVA(Context context) {
        this.mContext = context;
    }
    public HomeFragmentRVA(Context context, ArrayList<DataModel> list) {
        this.mContext = context;
        this.list=list;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

       //痊癒變數
        private ImageView imageView;
        private RelativeLayout mapRelativeLayout;
        private TextView tvtvStatus1,tvtvStatus2;

        public ViewHolder(View itemView,int viewType) {
            super(itemView);
            if(viewType==1){
               //FindViewByID
                imageView=(ImageView)itemView.findViewById(R.id.imageView);
                mapRelativeLayout=(RelativeLayout)itemView.findViewById(R.id.mapRelativeLayout);
                tvtvStatus1=(TextView)itemView.findViewById(R.id.tvtvStatus1);
                tvtvStatus2=(TextView)itemView.findViewById(R.id.tvtvStatus2);
            }
            else {
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();

                    Snackbar.make(v, "Click detected on item " +position,
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
            });

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.card_qai, parent, false),viewType);
        }else if(viewType==1){
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.card_map, parent, false),viewType);
        }else
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.card_now, parent, false),viewType);


    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        if(position==1){
            mViewHolder=viewHolder;
            //載入時間+圖片
            initTime();
            //imageView圖片點擊事件
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mStatus){
                        viewHolder.tvtvStatus1.setTextColor(mContext.getResources().getColor(R.color.tvStatus2));
                        viewHolder.tvtvStatus2.setTextColor(mContext.getResources().getColor(R.color.tvStatus1));
                        mStatus = false;
                    }
                    else{
                        viewHolder.tvtvStatus1.setTextColor(mContext.getResources().getColor(R.color.tvStatus1));
                        viewHolder.tvtvStatus2.setTextColor(mContext.getResources().getColor(R.color.tvStatus2));
                        mStatus=true;
                    }
                    initTime();
                }
            });
            //RelativeLayout版面點擊事件
            viewHolder.mapRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mStatus){
                        viewHolder.tvtvStatus1.setTextColor(mContext.getResources().getColor(R.color.tvStatus2));
                        viewHolder.tvtvStatus2.setTextColor(mContext.getResources().getColor(R.color.tvStatus1));
                        mStatus = false;
                    }
                    else{
                        viewHolder.tvtvStatus1.setTextColor(mContext.getResources().getColor(R.color.tvStatus1));
                        viewHolder.tvtvStatus2.setTextColor(mContext.getResources().getColor(R.color.tvStatus2));
                        mStatus=true;
                    }
                    initTime();
                }
            });

        }else{

        }

    }

    @Override
    public int getItemCount() {
        return 10;
    }
    @Override
    public int getItemViewType(int position) {return  position;}


    private void addData() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Document document = Jsoup.connect("http://e-info.org.tw/taxonomy/term/258/all?page="+page)
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
                        list.add(new DataModel(title.text(),time.text(),detail.text(),Image.attr("abs:src"),url.attr("abs:href")));
                    }
                    for(int i =0;i<list.size();i++){
                        Log.e("Title"+i,list.get(i).getTitle());
                        Log.e("time"+i,list.get(i).getTime());
                        Log.e("detail"+i,list.get(i).getDetail());
                        Log.e("Image"+i,list.get(i).getImage());
                        Log.e("url"+i,list.get(i).getUrl());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("TAG", "run: " + e.getMessage());
                }

            }
        }).start();
    }
    private void initTime() {

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        //清空
        mDate="";
        //年
        mDate=year+"";
        //月份
        if(Integer.toString((month+1)).length()==1)
            mDate+="0"+(month+1);
        else
            mDate+=(month+1);
        //日期(先判斷小時是否00)
        if(hour==0){
            hour=24;
            day-=1;
        }
        if(Integer.toString(day).length()==1)
            mDate+="0"+day;
        else
            mDate+=day;
        //小時並判斷錯誤偵測
        if(mCheck){
            if(Integer.toString((hour-1)).length()==1)
                mDate+="-0"+(hour-1);
            else
                mDate+="-"+(hour-1);
        }else{
            if(Integer.toString((hour-2)).length()==1)
                mDate+="-0"+(hour-2);
            else
                mDate+="-"+(hour-2);
        }
        Log.e("reset", "進入"+mDate);
        //載入圖片
        DownloadImageTask downloadImageTask=new DownloadImageTask(mViewHolder.imageView);
        if(mStatus)
            downloadImageTask.execute("http://taqm.epa.gov.tw/taqm/map_Contour/"+mDate+"-0-33.jpg");
        else
            downloadImageTask.execute("http://taqm.epa.gov.tw/taqm/map_Contour/"+mDate+"-6-33.jpg");

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                mCheck=false;
                initTime();
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}