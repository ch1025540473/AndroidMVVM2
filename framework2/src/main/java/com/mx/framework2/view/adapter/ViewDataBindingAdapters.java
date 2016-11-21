package com.mx.framework2.view.adapter;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.view.View;
import android.widget.ImageView;

import com.mx.framework2.view.ImageGestureView;
import com.mx.framework2.viewmodel.command.OnClickCommand;
import com.mx.framework2.viewmodel.command.OnLoadImageCommand;
import com.mx.framework2.viewmodel.command.OnLongCommand;

/**
 * Created by liuyuxuan on 16/8/23.
 */
public class ViewDataBindingAdapters {

    @BindingConversion
    public static View.OnClickListener click(final OnClickCommand onClickCommand) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickCommand != null) {
                    onClickCommand.execute(view.getId());
                }
            }
        };
    }

    @BindingAdapter("longClick")
    public static void longClick(View view, final OnLongCommand onLongCommand) {
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return onLongCommand.onLongCommand(v.getId());
            }
        });
    }

    @BindingConversion
    public static String classConversion(Class className) {
        return className.getName();
    }


    @BindingAdapter("showOrHide")
    public static void showOrHide(View view, boolean showOrHide) {
        view.setVisibility(showOrHide ? View.VISIBLE : View.GONE);
    }

    public static void setImageLoadCommand(ImageGestureView imageGestureView, final OnLoadImageCommand onLoadImageCommand) {
        imageGestureView.setOnLoadImageListener(new ImageGestureView.OnLoadImageListener() {
            @Override
            public void onLoadSuccess(ImageGestureView imageGestureView, int w, int h) {
                onLoadImageCommand.onLoadSuccess(imageGestureView.getId(), w, h);
            }

            @Override
            public void onLoadFailure(ImageGestureView imageGestureView, Throwable throwable) {
                onLoadImageCommand.onLoadFailure(imageGestureView.getId(), throwable);
            }
        });
    }

    @BindingAdapter("android:src")
    public static void adaptSrcToRes(final View view, final int resId) {
        if (view instanceof ImageView) {
            ((ImageView) view).setImageResource(resId);
        }
    }

    @BindingAdapter("android:visibility")
    public static void adaptVisibility(final View view, final boolean isVisible) {
        if (view != null) {
            view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }
}
