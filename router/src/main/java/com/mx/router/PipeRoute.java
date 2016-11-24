package com.mx.router;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

import com.mx.engine.utils.CheckUtils;
import com.mx.framework2.view.ui.ActivityStarter;

import java.util.List;
import java.util.Set;

/**
 * Created by chenbaocheng on 16/11/14.
 */

public class PipeRoute implements Route, Pipe, UriAccess {
    private Router router;
    private Uri uri;
    private RouteClient client;

    private PipeRoute() {
    }

    RouteClient getRouteClient() {
        return client;
    }

    public Class<?> getCallbackDataType() {
        return getRouteClient().getCallbackDataType();
    }

    @Override
    public void success(Object data) {
        getRouteClient().success(this, router.convert(this, data));
    }

    @Override
    public void fail(String message, Throwable reason) {
        getRouteClient().fail(this, message, reason);
    }

    @Override
    public Context getContext() {
        ActivityStarter activityStarter = getActivityStarter();
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
    public Route route() {
        router.route(this);
        return this;
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

    private String getParameter(String name, Object defaultValue) {
        String value = uri.getQueryParameter(name);
        if (value == null) {
            return defaultValue.toString();
        }

        return value;
    }

    @Override
    public String getStringParameter(String name, String defaultValue) {
        return getParameter(name, defaultValue);
    }

    @Override
    public int getIntParameter(String name, int defaultValue) {
        return Integer.valueOf(getParameter(name, defaultValue));
    }

    @Override
    public long getLongParameter(String name, long defaultValue) {
        return Long.valueOf(getParameter(name, defaultValue));
    }

    @Override
    public boolean getBooleanParameter(String name, boolean defaultValue) {
        return uri.getBooleanQueryParameter(name, defaultValue);
    }

    @Override
    public float getFloatParameter(String name, float defaultValue) {
        return Float.valueOf(getParameter(name, defaultValue));
    }

    @Override
    public double getDoubleParameter(String name, double defaultValue) {
        return Double.valueOf(getParameter(name, defaultValue));
    }

    @Override
    public List<String> getParameters(String name) {
        return uri.getQueryParameters(name);
    }

    @Override
    public Set<String> getParameterNames() {
        return uri.getQueryParameterNames();
    }

    public static class Builder {
        private Router router;
        private Uri.Builder uriBuilder;
        private Callback callback;
        private Object from;

        Builder(@NonNull Router router) {
            this.router = router;
        }

        public Route buildAndRoute() {
            return build().route();
        }

        public Route build() {
            PipeRoute pipeRoute = new PipeRoute();
            pipeRoute.router = this.router;
            pipeRoute.uri = this.uriBuilder.build();
            pipeRoute.client = RouteClient.newRouteClient(from, callback);

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
            uriBuilder.appendQueryParameter(name, value.toString());
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
