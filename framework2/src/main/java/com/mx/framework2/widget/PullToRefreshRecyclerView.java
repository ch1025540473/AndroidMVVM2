package com.mx.framework2.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import com.handmark.pulltorefresh.library.LoadingLayoutBase;
import com.handmark.pulltorefresh.library.recyclerview.UltimateRecyclerView;
import com.handmark.pulltorefresh.library.recyclerview.WrapRecyclerView;
import com.mx.engine.utils.ObjectUtils;
import com.mx.framework2.R;
import com.mx.framework2.view.adapter.ViewModelRecyclerViewAdapter;
import com.mx.framework2.view.factory.ItemViewFactory;

import java.lang.reflect.Constructor;
import java.util.Collection;

/**
 * Created by liuyuxuan on 16/8/22.
 */
public class PullToRefreshRecyclerView extends UltimateRecyclerView {

    private final ViewModelRecyclerViewAdapter adapter;
    private String itemViewFactory;
    private RecyclerView.OnScrollListener onScrollListener;
    private OnScrollCommand onScrollCommand;

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        adapter = new ViewModelRecyclerViewAdapter(context);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemizedView);
            itemViewFactory = typedArray.getString(R.styleable.ItemizedView_itemViewFactory);
            setItemViewFactory(itemViewFactory);
            typedArray.recycle();
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.PullToRefreshRecyclerView);
            String headerClass = typedArray.getString(R.styleable.PullToRefreshRecyclerView_headerClassName);
            setHeaderClassName(headerClass);
            String footerClass = typedArray.getString(R.styleable.PullToRefreshRecyclerView_footerClassName);
            setFooterClassName(footerClass);
            typedArray.recycle();
        }
        setMode(Mode.PULL_FROM_START);
    }

    private void setHeaderClassName(String headerClassName) {
        try {
            Constructor c = Class.forName(headerClassName).getDeclaredConstructor(new Class[]{Context.class});
            LoadingLayoutBase headerLayout = (LoadingLayoutBase) c.newInstance(new Object[]{getContext()});
            setHeaderLayout(headerLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFooterClassName(String footerClassName) {
        try {
            Constructor c = Class.forName(footerClassName).getDeclaredConstructor(new Class[]{Context.class});
            FooterLoadingView footerLayout = (FooterLoadingView) c.newInstance(new Object[]{getContext()});
            setSecondFooterLayout(footerLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected WrapRecyclerView createRefreshableView(Context context, AttributeSet attrs) {
        WrapRecyclerView recyclerView = new InternalWrapRecyclerView(context, attrs) {
            @Override
            public void setLayoutManager(LayoutManager layout) {
                super.setLayoutManager(layout);
                setAdapter(adapter);
            }
        };
        recyclerView.setId(com.handmark.pulltorefresh.library.R.id.ultimate_recycler_view);
        recyclerView.setScrollingTouchSlop(RecyclerView.TOUCH_SLOP_PAGING);
        return recyclerView;
    }

    public PullToRefreshRecyclerView(Context context) {
        super(context);
        adapter = new ViewModelRecyclerViewAdapter(context);
    }

    public void setItemViewFactory(String className) {
        ItemViewFactory factory = ObjectUtils.newInstance(className);
        factory.setContext(getContext());
        adapter.setItemViewFactory(factory);
    }

    public void setItems(Collection items) {
        adapter.putItems(items);
    }

    public void setOnScrollCommand(
            final OnScrollCommand onScrollCommand) {
        Log.d("PTR", "onScrollCommand=" + onScrollCommand.getClass());
        this.onScrollCommand = onScrollCommand;
        if (onScrollListener == null) {
            onScrollListener = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    PullToRefreshRecyclerView.this.onScrollCommand.onScrollStateChanged(recyclerView.getId(), newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    PullToRefreshRecyclerView.this.onScrollCommand.onScrolled(recyclerView.getId(),
                            recyclerView.computeVerticalScrollOffset(),
                            recyclerView.computeHorizontalScrollOffset(), dx, dy);
                }
            };
            getRefreshableView().addOnScrollListener(onScrollListener);
        }
    }
}
