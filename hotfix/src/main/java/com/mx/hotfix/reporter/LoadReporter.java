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

package com.mx.hotfix.reporter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;

import com.mx.hotfix.util.UpgradePatchRetry;
import com.mx.hotfix.util.Utils;
import com.tencent.tinker.lib.reporter.DefaultLoadReporter;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.shareutil.ShareConstants;

import java.io.File;


/**
 * optional, you can just use DefaultLoadReporter
 */
public class LoadReporter extends DefaultLoadReporter {
    private Handler handler = new Handler();

    public LoadReporter(Context context) {
        super(context);
    }

    @Override
    public void onLoadPatchListenerReceiveFail(final File patchFile, int errorCode, final boolean isUpgrade) {
        super.onLoadPatchListenerReceiveFail(patchFile, errorCode, isUpgrade);
        switch (errorCode) {
            case ShareConstants.ERROR_PATCH_NOTEXIST:
//                Toast.makeText(context, "patch file is not exist", Toast.LENGTH_LONG).show();
                break;
            case ShareConstants.ERROR_PATCH_RUNNING:
                // try later
                // only retry for upgrade patch
                if (isUpgrade) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TinkerInstaller.onReceiveUpgradePatch(context, patchFile.getAbsolutePath());
                        }
                    }, 60 * 1000);
                }
                break;
            case Utils.ERROR_PATCH_ROM_SPACE:
//                Toast.makeText(context, "rom space is not enough", Toast.LENGTH_LONG).show();
                break;
        }
        HotfixReport.onTryApplyFail(errorCode);
    }

    @Override
    public void onLoadResult(File patchDirectory, int loadCode, long cost) {
        super.onLoadResult(patchDirectory, loadCode, cost);
        switch (loadCode) {
            case ShareConstants.ERROR_LOAD_OK:
                HotfixReport.onLoaded(cost);
                break;
        }
        Looper.getMainLooper().myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                UpgradePatchRetry.getInstance(context).onPatchRetryLoad();
                return false;
            }
        });
    }

    @Override
    public void onLoadException(Throwable e, int errorCode) {
        super.onLoadException(e, errorCode);
        HotfixReport.onLoadException(e, errorCode);
    }

    @Override
    public void onLoadFileMd5Mismatch(File file, int fileType) {
        super.onLoadFileMd5Mismatch(file, fileType);
        HotfixReport.onLoadFileMisMatch(fileType);
    }

    @Override
    public void onLoadFileNotFound(File file, int fileType, boolean isDirectory) {
        super.onLoadFileNotFound(file, fileType, isDirectory);
        HotfixReport.onLoadFileNotFound(fileType);
    }

    @Override
    public void onLoadPackageCheckFail(File patchFile, int errorCode) {
        super.onLoadPackageCheckFail(patchFile, errorCode);
        HotfixReport.onLoadPackageCheckFail(errorCode);
    }

    @Override
    public void onLoadPatchInfoCorrupted(String oldVersion, String newVersion, File patchInfoFile) {
        super.onLoadPatchInfoCorrupted(oldVersion, newVersion, patchInfoFile);
        HotfixReport.onLoadInfoCorrupted();
    }

    @Override
    public void onLoadPatchVersionChanged(String oldVersion, String newVersion, File patchDirectoryFile, String currentPatchName) {
        super.onLoadPatchVersionChanged(oldVersion, newVersion, patchDirectoryFile, currentPatchName);
    }

}
