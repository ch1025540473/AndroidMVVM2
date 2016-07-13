package com.mx.engine.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by chenbaocheng on 16/4/21.
 */
public class TimeAdapter extends TypeAdapter<Time> {

    @Override
    public void write(JsonWriter out, Time value) throws IOException {
        out.value(value.getMilliseconds());
    }

    @Override
    public Time read(JsonReader in) throws IOException {
        return new Time(in.nextLong());
    }
}