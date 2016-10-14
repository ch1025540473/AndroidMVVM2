package com.mx.framework2.viewmodel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mx.engine.event.EventProxy;
import com.mx.framework2.Module;
import com.mx.framework2.event.BroadcastEvent;
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
public class LifecycleViewModel extends ViewModel implements Lifecycle, ModuleAware,
        ActivityStarter {
    private LifecycleState lifecycleState;
    private EventProxy eventProxy;
    private Reference<ViewModelScope> scopeRef;
    private Module module;
    private boolean isAttachedToView = false;
    private String tag;
    private String scopeId;
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
            scopeId = null;
            return;
        }
        // vm 所在scope发生了变化,此时vm被重用 需要于上一次的scope脱离
        if (viewModelScope != getViewModelScope()) {
            detachedFromView();
        }

        this.scopeRef = new WeakReference<>(viewModelScope);
        scopeId = viewModelScope.getViewModelScopeId();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        getViewModelScope().registerActivityResultReceiver(requestCode, getTag());
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
        return scopeRef == null ? null : scopeRef.get();
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
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
            onDetachedFromView();
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
    public final synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback
                                                                           callback) {
        super.removeOnPropertyChangedCallback(callback);
        if (null != callback && callback.getClass().getName()
                .contains("ViewDataBinding$WeakPropertyListener")) {
            detachedFromView();
        }
        Log.d("home>>>", "removeOnPropertyChangedCallback ");
    }

    @Override
    public final synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback
                                                                        callback) {
        super.addOnPropertyChangedCallback(callback);
        if (null != callback && callback.getClass().getName()
                .contains("ViewDataBinding$WeakPropertyListener")) {
            attachedToView();
        }
        Log.d("home>>>", "addOnPropertyChangedCallback ");
    }

    public String getUniqueId() {
        return generateUniqueId(scopeId, getTag());
    }

    public static String generateUniqueId(String scopeId, String viewModelId) {
        return String.format("%s_%s", scopeId, viewModelId);
    }

    @Override
    public <T extends BroadcastEvent> void postEvent(T event) {
        super.postEvent(event);
    }
}
