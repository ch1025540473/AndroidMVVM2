package com.mx.router.converter;

import android.support.v4.app.Fragment;

import com.mx.router.Route;

/**
 * Created by chenbaocheng on 16/11/15.
 */

public class FragmentConverter extends BaseConverter<Fragment> {
    @Override
    public Fragment convert(Route route, Object data) {
        return data instanceof Fragment ? (Fragment) data : null;
    }
}
