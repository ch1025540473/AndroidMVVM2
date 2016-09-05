package com.mx.demo.view.factory;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.databinding.tool.util.L;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mx.demo.R;
import com.mx.demo.databinding.ListitemChildListBinding;
import com.mx.demo.databinding.ListitemColorBinding;
import com.mx.demo.databinding.ListitemTextBinding;
import com.mx.demo.viewmodel.ChildListViewModel;
import com.mx.demo.viewmodel.ColorItemViewModel;
import com.mx.demo.viewmodel.TextItemViewModel;
import com.mx.demo.viewmodel.viewbean.ChildItemViewBean;
import com.mx.demo.viewmodel.viewbean.ChildListViewBean;
import com.mx.demo.viewmodel.viewbean.ColorItemViewBean;
import com.mx.demo.viewmodel.viewbean.ItemViewBean;
import com.mx.demo.viewmodel.viewbean.TextItemViewBean;
import com.mx.framework2.view.factory.ItemViewFactory;
import com.mx.framework2.viewmodel.AbsItemViewModel;
import com.mx.framework2.viewmodel.ViewModel;
import com.mx.framework2.widget.ClickCommand;

/**
 * Created by chenbaocheng on 16/8/14.
 */
public class DemoItemViewFactory extends ItemViewFactory<ItemViewBean> {

    public static String getClassName() {
        return DemoItemViewFactory.class.getName();
    }

    @Override
    public Class<? extends AbsItemViewModel> getViewModelType(ItemViewBean item) {

        if (item instanceof ColorItemViewBean) {
            return ColorItemViewModel.class;
        } else if (item instanceof TextItemViewBean) {
            return TextItemViewModel.class;
        } else if (item instanceof ChildListViewBean) {
            return ChildListViewModel.class;
        }
        return null;
    }

    @Override
    public ViewDataBinding createViewDataBinding(AbsItemViewModel<ItemViewBean> viewModel) {

        if (viewModel instanceof ColorItemViewModel) {
            ListitemColorBinding binding = inflate(R.layout.listitem_color);
            binding.setModel((ColorItemViewModel) viewModel);
            return binding;
        } else if (viewModel instanceof TextItemViewModel) {
            ListitemTextBinding binding = inflate(R.layout.listitem_text);
            binding.setModel((TextItemViewModel) viewModel);
            return binding;
        } else if (viewModel instanceof ChildListViewModel) {
            ListitemChildListBinding binding = inflate(R.layout.listitem_child_list);
            binding.setModel((ChildListViewModel) viewModel);
            return binding;
        }

        return null;
    }

}
