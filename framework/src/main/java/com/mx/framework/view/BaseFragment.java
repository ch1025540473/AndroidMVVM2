
package com.mx.framework.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mx.engine.utils.CheckUtils;
import com.mx.framework.DataSourceChangeAware;
import com.mx.framework.viewmodel.ViewModel;
import com.mx.framework.viewmodel.ViewModelManager;

import java.util.Collection;
import java.util.List;

/**
 * Created by liuyuxuan on 16/4/20.
 */
@Deprecated
public class BaseFragment extends Fragment implements DataSourceChangeAware {

    private ViewModelManager viewModelManager;
    private RunState runState;

    public final ViewModelManager getViewModelManager() {
        CheckUtils.checkNotNull(viewModelManager);
        return viewModelManager;
    }

    public synchronized <T extends ViewModel> T getViewModel(Class<T> modelClass) {
        return getViewModelManager().getViewModel(modelClass);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModelManager = new ViewModelManager(savedInstanceState);
        runState = RunState.Created;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getViewModelManager().saveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        runState = RunState.Created;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        attachActivity();

    }

    private void attachActivity() {
        Collection<ViewModel> viewModels = getViewModelManager().getAllModel();
        if (viewModels != null) {
            for (ViewModel vm : viewModels) {
                FragmentActivity activity = getActivity();
                if (null != vm && activity instanceof BaseActivity) {
                    vm.setActivity((BaseActivity) activity);
                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        attachActivity();
        getViewModelManager().start();
        runState = RunState.Started;
    }

    @Override
    public void onStop() {
        super.onStop();
        getViewModelManager().stop();
        runState = RunState.Stoped;

        Collection<ViewModel> viewModels = getViewModelManager().getAllModel();
        if (viewModels != null) {
            for (ViewModel vm : viewModels) {
                if (vm != null) {
                    vm.setActivity(null);
                }
            }
        }
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
    public void requestDataReloading(DataSourceChangeAware sender) {
        reloadData(sender);
    }

    @Override
    public void reloadData(DataSourceChangeAware sender) {

        Collection<ViewModel> collections = getViewModelManager().getAllModel();

        if (null != collections) {

            for (ViewModel vm : collections) {
                vm.reloadData(this);
            }
        }

        List<Fragment> fragments = getChildFragmentManager().getFragments();

        if (null != fragments) {

            for (Fragment fragment : fragments) {
                if (fragment instanceof BaseFragment) {
                    BaseFragment baseFragment = (BaseFragment) fragment;
                    baseFragment.reloadData(sender);
                }
            }
        }
    }

}
