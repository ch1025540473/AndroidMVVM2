package com.mx.framework2.view.ui;

import android.content.Intent;

/**
 * Created by chenbaocheng on 16/9/22.
 */

public interface ActivityStarter {
    void startActivity(Intent intent);

    void startActivityForResult(Intent intent, int requestCode);
}
