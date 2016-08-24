package com.mx.demo.viewmodel.viewbean;

import java.util.List;

/**
 * Created by liuyuxuan on 16/8/24.
 */
public class ChildListViewBean extends ItemViewBean {

    List<ChildItemViewBean> childItemViewBeanList;

    public List<ChildItemViewBean> getChildItemViewBeanList() {
        return childItemViewBeanList;
    }

    public void setChildItemViewBeanList(List<ChildItemViewBean> childItemViewBeanList) {
        this.childItemViewBeanList = childItemViewBeanList;
    }


}
