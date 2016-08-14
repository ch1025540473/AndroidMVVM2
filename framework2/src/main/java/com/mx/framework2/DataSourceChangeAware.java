package com.mx.framework2;

/**
 * Created by liuyuxuan on 16/5/10.
 */
public interface DataSourceChangeAware {

    void requestDataReloading(DataSourceChangeAware sender);

    void reloadData(DataSourceChangeAware sender);
}
