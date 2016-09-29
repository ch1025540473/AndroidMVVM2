package com.mx.framework2.viewmodel;

import com.mx.framework2.view.ui.ActivityStarter;

/**
 * Created by liuyuxuan on 16/8/18.
 */
public interface ViewModelScope extends ActivityStarter {
    void addViewModel(LifecycleViewModel lifecycleViewModel);

    void removeViewModel(LifecycleViewModel lifecycleViewModel);

    void registerActivityResultReceiver(int requestCode, String viewModelId);

    String getViewModelScopeId();
}
