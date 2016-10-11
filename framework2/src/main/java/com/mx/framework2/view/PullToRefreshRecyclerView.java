package com.mx.framework2.view;

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
import com.mx.framework2.viewmodel.command.OnScrollCommand;
import com.mx.framework2.viewmodel.proxy.PTRRecyclerViewProxy;

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
    private PTRRecyclerViewProxy recyclerViewProxy;
    private String headerClassName = "";
    private String footerClassName = "";

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        adapter = new ViewModelRecyclerViewAdapter(context);
        getRefreshableView().setAdapter(adapter);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemizedView);

            if (typedArray != null) {
                itemViewFactory = typedArray.getString(R.styleable.ItemizedView_itemViewFactory);
                if (itemViewFactory != null) {
                    setItemViewFactory(itemViewFactory);
                }
                typedArray.recycle();
            }

            typedArray = context.obtainStyledAttributes(attrs, R.styleable.PullToRefreshRecyclerView);
            if (typedArray != null) {
                String headerClass = typedArray.getString(R.styleable.PullToRefreshRecyclerView_headerClassName);
                if (headerClass != null) {
                    setHeaderClassName(headerClass);
                }
                String footerClass = typedArray.getString(R.styleable.PullToRefreshRecyclerView_footerClassName);
                if (footerClass != null) {
                    setFooterClassName(footerClass);
                }
                typedArray.recycle();
            }

        }
        setMode(Mode.PULL_FROM_START);

    }

    public void setHeaderClassName(String headerClassName) {
        if (headerClassName == null
                || this.headerClassName.equals(headerClassName)) {
            return;
        }
        try {
            Constructor c = Class.forName(headerClassName).getDeclaredConstructor(new Class[]{Context.class});
            LoadingLayoutBase headerLayout = (LoadingLayoutBase) c.newInstance(new Object[]{getContext()});
            setHeaderLayout(headerLayout);
            this.headerClassName = headerClassName;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFooterClassName(String footerClassName) {
        if (footerClassName == null
                || this.footerClassName.equals(footerClassName)) {
            return;
        }
        try {
            Constructor c = Class.forName(footerClassName).getDeclaredConstructor(new Class[]{Context.class});
            FooterLoadingView footerLayout = (FooterLoadingView) c.newInstance(new Object[]{getContext()});
            setSecondFooterLayout(footerLayout);
            this.footerClassName = footerClassName;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected WrapRecyclerView createRefreshableView(Context context, AttributeSet attrs) {
        WrapRecyclerView recyclerView = new InternalWrapRecyclerView(context, attrs);
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

    public void setProxy(PTRRecyclerViewProxy recyclerViewProxy) {
        if (recyclerViewProxy == null) {
            return;
        }
        if (recyclerViewProxy != this.recyclerViewProxy) {
            recyclerViewProxy.attach(this);
            this.recyclerViewProxy = recyclerViewProxy;
        }
    }


    public void setOnScrollCommand(
            OnScrollCommand command) {
        this.onScrollCommand = command;
        if (onScrollListener == null) {
            onScrollListener = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if (onScrollCommand != null) {
                        onScrollCommand.onScrollStateChanged(recyclerView.getId(), newState);
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (onScrollCommand != null) {
                        onScrollCommand.onScrolled(recyclerView.getId(),
                                recyclerView.computeVerticalScrollOffset(),
                                recyclerView.computeHorizontalScrollOffset(), dx, dy);
                    }

                }
            };
            getRefreshableView().addOnScrollListener(onScrollListener);
        }
    }
}
