package com.mx.demo.viewmodel;

import com.mx.demo.viewmodel.viewbean.ChildItemViewBean;
import com.mx.demo.viewmodel.viewbean.ChildListViewBean;
import com.mx.demo.viewmodel.viewbean.ItemViewBean;
import com.mx.framework2.viewmodel.RecyclerItemViewModel;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by liuyuxuan on 16/8/24.
 */
public class ChildListViewModel extends RecyclerItemViewModel<ItemViewBean> {
    List<ChildItemViewBean> childListViewBeanList = new LinkedList<>();

    public List<ChildItemViewBean> getChildList() {
        return childListViewBeanList;
    }

    @Override
    protected void onItemChange(ItemViewBean oldItem, ItemViewBean item) {
        childListViewBeanList = ((ChildListViewBean) item).getChildItemViewBeanList();
        notifyChange();
    }
}
