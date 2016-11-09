package com.mx.demo;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.mx.framework2.BaseApplication;

/**
 * Created by chenbaocheng on 16/8/15.
 */
public class DemoApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        installModule(DemoModule.get());
    }
}
