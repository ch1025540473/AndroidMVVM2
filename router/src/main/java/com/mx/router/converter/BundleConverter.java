package com.mx.router.converter;

import android.os.Bundle;

/**
 * Created by chenbaocheng on 16/11/15.
 */

public class BundleConverter extends BaseConverter {
    @Override
    protected Object performConvert(Object data, Class<?> targetType) {
        if (targetType.isAssignableFrom(Bundle.class) && data instanceof Bundle) {
            return data;
        }

        return null;
    }
}
