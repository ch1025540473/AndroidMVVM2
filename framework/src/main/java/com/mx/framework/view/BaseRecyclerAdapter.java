
package com.mx.framework.view;

import android.support.v7.widget.RecyclerView;

import com.mx.framework.viewmodel.ViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by chenbaocheng on 16/5/5.
 */
public abstract class BaseRecyclerAdapter<ItemType> extends RecyclerView.Adapter {
    protected final BaseActivity activity;
    private final List<ItemType> items;
    protected ViewModel viewModel;

    public BaseRecyclerAdapter(ViewModel viewModel) {
        this.viewModel = viewModel;
        this.activity = viewModel.getActivity();
        this.items = new ArrayList<>();
    }

    protected final ViewModel getViewModel() {
        return viewModel;
    }

    public int getCount() {
        return items.size();
    }

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

    public void removeItem(int position) {
        this.items.remove(position);
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

    public void insertItem(int index, ItemType itemType) {
        this.items.add(index, itemType);
        onDataChange();
    }

    public void removeItems(int start) {

        for (int i = items.size() - 1; i >= start; i--) {
            items.remove(i);
        }

    }

    public void removeItem(ItemType itemType) {
        items.remove(itemType);
    }

    public int indexOf(ItemType itemType) {
        return items.indexOf(itemType);
    }

    public void clear() {
        this.items.clear();
        onDataChange();
    }

    protected BaseActivity getActivity() {
        return this.activity;
    }
}
