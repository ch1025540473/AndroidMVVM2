package com.mx.framework2;

import android.support.multidex.*;
import android.util.Log;

import com.orhanobut.logger.*;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Created by liuyuxuan on 16/4/19.
 */
public class BaseApplication extends com.mx.framework.BaseApplication {
    private static BaseApplication baseApplication;
    private Map<String, Module> modules;

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
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

    public static BaseApplication instance() {
//        checkNotNull(baseApplication);
        return baseApplication;
    }
}
