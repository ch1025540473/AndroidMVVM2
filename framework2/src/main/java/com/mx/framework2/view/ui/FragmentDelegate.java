package com.mx.framework2.view.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.mx.engine.event.EventProxy;
import com.mx.engine.utils.CheckUtils;
import com.mx.framework2.viewmodel.LifecycleViewModel;
import com.mx.framework2.viewmodel.ViewModelManager;
import com.mx.framework2.viewmodel.ViewModelScope;

import java.util.UUID;

/**
 * Created by zhulianggang on 16/9/26.
 */

public class FragmentDelegate implements ViewModelScope {
    private final String UUID_KEY = "UUID_KEY_FRAMEWORK2_" + getClass().getName();
    private String uuid;

    private ViewModelManager viewModelManager = new ViewModelManager();
    private RunState runState;

    public final ViewModelManager getViewModelManager() {
        CheckUtils.checkNotNull(viewModelManager);
        return viewModelManager;
    }

    public void onAttach(Context context) {
        viewModelManager.onAttachContext(context);
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

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

    }

    public void onStart() {
        getViewModelManager().start();
        runState = RunState.Started;
        EventProxy.getDefault().register(this);
    }

    public void onStop() {
        getViewModelManager().stop();
        runState = RunState.Stoped;
        EventProxy.getDefault().unregister(this);

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

    public void registerActivityResultReceiver(int requestCode, String receiverId) {
        getViewModelManager().registerActivityResultReceiver(requestCode, receiverId);
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
}
