package com.mx.framework2.view.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mx.engine.event.EventProxy;
import com.mx.engine.utils.CheckUtils;
import com.mx.framework2.event.Events;
import com.mx.framework2.view.DataBindingFactory;
import com.mx.framework2.viewmodel.LifecycleViewModel;
import com.mx.framework2.viewmodel.ViewModelManager;
import com.mx.framework2.viewmodel.ViewModelScope;

import java.lang.ref.WeakReference;
import java.util.UUID;

/**
 * Created by zhulianggang on 16/9/26.
 */

public class FragmentDelegate implements ViewModelScope {
    private final String UUID_KEY = "UUID_KEY_FRAMEWORK2_" + getClass().getName();
    private String uuid;
    private WeakReference<Context> context;
    private ViewModelManager viewModelManager = new ViewModelManager();
    private RunState runState;
    private WeakReference<View> contentView;
    private WeakReference<Fragment> fragmentRef;

    private Fragment getFragment() {
        return fragmentRef == null ? null : fragmentRef.get();
    }

    public void setFragment(Fragment fragmentRef) {
        this.fragmentRef = new WeakReference<>(fragmentRef);
    }

    public final ViewModelManager getViewModelManager() {
        CheckUtils.checkNotNull(viewModelManager);
        return viewModelManager;
    }

    public void onAttach(Context context) {
        viewModelManager.onAttachContext(context);
        this.context = new WeakReference<>(context);
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        runState = RunState.Created;

        if (savedInstanceState != null) {
            uuid = savedInstanceState.getString(UUID_KEY);
            CheckUtils.checkNotNull(uuid, "uuid is null");
        } else {
            uuid = UUID.randomUUID().toString();
        }

        viewModelManager.setSavedInstanceState(savedInstanceState);
        viewModelManager.create();
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putString(UUID_KEY, uuid);
        getViewModelManager().saveInstanceState(outState);
    }

    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        getViewModelManager().restoreInstanceState(savedInstanceState);
    }

    public void onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventProxy.getDefault().register(this);
        runState = RunState.Created;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        contentView = new WeakReference<>(view);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

    }

    private View getContentView() {
        return contentView == null ? null : contentView.get();
    }

    public void onStart() {
        getViewModelManager().start();
        runState = RunState.Started;
        EventProxy.getDefault().register(this);
    }

    public void onStop() {
        getViewModelManager().stop();
        runState = RunState.Stopped;
        EventProxy.getDefault().unregister(this);
        DataBindingFactory.checkViewDataBinding(getContentView());

    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        getViewModelManager().setUserVisibleHint(isVisibleToUser);
    }

    public void onDestroy() {
        runState = RunState.Destroyed;
    }

    public void onPause() {
        runState = RunState.Paused;
        getViewModelManager().pause();
    }

    public void onResume() {
        runState = RunState.Resumed;
        getViewModelManager().resume();
        DataBindingFactory.checkViewDataBinding(getContentView());
    }

    public RunState getRunState() {
        return runState;
    }

    @Override
    public void addViewModel(LifecycleViewModel lifecycleViewModel) {
        getViewModelManager().addViewModel(lifecycleViewModel);
    }

    @Override
    public void removeViewModel(LifecycleViewModel lifecycleViewModel) {
        getViewModelManager().removeViewModel(lifecycleViewModel);
    }

    @Override
    public void registerActivityResultReceiver(int requestCode, String receiverId) {
        getViewModelManager().registerActivityResultReceiver(requestCode, receiverId);
        BaseActivity baseActivity = (BaseActivity) getFragment().getActivity();
        EventProxy.getDefault().post(new Events.RequestCodeRegisterEvent(requestCode, baseActivity.getActivityId()));
    }

    @Override
    public String getViewModelScopeId() {
        return uuid;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getViewModelManager().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void startActivity(Intent intent) {
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
    }

    @Override
    public Context getContext() {
        return context == null ? null : context.get();
    }
}
