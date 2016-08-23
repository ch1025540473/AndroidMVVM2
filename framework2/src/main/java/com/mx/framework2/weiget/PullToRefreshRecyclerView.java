package com.mx.framework2.weiget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.handmark.pulltorefresh.library.recyclerview.UltimateRecyclerView;
import com.handmark.pulltorefresh.library.recyclerview.WrapRecyclerView;
import com.mx.engine.utils.ObjectUtils;
import com.mx.framework2.R;
import com.mx.framework2.view.adapter.ViewModelRecyclerViewAdapter;
import com.mx.framework2.view.factory.ItemViewFactory;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by liuyuxuan on 16/8/22.
 */
public class PullToRefreshRecyclerView extends UltimateRecyclerView {

    private final ViewModelRecyclerViewAdapter adapter;
    private String itemViewFactory;

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        adapter = new ViewModelRecyclerViewAdapter(context);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemizedView);
            itemViewFactory = typedArray.getString(R.styleable.ItemizedView_itemViewFactory);
            setItemViewFactory(itemViewFactory);
            typedArray.recycle();
        }
        setMode(Mode.PULL_FROM_START);
    }

    @Override
    protected WrapRecyclerView createRefreshableView(Context context, AttributeSet attrs) {
        return super.createRefreshableView(context, attrs);
    }

    public PullToRefreshRecyclerView(Context context) {
        super(context);
        adapter = new ViewModelRecyclerViewAdapter(context);
    }

    public ViewModelRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    public void setItemViewFactory(String className) {
        ItemViewFactory factory = ObjectUtils.newInstance(className);
        factory.setContext(getContext());
        adapter.setItemViewFactory(factory);
    }

    public void setItems(Collection items) {
        adapter.putItems(items);
    }
}
