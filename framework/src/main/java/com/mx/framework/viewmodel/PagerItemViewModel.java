package com.mx.framework.viewmodel;

import android.databinding.ViewDataBinding;

/**
 * Created by liuyuxuan on 16/6/2.
 */
@Deprecated
public abstract class PagerItemViewModel<DataBindingType extends ViewDataBinding, ItemType> extends AbsItemViewModel<DataBindingType, ItemType> {

    public PagerItemViewModel() {
        super();
    }

}
