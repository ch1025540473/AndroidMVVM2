package com.mx.gunit;

import android.os.Looper;
import android.support.annotation.Nullable;

import org.junit.Assert;

import java.util.Collection;
import java.util.Map;

/**
 * Created by chenbaocheng on 17/5/3.
 */

public abstract class Asserts extends Assert {

    static public void assertCollectionSizeEqual(Collection<?> collection, int expectedSize) {
        assertNotNull(collection);
        if (collection.size() != expectedSize) {
            fail("collection size =" + collection.size() + ", not equal expected size=" + expectedSize);
        }
    }

    static public void assertCollectionSizeNotEqual(Collection<?> collection, int expectedSize) {
        assertNotNull(collection);
        if (collection.size() == expectedSize) {
            fail("collection size =" + collection.size() + ", equal expected size=" + expectedSize);
        }
    }

    public static void assertNotEmpty(@Nullable final Collection<?> collection) {
        assertNotEmpty(collection, null);
    }

    public static void assertNotEmpty(@Nullable final Collection<?> collection, final String name) {
        assertNotNull(name, collection);
        if (collection.isEmpty()) {
            String formatted = "";
            if (name != null) {
                formatted = name + ".";
            }
            fail(formatted + "this collection contains no elements");
        }
    }


    public static void assertNoNullElements(@Nullable final Collection<?> collection) {
        assertNoNullElements(collection, null);
    }

    public static void assertNoNullElements(@Nullable final Collection<?> collection, final String name) {
        assertNotNull(name, collection);
        for (final Object o : collection) {
            if (null == o) {
                String formatted = "";
                if (name != null) {
                    formatted = name + ".";
                }
                fail(formatted + "collection contain null elements");
            }
        }
    }

    public static void assertNoNullElements(@Nullable final Object[] array) {
        assertNoNullElements(array, null);
    }

    public static void assertNoNullElements(@Nullable final Object[] array, final String name) {
        assertNotNull(name, array);
        for (final Object o : array) {
            if (null == o) {
                String formatted = "";
                if (name != null) {
                    formatted = name + ".";
                }
                fail(formatted + "cannot contain null elements");
            }
        }
    }

    public static void assertNotEmpty(@Nullable final Map<?, ?> map) {
        assertNotEmpty(null, map);
    }

    public static void assertNotEmpty(final String name, @Nullable final Map<?, ?> map) {
        assertNotNull(name, map);
        if (map.isEmpty()) {
            String formatted = "";
            if (name != null) {
                formatted = name + ".";
            }
            fail(formatted + "cannot be empty");
        }
    }

    public static void assertIsMainThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            fail("Current thread is not the main thread");
        }
    }
}
