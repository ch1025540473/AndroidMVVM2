package com.mx.framework2;

import android.content.Context;

import com.mx.engine.event.BroadcastEvent;
import com.mx.engine.event.EventProxy;
import com.mx.framework2.event.Events;
import com.mx.framework2.model.UseCaseHolder;
import com.mx.framework2.model.UseCaseManager;
import com.mx.framework2.viewmodel.ViewModelFactory;
import com.mx.framework2.viewmodel.ViewModelFactoryImpl;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by liuyuxuan on 16/5/10.
 * 1,升级数据库
 * 2,升级文件
 * 3,模块通信
 * 4,管理模块
 * 5,Module和Module互相不可视
 * 6,启动模块内部的activity;
 */
public abstract class Module {
    private EventProxy eventProxy;
    private UseCaseManager userCaseManager;
    private ViewModelFactory viewModelFactory;
    private static Set<Class<? extends Module>> instances = new HashSet<>();

    protected Module() {
        if (instances.contains(this.getClass())) {
            throw new RuntimeException();
        } else {
            instances.add(this.getClass());
        }
    }

    protected void onInstall(Context context) {
        eventProxy = EventProxy.getDefault();
        eventProxy.register(this);
        userCaseManager = new UseCaseManager(context);
        viewModelFactory = new ViewModelFactoryImpl(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onActivityDestroy(Events.ActivityDestroyEvent event) {
        if (event.getActivityInfo().isFinished()) {
            Logger.t("module").d("onActivityDestroy " + event.getActivityInfo().getActivityName());
            userCaseManager.onUseCaseHolderDestroy(event.getActivityInfo());
        }
    }

    public UseCaseManager getUserCaseManager() {
        return userCaseManager;
    }

    protected abstract void onStart(UseCaseManager userCaseManager);

    public ViewModelFactory getViewModelFactory() {
        return viewModelFactory;
    }

    //TODO:

    //    public void onUpdateDataBase() {
//
//    }
//
//    public void onUpdateFileData() {
//
//    }
//
//    public void checkUpdate() {
//
//
//    }
// TODO:
    public <T extends BroadcastEvent> void postEvent(T event) {
        eventProxy.post(event);
    }

}
