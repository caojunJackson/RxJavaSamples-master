package example.com.rxlearn.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding.view.RxView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import example.com.rxlearn.R;
import example.com.rxlearn.model.HttpKeyResult;
import example.com.rxlearn.model.HttpWxHotResult;
import example.com.rxlearn.model.KeyEx;
import example.com.rxlearn.model.WxNews;
import example.com.rxlearn.network.NetWork;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Nevermore on 16/6/24.
 */
public class PostFragment extends BaseFragment {
    private static final String TAG = "PostFragment";
    @Bind(R.id.etNum)
    EditText mEtNum;
    @Bind(R.id.etText)
    EditText mEtText;
    @Bind(R.id.btnLoading)
    Button mBtnLoading;
    @Bind(R.id.llHelper)
    LinearLayout mLlHelper;
    @Bind(R.id.tvShowInfo)
    AppCompatTextView mTvShowInfo;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, null);
        ButterKnife.bind(this, view);


        initViewEvent();
        return view;
    }

    private void initViewEvent() {
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        mSwipeRefreshLayout.setEnabled(false);

        RxView.clicks(mBtnLoading)  //给mBtnLoading添加点击事件
                .debounce(400, TimeUnit.MILLISECONDS) //400ms防止重复点击
                .observeOn(AndroidSchedulers.mainThread()) //默认在非UI线程，切换到主线程执行Action1的Call方法内容
                .subscribe(new Action1<Void>() { //执行任务
                    @Override
                    public void call(Void aVoid) {
                        unSubscribe();
                        mTvShowInfo.setText("");
                        mSwipeRefreshLayout.setRefreshing(true);
                        getExText();
                    }
                });
    }

    private void getExText() {
        Subscriber<HttpKeyResult<KeyEx>> httpKeyResultSubscriber = new Subscriber<HttpKeyResult<KeyEx>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                mSwipeRefreshLayout.setRefreshing(false);
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(HttpKeyResult<KeyEx> keyExHttpKeyResult) {
                mSwipeRefreshLayout.setRefreshing(false);
                Log.d(TAG, "onNext: " + keyExHttpKeyResult.toString());
                List<String> list = keyExHttpKeyResult.getT().getList();
                for (int i = 0; i < list.size(); i++) {
                    String s = list.get(i) + "\n";
                    mTvShowInfo.setText(mTvShowInfo.getText() + s);
                }

            }
        };
        int num = TextUtils.isEmpty(mEtNum.getText()) ? 10 : Integer.valueOf(mEtNum.getText().toString());
        String text = TextUtils.isEmpty(mEtText.getText()) ? "这就是传说中Retrofit的Post用法" : mEtText.getText().toString();
        Map<String, String> map = new HashMap<>();
        map.put("num", num + "");
        map.put("text", text);
        mSubscription = NetWork.getStoreKeyExApi()
                .exKey(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(httpKeyResultSubscriber);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
