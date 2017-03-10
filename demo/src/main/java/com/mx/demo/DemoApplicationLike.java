package com.mx.demo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;

import com.mx.framework2.ModuleManager;
import com.mx.hotfix.HotfixApplication;
import com.mx.router.Router;
import com.mx.router.converter.BundleConverter;
import com.mx.router.converter.FragmentConverter;
import com.mx.router.converter.ViewConverter;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.loader.shareutil.ShareConstants;

import java.util.Locale;

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
        super.onBaseContextAttached(wrapContext(base, Locale.CHINESE)); // wrapContext context to change language
        Router.getDefault().init("gomeplus://com.mx");
        Router.getDefault().addDataConverter(new FragmentConverter());
        Router.getDefault().addDataConverter(new ViewConverter());
        Router.getDefault().addDataConverter(new BundleConverter());
        ModuleManager.getInstance().init(base);
        ModuleManager.getInstance().installModule(DemoModule.get());
    }

    /**
     * Get current system locale
     *
     * @param context the context
     * @return current system locale
     */
    protected static Locale getSystemLocale(Context context) {
        Configuration config = context.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!config.getLocales().isEmpty()) {
                return config.getLocales().get(0);
            }
        } else {
            //noinspection deprecation
            return config.locale;
        }

        return null;
    }

    /**
     * Get a context with a new locale.
     *
     * @param context the context
     * @param locale  new locale
     * @return a context with new locale
     */
    private static Context wrapContext(Context context, Locale locale) {
        Locale sysLocale = getSystemLocale(context);
        if (sysLocale != null && sysLocale.equals(locale)) {
            // The same locale, no need to change anything
            return context;
        }

        Locale.setDefault(locale);
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocales(new LocaleList(locale));
        } else {
            //noinspection deprecation
            config.locale = locale;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return context.createConfigurationContext(config);
        } else {
            //noinspection deprecation
            resources.updateConfiguration(config, dm);
            return context;
        }
    }
}
