package com.mx.router;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.Method;

/**
 * Created by chenbaocheng on 16/11/28.
 */

class SubscribeHandler {
    private final Object enclosingObject;
    private final Method method;
    private final Handler handler;

    SubscribeHandler(Object enclosingObject, Method method) {
        this.enclosingObject = enclosingObject;
        this.method = method;
        this.handler = new Handler(Looper.getMainLooper()); // 目前只支持主线程回调，未来可以支持其他线程
    }

    public boolean handle(final Bundle data) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    method.setAccessible(true);
                    method.invoke(enclosingObject, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            handler.post(runnable);
        }

        return true;
    }

    public Object getEnclosingObject() {
        return enclosingObject;
    }

    public Method getMethod() {
        return method;
    }

    public static String parseUri(Method method) {
        RouteSubscribe annotation = method.getAnnotation(RouteSubscribe.class);
        if (annotation == null || annotation.uri().isEmpty()) {
            throw new RuntimeException("Empty uri found in annotation " + RouteSubscribe.class.getSimpleName());
        }

        Uri uri = Uri.parse(annotation.uri());
        if (uri.getQueryParameterNames().size() > 0) {
            throw new RuntimeException("Cannot subscribe a uri which has parameters, in annotation "
                    + RouteSubscribe.class.getSimpleName());
        }

        return uri.toString();
    }

    public static boolean acceptable(Method method) {
        if (method == null) {
            return false;
        }

        RouteSubscribe annotation = method.getAnnotation(RouteSubscribe.class);
        if (annotation == null) {
            return false;
        }

        String uri = parseUri(method);
        if (uri == null || uri.isEmpty()) {
            return false;
        }

        Class<?>[] paramTypes = method.getParameterTypes();
        return paramTypes.length == 1
                && Bundle.class.isAssignableFrom(paramTypes[0]);
    }
}
