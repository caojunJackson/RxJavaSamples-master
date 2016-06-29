package example.com.rxlearn.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.rxbinding.view.RxView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import example.com.rxlearn.App;
import example.com.rxlearn.R;
import example.com.rxlearn.model.HttpUploadResult;
import example.com.rxlearn.model.SearchImage;
import example.com.rxlearn.network.NetWork;
import example.com.rxlearn.network.api.DownloadApi;
import example.com.rxlearn.network.api.SearchApi;
import example.com.rxlearn.network.api.UploadApi;
import example.com.rxlearn.utils.Encode;
import example.com.rxlearn.utils.FileUtils;
import example.com.rxlearn.utils.helper.ProcessListener;
import example.com.rxlearn.utils.helper.ProcessRequestBody;
import example.com.rxlearn.utils.helper.ProcessResponseBody;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
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
    @Bind(R.id.btnUpload)
    Button mBtnUpload;
    @Bind(R.id.btnDownload)
    Button mBtnDownload;
    @Bind(R.id.tvShowUpload)
    TextView mTvShowUpload;
    @Bind(R.id.llContent)
    LinearLayout mLlContent;
    private List<SearchImage> mDatas = new ArrayList<>();
    private ProgressDialog mDialog;
    private File mFile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_build_retrofit, null);
        ButterKnife.bind(this, view);
        initDialog();
        initView();
        return view;
    }

    private void initDialog() {
        mDialog = new ProgressDialog(getActivity());
        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDialog.setMax(100);
    }

    private void initView() {
        initBtnBasic();
        initTrans();
        initAdapt();
        initLog();
        initUpload();
        initDownload();
    }

    private void initDownload() {
        final OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public okhttp3.Response intercept(Chain chain) throws IOException {
                                ResponseBody responseBody = new ProcessResponseBody(chain.proceed(chain.request()).body(),
                                        new ProcessListener() {
                                            @Override
                                            public void onProcess(final long current, final long total, final boolean isCompleted) {
                                                Log.d(TAG, "onProcess: " + current + "/" + total + ",isCompleted:" + isCompleted);
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        mDialog.show();
                                                        int rate = (int) (current * 100 / total);
                                                        mDialog.setProgress(rate);
                                                        if (isCompleted) {
                                                            mDialog.dismiss();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                return chain.proceed(chain.request()).newBuilder().body(responseBody).build();
                            }
                        }
                )
                .build();
        final DownloadApi downloadApi = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl("http://testfileservice.4sonline.net:88/fileService/weixin/")
                .build()
                .create(DownloadApi.class);
        RxView.clicks(mBtnDownload)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mDialog.setMessage("数据下载中...");
                        mDialog.setTitle("下载");
                        Call<ResponseBody> call = downloadApi.download();
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                try {
                                    InputStream is = response.body().byteStream();
                                    File file = new File(Environment.getExternalStorageDirectory(), "my.jpg");
                                    FileOutputStream fos = new FileOutputStream(file);
                                    BufferedInputStream bis = new BufferedInputStream(is);
                                    byte[] buffer = new byte[1024];
                                    int len;
                                    while ((len = bis.read(buffer)) != -1) {
                                        fos.write(buffer, 0, len);
                                        fos.flush();
                                    }
                                    fos.close();
                                    bis.close();
                                    is.close();
                                    Log.d(TAG, "onResponse: " + file.getAbsolutePath());
                                    Glide.with(getActivity())
                                            .load(file)
                                            .asBitmap()
                                            .into(new SimpleTarget<Bitmap>() {
                                                @Override
                                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                                    mLlContent.setBackground(new BitmapDrawable(resource));
                                                }
                                            });
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

    private void initUpload() {

        final OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public okhttp3.Response intercept(Chain chain) throws IOException {
                                RequestBody requestBody = new ProcessRequestBody(chain.request().body(), new ProcessListener() {
                                    @Override
                                    public void onProcess(final long current, final long total, final boolean isCompleted) {
//                                        Log.d(TAG, "onProcess: " + current + "/" + total + ",isCompleted:" + isCompleted);
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mDialog.show();
                                                int rate = (int) (current * 100 / total);
                                                mDialog.setProgress(rate);
                                                if (isCompleted) {
                                                    mDialog.dismiss();
                                                }
                                            }
                                        });
                                    }
                                });
                                return chain.proceed(chain.request()
                                        .newBuilder()
                                        .post(requestBody)
                                        .build());
                            }
                        })
                .build();


        RxView.clicks(mBtnUpload)
                .subscribe(new Action1<Void>() {
                               @Override
                               public void call(Void aVoid) {
                                   mDialog.setMessage("数据上传中...");
                                   mDialog.setTitle("上传");
                                   if (mFile == null || !mFile.isFile()) {
                                       Toast.makeText(App.getContext(), "请先选择要上传的文件", Toast.LENGTH_SHORT).show();
                                       Intent intent = new Intent();
                                       intent.setType("image/*");
                                       intent.setAction(Intent.ACTION_GET_CONTENT);
                                       startActivityForResult(intent, 1);
                                       return;
                                   }
                                   String sign = Encode.getMD5Str("4003abcdefg", 0, 32);
                                   MultipartBody multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                           // TODO: 2016/6/29   删除一些参数  这个服务器不能乱传数据  等我找个可以玩的服务器再回归
                                           .addFormDataPart("f", "1.jpg", RequestBody.create(UploadApi.MEDIA_TYPE_JPG, mFile))
                                           .build();

                                   mSubscription = new Retrofit.Builder()
                                           .client(httpClient)
                                           .baseUrl("http://testfileservice.4sonline.net:88/")
                                           .addConverterFactory(GsonConverterFactory.create())
                                           .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                                           .build()
                                           .create(UploadApi.class)
                                           .upload(multipartBody)
                                           .subscribeOn(Schedulers.io())
                                           .observeOn(AndroidSchedulers.mainThread())
                                           .subscribe(new Observer<HttpUploadResult>() {
                                               @Override
                                               public void onCompleted() {
                                                   Log.d(TAG, "onCompleted: ");
                                               }

                                               @Override
                                               public void onError(Throwable e) {
                                                   Log.d(TAG, "onError: " + e.getMessage());
                                               }

                                               @Override
                                               public void onNext(HttpUploadResult httpUploadResult) {
                                                   Log.d(TAG, "onNext: " + httpUploadResult.toString());
                                                   mTvShowUpload.setText(httpUploadResult.getUrl());
                                                   Glide.with(getActivity())
                                                           .load(httpUploadResult.getUrl())
                                                           .asBitmap()
                                                           .into(new SimpleTarget<Bitmap>() {
                                                               @Override
                                                               public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                                                   mLlContent.setBackground(new BitmapDrawable(resource));
                                                               }
                                                           });
                                               }
                                           });
                               }
                           }

                );
    }

    private void initLog() {
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            /**
             * 当选择的图片不为空的话，在获取到图片的途径
             */
            Uri uri = data.getData();
            mFile = FileUtils.getFile(getActivity(), uri);
            Log.d(TAG, "onActivityResult: path=" + mFile.getAbsolutePath());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
