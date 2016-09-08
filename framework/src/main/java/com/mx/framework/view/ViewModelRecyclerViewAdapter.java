package com.mx.framework.view;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.mx.framework.viewmodel.RecyclerItemViewModel;
import com.mx.framework.viewmodel.ViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by liuyuxuan on 16/5/17.
 */
@Deprecated
public abstract class ViewModelRecyclerViewAdapter<ItemType> extends BaseRecyclerAdapter<ItemType> {

    private final List<RecyclerItemViewModel<?, ItemType>> viewModelTypes;

    public ViewModelRecyclerViewAdapter(ViewModel viewModel) {
        super(viewModel);
        this.viewModelTypes = new ArrayList<>();
        setHasStableIds(true);
    }

    @Override
    protected final void onDataChange() {
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    protected abstract RecyclerItemViewModel<?, ItemType> getViewModelType(int position);

    @Override
    public final int getItemViewType(int position) {
        RecyclerItemViewModel<?, ItemType> items = getViewModelType(position);
        int index = viewModelTypes.indexOf(items);
        if (index == -1) {
            viewModelTypes.add(items);
            index = viewModelTypes.indexOf(items);
        }
        Log.d("index", "index=" + index+ items.getClass().getName());

        return index;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerItemViewModel<?, ItemType> recyclerItemViewModel;
        ViewDataBinding viewDataBinding;

        public ViewHolder(RecyclerItemViewModel<?, ItemType> recyclerItemViewModel, ViewDataBinding viewDataBinding) {
            super(viewDataBinding.getRoot());
            this.recyclerItemViewModel = recyclerItemViewModel;
            this.viewDataBinding = viewDataBinding;
        }
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("index", "viewType=" + viewType);
        RecyclerItemViewModel<?, ItemType> vm = viewModelTypes.get(viewType);
        Log.d("index", "viewType=" + viewType+vm.getClass().getName());
        return new ViewHolder(vm, vm.createViewDataBinding());
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        RecyclerItemViewModel recyclerItemViewModel = viewHolder.recyclerItemViewModel;
        recyclerItemViewModel.updateView(viewHolder.viewDataBinding, getItem(position));
    }

    @Override
    public final int getItemCount() {
        return getCount();
    }

    public void putItems(Collection<ItemType> items) {
//        viewModelTypes.clear();
        super.putItems(items);
    }

    public void clear() {
//        viewModelTypes.clear();
        super.clear();
    }
}
