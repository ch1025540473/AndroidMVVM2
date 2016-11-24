package com.mx.router;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by chenbaocheng on 16/11/24.
 */

public class RouteView extends FrameLayout {
    private Uri uri;

    public RouteView(Context context) {
        super(context);
    }

    public RouteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.UriAccess);
        if (a.hasValue(R.styleable.UriAccess_uri)) {
            String uriString = a.getString(R.styleable.UriAccess_uri);
            setUri(Uri.parse(uriString));
        }
        a.recycle();
    }

    public RouteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setUri(Uri uri) {
        this.uri = uri;
        routeContentView();
    }

    public Uri getUri() {
        return uri;
    }

    private void routeContentView() {
        if (uri != null) {
            Router.getDefault().newRoute().uri(uri).from(this).callback(new Callback<View>() {
                @Override
                public void onRouteSuccess(Route route, View data) {
                    removeAllViews();
                    addView(data);
                }

                @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
                @Override
                public void onRouteFailure(Route route) {
                    if (route.getReason() != null) {
                        route.getReason().printStackTrace();
                    }
                }
            }).buildAndRoute();
        }
    }
}
