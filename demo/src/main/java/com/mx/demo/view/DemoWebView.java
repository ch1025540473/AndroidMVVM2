package com.mx.demo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mx.activitystarter.ActivityResultCallback;
import com.mx.activitystarter.ActivityStarter;
import com.mx.demo.view.ui.ThirdActivity;
import com.mx.engine.json.GsonFactory;
import com.mx.framework2.view.ui.BaseActivity;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenbaocheng on 16/11/11.
 */

public class DemoWebView extends WebView {

    public static final String TEST_PAGE_URL = "file:///android_asset/load_test.html";

    public DemoWebView(Context context) {
        super(context);
        setWebView(context);
    }

    public DemoWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWebView(context);
    }

    public DemoWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWebView(context);
    }

    private void setWebView(final Context context) {
        setWebViewClient(new WebViewClient() {
            @SuppressLint("NewApi")
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                String target = "http://architect.mx.com/load/test.json";
                if (url.equals(target)) {
                    final PipedOutputStream os = new PipedOutputStream();
                    final InputStream is;
                    try {
                        is = new PipedInputStream(os);
                        WebResourceResponse resp = new WebResourceResponse("text/json", "UTF-8", is);

                        resp.setResponseHeaders(new HashMap<String, String>() {
                            {
                                put("Access-Control-Allow-Origin", "*");
                            }
                        });

                        final Writer writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ActivityStarter s = BaseActivity.getTopActivityStarter();
                                final String name = context.getClass().getName();
                                s.startActivityForResult(new Intent(context, ThirdActivity.class)
                                        , new ActivityResultCallback() {
                                            @Override
                                            public void onActivityResult(int resultCode, Intent data) {
                                                Map<String, String> map = new HashMap<>();
                                                map.put("abc", "汉字");//data.getStringExtra("data"));
                                                try {
                                                    writer.write(GsonFactory.newGson().toJson(map));
                                                    writer.close();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                            }
                        }, 500);
                        return resp;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(String.format("<WEB> not equals"));
                return super.shouldInterceptRequest(view, url);
            }
        });
        setWebChromeClient(new WebChromeClient());

        requestFocus();

        WebSettings settings = getSettings();
        settings.setAppCacheEnabled(true);
        settings.setAppCacheMaxSize(8 * 1024 * 1024);
        settings.setAppCachePath(context.getCacheDir().getAbsolutePath() + "webviewCache"); //设置缓存路径

        settings.setDomStorageEnabled(true);
        settings.setDatabasePath("");

        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        if (Build.VERSION.SDK_INT >= 21) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        System.out.println("WEB loading");
        loadUrl(TEST_PAGE_URL);
        System.out.println("WEB loaded");
    }
}
