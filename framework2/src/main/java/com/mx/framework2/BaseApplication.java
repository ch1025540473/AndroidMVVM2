package com.mx.framework2;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Created by liuyuxuan on 16/4/19.
 */
public class BaseApplication extends com.mx.framework.BaseApplication {
    private static Application baseApplication = null;
    private Map<String, Module> modules;

    public BaseApplication(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag,
                           long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent,
                           Resources[] resources, ClassLoader[] classLoader, AssetManager[] assetManager) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent, resources, classLoader, assetManager);
    }

    //    @Override
//    public void onCreate() {
//        super.onCreate();
//        baseApplication = this;
//        modules = new LinkedHashMap<>();
//        // install modules
//    }
//replace
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        baseApplication = getApplication();
        modules = new LinkedHashMap<>();
        Log.d("BaseApplication", "BuildConfig.BUILD_TYPE=" + BuildConfig.BUILD_TYPE);
        Log.d("BaseApplication", "BuildConfig.logger_debug=" + BuildConfig.logger_debug);
        // install modules
        if (BuildConfig.logger_debug) {
            Logger.init().methodCount(3)
                    .logLevel(LogLevel.FULL)
                    .methodOffset(2);
        } else {
            Logger.init().methodCount(3)
                    .logLevel(LogLevel.NONE)
                    .methodOffset(2);
        }


    }

    public void installModule(Module module) {
        assert (null != module);
        modules.put(module.getClass().getName(), module);
//        module.checkUpdate();
//        module.onUpdataDataBase();
//        module.onUpdateFileData();
        module.onStart(module.getUserCaseManager());
    }

    public static Application instance() {
//        checkNotNull(baseApplication);
        return baseApplication;
    }


}
