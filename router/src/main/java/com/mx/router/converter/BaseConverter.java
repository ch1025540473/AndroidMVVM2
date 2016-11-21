package com.mx.router.converter;

import com.mx.router.DataConverter;

/**
 * Created by chenbaocheng on 16/11/15.
 */

abstract class BaseConverter implements DataConverter {
    @Override
    public Object convert(Object data, Class<?> targetType) {
        return performConvert(data, targetType);
    }

    protected abstract Object performConvert(Object data, Class<?> targetType);
}
