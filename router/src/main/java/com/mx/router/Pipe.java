package com.mx.router;

import com.mx.framework2.view.ui.ActivityStarter;

/**
 * Created by chenbaocheng on 16/11/19.
 */
public interface Pipe extends UriAccess {
    void success(Object data);

    void fail(String message, Throwable reason);

    ActivityStarter getActivityStarter();
}
