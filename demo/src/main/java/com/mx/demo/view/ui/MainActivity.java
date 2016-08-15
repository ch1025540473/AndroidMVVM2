package com.mx.demo.view.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mx.demo.DemoModule;
import com.mx.demo.R;
import com.mx.demo.databinding.ActivityMainBinding;
import com.mx.demo.model.DemoUseCase;
import com.mx.demo.model.bean.ApiBean;
import com.mx.demo.viewmodel.viewbean.ColorItemViewBean;
import com.mx.demo.viewmodel.viewbean.ItemViewBean;
import com.mx.demo.viewmodel.viewbean.TextItemViewBean;
import com.mx.framework2.view.ViewModelRecyclerView;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // 翻译数据
        List<ApiBean> apiBeans = DemoModule.get().getUserCaseManager().obtainUseCase(DemoUseCase.class).queryBeanList();
        List<ItemViewBean> viewBeans = new LinkedList<>();
        for (ApiBean apiBean : apiBeans) {
            if (apiBean.type == 1) {
                ColorItemViewBean viewBean = new ColorItemViewBean();
                viewBean.setColor(apiBean.color);
                viewBeans.add(viewBean);
            } else if (apiBean.type == 2) {
                TextItemViewBean viewBean = new TextItemViewBean();
                viewBean.setText(apiBean.content);
                viewBean.setUpperCase(apiBean.isTitle);
                viewBeans.add(viewBean);
            }
        }
        // 业务bean -> main view model -> view beans -> recyclerview -> item view model -> item view bean
        // 绑定数据和ItemView工厂
        ViewModelRecyclerView recyclerView = mainBinding.demoMylist;
        recyclerView.setItemViewFactory("com.mx.demo.view.factory.DemoItemViewFactory");
        recyclerView.setItems(viewBeans);
    }
}
