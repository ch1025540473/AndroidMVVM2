package com.mx.framework2.view;

import android.databinding.ViewDataBinding;

import com.mx.framework2.viewmodel.ItemViewModel;

/**
 * Created by chenbaocheng on 16/8/14.
 */
public interface ItemViewBindingFactory<ItemType> {
    ViewDataBinding getViewDataBinding(ItemViewModel<ItemType> viewModel);
}
