package com.mx.activitystarter;

import android.content.Context;
import android.content.Intent;

/**
 * Created by liuyuxuan on 16/10/8.
 */
public interface RawActivityStarter {
    void startActivity(Intent intent);

    void startActivityForResult(Intent intent, int requestCode);

    Context getContext();
}
