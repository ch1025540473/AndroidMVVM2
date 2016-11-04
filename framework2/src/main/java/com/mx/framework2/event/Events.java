package com.mx.framework2.event;

import android.app.Activity;

import com.mx.engine.event.BroadcastEvent;
import com.mx.framework2.view.ui.ActivityInfo;
import com.mx.framework2.view.ui.BaseActivity;
import com.mx.framework2.viewmodel.Lifecycle;
import com.mx.framework2.viewmodel.ViewModelScope;

/**
 * Created by liuyuxuan on 16/8/15.
 */
public class Events {
    public static class ActivityDestroyEvent extends BroadcastEvent {
        private final ActivityInfo activityInfo;

        public ActivityDestroyEvent(ActivityInfo activityInfo) {
            this.activityInfo = activityInfo;
        }

        public ActivityInfo getActivityInfo() {
            return activityInfo;
        }
    }

    public static class RequestCodeRegisterEvent extends BroadcastEvent {
        private int requestCode;
        private String activityId;

        public RequestCodeRegisterEvent(int requestCode, String activityId) {
            this.requestCode = requestCode;
            this.activityId = activityId;
        }

        public int getRequestCode() {
            return requestCode;
        }

        public void setRequestCode(int requestCode) {
            this.requestCode = requestCode;
        }

        public String getActivityId() {
            return activityId;
        }

        public void setActivityId(String activityId) {
            this.activityId = activityId;
        }
    }

}
