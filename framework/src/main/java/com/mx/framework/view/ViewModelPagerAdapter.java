package com.mx.framework.view;

import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;

import com.mx.framework.viewmodel.PagerItemViewModel;
import com.mx.framework.viewmodel.ViewModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuyuxuan on 16/6/2.
 */
@Deprecated
public abstract class ViewModelPagerAdapter<ItemType> extends BaseViewPagerAdapter<ItemType> {

    private class ItemView {
        PagerItemViewModel itemViewModel;
        ViewDataBinding viewDataBinding;
    }

    Map<Integer, ItemView> buffer;


    public ViewModelPagerAdapter(ViewModel viewModel) {
        super(viewModel);
        buffer = new HashMap<>();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ItemView itemView = buffer.get(position);

        if (null == itemView) {
            PagerItemViewModel itemViewModel = getViewModelType(position);
            itemView = new ItemView();
            itemView.itemViewModel = itemViewModel;
            itemView.viewDataBinding = itemViewModel.createViewDataBinding();
        }

        itemView.itemViewModel.updateView(itemView.viewDataBinding, getItem(position));
        container.addView(itemView.viewDataBinding.getRoot());
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ItemView itemView = (ItemView) object;
        container.removeView(itemView.viewDataBinding.getRoot());
        buffer.remove(object);
        super.destroyItem(container, position, object);
    }


    @Override
    protected final void onDataChange() {

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        ItemView itemView = (ItemView) object;
        return view == itemView.viewDataBinding.getRoot();
    }

    protected abstract PagerItemViewModel<?, ItemType> getViewModelType(int position);


}
