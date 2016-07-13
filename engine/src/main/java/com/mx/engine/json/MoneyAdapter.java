package com.mx.engine.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by chenbaocheng on 16/4/21.
 */
public class MoneyAdapter extends TypeAdapter<Money> {

    @Override
    public void write(JsonWriter out, Money value) throws IOException {
        out.value(value.getValue());
    }

    @Override
    public Money read(JsonReader in) throws IOException {
        return new Money(in.nextLong());
    }
}