package com.mx.framework2.event;

import com.mx.framework2.view.ui.ActivityStarter;

/**
 * Created by liuyuxuan on 16/10/8.
 */

public class BroadcastEvent extends com.mx.engine.event.BroadcastEvent {
    private ActivityStarter activityStarter;

    public ActivityStarter getActivityStarter() {
        return activityStarter;
    }

    public void setActivityStarter(ActivityStarter activityStarter) {
        this.activityStarter = activityStarter;
    }
}
