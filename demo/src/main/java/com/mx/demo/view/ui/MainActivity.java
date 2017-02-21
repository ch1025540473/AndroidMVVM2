package com.mx.demo.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.memory.PooledByteBuffer;
import com.mx.demo.R;
import com.mx.framework2.view.DataBindingFactory;
import com.mx.framework2.view.ui.BaseActivity;
import com.orhanobut.logger.Logger;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends BaseActivity {
    List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragments= new LinkedList<>();
        fragments.add(new MainFragment());
        fragments.add(new MainFragment());

        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_pager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return 2;
            }
        });

    }

}
