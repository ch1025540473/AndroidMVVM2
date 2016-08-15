package com.mx.demo;

import com.mx.demo.model.DemoUseCase;
import com.mx.framework2.Module;
import com.mx.framework2.model.UseCaseManager;

/**
 * Created by chenbaocheng on 16/8/14.
 */
public class DemoModule extends Module {
    private static volatile DemoModule instance = null;

    public static DemoModule get(){
        if(instance != null){
            return instance;
        }

        synchronized (DemoModule.class){
            if(instance == null){
                instance = new DemoModule();
            }
        }

        return instance;
    }

    @Override
    protected void onStart(UseCaseManager userCaseManager) {
        userCaseManager.register(DemoUseCase.class);
    }
}
