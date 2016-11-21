package com.mx.router.converter;

import android.support.v4.app.Fragment;

/**
 * Created by chenbaocheng on 16/11/15.
 */

public class FragmentConverter extends BaseConverter {
    @Override
    protected Object performConvert(Object data, Class<?> targetType) {
        if (targetType.isAssignableFrom(Fragment.class) && data instanceof Fragment) {
            return data;
        }

        return null;
    }
}
