package com.mx.framework2.view.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.mx.framework2.view.factory.ItemViewFactory;
import com.mx.framework2.viewmodel.RecyclerItemViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyuxuan on 16/5/17.
 */
public class ViewModelRecyclerViewAdapter extends BaseRecyclerAdapter {
    private final List<Class<?>> viewModelTypes;
    private ItemViewFactory itemViewFactory;

    public ViewModelRecyclerViewAdapter(Context context) {
        super(context);
        this.viewModelTypes = new ArrayList<>();
        setHasStableIds(false);
    }

    @Override
    protected final void onDataChange() {
        Log.d("onDataChange", "notifyDataSetChanged");
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("unchecked")
    protected RecyclerItemViewModel getViewModel(int position) {
        RecyclerItemViewModel vm = (RecyclerItemViewModel) itemViewFactory.getViewModel(getItem(position));
        return vm;
    }

    @Override
    public final int getItemViewType(int position) {
        RecyclerItemViewModel vm = getViewModel(position);
        Class<?> type = vm.getClass();
        int index = viewModelTypes.indexOf(type);
        if (index == -1) {
            viewModelTypes.add(type);
            index = viewModelTypes.indexOf(type);
        }

        return index;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerItemViewModel recyclerItemViewModel;
        ViewDataBinding viewDataBinding;

        public ViewHolder(RecyclerItemViewModel recyclerItemViewModel, ViewDataBinding viewDataBinding) {
            super(viewDataBinding.getRoot());
            this.recyclerItemViewModel = recyclerItemViewModel;
            this.viewDataBinding = viewDataBinding;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerItemViewModel vm = (RecyclerItemViewModel) itemViewFactory.obtainViewModel(viewModelTypes.get(viewType));
        return new ViewHolder(vm, itemViewFactory.getViewDataBinding(vm));
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        RecyclerItemViewModel recyclerItemViewModel = viewHolder.recyclerItemViewModel;
        recyclerItemViewModel.setItem(getItem(position));
        viewHolder.viewDataBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return getCount();
    }

    public ItemViewFactory getItemViewFactory() {
        return itemViewFactory;
    }

    public void setItemViewFactory(ItemViewFactory itemViewFactory) {
        this.itemViewFactory = itemViewFactory;
    }
}
