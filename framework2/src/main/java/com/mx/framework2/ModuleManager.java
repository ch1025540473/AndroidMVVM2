package com.mx.framework2;

import android.content.Context;

import com.mx.engine.utils.CheckUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuyuxuan on 2017/2/7.
 */

public class ModuleManager {
    private Map<String, Module> modules;
    private static ModuleManager moduleManager;

    public static ModuleManager getInstance() {
        if (moduleManager != null) {
            return moduleManager;
        }
        synchronized (ModuleManager.class) {
            if (moduleManager == null) {
                moduleManager = new ModuleManager();
            }
        }
        return moduleManager;
    }

    private ModuleManager() {
        modules = new HashMap<>();
    }

    public void installModule(Context context, Module module) {
        CheckUtils.checkNotNull(module);
        modules.put(module.getClass().getName(), module);
        module.onInstall(context);
        module.onStart(module.getUserCaseManager());
    }
}
