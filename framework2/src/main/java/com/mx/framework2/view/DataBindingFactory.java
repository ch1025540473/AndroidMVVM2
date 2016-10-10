package com.mx.framework2.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.mx.framework2.view.ui.BaseActivity;
import com.mx.framework2.viewmodel.Lifecycle;
import com.mx.framework2.viewmodel.ViewModel;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by liuyuxuan on 16/8/18.
 */
public class DataBindingFactory {

    public static <T extends ViewDataBinding> T setContentView(BaseActivity baseActivity, @LayoutRes int layout) {
        final T value = DataBindingUtil.setContentView(baseActivity, layout);
        value.getRoot().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {
                List<Lifecycle> lifecycleList = getLifecycleListFromDataBinding(value);
                for (Lifecycle lifecycle : lifecycleList) {
                    lifecycle.attachedToView();
                }
            }

            @Override
            public void onViewDetachedFromWindow(View view) {
                List<Lifecycle> lifecycleList = getLifecycleListFromDataBinding(value);
                for (Lifecycle lifecycle : lifecycleList) {
                    lifecycle.detachedFromView();
                }

            }
        });
        return value;
    }

    public static
    @NonNull
    List<ViewModel> getViewModelsFromDataBinding(ViewDataBinding viewDataBinding) {
        return findByClass(viewDataBinding, ViewModel.class);
    }

    private static
    @NonNull
    List<Lifecycle> getLifecycleListFromDataBinding(ViewDataBinding viewDataBinding) {
        return findByClass(viewDataBinding, Lifecycle.class);
    }

    private static
    @NonNull
    <T> List<T> findByClass(ViewDataBinding viewDataBinding, Class<T> clazz) {
        List<T> list = new LinkedList<>();
        try {
            Field[] fields = viewDataBinding.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object obj = field.get(viewDataBinding);
                if (clazz.isInstance(obj)) {
                    list.add(clazz.cast(obj));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static final <T extends ViewDataBinding> T inflate(Context context, @LayoutRes int layoutId) {
        final T value = DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, null, false);
        value.getRoot().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {
                List<Lifecycle> lifecycleList = getLifecycleListFromDataBinding(value);
                for (Lifecycle lifecycle : lifecycleList) {
                    lifecycle.attachedToView();
                }
            }

            @Override
            public void onViewDetachedFromWindow(View view) {
                List<Lifecycle> lifecycleList = getLifecycleListFromDataBinding(value);
                for (Lifecycle lifecycle : lifecycleList) {
                    lifecycle.detachedFromView();
                }
            }
        });
        return value;
    }
}
