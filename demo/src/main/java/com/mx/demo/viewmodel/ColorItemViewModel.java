package com.mx.demo.viewmodel;

import android.graphics.Color;

import com.mx.demo.event.RemoveTxtEvent;
import com.mx.demo.viewmodel.viewbean.ColorItemViewBean;
import com.mx.demo.viewmodel.viewbean.ItemViewBean;
import com.mx.framework2.viewmodel.RecyclerItemViewModel;
import com.mx.framework2.widget.ClickCommand;

/**
 * Created by chenbaocheng on 16/8/14.
 */
public class ColorItemViewModel extends RecyclerItemViewModel<ItemViewBean> {
    ItemViewBean item;

    public ClickCommand getClickCommand() {
        return new ClickCommand() {
            @Override
            public void execute(int viewId) {
                RemoveTxtEvent removeTxtEvent = new RemoveTxtEvent();
                removeTxtEvent.setId(item.getId());
                postEvent(removeTxtEvent);
            }
        };
    }

    private int color = Color.BLACK;

    @Override
    protected void onItemChange(ItemViewBean oldItem, ItemViewBean item) {
        this.item = item;
        if (((ColorItemViewBean) item).getColor() != null) {
            switch (((ColorItemViewBean) item).getColor()) {
                case "red":
                    color = Color.RED;
                    break;
                case "yellow":
                    color = Color.YELLOW;
                    break;
                case "blue":
                    color = Color.BLUE;
                    break;
                default:
                    color = Color.WHITE;
            }
            notifyChange();
        }
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
