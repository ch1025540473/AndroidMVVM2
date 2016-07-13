package com.mx.framework;

import android.support.multidex.MultiDexApplication;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Created by liuyuxuan on 16/4/19.
 */
public class BaseApplication extends MultiDexApplication {

    private static BaseApplication baseApplication;

    private Map<String, Module> modules;


    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        modules = new LinkedHashMap<>();

        // install modules
    }

    public void installModule( Module module) {
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
