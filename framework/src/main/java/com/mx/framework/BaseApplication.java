package com.mx.framework;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.support.multidex.MultiDex;

import com.mx.framework.hotfix.log.MyLogImp;
import com.mx.framework.hotfix.util.HotfixManager;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.app.DefaultApplicationLike;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Created by liuyuxuan on 16/4/19.
 */
@Deprecated
//public class BaseApplication extends MultiDexApplication {
public class BaseApplication extends DefaultApplicationLike {

    public static Application application = null;
    public static Context context = null;

    private static final String TAG = "Tinker.SampleApplicationLike";

    public BaseApplication(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag,
                           long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent,
                           Resources[] resources, ClassLoader[] classLoader, AssetManager[] assetManager) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent, resources, classLoader, assetManager);
    }

    private Map<String, Module> modules;


//    @Override
//    public void onCreate() {
//        super.onCreate();
//        baseApplication = this;
//        modules = new LinkedHashMap<>();
//
//        // install modules
//    }

    public void installModule( Module module) {
        assert (null != module);
        modules.put(module.getClass().getName(), module);
//        module.checkUpdate();
//        module.onUpdataDataBase();
//        module.onUpdateFileData();
        module.onStart(module.getUserCaseManager());
    }


    public static Application instance() {
//        checkNotNull(baseApplication);
        return application;
    }

    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        modules = new LinkedHashMap<>();
        //you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        application = getApplication();
        context = getApplication();
        HotfixManager.setTinkerApplicationLike(this);
        HotfixManager.initFastCrashProtect();
        //should set before tinker is installed
        HotfixManager.setUpgradeRetryEnable(true);

        //optional set logIml, or you can use default debug log
        TinkerInstaller.setLogIml(new MyLogImp());

        //installTinker after load multiDex
        //or you can put com.tencent.tinker.** to main dex
        HotfixManager.installTinker(this);
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        getApplication().registerActivityLifecycleCallbacks(callback);
    }

}
