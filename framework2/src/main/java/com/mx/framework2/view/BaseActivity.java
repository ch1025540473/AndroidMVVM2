package com.mx.framework2.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;

import com.mx.engine.utils.CheckUtils;
import com.mx.engine.utils.ObjectUtils;
import com.mx.framework2.DataSourceChangeAware;
import com.mx.framework2.viewmodel.ViewModel;
import com.mx.framework2.viewmodel.ViewModelManager;

import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

//import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by liuyuxuan on 16/4/20.
 * 1,分配ViewModel的职责;
 * 2,提供viewModel的共享数据;
 * 3,提供ViewModel的通信;
 */
public class BaseActivity extends FragmentActivity implements DataSourceChangeAware {

    // add  get put

    private static final String VIEW_MODEL_SHARE_KEY = "view_model_share_key";

    private HashMap<String, Object> modelViewShareMap = new HashMap<>();

    private final SparseArray<ActivityResultListener> activityResultListeners = new SparseArray<>();


    private final List<Reference<BaseFragment>> fragments = new LinkedList<>();

    public <Value extends Serializable> void put(@NonNull String key, @NonNull Value value) {
//        checkNotNull(key);
//        checkNotNull(value);
        modelViewShareMap.put(key, value);
    }

    public <Value extends Serializable> Value get(@NonNull String key) {
//        checkNotNull(key);

        return (Value) modelViewShareMap.get(key);
    }

    public List<BaseFragment> getFragments() {

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
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof BaseFragment) {
            fragments.add(new SoftReference<BaseFragment>((BaseFragment) fragment));
        }
    }

    @Override
    public void requestDataReloading(DataSourceChangeAware sender) {
        reloadData(sender);
    }

    @Override
    public void reloadData(DataSourceChangeAware sender) {
        Collection<ViewModel> collections = getViewModelManager().getAllModel();

        if (null != collections) {
            for (ViewModel vm : collections) {
                vm.reloadData(sender);
            }
        }

        for (BaseFragment baseFragment : getFragments()) {
            baseFragment.reloadData(sender);
        }
    }

    public static interface ActivityResultListener {
        void onActivityResult(int requestCode, int resultCode, Intent intent);
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
        viewModelManager = new ViewModelManager(savedInstanceState);
        if (null != savedInstanceState) {
            modelViewShareMap = ObjectUtils.hexStringToObject(savedInstanceState.getString(VIEW_MODEL_SHARE_KEY));
        } else {
            modelViewShareMap = new HashMap<>();
        }
        this.runState = RunState.Created;
    }

    @Override
    protected void onStart() {
        super.onStart();
        getViewModelManager().start();
        this.runState = RunState.Started;
    }

    @Override
    protected void onStop() {
        super.onStop();
        getViewModelManager().stop();
        this.runState = RunState.Stoped;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(VIEW_MODEL_SHARE_KEY, ObjectUtils.objectToHexString(modelViewShareMap));
        getViewModelManager().saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        this.runState = RunState.Destroyed;
        activityResultListeners.clear();
        super.onDestroy();
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


    /**
     * 显示进度条
     */
    public void showLoadingDialog() {

    }

    /**
     * @param message    ： 提示信息
     * @param cancelable ：是否可取消
     */
    public void showLoadingDialog(String message, boolean cancelable) {

    }

    /**
     * 隐藏进度条
     */
    public void dismissLoadingDialog() {

    }


}
