package com.mx.framework.viewmodel;

import com.mx.framework.view.BaseActivity;
import com.mx.framework.view.BaseFragment;

/**
 * Created by liuyuxuan on 16/6/6.
 */
@Deprecated
public interface ViewModelFactory {

    public <T extends ViewModel> T createViewModel(Class<T> viewModelClassType, BaseFragment baseFragment);

    public <T extends ViewModel> T createViewModel(Class<T> viewModelClassType, BaseActivity baseActivity);

}
