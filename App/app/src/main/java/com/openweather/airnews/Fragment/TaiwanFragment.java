package com.openweather.airnews.Fragment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.openweather.airnews.R;

import java.io.InputStream;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaiwanFragment extends Fragment {


    private String mDate="";
    private Boolean mCheck=true;
    private int mIndex=0;
    private View mView;
    private ImageView imageView;
    private RelativeLayout mapRelativeLayout;
    private TextView tvtvStatus1,tvtvStatus2;
    private MaterialSpinner spinnerView;

    public TaiwanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_taiwan, container, false);
        mView=view;
        imageView=(ImageView)view.findViewById(R.id.imageView);
        mapRelativeLayout=(RelativeLayout)view.findViewById(R.id.mapRelativeLayout);
        tvtvStatus1=(TextView)view.findViewById(R.id.tvtvStatus1);
        tvtvStatus2=(TextView)view.findViewById(R.id.tvtvStatus2);
        spinnerView= (MaterialSpinner) view.findViewById(R.id.spinnerView);

        //init();
        initTime();
        initSpanner();

        return view;
    }

    private void initSpanner() {

        spinnerView.setItems("全台", "北部空品區", "竹苗空品區", "中部空品區", "雲嘉南空品區","高屏空品區","宜蘭空品區","花蓮空品區");
        spinnerView.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                switch (position){
                    case 0:
                        mIndex=0;
                        initTime();
                        break;
                    case 1:
                        mIndex=1;
                        initTime();
                        break;
                    case 2:
                        mIndex=3;
                        initTime();
                        break;
                    case 3:
                        mIndex=4;
                        initTime();
                        break;
                    case 4:
                        mIndex=6;
                        initTime();
                        break;
                    case 5:
                        mIndex=7;
                        initTime();
                        break;
                    case 6:
                        mIndex=8;
                        initTime();
                        break;
                    case 7:
                        mIndex=9;
                        initTime();
                        break;
                    default:
                        break;
                }
            }
        });
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
        Log.e("time",mDate);
        //載入圖片
        DownloadImageTask downloadImageTask=new DownloadImageTask(imageView);
        downloadImageTask.execute("http://taqm.epa.gov.tw/taqm/map_Contour/"+mDate+"-"+mIndex+"-33.jpg");

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
