package com.mx.router;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

import com.mx.activitystarter.ActivityStarter;
import com.google.gson.Gson;
import com.mx.engine.json.GsonFactory;
import com.mx.engine.utils.CheckUtils;

import java.util.List;
import java.util.Set;

/**
 * Created by chenbaocheng on 16/11/14.
 */

class PipeRoute implements Route, Pipe, UriAccess {
    private Router router;
    private Uri uri;
    private RouteClient client;
    private int resultCode = Router.RESULT_UNKNOWN;

    private Object data;

    private PipeRoute() {
    }

    RouteClient getRouteClient() {
        return client;
    }

    public Class<?> getCallbackDataType() {
        return getRouteClient().getCallbackDataType();
    }

    @Override
    public boolean needInstantReturn() {
        return getRouteClient().getCallback() == null;
    }

    @Override
    public void success() {
        success(null);
    }

    @Override
    public void success(Object data) {
        resultCode = Router.RESULT_OK;
        if (data == null) {
            getRouteClient().success(this, null);
            return;
        }

        Object convertedData = router.convert(this, data);
        if (convertedData != null) {
            getRouteClient().success(this, convertedData);
        } else {
            String message = "Data convert failed.";
            getRouteClient().fail(this, message, new RuntimeException(message));
        }
    }

    @Override
    public void cancel() {
        resultCode = Router.RESULT_CANCELED;
        getRouteClient().fail(this, "Route is canceled", null);
    }

    @Override
    public void fail(String message, Throwable reason) {
        fail(Router.RESULT_FAILED, message, reason);
    }

    @Override
    public void fail(int resultCode, String message, Throwable reason) {
        this.resultCode = resultCode;
        getRouteClient().fail(this, message, reason);
    }

    @Override
    public Context getContext() {
        ActivityStarter
                activityStarter = getActivityStarter();
        if (activityStarter != null) {
            return activityStarter.getContext();
        }

        return null;
    }

    @Override
    public ActivityStarter getActivityStarter() {
        return getRouteClient().getActivityStarter();
    }

    @Override
    public Object getData(Class<?> targetType) {
        if (data != null) {
            Gson gson = GsonFactory.newGson();
            String json = gson.toJson(data);
            return gson.fromJson(json, targetType);
        } else {
            return null;
        }
    }

    @Override
    public Object route() {
        router.route(this);
        return getRouteClient().getResult();
    }

    @Override
    public String getMessage() {
        return getRouteClient().getMessage();
    }

    @Override
    public Throwable getReason() {
        return getRouteClient().getFailedReason();
    }

    @Override
    public Router getRouter() {
        return router;
    }

    @Override
    public Uri getUri() {
        return uri;
    }

    @Override
    public Uri getUriWithoutQuery() {
        return uri.buildUpon().clearQuery().build();
    }

    @Override
    public boolean containsParameter(String name) {
        return uri.getQueryParameter(name) != null;
    }

    private String getParameter(String name) {
        return uri.getQueryParameter(name);
    }

    @Override
    public String getStringParameter(String name, String defaultValue) {
        String value = getParameter(name);
        if (value == null) {
            return defaultValue;
        }

        return value;
    }

    @Override
    public int getIntParameter(String name, int defaultValue) {
        String value = getParameter(name);
        if (value == null) {
            return defaultValue;
        }

        return Integer.valueOf(value);
    }

    @Override
    public long getLongParameter(String name, long defaultValue) {
        String value = getParameter(name);
        if (value == null) {
            return defaultValue;
        }

        return Long.valueOf(value);
    }

    @Override
    public boolean getBooleanParameter(String name, boolean defaultValue) {
        return uri.getBooleanQueryParameter(name, defaultValue);
    }

    @Override
    public float getFloatParameter(String name, float defaultValue) {
        String value = getParameter(name);
        if (value == null) {
            return defaultValue;
        }

        return Float.valueOf(value);
    }

    @Override
    public double getDoubleParameter(String name, double defaultValue) {
        String value = getParameter(name);
        if (value == null) {
            return defaultValue;
        }

        return Double.valueOf(value);
    }

    @Override
    public RouteMethod getMethod() {
        String method = getParameter(Router.PARAM_NAME_METHOD);
        try {
            return RouteMethod.valueOf(method);
        } catch (IllegalArgumentException e) {
            return RouteMethod.GET;
        }
    }

    @Override
    public List<String> getParameters(String name) {
        return uri.getQueryParameters(name);
    }

    @Override
    public Set<String> getParameterNames() {
        return uri.getQueryParameterNames();
    }

    @Override
    public int getResultCode() {
        return resultCode;
    }

    public static class Builder {
        private Router router;
        private Uri.Builder uriBuilder;
        private RouteMethod method;
        private Callback callback;
        private Object from;
        private Object data;

        Builder(@NonNull Router router) {
            this.router = router;
        }

        public Object buildAndRoute() {
            return build().route();
        }

        public Builder data(Object data){
            this.data = data;
            return this;
        }

        public Route build() {
            PipeRoute pipeRoute = new PipeRoute();
            pipeRoute.router = this.router;
            pipeRoute.client = RouteClient.newRouteClient(from, callback);

            if (method == null) {
                method = RouteMethod.GET;
            } else if (method == RouteMethod.POST || method == RouteMethod.PUT) {
                if (data != null) {
                    pipeRoute.data = this.data;
                }
            }
            this.uriBuilder.appendQueryParameter(Router.PARAM_NAME_METHOD, method.toString());
            pipeRoute.uri = this.uriBuilder.build();

            return pipeRoute;
        }

        public Builder uri(String uriString) {
            this.uriBuilder = Uri.parse(uriString).buildUpon();
            return this;
        }

        public Builder uri(Uri uri) {
            this.uriBuilder = uri.buildUpon();
            return this;
        }

        public Builder appendParameter(String name, Object value) {
            CheckUtils.checkNotNull(uriBuilder, "Call uri() before calling appendParameter().");
            if (name != null && value != null) {
                uriBuilder.appendQueryParameter(name, value.toString());
            }
            return this;
        }

        public Builder method(RouteMethod method) {
            this.method = method;
            return this;
        }

        public Builder from(final Context context) {
            CheckUtils.checkNotNull(context);
            this.from = context;
            return this;
        }

        public Builder from(ActivityStarter activityStarter) {
            CheckUtils.checkNotNull(activityStarter);
            this.from = activityStarter;
            return this;
        }

        public Builder from(final Activity activity) {
            CheckUtils.checkNotNull(activity);
            this.from = activity;
            return this;
        }

        public Builder from(final Fragment fragment) {
            CheckUtils.checkNotNull(fragment);
            this.from = fragment;

            return this;
        }

        public Builder from(final View view) {
            CheckUtils.checkNotNull(view);
            this.from = view;

            return this;
        }

        public Builder callback(Callback callback) {
            this.callback = callback;
            return this;
        }
    }
}
