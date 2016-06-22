package example.com.rxlearn.ui.fragment;

import android.app.AlertDialog;
import android.app.Fragment;

import butterknife.OnClick;
import example.com.rxlearn.R;
import rx.Subscription;

/**
 * Created by Administrator on 2016/6/22.
 */
public abstract class BaseFragment extends Fragment {
    protected Subscription subscription;

    @OnClick(R.id.btnTips)
    void tips() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getTitleRes())
                .setView(getActivity().getLayoutInflater().inflate(getDialogRes(), null))
                .show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    protected abstract int getDialogRes();

    protected abstract int getTitleRes();
}
