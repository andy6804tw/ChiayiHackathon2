package com.openweather.airnews.Fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openweather.airnews.Adapter.NewsFragmentRVA;
import com.openweather.airnews.LoadingSplash.SplashActivity;
import com.openweather.airnews.R;
import com.wj.refresh.OnRefreshListener;
import com.wj.refresh.PullRefreshLayout;

import org.jsoup.nodes.Document;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    private PullRefreshLayout mRefreshLayout;
    private Document document;

    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView =(RecyclerView) view.findViewById(R.id.recycler_view);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NewsFragmentRVA(getActivity(), SplashActivity.list);
        recyclerView.setAdapter(adapter);

        mRefreshLayout = (PullRefreshLayout) view.findViewById(R.id.refresh_layout);
        mRefreshLayout.setMode(0x1);
        initEvent();
        return view;
    }

    private void initEvent() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onPullDownRefresh() {
                mHandler.sendEmptyMessageDelayed(0, 5000);
            }

            @Override
            public void onPullUpRefresh() {
                mHandler.sendEmptyMessageDelayed(0, 5000);
            }
        });

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mRefreshLayout.onRefreshComplete();
        }
    };

}