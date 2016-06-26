package example.com.rxlearn.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import example.com.rxlearn.R;
import example.com.rxlearn.adapter.WxHotImageAdapter;
import example.com.rxlearn.model.HttpWxHotResult;
import example.com.rxlearn.model.WxNews;
import example.com.rxlearn.network.NetWork;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Nevermore on 16/6/24.
 */
public class GetFragment extends BaseFragment {
    private static final String TAG = "ShowImageFragment";
    @Bind(R.id.btnLoading)
    Button mBtnLoading;
    @Bind(R.id.recycleView)
    RecyclerView mRecycleView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.etNum)
    EditText mEtNum;
    @Bind(R.id.etTitle)
    EditText mEtTitle;
    @Bind(R.id.llHelper)
    LinearLayout mLlHelper;


    private WxHotImageAdapter mAdapter = new WxHotImageAdapter();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_get, null);
        ButterKnife.bind(this, view);


        initViewEvent();
        return view;
    }

    private void initViewEvent() {
        mRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mRecycleView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        mSwipeRefreshLayout.setEnabled(false);

        RxView.clicks(mBtnLoading)  //给mBtnLoading添加点击事件
                .debounce(400, TimeUnit.MILLISECONDS) //400ms防止重复点击
                .observeOn(AndroidSchedulers.mainThread()) //默认在非UI线程，切换到主线程执行Action1的Call方法内容
                .subscribe(new Action1<Void>() { //执行任务
                    @Override
                    public void call(Void aVoid) {
                        unSubscribe();
                        mAdapter.setDatas(null);
                        mSwipeRefreshLayout.setRefreshing(true);
                        getWxHot();
                        mLlHelper.setVisibility(View.GONE);
                    }
                });
    }

    private void getWxHot() {
        Subscriber<HttpWxHotResult<List<WxNews>>> httpResultSubscriber = new Subscriber<HttpWxHotResult<List<WxNews>>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onNext(HttpWxHotResult<List<WxNews>> httpResult) {
                mSwipeRefreshLayout.setRefreshing(false);
                List<WxNews> datas = httpResult.getNewslist();
                for (int i = 0; i < datas.size(); i++) {
                    Log.d(TAG, "onNext: " + datas.get(i).toString());
                }
                mAdapter.setDatas(datas);
            }


        };

        int num = TextUtils.isEmpty(mEtNum.getText()) ? 10 : Integer.valueOf(mEtNum.getText().toString());
        String title = TextUtils.isEmpty(mEtTitle.getText()) ? "欧洲杯" : mEtTitle.getText().toString();
        mSubscription = NetWork.getStoreWxHotApi()
                .searchTitle("wxhot", num, title)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(httpResultSubscriber);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
