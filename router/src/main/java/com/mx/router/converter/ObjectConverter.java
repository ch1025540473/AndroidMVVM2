package com.mx.router.converter;

import com.google.gson.Gson;
import com.mx.engine.json.GsonFactory;

/**
 * Created by chenbaocheng on 16/11/15.
 */

public class ObjectConverter extends BaseConverter {
    @Override
    protected Object performConvert(Object data, Class<?> targetType) {
        if (targetType.isInstance(data)) {
            return data;
        } else {
            Gson gson = GsonFactory.newGson();
            String json = gson.toJson(data);

            return gson.fromJson(json, targetType);
        }
    }
}
