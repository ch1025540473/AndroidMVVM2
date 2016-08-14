package com.mx.framework2.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mx.engine.utils.CheckUtils;
import com.mx.framework2.Module;
import com.mx.engine.event.BroadcastEvent;
import com.mx.engine.event.EventProxy;
import com.mx.framework2.model.UseCase;
import com.mx.framework2.model.UseCaseHolder;
import com.mx.framework2.view.BaseActivity;
import com.mx.framework2.view.RunState;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.mx.engine.utils.CheckUtils.checkArgument;
import static com.mx.engine.utils.CheckUtils.checkNotNull;

/**
 * Created by liuyuxuan on 16/4/20.
 */
public abstract class ViewModel extends BaseObservable implements BaseActivity.ActivityResultListener, ActivityAware, ModuleAware, ViewModelManagerAware, UseCaseHolder {
    @Override
    public void setViewModelManager(@NonNull ViewModelManager viewModelManager) {
        checkNotNull(viewModelManager);
        this.viewModelManager = viewModelManager;
    }

    @Override
    public void setActivity(BaseActivity baseActivity) {
        if (null == baseActivity) {
            this.activityRef = null;
            return;
        }
        this.activityRef = new WeakReference<BaseActivity>(baseActivity);
    }

    @Override
    public void setModule(@NonNull Module module) {
        checkNotNull(module);
        this.module = module;
    }


    private EventProxy eventProxy;

    /* Just mark a method in ViewModel */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.SOURCE)
    protected @interface Command {
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.SOURCE)
    protected @interface BindView {
    }

    private Reference<BaseActivity> activityRef;
    RunState runState;
    private ViewModelManager viewModelManager;
    private Map<Class, UseCase> useCases;
    private Module module;
    private static Handler handler;
    private Reference<Context> contextRef;

    protected final void runOnUIThread(@NonNull Runnable runnable, long delayTime) {
        CheckUtils.checkNotNull(runnable);
        if (null == handler) {
            handler = new Handler();
        }
        handler.postDelayed(runnable, delayTime);

    }

    protected final void runOnUIThread(@NonNull Runnable runnable) {
        runOnUIThread(runnable, 0);
    }

    public ViewModel() {
        eventProxy = eventProxy.getDefault();
        runState = RunState.Created;
    }

    public final Context getContext() {
        return contextRef.get();
    }

    public final void setContext(@NonNull Context context) {
        checkNotNull(context);
        this.contextRef = new WeakReference<Context>(context);
    }

    protected final <T extends UseCase> T obtainUseCase(Class<T> classType) {
        checkNotNull(module);
        if (null == useCases) {
            useCases = new LinkedHashMap<>();
        }
        T useCase = (T) useCases.get(classType);
        if (null == useCase) {
            useCase = module.getUserCaseManager().obtainUseCase(classType, this);
            useCases.put(classType, useCase);
        }
        return useCase;
    }

    @Override
    public BaseActivity getActivity() {
        if (this.activityRef == null) {
            return null;
        }
        return this.activityRef.get();
    }

    public RunState getRunState() {

        return runState;
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        BaseActivity baseActivity = getActivity() == null ? (BaseActivity) getContext() : getActivity();
        baseActivity.registerActivityResultListener(requestCode, this);
        baseActivity.startActivityForResult(intent, requestCode);
    }

    public void onWindowFocusChanged(boolean hasFocus) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

    }

    @Deprecated
    public ViewModel(@NonNull ViewModelManager viewModelManager, @NonNull BaseActivity activityRef) {

        checkArgument(null != viewModelManager);
        checkArgument(null != activityRef);
        checkArgument(activityRef.getRunState() == RunState.Created);
        eventProxy = eventProxy.getDefault();
        this.viewModelManager = viewModelManager;
        this.activityRef = new SoftReference<BaseActivity>(activityRef);
        runState = RunState.Created;
    }

    public final <T extends ViewModel> T getViewModel(Class<T> modelClass) {
        return (T) viewModelManager.getViewModel(modelClass);
    }

    private final void init() {
        Log.d("ViewModel", "thisHashCode=" + hashCode());
        eventProxy.register(this);
    }


    private void recycle() {

        if (eventProxy.isRegistered(this)) {
            eventProxy.unregister(this);
        }
    }


    public final <T extends BroadcastEvent> void postEvent(T event) {
        eventProxy.post(event);
    }

    public void onSaveInstanceState(Bundle outState) {

    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    final void create(Bundle savedInstanceState) {
        runState = RunState.Created;
        onCreate(savedInstanceState);
    }


    protected void onCreate(Bundle savedInstanceState) {

    }

    final void start() {
        runState = RunState.Started;
        init();
        onStart();
    }

    protected void onStart() {

    }

    final void resume() {
        runState = RunState.Resumed;
        onResume();
    }

    protected void onResume() {

    }

    final void pause() {
        runState = RunState.Paused;
        onPause();
    }

    protected void onPause() {

    }

    final void stop() {
        runState = RunState.Stoped;
        onStop();
        recycle();
    }

    protected void onStop() {


    }

    final void distory() {
        runState = RunState.Destroyed;
        if (null != useCases) {
            for (UseCase useCase : useCases.values()) {
                module.getUserCaseManager().closeUseCase(useCase, this);
            }
        }
        onDistory();
    }

    protected void onDistory() {


    }


}
