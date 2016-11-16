package com.mx.router.converter;

import android.os.Bundle;

import com.mx.router.Route;

/**
 * Created by chenbaocheng on 16/11/15.
 */

public class BundleConverter extends BaseConverter<Bundle> {
    @Override
    public Bundle convert(Route route, Object data) {
        return data instanceof Bundle ? (Bundle) data : null;
    }
}
