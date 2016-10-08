package com.mx.framework2.view.ui;

import android.content.Intent;

/**
 * Created by liuyuxuan on 16/10/8.
 */
public interface RawActivityStarter {
    void startActivity(Intent intent);

    void startActivityForResult(Intent intent, int requestCode);
}
