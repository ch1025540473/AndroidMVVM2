package com.mx.framework2.viewmodel;

/**
 * Created by liuyuxuan on 16/8/18.
 */
public interface ViewModelScope {
    void addViewModel(LifecycleViewModel lifecycleViewModel);

    void removeViewModel(LifecycleViewModel lifecycleViewModel);

}
