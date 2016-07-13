package com.mx.engine.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by chenbaocheng on 16/4/21.
 */
public class GsonFactory {
    public static Gson newGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Time.class, new com.mx.engine.json.TimeAdapter());
        builder.registerTypeAdapter(Money.class, new MoneyAdapter());

        return builder.create();
    }
}
