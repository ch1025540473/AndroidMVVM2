package com.mx.framework2.viewmodel;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;

import com.mx.framework2.event.BroadcastEvent;
import com.mx.framework2.view.DataBindingFactory;

/**
 * Created by chenbaocheng on 16/5/5.
 */
public abstract class AbsItemViewModel<ItemType> extends ViewModel {
    private View view;

    public void setView(View view) {
        this.view = view;
    }

    @Override
    public final <T extends BroadcastEvent> void postEvent(T event) {
        View v = view;
        if(v == null){
            super.postEvent(event);
            return;
        }

        ViewDataBinding binding = DataBindingUtil.findBinding((View)v.getParent());
        if(binding == null){
            super.postEvent(event);
            return;
        }

        for (ViewModel viewModel : DataBindingFactory.getViewModelsFromDataBinding(binding)) {
            viewModel.postEvent(event);
            return;
        }

        super.postEvent(event);
    }

    private ItemType item = null;

    protected abstract void onItemChange(ItemType oldItem, ItemType item);

    public final void setItem(ItemType item) {
        ItemType oldItem = this.item;
        this.item = item;

        onItemChange(oldItem, item);
    }

    public final ItemType getItem() {
        return item;
    }
}
