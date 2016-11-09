package com.mx.framework2.view.ui;

import com.android.annotations.NonNull;
import com.mx.engine.event.EventProxy;
import com.mx.engine.utils.CheckUtils;
import com.mx.framework2.event.Events;
import com.orhanobut.logger.Logger;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Stack;

/**
 * Created by liuyuxuan on 2016/10/31.
 */
class ActivityStack {
    private static volatile ActivityStack activityStack;

    static ActivityStack getInstance() {
        if (activityStack == null) {
            synchronized (ActivityStack.class) {
                if (activityStack == null) {
                    activityStack = new ActivityStack();
                }
            }
        }
        return activityStack;
    }

    private LinkedHashMap<String, ActivityInfo> activityInfoMap = new LinkedHashMap<>();

    private ActivityStack() {
        EventProxy.getDefault().register(this);
    }

    void onActivityCreate(@NonNull BaseActivity baseActivity) {
        CheckUtils.checkNotNull(baseActivity);
        ActivityInfo activityInfo = activityInfoMap.get(baseActivity.getActivityId());
        if (activityInfo == null) {
            activityInfo = new ActivityInfo(baseActivity);
        }
        activityInfoMap.put(activityInfo.getActivityId(), activityInfo);
        updateActivityInfoState(baseActivity);
    }

    void onActivityStart(@NonNull BaseActivity baseActivity) {
        updateActivityInfoState(baseActivity);
    }

    void onActivityResume(@NonNull BaseActivity baseActivity) {
        updateActivityInfoState(baseActivity);
        Stack<ActivityInfo> stack = new Stack<>();
        stack.addAll(activityInfoMap.values());
        CheckUtils.checkArgument(stack.size() > 0);
        while (!stack.peek().getActivityId().equals(baseActivity.getActivityId())) {
            ActivityInfo activityInfo = stack.pop();
            if (activityInfo.getRunState().equals(RunState.Hibernating)) {
                activityInfo.setRunState(RunState.Destroyed);
                onActivityDestroy(activityInfo);
            }
        }
        Logger.t("ActivityStack").d("onActivityResume \n stack =" + activityInfoMap.values());
    }

    void onActivityPause(@NonNull BaseActivity baseActivity) {
        updateActivityInfoState(baseActivity);
    }


    void onActivityStop(@NonNull BaseActivity baseActivity) {
        updateActivityInfoState(baseActivity);
    }

    void onActivityDestroy(@NonNull BaseActivity baseActivity) {
        updateActivityInfoState(baseActivity);
        onActivityDestroy(getActivityInfo(baseActivity));
    }

    private void onActivityDestroy(@NonNull ActivityInfo activityInfo) {
        Logger.t("ActivityStack").d("onActivityDestroy \n " + activityInfo);
        if (activityInfo.isFinished()) {
            cancelFlyRequestCodes(activityInfo.getFlyingRequestCodes());
            activityInfoMap.remove(activityInfo.getActivityId());
        }
        EventProxy.getDefault().post(new Events.ActivityDestroyEvent(activityInfo));
    }

    //TODO 下期重构 FlyingRequestCode 由ActivityResultManager管理；
    void onActivityResult(int requestCode, BaseActivity baseActivity) {
        requestCode &= 0xffff;
        updateActivityInfoState(baseActivity);
        ActivityInfo activityInfo = getActivityInfo(baseActivity);
        activityInfo.removeFlyingRequestCode(requestCode);
        ActivityResultManager.getInstance().onRequestCodeConsumed(requestCode);
        Logger.t("ActivityStack").d("onActivityResult \n stack =" + activityInfoMap.values());
    }

    void onStartActivityForResult(int requestCode, BaseActivity baseActivity) {
        requestCode &= 0xffff;
        updateActivityInfoState(baseActivity);
        ActivityInfo activityInfo = getActivityInfo(baseActivity);
        activityInfo.addFlyingRequestCode(requestCode);
        Logger.t("ActivityStack").d("onStartActivityForResult\n stack =" + activityInfoMap.values());
    }

    private void updateActivityInfoState(@NonNull BaseActivity baseActivity) {
        CheckUtils.checkNotNull(baseActivity);
        ActivityInfo activityInfo = activityInfoMap.get(baseActivity.getActivityId());
        CheckUtils.checkNotNull(activityInfo);
        activityInfo.setRunState(baseActivity.getRunState());
    }

    ActivityInfo getActivityInfo(@NonNull BaseActivity baseActivity) {
        CheckUtils.checkNotNull(baseActivity);
        ActivityInfo activityInfo = activityInfoMap.get(baseActivity.getActivityId());
        CheckUtils.checkNotNull(activityInfo);
        return activityInfo;
    }

    private void cancelFlyRequestCodes(@NonNull List<Integer> flyRequestCodes) {
        CheckUtils.checkNotNull(flyRequestCodes);
        for (Integer requestCode : flyRequestCodes) {
            ActivityResultManager.getInstance().onRequestCodeConsumed(requestCode);
        }
    }
}
