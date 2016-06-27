package com.example.oldjava.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.oldjava.App;
import com.example.oldjava.R;
import com.example.oldjava.SearchImageAdapter;
import com.example.oldjava.model.SearchImage;
import com.example.oldjava.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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


    private OkHttpClient mOkHttpClient = new OkHttpClient();
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpeg");
    private File mFile;
    private static String sign = getMD5Str("4003abcdefg", 0, 32);
    public final static String API_SERVER_FILE = "http://testfileservice.4sonline.net:88/";// 图片服务器测试路径
    public final static String UPLOAD = API_SERVER_FILE
            + "fileService/upload.action";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload, null);
        ButterKnife.bind(this, view);
        initViewEvent();
        return view;
    }

    private void initViewEvent() {
        mBtnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2016/6/27
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });
        mBtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2016/6/27
                MultipartBody.Builder mBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                mBuilder.addFormDataPart("sign", sign);
                mBuilder.addFormDataPart("sessionToken", "");
                mBuilder.addFormDataPart("fid", "t_pic");
                mBuilder.addFormDataPart("uid", "4003");
                mBuilder.addFormDataPart("f", "1.jpg", RequestBody.create(MEDIA_TYPE_JPG, mFile));
                mBuilder.build();
                Request request = new Request.Builder().url(UPLOAD)
                        .post(mBuilder.build())
                        .build();

                call = mOkHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d(TAG, "onResponse: " + response.toString());
                        Log.d(TAG, "onResponse: " + response.body().string());
                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            /**
             * 当选择的图片不为空的话，在获取到图片的途径
             */
            Uri uri = data.getData();
            Log.d(TAG, "uri = " + uri);
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

    public static String getMD5Str(String str, int offset, int len) {
        MessageDigest messageDigest = null;
        if (str == null)
            return "";

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] bytes = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            if (Integer.toHexString(0xFF & bytes[i]).length() == 1)
                md5StrBuff.append("0").append(
                        Integer.toHexString(0xFF & bytes[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & bytes[i]));
        }

        return md5StrBuff.substring(offset, len).toString();
    }
}
