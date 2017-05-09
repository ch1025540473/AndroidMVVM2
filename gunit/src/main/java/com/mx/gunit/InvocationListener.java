package com.mx.gunit;

import java.lang.reflect.Method;

/**
 * Created by chenbaocheng on 17/5/9.
 */
public interface InvocationListener {
    void onInvoke(Object obj, Method method, Object args);
}
