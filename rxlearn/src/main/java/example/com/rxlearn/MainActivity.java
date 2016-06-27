package example.com.rxlearn;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import butterknife.ButterKnife;
import example.com.rxlearn.ui.fragment.BasicUseFrag;
import example.com.rxlearn.ui.fragment.GetFragment;
import example.com.rxlearn.ui.fragment.PostFragment;
import example.com.rxlearn.ui.fragment.ShowImageFragment;
import example.com.rxlearn.ui.fragment.UploadFragment;

public class MainActivity extends AppCompatActivity {
    public final static String TAG = "MainActivity";
    @Bind(R.id.toolBar)
    Toolbar toolBar;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.viewPager)
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolBar);
        viewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public int getCount() {
                return 6;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new BasicUseFrag();
                    case 1:
                        return new ShowImageFragment();
                    case 2:
                        return new GetFragment();
                    case 3:
                        return new PostFragment();
                    case 4:
                        return new UploadFragment();
                    case 5:
                        return new UploadFragment();
                    default:
                        return new BasicUseFrag();
                }
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return getString(R.string.title_basic_use);
                    case 1:
                        return getString(R.string.title_niubility);
                    case 2:
                        return "Get用法";
                    case 3:
                        return "Post用法";
                    case 4:
                        return "上传文件";
                    case 6:
                        return "下载文件";
                    default:
                        return getString(R.string.title_basic_use);
                }
            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }


}

