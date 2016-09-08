package com.mx.framework.viewmodel;

import android.databinding.ViewDataBinding;

/**
 * Created by liuyuxuan on 16/6/25.
 */
@Deprecated
public abstract class ListItemViewModel<DataBindingType extends ViewDataBinding, ItemType> extends AbsItemViewModel<DataBindingType, ItemType> {
    public ListItemViewModel() {
        super();
    }
}
