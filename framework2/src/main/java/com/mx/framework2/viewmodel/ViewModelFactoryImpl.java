package com.mx.framework2.viewmodel;

import android.content.Context;

import com.mx.engine.utils.CheckUtils;
import com.mx.engine.utils.ObjectUtils;
import com.mx.framework2.Module;
import com.mx.framework2.R;
import com.mx.framework2.view.ui.BaseActivity;
import com.mx.framework2.view.ui.BaseFragment;
import com.mx.framework2.view.ui.RunState;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by liuyuxuan on 16/6/6.
 */
public class ViewModelFactoryImpl implements ViewModelFactory {
    private Module module;
    private Map<String, LifecycleViewModel> viewModelPool = new WeakHashMap<>(10, 1);

    public ViewModelFactoryImpl(Module module) {
        this.module = module;
    }

    public static <T> T newInstance(Class<T> cls, Object defaultParam) {
        T value = ObjectUtils.newInstance(cls, defaultParam);
        if (value == null) {
            value = ObjectUtils.newInstance(cls);
        }

        return value;
    }

    private <T extends LifecycleViewModel> T createViewModel(Context context, ViewModelScope scope, String viewModelId, Class<T> viewModelClass) {
        T value;
        String uniqueId = LifecycleViewModel.generateUniqueId(scope, viewModelId);
        if (viewModelPool.containsKey(uniqueId)) {
            value = (T) viewModelPool.get(viewModelId);
        } else {
            value = newInstance(viewModelClass, scope);
        }

        if (null != value) {
            if (value instanceof ModuleAware) {
                value.setModule(module);
            }

            value.setViewModelScope(scope);
            value.setContext(context);
            value.setId(viewModelId);
        }

        return value;
    }

    @Override
    public <T extends LifecycleViewModel> T createViewModel(String viewModelId, Class<T> viewModelClass, BaseFragment baseFragment) {
        CheckUtils.checkNotNull(viewModelClass);
        CheckUtils.checkNotNull(baseFragment);
        //TODO: R.string.activity_msg means ambiguously
        CheckUtils.checkArgument(baseFragment.getRunState() == RunState.Created, baseFragment.getString(R.string.activity_msg));

        return createViewModel(baseFragment.getContext(), baseFragment, viewModelId, viewModelClass);
    }

    @Override
    public <T extends LifecycleViewModel> T createViewModel(String viewModelId, Class<T> viewModelClass, BaseActivity baseActivity) {
        CheckUtils.checkNotNull(viewModelClass);
        CheckUtils.checkNotNull(baseActivity);
        CheckUtils.checkArgument(baseActivity.getRunState() == RunState.Created, baseActivity.getString(R.string.activity_msg));

        return createViewModel(baseActivity, baseActivity, viewModelId, viewModelClass);
    }
}
