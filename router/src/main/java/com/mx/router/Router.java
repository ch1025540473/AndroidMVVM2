package com.mx.router;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.mx.engine.utils.CheckUtils;
import com.mx.router.converter.ObjectConverter;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by chenbaocheng on 16/11/14.
 */

public class Router {
    private static volatile Router defaultInstance = null;

    public static Router getDefault() {
        if (defaultInstance != null) {
            return defaultInstance;
        }

        synchronized (Router.class) {
            if (defaultInstance == null) {
                defaultInstance = new Router();
            }
        }

        return defaultInstance;
    }

    private Uri baseUri = null;
    private Map<String, RouteRule> routeTable;
    private Map<Activity, String> activityIds;
    private List<DataConverter> dataConverters;

    private Router() {
        routeTable = Collections.synchronizedMap(new HashMap<String, RouteRule>());
        activityIds = Collections.synchronizedMap(new WeakHashMap<Activity, String>());
        dataConverters = Collections.synchronizedList(new LinkedList<DataConverter>());
        dataConverters.add(new ObjectConverter()); // As default converter
    }

    public void init(String baseUri) {
        this.baseUri = Uri.parse(baseUri);
    }

    public Uri getBaseUri() {
        return baseUri;
    }

    private Uri fullUri(Uri uri) {
        if (uri.isAbsolute()) {
            return uri;
        }

        Uri.Builder builder = baseUri.buildUpon();
        for (String segment : uri.getPathSegments()) {
            builder.appendPath(segment);
        }

        return builder.build();
    }

    private Uri fullUri(String uriString) {
        return fullUri(Uri.parse(uriString));
    }

    public void registerRule(@NonNull String uriString, @NonNull RouteRule rule) {
        CheckUtils.checkNotNull(uriString);
        CheckUtils.checkNotNull(rule);

        registerRule(Uri.parse(uriString), rule);
    }

    public void registerRule(@NonNull Uri uri, @NonNull RouteRule rule) {
        CheckUtils.checkNotNull(uri);
        CheckUtils.checkNotNull(rule);

        if (uri.isOpaque()) {
            throw new RuntimeException("Rule uri cannot be relative or be opaque.");
        }
        if (!uri.getQueryParameterNames().isEmpty()) {
            throw new RuntimeException("Rule uri cannot have parameters.");
        }

        routeTable.put(fullUri(uri).toString(), rule);
    }

    public void unregisterRule(@NonNull String uriString) {
        CheckUtils.checkNotNull(uriString);
        routeTable.remove(fullUri(uriString).toString());
    }

    public void unregisterRule(@NonNull Uri uri) {
        CheckUtils.checkNotNull(uri);
        unregisterRule(uri.toString());
    }

    /**
     * Add a data converter for the callback data.
     * Later added converter has a higher priority.
     *
     * @param converter data converter
     */
    public void addDataConverter(DataConverter converter) {
        CheckUtils.checkNotNull(converter);
        dataConverters.add(0, converter);
    }

    public void removeDataConverter(DataConverter converter) {
        dataConverters.remove(converter);
    }

    public Route.Builder newRoute() {
        return new Route.Builder(this);
    }

//    private static final String BUNDLE_KEY_ACTIVITY_ID = Router.class.getName() + "_activity_id";
//
//    public void saveState(@NonNull Activity activity, Bundle outState) {
//        if(outState == null){
//            return;
//        }
//
//        String id;
//        if (outState.containsKey(BUNDLE_KEY_ACTIVITY_ID)) {
//            id = outState.getString(BUNDLE_KEY_ACTIVITY_ID);
//        } else {
//            id = UUID.randomUUID().toString();
//            outState.putString(BUNDLE_KEY_ACTIVITY_ID, id);
//        }
//
//        activityIds.put(activity, id);
//    }
//
//    public void restoreState(@NonNull Activity activity, Bundle savedState) {
//        if(savedState == null){
//            return;
//        }
//
//        if(activityIds.containsKey(activity) && savedState.containsKey(activityIds.get(activity))){
//
//        }
//    }
//
//    public void onStart(Callback callback) {
//    }
//
//    public void onStop(Callback callback) {
//    }
//
//    public void onDestroy(Callback callback) {
//    }

    void route(Route route) {
        String entryKey = route.getUriWithoutQuery().toString();
        String uri = fullUri(entryKey).toString();
        if (!routeTable.containsKey(uri)) {
            throw new RuntimeException("No such route in Router: " + entryKey);
        }

        routeTable.get(uri).handleRoute(route);
    }

    Object convert(Route route, Object data) {
        List<DataConverter> converters = new LinkedList<>(dataConverters);

        for (DataConverter converter : converters) {
            Object converted = converter.convert(route, data);
            if (converted != null) {
                return converted;
            }
        }

        return null;
    }
}
