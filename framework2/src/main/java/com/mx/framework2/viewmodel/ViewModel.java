package com.mx.framework2.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mx.engine.utils.CheckUtils;
import com.mx.framework2.Module;
import com.mx.engine.event.BroadcastEvent;
import com.mx.engine.event.EventProxy;
import com.mx.framework2.model.UseCase;
import com.mx.framework2.view.ui.BaseActivity;
import com.mx.framework2.view.ui.RunState;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

import static com.mx.engine.utils.CheckUtils.checkArgument;
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

    public final <T extends BroadcastEvent> void postEvent(T event) {
        EventProxy.getDefault().post(event);
    }

}
