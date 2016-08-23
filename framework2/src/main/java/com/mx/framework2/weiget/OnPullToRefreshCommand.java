package com.mx.framework2.weiget;

/**
 * Created by liuyuxuan on 16/8/23.
 */
public interface OnPullToRefreshCommand {

    public void onMove(boolean isPullToRefresh, float scaleOfLayout);

    public void onRefreshing();

    public void onReset();

}
