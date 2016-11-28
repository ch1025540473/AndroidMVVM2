package com.mx.router;

import android.content.Context;

import com.mx.framework2.view.ui.ActivityStarter;

/**
 * Created by chenbaocheng on 16/11/19.
 */
public interface Pipe extends UriAccess {
    void success();

    void success(Object data);

    void fail(String message, Throwable reason);

    Context getContext();

    ActivityStarter getActivityStarter();
}
