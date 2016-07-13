package com.widget.ptr.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.widget.ptr.R;
import com.widget.ptr.builder.PtrMode;

public class RotatePtrLoadingMoreFooterView extends PtrLoadingMoreFooterView {

    private RelativeLayout rootView;
    private TextView refreshText;
    private TextView refreshTime;
    private ImageView image;

    private String refreshing;
    private String loading;
    private String complete;
    private String lastUpdateTime;
    private Matrix imageMatrix;
    private RotateAnimation rotateAnimation;

    private Drawable imageDrawable;
    private LayoutParams layoutParams;


    public RotatePtrLoadingMoreFooterView(Context context, PtrMode mode) {
        super(context, mode);

    }

    @Override
    protected void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.loadinglayout, this, false);
        rootView = (RelativeLayout) view.findViewById(R.id.fl_root);
        refreshText = (TextView) view.findViewById(R.id.tv_refresh);
        refreshTime = (TextView) view.findViewById(R.id.tv_refresh_time);
        image = (ImageView) view.findViewById(R.id.iv_image);
        layoutParams = (LayoutParams) rootView.getLayoutParams();
        refreshing = context.getResources().getString(R.string.refreshing);
        loading = context.getResources().getString(R.string.loading);
        complete = context.getResources().getString(R.string.complete);
        lastUpdateTime = getLastTime();
        if (!TextUtils.isEmpty(lastUpdateTime)) {
            refreshTime.setText(lastUpdateTime);
        }
        imageDrawable = context.getResources().getDrawable(R.mipmap.default_ptr_rotate);
        image.setScaleType(ImageView.ScaleType.MATRIX);
        imageMatrix = new Matrix();
        image.setImageMatrix(imageMatrix);
        rotateAnimation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
        rotateAnimation.setDuration(ROTATION_ANIMATION_DURATION);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        addView(view);
    }

    @Override
    protected void onRefreshImpl() {
        image.setImageDrawable(imageDrawable);
        if (null != image.getAnimation()) {
            image.clearAnimation();
        }
        image.startAnimation(rotateAnimation);

        if (PtrMode.BOTH == mode || PtrMode.TOP == mode) {
            if (null != refreshText) {
                refreshText.setText(refreshing);
            }
            if (null != refreshTime) {
                if (TextUtils.isEmpty(lastUpdateTime)) {
                    refreshTime.setVisibility(View.GONE);
                } else {
                    refreshTime.setVisibility(View.VISIBLE);
                }
            }
        } else {
            if (null != refreshText) {
                refreshText.setText(loading);
            }
        }
    }

    @Override
    protected void onResetImpl() {
        if (null != refreshText) {
            refreshText.setText(complete);
        }
        image.setImageDrawable(getResources().getDrawable(R.mipmap.refresh_complete));
        image.setVisibility(View.VISIBLE);

        image.clearAnimation();
        resetImageRotation();

        refreshTime.setVisibility(GONE);
    }

    public final void setHeight(int height) {
        layoutParams.height = height;
        requestLayout();
    }

    public final void setWidth(int width) {
        layoutParams.width = width;
        requestLayout();
    }

    public final int getContentSize() {
        return rootView.getHeight();
    }

    public void setLayoutPadding(int left, int top, int right, int bottom) {
        layoutParams.setMargins(left, top, right, bottom);
        setLayoutParams(layoutParams);
    }

    private void resetImageRotation() {
        if (null != imageMatrix) {
            imageMatrix.reset();
            image.setImageMatrix(imageMatrix);
        }
    }

    private String getLastTime() {
        SharedPreferences sp = context.getSharedPreferences("RefreshRecycleView", Activity.MODE_PRIVATE);
        String lastUpdateTime = sp.getString("LastUpdateTime", "");
        return lastUpdateTime;
    }
}
