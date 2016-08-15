package com.mx.framework2.viewmodel;

import com.mx.framework2.view.ui.BaseActivity;
import com.mx.framework2.view.ui.BaseFragment;

/**
 * Created by liuyuxuan on 16/6/6.
 */
public interface ViewModelFactory {

    public <T extends ViewModel> T createViewModel(Class<T> viewModelClassType, BaseFragment baseFragment);

    public <T extends ViewModel> T createViewModel(Class<T> viewModelClassType, BaseActivity baseActivity);

}
