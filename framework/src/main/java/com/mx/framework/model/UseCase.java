package com.mx.framework.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;


import java.io.Serializable;

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

    final void open() {
        onOpen();
    }

    public final void close() {
        onClose();

        if (null != useCaseManager) {
            useCaseManager.closeUseCase(this);
        }
    }

    protected abstract void onOpen();

    protected abstract void onClose();

    protected Context getContext() {
        return this.context;
    }

}
