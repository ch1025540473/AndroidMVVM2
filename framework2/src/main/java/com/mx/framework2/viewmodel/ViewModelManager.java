package com.mx.framework2.viewmodel;

import android.content.Context;
import android.os.Bundle;

import com.mx.engine.utils.CheckUtils;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by liuyuxuan on 16/4/20.
 */
public class ViewModelManager {
    private final List<LifecycleViewModel> lifecycleViewModelList;
    Bundle savedInstanceState;
    LifecycleState lifecycleState;
    private List<Visitor<LifecycleViewModel>> visitors = new LinkedList<>();

    private Visitor<LifecycleViewModel> onCreateVisitor = new Visitor<LifecycleViewModel>() {
        @Override
        public void visit(LifecycleViewModel vm) {
            vm.create(savedInstanceState);
        }
    };
    private Visitor<LifecycleViewModel> onStartVisitor = new Visitor<LifecycleViewModel>() {
        @Override
        public void visit(LifecycleViewModel vm) {
            vm.start();
        }
    };
    private Visitor<LifecycleViewModel> onRestartVisitor = new Visitor<LifecycleViewModel>() {
        @Override
        public void visit(LifecycleViewModel vm) {
            vm.restart();
        }
    };
    private Visitor<LifecycleViewModel> onResumeVisitor = new Visitor<LifecycleViewModel>() {
        @Override
        public void visit(LifecycleViewModel vm) {
            vm.onResume();
        }
    };
    private Visitor<LifecycleViewModel> onPauseVisitor = new Visitor<LifecycleViewModel>() {
        @Override
        public void visit(LifecycleViewModel vm) {
            vm.pause();
        }
    };

    private Visitor<LifecycleViewModel> onStopVisitor = new Visitor<LifecycleViewModel>() {
        @Override
        public void visit(LifecycleViewModel vm) {
            vm.stop();
        }
    };

    private abstract class ParameterVisitor<Parameter> implements Visitor<LifecycleViewModel> {
        Parameter parameter;

        public ParameterVisitor(Parameter parameter) {
            this.parameter = parameter;
        }

        public Parameter getParameter() {
            return parameter;
        }

        public void setParameter(Parameter parameter) {
            this.parameter = parameter;
        }
    }

    private ParameterVisitor<Boolean> onUserVisibleHintVisitor =
            new ParameterVisitor<Boolean>(null) {
                @Override
                public void visit(LifecycleViewModel data) {
                    data.setUserVisibleHint(getParameter());
                }
            };

    private ParameterVisitor<Bundle> onSaveInstanceState =
            new ParameterVisitor<Bundle>(null) {
                @Override
                public void visit(LifecycleViewModel data) {
                    data.onSaveInstanceState(getParameter());
                }
            };

    private ParameterVisitor<Bundle> onRestoreInstanceState =
            new ParameterVisitor<Bundle>(null) {
                @Override
                public void visit(LifecycleViewModel data) {
                    data.onRestoreInstanceState(getParameter());
                }
            };

    private ParameterVisitor<Boolean> onWindowFocusChanged =
            new ParameterVisitor<Boolean>(null) {
                @Override
                public void visit(LifecycleViewModel data) {
                    data.onWindowFocusChanged(getParameter());
                }
            };

    public ViewModelManager(Bundle savedInstanceState) {
        lifecycleViewModelList = new LinkedList<>();
        this.savedInstanceState = savedInstanceState;

    }

    public void addViewModel(LifecycleViewModel lifecycle) {
        CheckUtils.checkNotNull(lifecycle);
        if (!lifecycleViewModelList.contains(lifecycle)) {
            lifecycleViewModelList.add(lifecycle);
            for (Visitor<LifecycleViewModel> visitor : visitors) {
                lifecycle.accept(visitor);
            }
        }
    }

    public void removeViewModel(LifecycleViewModel lifecycle) {
        CheckUtils.checkNotNull(lifecycle);
        lifecycleViewModelList.remove(lifecycle);
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        onUserVisibleHintVisitor.setParameter(isVisibleToUser);
        for (Lifecycle lifecycle : lifecycleViewModelList) {
            lifecycle.accept(onUserVisibleHintVisitor);
        }
        visitors.add(onUserVisibleHintVisitor);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        onWindowFocusChanged.setParameter(hasFocus);
        for (Lifecycle lifecycle : lifecycleViewModelList) {
            lifecycle.accept(onWindowFocusChanged);
        }
        visitors.add(onWindowFocusChanged);
    }

    public void create() {
        visitors.add(onCreateVisitor);
        lifecycleState = LifecycleState.Created;
        for (Lifecycle lifecycle : lifecycleViewModelList) {
            lifecycle.accept(onCreateVisitor);
        }
    }

    public void start() {
        visitors.add(onStartVisitor);
        lifecycleState = LifecycleState.Started;
        for (Lifecycle lifecycle : lifecycleViewModelList) {
            lifecycle.accept(onStartVisitor);
        }
    }

    public void restart() {
        visitors.add(onRestartVisitor);
        lifecycleState = LifecycleState.Started;
        for (Lifecycle lifecycle : lifecycleViewModelList) {
            lifecycle.accept(onRestartVisitor);
        }
    }

    public void resume() {
        lifecycleState = LifecycleState.Resumed;
        visitors.add(onResumeVisitor);
        for (Lifecycle lifecycle : lifecycleViewModelList) {
            lifecycle.accept(onResumeVisitor);
        }
    }

    public void pause() {
        lifecycleState = LifecycleState.Paused;
        visitors.add(onPauseVisitor);
        for (Lifecycle lifecycle : lifecycleViewModelList) {
            lifecycle.accept(onPauseVisitor);
        }
    }

    public void stop() {
        lifecycleState = LifecycleState.Stopped;
        for (Lifecycle lifecycle : lifecycleViewModelList) {
            lifecycle.accept(onStopVisitor);
        }
        visitors.clear();
    }

    public void saveInstanceState(Bundle outState) {
        onSaveInstanceState.setParameter(outState);
        visitors.add(onSaveInstanceState);
        for (Lifecycle lifecycle : lifecycleViewModelList) {
            lifecycle.accept(onSaveInstanceState);
        }
    }

    public void onAttachContext(Context context) {
        for (LifecycleViewModel lifecycle : lifecycleViewModelList) {
            lifecycle.setContext(context);
        }
    }

    public void restoreInstanceState(Bundle savedInstanceState) {
        onRestoreInstanceState.setParameter(savedInstanceState);
        visitors.add(onRestoreInstanceState);
        for (Lifecycle lifecycle : lifecycleViewModelList) {
            lifecycle.accept(onRestoreInstanceState);
        }
    }

}
