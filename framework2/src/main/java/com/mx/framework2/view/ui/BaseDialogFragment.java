package com.mx.framework2.view.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mx.framework2.viewmodel.LifecycleViewModel;

/**
 * Created by zhulianggang on 16/9/21.
 */

public class BaseDialogFragment extends DialogFragment {

    private FragmentDelegate fragmentDelegate = new FragmentDelegate();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentDelegate.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentDelegate.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentDelegate.onCreateView(inflater, container, savedInstanceState);
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        fragmentDelegate.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        fragmentDelegate.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentDelegate.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onStart() {
        super.onStart();
        fragmentDelegate.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        fragmentDelegate.onStop();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        fragmentDelegate.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentDelegate.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        fragmentDelegate.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentDelegate.onResume();
    }

    public RunState getRunState() {
        return fragmentDelegate.getRunState();
    }


    public void addViewModel(LifecycleViewModel lifecycleViewModel) {
        fragmentDelegate.addViewModel(lifecycleViewModel);
    }

    public void removeViewModel(LifecycleViewModel lifecycleViewModel) {
        fragmentDelegate.removeViewModel(lifecycleViewModel);
    }

    public void registerActivityResultReceiver(int requestCode, String receiverId) {
        fragmentDelegate.registerActivityResultReceiver(requestCode,receiverId);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragmentDelegate.onActivityResult(requestCode, resultCode, data);
    }

}