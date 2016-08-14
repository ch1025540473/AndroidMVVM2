package com.mx.framework2.viewmodel;

import com.mx.framework2.view.BaseActivity;

/**
 * Created by liuyuxuan on 16/6/6.
 */
public interface ActivityAware {

    void setActivity(BaseActivity activity);

    BaseActivity getActivity();

}
