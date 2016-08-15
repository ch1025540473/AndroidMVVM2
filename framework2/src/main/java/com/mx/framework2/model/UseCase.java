package com.mx.framework2.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;


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

    private List<WeakReference<UseCaseHolder>> useCaseHolders = new LinkedList<>();
    private Context context;
    private boolean isOpen = false;

    final void setContext(Context context) {
        this.context = context;
    }

    protected Context getContext() {
        return this.context;
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

    final synchronized void open() {
        isOpen = true;
        onOpen();
        clean();
    }

    final synchronized void close() {
        if (isOpen) {
            useCaseHolders.clear();
            isOpen = false;
            onClose();
        }
    }

    private boolean useCaseHolderExists(UseCaseHolder useCaseHolder) {
        ListIterator<WeakReference<UseCaseHolder>> iterator = useCaseHolders.listIterator();
        while (iterator.hasNext()) {
            WeakReference<UseCaseHolder> reference = iterator.next();
            if (reference.get() != null && reference.get() == useCaseHolder) {
                return true;
            }
        }
        return false;
    }

    private void removeUseCaseHolder(UseCaseHolder useCaseHolder) {
        ListIterator<WeakReference<UseCaseHolder>> iterator = useCaseHolders.listIterator();
        while (iterator.hasNext()) {
            WeakReference<UseCaseHolder> reference = iterator.next();
            if (reference.get() != null && reference.get().equals(useCaseHolder)) {
                iterator.remove();
                return;
            }
        }
    }

    void addUseCaseHolder(UseCaseHolder useCaseHolder) {
        if (!useCaseHolderExists(useCaseHolder)) {
            useCaseHolders.add(new WeakReference(useCaseHolder));
        }
    }

    final boolean isOpen() {
        return isOpen;
    }

    private void clean() {
        ListIterator<WeakReference<UseCaseHolder>> iterator = useCaseHolders.listIterator();
        while (iterator.hasNext()) {
            WeakReference<UseCaseHolder> reference = iterator.next();
            if (reference.get() == null || reference.get().isDestroyed()) {
                iterator.remove();
            }
        }
    }

    public void onHolderDestroy(UseCaseHolder holder) {
        removeUseCaseHolder(holder);
        clean();
        if (useCaseHolders.size() == 0) {
            close();
        }
    }

    protected abstract void onOpen();

    protected abstract void onClose();

}
