package com.mx.engine.utils;


import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;


/**
 * Created by liuyuxuan on 16/5/23.
 */
public class ObjectUtilsTest {


    public static class TestObject {
        String name;
        int count;
    }

    public void testObjectToHexString() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("ObjectUtilsTest");
        arrayList.add("AndroidTestCase");
        String hexString = ObjectUtils.objectToHexString(arrayList);
        assertNotNull(hexString);
    }
    @Test
    public void testHexStringToObject() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("ObjectUtilsTest");
        arrayList.add("AndroidTestCase");
        String hexString = ObjectUtils.objectToHexString(arrayList);
        ArrayList<String> strings = ObjectUtils.hexStringToObject(hexString);
        assertNotNull(strings);
        assertEquals(strings.get(0), "ObjectUtilsTest");
        assertEquals(strings.get(1), "AndroidTestCase");
    }

    public void testCast() {
        Object obj = "testCast";
        String txt = ObjectUtils.cast(obj, String.class);
        assertNotNull(txt);
        assertEquals(obj, txt);
    }

    public void testNewInstance() {
        TestObject testObject = ObjectUtils.newInstance(TestObject.class);
        assertNotNull(testObject);
        TestObject testObject1 = ObjectUtils.newInstance(TestObject.class.getName());
        assertNotNull(testObject1);
    }
}