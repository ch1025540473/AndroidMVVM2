package com.mx.framework.viewmodel;

import com.mx.framework.view.BaseActivity;

/**
 * Created by liuyuxuan on 16/6/6.
 */
public interface ActivityAware {

    void setActivity(BaseActivity activity);

    BaseActivity getActivity();

}
