package com.example.oldjava.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.oldjava.App;
import com.example.oldjava.R;
import com.example.oldjava.SearchImageAdapter;
import com.example.oldjava.model.SearchImage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Nevermore on 16/6/24.
 */
public class ShowImageFragment extends BaseFragment {
    private static final String TAG = "ShowImageFragment";

    @Bind(R.id.btnLoading)
    Button mBtnLoading;
    @Bind(R.id.recycleView)
    RecyclerView mRecycleView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private OkHttpClient mOkHttpClient = new OkHttpClient();
    private List<SearchImage> mDatas = new ArrayList<>();
    private SearchImageAdapter mAdapter = new SearchImageAdapter();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_image, null);
        ButterKnife.bind(this, view);
        mRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRecycleView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        mSwipeRefreshLayout.setEnabled(false);

        initViewEvent();
        return view;
    }

    private void initViewEvent() {


        mBtnLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                search();
            }
        });
    }

    private void search() {
        mSwipeRefreshLayout.setRefreshing(true);
        Request request = new Request.Builder()
                .url("http://zhuangbi.info/search?q=" + getString(R.string.niubility))
                .build();

        call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(App.getContext(), "", Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resStr = response.body().string();
                Gson gson = new Gson();
                mDatas = gson.fromJson(resStr, new TypeToken<List<SearchImage>>() {
                }.getType());
                for (int i = 0; i < mDatas.size(); i++) {
                    Log.d(TAG, "onResponse: " + mDatas.get(i).toString());
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mAdapter.setDatas(mDatas);
                    }
                });

            }

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
