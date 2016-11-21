package com.mx.hotfix.installer;

import android.content.Context;

import com.tencent.tinker.lib.tinker.TinkerInstaller;

/**
 * Created by wwish on 16/11/21.
 */

public class HotfixInstaller {
    /**
     * new patch file to install, try install them with :patch process
     *
     * @param context
     * @param patchLocation
     */
    public static void onReceiveUpgradePatch(Context context, String patchLocation) {
        TinkerInstaller.onReceiveUpgradePatch(context, patchLocation);
    }
}
