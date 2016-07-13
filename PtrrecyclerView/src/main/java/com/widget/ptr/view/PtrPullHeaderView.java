package com.widget.ptr.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.text.SimpleDateFormat;

import in.srain.cube.views.ptr.PtrUIHandler;

/**
 * Created by liuyuxuan on 16/6/16.
 */
public abstract class PtrPullHeaderView extends FrameLayout implements PtrUIHandler {
    protected String lastUpdateTimeKey;
    protected boolean mShouldShowLastUpdate;
    protected final static String KEY_SharedPreferences = "cube_ptr_classic_last_update";
    protected static SimpleDateFormat sDataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    protected LastUpdateTimeUpdater mLastUpdateTimeUpdater = new LastUpdateTimeUpdater();

    public PtrPullHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PtrPullHeaderView(Context context) {
        super(context);
    }

    public PtrPullHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected class LastUpdateTimeUpdater implements Runnable {

        private boolean mRunning = false;

        public void start() {
            if (TextUtils.isEmpty(lastUpdateTimeKey)) {
                return;
            }
            mRunning = true;
            run();
        }

        public void stop() {
            mRunning = false;
            removeCallbacks(this);
        }

        @Override
        public void run() {
            tryUpdateLastUpdateTime();
            if (mRunning) {
                postDelayed(this, 1000);
            }
        }
    }

    protected void tryUpdateLastUpdateTime() {

    }

    public void setLastUpdateTimeKey(String lastUpdateTimeKey) {
        this.lastUpdateTimeKey = lastUpdateTimeKey;
    }

    public void setLastUpdateTimeRelateObject(Object object) {

    }
}
