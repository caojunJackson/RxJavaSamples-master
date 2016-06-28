package example.com.rxlearn.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.rxbinding.view.RxView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import example.com.rxlearn.R;
import example.com.rxlearn.model.SearchImage;
import example.com.rxlearn.network.api.SearchApi;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/6/22.
 */
public class BuildRetrofitFrag extends BaseFragment {


    @Bind(R.id.btnBasic)
    Button mBtnBasic;
    @Bind(R.id.btnTrans)
    Button mBtnTrans;
    @Bind(R.id.btnAdapter)
    Button mBtnAdapter;
    @Bind(R.id.btnLog)
    Button mBtnLog;
    private List<SearchImage> mDatas = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_build_retrofit, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        initBtnBasic();
        initTrans();
        initAdapt();
        initLog();
        initProcess();
    }

    private void initProcess() {
//        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
//            @Override
//            public okhttp3.Response intercept(Chain chain) throws IOException {
//                okhttp3.Response response = chain.proceed(chain.request());
//            }
//        }).build();
    }

    private void initLog() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS);
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.v("Retrofit", message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(logger).build();
        final Retrofit build = new Retrofit.Builder()
                .baseUrl("http://zhuangbi.info/")
                .addConverterFactory(GsonConverterFactory.create())//添加转换器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//添加适配器 连接RxJava
                .client(httpClient)
                .build();
        RxView.clicks(mBtnLog)
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                               @Override
                               public void call(Void aVoid) {
                                   //不需要Call了，直接使用Observable，顺滑的切换到RxJava领域
                                   Observable<List<SearchImage>> observable = build.create(SearchApi.class)
                                           .search("牛逼")
                                           .subscribeOn(Schedulers.io())
                                           .observeOn(AndroidSchedulers.mainThread());
                                   //这里主要是为了演示，代码简洁，实际中不要这样，
                                   // 还要考虑取消一个请求，返回错误值处理，业务需求等等
                                   observable.subscribe(new Action1<List<SearchImage>>() {
                                       @Override
                                       public void call(List<SearchImage> searchImages) {
                                           for (SearchImage searchImage : searchImages) {
                                               Log.d(TAG, "call: " + searchImage.toString());
                                           }
                                       }
                                   });
                               }
                           }
                );
    }

    private void initAdapt() {

        final Retrofit build = new Retrofit.Builder()
                .baseUrl("http://zhuangbi.info/")
                .addConverterFactory(GsonConverterFactory.create())//添加转换器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//添加适配器 连接RxJava
                .build();
        //使用RxBinding处理点击事件 RxBinding基于RxJava实现
        RxView.clicks(mBtnAdapter)
                .debounce(500, TimeUnit.MILLISECONDS) //排除500ms内重复点击事件
                .observeOn(AndroidSchedulers.mainThread())//切换到主线程执行响应内容,相当于在回调方法中执行runOnUiThread方法
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        //不需要Call了，直接使用Observable，顺滑的切换到RxJava领域
                        Observable<List<SearchImage>> observable = build.create(SearchApi.class)
                                .search("牛逼")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                        //这里主要是为了演示，代码简洁，实际中不要这样，
                        // 还要考虑取消一个请求，返回错误值处理，业务需求等等
                        observable.subscribe(new Action1<List<SearchImage>>() {
                            @Override
                            public void call(List<SearchImage> searchImages) {
                                for (SearchImage searchImage : searchImages) {
                                    Log.d(TAG, "call: " + searchImage.toString());
                                }
                            }
                        });
                    }
                });
    }

    private void initTrans() {
        final Retrofit build = new Retrofit.Builder()
                .baseUrl("http://zhuangbi.info/")
                .addConverterFactory(GsonConverterFactory.create())//添加转换器
                .build();

        mBtnTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<List<SearchImage>> call = build.create(SearchApi.class).transSearch("牛逼");
                call.enqueue(new Callback<List<SearchImage>>() {
                    @Override
                    public void onResponse(Call<List<SearchImage>> call, Response<List<SearchImage>> response) {
                        mDatas.clear();
                        //使用转换器，内部已经帮我们实现转换，省去了new Gson().form(jsonStr,new TypeToken<>())的过程
                        mDatas = response.body();
                        for (SearchImage searchImage : mDatas) {
                            Log.d(TAG, "onResponse: " + searchImage.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<SearchImage>> call, Throwable t) {
                        Log.e(TAG, "onFailure: ", t);
                    }
                });
            }
        });


    }


    private void initBtnBasic() {
        mBtnBasic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit build = new Retrofit.Builder().baseUrl("http://zhuangbi.info/").build();
                Call<ResponseBody> call = build.create(SearchApi.class).basicSearch("牛逼");
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String res = response.body().string();
                            mDatas = new Gson().fromJson(res, new TypeToken<List<SearchImage>>() {
                            }.getType());
                            for (SearchImage searchImage : mDatas) {
                                Log.d(TAG, "onResponse: " + searchImage.toString());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e(TAG, "onFailure: ", t);
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
