package com.mx.framework;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created by liuyuxuan on 16/6/21.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class StringTest {

    @Test
    public void testLength() throws Exception {
        try {
            Class c = Class.forName("com.android.internal.os.BackgroundThread");
            System.out.println(c);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int count(int n) {
        if (n < 2) {
            return n;
        } else {
            System.out.println(n);
            return count(n - 2) + count(n - 1);
        }
    }


}
