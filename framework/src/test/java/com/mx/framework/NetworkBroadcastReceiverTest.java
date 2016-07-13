package com.mx.framework;

import android.app.Application;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mx.engine.BuildConfig;
import com.mx.engine.event.EventProxy;
import com.mx.engine.event.NetworkBroadcastEvent;
import com.mx.framework.NetworkBroadcastReceiver;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * Created by liuyuxuan on 16/5/24.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class NetworkBroadcastReceiverTest {

    Application application;

    @Mock
    NetworkInfo networkInfo;

    @Mock
    EventProxy eventProxy;

    NetworkBroadcastReceiver networkBroadcastReceiver;

    @Before
    public void setUp() {
        application = RuntimeEnvironment.application;
        MockitoAnnotations.initMocks(this);
        networkBroadcastReceiver = new NetworkBroadcastReceiver();
    }

    @Test
    public void testOnReceive() throws Exception {
        assertNotNull(application);
        ShadowApplication shadowApplication = ShadowApplication.getInstance();
        assertNotNull(shadowApplication);
        String action = "android.net.conn.CONNECTIVITY_CHANGE";
        Intent intent = new Intent(action);
        //测试是否注册广播接收者
        assertTrue(shadowApplication.hasReceiverForIntent(intent));

        NetworkBroadcastReceiver networkBroadcastReceiver = new NetworkBroadcastReceiver();
        networkBroadcastReceiver.onReceive(application, intent);

        int state = getNetWorkState("state");
        int STATE_CONNECTED_MOBILE = NetworkBroadcastEvent.STATE_CONNECTED_MOBILE;
        assertEquals(state, STATE_CONNECTED_MOBILE);
    }

    private int getNetWorkState(String stateFieldName) throws Exception {
        Field stateField = NetworkBroadcastReceiver.class.getDeclaredField(stateFieldName);
        int state = (int) stateField.get(NetworkBroadcastReceiver.class);
        return state;
    }


    @Test
    public void testOnState_mobile() throws Exception {
        Mockito.when(networkInfo.isConnected()).thenReturn(true);
        Mockito.when(networkInfo.getType()).thenReturn(ConnectivityManager.TYPE_MOBILE);
        networkBroadcastReceiver.onState(networkInfo, eventProxy);
        int state = getNetWorkState("state");
        int STATE_CONNECTED_MOBILE = NetworkBroadcastEvent.STATE_CONNECTED_MOBILE;
        assertEquals(state, STATE_CONNECTED_MOBILE);

        ArgumentCaptor<NetworkBroadcastEvent> argument = ArgumentCaptor.forClass(NetworkBroadcastEvent.class);
        Mockito.verify(eventProxy).post(argument.capture());

        assertEquals(true, argument.getValue().isConnected());
        assertEquals(STATE_CONNECTED_MOBILE, argument.getValue().getState());
    }

    @Test
    public void testOnState_wifi() throws Exception {
        Mockito.when(networkInfo.isConnected()).thenReturn(true);
        Mockito.when(networkInfo.getType()).thenReturn(ConnectivityManager.TYPE_WIFI);
        networkBroadcastReceiver.onState(networkInfo, eventProxy);
        int state = getNetWorkState("state");
        int STATE_CONNECTED_WIFI = NetworkBroadcastEvent.STATE_CONNECTED_WIFI;
        assertEquals(state, STATE_CONNECTED_WIFI);

        ArgumentCaptor<NetworkBroadcastEvent> argument = ArgumentCaptor.forClass(NetworkBroadcastEvent.class);
        Mockito.verify(eventProxy).post(argument.capture());

        assertEquals(true, argument.getValue().isConnected());
        assertEquals(STATE_CONNECTED_WIFI, argument.getValue().getState());
    }

    @Test
    public void testOnState_unknown() throws Exception {
        Mockito.when(networkInfo.isConnected()).thenReturn(true);
        Mockito.when(networkInfo.getType()).thenReturn(ConnectivityManager.TYPE_VPN);
        networkBroadcastReceiver = new NetworkBroadcastReceiver();
        networkBroadcastReceiver.onState(networkInfo, eventProxy);
        int state = getNetWorkState("state");
        int STATE_CONNECTED_UNKNOWN = NetworkBroadcastEvent.STATE_CONNECTED_UNKNOWN;
        assertEquals(state, STATE_CONNECTED_UNKNOWN);

        ArgumentCaptor<NetworkBroadcastEvent> argument = ArgumentCaptor.forClass(NetworkBroadcastEvent.class);
        Mockito.verify(eventProxy).post(argument.capture());
        assertEquals(true, argument.getValue().isConnected());
        assertEquals(STATE_CONNECTED_UNKNOWN, argument.getValue().getState());

    }


    @Test
    public void testOnState_disconnect() throws Exception {
        Mockito.when(networkInfo.isConnected()).thenReturn(false);
        networkBroadcastReceiver.onState(networkInfo, eventProxy);
        int state = getNetWorkState("state");
        int STATE_DISCONNECT = NetworkBroadcastEvent.STATE_DISCONNECT;
        assertEquals(state, STATE_DISCONNECT);
        ArgumentCaptor<NetworkBroadcastEvent> argument = ArgumentCaptor.forClass(NetworkBroadcastEvent.class);
        Mockito.verify(eventProxy).post(argument.capture());
        assertEquals(false, argument.getValue().isConnected());
        assertEquals(STATE_DISCONNECT, argument.getValue().getState());

        networkBroadcastReceiver.onState(null, eventProxy);
        state = getNetWorkState("state");
        assertEquals(state, STATE_DISCONNECT);
        argument = ArgumentCaptor.forClass(NetworkBroadcastEvent.class);
        Mockito.verify(eventProxy).post(argument.capture());
        assertEquals(false, argument.getValue().isConnected());
        assertEquals(STATE_DISCONNECT, argument.getValue().getState());

    }


}