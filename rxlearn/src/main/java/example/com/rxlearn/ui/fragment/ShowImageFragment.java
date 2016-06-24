package example.com.rxlearn.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import example.com.rxlearn.App;
import example.com.rxlearn.R;
import example.com.rxlearn.adapter.SearchImageAdapter;
import example.com.rxlearn.model.SearchImage;
import example.com.rxlearn.network.NetWork;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
        RxView.clicks(mBtnLoading)  //给mBtnLoading添加点击事件
                .debounce(400, TimeUnit.MILLISECONDS) //400ms防止重复点击
                .observeOn(AndroidSchedulers.mainThread()) //默认在非UI线程，切换到主线程执行Action1的Call方法内容
                .subscribe(new Action1<Void>() { //执行任务
                    @Override
                    public void call(Void aVoid) {
                        unSubscribe();
                        mAdapter.setDatas(null);
                        mSwipeRefreshLayout.setRefreshing(true);
                        search(getString(R.string.niubility));
                    }
                });
    }

    private void search(String title) {

        Subscriber<List<SearchImage>> subscriber = new Subscriber<List<SearchImage>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(App.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(List<SearchImage> searchImages) {
                mSwipeRefreshLayout.setRefreshing(false);
                //显示数据
                mAdapter.setDatas(searchImages);
            }
        };
        mSubscription = NetWork.getSearchApi() //获取SearchApi 具备网络通信能力
                .search(title) //访问特定Api接口
                .subscribeOn(Schedulers.io())//指定在非UI线程访问,执行(被观察）事件
                .observeOn(AndroidSchedulers.mainThread())//指定在UI线程执行观察者(回调)结果
                .subscribe(subscriber);//观察者(回调)执行
    }


    @Override
    protected int getDialogRes() {
        return 0;
    }

    @Override
    protected int getTitleRes() {
        return R.string.niubility;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
