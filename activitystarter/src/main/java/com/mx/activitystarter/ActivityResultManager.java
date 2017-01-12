package com.mx.activitystarter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.android.annotations.NonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by chenbaocheng on 16/9/27.
 */

public final class ActivityResultManager {
    private static volatile ActivityResultManager instance = null;
    private List<ActivityRequestInfo> activityRequestInfoList;

    public static ActivityResultManager getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (ActivityResultManager.class) {
            if (instance == null) {
                instance = new ActivityResultManager();
            }
            return instance;
        }
    }

    private static final int MIN_REQUEST_CODE = 65434;
    private static final int MAX_REQUEST_CODE = 65534;

    private AtomicInteger requestCode;
    private Map<Integer, ActivityResultCallback> callbackMap;

    @SuppressLint("UseSparseArrays")
    //Since request code may be large number, it is not suitable for SparseArray.
    private ActivityResultManager() {
        this.requestCode = new AtomicInteger(MIN_REQUEST_CODE);
        this.callbackMap = Collections.synchronizedMap(new HashMap<Integer, ActivityResultCallback>());
        this.activityRequestInfoList = Collections.synchronizedList(new LinkedList<ActivityRequestInfo>());
    }

    private int generateRequestCode() {
        if (requestCode.get() > MAX_REQUEST_CODE) {
            requestCode.set(MIN_REQUEST_CODE);
        }
        return requestCode.incrementAndGet();
    }

    private void setOnActivityResultCallback(int requestCode, ActivityResultCallback callback) {
        callbackMap.put(requestCode, callback);
    }

    public void onActivityCreate(@NonNull ActivityIdentifiable activityIdentifiable, Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            ActivityRequestInfo activityRequestInfo = new ActivityRequestInfo();
            activityRequestInfo.setActivityIdentifiable(activityIdentifiable);
            activityRequestInfo.setActivityId(activityIdentifiable.getActivityId());
            activityRequestInfoList.add(activityRequestInfo);
        } else {
            ActivityRequestInfo activityRequestInfo = findActivityRequestInfoById(activityIdentifiable.getActivityId());
            if (activityRequestInfo != null) {
                activityRequestInfo.setActivityIdentifiable(activityIdentifiable);
            } else {
                throw new NullPointerException("activityRequestInfo==null");
            }
        }
    }

    public void removeActivityResult(ActivityIdentifiable activityIdentifiable) {
        ActivityRequestInfo activityRequestInfo = findActivityRequestInfoById(activityIdentifiable.getActivityId());
        if (activityRequestInfo != null) {
            activityRequestInfoList.remove(activityRequestInfo);
            if (activityRequestInfo.getRequestCodeList() != null) {
                for (int requestCode : activityRequestInfo.getRequestCodeList()) {
                    onRequestCodeConsumed(requestCode);
                }
            }
        }
    }

    public int generateRequestCodeForCallback(ActivityResultCallback callback) {
        int requestCode = generateRequestCode();
        setOnActivityResultCallback(requestCode, callback);
        return requestCode;
    }

    public void registerRequestCode(ActivityIdentifiable activityIdentifiable, int requestCode) {
        requestCode &= 0xffff;
        ActivityRequestInfo activityRequestInfo = findActivityRequestInfoById(activityIdentifiable.getActivityId());
        if (activityRequestInfo != null) {
            activityRequestInfo.addRequestCode(requestCode);
        } else {
            throw new NullPointerException("activityRequestInfo==null");
        }
    }

    public void onActivityResult(ActivityIdentifiable activityIdentifiable, int requestCode, int resultCode, Intent intent) {
        requestCode &= 0xffff;
        ActivityResultCallback callback = callbackMap.remove(requestCode);
        if (callback != null) {
            callback.onActivityResult(resultCode, intent);
        }
        ActivityRequestInfo activityRequestInfo = findActivityRequestInfoById(activityIdentifiable.getActivityId());
        if (activityRequestInfo != null) {
            activityRequestInfo.removeRequestCode(requestCode);
        } else {
            throw new NullPointerException("activityRequestInfo==null");
        }
    }

    private void onRequestCodeConsumed(int requestCode) {
        callbackMap.remove(requestCode);
    }

    private ActivityRequestInfo findActivityRequestInfoById(@NonNull String activityId) {
        for (ActivityRequestInfo activityRequestInfo : activityRequestInfoList) {
            if (activityId.equals(activityRequestInfo.getActivityId())) {
                return activityRequestInfo;
            }
        }
        return null;
    }


}
