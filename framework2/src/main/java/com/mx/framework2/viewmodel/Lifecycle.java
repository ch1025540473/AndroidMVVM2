package com.mx.framework2.viewmodel;

import android.os.Bundle;

/**
 * Created by liuyuxuan on 16/8/18.
 */
public interface Lifecycle {

    void attachedToView();

    void detachedFromView();

    void onWindowFocusChanged(boolean hasFocus);

    void onSaveInstanceState(Bundle bundle);

    void onRestoreInstanceState(Bundle bundle);

    void setUserVisibleHint(boolean isVisibleToUser);

    void accept(Visitor viewModelVisitor);

    void create(Bundle bundle);

    void start();

    void stop();

    void resume();

    void pause();
}