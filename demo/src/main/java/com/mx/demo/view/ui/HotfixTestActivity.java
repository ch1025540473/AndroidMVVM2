package com.mx.demo.view.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mx.demo.R;
import com.mx.hotfix.patcher.HotfixPatcher;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;

/**
 * Created by wwish on 16/11/3.
 */

public class HotfixTestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d("www", "iiiii999999iii4444666iiiiiiiii");
        setContentView(R.layout.activity_third);
        findViewById(R.id.result_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                HotfixInstaller.onReceiveUpgradePatch(getApplicationContext(), Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip.apk");
                HotfixPatcher hotfixInstaller = HotfixPatcher.getInstance();
                hotfixInstaller.patch(Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip.apk");
//                hotfixInstaller.onReceiveUpgradePatch(getApplicationContext(), Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip.apk",new PatchResultListener(){
//                    @Override
//                    public void onHotfixResultReceived(HotfixResult path) {
//                        Log.d("www", "iiiiiooipiiyyiHotfixResultiiiiii ii"+path.isSuccess);
//                    }
//                });


                Log.d("www", "iiiiii5iiiiiiiii");
            }
        });

        findViewById(R.id.result_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TinkerInstaller.loadArmLibrary(getApplicationContext(), "stlport_shared");
////                TinkerInstaller.loadLibraryFromTinker(getApplicationContext(), "assets/x86", "stlport_shared");
//                android.os.Process.killProcess(android.os.Process.myPid());
//                Tinker.with(getApplicationContext()).cleanPatch();
                Log.d("www", "iiiiiiiiiiiiiiii9");
                HotfixPatcher hotfixInstaller = HotfixPatcher.getInstance();
                Log.d("www", "hotfixInstaller.getPatchVersion(getApplicationContext()) version : " + hotfixInstaller.getPatchVersion());
                Log.d("www", "HotfixLoader.getLoadPatchFileVersion() " + HotfixPatcher.getInstance().getPatchVersion());
            }
        });
    }


    public boolean showInfo(Context context) {
        // add more Build Info
        final StringBuilder sb = new StringBuilder();
        Tinker tinker = Tinker.with(getApplicationContext());
        if (tinker.isTinkerLoaded()) {
            sb.append(String.format("[patch is loaded] \n"));
            sb.append(String.format("[buildConfig TINKER_ID] %s \n", BuildInfo.TINKER_ID));
            sb.append(String.format("[buildConfig BASE_TINKER_ID] %s \n", BaseBuildInfo.BASE_TINKER_ID));

            sb.append(String.format("[buildConfig MESSSAGE] %s \n", BuildInfo.MESSAGE));
            sb.append(String.format("[TINKER_ID] %s \n", tinker.getTinkerLoadResultIfPresent().getPackageConfigByName(ShareConstants.TINKER_ID)));
            sb.append(String.format("[packageConfig patchMessage] %s \n", tinker.getTinkerLoadResultIfPresent().getPackageConfigByName("patchMessage")));
            sb.append(String.format("[TINKER_ID Rom Space] %d k \n", tinker.getTinkerRomSpace()));

//            sb.append(String.format("[patch is loaded] \n"));
//            sb.append(String.format("[buildConfig CLIENTVERSION] %s \n", BuildInfo.CLIENTVERSION));
//            sb.append(String.format("[buildConfig MESSSAGE] %s \n", BuildInfo.MESSAGE));
//            sb.append(String.format("[TINKER_ID] %s \n", tinker.getTinkerLoadResultIfPresent().getPackageConfigByName(ShareConstants.TINKER_ID)));
//            sb.append(String.format("[REAL TINKER_ID] %s \n", tinker.getTinkerLoadResultIfPresent().getTinkerID()));
//            sb.append(String.format("[packageConfig patchMessage] %s \n", tinker.getTinkerLoadResultIfPresent().getPackageConfigByName("patchMessage")));
//            sb.append(String.format("[TINKER_ID Rom Space] %d k \n", tinker.getTinkerRomSpace()));

        } else {
            sb.append(String.format("[patch is not loaded] \n"));
            sb.append(String.format("[buildConfig TINKER_ID] %s \n", BuildInfo.TINKER_ID));
            sb.append(String.format("[buildConfig BASE_TINKER_ID] %s \n", BaseBuildInfo.BASE_TINKER_ID));

            sb.append(String.format("[buildConfig MESSSAGE] %s \n", BuildInfo.MESSAGE));
            sb.append(String.format("[TINKER_ID] %s \n", ShareTinkerInternals.getManifestTinkerID(getApplicationContext())));
//            sb.append(String.format("[patch is not loaded] \n"));
//            sb.append(String.format("[buildConfig CLIENTVERSION] %s \n", BuildInfo.CLIENTVERSION));
//            sb.append(String.format("[buildConfig MESSSAGE] %s \n", BuildInfo.MESSAGE));
//            sb.append(String.format("[TINKER_ID] %s \n", ShareTinkerInternals.getManifestTinkerID(getApplicationContext())));
        }
        sb.append(String.format("[BaseBuildInfo Message] %s \n", BaseBuildInfo.TEST_MESSAGE));

        final TextView v = new TextView(context);
        v.setText(sb);
        v.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        v.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        v.setTextColor(0xFF000000);
        v.setTypeface(Typeface.MONOSPACE);
        final int padding = 16;
        v.setPadding(padding, padding, padding, padding);

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setView(v);
        final AlertDialog alert = builder.create();
        alert.show();
        return true;
    }


}
