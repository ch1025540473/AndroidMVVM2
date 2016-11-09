package com.mx.demo.view.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.mx.demo.R;
import com.mx.framework2.view.ImageGestureView;
import com.mx.framework2.view.ui.BaseActivity;

/**
 * Created by liuyuxuan on 2016/11/7.
 */

public class MultiImageDetailActivity extends BaseActivity {

    private static final String TAG = "MultiPreviewActivity";

    private ViewPager mViewPager;

    private ImagePageAdapter mAdapter;

    private static final String PIC_DIR = "pictures";

    private String[] mPictures = {
            "http://10.69.213.78:6080/gomeplus_android_mvvm/gomeplus/uploads/7a1070afcbb044a303309502b38da143/ccc.jpg",
            "http://10.69.213.78:6080/gomeplus_android_mvvm/gomeplus/uploads/d35a1a853707d178719cc62c26706aa9/aaa.jpg",
            "http://10.69.213.78:6080/gomeplus_android_mvvm/gomeplus/uploads/40f840371a55d050910527cc633d987b/world.jpg",
            "http://10.69.213.78:6080/gomeplus_android_mvvm/gomeplus/uploads/61dfc1f494e94c451a5b42293aeb5de8/bear.png"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multi_image_detail_activity);
        mViewPager = (ViewPager) findViewById(R.id.vp_pager);
        mAdapter = new ImagePageAdapter();
        mViewPager.setAdapter(mAdapter);
    }

    private class ImagePageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPictures.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageGestureView imageView = new ImageGestureView(container.getContext());
            imageView.setImageUri(Uri.parse(mPictures[position]));
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
