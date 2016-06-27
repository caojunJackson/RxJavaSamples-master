package com.example.oldjava.ui.fragment;

import android.app.Fragment;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/6/22.
 */
public abstract class BaseFragment extends Fragment {

    protected Call call;


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
    }

}
