package com.mx.router;

/**
 * Created by chenbaocheng on 16/11/15.
 */

public interface DataConverter {
    Object convert(Object data, Class<?> targetType);
}
