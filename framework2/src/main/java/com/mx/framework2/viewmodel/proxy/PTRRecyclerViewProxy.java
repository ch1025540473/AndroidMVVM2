package com.mx.framework2.viewmodel.proxy;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.recyclerview.WrapRecyclerView;
import com.mx.framework2.view.FooterLoadingView;
import com.mx.framework2.viewmodel.command.OnLoadMoreCommand;
import com.mx.framework2.viewmodel.command.OnPullDownCommand;
import com.mx.framework2.viewmodel.command.OnScrollCommand;
import com.mx.framework2.viewmodel.command.OnStartRefreshingCommand;
import com.mx.framework2.view.PullToRefreshRecyclerView;

import java.lang.ref.WeakReference;

import static com.mx.framework2.viewmodel.proxy.PTRRecyclerViewProxy.PTRMode.BOTH;
import static com.mx.framework2.viewmodel.proxy.PTRRecyclerViewProxy.PTRMode.BOTTOM;
import static com.mx.framework2.viewmodel.proxy.PTRRecyclerViewProxy.PTRMode.NONE;

/**
 * Created by liuyuxuan on 16/9/19.
 */

public class PTRRecyclerViewProxy {

    public enum PTRMode {
        NONE, BOTH, TOP, BOTTOM
    }
    private PTRMode ptrMode = BOTH;
    private OnStartRefreshingCommand onStartRefreshingCommand;
    private OnPullDownCommand onPullDownCommand;
    private OnLoadMoreCommand onLoadMoreCommand;
    private boolean isLoadMoreComplete = false;
    private OnScrollCommand onScrollCommand;
    private WeakReference<PullToRefreshRecyclerView> ptrRecyclerViewRef;

    public final void attach(PullToRefreshRecyclerView ptrRecyclerView) {
        this.ptrRecyclerViewRef = new WeakReference<PullToRefreshRecyclerView>(ptrRecyclerView);
        setOnPullDownCommand(onPullDownCommand);
        setOnScrollCommand(onScrollCommand);
        setPtrMode(ptrMode);
        setOnStartRefreshingCommand(onStartRefreshingCommand);
        setOnLoadMoreCommand(onLoadMoreCommand);
        setLoadMoreComplete(isLoadMoreComplete);
    }

    public OnScrollCommand getOnScrollCommand() {
        return onScrollCommand;
    }

    public void setOnScrollCommand(OnScrollCommand onScrollCommand) {
        this.onScrollCommand = onScrollCommand;
        PullToRefreshRecyclerView ptrRecyclerView = getPtrRecyclerView();
        if (ptrRecyclerView != null) {
            ptrRecyclerView.setOnScrollCommand(onScrollCommand);
        }
    }

    private PullToRefreshRecyclerView getPtrRecyclerView() {
        if (ptrRecyclerViewRef != null) {
            return ptrRecyclerViewRef.get();
        }
        return null;
    }


    public PTRMode getPtrMode() {
        return ptrMode;
    }

    public void setPtrMode(PTRMode ptrMode) {
        this.ptrMode = ptrMode;
        PullToRefreshRecyclerView ptrRecyclerView = getPtrRecyclerView();
        if (ptrRecyclerView != null) {
            boolean footerEnable = (this.ptrMode.equals(BOTH)
                    || this.ptrMode.equals(PTRMode.BOTTOM));
            FooterLoadingView footer = (FooterLoadingView) ptrRecyclerView.getSecondFooterLayout();
            if (footer != null) {
                footer.onLoading(footerEnable);
                ptrRecyclerView.setLoadMoreEnabled(footerEnable);
            }
            if (this.ptrMode.equals(NONE) || this.ptrMode.equals(BOTTOM)) {
                ptrRecyclerView.onRefreshComplete();
                ptrRecyclerView.setMode(PullToRefreshBase.Mode.DISABLED);
            } else {
                ptrRecyclerView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
        }
    }

    public OnStartRefreshingCommand getOnStartRefreshingCommand() {
        return onStartRefreshingCommand;
    }

    public void setOnStartRefreshingCommand(OnStartRefreshingCommand onStartRefreshingCommand) {
        this.onStartRefreshingCommand = onStartRefreshingCommand;
        PullToRefreshRecyclerView ptrRecyclerView = getPtrRecyclerView();
        if (ptrRecyclerView != null) {
            ptrRecyclerView.setOnRefreshListener(new PullToRefreshBase
                    .OnRefreshListener<WrapRecyclerView>() {
                @Override
                public void onRefresh(PullToRefreshBase<WrapRecyclerView> refreshView) {
                    PTRRecyclerViewProxy.this.onStartRefreshingCommand.onStartRefreshing();
                }
            });
        }
    }

    public OnPullDownCommand getOnPullDownCommand() {
        return onPullDownCommand;
    }

    public void setOnPullDownCommand(OnPullDownCommand onPullDownCommand) {
        this.onPullDownCommand = onPullDownCommand;
        PullToRefreshRecyclerView ptrRecyclerView = getPtrRecyclerView();
        if (ptrRecyclerView != null) {
            ptrRecyclerView.setPullToRefreshHeaderListener(onPullDownCommand);
        }
    }

    public OnLoadMoreCommand getOnLoadMoreCommand() {
        return onLoadMoreCommand;
    }

    public void setOnLoadMoreCommand(OnLoadMoreCommand onLoadMoreCommand) {
        this.onLoadMoreCommand = onLoadMoreCommand;
        PullToRefreshRecyclerView ptrRecyclerView = getPtrRecyclerView();
        if (ptrRecyclerView != null) {
            ptrRecyclerView.setOnLastItemVisibleListener(new PullToRefreshBase
                    .OnLastItemVisibleListener() {
                @Override
                public void onLastItemVisible() {
                    PTRRecyclerViewProxy.this.onLoadMoreCommand.onLoadMore();
                }
            });
        }
    }

    public boolean isRefresh() {
        PullToRefreshRecyclerView ptrRecyclerView = getPtrRecyclerView();
        if (ptrRecyclerView != null) {
            return ptrRecyclerView.isRefreshing();
        }
        return false;
    }

    public void setRefresh(boolean isRefresh) {
        PullToRefreshRecyclerView ptrRecyclerView = getPtrRecyclerView();
        if (ptrRecyclerView != null) {
            if (isRefresh) {
                ptrRecyclerView.setRefreshing();
            } else {
                ptrRecyclerView.onRefreshComplete();
            }
        }
    }

    public boolean isLoadMoreComplete() {
        return isLoadMoreComplete;
    }

    public void setLoadMoreComplete(boolean loadMoreComplete) {
        PullToRefreshRecyclerView ptrRecyclerView = getPtrRecyclerView();
        isLoadMoreComplete = loadMoreComplete;
        if (ptrRecyclerView != null) {
            if (isLoadMoreComplete) {
                ptrRecyclerView.onLoadMoreComplete();
            } else {
                ptrRecyclerView.setStartLoading();
            }
        }
    }


    public int findFirstVisibleItemPosition() {
        PullToRefreshRecyclerView ptrRecyclerView = getPtrRecyclerView();
        if (ptrRecyclerView != null) {
            RecyclerView.LayoutManager layoutManager = ptrRecyclerView.getRefreshableView().getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                return ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            }
            if (layoutManager instanceof GridLayoutManager) {
                return ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
            }
            if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager lm = (StaggeredGridLayoutManager) layoutManager;
                int[] viewsIds = lm.findFirstCompletelyVisibleItemPositions(null);
                RecyclerView.ViewHolder firstViewHolder = ptrRecyclerView.getRefreshableView().findViewHolderForLayoutPosition(viewsIds[0]);
                View itemView = firstViewHolder.itemView;
                return lm.getPosition(itemView);
            }
        }
        return -1;
    }

    public int findLastVisibleItemPosition() {
        PullToRefreshRecyclerView ptrRecyclerView = getPtrRecyclerView();
        if (ptrRecyclerView != null) {
            RecyclerView.LayoutManager layoutManager = ptrRecyclerView.getRefreshableView().getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            if (layoutManager instanceof GridLayoutManager) {
                return ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager lm = (StaggeredGridLayoutManager) layoutManager;
                int[] viewsIds = lm.findLastCompletelyVisibleItemPositions(null);
                RecyclerView.ViewHolder firstViewHolder = ptrRecyclerView.getRefreshableView().findViewHolderForLayoutPosition(viewsIds[0]);
                View itemView = firstViewHolder.itemView;
                return lm.getPosition(itemView);
            }
        }
        return -1;
    }

    public void scrollToPositionWithOffset(int position, int offset) {
        PullToRefreshRecyclerView ptrRecyclerView = getPtrRecyclerView();
        if (ptrRecyclerView != null) {
            if (ptrRecyclerView.getRefreshableView().getLayoutManager() instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) ptrRecyclerView.getRefreshableView().getLayoutManager();
                linearLayoutManager.scrollToPositionWithOffset(position, offset);
            }
        }
    }
}
