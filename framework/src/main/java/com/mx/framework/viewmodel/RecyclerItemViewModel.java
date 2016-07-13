package com.mx.framework.viewmodel;

import android.databinding.ViewDataBinding;

/**
 * Created by chenbaocheng on 16/5/5.
 */
public abstract class RecyclerItemViewModel<DataBindingType extends ViewDataBinding, ItemType> extends AbsItemViewModel<DataBindingType, ItemType> {


    public RecyclerItemViewModel() {
        super();
    }
}
