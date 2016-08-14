package com.mx.framework2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mx.engine.event.EventProxy;
import com.mx.engine.event.NetworkBroadcastEvent;

/**
 * Created by liuyuxuan on 16/5/13.
 */
public final class NetworkBroadcastReceiver extends BroadcastReceiver {
    static int state = NetworkBroadcastEvent.STATE_UNINIT;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (android.net.ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivityManager) {
            onState(connectivityManager.getActiveNetworkInfo(), EventProxy.getDefault());
        }
    }

    protected void onState(NetworkInfo networkInfo, EventProxy eventProxy) {
        int stateTo = NetworkBroadcastEvent.STATE_DISCONNECT;

        if (networkInfo == null || !networkInfo.isConnected()) {
            stateTo = NetworkBroadcastEvent.STATE_DISCONNECT;
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            stateTo = NetworkBroadcastEvent.STATE_CONNECTED_MOBILE;
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            stateTo = NetworkBroadcastEvent.STATE_CONNECTED_WIFI;
        } else {
            stateTo = NetworkBroadcastEvent.STATE_CONNECTED_UNKNOWN;
        }

        if (state != stateTo) {
            state = stateTo;
            if (eventProxy != null) {
                eventProxy.post(new NetworkBroadcastEvent(state));
            }
        }
    }
}
