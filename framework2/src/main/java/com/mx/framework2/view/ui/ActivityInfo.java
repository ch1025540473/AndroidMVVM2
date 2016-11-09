package com.mx.framework2.view.ui;

import com.mx.engine.utils.CheckUtils;
import com.mx.framework2.model.UseCaseHolder;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by liuyuxuan on 2016/10/31.
 */

public class ActivityInfo implements UseCaseHolder {
    private String activityId;
    private List<Integer> flyingRequestCodes;
    private WeakReference<BaseActivity> activityRef;
    private RunState runState;
    private String activityName;

    @Override
    public String toString() {
        return "{activityName= " + activityName + "} { activityId=" + activityId + "} { runState=" + runState + "}\n"
                + "flyingRequestCodes {" + flyingRequestCodes + "}";
    }

    public String getActivityName() {
        return activityName;
    }

    public ActivityInfo(BaseActivity activity) {
        this.activityRef = new WeakReference<>(activity);
        CheckUtils.checkNotNull(activity);
        activityId = activity.getActivityId();
        activityName = activity.getClass().getName();
        flyingRequestCodes = new LinkedList<>();
        runState = activity.getRunState();
    }

    public RunState getRunState() {
        return runState;
    }

    public boolean isFinished() {
        return runState == RunState.Destroyed;
    }

    @SuppressWarnings("all")
    public String getActivityId() {
        return activityId;
    }

    List<Integer> getFlyingRequestCodes() {
        return Collections.unmodifiableList(flyingRequestCodes);
    }

    void removeFlyingRequestCode(int flyingRequestCode) {
        flyingRequestCodes.remove(Integer.valueOf(flyingRequestCode));
    }

    void addFlyingRequestCode(int flyingRequestCode) {
        flyingRequestCodes.add(flyingRequestCode);
    }

    void setRunState(RunState runState) {
        this.runState = runState;
    }

    @Override
    public String getUseCaseHolderId() {
        return activityId;
    }
}
