package com.mx.engine.image;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;

import com.mx.engine.widget.DraweeImageView;
import com.facebook.datasource.DataSubscriber;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.image.ImageInfo;

import okhttp3.OkHttpClient;

/**
 * 图片加载封装类
 * <p>
 * Created by wuhenzhizao on 15/12/31.
 */
public class ImageLoader {

    private static ImageLoader imageLoader;

    public static ImageLoader instance() {

        if (null != imageLoader) {
            return imageLoader;
        }

        synchronized (ImageLoader.class) {
            if (null == imageLoader) {
                imageLoader = new ImageLoader();
            }
        }

        return imageLoader;
    }


    /**
     * Frasco初始化
     *
     * @param context
     */
    public void initAndConfig(Application context, OkHttpClient client) {
        Fresco.initialize(context, ImageConfig.getOkHttpImagePipelineConfig(context, client));
    }

    /**
     * 通过图片资源ID，显示图片
     *
     * @param callerContext : Activity || Fragment，由当前页面类型决定
     * @param view
     * @param resId         ：app内部图片ID
     */
    public void displayRes(Object callerContext, DraweeImageView view, int resId) {
        ImageConfig.load(callerContext, view, resId);
    }

    /**
     * 显示Url图片
     *
     * @param callerContext : Activity || Fragment，由当前页面类型决定
     * @param view
     * @param url           : 图片地址
     *                      支持的类型：
     *                      远程图片	http://, https://	HttpURLConnection 或者参考 使用其他网络加载方案
     *                      本地文件	file://	FileInputStream
     *                      Content provider	content://	ContentResolver
     *                      asset目录下的资源	asset://	AssetManager
     *                      res目录下的资源	res://	Resources.openRawResource
     */
    public void displayUrl(Object callerContext, DraweeImageView view, String url) {
        display(callerContext, view, url, null, 0, null, null);
    }

    /**
     * 显示本地路径下的图片
     * @param callerContext
     * @param view
     * @param url
     * @param options
     */
    public void displayFileUrl(Object callerContext, DraweeImageView view, String url,ResizeOptions options) {
        display(callerContext, view, "file://"+url, null, 0, Priority.HIGH, options);
    }

    /**
     * 显示Url图片，压缩图片大小
     * 应用场景：获取服务端指定大小图片
     *
     * @param callerContext : Activity || Fragment，由当前页面类型决定
     * @param view
     * @param url
     * @param size          : 从接口获取的图片尺寸，目前仅支持3中，{@link ImageSize }
     */
    public void displayResizeUrl(Object callerContext, DraweeImageView view, String url, int size) {
        display(callerContext, view, url, null, size, null, null);
    }

    /**
     * 显示Url图片
     * 应用场景：优先加载某些图片
     *
     * @param callerContext : Activity || Fragment，由当前页面类型决定
     * @param view
     * @param url
     * @param priority      : 优先级
     */
    public void displayPriorityUrl(Object callerContext, DraweeImageView view, String url, Priority priority) {
        display(callerContext, view, url, null, 0, priority, null);
    }

    public void displayResizePriorityUrl(Object callerContext, DraweeImageView view, String url, int size, Priority priority) {
        display(callerContext, view, url, null, size, priority, null);
    }

    /**
     * 显示Url图片，压缩图片大小
     * 应用场景：如，相册加载本地图片
     *
     * @param callerContext : Activity || Fragment，由当前页面类型决定
     * @param view
     * @param url
     * @param options       ： 图片缩放选项
     */
    public void displayAlbumUrl(Object callerContext, DraweeImageView view, String url, ResizeOptions options) {
        display(callerContext, view, url, null, 0, null, options);
    }

    /**
     * 加载图片，先显示低分辨率的图，然后是高分辨率的图
     *
     * @param callerContext : Activity || Fragment，由当前页面类型决定
     * @param view
     * @param url
     * @param thumbnailUrl  ：低分辨率图片
     */
    public void displayThumbnailUrl(Object callerContext, DraweeImageView view, String url, String thumbnailUrl) {
        display(callerContext, view, url, thumbnailUrl, 0, null, null);
    }

    public void displayThumbnailResizeUrl(Object callerContext, DraweeImageView view, String url, String thumbnailUrl, int size) {
        display(callerContext, view, url, thumbnailUrl, size, null, null);
    }

    /**
     * 显示图片
     *
     * @param callerContext ：绑定的对象，可以是Actiivty或Fragment
     * @param view          : 显示图片的控件
     * @param url           ：图片地址
     * @param thumbnailUrl  ：低分辨率图片地址
     * @param size          ：图片大小
     * @param priority      ：优先级，可以设置是否优先加载, LOW, MEDIUM, HIGH
     */
    private void display(Object callerContext, DraweeImageView view, String url, String thumbnailUrl, int size, Priority priority, ResizeOptions options) {
        //这种写法调用者不知道.......
        if (!(callerContext instanceof Activity) && !(callerContext instanceof Fragment)) {
            throw new ClassCastException("请传入正确的CallerContext类型，必须是Activity或Fragment");
        }

        if (size == ImageSize.SIZE_60 || size == ImageSize.SIZE_260 || size == ImageSize.SIZE_360) {
            url = ImageConfig.getCompressedImageUrl(url, size);
        }
        ImageConfig.load(callerContext, view, url, thumbnailUrl, priority, options, false);

    }

    /**
     * 显示广告图，不受设置中省流模式的影响
     *
     * @param callerContext
     * @param view
     * @param url
     */
    public void displayAdvUrl(Object callerContext, DraweeImageView view, String url) {
        ImageConfig.load(callerContext, view, url, null, null, null, true);
    }

    /**
     * 从内存中移除图片
     */
    public void removeImageFromCache(String url) {
        if (!TextUtils.isEmpty(url)) {
            Fresco.getImagePipeline().evictFromCache(Uri.parse(url));
        }
    }

    /**
     * 清理内存缓存，在内存不足时手动调用
     */
    public void clearMemoryCache() {
        Fresco.getImagePipeline().clearMemoryCaches();
    }

    /**
     * 清理硬盘缓存
     */
    public void clearDiskCache() {
        ImagePipelineFactory factory = Fresco.getImagePipelineFactory();
        factory.getImagePipeline().clearCaches();
    }

    /**
     * 获取缓存大小
     *
     * @return
     */
    public double getDiskCacheSize() {
        return Fresco.getImagePipelineFactory().getMainDiskStorageCache().getSize();
    }

    /**
     * 释放图片缓存资源
     */
    public void releaseResources() {
        Fresco.shutDown();
    }

    /**
     * 是否在移动网络下显示图片
     *
     * @param showImageInMobileNetwork
     */
    public void setShowImageInMobileNetwork(boolean showImageInMobileNetwork) {
        ImageConfig.setUndisplayOutOffWifiNetWork(showImageInMobileNetwork);
    }
    /**网络加载*/
    public void loadBitmapFromUrl(Object callerContext, DraweeImageView view, String url,DataSubscriber dataSubscriber) {
        loadBitmap(callerContext, view, url, Priority.HIGH, 0, null,dataSubscriber);
    }

    /**本地加载*/
    public void loadBitmapFromFileUrl(Object callerContext, DraweeImageView view, String url, ResizeOptions options,DataSubscriber subscriber) {
        loadBitmap(callerContext, view, "file://"+url,Priority.HIGH, 0, options,subscriber);
    }


    private void loadBitmap(Object callerContext, final DraweeImageView view, String url, Priority priority, int size, ResizeOptions options, DataSubscriber subscriber) {
        if (!(callerContext instanceof Activity) && !(callerContext instanceof Fragment)) {
            throw new ClassCastException("请传入正确的CallerContext类型，必须是Activity或Fragment");
        }

        if (size == ImageSize.SIZE_60 || size == ImageSize.SIZE_260 || size == ImageSize.SIZE_360) {
            url = ImageConfig.getCompressedImageUrl(url, size);
        }
        ImageConfig.load(callerContext,view,url,priority,options,subscriber);

    }

    /**
     * 通过imageWidth 的宽度，自动适应高度
     *
     * @param draweeImageView view
     * @param url             Uri
     * @param imageWidth      width
     * @param mContext        context
     */
    public static void displayFixSizeImage(Context mContext, final DraweeImageView draweeImageView, String url, int imageSize, final int imageWidth) {

        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                if (imageInfo == null) {
                    return;
                }
                final ViewGroup.LayoutParams layoutParams = draweeImageView.getLayoutParams();
                int height = imageInfo.getHeight();
                int width = imageInfo.getWidth();
                layoutParams.width = imageWidth;
                layoutParams.height = (int) ((float) (imageWidth * height) / (float) width);
                draweeImageView.setLayoutParams(layoutParams);
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                Log.d("TAG", "Intermediate image received");
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                throwable.printStackTrace();
            }
        };

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setControllerListener(controllerListener)
                .setUri(Uri.parse(ImageConfig.getCompressedImageUrl(url, imageSize)))
                .build();
        draweeImageView.setController(controller);
    }

    /**
     * 网络加载
     */
    public void loadBitmapFromUrl(Context callerContext, DraweeImageView view, String url, DataSubscriber dataSubscriber) {
        loadBitmap(callerContext, view, url, Priority.HIGH, 0, null, dataSubscriber);
    }

    /**
     * 本地加载
     */
    public void loadBitmapFromFileUrl(Context callerContext, DraweeImageView view, String url, ResizeOptions options, DataSubscriber subscriber) {
        loadBitmap(callerContext, view, "file://" + url, Priority.HIGH, 0, options, subscriber);
    }

    private void loadBitmap(Context callerContext, final DraweeImageView view, String url, Priority priority, int size, ResizeOptions options, DataSubscriber subscriber) {
        if (!(callerContext instanceof Activity)) {
            throw new ClassCastException("请传入正确的CallerContext类型，必须是Activity");
        }

        if (size == ImageSize.SIZE_60 || size == ImageSize.SIZE_260 || size == ImageSize.SIZE_360) {
            url = ImageConfig.getCompressedImageUrl(url, size);
        }
        ImageConfig.load(callerContext, view, url, priority, options, subscriber);

    }
}