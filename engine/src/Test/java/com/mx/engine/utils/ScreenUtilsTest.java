package com.mx.engine.utils;

import android.app.Application;
import android.content.Context;
import android.view.View;


import com.mx.engine.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static junit.framework.Assert.assertEquals;

/**
 * Created by liuyuxuan on 16/5/24.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class ScreenUtilsTest {

    Context context;

    @Before
    public void setUp() {
        Application application = RuntimeEnvironment.application;
        context = application.getBaseContext();
    }

    @Test
    public void test() {

        String txt="[qq]三大科拉罗夫大煞风景的是[哈哈]";
//        Pattern p = Pattern.compile("(?<=\\[).*?(?=\\])");
        Pattern p = Pattern.compile("\\[(.*?)\\]");
        Matcher m = p.matcher(txt);
        List<String> result=new ArrayList<String>();
        while(m.find()){
            result.add(m.group());
        }
        for(String s1:result){
            System.out.println(s1);
        }
    }

    @Test
    public void testDpToPx() throws Exception {
        float p = ScreenUtils.dp2Px(context, 3);
        assertEquals(3.0f, p);
        p = ScreenUtils.dp2Px(null, 3);
        assertEquals(-1.f, p);
    }

    @Test
    public void testPxToDp() throws Exception {
        ScreenUtils.dp2Px(context, 10);
    }

    @Test
    public void density() {
        ScreenUtils.density(context);
    }


    @Test
    public void testPx2DpCeilInt() throws Exception {
        ScreenUtils.px2DpCeilInt(context, 2.3f);
    }

    @Test
    public void testSp2px() throws Exception {
        ScreenUtils.sp2px(context, 33f);

    }

    @Test
    public void testPx2sp() throws Exception {
        ScreenUtils.px2sp(context, 11f);

    }

    @Test
    public void testGetDisplayMetrics() throws Exception {
        ScreenUtils.getDisplayMetrics(context);
    }

    @Test
    public void testGetScreenPixelSize() throws Exception {
        ScreenUtils.getScreenWidth(context);

    }

    @Test
    public void testHideSoftInputKeyBoard() throws Exception {
        View view = Mockito.mock(View.class);
        ScreenUtils.hideSoftInputKeyBoard(context, view);
        Mockito.verify(view).getWindowToken();
    }

    @Test
    public void testShowSoftInputKeyBoard() throws Exception {
        ScreenUtils.showSoftInputKeyBoard(context, null);
    }

    @Test
    public void testGetStatusBarHeight() throws Exception {
        ScreenUtils.getStatusBarHeight(context);
    }

    @Test
    public void testGetScreenWidth() {
        ScreenUtils.getScreenWidth(context);
    }

    @Test
    public void testGetScreenHeight() {
        ScreenUtils.getScreenHeight(context);
    }

}