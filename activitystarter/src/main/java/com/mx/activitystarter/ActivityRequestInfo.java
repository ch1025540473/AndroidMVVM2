package com.mx.activitystarter;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Created by liuyuxuan on 2017/1/10.
 */

class ActivityRequestInfo {
    private WeakReference<ActivityIdentifiable> activityWeakReference;
    private String activityId;
    private Set<Integer> requestCodeSet;

    ActivityRequestInfo() {
        requestCodeSet = new HashSet<>();
    }

    ActivityIdentifiable getActivityIdentifiable() {
        return activityWeakReference == null ? null : activityWeakReference.get();
    }

    void setActivityIdentifiable(ActivityIdentifiable activity) {
        this.activityWeakReference = new WeakReference<>(activity);
    }

    String getActivityId() {
        return activityId;
    }

    void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    void addRequestCode(int requestCode) {
        if (!requestCodeSet.contains(requestCode)) {
            requestCodeSet.add(requestCode);
        }
    }

    void removeRequestCode(int requestCode) {
        requestCodeSet.remove(Integer.valueOf(requestCode));
    }

    public Set<Integer> getRequestCodeList() {
        return requestCodeSet;
    }
}
