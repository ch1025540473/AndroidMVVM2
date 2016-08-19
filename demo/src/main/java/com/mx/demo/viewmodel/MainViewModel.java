package com.mx.demo.viewmodel;

import android.os.Bundle;

import com.mx.demo.DemoModule;
import com.mx.demo.model.DemoUseCase;
import com.mx.demo.model.bean.ApiBean;
import com.mx.demo.view.factory.DemoItemViewFactory;
import com.mx.demo.viewmodel.viewbean.ColorItemViewBean;
import com.mx.demo.viewmodel.viewbean.ItemViewBean;
import com.mx.demo.viewmodel.viewbean.TextItemViewBean;
import com.mx.framework2.viewmodel.ViewModel;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by chenbaocheng on 16/8/18.
 */
public class MainViewModel extends ViewModel {
    private List<ItemViewBean> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 翻译数据
        List<ApiBean> apiBeans = DemoModule.get().getUserCaseManager().obtainUseCase(DemoUseCase.class, getActivity()).queryBeanList();
        items = new LinkedList<>();
        for (ApiBean apiBean : apiBeans) {
            if (apiBean.type == 1) {
                ColorItemViewBean viewBean = new ColorItemViewBean();
                viewBean.setColor(apiBean.color);
                items.add(viewBean);
            } else if (apiBean.type == 2) {
                TextItemViewBean viewBean = new TextItemViewBean();
                viewBean.setText(apiBean.content);
                viewBean.setUpperCase(apiBean.isTitle);
                items.add(viewBean);
            }
        }
        // 业务bean -> main view model -> view beans -> recyclerview -> item view model -> item view bean

        notifyChange();
    }

    public List<ItemViewBean> getItems() {
        return items;
    }
}
