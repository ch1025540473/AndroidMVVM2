package com.mx.framework2.viewmodel;

import android.databinding.ViewDataBinding;

/**
 * Created by liuyuxuan on 16/6/7.
 */
public abstract class IncludeViewModel<DataBindingType extends ViewDataBinding> extends ViewModel {

    private DataBindingType dataBinding;

    public IncludeViewModel() {
        super();
    }

    public final void setDataBinding(DataBindingType dataBinding) {
        this.dataBinding = dataBinding;
    }

    protected final DataBindingType getDataBinding() {
        return dataBinding;
    }

}
