/*
 * Tencent is pleased to support the open source community by making Tinker available.
 *
 * Copyright (C) 2016 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mx.hotfix.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.mx.hotfix.patcher.HotfixResult;
import com.mx.hotfix.util.Utils;
import com.tencent.tinker.lib.service.DefaultTinkerResultService;
import com.tencent.tinker.lib.service.PatchResult;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.lib.util.TinkerServiceInternals;
import com.tencent.tinker.loader.shareutil.SharePatchFileUtil;

import java.io.File;


public class HotfixResultService extends DefaultTinkerResultService {
    private static final String TAG = "HotfixResultService";



    @Override
    public void onPatchResult(final PatchResult result) {
        if (result == null) {
            TinkerLog.e(TAG, "HotfixResultService received null result!!!!");
            return;
        }
        TinkerLog.i(TAG, "HotfixResultService receive result: %s", result.toString());

        //first, we want to kill the recover process
        TinkerServiceInternals.killTinkerPatchServiceProcess(getApplicationContext());

        // is success and newPatch, it is nice to delete the raw file, and restart at once
        // for old patch, you can't delete the patch file
        if (result.isSuccess && result.isUpgradePatch) {

            File rawFile = new File(result.rawPatchFilePath);
            if (rawFile.exists()) {
//                Properties properties = ShareTinkerInternals.fastGetPatchPackageMeta(rawFile);
//                if (properties != null) {
//                    storeResult(properties.getProperty(Utils.PATCHVERSION));
//                    TinkerLog.d(TAG, "print patch version code " + properties.getProperty(Utils.PATCHVERSION));
//                }
                TinkerLog.i(TAG, "save delete raw patch file");
                SharePatchFileUtil.safeDeleteFile(rawFile);
            }
            //not like TinkerResultService, I want to restart just when I am at background!
            //if you have not install tinker this moment, you can use TinkerApplicationHelper api
            if (checkIfNeedKill(result)) {
                if (Utils.isBackground()) {
                    TinkerLog.i(TAG, "it is in background, just restart process");
                    restartProcess();
                } else {
                    //we can wait process at background, such as onAppBackground
                    //or we can restart when the screen off
                    TinkerLog.i(TAG, "tinker wait screen to restart process");
                    new ScreenState(getApplicationContext(), new ScreenState.IOnScreenOff() {
                        @Override
                        public void onScreenOff() {
                            restartProcess();
                        }
                    });
                }
            } else {
                TinkerLog.i(TAG, "I have already install the newly patch version!");
            }
        }

        if (result == null) {
            TinkerLog.e(TAG, "HotfixResultService received null result!!!!");
            return;
        }
        TinkerLog.i(TAG, "HotfixResultService receive result: %s", result.toString());
        HotfixResult hotfixResult = getHotfixResult(result);
        Intent broadcastIntent = new Intent(Utils.BROASDCAST_HOTFIX_ACTION);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(Utils.BROASDCAST_HOTFIX_RESULT, hotfixResult);
        broadcastIntent.putExtras(mBundle);
        getApplicationContext().sendBroadcast(broadcastIntent);

        //repair current patch fail, just clean!
        if (!result.isSuccess && !result.isUpgradePatch) {
            //if you have not install tinker this moment, you can use TinkerApplicationHelper api
            Tinker.with(getApplicationContext()).cleanPatch();
        }
    }

    /**
     * you can restart your process through service or broadcast
     */
    private void restartProcess() {
        TinkerLog.i(TAG, "app is background now, i can kill quietly");
        //you can send service or broadcast intent to restart your process
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    static class ScreenState {
        interface IOnScreenOff {
            void onScreenOff();
        }

        ScreenState(Context context, final IOnScreenOff onScreenOffInterface) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            context.registerReceiver(new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent in) {
                    String action = in == null ? "" : in.getAction();
                    TinkerLog.i(TAG, "ScreenReceiver action [%s] ", action);
                    if (Intent.ACTION_SCREEN_OFF.equals(action)) {

                        context.unregisterReceiver(this);

                        if (onScreenOffInterface != null) {
                            onScreenOffInterface.onScreenOff();
                        }
                    }
                }
            }, filter);
        }
    }


    private HotfixResult getHotfixResult(PatchResult result) {
        HotfixResult hotfixResult = new HotfixResult();
        hotfixResult.costTime = result.costTime;
        hotfixResult.e = result.e;
        hotfixResult.isSuccess = result.isSuccess;
        hotfixResult.isUpgradePatch = result.isUpgradePatch;
        hotfixResult.patchVersion = result.patchVersion;
        hotfixResult.rawPatchFilePath = result.rawPatchFilePath;
        return hotfixResult;
    }

}
