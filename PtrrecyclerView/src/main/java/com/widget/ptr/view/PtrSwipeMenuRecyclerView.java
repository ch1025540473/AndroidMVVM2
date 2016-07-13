package com.widget.ptr.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by wuhenzhizao on 16/7/6.
 */
public class PtrSwipeMenuRecyclerView extends PtrRecyclerView {

    public PtrSwipeMenuRecyclerView(Context context) {
        super(context);
    }

    public PtrSwipeMenuRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public PtrSwipeMenuRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected RecyclerView buildRecyclerView(Context context, AttributeSet attrs) {
        return new SwipeMenuRecyclerView(context, attrs);
    }
}
