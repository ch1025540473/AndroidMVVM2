package com.mx.framework2.view;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.mx.engine.utils.ObjectUtils;
import com.mx.framework2.view.adapter.ViewModelRecyclerViewAdapter;
import com.mx.framework2.view.factory.ItemViewFactory;

import java.util.Collection;

/**
 * Created by chenbaocheng on 16/8/14.
 */
public class ViewModelRecyclerView extends RecyclerView{
    private final ViewModelRecyclerViewAdapter adapter;

    public ViewModelRecyclerView(Context context) {
        this(context, null);
    }

    public ViewModelRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewModelRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        adapter = new ViewModelRecyclerViewAdapter(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        this.setAdapter(adapter);
        this.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public ViewModelRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    public void setItemViewFactory(String className){
        ItemViewFactory factory = ObjectUtils.newInstance(className);
        factory.setContext(getContext());
        adapter.setItemViewFactory(factory);
    }

    public void setItems(Collection items){
        adapter.putItems(items);
    }
}
