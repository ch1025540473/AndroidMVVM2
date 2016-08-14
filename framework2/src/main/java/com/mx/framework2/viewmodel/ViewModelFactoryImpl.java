package com.mx.framework2.viewmodel;

import android.support.annotation.NonNull;

import com.mx.engine.utils.CheckUtils;
import com.mx.engine.utils.ObjectUtils;
import com.mx.framework2.Module;
import com.mx.framework2.R;
import com.mx.framework2.view.BaseActivity;
import com.mx.framework2.view.BaseFragment;
import com.mx.framework2.view.RunState;

/**
 * Created by liuyuxuan on 16/6/6.
 */
public class ViewModelFactoryImpl implements ViewModelFactory {

    Module module;

    public ViewModelFactoryImpl(Module module) {
        this.module = module;

    }

    @Override
    public <T extends ViewModel> T createViewModel(@NonNull Class<T> viewModelClassType, @NonNull BaseFragment baseFragment) {
        CheckUtils.checkNotNull(viewModelClassType);
        CheckUtils.checkNotNull(baseFragment);
        CheckUtils.checkArgument(baseFragment.getRunState() == RunState.Created, baseFragment.getString(R.string.activity_msg));
        T value = ObjectUtils.newInstance(viewModelClassType, baseFragment);
        if (null != value) {
            if (value instanceof ModuleAware) {
                value.setModule(module);
            }
            if (value instanceof ActivityAware) {
                value.setActivity((BaseActivity) baseFragment.getActivity());
            }
            if (value instanceof ViewModelManagerAware) {
                value.setViewModelManager(baseFragment.getViewModelManager());
            }
            value.setContext(baseFragment.getContext());
        }
        return value;
    }

    @Override
    public <T extends ViewModel> T createViewModel(Class<T> viewModelClassType, BaseActivity baseActivity) {
        CheckUtils.checkNotNull(baseActivity);
        CheckUtils.checkArgument(baseActivity.getRunState() == RunState.Created, baseActivity.getString(R.string.activity_msg));
        CheckUtils.checkNotNull(viewModelClassType);
        T value = ObjectUtils.newInstance(viewModelClassType, baseActivity);

        if (null != value) {
            if (value instanceof ModuleAware) {
                value.setModule(module);
            }

            if (value instanceof ActivityAware) {
                value.setActivity(baseActivity);
            }

            if (value instanceof ViewModelManagerAware) {
                value.setViewModelManager(baseActivity.getViewModelManager());
            }
            value.setContext(baseActivity);
        }
        return value;
    }
}
