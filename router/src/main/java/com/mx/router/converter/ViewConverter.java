package com.mx.router.converter;

import android.view.View;

/**
 * Created by chenbaocheng on 16/11/15.
 */

public class ViewConverter extends BaseConverter {
    @Override
    protected Object performConvert(Object data, Class<?> targetType) {
        if (targetType.isAssignableFrom(View.class) && data instanceof View) {
            return data;
        }

        return null;
    }
}
