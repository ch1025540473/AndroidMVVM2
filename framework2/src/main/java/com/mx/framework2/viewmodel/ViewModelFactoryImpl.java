package com.mx.framework2.viewmodel;

import android.support.annotation.NonNull;

import com.mx.engine.utils.CheckUtils;
import com.mx.engine.utils.ObjectUtils;
import com.mx.framework2.Module;
import com.mx.framework2.R;
import com.mx.framework2.view.ui.BaseActivity;
import com.mx.framework2.view.ui.BaseFragment;
import com.mx.framework2.view.ui.RunState;

/**
 * Created by liuyuxuan on 16/6/6.
 */
public class ViewModelFactoryImpl implements ViewModelFactory {

    Module module;

    public ViewModelFactoryImpl(Module module) {
        this.module = module;

    }

    public static <T> T newInstance(Class<T> cls, Object defaultParam){
        T value = ObjectUtils.newInstance(cls, defaultParam);
        if(value == null){
            value = ObjectUtils.newInstance(cls);
        }

        return value;
    }

    @Override
    public <T extends LifecycleViewModel> T createViewModel(@NonNull Class<T> viewModelClassType, @NonNull BaseFragment baseFragment) {
        CheckUtils.checkNotNull(viewModelClassType);
        CheckUtils.checkNotNull(baseFragment);
        CheckUtils.checkArgument(baseFragment.getRunState() == RunState.Created, baseFragment.getString(R.string.activity_msg));
        T value = newInstance(viewModelClassType, baseFragment);
        if (null != value) {
            if (value instanceof ModuleAware) {
                value.setModule(module);
            }
            if (baseFragment instanceof ViewModelScope) {
                value.setViewModelScope(baseFragment);
            }
            value.setContext(baseFragment.getContext());
        }

        return value;
    }

    @Override
    public <T extends LifecycleViewModel> T createViewModel(Class<T> viewModelClassType, BaseActivity baseActivity) {
        CheckUtils.checkNotNull(baseActivity);
        CheckUtils.checkArgument(baseActivity.getRunState() == RunState.Created, baseActivity.getString(R.string.activity_msg));
        CheckUtils.checkNotNull(viewModelClassType);
        T value = newInstance(viewModelClassType, baseActivity);

        if (null != value) {
            if (value instanceof ModuleAware) {
                value.setModule(module);
            }

            if (baseActivity instanceof ViewModelScope) {
                value.setViewModelScope(baseActivity);
            }

            value.setContext(baseActivity);
        }
        return value;
    }
}
