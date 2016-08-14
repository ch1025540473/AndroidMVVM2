package com.mx.framework2.view;

import android.databinding.ViewDataBinding;

import com.mx.framework2.viewmodel.ItemViewModel;

/**
 * Created by chenbaocheng on 16/8/11.
 */
public abstract class ItemViewFactory<ItemType> implements ItemViewModelFactory<ItemType>,ItemViewBindingFactory<ItemType> {
}
