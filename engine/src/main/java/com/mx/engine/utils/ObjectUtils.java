package com.mx.engine.utils;

import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Created by liuyuxuan on 16/4/29.
 */
public class ObjectUtils {

    public static <T extends Serializable> String objectToHexString(T object) {
        String hexString = null;
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            hexString = StringUtils.bytesToHexString(bytes);
        } catch (Exception e) {
        } finally {
            if (null != baos) {
                try {
                    baos.close();
                } catch (IOException e) {
                }
            }
            if (null != oos) {
                try {
                    oos.close();
                } catch (IOException e) {
                }
            }
        }
        return hexString;
    }

    public static <T extends Serializable> T hexStringToObject(String hexString) {
        T object = null;
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {

            byte[] bytes = StringUtils.hexStringToBytes(hexString);
            if (bytes == null) {
                return null;
            }
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            object = (T) ois.readObject();

        } catch (Exception e) {
            return null;
        } finally {
            if (null != bais) {
                try {
                    bais.close();
                } catch (Exception e) {
                }
            }
            if (null != ois) {
                try {
                    bais.close();
                } catch (Exception e) {
                }
            }
        }

        return object;
    }


    public static <T> T cast(Object obj, Class<T> type) {
        if (!type.isInstance(obj)) {
            return null;
        }
        return type.cast(obj);
    }

    public static <T> T newInstance(String className) {

        try {
            Class<?> cls = Class.forName(className);
            T t = (T) cls.newInstance();
            return t;
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T newInstance(Class<T> cls) {
        try {
            T t = cls.newInstance();
            return t;
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T newInstance(Class<T> cls, Object object) {

        if (null == object) {
            return newInstance(cls);
        }

        try {
            cls.getConstructor(object.getClass()).newInstance(object);
            T value = (T) cls.getConstructors()[0].newInstance(object);
            return value;
        } catch (Exception e) {

        }

        return newInstance(cls);

    }

    /**
     * Get the Field object from an object's class or its all super classes.
     *
     * @param obj  the object to search
     * @param name field name
     * @return Field object
     * @throws NoSuchFieldException no field found with given name
     */
    public static Field getField(Object obj, String name) throws NoSuchFieldException {
        if (obj == null || TextUtils.isEmpty(name)) {
            return null;
        }

        Field field = null;
        NoSuchFieldException exception = null;

        Class<?> type = obj.getClass();
        while (type != null && !type.equals(Object.class)) {
            // loop up to Object, null means a primitive type
            try {
                field = type.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                // hold the first exception
                if (exception == null) {
                    exception = e;
                }
            }

            if (field != null) {
                // found the field, exit the loop
                break;
            } else {
                // if no such field in current class, then search super class
                type = type.getSuperclass();
            }
        }

        if (field == null && exception != null) {
            // didn't find any field
            throw exception;
        }

        return field;
    }

    /**
     * Get the value of a field
     *
     * @param obj  the object
     * @param name the field name
     * @return the value
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Object getFieldValue(Object obj, String name) throws NoSuchFieldException, IllegalAccessException {
        Field field = getField(obj, name);
        if (field == null) {
            return null;
        }

        field.setAccessible(true);
        return field.get(obj);
    }

}
