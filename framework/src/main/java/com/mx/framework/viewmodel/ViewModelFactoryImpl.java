package com.mx.framework.viewmodel;

import android.support.annotation.NonNull;

import com.mx.engine.utils.CheckUtils;
import com.mx.engine.utils.ObjectUtils;
import com.mx.framework.Module;
import com.mx.framework.R;
import com.mx.framework.view.BaseActivity;
import com.mx.framework.view.BaseFragment;
import com.mx.framework.view.RunState;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by liuyuxuan on 16/6/6.
 */
@Deprecated
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
    public <T extends ViewModel> T createViewModel(@NonNull Class<T> viewModelClassType, @NonNull BaseFragment baseFragment) {
        CheckUtils.checkNotNull(viewModelClassType);
        CheckUtils.checkNotNull(baseFragment);
        CheckUtils.checkArgument(baseFragment.getRunState() == RunState.Created, baseFragment.getString(R.string.activity_msg));
        T value = newInstance(viewModelClassType, baseFragment);
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
        T value = newInstance(viewModelClassType, baseActivity);

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
