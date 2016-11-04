package com.mx.framework2.view.ui;

import android.app.Activity;

import com.mx.engine.utils.CheckUtils;
import com.mx.framework2.model.UseCaseHolder;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by liuyuxuan on 2016/10/31.
 */

public class ActivityInfo {
    private String activityId;
    private List<Integer> requestCodeList;
    private WeakReference<BaseActivity> activityRef;
    private UseCaseHolder useCaseHolder;
    private RunState runState;
    private String activityName;

    @Override
    public String toString() {
        return "{activityName= " + activityName + "} { activityId=" + activityId + "} { runState=" + runState + "}\n"
                + "requestCodeList {" + requestCodeList + "}";
    }

    public String getActivityName() {
        return activityName;
    }

    public ActivityInfo(BaseActivity activity) {
        this.activityRef = new WeakReference<>(activity);
        CheckUtils.checkNotNull(activity);
        activityId = activity.getActivityId();
        activityName = activity.getClass().getName();
        useCaseHolder = new UseCaseHolder() {
            @Override
            public String getUseCaseHolderId() {
                return activityId;
            }
        };
        requestCodeList = new LinkedList<>();
        runState = activity.getRunState();
    }

    public RunState getRunState() {
        return runState;
    }

    public boolean isFinished() {
        return runState == RunState.Destroyed;
    }

    public UseCaseHolder getUseCaseHolder() {
        return useCaseHolder;
    }

    public String getActivityId() {
        return activityId;
    }

    List<Integer> getRequestCodeList() {
        return requestCodeList;
    }

    void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    void setRequestCodeList(List<Integer> requestCodeList) {
        this.requestCodeList = requestCodeList;
    }

    WeakReference<BaseActivity> getActivityRef() {
        return activityRef;
    }

    void setActivityRef(WeakReference<BaseActivity> activityRef) {
        this.activityRef = activityRef;
    }

    void setRunState(RunState runState) {
        this.runState = runState;
    }

}
