package com.mx.demo.view.factory;

import android.databinding.ViewDataBinding;

import com.mx.demo.R;
import com.mx.demo.databinding.ListitemColorBinding;
import com.mx.demo.databinding.ListitemTextBinding;
import com.mx.demo.viewmodel.ColorItemViewModel;
import com.mx.demo.viewmodel.TextItemViewModel;
import com.mx.demo.viewmodel.viewbean.ColorItemViewBean;
import com.mx.demo.viewmodel.viewbean.ItemViewBean;
import com.mx.demo.viewmodel.viewbean.TextItemViewBean;
import com.mx.framework2.view.factory.ItemViewFactory;
import com.mx.framework2.viewmodel.AbsItemViewModel;

/**
 * Created by chenbaocheng on 16/8/14.
 */
public class DemoItemViewFactory extends ItemViewFactory<ItemViewBean> {
    @Override
    public Class<? extends AbsItemViewModel> getViewModelType(ItemViewBean item) {
        if (item instanceof ColorItemViewBean) {
            return ColorItemViewModel.class;
        } else if (item instanceof TextItemViewBean){
            return TextItemViewModel.class;
        }
        return null;
    }

    @Override
    public ViewDataBinding createViewDataBinding(AbsItemViewModel<ItemViewBean> viewModel) {
        if (viewModel instanceof ColorItemViewModel) {
            ListitemColorBinding binding = inflate(R.layout.listitem_color);
            binding.setModel((ColorItemViewModel)viewModel);
            return binding;
        } else if (viewModel instanceof TextItemViewModel) {
            ListitemTextBinding binding = inflate(R.layout.listitem_text);
            binding.setModel((TextItemViewModel)viewModel);
            return binding;
        }

        return null;
    }
}
