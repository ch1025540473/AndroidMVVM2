package com.handmark.pulltorefresh.library;

/**
 * Created by liuyuxuan on 16/8/9.
 */
public interface PullToRefreshHeaderListener {

    /**
     * Call when the widget begins to slide
     */
    public void pullToRefresh();

    /**
     * Call when the LoadingLayout is fully displayed
     */
    public void releaseToRefresh();

    /**
     * Call when the LoadingLayout is sliding
     *
     * @param scaleOfLayout scaleOfLayout
     */
    public void onPull(float scaleOfLayout);

    /**
     * Call when the LoadingLayout is fully displayed and the widget has released.
     * Used to prompt the user loading data
     */
    public void refreshing();

    /**
     * Call when the data has loaded
     */
    public void reset();


}
