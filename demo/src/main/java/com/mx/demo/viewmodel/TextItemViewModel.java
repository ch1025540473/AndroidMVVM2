package com.mx.demo.viewmodel;

import com.mx.demo.BR;
import com.mx.demo.viewmodel.viewbean.ItemViewBean;
import com.mx.demo.viewmodel.viewbean.TextItemViewBean;
import com.mx.framework2.viewmodel.RecyclerItemViewModel;

/**
 * Created by chenbaocheng on 16/8/14.
 */
public class TextItemViewModel extends RecyclerItemViewModel<ItemViewBean> {
    private String textToShow = "default content";

    @Override
    protected void onItemChange(ItemViewBean oldItem, ItemViewBean item) {
        textToShow = ((TextItemViewBean)item).getText();
        if(((TextItemViewBean)item).isUpperCase()){
            textToShow = textToShow.toUpperCase();
        }
        notifyChange();
    }

    public String getTextToShow() {
        return textToShow;
    }

    public void setTextToShow(String textToShow) {
        this.textToShow = textToShow;
    }
}
