package com.mx.framework;

/**
 * Created by liuyuxuan on 16/5/10.
 */
@Deprecated
public interface DataSourceChangeAware {

    void requestDataReloading(DataSourceChangeAware sender);

    void reloadData(DataSourceChangeAware sender);
}
