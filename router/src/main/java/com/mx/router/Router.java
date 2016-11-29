package com.mx.router;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.mx.engine.utils.CheckUtils;
import com.mx.engine.utils.ObjectUtils;
import com.mx.router.converter.ObjectConverter;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private List<DataConverter> dataConverters;

    private Map<String, List<SubscribeHandler>> subscribingTable;
    private Map<String, RouteRule> routingTable;
    private Set<RouteClient> activeClients;

    private Router() {
        subscribingTable = Collections.synchronizedMap(new HashMap<String, List<SubscribeHandler>>());
        routingTable = Collections.synchronizedMap(new HashMap<String, RouteRule>());
        activeClients = Collections.synchronizedSet(new HashSet<RouteClient>());

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

    public void registerReceiver(Object obj) {
        CheckUtils.checkNotNull(obj);
        List<Method> methods = ObjectUtils.findMethods(obj.getClass(), new ObjectUtils.MethodFilter() {
            @Override
            public boolean accept(Method method) {
                return SubscribeHandler.acceptable(method);
            }
        });

        for (Method method : methods) {
            String uri = SubscribeHandler.parseUri(method);
            uri = fullUri(uri).toString();
            List<SubscribeHandler> handlers = subscribingTable.get(uri);
            if (handlers == null) {
                handlers = new LinkedList<>();
                subscribingTable.put(uri, handlers);
            }

            handlers.add(new SubscribeHandler(obj, method));
        }
    }

    public void unregisterReceiver(Object obj) {
        Iterator<String> keyIt = subscribingTable.keySet().iterator();
        while (keyIt.hasNext()) {
            List<SubscribeHandler> handlers = subscribingTable.get(keyIt.next());
            Iterator<SubscribeHandler> handlerIt = handlers.iterator();
            while (handlerIt.hasNext()) {
                SubscribeHandler handler = handlerIt.next();
                if (handler.getEnclosingObject() == obj) {
                    handlerIt.remove();
                }
            }
            if (handlers.isEmpty()) {
                keyIt.remove();
            }
        }
    }

    public void broadcast(String uri, Bundle data) {
        broadcast(Uri.parse(uri), data);
    }

    public void broadcast(Uri uri, Bundle data) {
        String fullUri = fullUri(uri).toString();
        List<SubscribeHandler> handlers = subscribingTable.get(fullUri);
        if (handlers != null) {
            for (SubscribeHandler handler : handlers) {
                handler.handle(data);
            }
        }
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

        routingTable.put(fullUri(uri).toString(), rule);
    }

    public void unregisterRule(@NonNull String uriString) {
        CheckUtils.checkNotNull(uriString);
        routingTable.remove(fullUri(uriString).toString());
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

    public PipeRoute.Builder newRoute() {
        return new PipeRoute.Builder(this);
    }

    private void cleanActiveClients(Object host) {
        Iterator<RouteClient> it = activeClients.iterator();
        while (it.hasNext()) {
            RouteClient client = it.next();
            if (client.getHost() == null || client.getHost() == host) {
                it.remove();
            }
        }
    }

    public void saveState(@NonNull Object host, Bundle outState) {
        cleanActiveClients(null);
        for (RouteClient client : activeClients) {
            client.pause(host, outState);
        }
    }

    public void restoreState(@NonNull Object host, Bundle savedState) {
        cleanActiveClients(null);
        for (RouteClient client : activeClients) {
            client.resume(host, savedState);
        }
    }

    public void onDestroy(Object host) {
        cleanActiveClients(host);
    }

    void route(PipeRoute pipeRoute) {
        String entryKey = pipeRoute.getUriWithoutQuery().toString();
        String uri = fullUri(entryKey).toString();
        if (!routingTable.containsKey(uri)) {
            throw new RuntimeException("No such pipeRoute in Router: " + entryKey);
        }

        activeClients.add(pipeRoute.getRouteClient());
        routingTable.get(uri).handleRoute(pipeRoute);
    }

    Object convert(PipeRoute pipeRoute, Object data) {
        List<DataConverter> converters = new LinkedList<>(dataConverters);

        for (DataConverter converter : converters) {
            Object converted = converter.convert(data, pipeRoute.getCallbackDataType());
            if (converted != null) {
                return converted;
            }
        }

        return null;
    }
}
