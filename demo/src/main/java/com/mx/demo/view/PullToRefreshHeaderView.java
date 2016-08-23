package com.mx.demo.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.LoadingLayoutBase;
import com.mx.demo.R;

/**
 * Created by liuyuxuan on 16/8/9.
 */
public class PullToRefreshHeaderView extends LoadingLayoutBase {


    private final int ROTATE_ANIM_DURATION = 180;
    private LinearLayout container;
    private ImageView arrowImageView;
    private ImageView progressImageView;
    private TextView hintTextView;
    private Animation rotateUpAnim;
    private Animation rotateDownAnim;

    public PullToRefreshHeaderView(Context context) {
        super(context);
        initView(context);
    }

    public PullToRefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PullToRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        container = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.pullrefresh_listview_header, null);
        addView(container);
        arrowImageView = (ImageView) findViewById(R.id.pullrefresh_header_arrow);
        hintTextView = (TextView) findViewById(R.id.pullrefresh_header_hint_textview);
        progressImageView = (ImageView) findViewById(R.id.pullrefresh_header_progressbar);
        rotateUpAnim = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        rotateUpAnim.setFillAfter(true);
        rotateDownAnim = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        rotateUpAnim.setFillAfter(true);
    }

    @Override
    public int getContentSize() {
        return (int) (60 * getResources().getDisplayMetrics().density);
    }

    @Override
    public void pullToRefresh() {
        progressImageView.setVisibility(GONE);
        arrowImageView.setVisibility(VISIBLE);
        hintTextView.setVisibility(VISIBLE);
        if (arrowImageView != null) {
            arrowImageView.clearAnimation();
            arrowImageView.startAnimation(rotateDownAnim);
        }
        hintTextView.setText(R.string.xlistview_header_hint_normal);
    }

    @Override
    public void releaseToRefresh() {
        progressImageView.setVisibility(GONE);
        arrowImageView.setVisibility(VISIBLE);
        hintTextView.setVisibility(VISIBLE);
        if (arrowImageView != null) {
            arrowImageView.clearAnimation();
            arrowImageView.startAnimation(rotateUpAnim);
        }
        hintTextView.setText(R.string.xlistview_header_hint_ready);
    }

    @Override
    public void onPull(float scaleOfLayout) {

    }

    @Override
    public void refreshing() {
        Log.d("header", "refreshing: ");
        arrowImageView.clearAnimation();
        arrowImageView.setVisibility(View.GONE);
        progressImageView.setVisibility(VISIBLE);
        AnimationDrawable drawable = (AnimationDrawable) progressImageView.getDrawable();
        drawable.start();
        hintTextView.setText(R.string.xlistview_header_hint_loading);
    }

    @Override
    public void reset() {
        arrowImageView.clearAnimation();
        Log.d("header", "reset: ");
    }


    @Override
    public void setPullLabel(CharSequence pullLabel) {

    }

    @Override
    public void setRefreshingLabel(CharSequence refreshingLabel) {

    }

    @Override
    public void setReleaseLabel(CharSequence releaseLabel) {

    }
}
