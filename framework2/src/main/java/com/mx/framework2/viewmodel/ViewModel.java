package com.mx.framework2.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.mx.engine.event.EventProxy;
import com.mx.engine.utils.CheckUtils;
import com.mx.framework2.event.BroadcastEvent;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import static com.mx.engine.utils.CheckUtils.checkNotNull;

/**
 * Created by liuyuxuan on 16/4/20.
 */
public abstract class ViewModel extends BaseObservable {
    private static Handler handler;
    private Reference<Context> contextRef;

    protected final void runOnUIThread(@NonNull Runnable runnable, long delayTime) {
        CheckUtils.checkNotNull(runnable);
        if (null == handler) {
            handler = new Handler();
        }
        handler.postDelayed(runnable, delayTime);
    }

    protected final void runOnUIThread(@NonNull Runnable runnable) {
        runOnUIThread(runnable, 0);
    }


    public final Context getContext() {
        return contextRef.get();
    }

    public final void setContext(@NonNull Context context) {
        checkNotNull(context);
        this.contextRef = new WeakReference<Context>(context);
    }

    public <T extends BroadcastEvent> void postEvent(T event) {
        EventProxy.getDefault().post(event);
    }

}
