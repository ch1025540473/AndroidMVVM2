package com.widget.ptr.listener;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.widget.ptr.builder.PtrMode;
import com.widget.ptr.view.PtrLoadingMoreFooterView;
import com.widget.ptr.view.RotatePtrLoadingMoreFooterView;
import com.widget.ptr.adapter.RefreshRecyclerViewAdapter;

public class LoadMoreRecyclerListener extends RecyclerView.OnScrollListener {

    private PtrMode mode;
    private RecyclerView recyclerView;
    private RefreshRecyclerViewAdapter mAdapter;

    public int firstVisibleItemPosition;
    private int lastVisibleItemPosition;

    private int[] mPositions;
    private int mScrollState;
    private OnLoadMoreListener mOnLoadMoreListener;
    private OnBothRefreshListener mOnBothRefreshListener;
    private PtrLoadingMoreFooterView mFooterLoadingLayout;

    /**
     * 是否是正则加载状态
     */
    private boolean isLoading = false;
    /**
     * 通过滚动方向判断是否允许上拉加载
     */
    public boolean isLoadingMoreEnabled = true;

    private boolean isLoadMore = true;
    /**
     * 加载更多之前RecyclerView的item数量
     */
    private int mOldItemCount;

    private boolean hasCompleted = false;

    public LoadMoreRecyclerListener(RecyclerView recyclerView, PtrMode mode) {
        this.recyclerView = recyclerView;
        this.mode = mode;
        mAdapter = (RefreshRecyclerViewAdapter) recyclerView.getAdapter();
    }

    public void setLoadingMoreEnabled(boolean enabled) {
        isLoadMore = enabled;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        hasCompleted = false;

        isLoadingMoreEnabled = dy > 0;

        RecyclerView.LayoutManager mLayoutManager = recyclerView.getLayoutManager();

        //初始化firstVisibleItemPosition和lastVisibleItemPosition
        if (null != mLayoutManager) {
            if (mLayoutManager instanceof LinearLayoutManager) {
                firstVisibleItemPosition = ((LinearLayoutManager) mLayoutManager)
                        .findFirstVisibleItemPosition();
                lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager)
                        .findLastVisibleItemPosition();
            } else if (mLayoutManager instanceof GridLayoutManager) {
                firstVisibleItemPosition = ((GridLayoutManager) mLayoutManager)
                        .findFirstVisibleItemPosition();
                lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager)
                        .findLastVisibleItemPosition();
            } else if (mLayoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager mStaggeredGridLayoutManager =
                        (StaggeredGridLayoutManager) mLayoutManager;
                if (null == mPositions) {
                    mPositions = new int[mStaggeredGridLayoutManager.getSpanCount()];
                }
                mStaggeredGridLayoutManager.findFirstVisibleItemPositions(mPositions);
                mStaggeredGridLayoutManager.findLastVisibleItemPositions(mPositions);
                firstVisibleItemPosition = getFirst(mPositions);
                lastVisibleItemPosition = getLast(mPositions);
            } else {
                throw new IllegalArgumentException(
                        "The layoutManager must be one of LinearLayoutManager, " +
                                "GridLayoutManager or StaggeredGridLayoutManager");
            }
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (PtrMode.BOTH != mode && PtrMode.BOTTOM != mode) {
            return;
        }

        if (null == recyclerView.getAdapter()
                || !(recyclerView.getAdapter() instanceof RefreshRecyclerViewAdapter)) {
            return;
        }

        mScrollState = newState;
        RecyclerView.LayoutManager mLayoutManager = recyclerView.getLayoutManager();
        int visibleItemCount = mLayoutManager.getChildCount();
        int totalItemCount = mLayoutManager.getItemCount();

        if ((visibleItemCount > 0
                && mScrollState == RecyclerView.SCROLL_STATE_IDLE
                && lastVisibleItemPosition >= totalItemCount - 1)
                && isLoadingMoreEnabled) {

            if (isLoading || !isLoadMore) {
                return;
            }

            if (hasCompleted) {
                hasCompleted = !hasCompleted;
                return;
            }

            if (PtrMode.BOTH == mode) {
                if (null != mOnBothRefreshListener) {
                    addFooterLoadingLayout();
                    mOnBothRefreshListener.onLoadMore();
                    return;
                }
            } else if (PtrMode.BOTTOM == mode) {
                if (null != mOnLoadMoreListener) {
                    addFooterLoadingLayout();
                    mOnLoadMoreListener.onLoadMore();
                    return;
                }
            }

        }
    }

    /**
     * 添加LoadMore布局
     */
    private void addFooterLoadingLayout() {
        isLoading = true;
        if (null == mFooterLoadingLayout) {
            mFooterLoadingLayout = new RotatePtrLoadingMoreFooterView(recyclerView.getContext(), PtrMode.BOTTOM);
        }
        mAdapter.removeFooter(mFooterLoadingLayout);
        mAdapter.addFooterView(mFooterLoadingLayout);
        mOldItemCount = mAdapter.getItemCount();
        recyclerView.smoothScrollToPosition(mOldItemCount - 1);
        mFooterLoadingLayout.onRefresh();
        mFooterLoadingLayout.setVisibility(View.VISIBLE);
    }


    public void setFooterLoadingLayout(PtrLoadingMoreFooterView ptrLoadingMoreFooterView) {

        if (null == ptrLoadingMoreFooterView) {
            return;
        }
        if (mFooterLoadingLayout != null) {
            mAdapter.removeFooter(mFooterLoadingLayout);
        }
        mFooterLoadingLayout = ptrLoadingMoreFooterView;
        mAdapter.addFooterView(mFooterLoadingLayout);
        mOldItemCount = mAdapter.getItemCount();
        recyclerView.smoothScrollToPosition(mOldItemCount - 1);
        mFooterLoadingLayout.onRefresh();
        mFooterLoadingLayout.setVisibility(View.VISIBLE);
    }


    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
    }

    public void setOnBothRefreshListener(OnBothRefreshListener onBothRefreshListener) {
        this.mOnBothRefreshListener = onBothRefreshListener;
    }

    /**
     * StaggeredGridLayoutManager firstVisibleItemPosition
     *
     * @param mPositions
     * @return
     */
    private int getFirst(int[] mPositions) {
        int first = mPositions[0];
        for (int value : mPositions) {
            if (value < first) {
                first = value;
            }
        }
        return first;
    }

    /**
     * StaggeredGridLayoutManager lastVisibleItemPosition
     *
     * @param mPositions
     * @return
     */
    private int getLast(int[] mPositions) {
        int last = mPositions[0];
        for (int value : mPositions) {
            if (value > last) {
                last = value;
            }
        }
        return last;
    }

    public void setMode(PtrMode mode) {
        this.mode = mode;
    }

    public void onRefreshComplete() {
        if (null != mAdapter && mAdapter.getFootersCount() > 0) {
            if (mAdapter.getLastFooter() instanceof PtrLoadingMoreFooterView) {
                isLoading = false;
                hasCompleted = true;
                mAdapter.removeFooter(mFooterLoadingLayout);
            }
        }
    }

}
