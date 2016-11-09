/*
 * This file provided by Facebook is for non-commercial testing and evaluation
 * purposes only.  Facebook reserves all rights not expressly granted.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * FACEBOOK BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.mx.engine.image;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.internal.Sets;
import com.facebook.common.internal.Supplier;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.ByteConstants;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSubscriber;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import okhttp3.OkHttpClient;

/**
 * 图片配置
 */
class ImageConfig {
    /***
     * 最大可运行内存
     */
    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();

    /**
     * 最大文件缓存限制
     */
    private static final int MAX_DISK_CACHE_SIZE = 40 * ByteConstants.MB;

    /**
     * 最大内存限制
     */
    private static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 4;

    /**
     * 缓存目录
     */
    private static final String IMAGE_PIPELINE_CACHE_DIR = "gome_plus_cache";

    /**
     * 是否显示图片
     */
    public static final boolean DISPLAY_IMAGE = true;

    /**
     * Pipeline配置
     */
    private static ImagePipelineConfig sOkHttpImagePipelineConfig;

    /**
     * 非wifi环境是否显示图片
     */
    private static boolean undisplayOutOffWifiNetWork = false;

    /**
     * Creates config using OkHttp as network backed.
     */
    public static ImagePipelineConfig getOkHttpImagePipelineConfig(Context context, OkHttpClient client) {
        if (sOkHttpImagePipelineConfig == null) {
            ImagePipelineConfig.Builder configBuilder = OkHttpImagePipelineConfigFactory.newBuilder(context, client);
            configureCaches(context, configBuilder);
            configureLoggingListeners(configBuilder);
            sOkHttpImagePipelineConfig = configBuilder.build();
        }
        return sOkHttpImagePipelineConfig;
    }

    /**
     * Configures disk and memory cache not to exceed common limits
     */
    private static void configureCaches(Context context, ImagePipelineConfig.Builder configBuilder) {
        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
                ImageConfig.MAX_MEMORY_CACHE_SIZE, // Max total size of elements in the cache
                Integer.MAX_VALUE,                     // Max entries in the cache
                ImageConfig.MAX_MEMORY_CACHE_SIZE, // Max total size of elements in eviction queue
                Integer.MAX_VALUE,                     // Max length of eviction queue
                Integer.MAX_VALUE);                    // Max cache entry size
        configBuilder
                .setBitmapMemoryCacheParamsSupplier(
                        new Supplier<MemoryCacheParams>() {
                            public MemoryCacheParams get() {
                                return bitmapCacheParams;
                            }
                        })
                .setMainDiskCacheConfig(
                        DiskCacheConfig.newBuilder(context)
                                .setBaseDirectoryPath(context.getApplicationContext().getCacheDir())
                                .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)
                                .setMaxCacheSize(ImageConfig.MAX_DISK_CACHE_SIZE)
                                .build())
//                .setDecodeFileDescriptorEnabled(true)
//                .setBitmapsConfig(Bitmap.Config.ARGB_8888) //TODO: if need high picture quality, remove comment here
//                .setResizeAndRotateEnabledForNetwork(true)
                .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig());
    }

    private static void configureLoggingListeners(ImagePipelineConfig.Builder configBuilder) {
        configBuilder.setRequestListeners(Sets.newHashSet((RequestListener) new RequestLoggingListener()));
    }

    private static ImageRequestBuilder setImageRequestBuilder(ImageRequestBuilder builder, Priority priority, ResizeOptions options) {
        builder.setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
//                .setAutoRotateEnabled(true)                      // 支持自动旋转
//                .setLocalThumbnailPreviewsEnabled(true)          // 设置使用本地预览图
                .setProgressiveRenderingEnabled(true);           // 支持渐进式图片
        if (priority != null) {
            builder.setRequestPriority(priority);                // 设置优先级
        }
        if (options != null) {
            builder.setResizeOptions(options);
        }
        return builder;
    }

    private static ImageRequest createImageRequestWidthUrl(String url, Priority priority, ResizeOptions options) {
        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url)).setAutoRotateEnabled(true);
        builder = setImageRequestBuilder(builder, priority, options);
        return builder.build();
    }

    private static ImageRequest createImageRequestWidthLocalResource(int resId) {
        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithResourceId(resId);
        builder = setImageRequestBuilder(builder, null, null);
        return builder.build();
    }

    /**
     * 获取PipelineDraweeControllerBuilder实例，实现对加载图片的控制和定制
     *
     * @param callerContext
     * @param view
     * @return
     */
    private static PipelineDraweeControllerBuilder getControllerBuilder(Object callerContext, SimpleDraweeView view) {
        return Fresco.newDraweeControllerBuilder()
                .setRetainImageOnFailure(false)
                .setOldController(view.getController())
                .setAutoPlayAnimations(true)
                .setCallerContext(callerContext)
                .setTapToRetryEnabled(true);
    }

    static String getCompressedImageUrl(String url, int size) {
        if (TextUtils.isEmpty(url)) return "";
        String newUrl = "";
        if (url.endsWith(".jpg")) {
            String prefix = url.substring(0, url.indexOf(".jpg"));
            newUrl = prefix + "_" + size + ".jpg";
        } else if (url.endsWith(".png")) {
            String prefix = url.substring(0, url.indexOf(".png"));
            newUrl = prefix + "_" + size + ".png";
        } else {
            newUrl = url;
        }
        return newUrl;
    }

    /**
     * 通过资源Id加载图片
     *
     * @param callerContext
     * @param view
     * @param resId
     */
    static void load(Object callerContext, SimpleDraweeView view, int resId) {
        PipelineDraweeControllerBuilder controllerBuilder = getControllerBuilder(callerContext, view);
        ImageRequest request = createImageRequestWidthLocalResource(resId);
        controllerBuilder.setImageRequest(request);
        view.setController(controllerBuilder.build());
    }

    /**
     * 加载图片方法，
     * 支持的类型：
     * 远程图片	http://, https://	HttpURLConnection 或者参考 使用其他网络加载方案
     * 本地文件	file://	FileInputStream
     * Content provider	content://	ContentResolver
     * asset目录下的资源	asset://	AssetManager
     * res目录下的资源	res://	Resources.openRawResource
     *
     * @param callerContext
     * @param view
     * @param url
     * @param thumbnailUrl
     * @param priority
     * @param options
     * @param isAdvUrl
     */
    static void load(Object callerContext, SimpleDraweeView view, String url, String thumbnailUrl, Priority priority, ResizeOptions options, boolean isAdvUrl) {
        // 禁用网络图片的情况下,只加载本地缓存图片
        if (undisplayOutOffWifiNetWork && !isAdvUrl) {
            Uri uri = Uri.parse(url);
            boolean isInMemoryCache = Fresco.getImagePipeline().isInBitmapMemoryCache(uri);
            DataSource<Boolean> dataSource = Fresco.getImagePipeline().isInDiskCache(uri);
            boolean isInDiskCache = (dataSource != null && dataSource.hasResult() && dataSource.getResult());
            if (!isInMemoryCache && !isInDiskCache) return;
        }

        ImageRequest thumbnailRequest = null;
        if (!TextUtils.isEmpty(thumbnailUrl)) {
            thumbnailRequest = createImageRequestWidthUrl(thumbnailUrl, priority, options);
        }
        ImageRequest request = null;
        if (!TextUtils.isEmpty(url)) {
            request = createImageRequestWidthUrl(url, priority, options);
        }

        PipelineDraweeControllerBuilder controllerBuilder = getControllerBuilder(callerContext, view);
        if (thumbnailRequest != null) {
            controllerBuilder.setLowResImageRequest(thumbnailRequest);
        }
        if (request != null) {
            controllerBuilder.setImageRequest(request);
        }
        view.setController(controllerBuilder.build());
    }

    static void setUndisplayOutOffWifiNetWork(boolean show) {
        undisplayOutOffWifiNetWork = show;
    }


    /**订阅bitmap*/
    static void load(Object callerContext,SimpleDraweeView view, String url,Priority priority, ResizeOptions options,DataSubscriber<CloseableReference<CloseableImage>> subscriber) {
//        Uri uri = Uri.parse(url);
//        boolean isInMemoryCache = Fresco.getImagePipeline().isInBitmapMemoryCache(uri);
//        boolean isInDiskCache = (dataSource != null && dataSource.hasResult() && dataSource.getResult());

        ImageRequest request = null;
        if (!TextUtils.isEmpty(url)) {
            request = createImageRequestWidthUrl(url, priority, options);
        }

        Fresco.getImagePipeline().fetchDecodedImage(request,callerContext).subscribe(subscriber, CallerThreadExecutor.getInstance());
    }
}
