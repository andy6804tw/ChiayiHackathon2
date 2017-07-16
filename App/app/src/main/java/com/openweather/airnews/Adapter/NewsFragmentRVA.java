package com.openweather.airnews.Adapter;

/**
 * Created by andy6804tw on 2017/7/3.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.openweather.airnews.DataModel.DataModel;
import com.openweather.airnews.R;
import com.wang.avi.AVLoadingIndicatorView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class NewsFragmentRVA extends RecyclerView.Adapter<NewsFragmentRVA.ViewHolder> {


    private final Context mContext;
    public ArrayList<DataModel> list;
    private int page=1;

    public NewsFragmentRVA(Context context, ArrayList<DataModel> list) {
        this.mContext = context;
        this.list=list;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public int currentItem;
        private CardView card_view;
        public ImageView itemImage;
        public TextView itemTitle;
        public TextView itemDetail;
        public TextView itemTime;
        //Footer
        public TextView tvFooter;
        private AVLoadingIndicatorView avi;
        public LinearLayout footerLinearLayout;

        public ViewHolder(View itemView,int viewType) {
            super(itemView);
            if(viewType!=list.size()){
                card_view=(CardView)itemView.findViewById(R.id.card_view);
                itemImage = (ImageView)itemView.findViewById(R.id.item_image);
                itemTitle = (TextView)itemView.findViewById(R.id.item_title);
                itemDetail =(TextView)itemView.findViewById(R.id.item_detail);
                itemTime =(TextView)itemView.findViewById(R.id.itemTime);
            }
            else {
                tvFooter = (TextView) itemView.findViewById(R.id.tvFooter);
                footerLinearLayout=(LinearLayout)itemView.findViewById(R.id.footerLinearLayout);
                avi= (AVLoadingIndicatorView)itemView.findViewById(R.id.avi);
                avi.setIndicator("LineSpinFadeLoaderIndicator");
                avi.hide();
            }

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();

                    Snackbar.make(v, "Click detected on item " + SplashActivity.list.size()+"  "+list.size()+" "+position,
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
            });*/

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == list.size()) {
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.footer_layout, parent, false),viewType);
        }else
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.card_layout, parent, false),viewType);


    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        //viewHolder.itemTitle.setText(titles[i]);
        //viewHolder.itemDetail.setText(details[i]);
        //viewHolder.itemImage.setImageResource(images[i]);
        if(position!=list.size()){
            if(!list.get(position).getImage().contains("logo.png")){
                new DownloadImageTask (viewHolder.itemImage).execute(list.get(position).getImage());
            }else{
                viewHolder.itemImage.setImageResource(R.mipmap.default_logo);
            }
            viewHolder.itemTitle.setText(list.get(position).getTitle());
            viewHolder.itemTime.setText(list.get(position).getTime());
            viewHolder.itemDetail.setText(list.get(position).getDetail());
            viewHolder.card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(position).getUrl())));
                }
            });
        }else{

           /* addData();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    notifyItemRemoved(10*page++);
                }
            }, 1500);*/
                viewHolder.footerLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addData();
                    viewHolder.avi.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            notifyItemRemoved(10*page+++1);
                            viewHolder.avi.hide();
                        }
                    }, 2500);
                   // notifyDataSetChanged();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size()+1;
    }
    @Override
    public int getItemViewType(int position) {return  position;}

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
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
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
                        list.add(new DataModel(title.text(),time.text(),detail.text().substring(0,25)+"...",Image.attr("abs:src"),url.attr("abs:href")));
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

}