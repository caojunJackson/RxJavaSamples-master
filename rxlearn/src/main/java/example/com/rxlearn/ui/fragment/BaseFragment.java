package example.com.rxlearn.ui.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.util.Log;

import butterknife.OnClick;
import example.com.rxlearn.R;
import okhttp3.Call;
import rx.Subscription;

/**
 * Created by Administrator on 2016/6/22.
 */
public abstract class BaseFragment extends Fragment {
    public final static String TAG = "BaseFragment";
    protected volatile Subscription mSubscription;


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unSubscribe();

    }

    protected void unSubscribe() {
        synchronized (Subscription.class) {
            if (mSubscription != null && !mSubscription.isUnsubscribed()) {
                mSubscription.unsubscribe();
            }
        }
    }

}
