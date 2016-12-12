package com.mx.router;

/**
 * Created by chenbaocheng on 16/11/19.
 */
public interface Route extends UriAccess {
    Router getRouter();

    Object route();

    int getResultCode();

    String getMessage();

    Throwable getReason();
}
