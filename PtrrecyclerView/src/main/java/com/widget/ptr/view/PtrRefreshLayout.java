package com.widget.ptr.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by liuyuxuan on 16/6/16.
 */
public class PtrRefreshLayout extends PtrFrameLayout {
    private LinearLayout ptrRefreshHeader;
    private PtrPullHeaderView ptrPullHeaderView;

    public PtrRefreshLayout(Context context) {
        super(context);
        initViews();
    }

    public PtrRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public PtrRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }

    private void initViews() {
        ptrRefreshHeader = new LinearLayout(getContext());
        ptrRefreshHeader.setOrientation(LinearLayout.HORIZONTAL);
        ptrPullHeaderView = new PtrDefaultHeaderView(getContext());
        ptrRefreshHeader.addView(ptrPullHeaderView, new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        setHeaderView(ptrRefreshHeader);
        addPtrUIHandler(ptrPullHeaderView);
    }


    public void setPtrHeaderView(@NonNull PtrPullHeaderView ptrPullHeaderView) {
        ptrRefreshHeader.removeView(this.ptrPullHeaderView);
        removePtrUIHandler(this.ptrPullHeaderView);
        addPtrUIHandler(ptrPullHeaderView);
        ptrRefreshHeader.addView(ptrPullHeaderView, new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        this.ptrPullHeaderView = ptrPullHeaderView;
    }

    /**
     * Specify the last update time by this key string
     *
     * @param key
     */
    public void setLastUpdateTimeKey(String key) {
        if (ptrPullHeaderView != null) {
            ptrPullHeaderView.setLastUpdateTimeKey(key);
        }
    }

    /**
     * Using an object to specify the last update time.
     *
     * @param object
     */
    public void setLastUpdateTimeRelateObject(Object object) {
        if (ptrPullHeaderView != null) {
            ptrPullHeaderView.setLastUpdateTimeRelateObject(object);
        }
    }
}
