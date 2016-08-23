package com.mx.framework2.weiget;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshHeaderListener;
import com.handmark.pulltorefresh.library.recyclerview.WrapRecyclerView;
import com.mx.framework2.R;

import java.util.Collection;

/**
 * Created by liuyuxuan on 16/8/22.
 */
public class PullToRefreshRecyclerViewDataBindingAdapters {


    @BindingAdapter({"onRefreshingCommand"})
    public static void setOnRefreshListener(PullToRefreshRecyclerView pullToRefreshRecyclerView,
                                            final OnRefreshingCommand onRefreshingCommand) {
        Log.d("PTR", "setOnRefreshListener=" + onRefreshingCommand.getClass().getName());
        pullToRefreshRecyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<WrapRecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<WrapRecyclerView> refreshView) {
                onRefreshingCommand.onRefreshing();
            }
        });
    }

    @BindingAdapter({"onLoadingCommand"})
    public static void setOnLoadingListener(final PullToRefreshRecyclerView pullToRefreshRecyclerView,
                                            final OnLoadingCommand onLoadingCommand) {
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

    @BindingAdapter({"layoutManager"})
    public static void setLayoutManager(PullToRefreshRecyclerView pullToRefreshRecyclerView,
                                        LayoutManagers.LayoutManagerFactory layoutManagerFactory) {
        Log.d("PTR", "setLayoutManager=" + layoutManagerFactory);
        RecyclerView recyclerView = pullToRefreshRecyclerView.getRefreshableView();
        recyclerView.setLayoutManager(layoutManagerFactory.create(recyclerView));
    }


    @BindingAdapter({"items"})
    public static void setItems(PullToRefreshRecyclerView pullToRefreshRecyclerView, Collection collection) {
        pullToRefreshRecyclerView.setItems(collection);
    }

    @BindingAdapter({"onScrollCommand"})
    public static void setScrollCommand(final PullToRefreshRecyclerView pullToRefreshRecyclerView,
                                        final OnScrollCommand onScrollCommand) {
        if (onScrollCommand == pullToRefreshRecyclerView.getRefreshableView().getTag(R.id.ptr_tag)) {
            return;
        }
        Log.d("PTR", "onScrollCommand=" + onScrollCommand.getClass());
        pullToRefreshRecyclerView.getRefreshableView().setTag(R.id.ptr_tag, onScrollCommand);
        pullToRefreshRecyclerView.getRefreshableView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                onScrollCommand.onScrollStateChanged(recyclerView.getId(), newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                onScrollCommand.onScrolled(recyclerView.getId(), recyclerView.computeVerticalScrollOffset(),
                        recyclerView.computeHorizontalScrollOffset(), dx, dy);
            }
        });
    }

    @BindingAdapter({"onPullToRefreshCommand"})
    public static void setOnPullToRefreshingCommand(final PullToRefreshRecyclerView pullToRefreshRecyclerView,
                                                    final OnPullToRefreshCommand onRefreshingCommand) {
        Log.d("PTR", "setOnPullToRefreshingCommand=" + onRefreshingCommand.getClass());
        pullToRefreshRecyclerView.setPullToRefreshHeaderListener(new PullToRefreshHeaderListener() {
            boolean isPullToRefresh = false;

            @Override
            public void pullToRefresh() {
                isPullToRefresh = true;
            }

            @Override
            public void releaseToRefresh() {
                isPullToRefresh = false;
            }

            @Override
            public void onPull(float scaleOfLayout) {
                onRefreshingCommand.onMove(isPullToRefresh, scaleOfLayout);

            }

            @Override
            public void refreshing() {
                onRefreshingCommand.onRefreshing();

            }

            @Override
            public void reset() {
                onRefreshingCommand.onReset();

            }
        });

    }

}
