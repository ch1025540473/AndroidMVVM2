package com.mx.demo.viewmodel;

import com.mx.demo.event.RemoveTxtEvent;
import com.mx.demo.viewmodel.viewbean.ChildColorItemViewBean;
import com.mx.demo.viewmodel.viewbean.ChildItemViewBean;
import com.mx.demo.viewmodel.viewbean.ChildTextItemViewBean;
import com.mx.framework2.viewmodel.RecyclerItemViewModel;
import com.mx.framework2.viewmodel.command.OnClickCommand;

/**
 * Created by liuyuxuan on 16/8/24.
 */
public class ChildTextItemViewModel extends RecyclerItemViewModel<ChildItemViewBean> {

    ChildTextItemViewBean bean;

    public OnClickCommand getCommand() {
        return new OnClickCommand() {
            @Override
            public void execute(int viewId) {
                postEvent(new RemoveTxtEvent());
            }
        };
    }

    public String getChildText() {
        return bean == null ? null : bean.getText();
    }

    @Override
    protected void onItemChange(ChildItemViewBean oldItem, ChildItemViewBean item) {
        bean = (ChildTextItemViewBean) item;
        notifyChange();
    }
}
