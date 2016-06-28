package example.com.rxlearn.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;

import java.io.File;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import example.com.rxlearn.App;
import example.com.rxlearn.R;
import example.com.rxlearn.model.HttpUploadResult;
import example.com.rxlearn.network.NetWork;
import example.com.rxlearn.network.api.UploadApi;
import example.com.rxlearn.utils.Encode;
import example.com.rxlearn.utils.FileUtils;
import okhttp3.Call;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Nevermore on 16/6/24.
 */
public class UploadFragment extends BaseFragment {
    private static final String TAG = "UploadFragment";
    @Bind(R.id.btnSelect)
    Button mBtnSelect;
    @Bind(R.id.btnUpload)
    Button mBtnUpload;
    @Bind(R.id.ivShow)
    ImageView mIvShow;
    @Bind(R.id.btnCancel)
    Button mBtnCancel;
    @Bind(R.id.ivUploadShow)
    ImageView mIvUploadShow;


    private OkHttpClient mOkHttpClient = new OkHttpClient();

    private File mFile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload, null);
        ButterKnife.bind(this, view);
        initViewEvent();
        return view;
    }

    private void initViewEvent() {
        RxView.clicks(mBtnSelect)
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, 1);
                    }
                });
        RxView.clicks(mBtnUpload)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        OkHttpClient httpClient = new OkHttpClient();
                        if (mFile == null || !mFile.isFile()) {
                            Log.d(TAG, "call: return");
                            Toast.makeText(App.getContext(), "请先选择要上传的文件", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String sign = Encode.getMD5Str("4003abcdefg", 0, 32);
                        MultipartBody multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                .addFormDataPart("sign", sign)
                                .addFormDataPart("sessionToken", "")
                                .addFormDataPart("fid", "t_pic")
                                .addFormDataPart("uid", "4003")
                                .addFormDataPart("f", "1.jpg", RequestBody.create(UploadApi.MEDIA_TYPE_JPG, mFile))
                                .build();

                        mSubscription = NetWork.getUploadApi()
                                .upload(multipartBody)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(httpUploadResultObserver);
                    }
                });

        RxView.clicks(mBtnCancel)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        unSubscribe();
                        Toast.makeText(App.getContext(), "已取消上传", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    Observer<HttpUploadResult> httpUploadResultObserver = new Observer<HttpUploadResult>() {
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
            Glide.with(getActivity()).load(httpUploadResult.getUrl()).into(mIvUploadShow);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            /**
             * 当选择的图片不为空的话，在获取到图片的途径
             */
            Uri uri = data.getData();
            mFile = FileUtils.getFile(getActivity(), uri);
            Log.d(TAG, "onActivityResult: path=" + mFile.getAbsolutePath());
            Glide.with(getActivity()).load(mFile).into(mIvShow);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
