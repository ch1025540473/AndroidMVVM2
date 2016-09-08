package com.mx.framework.view;

import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;

import com.mx.framework.viewmodel.ListItemViewModel;
import com.mx.framework.viewmodel.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenbaocheng on 16/5/5.
 */
@Deprecated
public abstract class ViewModelListAdapter<ItemType> extends BaseListAdapter<ItemType> {


    private final List<ListItemViewModel<?, ItemType>> viewModelTypes;
    int countViewType;

    public ViewModelListAdapter(ViewModel viewModel, int count) {
        super(viewModel);
        this.viewModelTypes = new ArrayList<>();
        this.countViewType = count;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemViewModel viewModel = getViewModelType(position);
        ViewDataBinding binding;
        if (convertView == null) {
            binding = viewModel.createViewDataBinding();
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (ViewDataBinding) convertView.getTag();
        }
        viewModel.updateView(binding, getItem(position));

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return viewModelTypes.indexOf(getViewModelType(position));
    }

    @Override
    public int getViewTypeCount() {
        return countViewType;
    }

    protected abstract ListItemViewModel<?, ItemType> getViewModelType(int position);

    @Override
    protected void onDataChange() {
        viewModelTypes.clear();
        for (int i = 0; i < getCount(); ++i) {
            ListItemViewModel<?, ItemType> type = getViewModelType(i);
            if (type != null && !viewModelTypes.contains(type)) {
                viewModelTypes.add(type);
            }
        }
        notifyDataSetChanged();
    }

}
