package com.mx.router;

import android.content.Context;

import com.mx.framework2.view.ui.ActivityStarter;

/**
 * Created by chenbaocheng on 16/11/19.
 */
public interface Pipe extends UriAccess {
    /**
     * Judge if client wants to get the result via return value
     */
    boolean needInstantReturn();

    /**
     * Success, but no data to return
     */
    void success();

    /**
     * Success and returns data
     *
     * @param data the data to return
     */
    void success(Object data);

    /**
     * User canceled current operation
     */
    void cancel();

    /**
     * Something went wrong during the operation
     *
     * @param message debug message describes what happened
     * @param reason  the exception if there is
     */
    void fail(String message, Throwable reason);

    /**
     * Something went wrong during the operation
     *
     * @param resultCode a code indicates the error
     * @param message    debug message describes what happened
     * @param reason     the exception if there is
     */
    void fail(int resultCode, String message, Throwable reason);

    Context getContext();

    /**
     * Get an ActivityStarter that can start an activity, which provides some advanced methods.
     */
    ActivityStarter getActivityStarter();
}
