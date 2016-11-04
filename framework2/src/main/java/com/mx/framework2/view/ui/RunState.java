package com.mx.framework2.view.ui;

/**
 * Created by liuyuxuan on 16/4/25.
 */
public enum RunState {
    Created(0),
    Started(1),
    Resumed(2),
    Paused(3),
    Stopped(4),
    Suspend(5),
    Destroyed(6);
    int value;

    RunState(int state) {
        this.value = value;
    }
}
