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

/**
 * Created by zhulianggang on 16/9/26.
 */

public class FragmentDelegate {
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
        viewModelManager.setSavedInstanceState(savedInstanceState);
        viewModelManager.create();
    }

    public void onSaveInstanceState(Bundle outState) {
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

    public void addViewModel(LifecycleViewModel lifecycleViewModel) {
        getViewModelManager().addViewModel(lifecycleViewModel);
    }

    public void removeViewModel(LifecycleViewModel lifecycleViewModel) {
        getViewModelManager().removeViewModel(lifecycleViewModel);
    }


    public void registerActivityResultReceiver(int requestCode, String receiverId) {
        getViewModelManager().registerActivityResultReceiver(requestCode, receiverId);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getViewModelManager().onActivityResult(requestCode, resultCode, data);
    }

}
