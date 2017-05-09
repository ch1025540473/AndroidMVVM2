package com.mx.gunit;

/**
 * Created by chenbaocheng on 17/5/9.
 */

public interface MethodMockable {
    MethodMock done() throws NoSuchMethodException;

    MethodMockable withParameters(Class<?>... params);

    MethodMockable withInvocationListener(InvocationListener listener);

    MethodMockable thenReturn(Object returnValue);
}
