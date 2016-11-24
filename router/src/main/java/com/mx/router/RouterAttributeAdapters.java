package com.mx.router;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.net.Uri;

/**
 * Created by chenbaocheng on 16/11/24.
 */

public class RouterAttributeAdapters {
    @BindingConversion
    public static Uri convertUri(String uriString) {
        return Uri.parse(uriString);
    }

    @BindingAdapter("uri")
    public static void bindUri(RouteView view, Uri uri) {
        view.setUri(uri);
    }
}
