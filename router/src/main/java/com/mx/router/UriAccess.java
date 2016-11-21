package com.mx.router;

import android.net.Uri;

import java.util.List;
import java.util.Set;

/**
 * Created by chenbaocheng on 16/11/19.
 */
public interface UriAccess {
    Uri getUri();

    Uri getUriWithoutQuery();

    boolean containsParameter(String name);

    String getStringParameter(String name, String defaultValue);

    int getIntParameter(String name, int defaultValue);

    long getLongParameter(String name, long defaultValue);

    boolean getBooleanParameter(String name, boolean defaultValue);

    float getFloatParameter(String name, float defaultValue);

    double getDoubleParameter(String name, double defaultValue);

    List<String> getParameters(String name);

    Set<String> getParameterNames();
}
