package com.widget.ptr.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.widget.ptr.R;
import com.widget.swipe.SwipeLayout;

/**
 * Created by wuhenzhizao on 16/7/6.
 */
public class SwipeMenuRecyclerView extends RecyclerView {
    private int touchSlop;

    public SwipeMenuRecyclerView(Context context) {
        super(context);
    }

    public SwipeMenuRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public SwipeMenuRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private boolean checked = false;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                checked = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!checked){
                    checked = true;
                    checkCloseSwipeLayout();
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private void checkCloseSwipeLayout(){
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewGroup parent = (ViewGroup) getChildAt(i);
            SwipeLayout swipeLayout = (SwipeLayout) parent.findViewById(R.id.swipe_layout);
            if (swipeLayout != null && swipeLayout.getOpenStatus() != SwipeLayout.Status.Close) {
                swipeLayout.close(true);
            }
        }
    }
}
