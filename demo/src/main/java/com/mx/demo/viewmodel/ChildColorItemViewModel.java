package com.mx.demo.viewmodel;

import android.graphics.Color;

import com.mx.demo.viewmodel.viewbean.ChildItemViewBean;
import com.mx.demo.viewmodel.viewbean.ColorItemViewBean;
import com.mx.framework2.viewmodel.RecyclerItemViewModel;

/**
 * Created by liuyuxuan on 16/8/24.
 */
public class ChildColorItemViewModel extends RecyclerItemViewModel<ChildItemViewBean> {

    private int color = Color.BLACK;

    @Override
    protected void onItemChange(ChildItemViewBean oldItem, ChildItemViewBean item) {

    }

    public int getColor() {
        return color;
    }
}
