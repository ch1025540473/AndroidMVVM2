package com.mx.gunit;

import org.powermock.api.mockito.PowerMockito;

import java.lang.reflect.Method;

/**
 * Created by chenbaocheng on 17/5/9.
 */

public class MethodMock {
    private Object owner;
    private Method method;
    private InvocationListener listener;
    private Object returnValue;

    public Object getOwner() {
        return owner;
    }

    public Method getMethod() {
        return method;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public static class MethodMockBuilder implements MethodMockable {
        private String name;
        private Class<?> type;
        private Object owner;
        private Class<?>[] params;
        private InvocationListener listener;
        private Object returnValue;

        public MethodMockBuilder(String name) {
            this.name = name;
        }

        @Override
        public MethodMock done() {
            MethodMock methodMock = new MethodMock();
            if (owner != null) {
                methodMock.owner = owner;
                methodMock.method = PowerMockito.method(owner.getClass(), name, params);
            } else if (type != null) {
                methodMock.method = PowerMockito.method(type, name, params);
            } else {
                throw new RuntimeException("Cannot build MethodMock without owner and type");
            }

            methodMock.listener = listener;
            methodMock.returnValue = returnValue;

            MethodMockManager.getInstance().addMethodMock(methodMock);

            return methodMock;
        }

        public MethodMockBuilder setClass(Class<?> type) {
            this.type = type;
            return this;
        }

        public MethodMockBuilder setOwner(Object owner) {
            this.owner = owner;
            return this;
        }

        @Override
        public MethodMockable withParameters(Class<?>... params) {
            this.params = params;
            return this;
        }

        public MethodMockable withInvocationListener(InvocationListener listener) {
            this.listener = listener;
            return this;
        }

        @Override
        public MethodMockable thenReturn(Object returnValue) {
            this.returnValue = returnValue;
            return this;
        }
    }
}
