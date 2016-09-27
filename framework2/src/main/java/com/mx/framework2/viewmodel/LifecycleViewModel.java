package com.mx.framework2.viewmodel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.mx.engine.event.EventProxy;
import com.mx.framework2.Module;
import com.mx.framework2.model.UseCase;
import com.mx.framework2.view.ui.ActivityResultCallback;
import com.mx.framework2.view.ui.ActivityResultManager;
import com.mx.framework2.view.ui.ActivityStarter;
import com.mx.framework2.view.ui.BaseActivity;
import com.mx.framework2.view.ui.BaseFragment;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import static com.mx.engine.utils.CheckUtils.checkNotNull;

/**
 * Created by liuyuxuan on 16/8/18.
 */
public class LifecycleViewModel extends ViewModel implements Lifecycle, ModuleAware, ActivityStarter {
    private LifecycleState lifecycleState;
    private EventProxy eventProxy;
    private Reference<ViewModelScope> scopeRef;
    private Module module;
    private boolean isAttachedToView = false;
    private String id;
    private ActivityResultManager activityResultManager = ActivityResultManager.getInstance();

    protected LifecycleState getLifecycleState() {
        return lifecycleState;
    }

    private final void init() {
        eventProxy = EventProxy.getDefault();
        eventProxy.register(this);
    }

    private final void recycle() {
        if (eventProxy.isRegistered(this)) {
            eventProxy.unregister(this);
        }
    }

    protected final <T extends UseCase> T obtainUseCase(Class<T> classType) {
        checkNotNull(module);
        T useCase = module.getUserCaseManager().obtainUseCase(classType, getActivity());
        return useCase;
    }

    final void setViewModelScope(ViewModelScope viewModelScope) {
        if (null == viewModelScope) {
            return;
        }
        this.scopeRef = new WeakReference<>(viewModelScope);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        getViewModelScope().registerActivityResultReceiver(requestCode, getId());
        getViewModelScope().startActivityForResult(intent, requestCode);
    }

    public void startActivityForResult(Intent intent, ActivityResultCallback callback) {
        int requestCode = activityResultManager.generateRequestCodeForCallback(callback);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void startActivity(Intent intent) {
        getViewModelScope().startActivity(intent);
    }

    private BaseActivity getActivity() {
        //TODO: This method can be refined someway
        BaseActivity baseActivity;
        ViewModelScope viewModelScope = getViewModelScope();
        if (viewModelScope instanceof BaseActivity) {
            baseActivity = (BaseActivity) viewModelScope;
        } else if (viewModelScope instanceof BaseFragment) {
            baseActivity = (BaseActivity) ((BaseFragment) viewModelScope).getActivity();
        } else {
            baseActivity = (BaseActivity) getContext();
        }
        return baseActivity;
    }

    private ViewModelScope getViewModelScope() {
        return scopeRef.get();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public void setModule(@NonNull Module module) {
        checkNotNull(module);
        this.module = module;
    }

    @Override
    public final void attachedToView() {
        if (!isAttachedToView) {
            isAttachedToView = true;
            init();
            onAttachedToView();
            getViewModelScope().addViewModel(this);
        }
    }

    @Override
    public final void detachedFromView() {
        if (isAttachedToView) {
            isAttachedToView = false;
            recycle();
            onDetachedFromView();
            getViewModelScope().removeViewModel(this);
        }
    }

    @Override
    public void create(Bundle bundle) {
        lifecycleState = LifecycleState.Created;
        onCreate(bundle);
    }

    @Override
    public final void start() {
        lifecycleState = LifecycleState.Started;
        onStart();
        init();
    }

    @Override
    public final void restart() {
        lifecycleState = LifecycleState.Started;
        onRestart();
    }

    @Override
    public final void resume() {
        lifecycleState = LifecycleState.Resumed;
        onResume();
    }

    @Override
    public final void pause() {
        lifecycleState = LifecycleState.Paused;
        onPause();
    }

    @Override
    public final void stop() {
        lifecycleState = LifecycleState.Stopped;
        onStop();
        recycle();
    }

    protected void onAttachedToView() {
    }

    protected void onCreate(Bundle bundle) {
    }

    protected void onStart() {
    }

    protected void onRestart() {
    }

    protected void onResume() {
    }

    protected void onPause() {
    }

    protected void onStop() {
    }

    protected void onDetachedFromView() {
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        activityResultManager.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void setUserVisibleHint(boolean userVisibleHint) {
    }

    @Override
    public void accept(Visitor viewModelVisitor) {
        viewModelVisitor.visit(this);
    }

    @Override
    public final synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        super.removeOnPropertyChangedCallback(callback);
        if (null != callback && callback.getClass().getName()
                .contains("ViewDataBinding$WeakPropertyListener")) {
            detachedFromView();
        }
    }

    @Override
    public final synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        super.addOnPropertyChangedCallback(callback);
        if (null != callback && callback.getClass().getName()
                .contains("ViewDataBinding$WeakPropertyListener")) {
            attachedToView();
        }
    }
}
