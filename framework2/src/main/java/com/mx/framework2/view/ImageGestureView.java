package com.mx.framework2.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.DraweeHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imageformat.ImageFormatChecker;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.mx.framework2.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import me.kareluo.intensify.image.IntensifyImageView;

/**
 * Created by liuyuxuan on 2016/11/4.
 */
//TODO 后期重构到独立的module中
public class ImageGestureView extends FrameLayout {
    private Uri uri;
    private InputStream imageInput;
    private OnLoadImageListener onLoadImageListener;

    public interface OnLoadImageListener {

        void onLoadSuccess(ImageGestureView imageGestureView, int w, int h);

        void onLoadFailure(ImageGestureView imageGestureView, Throwable throwable);
    }

    public ImageGestureView(Context context) {
        super(context);
    }

    public ImageGestureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageGestureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnLoadImageListener(OnLoadImageListener onLoadImageListener) {
        this.onLoadImageListener = onLoadImageListener;
    }

    public void setImageUri(Uri uri) {
        setTag(uri);
        if (this.uri != null) {
            try {
                if (imageInput != null) {
                    imageInput.close();
                }
            } catch (IOException e) {

            }
        }
        this.uri = uri;
        loadImage();
    }

    public void setSrc(String url) {
        setImageUri(Uri.parse(url));
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        try {
            if (imageInput != null) {
                imageInput.close();
            }
        } catch (IOException e) {

        }
        imageInput = null;
    }

    private void loadImage() {
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(1, 1))
                .build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        final GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(getResources()).build();
        DraweeHolder<GenericDraweeHierarchy> draweeHolder = DraweeHolder.create(hierarchy, getContext());
        final PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setOldController(draweeHolder.getController())
                .setImageRequest(imageRequest)
                .build();
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, this);
        dataSource.subscribe(new BaseDataSubscriber<CloseableReference<CloseableImage>>() {
                                 @Override
                                 protected void onNewResultImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                                     if (dataSource.getResult() == null) {
                                         return;
                                     }
                                     if (!getTag().equals(uri)) {
                                         CloseableReference.closeSafely(dataSource.getResult());
                                         return;
                                     }
                                     File file = getImageFile();
                                     BitmapFactory.Options options = new BitmapFactory.Options();
                                     options.inJustDecodeBounds = true;
                                     BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                                     ImageFormat imageFormat = ImageFormatChecker.getImageFormat(file.getAbsolutePath());
                                     try {
                                         if (imageInput != null) {
                                             imageInput.close();
                                         }
                                         imageInput = new FileInputStream(file);
                                         updateContentView(imageFormat.equals(ImageFormat.GIF));
                                         if (onLoadImageListener != null) {
                                             onLoadImageListener.onLoadSuccess(ImageGestureView.this, options.outWidth, options.outHeight);
                                         }
                                     } catch (Exception e) {
                                         if (onLoadImageListener != null) {
                                             onLoadImageListener.onLoadFailure(ImageGestureView.this, e);
                                         }
                                     } finally {
                                         CloseableReference.closeSafely(dataSource.getResult());
                                     }
                                 }

                                 @Override
                                 protected void onFailureImpl
                                         (DataSource<CloseableReference<CloseableImage>> dataSource) {
                                     if (onLoadImageListener != null) {
                                         onLoadImageListener.onLoadFailure(ImageGestureView.this, dataSource.getFailureCause());
                                     }
                                     if (dataSource.getResult() != null) {
                                         CloseableReference.closeSafely(dataSource.getResult());
                                     }
                                 }
                             }

                , UiThreadImmediateExecutorService.getInstance());

        controller.onClick();
    }

    public File getImageFile() {
        if (uri == null) {
            return null;
        }
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
                .getEncodedCacheKey(ImageRequest.fromUri(uri), getContext());
        BinaryResource resource = ImagePipelineFactory.getInstance()
                .getMainFileCache().getResource(cacheKey);
        return ((FileBinaryResource) resource).getFile();
    }

    private void updateContentView(boolean isGifFormat) {
        removeAllViews();
        if (isGifFormat) {
            SimpleDraweeView simpleDraweeView = createGifView();
            simpleDraweeView.setId(R.id.image_detail_id);
            simpleDraweeView.setImageURI(ImageGestureView.this.uri);
            addView(simpleDraweeView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        } else {
            IntensifyImageView intensifyImageView = createLargerImageView();
            intensifyImageView.setId(R.id.image_detail_id);
            intensifyImageView.setImage(imageInput);
            addView(intensifyImageView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
    }

    protected IntensifyImageView createLargerImageView() {
        return (IntensifyImageView) LayoutInflater.from(getContext()).inflate(R.layout.intensify_image, null);

    }

    protected SimpleDraweeView createGifView() {
        return new SimpleDraweeView(getContext());
    }


}
