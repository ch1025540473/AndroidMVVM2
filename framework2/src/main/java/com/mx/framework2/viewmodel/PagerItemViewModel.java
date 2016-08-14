package com.mx.framework2.viewmodel;

import android.databinding.ViewDataBinding;

/**
 * Created by liuyuxuan on 16/6/2.
 */
public abstract class PagerItemViewModel<DataBindingType extends ViewDataBinding, ItemType> extends AbsItemViewModel<DataBindingType, ItemType> {

    public PagerItemViewModel() {
        super();
    }

}
