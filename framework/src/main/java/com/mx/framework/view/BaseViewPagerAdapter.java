package com.mx.framework.view;

import android.support.v4.view.PagerAdapter;

import com.mx.framework.viewmodel.ViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by liuyuxuan on 16/6/2.
 */
public abstract class BaseViewPagerAdapter<ItemType> extends PagerAdapter {

    protected final BaseActivity activity;
    private final List<ItemType> items;
    protected final ViewModel viewModel;


    public BaseViewPagerAdapter(ViewModel viewModel) {
        this.viewModel = viewModel;
        this.activity = viewModel.getActivity();
        this.items = new ArrayList<>();
    }

    protected final ViewModel getViewModel() {
        return viewModel;
    }
    @Override
    public int getCount() {
        return this.items.size();
    }

    public ItemType getItem(int position) {
        return items.get(position);
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

    public void addItems(int position, Collection<ItemType> items) {
        this.items.addAll(position, items);
        onDataChange();
    }

    public void insertItem(int index, ItemType itemType) {
        this.items.add(index, itemType);
        onDataChange();
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
