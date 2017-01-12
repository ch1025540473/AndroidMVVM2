package com.mx.activitystarter;

import android.content.Intent;

/**
 * Created by chenbaocheng on 16/9/27.
 */

public interface ActivityResultCallback {
    void onActivityResult(int resultCode, Intent data);
}
