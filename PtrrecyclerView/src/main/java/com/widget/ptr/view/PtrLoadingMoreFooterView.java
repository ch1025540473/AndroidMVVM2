package com.widget.ptr.view;

import android.content.Context;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.widget.ptr.builder.PtrMode;

/**
 */
public abstract class PtrLoadingMoreFooterView extends FrameLayout {
    protected static final int ROTATION_ANIMATION_DURATION = 1200;
    protected static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();
    protected Context context;
    protected PtrMode mode;

    public PtrLoadingMoreFooterView(Context context, PtrMode mode) {
        super(context);
        this.context = context;
        this.mode = mode;
        init();
        reset();
    }

    protected void init() {
    }

    public final void onRefresh() {
        onRefreshImpl();
    }

    public void reset() {
        onResetImpl();
    }

    protected abstract void onRefreshImpl();

    protected abstract void onResetImpl();

}
