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

    @BindingAdapter(value = {"layoutManager", "items", "footerClassName", "headerClassName", "itemViewFactory", "proxy"}, requireAll = true)
    public static void buildPTR(PullToRefreshRecyclerView pullToRefreshRecyclerView,
                                LayoutManagers.LayoutManagerFactory layoutManagerFactory,
                                Collection items,
                                String footViewClassName,
                                String headViewClassName,
                                String itemViewFactoryClassName,
                                PTRRecyclerViewProxy proxy) {
        Log.d("PTR", "buildPTR= ======");

        RecyclerView recyclerView = pullToRefreshRecyclerView.getRefreshableView();
        recyclerView.setLayoutManager(layoutManagerFactory.create(recyclerView));
        pullToRefreshRecyclerView.setHeaderClassName(headViewClassName);
        pullToRefreshRecyclerView.setFooterClassName(footViewClassName);
        pullToRefreshRecyclerView.setItemViewFactory(itemViewFactoryClassName);
        pullToRefreshRecyclerView.setItems(items);
        pullToRefreshRecyclerView.setProxy(proxy);
    }


}
