package com.mx.framework2.event;

import android.app.Activity;

import com.mx.engine.event.BroadcastEvent;
import com.mx.framework2.view.ui.BaseActivity;

/**
 * Created by liuyuxuan on 16/8/15.
 */
public class Events {
    public static class ActivityDestroyEvent extends BroadcastEvent{
        private final BaseActivity activity;

        public ActivityDestroyEvent(BaseActivity activity){
            this.activity = activity;
        }

        public BaseActivity getActivity() {
            return activity;
        }
    }
}
