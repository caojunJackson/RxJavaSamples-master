package com.example.oldjava;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2016/6/22.
 */
public class App extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }
}
