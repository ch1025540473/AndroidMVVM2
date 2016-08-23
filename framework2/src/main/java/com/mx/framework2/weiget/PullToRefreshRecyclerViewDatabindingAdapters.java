package com.mx.framework2.weiget;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.recyclerview.WrapRecyclerView;

/**
 * Created by liuyuxuan on 16/8/22.
 */
public class PullToRefreshRecyclerViewDataBindingAdapters {


    @BindingAdapter({"onStartRefreshingCommand"})
    public static void setOnRefreshListener(PullToRefreshRecyclerView pullToRefreshRecyclerView,
                                            final OnStartRefreshingCommand onStartRefreshingCommand) {
        Log.d("PTR", "setOnRefreshListener=" + onStartRefreshingCommand.getClass().getName());
        pullToRefreshRecyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<WrapRecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<WrapRecyclerView> refreshView) {
                onStartRefreshingCommand.onStartRefreshing();
            }
        });
    }

    @BindingAdapter({"onLoadMoreCommand"})
    public static void setOnLoadingListener(final PullToRefreshRecyclerView pullToRefreshRecyclerView,
                                            final OnLoadMoreCommand onLoadMoreCommand) {
        Log.d("PTR", "setOnLoadingListener=" + onLoadMoreCommand.getClass().getName());
        if (null == onLoadMoreCommand) {
            pullToRefreshRecyclerView.setOnLastItemVisibleListener(null);
            return;
        }
        pullToRefreshRecyclerView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                Log.d("PTR", "onLastItemVisible");
                onLoadMoreCommand.onLoadMore();
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

    @BindingAdapter({"loadMoreEnable"})
    public static void loadMoreEnable(PullToRefreshRecyclerView pullToRefreshRecyclerView, boolean isLoadMoreEnable) {
        Log.d("PTR", "isLoadMoreEnable=" + isLoadMoreEnable);
        FooterLoadingView footer = (FooterLoadingView) pullToRefreshRecyclerView.getSecondFooterLayout();
        footer.onLoading(isLoadMoreEnable);
        pullToRefreshRecyclerView.setLoadMoreEnabled(isLoadMoreEnable);
    }

    @BindingAdapter({"loadMoreCompleted"})
    public static void loading(PullToRefreshRecyclerView pullToRefreshRecyclerView, boolean isLoadMoreCompleted) {
        Log.d("PTR", "isLoadingCompleted=" + isLoadMoreCompleted);
        if (isLoadMoreCompleted) {
            pullToRefreshRecyclerView.onLoadMoreComplete();
        } else {
            pullToRefreshRecyclerView.setStartLoading();
        }
    }

    @BindingAdapter({"layoutManager"})
    public static void setLayoutManager(PullToRefreshRecyclerView pullToRefreshRecyclerView,
                                        LayoutManagers.LayoutManagerFactory layoutManagerFactory) {
        Log.d("PTR", "setLayoutManager=" + layoutManagerFactory);
        RecyclerView recyclerView = pullToRefreshRecyclerView.getRefreshableView();
        recyclerView.setLayoutManager(layoutManagerFactory.create(recyclerView));
    }

    @BindingAdapter({"onPullDownCommand"})
    public static void setOnPullToRefreshingListener(final PullToRefreshRecyclerView pullToRefreshRecyclerView,
                                                     final OnPullDownCommand onRefreshingCommand) {
        Log.d("PTR", "setOnPullToRefreshingCommand=" + onRefreshingCommand.getClass());
        pullToRefreshRecyclerView.setPullToRefreshHeaderListener(onRefreshingCommand);

    }

}
