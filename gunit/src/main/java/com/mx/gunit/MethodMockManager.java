package com.mx.gunit;

import org.powermock.core.MockRepository;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.LinkedList;

/**
 * Created by chenbaocheng on 17/5/9.
 */

public class MethodMockManager {
    private static volatile MethodMockManager instance = null;

    public static MethodMockManager getInstance() {
        if (instance != null) {
            return instance;
        }

        synchronized (MethodMockManager.class) {
            if (instance == null) {
                instance = new MethodMockManager();
            }

            return instance;
        }
    }

    private LinkedList<MethodMock> methodMocks = new LinkedList<>();

    public void addMethodMock(MethodMock mock) {
        methodMocks.addLast(mock);
        if (!MockRepository.hasMethodProxy(mock.getMethod())) {
            MockRepository.putMethodProxy(mock.getMethod(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    MethodMock target = null;
                    for (MethodMock mock : methodMocks) {
                        if (mock.getMethod().equals(method) && mock.getOwner() == proxy) {
                            target = mock;
                            break;
                        }
                    }
                    if (target == null) {
                        for (MethodMock mock : methodMocks) {
                            if (mock.getMethod().equals(method) && mock.getOwner() == null) {
                                target = mock;
                                break;
                            }
                        }
                    }

                    if (target != null) {
                        if (target.getListener() != null) {
                            target.getListener().onInvoke(proxy, method, args);
                        }
                        return target.getReturnValue();
                    }

                    return method.invoke(proxy, args);
                }
            });
        }
    }

    public void removeMethodMock(MethodMock mock) {
        methodMocks.remove(mock);
        MockRepository.removeMethodProxy(mock.getMethod());
    }

    public void clear() {
        methodMocks.clear();
        MockRepository.clear();
    }
}
