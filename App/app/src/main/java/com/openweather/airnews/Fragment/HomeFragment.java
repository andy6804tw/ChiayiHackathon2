package com.openweather.airnews.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openweather.airnews.Adapter.HomeFragmentRVA;
import com.openweather.airnews.R;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private String mDate="";
    private Boolean mCheck=true;
    View mView;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_home, container, false);
        mView=view;

        recyclerView =(RecyclerView) view.findViewById(R.id.HomeRecyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HomeFragmentRVA(getActivity());
        recyclerView.setAdapter(adapter);

        initTime();

        return view;
    }

    private void initTime() {

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        //年
        mDate=year+"";
        //月份
        if(Integer.toString((month+1)).length()==1)
            mDate+="0"+(month+1);
        else
            mDate+=(month+1);
        //日期(先判斷是否00)
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


    }

//    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//        ImageView bmImage;
//
//        public DownloadImageTask(ImageView bmImage) {
//            this.bmImage = bmImage;
//        }
//
//        protected Bitmap doInBackground(String... urls) {
//            String urldisplay = urls[0];
//            Bitmap mIcon11 = null;
//            try {
//                InputStream in = new java.net.URL(urldisplay).openStream();
//                mIcon11 = BitmapFactory.decodeStream(in);
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                mCheck=false;
//                initTime();
//                e.printStackTrace();
//            }
//            return mIcon11;
//        }
//
//        protected void onPostExecute(Bitmap result) {
//            bmImage.setImageBitmap(result);
//        }
//    }

}
