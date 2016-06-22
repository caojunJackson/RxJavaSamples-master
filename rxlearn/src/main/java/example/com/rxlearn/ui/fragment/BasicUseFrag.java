package example.com.rxlearn.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import example.com.rxlearn.App;
import example.com.rxlearn.R;
import rx.Observable;
import rx.Observer;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.functions.Action0;
import rx.internal.operators.OnSubscribeSingle;
import rx.schedulers.Schedulers;
import rx.subjects.AsyncSubject;
import rx.subjects.BehaviorSubject;
import rx.subjects.Subject;

/**
 * Created by Administrator on 2016/6/22.
 */
public class BasicUseFrag extends BaseFragment {


    @Bind(R.id.tvShowInfo)
    TextView mTvShowInfo;
    @Bind(R.id.btnSend)
    Button mBtnSend;
    @Bind(R.id.btnSingle)
    Button btnSingle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_baisc_use, null);
        ButterKnife.bind(this, view);
        testRx();
        return view;
    }

    private void testRx() {

        final Observable observable = Observable.create(mStringOnSubscribe);
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                observable.subscribe(mToastObserver);
                observable.subscribe(mTextShowObserver);
            }
        });

        btnSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Single.create(new Single.OnSubscribe<String>() {
                    @Override
                    public void call(SingleSubscriber<? super String> singleSubscriber) {
                        singleSubscriber.onSuccess("hello RxJava Single!");
                    }
                })
                        .subscribeOn()
                        .subscribe(new SingleSubscriber<String>() {
                    @Override
                    public void onSuccess(String value) {
                        Toast.makeText(App.getContext(), value, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable error) {
                    }
                });
            }
        });
        Schedulers.newThread().createWorker().schedule(new Action0() {
            @Override
            public void call() {

            }
        });
    }


    Observable.OnSubscribe<String> mStringOnSubscribe = new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> subscriber) {
            subscriber.onNext(helloRxJava());
            subscriber.onNext("2");
            subscriber.onNext("3");
            subscriber.onCompleted();
        }
    };
    Observer<String> mTextShowObserver = new Observer<String>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onNext(String s) {
            mTvShowInfo.setText(s);
        }
    };
    Observer<String> mToastObserver = new Observer<String>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(String s) {
            Toast.makeText(App.getContext(), s, Toast.LENGTH_SHORT).show();
        }
    };

    private String helloRxJava() {
        return "Hello RxJava!";
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_basic_use;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_basic_use;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
