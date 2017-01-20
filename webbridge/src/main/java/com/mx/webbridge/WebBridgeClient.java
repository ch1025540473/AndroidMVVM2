package com.mx.webbridge;

import android.net.Uri;
import android.os.Build;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by chenbaocheng on 17/1/11.
 */

public class WebBridgeClient extends WebViewClient {
    protected WebBridge shouldInterceptRequest(WebView view, Uri uri) {
        return null;
    }

    private WebResourceResponse processRequest(WebView view, Uri uri) {
        WebBridge bridge = shouldInterceptRequest(view, uri);
        if (bridge == null) {
            return null;
        }

        return bridge.getResponse();
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WebResourceResponse response = processRequest(view, request.getUrl());
            if (response != null) {
                return response;
            }
        }

        return super.shouldInterceptRequest(view, request);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            WebResourceResponse response = processRequest(view, Uri.parse(url));
            if (response != null) {
                return response;
            }
        }

        return super.shouldInterceptRequest(view, url);
    }
}
