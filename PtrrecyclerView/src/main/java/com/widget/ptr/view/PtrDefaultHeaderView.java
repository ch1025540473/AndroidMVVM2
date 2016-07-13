package com.widget.ptr.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.widget.ptr.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

public class PtrDefaultHeaderView extends PtrPullHeaderView {

    private final static String KEY_SharedPreferences = "cube_ptr_classic_last_update";
    private static SimpleDateFormat sDataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private int rotateAniTime = 150;
    private RotateAnimation flipAnimation;
    private RotateAnimation reverseFlipAnimation;
    private TextView titleTextView;
    private View rotateView;
    private View progressBar;
    private long lastUpdateTime = -1;
    private TextView lastUpdateTextView;
    private String lastUpdateTimeKey;
    private boolean shouldShowLastUpdate;


    public PtrDefaultHeaderView(Context context) {
        super(context);
        initViews(null);
    }

    public PtrDefaultHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(attrs);
    }

    public PtrDefaultHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(attrs);
    }

    protected void initViews(AttributeSet attrs) {
        TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.PtrClassicHeader, 0, 0);
        if (arr != null) {
            rotateAniTime = arr.getInt(R.styleable.PtrClassicHeader_ptr_rotate_ani_time, rotateAniTime);
        }
        buildAnimation();
        View header = LayoutInflater.from(getContext()).inflate(R.layout.header_refresh, this);

        rotateView = header.findViewById(R.id.ptr_classic_header_rotate_view);

        titleTextView = (TextView) header.findViewById(R.id.ptr_classic_header_rotate_view_header_title);
        lastUpdateTextView = (TextView) header.findViewById(R.id.ptr_classic_header_rotate_view_header_last_update);
        progressBar = header.findViewById(R.id.ptr_classic_header_rotate_view_progressbar);

        resetView();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mLastUpdateTimeUpdater != null) {
            mLastUpdateTimeUpdater.stop();
        }
    }

    public void setRotateAniTime(int time) {
        if (time == rotateAniTime || time == 0) {
            return;
        }
        rotateAniTime = time;
        buildAnimation();
    }

    /**
     * Specify the last update time by this key string
     *
     * @param key
     */
    public void setLastUpdateTimeKey(String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        lastUpdateTimeKey = key;
    }

    /**
     * Using an object to specify the last update time.
     *
     * @param object
     */
    public void setLastUpdateTimeRelateObject(Object object) {
        setLastUpdateTimeKey(object.getClass().getName());
    }

    private void buildAnimation() {
        flipAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        flipAnimation.setInterpolator(new LinearInterpolator());
        flipAnimation.setDuration(rotateAniTime);
        flipAnimation.setFillAfter(true);

        reverseFlipAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        reverseFlipAnimation.setInterpolator(new LinearInterpolator());
        reverseFlipAnimation.setDuration(rotateAniTime);
        reverseFlipAnimation.setFillAfter(true);
    }

    private void resetView() {
        hideRotateView();
        progressBar.setVisibility(INVISIBLE);
    }

    private void hideRotateView() {
        rotateView.clearAnimation();
        rotateView.setVisibility(INVISIBLE);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        resetView();
        shouldShowLastUpdate = true;
        tryUpdateLastUpdateTime();
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {

        shouldShowLastUpdate = true;
        tryUpdateLastUpdateTime();
        mLastUpdateTimeUpdater.start();

        progressBar.setVisibility(INVISIBLE);

        rotateView.setVisibility(VISIBLE);
        titleTextView.setVisibility(VISIBLE);
        if (frame.isPullToRefresh()) {
            titleTextView.setText(getResources().getString(R.string.cube_ptr_pull_down_to_refresh));
        } else {
            titleTextView.setText(getResources().getString(R.string.cube_ptr_pull_down_to_refresh));
        }
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        shouldShowLastUpdate = false;
        hideRotateView();
        progressBar.setVisibility(VISIBLE);
        titleTextView.setVisibility(VISIBLE);
        titleTextView.setText(R.string.cube_ptr_refreshing);

        tryUpdateLastUpdateTime();
        mLastUpdateTimeUpdater.stop();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {

        hideRotateView();
        progressBar.setVisibility(INVISIBLE);

        titleTextView.setVisibility(VISIBLE);
        titleTextView.setText(getResources().getString(R.string.cube_ptr_refresh_complete));

        // update last update time
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(KEY_SharedPreferences, 0);
        if (!TextUtils.isEmpty(lastUpdateTimeKey)) {
            lastUpdateTime = new Date().getTime();
            sharedPreferences.edit().putLong(lastUpdateTimeKey, lastUpdateTime).commit();
        }
    }

    public void tryUpdateLastUpdateTime() {
        if (TextUtils.isEmpty(lastUpdateTimeKey) || !shouldShowLastUpdate) {
            lastUpdateTextView.setVisibility(GONE);
        } else {
            String time = getLastUpdateTime();
            if (TextUtils.isEmpty(time)) {
                lastUpdateTextView.setVisibility(GONE);
            } else {
                lastUpdateTextView.setVisibility(VISIBLE);
                lastUpdateTextView.setText(time);
            }
        }
    }

    private String getLastUpdateTime() {

        if (lastUpdateTime == -1 && !TextUtils.isEmpty(lastUpdateTimeKey)) {
            lastUpdateTime = getContext().getSharedPreferences(KEY_SharedPreferences, 0).getLong(lastUpdateTimeKey, -1);
        }

        if (lastUpdateTime == -1) {
            return null;
        }

        long diffTime = new Date().getTime() - lastUpdateTime;
        int seconds = (int) (diffTime / 1000);
        if (diffTime < 0) {
            return null;
        }
        if (seconds <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getContext().getString(R.string.cube_ptr_last_update));

        if (seconds < 60) {
            sb.append(seconds + getContext().getString(R.string.cube_ptr_seconds_ago));
        } else {
            int minutes = (seconds / 60);
            if (minutes > 60) {
                int hours = minutes / 60;
                if (hours > 24) {
                    Date date = new Date(lastUpdateTime);
                    sb.append(sDataFormat.format(date));
                } else {
                    sb.append(hours + getContext().getString(R.string.cube_ptr_hours_ago));
                }

            } else {
                sb.append(minutes + getContext().getString(R.string.cube_ptr_minutes_ago));
            }
        }
        return sb.toString();
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {

        final int mOffsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        final int lastPos = ptrIndicator.getLastPosY();

        if (currentPos < mOffsetToRefresh && lastPos >= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                crossRotateLineFromBottomUnderTouch(frame);
                if (rotateView != null) {
                    rotateView.clearAnimation();
                    rotateView.startAnimation(reverseFlipAnimation);
                }
            }
        } else if (currentPos > mOffsetToRefresh && lastPos <= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                crossRotateLineFromTopUnderTouch(frame);
                if (rotateView != null) {
                    rotateView.clearAnimation();
                    rotateView.startAnimation(flipAnimation);
                }
            }
        }
    }

    private void crossRotateLineFromTopUnderTouch(PtrFrameLayout frame) {
        if (!frame.isPullToRefresh()) {
            titleTextView.setVisibility(VISIBLE);
            titleTextView.setText(R.string.cube_ptr_release_to_refresh);
        }
    }

    private void crossRotateLineFromBottomUnderTouch(PtrFrameLayout frame) {
        titleTextView.setVisibility(VISIBLE);
        if (frame.isPullToRefresh()) {
            titleTextView.setText(getResources().getString(R.string.cube_ptr_pull_down_to_refresh));
        } else {
            titleTextView.setText(getResources().getString(R.string.cube_ptr_pull_down_to_refresh));
        }
    }


}
