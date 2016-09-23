package com.mx.framework2.view.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mx.engine.event.EventProxy;
import com.mx.engine.utils.CheckUtils;
import com.mx.framework2.viewmodel.LifecycleViewModel;
import com.mx.framework2.viewmodel.ViewModelManager;
import com.mx.framework2.viewmodel.ViewModelScope;

/**
 * Created by liuyuxuan on 16/4/20.
 */
//TODO 监测生命状态

public class BaseFragment extends Fragment implements ViewModelScope {
    private ViewModelManager viewModelManager;

    private RunState runState;

    public final ViewModelManager getViewModelManager() {
        CheckUtils.checkNotNull(viewModelManager);
        return viewModelManager;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModelManager.onAttachContext(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModelManager = new ViewModelManager(savedInstanceState);
        runState = RunState.Created;
        viewModelManager.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getViewModelManager().saveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        getViewModelManager().restoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventProxy.getDefault().register(this);
        runState = RunState.Created;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        getViewModelManager().start();
        runState = RunState.Started;
        EventProxy.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getViewModelManager().stop();
        runState = RunState.Stoped;
        EventProxy.getDefault().unregister(this);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        getViewModelManager().setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onDestroy() {
        runState = RunState.Destroyed;
        super.onDestroy();

    }

    @Override
    public void onPause() {
        super.onPause();
        runState = RunState.Paused;
        getViewModelManager().pause();
    }

    @Override
    public void onResume() {
        super.onResume();
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

}
