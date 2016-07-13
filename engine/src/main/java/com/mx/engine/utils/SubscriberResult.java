package com.mx.engine.utils;

import rx.Subscriber;

/**
 * Created by liuyuxuan on 16/6/28.
 */
public abstract class SubscriberResult<Success> extends Subscriber<Success> {

    @Deprecated
    @Override
    public final void onCompleted() {

    }

    @Deprecated
    @Override
    public final void onError(Throwable e) {

    }

    @Deprecated
    @Override
    public final void onNext(Success success) {

    }

    public abstract void onSuccess(Success success);

    public abstract void onError(int errorCode, String msg);

    public abstract void onFailure(Throwable e);

}
