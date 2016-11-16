package com.mx.router.converter;

import com.google.gson.Gson;
import com.mx.engine.json.GsonFactory;
import com.mx.router.Route;

/**
 * Created by chenbaocheng on 16/11/15.
 */

public class ObjectConverter extends BaseConverter<Object> {
    @Override
    public Object convert(Route route, Object data) {
        Class<?> dataType = route.getCallbackDataType();

        if (dataType.isInstance(data)) {
            return data;
        } else {
            Gson gson = GsonFactory.newGson();
            String json = gson.toJson(data);

            return gson.fromJson(json, dataType);
        }
    }
}
