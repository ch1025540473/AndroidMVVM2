package com.mx.framework2.viewmodel;

/**
 * Created by chenbaocheng on 16/8/11.
 */
public abstract class ItemViewModel<ItemType> {
    private ItemType item = null;

    protected abstract void onItemChange(ItemType oldItem, ItemType newItem);

    public final void setItem(ItemType item){
        if(item != null && !item.equals(this.item)){
            onItemChange(this.item, item);
        }
    }

    public final ItemType getItem(){
        return item;
    }
}
