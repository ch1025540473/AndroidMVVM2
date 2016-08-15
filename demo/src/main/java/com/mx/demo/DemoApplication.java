package com.mx.demo;

import android.app.Application;

import com.mx.framework2.BaseApplication;

/**
 * Created by chenbaocheng on 16/8/15.
 */
public class DemoApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        installModule(DemoModule.get());
    }
}
