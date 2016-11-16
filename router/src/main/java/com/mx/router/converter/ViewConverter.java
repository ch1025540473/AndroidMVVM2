package com.mx.router.converter;

import android.view.View;

import com.mx.router.Route;

/**
 * Created by chenbaocheng on 16/11/15.
 */

public class ViewConverter extends BaseConverter<View> {
    @Override
    public View convert(Route route, Object data) {
        return data instanceof View ? (View) data : null;
    }
}
