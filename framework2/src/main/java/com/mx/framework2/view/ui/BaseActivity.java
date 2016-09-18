package com.mx.framework2.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;

import com.mx.engine.event.EventProxy;
import com.mx.engine.utils.CheckUtils;
import com.mx.engine.utils.ObjectUtils;
import com.mx.framework2.event.Events;
import com.mx.framework2.model.UseCaseHolder;
import com.mx.framework2.viewmodel.Lifecycle;
import com.mx.framework2.viewmodel.LifecycleViewModel;
import com.mx.framework2.viewmodel.ViewModelManager;
import com.mx.framework2.viewmodel.ViewModelScope;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

//import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by liuyuxuan on 16/4/20.
 * 1,分配ViewModel的职责;
 * 2,提供viewModel的共享数据;
 * 3,提供ViewModel的通信;
 */
//TODO 监测生命状态
public class BaseActivity extends FragmentActivity implements UseCaseHolder, ViewModelScope {
    private final String UUID_KEY = "UUID_KEY_FRAMEWORK2_" + getClass().getName();
    private String uuid;
    private final SparseArray<ActivityResultListener> activityResultListeners = new SparseArray<>();
    private final List<Reference<BaseFragment>> fragments = new LinkedList<>();

    public interface ActivityResultListener {
        void onActivityResult(int requestCode, int resultCode, Intent intent);
    }

    private List<BaseFragment> getFragments() {

        List<BaseFragment> baseFragments = new LinkedList<>();

        ListIterator<Reference<BaseFragment>> listIterator = fragments.listIterator();

        while (listIterator.hasNext()) {
            Reference<BaseFragment> fragmentReference = listIterator.next();
            BaseFragment baseFragment = fragmentReference.get();
            if (null == baseFragment) {
                listIterator.remove();
            } else {
                baseFragments.add(baseFragment);
            }
        }

        return baseFragments;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        viewModelManager.restart();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof BaseFragment) {
            fragments.add(new SoftReference<BaseFragment>((BaseFragment) fragment));
        }
    }


    public final void registerActivityResultListener(int requestCode, ActivityResultListener activityResultListener) {
        activityResultListeners.put(requestCode, activityResultListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ActivityResultListener activityResultListener = activityResultListeners.get(requestCode);
        if (null != activityResultListener) {
            activityResultListener.onActivityResult(requestCode, resultCode, data);
            activityResultListeners.remove(requestCode);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private RunState runState = null;

    private boolean isHasFocus = false;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus != isHasFocus) {
            getViewModelManager().onWindowFocusChanged(hasFocus);
            isHasFocus = hasFocus;
        }
    }

    public RunState getRunState() {
        return this.runState;
    }


    private ViewModelManager viewModelManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            uuid = savedInstanceState.getString(UUID_KEY);
            CheckUtils.checkNotNull(uuid, "uuid is null");
        } else {
            uuid = UUID.randomUUID().toString();
        }
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        viewModelManager = new ViewModelManager(savedInstanceState);
        viewModelManager.create();
        this.runState = RunState.Created;
        EventProxy.getDefault().register(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getViewModelManager().start();
        this.runState = RunState.Started;
        EventProxy.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getViewModelManager().stop();
        EventProxy.getDefault().unregister(this);
        this.runState = RunState.Stoped;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getViewModelManager().saveInstanceState(outState);
        outState.putString(UUID_KEY, uuid);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        getViewModelManager().restoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        this.runState = RunState.Destroyed;
        super.onDestroy();
        activityResultListeners.clear();
        EventProxy.getDefault().post(new Events.ActivityDestroyEvent(this));
    }

    @Override
    protected void onResume() {
        this.runState = RunState.Resumed;
        super.onResume();
        getViewModelManager().resume();
    }

    @Override
    protected void onPause() {
        this.runState = RunState.Paused;
        super.onPause();
        getViewModelManager().pause();
    }

    public final ViewModelManager getViewModelManager() {
        CheckUtils.checkNotNull(viewModelManager);
        return viewModelManager;
    }

    @Override
    public String getUseCaseHolderId() {
        return uuid;
    }

    @Override
    public void addViewModel(LifecycleViewModel lifecycle) {
        getViewModelManager().addViewModel(lifecycle);
    }

    @Override
    public void removeViewModel(LifecycleViewModel lifecycle) {
        getViewModelManager().removeViewModel(lifecycle);
    }

}
