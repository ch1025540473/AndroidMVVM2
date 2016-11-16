package com.mx.router;

/**
 * Created by chenbaocheng on 16/11/14.
 */

public interface Callback<T> {
    void onRouteSuccess(Route route, T data);

    void onRouteFailure(Route route);
}