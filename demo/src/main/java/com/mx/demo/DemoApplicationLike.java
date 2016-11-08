package com.mx.demo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.mx.framework2.BaseApplication;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * Created by chenbaocheng on 16/8/15.
 */
@SuppressWarnings("unused")
@DefaultLifeCycle(application = "com.mx.demo.DemoApplication",
        flags = ShareConstants.TINKER_ENABLE_ALL,
        loadVerifyFlag = false)
public class DemoApplicationLike extends BaseApplication {

    public DemoApplicationLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag,
                               long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent,
                               Resources[] resources, ClassLoader[] classLoader, AssetManager[] assetManager) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent, resources, classLoader, assetManager);
    }
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        installModule(DemoModule.get());
//    }


    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        installModule(DemoModule.get());
        System.out.println("88888888888888888888888");
    }
}
