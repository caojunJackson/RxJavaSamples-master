package example.com.rxlearn.ui.fragment;

import android.app.AlertDialog;
import android.app.Fragment;

import butterknife.OnClick;
import example.com.rxlearn.R;
import okhttp3.Call;
import rx.Subscription;

/**
 * Created by Administrator on 2016/6/22.
 */
public abstract class BaseFragment extends Fragment {
    protected Subscription mSubscription;


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unSubscribe();

    }

    protected void unSubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

}
