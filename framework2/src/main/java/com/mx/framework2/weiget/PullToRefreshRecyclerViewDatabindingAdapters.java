package com.mx.framework2.weiget;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.handmark.pulltorefresh.library.LoadingLayoutBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.recyclerview.WrapRecyclerView;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.List;

/**
 * Created by liuyuxuan on 16/8/22.
 */
public class PullToRefreshRecyclerViewDatabindingAdapters {

    public interface OnRefreshingCommand {
        void onRefreshing();
    }

    public interface OnLoadingCommand {
        void onLoading();
    }

    @BindingAdapter({"onRefreshingCommand"})
    public static void setOnRefreshListener(PullToRefreshRecyclerView pullToRefreshRecyclerView, final OnRefreshingCommand onRefreshingCommand) {
        Log.d("PTR", "setOnRefreshListener=" + onRefreshingCommand.getClass().getName());
        pullToRefreshRecyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<WrapRecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<WrapRecyclerView> refreshView) {
                onRefreshingCommand.onRefreshing();
            }
        });
    }

    @BindingAdapter({"onLoadingCommand"})
    public static void setOnLoadingListener(final PullToRefreshRecyclerView pullToRefreshRecyclerView, final OnLoadingCommand onLoadingCommand) {
        Log.d("PTR", "setOnLoadingListener=" + onLoadingCommand.getClass().getName());
        if (null == onLoadingCommand) {
            pullToRefreshRecyclerView.setOnLastItemVisibleListener(null);
            return;
        }
        pullToRefreshRecyclerView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                Log.d("PTR", "onLastItemVisible");
                onLoadingCommand.onLoading();
            }
        });
    }

    @BindingAdapter({"refreshing"})
    public static void refreshing(PullToRefreshRecyclerView pullToRefreshRecyclerView, boolean isRefreshing) {
        if (isRefreshing) {
            pullToRefreshRecyclerView.setRefreshing();
        } else {
            pullToRefreshRecyclerView.onRefreshComplete();
        }
    }

    @BindingAdapter({"loadingEnable"})
    public static void loadingEnable(PullToRefreshRecyclerView pullToRefreshRecyclerView, boolean isLoadingEnable) {
        Log.d("PTR", "loading=" + isLoadingEnable);
        FooterLoadingView footer = (FooterLoadingView) pullToRefreshRecyclerView.getSecondFooterLayout();
        footer.onLoading(isLoadingEnable);
        pullToRefreshRecyclerView.setLoadingEnabled(isLoadingEnable);
    }

    @BindingAdapter({"loadingCompleted"})
    public static void loading(PullToRefreshRecyclerView pullToRefreshRecyclerView, boolean isLoadingCompleted) {
        Log.d("PTR", "isLoadingCompleted=" + isLoadingCompleted);
        if (isLoadingCompleted) {
            pullToRefreshRecyclerView.onLoadingComplete();
        } else {
            pullToRefreshRecyclerView.setLoading();
        }
    }

    @BindingAdapter({"headerClassName"})
    public static void setHeaderClassName(PullToRefreshRecyclerView pullToRefreshRecyclerView, String headerClassName) {
        try {
            Log.d("PTR", "setPullHeaderView=" + headerClassName);
            Constructor c = Class.forName(headerClassName).getDeclaredConstructor(new Class[]{Context.class});
            LoadingLayoutBase headerLayout = (LoadingLayoutBase) c.newInstance(new Object[]{pullToRefreshRecyclerView.getContext()});
            pullToRefreshRecyclerView.setHeaderLayout(headerLayout);
            Log.d("PTR", "setPullHeaderView==");
        } catch (Exception e) {
            Log.d("PTR", "setPullHeaderView==" + e.getMessage());
            e.printStackTrace();
        }
    }

    @BindingAdapter({"footerClassName"})
    public static void setFooterClassName(PullToRefreshRecyclerView pullToRefreshRecyclerView, String footerClassName) {
        try {
            Log.d("PTR", "setFooterClassName=" + footerClassName);
            Constructor c = Class.forName(footerClassName).getDeclaredConstructor(new Class[]{Context.class});
            FooterLoadingView footerLayout = (FooterLoadingView) c.newInstance(new Object[]{pullToRefreshRecyclerView.getContext()});
            pullToRefreshRecyclerView.setSecondFooterLayout(footerLayout);
            pullToRefreshRecyclerView.setTag(footerLayout);
            loading(pullToRefreshRecyclerView, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BindingAdapter({"layoutManager"})
    public static void setLayoutManager(PullToRefreshRecyclerView pullToRefreshRecyclerView, LayoutManagers.LayoutManagerFactory layoutManagerFactory) {
        Log.d("PTR", "setLayoutManager=" + layoutManagerFactory);
        RecyclerView recyclerView = pullToRefreshRecyclerView.getRefreshableView();
        recyclerView.setLayoutManager(layoutManagerFactory.create(recyclerView));
        recyclerView.setAdapter(pullToRefreshRecyclerView.getAdapter());
    }

    @BindingAdapter({"itemViewFactory"})
    public static void setItemViewFactory(PullToRefreshRecyclerView pullToRefreshRecyclerView, String className) {
        Log.d("PTR", "setItemViewFactory=" + className);
        pullToRefreshRecyclerView.setItemViewFactory(className);
    }

    @BindingAdapter({"items"})
    public static void setItems(PullToRefreshRecyclerView pullToRefreshRecyclerView, Collection collection) {
        pullToRefreshRecyclerView.setItems(collection);
    }

}
