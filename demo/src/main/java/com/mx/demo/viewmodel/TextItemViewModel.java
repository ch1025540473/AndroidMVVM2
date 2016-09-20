package com.mx.demo.viewmodel;

import android.util.Log;

import com.mx.demo.event.RemoveTxtEvent;
import com.mx.demo.viewmodel.viewbean.ItemViewBean;
import com.mx.demo.viewmodel.viewbean.TextItemViewBean;
import com.mx.framework2.viewmodel.RecyclerItemViewModel;
import com.mx.framework2.viewmodel.command.OnClickCommand;

/**
 * Created by chenbaocheng on 16/8/14.
 */
public class TextItemViewModel extends RecyclerItemViewModel<ItemViewBean> {
    TextItemViewBean item;

    public OnClickCommand getClickCommand() {
        return new OnClickCommand() {
            @Override
            public void execute(int viewId) {
                Log.d("PTR", "OnClickCommand==> " + item.getText());
                RemoveTxtEvent removeTxtEvent = new RemoveTxtEvent();
                removeTxtEvent.setId(item.getId());
                postEvent(removeTxtEvent);
            }
        };
    }

    private String textToShow = "default content";

    @Override
    protected void onItemChange(ItemViewBean oldItem, ItemViewBean item) {
        this.item = (TextItemViewBean) item;
        textToShow = ((TextItemViewBean) item).getText();
        if (((TextItemViewBean) item).isUpperCase()) {
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
