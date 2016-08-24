package com.mx.framework2.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.mx.engine.utils.ObjectUtils;
import com.mx.framework2.R;
import com.mx.framework2.view.adapter.ViewModelRecyclerViewAdapter;
import com.mx.framework2.view.factory.ItemViewFactory;
import com.mx.framework2.widget.LayoutManagers;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by chenbaocheng on 16/8/14.
 */
public class DataBindingRecyclerView extends RecyclerView {
    private ViewModelRecyclerViewAdapter adapter;
    private String itemViewFactory;

    public DataBindingRecyclerView(Context context) {
        this(context, null);
    }

    public DataBindingRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DataBindingRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        adapter = new ViewModelRecyclerViewAdapter(context);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemizedView);
            itemViewFactory = typedArray.getString(R.styleable.ItemizedView_itemViewFactory);
            setItemViewFactory(itemViewFactory);
            typedArray.recycle();
        }
    }

    @Override
    public ViewModelRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    public void setItemViewFactory(String className) {
        ItemViewFactory factory = ObjectUtils.newInstance(className);
        factory.setContext(getContext());
        adapter.setItemViewFactory(factory);
    }

    public void setLayoutManager(LayoutManagers.LayoutManagerFactory factory) {
        super.setLayoutManager(factory.create(this));
        setAdapter(adapter);
    }

    public void setItems(Collection items) {
        adapter.putItems(items);
    }
}
