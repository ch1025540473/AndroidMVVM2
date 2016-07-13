package com.mx.framework.view;

import android.widget.BaseAdapter;

import com.mx.framework.viewmodel.ViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by chenbaocheng on 16/5/5.
 */
public abstract class BaseListAdapter<ItemType> extends BaseAdapter {
    protected final BaseActivity activity;
    protected final ViewModel viewModel;
    private final List<ItemType> items;


    public BaseListAdapter(ViewModel viewModel) {
        this.activity = viewModel.getActivity();
        this.items = new ArrayList<>();
        this.viewModel = viewModel;
    }
    protected final ViewModel getViewModel() {
        return viewModel;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public ItemType getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    protected abstract void onDataChange();

    public void addItem(ItemType item) {
        this.items.add(item);
        onDataChange();
    }

    public void addItems(Collection<ItemType> items) {
        this.items.addAll(items);
        onDataChange();
    }

    public void putItems(Collection<ItemType> items) {
        this.items.clear();
        this.items.addAll(items);
        onDataChange();
    }

    public void removeItem(ItemType itemType) {
        items.remove(itemType);
    }

    public void clear() {
        this.items.clear();
        onDataChange();
    }

    protected BaseActivity getActivity() {
        return this.activity;
    }
}
