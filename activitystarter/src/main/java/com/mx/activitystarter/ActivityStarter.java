package com.mx.activitystarter;

import android.content.Intent;

/**
 * Created by chenbaocheng on 16/9/22.
 */

public interface ActivityStarter extends RawActivityStarter {

    void startActivityForResult(Intent intent, ActivityResultCallback callback);
}
