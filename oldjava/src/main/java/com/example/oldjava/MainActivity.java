package com.example.oldjava;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.oldjava.ui.fragment.ShowImageFragment;
import com.example.oldjava.ui.fragment.UploadFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Bind(R.id.toolBar)
    Toolbar mToolBar;
    @Bind(R.id.tabLayout)
    TabLayout mTabLayout;
    @Bind(R.id.viewPager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolBar);
        mViewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new ShowImageFragment();
                    case 1:
                        return new UploadFragment();
                    default:
                        return new ShowImageFragment();
                }
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return getString(R.string.niubility);
                    case 1:
                        return "上传";
                    default:
                        return getString(R.string.niubility);
                }
            }
        });
        mTabLayout.setupWithViewPager(mViewPager);
    }


}
