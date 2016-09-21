package com.mx.framework2.view.adapter;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.recyclerview.WrapRecyclerView;
import com.mx.framework2.viewmodel.command.OnLoadMoreCommand;
import com.mx.framework2.viewmodel.command.OnPullDownCommand;
import com.mx.framework2.viewmodel.command.OnStartRefreshingCommand;
import com.mx.framework2.view.FooterLoadingView;
import com.mx.framework2.view.LayoutManagers;
import com.mx.framework2.view.PullToRefreshRecyclerView;
import com.mx.framework2.viewmodel.proxy.PTRRecyclerViewProxy;

import java.util.Collection;

/**
 * Created by liuyuxuan on 16/8/22.
 */
public class PullToRefreshRecyclerViewDataBindingAdapters {
    @BindingAdapter({"layoutManager"})
    public static void setLayoutManager(PullToRefreshRecyclerView pullToRefreshRecyclerView, LayoutManagers.LayoutManagerFactory layoutManagerFactory) {
        Log.d("PTR", "buildPTR= ====== setLayoutManager");
        RecyclerView recyclerView = pullToRefreshRecyclerView.getRefreshableView();
        recyclerView.setLayoutManager(layoutManagerFactory.create(recyclerView));
    }

}
