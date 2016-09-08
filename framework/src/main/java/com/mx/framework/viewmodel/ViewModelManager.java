package com.mx.framework.viewmodel;

import android.os.Bundle;

import com.mx.engine.utils.CheckUtils;
import com.mx.framework.view.RunState;

import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * Created by liuyuxuan on 16/4/20.
 */
@Deprecated
public class ViewModelManager {

    private final LinkedHashMap<String, ViewModel> viewModelMap;
    Bundle savedInstanceState;

    public ViewModelManager(Bundle savedInstanceState) {
        viewModelMap = new LinkedHashMap<>();
        this.savedInstanceState = savedInstanceState;
    }


    public <T extends ViewModel> T getViewModel(Class<T> modelClass) {
        return (T) viewModelMap.get(modelClass.getName());
    }

    public void addViewModel(ViewModel vm) {
        CheckUtils.checkNotNull(vm);
        vm.create(savedInstanceState);
        CheckUtils.checkArgument(vm.getRunState() == RunState.Created);
        viewModelMap.put(vm.getClass().getName(), vm);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        for (ViewModel vm : viewModelMap.values()) {
            vm.onWindowFocusChanged(hasFocus);
        }
    }

    public void start() {
        for (ViewModel model : viewModelMap.values()) {
            model.start();
        }
    }

    public void resume() {

        for (ViewModel model : viewModelMap.values()) {
            model.resume();
        }
    }

    public void pause() {
        for (ViewModel model : viewModelMap.values()) {
            model.pause();
        }
    }

    public void stop() {
        for (ViewModel model : viewModelMap.values()) {
            model.stop();
        }
    }

    public void saveInstanceState(Bundle outState) {

        for (ViewModel model : viewModelMap.values()) {
            model.onSaveInstanceState(outState);
        }
    }

    public Collection<ViewModel> getAllModel() {
        return viewModelMap.values();
    }

    public void restoreInstanceState(Bundle savedInstanceState) {
        for (ViewModel model : viewModelMap.values()) {
            model.onRestoreInstanceState(savedInstanceState);
        }
    }

}
