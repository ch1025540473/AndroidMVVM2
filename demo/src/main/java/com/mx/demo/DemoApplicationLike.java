package com.mx.demo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.mx.framework2.ModuleManager;
import com.mx.hotfix.HotfixApplication;
import com.mx.router.Router;
import com.mx.router.converter.BundleConverter;
import com.mx.router.converter.FragmentConverter;
import com.mx.router.converter.ViewConverter;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * Created by chenbaocheng on 16/8/15.
 */
@SuppressWarnings("unused")
@DefaultLifeCycle(application = "com.mx.demo.DemoApplication",
        flags = ShareConstants.TINKER_ENABLE_ALL,
        loadVerifyFlag = false)
public class DemoApplicationLike extends HotfixApplication {

    public DemoApplicationLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag,
                               long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent,
                               Resources[] resources, ClassLoader[] classLoader, AssetManager[] assetManager) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent, resources, classLoader, assetManager);
    }

    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        Router.getDefault().init("gomeplus://com.mx");
        Router.getDefault().addDataConverter(new FragmentConverter());
        Router.getDefault().addDataConverter(new ViewConverter());
        Router.getDefault().addDataConverter(new BundleConverter());
        ModuleManager.getInstance().init(base);
        ModuleManager.getInstance().installModule(DemoModule.get());
    }
}
