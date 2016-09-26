package com.mx.framework2.viewmodel.proxy;

import android.support.v4.app.FragmentManager;

import com.mx.engine.utils.CheckUtils;
import com.mx.framework2.view.ui.BaseDialogFragment;

import java.lang.ref.WeakReference;

/**
 * Created by zhulianggang on 16/9/23.
 */

public class DialogProxy {
    private final WeakReference<FragmentManager> fragmentManager;
    private final BaseDialogFragment dialogFragment;
    private final String tag;

    public DialogProxy(FragmentManager fragmentManager, BaseDialogFragment dialogFragment, String tag) {
        this.fragmentManager = new WeakReference<>(fragmentManager);
        this.dialogFragment = dialogFragment;
        this.tag = tag;
    }

    public void show() {
        CheckUtils.checkNotNull(dialogFragment);
        CheckUtils.checkNotNull(fragmentManager.get());
        dialogFragment.show(fragmentManager.get(), tag);
    }

    public void dismiss() {
        CheckUtils.checkNotNull(dialogFragment);
        CheckUtils.checkNotNull(fragmentManager.get());
        dialogFragment.dismiss();
    }

}
