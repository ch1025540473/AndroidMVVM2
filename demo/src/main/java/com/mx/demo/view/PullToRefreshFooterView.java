package com.mx.demo.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mx.demo.R;
import com.mx.engine.utils.ScreenUtils;
import com.mx.framework2.view.FooterLoadingView;

/**
 * Created by liuyuxuan on 16/8/9.
 */
public class PullToRefreshFooterView extends FooterLoadingView {

    public static String getClassName() {
        return PullToRefreshFooterView.class.getName();
    }

    private View layout;
    private ImageView progressImageView;
    private TextView hintView;
    boolean isLoading = false;

    @Override
    public void onLoading(boolean isLoading) {
        if (this.isLoading == isLoading) {
            return;
        }
        this.isLoading = isLoading;
        if (isLoading) {
            loading();
        } else {
            completed();
        }
    }


    public PullToRefreshFooterView(Context context) {
        super(context);
        initView(context);
    }

    public PullToRefreshFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PullToRefreshFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        layout = LayoutInflater.from(context).inflate(R.layout.pullrefresh_listview_footer, null);
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        addView(layout);
        progressImageView = (ImageView) layout.findViewById(R.id.pullrefresh_footer_progressbar);
        hintView = (TextView) layout.findViewById(R.id.pullrefresh_footer_hint_textview);
        ViewGroup.LayoutParams v = layout.getLayoutParams();
        v.height = ScreenUtils.dp2PxInt(getContext(), 0);
        layout.setLayoutParams(v);
    }


    private void completed() {
        hintView.setVisibility(View.VISIBLE);
        progressImageView.setVisibility(View.GONE);
        AnimationDrawable drawable = (AnimationDrawable) progressImageView.getDrawable();
        drawable.stop();
        ViewGroup.LayoutParams v = layout.getLayoutParams();
        v.height = ScreenUtils.dp2PxInt(getContext(), 0);
        layout.setLayoutParams(v);
    }


    private void loading() {
        ViewGroup.LayoutParams v = layout.getLayoutParams();
        v.height = ScreenUtils.dp2PxInt(getContext(), 40);
        layout.setLayoutParams(v);
        hintView.setVisibility(View.GONE);
        progressImageView.setVisibility(View.VISIBLE);
        AnimationDrawable drawable = (AnimationDrawable) progressImageView.getDrawable();
        drawable.start();
        layout.requestLayout();
        layout.invalidate();
    }


}
