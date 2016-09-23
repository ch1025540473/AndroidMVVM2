package com.mx.framework2.view.ui;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by zhulianggang on 16/9/22.
 */

public interface GomeShowDialog {
    public void showDialog(DialogFragment dialogFragment, String tag);

    public int showDialog(DialogFragment dialogFragment, FragmentTransaction transaction, String tag);
}
