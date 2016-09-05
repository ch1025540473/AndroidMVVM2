package com.mx.demo.view.factory;

import android.databinding.ViewDataBinding;
import android.util.Log;

import com.mx.demo.R;
import com.mx.demo.databinding.ListitemChildColorBinding;
import com.mx.demo.databinding.ListitemChildTextBinding;
import com.mx.demo.viewmodel.ChildColorItemViewModel;
import com.mx.demo.viewmodel.ChildTextItemViewModel;
import com.mx.demo.viewmodel.viewbean.ChildColorItemViewBean;
import com.mx.demo.viewmodel.viewbean.ChildItemViewBean;
import com.mx.demo.viewmodel.viewbean.ChildTextItemViewBean;
import com.mx.framework2.view.factory.ItemViewFactory;
import com.mx.framework2.viewmodel.AbsItemViewModel;

/**
 * Created by liuyuxuan on 16/8/24.
 */
public class ChildItemViewFactory extends ItemViewFactory<ChildItemViewBean> {

    public static String getClassName() {
        return ChildItemViewFactory.class.getName();
    }

    @Override
    protected Class<? extends AbsItemViewModel> getViewModelType(ChildItemViewBean item) {
        Log.d("ChildItemView", "getViewModelType=" + item.getClass());
        if (item instanceof ChildTextItemViewBean) {
            return ChildTextItemViewModel.class;
        } else if (item instanceof ChildColorItemViewBean) {
            return ChildColorItemViewModel.class;
        }
        return null;
    }

    @Override
    protected ViewDataBinding createViewDataBinding(AbsItemViewModel<ChildItemViewBean> viewModel) {
        if (viewModel instanceof ChildColorItemViewModel) {
            ListitemChildColorBinding childColorBinding = inflate(R.layout.listitem_child_color);
            childColorBinding.setModel((ChildColorItemViewModel) viewModel);
            return childColorBinding;
        } else if (viewModel instanceof ChildTextItemViewModel) {
            ListitemChildTextBinding childTextBinding = inflate(R.layout.listitem_child_text);
            childTextBinding.setModel((ChildTextItemViewModel) viewModel);
            return childTextBinding;
        }

        return null;
    }
}
