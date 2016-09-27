package com.mx.framework2.view.ui;

import android.annotation.SuppressLint;
import android.content.Intent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by chenbaocheng on 16/9/27.
 */

public final class ActivityResultManager {
    private static volatile ActivityResultManager instance = null;

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
    }

    public int generateRequestCode() {
        if (requestCode.get() > MAX_REQUEST_CODE) {
            requestCode.set(MIN_REQUEST_CODE);
        }

        return requestCode.incrementAndGet();
    }

    public void setOnActivityResultCallback(int requestCode, ActivityResultCallback callback) {
        callbackMap.put(requestCode, callback);
    }

    public int generateRequestCodeForCallback(ActivityResultCallback callback) {
        int requestCode = generateRequestCode();
        setOnActivityResultCallback(requestCode, callback);

        return requestCode;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        ActivityResultCallback callback = callbackMap.remove(requestCode);
        if (callback != null) {
            callback.onActivityResult(resultCode, intent);
        }
    }
}
