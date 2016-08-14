package com.mx.framework2.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;


import com.mx.engine.event.EventProxy;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import static com.mx.engine.utils.CheckUtils.checkNotNull;

/***
 * 1. 内存中缓存数据;
 * 2. SharedPreferences中持久化数据;
 * 3. 对releam数据库的操作;
 * 4. 网络访问;
 * 5. 接收,发送广播;
 */
public abstract class UseCase {

    UseCaseManager useCaseManager;
    List<WeakReference<UseCaseHolder>> useCaseHodlers;
    private Context context;

    final void setUseCaseManager(@Nullable UseCaseManager useCaseManager) {
        checkNotNull(useCaseManager);
        this.useCaseManager = useCaseManager;
    }

    final void setContext(Context context) {
        this.context = context;
    }

    protected final <Value extends Serializable> void preferencePutInt(@Nullable String fileName, @Nullable String key, int value) {

        checkNotNull(fileName);
        checkNotNull(key);
        checkNotNull(value);
        checkNotNull(context);

        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(key, value).apply();
    }

    protected final <Value extends Serializable> void preferencePutLong(@Nullable String fileName, @Nullable String key, @Nullable long value) {

        checkNotNull(fileName);
        checkNotNull(key);
        checkNotNull(value);
        checkNotNull(context);

        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        sharedPreferences.edit().putLong(key, value).apply();
    }

    protected final <Value extends Serializable> void preferencePutFloat(@Nullable String fileName, @Nullable String key, float value) {
        checkNotNull(fileName);
        checkNotNull(key);
        checkNotNull(value);
        checkNotNull(context);

        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        sharedPreferences.edit().putFloat(key, value).apply();
    }


    protected final <Value extends Serializable> void preferencePutString(@Nullable String fileName, @Nullable String key, String value) {

        checkNotNull(fileName);
        checkNotNull(key);
        checkNotNull(value);
        checkNotNull(context);

        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key, value).apply();
    }

    protected float preferenceGetFloat(@Nullable String fileName, @Nullable String key) {

        checkNotNull(fileName);
        checkNotNull(key);
        checkNotNull(context);

        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(key, 0);
    }


    protected String preferenceGetString(@Nullable String fileName, @Nullable String key) {

        checkNotNull(fileName);
        checkNotNull(key);
        checkNotNull(context);

        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    protected long preferenceGetLong(@Nullable String fileName, @Nullable String key) {

        checkNotNull(fileName);
        checkNotNull(key);
        checkNotNull(context);

        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, 0);
    }

    protected int preferenceGetInt(@Nullable String fileName, @Nullable String key) {
        checkNotNull(fileName);
        checkNotNull(key);
        checkNotNull(context);

        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, 0);
    }

    final synchronized void open(UseCaseHolder useCaseHolder) {

        if (null == useCaseHodlers) {
            useCaseHodlers = new LinkedList<>();
        }
        if (!isOpen()) {
            EventProxy.getDefault().register(this);
            onOpen();
        }

        if (!isExistHolder(useCaseHolder)) {
            useCaseHodlers.add(new WeakReference(useCaseHolder));
        }
    }

    final synchronized void close(UseCaseHolder UseCaseHolder) {
        removeUseCaseHolder(UseCaseHolder);
        if (!isOpen()) {
            EventProxy.getDefault().unregister(this);
            useCaseHodlers.clear();
            onClose();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void checkCloseUseCase(CloseUseCaseEvent closeUseCasEvent) {
        close(null);
    }

    private boolean isExistHolder(UseCaseHolder useCaseHolder) {

        if (useCaseHodlers == null || useCaseHodlers.size() < 1) {
            return false;
        }
        ListIterator<WeakReference<UseCaseHolder>> iterator = useCaseHodlers.listIterator();
        while (iterator.hasNext()) {
            WeakReference<UseCaseHolder> reference = iterator.next();
            if (reference.get() != null && reference.get() == useCaseHolder) {
                return true;
            }
        }
        return false;
    }

    private void removeUseCaseHolder(UseCaseHolder useCaseHolder) {
        if (useCaseHodlers == null || useCaseHodlers.size() < 1) {
            return;
        }
        ListIterator<WeakReference<UseCaseHolder>> iterator = useCaseHodlers.listIterator();
        while (iterator.hasNext()) {
            WeakReference<UseCaseHolder> reference = iterator.next();
            if (reference.get() != null && reference.get().equals(useCaseHolder)) {
                iterator.remove();
                return;
            }
        }
    }

    private boolean isOpen() {
        if (useCaseHodlers == null || useCaseHodlers.size() < 1) {
            return false;
        }
        ListIterator<WeakReference<UseCaseHolder>> iterator = useCaseHodlers.listIterator();
        while (iterator.hasNext()) {
            WeakReference<UseCaseHolder> reference = iterator.next();
            if (reference.get() == null) {
                iterator.remove();
            }
        }
        return useCaseHodlers.size() > 0;
    }


    protected abstract void onOpen();

    protected abstract void onClose();

    protected Context getContext() {
        return this.context;
    }

}
