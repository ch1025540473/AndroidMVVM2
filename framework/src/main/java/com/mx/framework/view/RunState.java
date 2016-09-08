package com.mx.framework.view;

/**
 * Created by liuyuxuan on 16/4/25.
 */
@Deprecated
public enum RunState {
    Created(0),
    Started(1),
    Resumed(2),
    Paused(3),
    Stoped(4),
    Destroyed(5);
    int value;

    RunState(int state) {
        this.value = value;
    }
}
