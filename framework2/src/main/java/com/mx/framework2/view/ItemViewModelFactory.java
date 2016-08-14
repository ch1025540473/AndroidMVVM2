package com.mx.framework2.view;

import com.mx.framework2.viewmodel.ItemViewModel;

/**
 * Created by chenbaocheng on 16/8/14.
 */
public interface ItemViewModelFactory<ItemType> {
    ItemViewModel<ItemType> getViewModel(ItemType item);
}
