package com.mx.framework2.viewmodel;

/**
 * Created by liuyuxuan on 16/8/19.
 */
public enum LifecycleState {
    Created(0),
    Started(1),
    Resumed(2),
    Paused(3),
    Stopped(4);
    int value;

    LifecycleState(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
