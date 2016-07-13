package com.mx.engine.event;

/**
 * Created by liuyuxuan on 16/5/5.
 */
public class NetworkBroadcastEvent extends BroadcastEvent {

    private final int state;

    public NetworkBroadcastEvent(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public final boolean isConnected() {
        return (state == STATE_CONNECTED_MOBILE || state == STATE_CONNECTED_WIFI || state == STATE_CONNECTED_UNKNOWN);
    }

    public static final int STATE_UNINIT = 0;
    public static final int STATE_DISCONNECT = 1;
    public static final int STATE_CONNECTED_MOBILE = 2;
    public static final int STATE_CONNECTED_WIFI = 3;
    public static final int STATE_CONNECTED_UNKNOWN = 4;


}
