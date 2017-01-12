package com.mx.activitystarter;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by liuyuxuan on 2017/1/10.
 */

class ActivityRequestInfo {
    private String activityId;
    private Set<Integer> requestCodeSet;

    ActivityRequestInfo() {
        requestCodeSet = new HashSet<>();
    }

    String getActivityId() {
        return activityId;
    }

    void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    void addRequestCode(int requestCode) {
        requestCodeSet.add(requestCode);
    }

    void removeRequestCode(int requestCode) {
        requestCodeSet.remove(Integer.valueOf(requestCode));
    }

    public Set<Integer> getRequestCodeList() {
        return requestCodeSet;
    }
}
