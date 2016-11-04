package com.mx.framework2.view.ui;

import com.android.annotations.NonNull;
import com.mx.engine.event.EventProxy;
import com.mx.engine.utils.CheckUtils;
import com.mx.framework2.event.Events;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Stack;

/**
 * Created by liuyuxuan on 2016/10/31.
 */
class ActivityStack {
    private static volatile ActivityStack activityStack;

    public static ActivityStack getInstance() {
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

    public void onActivityCreate(@NonNull BaseActivity baseActivity) {
        CheckUtils.checkNotNull(baseActivity);
        ActivityInfo activityInfo = activityInfoMap.get(baseActivity.getActivityId());
        if (activityInfo == null) {
            activityInfo = new ActivityInfo(baseActivity);
        }
        activityInfoMap.put(activityInfo.getActivityId(), activityInfo);
        updateActivityInfoState(baseActivity);
    }

    public void onActivityStart(@NonNull BaseActivity baseActivity) {
        updateActivityInfoState(baseActivity);
    }

    public void onActivityResume(@NonNull BaseActivity baseActivity) {
        updateActivityInfoState(baseActivity);
        Stack<ActivityInfo> stack = new Stack();
        stack.addAll(activityInfoMap.values());
        CheckUtils.checkArgument(stack.size() > 0);
        while (!stack.peek().getActivityId().equals(baseActivity.getActivityId())) {
            ActivityInfo activityInfo = stack.pop();
            if (activityInfo.getRunState().equals(RunState.Suspend)) {
                activityInfo.setRunState(RunState.Destroyed);
                EventProxy.getDefault().post(new Events.ActivityDestroyEvent(activityInfo));
                unRegisterRequestCode(activityInfo.getRequestCodeList());
                activityInfoMap.remove(activityInfo.getActivityId());
            }
        }
        Logger.t("ActivityStack").d("onActivityResume \n\nstack =" + stack);
    }

    public void onActivityPause(@NonNull BaseActivity baseActivity) {
        updateActivityInfoState(baseActivity);
    }

    private ActivityInfo updateActivityInfoState(@NonNull BaseActivity baseActivity) {
        CheckUtils.checkNotNull(baseActivity);
        ActivityInfo activityInfo = activityInfoMap.get(baseActivity.getActivityId());
        CheckUtils.checkNotNull(activityInfo);
        activityInfo.setRunState(baseActivity.getRunState());
        return activityInfo;
    }

    public void onActivityStop(@NonNull BaseActivity baseActivity) {
        updateActivityInfoState(baseActivity);
    }

    public void onActivityDestroy(@NonNull BaseActivity baseActivity) {
        CheckUtils.checkNotNull(baseActivity);
        ActivityInfo activityInfo = updateActivityInfoState(baseActivity);
        CheckUtils.checkNotNull(activityInfo);
        if (baseActivity.isFinishing()) {
            unRegisterRequestCode(activityInfo.getRequestCodeList());
            activityInfoMap.remove(activityInfo.getActivityId());
        }
        EventProxy.getDefault().post(new Events.ActivityDestroyEvent(activityInfo));
    }

    public void onActivityResult(int requestCode, BaseActivity baseActivity) {
        ActivityInfo activityInfo = updateActivityInfoState(baseActivity);
        List<Integer> requestCodeList = activityInfo.getRequestCodeList();
        requestCodeList.remove(new Integer(requestCode));
        ActivityResultManager.getInstance().onRequestCodeConsumed(requestCode);
    }

    private void unRegisterRequestCode(@NonNull List<Integer> requestCodeList) {
        CheckUtils.checkNotNull(requestCodeList);
        for (Integer requestCode : requestCodeList) {
            ActivityResultManager.getInstance().onRequestCodeConsumed(requestCode);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveRequestCodeRegisterEvent(Events.RequestCodeRegisterEvent event) {
        String activityId = event.getActivityId();
        ActivityInfo activityInfo = activityInfoMap.get(activityId);
        activityInfo.getRequestCodeList().add(event.getRequestCode());
    }
}
