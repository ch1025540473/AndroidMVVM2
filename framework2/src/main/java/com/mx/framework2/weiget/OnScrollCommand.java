package com.mx.framework2.weiget;

/**
 * Created by liuyuxuan on 16/8/23.
 */
public interface OnScrollCommand {

    void onScrollStateChanged(int id, int state);

    void onScrolled(int id, int v, int h, int dx, int dy);

}
