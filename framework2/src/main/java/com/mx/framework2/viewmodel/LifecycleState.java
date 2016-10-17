package com.mx.framework2.viewmodel;

/**
 * Created by liuyuxuan on 16/8/19.
 */
public enum LifecycleState {
    Init(-1),
    Created(0),
    Started(1),
    Restarted(2),
    Resumed(3),
    Paused(4),
    Stopped(5);
    int value;

    LifecycleState(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
