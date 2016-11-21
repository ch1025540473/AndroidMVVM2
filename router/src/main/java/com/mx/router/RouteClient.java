package com.mx.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;

import com.mx.engine.utils.ObjectUtils;
import com.mx.framework2.view.ui.ActivityResultCallback;
import com.mx.framework2.view.ui.ActivityStarter;
import com.mx.framework2.view.ui.BaseActivity;

import java.lang.ref.WeakReference;
import java.util.UUID;

/**
 * Created by chenbaocheng on 16/11/19.
 */

public abstract class RouteClient {
    private final String tag;
    private WeakReference<Object> host;
    private WeakReference<Callback> callback;
    private boolean callbackOnHost;

    private Handler uiHandler;
    private boolean isPausing = false;
    private Runnable pendingRunnable = null;

    private String message;
    private Throwable failedReason;

    public RouteClient(Object host, Callback callback) {
        this.tag = UUID.randomUUID().toString();
        this.callbackOnHost = host == callback;
        setHost(host);
        setCallback(callback);

        this.uiHandler = new Handler(Looper.getMainLooper());
    }

    public boolean pause(Object host, Bundle outState) {
        if (getHost() != host) {
            // not my business
            return false;
        }

        this.isPausing = true;
        if (outState != null) {
            outState.putBoolean(tag, true); // save my tag to compare when client resumes
        }

        return true;
    }

    public boolean resume(Object host, Bundle savedState) {
        if (getHost() != host) {
            // not my business
            return false;
        }

        this.isPausing = false;
        if (savedState != null && savedState.containsKey(tag)) {
            // is my host by comparing tag
            setHost(host); // reset the host in case the host was rebuilt.
        }
        if (pendingRunnable != null) {
            uiHandler.post(pendingRunnable);
            pendingRunnable = null;
        }

        return true;
    }

    public Class<?> getCallbackDataType() {
        if (getCallback() != null) {
            return ObjectUtils.getGenericClass(getCallback(), Callback.class, 0);
        } else {
            return null;
        }
    }

    private void performSuccess(Route route, Object convertedData) {
        if (getCallback() != null) {
            if (convertedData != null) {
                //noinspection unchecked
                getCallback().onRouteSuccess(route, convertedData);
            } else {
                String message = "Data convert failed.";
                fail(route, message, new RuntimeException(message));
            }
        }
    }

    public void success(final Route route, final Object convertedData) {
        if (isPausing) {
            pendingRunnable = new Runnable() {
                @Override
                public void run() {
                    performSuccess(route, convertedData);
                }
            };
        } else {
            performSuccess(route, convertedData);
        }
    }

    private void performFail(Route route, String message, Throwable reason) {
        this.message = message;
        this.failedReason = reason;

        if (getCallback() != null) {
            getCallback().onRouteFailure(route);
        }
    }

    public void fail(final Route route, final String message, final Throwable reason) {
        if (isPausing) {
            pendingRunnable = new Runnable() {
                @Override
                public void run() {
                    performFail(route, message, reason);
                }
            };
        } else {
            performFail(route, message, reason);
        }
    }


    public abstract ActivityStarter getActivityStarter();

    public Object getHost() {
        return host.get();
    }

    public void setHost(Object host) {
        if (host != null) {
            this.host = new WeakReference<>(host);
        } else {
            this.host = null;
        }

        if (callbackOnHost && host instanceof Callback) {
            // if the host is Activity or something which could be rebuilt and implements the Callback,
            // changing host means changing callback at the same time.
            setCallback((Callback) host);
        }
    }

    private void setCallback(Callback callback) {
        if (callback != null) {
            this.callback = new WeakReference<>(callback);
        } else {
            this.callback = null;
        }
    }

    public Callback getCallback() {
        return callback.get();
    }

    public Throwable getFailedReason() {
        return failedReason;
    }

    public String getMessage() {
        return message;
    }

    public boolean isPausing() {
        return isPausing;
    }

    public static RouteClient newRouteClient(Object fromObj, Callback callback) {
        if (fromObj instanceof ActivityStarter) {
            return new ActivityStarterClient((ActivityStarter) fromObj, callback);
        } else if (fromObj instanceof BaseActivity) {
            return new ActivityClient((BaseActivity) fromObj, callback);
        } else if (fromObj instanceof Activity) {
            return new GeneralActivityClient((Activity) fromObj, callback);
        } else if (fromObj instanceof Fragment) {
            return new GeneralFragmentClient((Fragment) fromObj, callback);
        } else {
            throw new RuntimeException("Cannot create RouteClient");
        }
    }

    private static class ActivityStarterClient extends RouteClient {
        public ActivityStarterClient(ActivityStarter activityStarter, Callback callback) {
            super(activityStarter, callback);
        }

        @Override
        public ActivityStarter getActivityStarter() {
            return (ActivityStarter) getHost();
        }
    }

    private static class ActivityClient extends RouteClient {
        public ActivityClient(BaseActivity activity, Callback callback) {
            super(activity, callback);
        }

        @Override
        public ActivityStarter getActivityStarter() {
            Object host = getHost();
            if (host != null) {
                return ((BaseActivity) host).getActivityStarter();
            } else {
                return null;
            }
        }
    }

    private static abstract class GeneralBaseClient extends RouteClient {
        public GeneralBaseClient(Object host, Callback callback) {
            super(host, callback);
            if (callback != null && callback != host) {
                throw new RuntimeException(host + " must implements interface " + Callback.class.getName());
            }
        }
    }

    private static class GeneralActivityClient extends GeneralBaseClient {
        public GeneralActivityClient(Activity activity, Callback callback) {
            super(activity, callback);
        }

        public Activity getActivity() {
            return (Activity) getHost();
        }

        @Override
        public ActivityStarter getActivityStarter() {
            return new ActivityStarter() {
                @Override
                public void startActivityForResult(Intent intent, ActivityResultCallback callback) {
                    throw new UnsupportedOperationException("You cannot use startActivityForResult with a callback," +
                            " due to this route is client an Activity or Fragment.");
                }

                @Override
                public void startActivity(Intent intent) {
                    Activity activity = getActivity();
                    if (activity != null) {
                        activity.startActivity(intent);
                    }
                }

                @Override
                public void startActivityForResult(Intent intent, int requestCode) {
                    Activity activity = getActivity();
                    if (activity != null) {
                        activity.startActivityForResult(intent, requestCode);
                    }
                }

                @Override
                public Context getContext() {
                    return getActivity();
                }
            };
        }
    }

    private static class GeneralFragmentClient extends GeneralBaseClient {
        public GeneralFragmentClient(Fragment fragment, Callback callback) {
            super(fragment, callback);
        }

        public Fragment getFragment() {
            return (Fragment) getHost();
        }

        @Override
        public ActivityStarter getActivityStarter() {
            return new ActivityStarter() {
                @Override
                public void startActivityForResult(Intent intent, ActivityResultCallback callback) {
                    throw new UnsupportedOperationException("You cannot use startActivityForResult with a callback," +
                            " due to this route is client an Activity or Fragment.");
                }

                @Override
                public void startActivity(Intent intent) {
                    Fragment activity = getFragment();
                    if (activity != null) {
                        activity.startActivity(intent);
                    }
                }

                @Override
                public void startActivityForResult(Intent intent, int requestCode) {
                    Fragment activity = getFragment();
                    if (activity != null) {
                        activity.startActivityForResult(intent, requestCode);
                    }
                }

                @Override
                public Context getContext() {
                    return getFragment().getContext();
                }
            };
        }
    }
}
