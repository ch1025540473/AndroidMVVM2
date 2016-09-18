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

/**
 * Created by chenbaocheng on 16/8/14.
 */
public class DataBindingRecyclerView extends RecyclerView {
    private ViewModelRecyclerViewAdapter adapter;
    private String itemViewFactory;
    private boolean isLooped = false;

    public DataBindingRecyclerView(Context context) {
        this(context, null);
    }

    public DataBindingRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DataBindingRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DataBindingRecyclerView);
            if (typedArray != null) {
                isLooped = typedArray.getBoolean(R.styleable.DataBindingRecyclerView_looped, false);
                typedArray.recycle();
            }
            initAdapter();
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemizedView);
            itemViewFactory = typedArray.getString(R.styleable.ItemizedView_itemViewFactory);
            if (itemViewFactory != null) {
                setItemViewFactory(itemViewFactory);
            }
            typedArray.recycle();
        } else {
            initAdapter();
        }
    }

    private void initAdapter() {
        if (isLooped) {
            adapter = new ViewModelRecyclerViewAdapter(getContext()) {
                private int length = 100000000;
                Collection<?> items;

                @Override
                public int getItemCount() {
                    return length == 0 ? 0 : length;
                }

                @Override
                public Object getItem(int position) {
                    position = getCount() == 0 ? 0 : position % getCount();
                    return super.getItem(position);
                }

                @Override
                public void putItems(Collection<?> items) {
                    super.putItems(items);

                    if (this.items == null||!this.items.equals(items)) {
                        getLayoutManager().scrollToPosition((length / 2) / items.size());
                        this.items = items;
                    }
                }
            };
        } else {
            adapter = new ViewModelRecyclerViewAdapter(getContext());
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
