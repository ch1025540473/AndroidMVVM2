package com.mx.framework.viewmodel;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by chenbaocheng on 16/5/5.
 */
public abstract class AbsItemViewModel<DataBindingType extends ViewDataBinding, ItemType> extends ViewModel {

    private final Map<View, ItemType> viewDataMapping;

    public AbsItemViewModel() {
        super();
        this.viewDataMapping = new WeakHashMap<>();
    }

    public final void updateView(DataBindingType dataBinding, ItemType item) {

        this.viewDataMapping.put(dataBinding.getRoot(), item);
        onUpdateView(dataBinding, item);
    }

    protected final ItemType getItemByView(View view) {

        return viewDataMapping.get(view);
    }


    protected final DataBindingType findDataBindingByView(View view) {

        return DataBindingUtil.findBinding(view);

    }


    public abstract DataBindingType createViewDataBinding();

    protected abstract void onUpdateView(DataBindingType dataBinding, ItemType item);

    @Override
    protected void onLoadData() {

    }
}
