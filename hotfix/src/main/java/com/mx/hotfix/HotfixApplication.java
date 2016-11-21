package com.mx.hotfix;


import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.support.multidex.MultiDex;

import com.mx.hotfix.log.HotfixLogImp;
import com.mx.hotfix.util.HotfixManager;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.app.DefaultApplicationLike;

import static com.mx.hotfix.util.HotfixApplicationContext.application;
import static com.mx.hotfix.util.HotfixApplicationContext.context;

/**
 * Created by wwish on 16/11/17.
 */

public class HotfixApplication extends DefaultApplicationLike {
    public HotfixApplication(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent, Resources[] resources, ClassLoader[] classLoader, AssetManager[] assetManager) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent, resources, classLoader, assetManager);
    }

    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        //you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        application = getApplication();
        context = getApplication();
        HotfixManager.setTinkerApplicationLike(this);
        HotfixManager.initFastCrashProtect();
        //should set before tinker is installed
        HotfixManager.setUpgradeRetryEnable(true);

        //optional set logIml, or you can use default debug log
        TinkerInstaller.setLogIml(new HotfixLogImp());

        //installTinker after load multiDex
        //or you can put com.tencent.tinker.** to main dex
        HotfixManager.installTinker(this);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        getApplication().registerActivityLifecycleCallbacks(callback);
    }
}

