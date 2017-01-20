package com.mx.webbridge;

import android.net.Uri;
import android.webkit.WebResourceResponse;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenbaocheng on 17/1/11.
 */

public class WebBridge {
    private Uri uri;
    private PipedInputStream inputStream;
    private PipedOutputStream outputStream;
    private WebResourceResponse response;

    private WebBridge() {
    }

    public WebResourceResponse getResponse() {
        return response;
    }

    class Builder {
        private Uri uri;
        private String mimeType = "text/json";
        private String encoding = "UTF-8";
        private int statusCode = 200;
        private String reasonPhrase = "";
        private Map<String, String> headers = new HashMap<>();

        public void build() {
            //TODO
        }

        public Builder setUri(Uri uri) {
            this.uri = uri;
            return this;
        }

        public Builder setMimeType(String mime) {
            this.mimeType = mime;
            return this;
        }

        public Builder setEncoding(String encoding) {
            this.encoding = encoding;
            return this;
        }

        public Builder setStatusCode(int statusCode, String reasonPhrase) {
            this.statusCode = statusCode;
            this.reasonPhrase = reasonPhrase;
            return this;
        }

        public Builder putHeader(String name, String value) {
            this.headers.put(name, value);
            return this;
        }
    }


}
