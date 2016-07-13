package com.widget.ptr.builder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.widget.ptr.adapter.RefreshRecyclerViewAdapter;
import com.widget.ptr.listener.LoadMoreRecyclerListener;
import com.widget.ptr.listener.OnBothRefreshListener;
import com.widget.ptr.listener.OnLoadMoreListener;
import com.widget.ptr.listener.OnPullDownListener;
import com.widget.ptr.view.PtrLoadingMoreFooterView;
import com.widget.ptr.view.PtrPullHeaderView;
import com.widget.ptr.view.PtrRecyclerView;

/**
 * Created by liuyuxuan on 16/6/20.
 */
public class PtrRecyclerViewBuilder {
    private RecyclerView.LayoutManager layoutManager;
    private PtrPullHeaderView ptrPullHeaderView;
    private PtrLoadingMoreFooterView ptrLoadingMoreFooterView;
    private RefreshRecyclerViewAdapter refreshRecyclerViewAdapter;
    private PtrMode ptrMode;
    private OnBothRefreshListener onBothRefreshListener;
    private OnPullDownListener onPullDownListener;
    private OnLoadMoreListener onLoadMoreListener;
    private RecyclerView.ItemDecoration decoration;
    private LoadMoreRecyclerListener loadMoreRecyclerListener;
    private RecyclerView.ItemAnimator itemAnimator;

    public PtrRecyclerViewBuilder(@NonNull RecyclerView.Adapter adapter) {

        if (null == adapter) {
            throw new NullPointerException("");
        }
        refreshRecyclerViewAdapter = new RefreshRecyclerViewAdapter(adapter);
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return layoutManager;
    }

    public PtrPullHeaderView getPtrPullHeaderView() {
        return ptrPullHeaderView;
    }

    public PtrLoadingMoreFooterView getPtrLoadingMoreFooterView() {
        return ptrLoadingMoreFooterView;
    }

    public RefreshRecyclerViewAdapter getRefreshRecyclerViewAdapter() {
        return refreshRecyclerViewAdapter;
    }

    public PtrMode getPtrMode() {
        return ptrMode;
    }

    public OnBothRefreshListener getOnBothRefreshListener() {
        return onBothRefreshListener;
    }

    public OnPullDownListener getOnPullDownListener() {
        return onPullDownListener;
    }

    public OnLoadMoreListener getOnLoadMoreListener() {
        return onLoadMoreListener;
    }

    public RecyclerView.ItemDecoration getDecoration() {
        return decoration;
    }

    public LoadMoreRecyclerListener getLoadMoreRecyclerListener() {
        return loadMoreRecyclerListener;
    }

    public void setLoadMoreRecyclerListener(LoadMoreRecyclerListener loadMoreRecyclerListener) {
        this.loadMoreRecyclerListener = loadMoreRecyclerListener;
    }

    public RecyclerView.ItemAnimator getItemAnimator() {
        return itemAnimator;
    }

    public PtrRecyclerViewBuilder setLayoutManager(@NonNull RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        return this;
    }

    public PtrRecyclerViewBuilder addHeaderView(@NonNull View view) {
        refreshRecyclerViewAdapter.addHeaderView(view);
        return this;
    }

    public PtrRecyclerViewBuilder addHeaderView(@NonNull View view, int position) {
        refreshRecyclerViewAdapter.addHeaderView(view, position);
        return this;
    }

    public PtrRecyclerViewBuilder setPtrMode(@NonNull PtrMode ptrMode) {
        this.ptrMode = ptrMode;
        return this;
    }

    public PtrRecyclerViewBuilder setPtrPullHeaderView(@NonNull PtrPullHeaderView ptrPullHeaderView) {
        this.ptrPullHeaderView = ptrPullHeaderView;
        return this;
    }

    public PtrRecyclerViewBuilder setPtrLoadingMoreFooterView(@NonNull PtrLoadingMoreFooterView ptrLoadingMoreFooterView) {
        this.ptrLoadingMoreFooterView = ptrLoadingMoreFooterView;
        return this;
    }

    public PtrRecyclerViewBuilder setOnBothRefreshListener(@NonNull OnBothRefreshListener onBothRefreshListener) {
        this.onBothRefreshListener = onBothRefreshListener;
        return this;
    }

    public PtrRecyclerViewBuilder setOnPullDownListener(@NonNull OnPullDownListener onPullDownListener) {
        this.onPullDownListener = onPullDownListener;
        return this;
    }

    public PtrRecyclerViewBuilder setOnLoadMoreListener(@NonNull OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
        return this;
    }

    public PtrRecyclerViewBuilder setDecoration(RecyclerView.ItemDecoration decoration) {
        this.decoration = decoration;
        return this;
    }


    public PtrRecyclerViewBuilder setItemAnimator(RecyclerView.ItemAnimator itemAnimator) {
        this.itemAnimator = itemAnimator;
        return this;
    }

    public void commit(PtrRecyclerView recyclerView) {

        if (null == recyclerView) {
            throw new NullPointerException("recyclerView is null");
        }
        if (null == getLayoutManager()) {
            throw new NullPointerException("layoutManager is null");
        }
        if (null == getRefreshRecyclerViewAdapter()) {
            throw new NullPointerException("refreshRecyclerViewAdapter is null");
        }
        if (null == getPtrMode()) {
            throw new NullPointerException("ptrMode is null");
        }

        getRefreshRecyclerViewAdapter().putLayoutManager(getLayoutManager());
        recyclerView.setAdapter(getRefreshRecyclerViewAdapter());
        recyclerView.setMode(getPtrMode());
        loadMoreRecyclerListener = new LoadMoreRecyclerListener(recyclerView.getRecyclerView(), ptrMode);
        recyclerView.addOnScrollListener(loadMoreRecyclerListener);

        if (PtrMode.BOTH == getPtrMode()) {
            if (null != onBothRefreshListener) {
                recyclerView.setOnBothRefreshListener(onBothRefreshListener);
            }
        } else if (PtrMode.TOP == getPtrMode()) {
            if (null != onPullDownListener) {
                recyclerView.setOnPullDownListener(onPullDownListener);
            }
        } else if (PtrMode.BOTTOM == getPtrMode()) {
            if (null != onLoadMoreListener) {
                recyclerView.setOnLoadMoreListener(onLoadMoreListener);
            }
        }
        recyclerView.addItemDecoration(getDecoration());
        recyclerView.setItemAnimator(getItemAnimator());
        recyclerView.setLayoutManager(getLayoutManager());
        recyclerView.setFooterView(getPtrLoadingMoreFooterView());
        recyclerView.setPtrHeaderView(getPtrPullHeaderView());
    }

}
