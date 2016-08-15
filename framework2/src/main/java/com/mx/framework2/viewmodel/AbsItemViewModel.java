package com.mx.framework2.viewmodel;

/**
 * Created by chenbaocheng on 16/5/5.
 */
public abstract class AbsItemViewModel<ItemType> extends ViewModel {

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
