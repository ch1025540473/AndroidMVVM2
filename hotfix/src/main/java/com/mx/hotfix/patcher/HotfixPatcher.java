package com.mx.hotfix.patcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.mx.hotfix.util.Utils;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.lib.tinker.TinkerLoadResult;
import com.tencent.tinker.lib.util.TinkerLog;

import static com.mx.hotfix.util.HotfixApplicationContext.context;


/**
 * Created by wwish on 16/11/21.
 */

public class HotfixPatcher {

    private static final String TAG = HotfixPatcher.class.getSimpleName(); //"HotfixPatcher";
    private PatchResultListener patchResultListener;
    private static HotfixPatcher instance;

    private HotfixPatcher() {
    }

    public static HotfixPatcher getInstance() {
        if (instance == null) {
            synchronized (HotfixPatcher.class) {
                if (instance == null) {
                    instance = new HotfixPatcher();
                }
            }
        }
        return instance;
    }

    /**
     * @return hotfix patch version
     */
    public String getPatchVersion() {
        TinkerLoadResult tinkerLoadResult = Tinker.with(context).getTinkerLoadResultIfPresent();
        return tinkerLoadResult.getPackageConfigByName(Utils.PATCHVERSION);
    }

    public void patch(String patchLocation) {
        onReceiveUpgradePatch(patchLocation);
    }

    /**
     * new patch file to install, try install them with :patch process
     *
     * @param patchLocation
     */
    private void onReceiveUpgradePatch(String patchLocation) {
        TinkerInstaller.onReceiveUpgradePatch(context, patchLocation);
    }

    /**
     * new patch file to install, try install them with :patch process
     *
     * @param patchLocation
     * @param patchResultListener
     */

    public void onReceiveUpgradePatch(String patchLocation, PatchResultListener patchResultListener) {
        if (context == null) {
            TinkerLog.e(TAG, "HotfixPatcher received a null context, ignoring.");
            return;
        }
        if (patchLocation == null) {
            TinkerLog.e(TAG, "HotfixPatcher received a null patchLocation, ignoring.");
            return;
        }
        TinkerInstaller.onReceiveUpgradePatch(context, patchLocation);
        this.patchResultListener = patchResultListener;
        if (this.patchResultListener != null) {
            context.registerReceiver(resultBroadcastReceiver, new IntentFilter(Utils.BROASDCAST_HOTFIX_ACTION));
        }

    }

    private void setHotfixInstallerResult(HotfixResult hotfixResult) {
        patchResultListener.onHotfixResultReceived(hotfixResult);
    }


    private BroadcastReceiver resultBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            HotfixResult hotfixResult = (HotfixResult) intent.getSerializableExtra(Utils.BROASDCAST_HOTFIX_RESULT);
            setHotfixInstallerResult(hotfixResult);
            if (context == null) {
                throw new RuntimeException("content is null !");
            }
            context.unregisterReceiver(resultBroadcastReceiver);

        }
    };


//    /**
//     * clean all patch files!
//     *
//     * @param context
//     */
//    public  void cleanPatch(Context context) {
//        if (context == null) {
//            TinkerLog.e(TAG, "HotfixPatcher received a null context, ignoring.");
//            return;
//        }
//        TinkerInstaller.cleanPatch(context);
//    }

    /**
     * usage for native library
     * use TinkerInstaller.loadLibraryFromTinker replace your System.loadLibrary for auto update library!
     *
     * @param relativePath such as lib/armeabi
     * @param libname      for the lib libTest.so, you can pass Test or libTest, or libTest.so
     * @return boolean
     */
    public boolean loadLibrary(String relativePath, String libname) {
        if (context == null) {
            TinkerLog.e(TAG, "HotfixPatcher received a null context, ignoring.");
            return false;
        }
        if (context == null) {
            TinkerLog.e(TAG, "HotfixPatcher received a null patchLocation, ignoring.");
            return false;
        }
        return TinkerInstaller.loadLibraryFromTinker(context, relativePath, libname);
    }


    /**
     * some file does not exist, repair them with :patch process
     * Generally you will not use it
     *
     * @param patchLocation
     */
    private void onReceiveRepairPatch(String patchLocation) {
        TinkerInstaller.onReceiveRepairPatch(context, patchLocation);
    }
}
