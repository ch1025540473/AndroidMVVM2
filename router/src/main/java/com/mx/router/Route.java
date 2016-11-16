package com.mx.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.mx.engine.utils.CheckUtils;
import com.mx.engine.utils.ObjectUtils;
import com.mx.framework2.view.ui.ActivityResultCallback;
import com.mx.framework2.view.ui.ActivityStarter;
import com.mx.framework2.view.ui.BaseActivity;

import java.lang.ref.WeakReference;

/**
 * Created by chenbaocheng on 16/11/14.
 */

public class Route {
    private Router router;
    private Uri uri;
    private ActivityStarter activityStarter;
    private WeakReference<Callback> callbackRef;
    private boolean isDestroyed = false;

    private String message;
    private Throwable failedReason;

    private Route() {
    }

    Callback getCallback() {
        if (callbackRef == null) {
            return null;
        } else {
            return callbackRef.get();
        }
    }

    public Class<?> getCallbackDataType() {
        if (getCallback() != null) {
            return ObjectUtils.getGenericClass(getCallback(), Callback.class, 0);
        } else {
            return null;
        }
    }

    public void success(Object data) {
        if (getCallback() != null) {
            Object toData = router.convert(this, data);
            if (toData != null) {
                getCallback().onRouteSuccess(this, toData);
            } else {
                String message = "Data convert failed.";
                fail(message, new RuntimeException(message));
            }
        }
    }

    public void fail(String message, Throwable reason) {
        this.message = message;
        this.failedReason = reason;

        if (getCallback() != null) {
            getCallback().onRouteFailure(this);
        }
    }

    public Router getRouter() {
        return router;
    }

    public Uri getUri() {
        return uri;
    }

    public Uri getUriWithoutQuery() {
        return uri.buildUpon().clearQuery().build();
    }

    public ActivityStarter getActivityStarter() {
        return activityStarter;
    }

    public Route route() {
        router.route(this);
        return this;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    /**
     * Destroy this route, which means its callback has been detached.
     */
    public void destroy() {
        isDestroyed = true;
        callbackRef = null;
    }

    public static class Builder {
        private Router router;
        private Uri.Builder uriBuilder;
        private ActivityStarter activityStarter;
        private Callback callback;

        Builder(@NonNull Router router) {
            this.router = router;
        }

        public Route buildAndRoute() {
            Route route = build();
            route.route();

            return route;
        }

        public Route build() {
            Route route = new Route();
            route.router = this.router;
            route.uri = this.uriBuilder.build();
            route.activityStarter = this.activityStarter;
            route.callbackRef = new WeakReference<>(this.callback);

            return route;
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
            this.activityStarter = activityStarter;
            return this;
        }

        public Builder from(final Activity activity) {
            CheckUtils.checkNotNull(activity);

            if (activity instanceof BaseActivity) {
                from(((BaseActivity) activity).getActivityStarter());
            } else {
                from(new ActivityStarter() {
                    @Override
                    public void startActivityForResult(Intent intent, ActivityResultCallback callback) {
                        throw new UnsupportedOperationException("You cannot use startActivityForResult with a callback," +
                                " until the whole framework2 refactor is done.");
                    }

                    @Override
                    public void startActivity(Intent intent) {
                        activity.startActivity(intent);
                    }

                    @Override
                    public void startActivityForResult(Intent intent, int requestCode) {
                        activity.startActivityForResult(intent, requestCode);
                    }

                    @Override
                    public Context getContext() {
                        return activity;
                    }
                });
            }
            return this;
        }

        public Builder from(final Fragment fragment) {
            CheckUtils.checkNotNull(fragment);
            from(new ActivityStarter() {
                @Override
                public void startActivityForResult(Intent intent, ActivityResultCallback callback) {
                    throw new UnsupportedOperationException("You cannot use startActivityForResult with a callback," +
                            " until the whole framework2 refactor is done.");
                }

                @Override
                public void startActivity(Intent intent) {
                    fragment.startActivity(intent);
                }

                @Override
                public void startActivityForResult(Intent intent, int requestCode) {
                    fragment.startActivityForResult(intent, requestCode);
                }

                @Override
                public Context getContext() {
                    return fragment.getContext();
                }
            });

            return this;
        }

        public Builder callback(Callback callback) {
            this.callback = callback;
            return this;
        }
    }
}
