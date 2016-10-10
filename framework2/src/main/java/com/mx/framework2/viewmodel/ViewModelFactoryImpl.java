package com.mx.framework2.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mx.engine.utils.CheckUtils;
import com.mx.engine.utils.ObjectUtils;
import com.mx.framework2.Module;
import com.mx.framework2.R;
import com.mx.framework2.view.ui.BaseActivity;
import com.mx.framework2.view.ui.BaseDialogFragment;
import com.mx.framework2.view.ui.BaseFragment;
import com.mx.framework2.view.ui.RunState;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by liuyuxuan on 16/6/6.
 */
public class ViewModelFactoryImpl implements ViewModelFactory {
    private Module module;
    private List<WeakReference<LifecycleViewModel>> viewModelPool = new LinkedList<>();

    public ViewModelFactoryImpl(Module module) {
        this.module = module;
    }

    @Override
    public <T extends LifecycleViewModel> T createViewModel(String viewModelTag, Class<T> viewModelClassType, BaseDialogFragment baseDialogFragment) {
        CheckUtils.checkNotNull(viewModelClassType);
        CheckUtils.checkNotNull(baseDialogFragment);
        //TODO: R.string.activity_msg means ambiguously
        CheckUtils.checkArgument(baseDialogFragment.getRunState() == RunState.Created,
                baseDialogFragment.getString(R.string.activity_msg));
        return createViewModel(baseDialogFragment.getContext(), baseDialogFragment, viewModelTag, viewModelClassType);
    }

    public static <T> T newInstance(Class<T> cls, Object defaultParam) {
        T value = ObjectUtils.newInstance(cls, defaultParam);
        if (value == null) {
            value = ObjectUtils.newInstance(cls);
        }

        return value;
    }

    private <T extends LifecycleViewModel> T getViewModelFromPool(@NonNull String uniqueId) {
        CheckUtils.checkNotNull(uniqueId);
        ListIterator<WeakReference<LifecycleViewModel>> listIterator = viewModelPool.listIterator();
        while (listIterator.hasNext()) {
            WeakReference<LifecycleViewModel> vmRef = listIterator.next();
            if (vmRef.get() == null) {
                listIterator.remove();
            }
        }

        for (WeakReference<LifecycleViewModel> vmRef : viewModelPool) {
            LifecycleViewModel viewModel = vmRef.get();
            if (viewModel != null && viewModel.getUniqueId().equals(uniqueId)) {
                return (T) viewModel;
            }
        }
        return null;
    }


    private <T extends LifecycleViewModel> T createViewModel(Context context, ViewModelScope
            scope, String viewModelTag, Class<T> viewModelClass) {
        String uniqueId = LifecycleViewModel.generateUniqueId(scope.getViewModelScopeId(), viewModelTag);
        T value = getViewModelFromPool(uniqueId);
        if (value == null) {
            value = newInstance(viewModelClass, scope);
            viewModelPool.add(new WeakReference<LifecycleViewModel>(value));
        }
        if (null != value) {
            if (value instanceof ModuleAware) {
                value.setModule(module);
            }
            value.setViewModelScope(scope);
            value.setContext(context);
            value.setTag(viewModelTag);
        }
        return value;
    }

    @Override
    public <T extends LifecycleViewModel> T createViewModel(String viewModelTag, Class<T>
            viewModelClass, BaseFragment baseFragment) {
        CheckUtils.checkNotNull(viewModelClass);
        CheckUtils.checkNotNull(baseFragment);
        //TODO: R.string.activity_msg means ambiguously
        CheckUtils.checkArgument(baseFragment.getRunState() == RunState.Created,
                baseFragment.getString(R.string.activity_msg));
        return createViewModel(baseFragment.getContext(), baseFragment, viewModelTag, viewModelClass);
    }

    @Override
    public <T extends LifecycleViewModel> T createViewModel(String viewModelTag, Class<T>
            viewModelClass, BaseActivity baseActivity) {
        CheckUtils.checkNotNull(viewModelClass);
        CheckUtils.checkNotNull(baseActivity);
        CheckUtils.checkArgument(baseActivity.getRunState() == RunState.Created,
                baseActivity.getString(R.string.activity_msg));
        return createViewModel(baseActivity, baseActivity, viewModelTag, viewModelClass);
    }
}
