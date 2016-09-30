package com.mx.framework2.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mx.engine.utils.CheckUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by liuyuxuan on 16/4/20.
 */
// TODO 1 visit 优化, 2 savedInstenceState  于viewModel 一一对应;
public class ViewModelManager {
    private static final String BUNDLE_KEY_ACTIVITY_RESULT_RECEIVERS = "framework_activity_result_receivers";

    private final List<LifecycleViewModel> lifecycleViewModelList;
    private Bundle savedInstanceState;
    private LifecycleState lifecycleState;
    private List<Visitor<LifecycleViewModel>> visitors = new LinkedList<>();

    @SuppressLint("UseSparseArrays") // Request codes are too large to fit in a SparseArray
    private HashMap<Integer, String> activityResultReceivers = new HashMap<>();

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
            vm.resume();
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

    public ViewModelManager() {
        lifecycleViewModelList = new LinkedList<>();
    }

    public void setSavedInstanceState(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
    }

    public void addViewModel(LifecycleViewModel lifecycle) {
        CheckUtils.checkNotNull(lifecycle);

        if (lifecycleViewModelList.contains(lifecycle)) {
            return;
        }
        if (lifecycle.getTag() != null && findViewModelByTag(lifecycle.getTag()) != null) {
            throw new RuntimeException("Duplicated view model tag: " + lifecycle.getTag());
        }
        lifecycleViewModelList.add(lifecycle);

        for (Visitor<LifecycleViewModel> visitor : visitors) {
            lifecycle.accept(visitor);
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
        if (savedInstanceState != null) {
            activityResultReceivers = (HashMap<Integer, String>) savedInstanceState.getSerializable(BUNDLE_KEY_ACTIVITY_RESULT_RECEIVERS);
        }

        visitors.add(onCreateVisitor);
        lifecycleState = LifecycleState.Created;
        for (Lifecycle lifecycle : lifecycleViewModelList) {
            lifecycle.accept(onCreateVisitor);
        }
    }

    public LifecycleViewModel findViewModelByTag(String tag) {
        CheckUtils.checkNotNull(tag);
        for (LifecycleViewModel viewModel : lifecycleViewModelList) {
            if (tag.equals(viewModel.getTag())) {
                return viewModel;
            }
        }

        return null;
    }

    public void registerActivityResultReceiver(int requestCode, String viewModelId) {
        activityResultReceivers.put(requestCode, viewModelId);
    }

    public void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        Visitor<LifecycleViewModel> visitor = new Visitor<LifecycleViewModel>() {
            @Override
            public void visit(LifecycleViewModel vm) {
                String tag = activityResultReceivers.get(requestCode);
                if (tag != null && tag.equals(vm.getTag())) {
                    activityResultReceivers.remove(requestCode);
                    vm.onActivityResult(requestCode, resultCode, intent);
                }
            }
        };
        visitors.add(visitor);

        for (Lifecycle lifecycle : lifecycleViewModelList) {
            lifecycle.accept(visitor);
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

    public void saveInstanceState(Bundle savedInstanceState) {
        onSaveInstanceState.setParameter(savedInstanceState);
        visitors.add(onSaveInstanceState);
        for (Lifecycle lifecycle : lifecycleViewModelList) {
            lifecycle.accept(onSaveInstanceState);
        }
        savedInstanceState.putSerializable(BUNDLE_KEY_ACTIVITY_RESULT_RECEIVERS, activityResultReceivers);
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
