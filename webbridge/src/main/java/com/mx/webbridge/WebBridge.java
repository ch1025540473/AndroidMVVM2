package com.mx.webbridge;
import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.webkit.WebResourceResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenbaocheng on 17/1/11.
 */

public abstract class WebBridge {
    private Uri uri;
    private PipedInputStream inputStream;
    private PipedOutputStream outputStream;
    private WebResourceResponse response;
    private Writer writer;

    private WebBridge() {
    }

    public Writer getWriter() {
        return writer;
    }

    WebResourceResponse getResponse() {
        return response;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public  static class Builder {
        private Uri uri;
        private String mimeType = "text/json";
        private String encoding = "UTF-8";
        private int statusCode = 200;
        private String reasonPhrase = "HTTPOK";
        private Map<String, String> headers = new HashMap<>();

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public WebBridge build() throws UnsupportedEncodingException {
            WebBridge webBridge = new WebBridge() {
            };
            webBridge.outputStream = new PipedOutputStream();
            try {
                webBridge.inputStream = new PipedInputStream(webBridge.outputStream);
            } catch (IOException e) {
                // never happen
            }

            try {
                webBridge.writer = new OutputStreamWriter(webBridge.outputStream, encoding);
            } finally {
                try {
                    webBridge.outputStream.close();
                    webBridge.outputStream.close();
                } catch (IOException e) {
                    // never happen
                }
            }
            webBridge.response = new WebResourceResponse(mimeType, encoding, webBridge.inputStream);
            webBridge.uri = uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webBridge.response.setStatusCodeAndReasonPhrase(statusCode, reasonPhrase);
                webBridge.response.setResponseHeaders(headers);
            }

            return webBridge;
        }

        public Builder setUri(Uri uri) {
            this.uri = uri;
            return this;
        }

        /**
         * Sets the resource response's MIME type, for example "text/html".
         * @param mime  The resource response's MIME type
         * @return
         */
        public Builder setMimeType(String mime) {
            this.mimeType = mime;
            return this;
        }

        /**
         * Sets the resource response's encoding, for example "UTF-8". This is used to decode the data from the input stream.
         * @param encoding The resource response's encoding
         * @return
         */
        public Builder setEncoding(String encoding) {
            this.encoding = encoding;
            return this;
        }

        /**
         * Sets the resource response's status code and reason phrase.
         * @param statusCode the status code needs to be in the ranges [100, 299], [400, 599]. Causing a redirect by specifying a 3xx code is not supported.
         * @param reasonPhrase the phrase describing the status code, for example "OK". Must be non-null and not empty.
         * @return
         */
        public Builder setStatusCode(int statusCode, String reasonPhrase) {
            this.statusCode = statusCode;
            this.reasonPhrase = reasonPhrase;
            return this;
        }

        /**
         * Sets the headers for the resource response.
         * Mapping of header name -> header value.
         * @param name  header name
         * @param value header value
         * @return
         */
        public Builder putHeader(String name, String value) {
            this.headers.put(name, value);
            return this;
        }
    }


}
