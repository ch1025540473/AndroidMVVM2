package com.mx.framework2.viewmodel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mx.engine.event.EventProxy;
import com.mx.framework2.Module;
import com.mx.framework2.model.UseCase;
import com.mx.framework2.view.ui.BaseActivity;
import com.mx.framework2.view.ui.BaseFragment;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import static com.mx.engine.utils.CheckUtils.checkNotNull;

/**
 * Created by liuyuxuan on 16/8/18.
 */
public class LifecycleViewModel extends ViewModel implements Lifecycle, BaseActivity.ActivityResultListener, ModuleAware {
    private LifecycleState lifecycleState;
    private EventProxy eventProxy;
    private Reference<ViewModelScope> scopeRef;
    private Module module;
    private boolean isAttachedToView = false;

    protected LifecycleState getLifecycleState() {
        return lifecycleState;
    }

    private final void init() {
        Log.d("ViewModel", "thisHashCode=" + hashCode());
        eventProxy = EventProxy.getDefault();
        eventProxy.register(this);
    }

    private void recycle() {
        if (eventProxy.isRegistered(this)) {
            eventProxy.unregister(this);
        }
    }

    protected final <T extends UseCase> T obtainUseCase(Class<T> classType) {
        checkNotNull(module);
        T useCase = useCase = module.getUserCaseManager().obtainUseCase(classType, getActivity());
        return useCase;
    }

    final void setViewModelScope(ViewModelScope viewModelScope) {
        if (null == viewModelScope) {
            return;
        }
        this.scopeRef = new WeakReference<>(viewModelScope);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        getActivity().registerActivityResultListener(requestCode, this);
        getActivity().startActivityForResult(intent, requestCode);
    }

    private BaseActivity getActivity() {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

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
        onCreate();
    }

    @Override
    public final void start() {
        lifecycleState = LifecycleState.Started;
        onStart();
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
        lifecycleState = lifecycleState.Stopped;
        onStop();
    }

    protected void onAttachedToView() {
    }

    protected void onCreate() {
    }

    protected void onStart() {
    }

    protected void onResume() {
    }

    protected void onPause() {
    }

    protected void onStop() {
    }

    protected void onDetachedFromView() {
    }

    public void onWindowFocusChanged(boolean hasFocus) {
    }

    public void onSaveInstanceState(Bundle outState) {
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }

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
