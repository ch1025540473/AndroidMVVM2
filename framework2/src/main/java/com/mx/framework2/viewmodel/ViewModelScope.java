package com.mx.framework2.viewmodel;

import com.mx.activitystarter.RawActivityStarter;

/**
 * Created by liuyuxuan on 16/8/18.
 */
public interface ViewModelScope extends RawActivityStarter {
    void addViewModel(LifecycleViewModel lifecycleViewModel);

    void removeViewModel(LifecycleViewModel lifecycleViewModel);

    void registerActivityResultReceiver(int requestCode, String viewModelId);

    String getViewModelScopeId();
}
