package com.mx.framework2.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;

import com.mx.framework2.BuildConfig;
import com.mx.framework2.ReflectUtil;
import com.mx.framework2.viewmodel.LifecycleViewModel;
import com.mx.framework2.viewmodel.ViewModelManager;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * Created by wwish on 16/9/12.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class BaseActivityTest {


    BaseActivity mBaseActivity;

    public class TestActivityResultListener implements BaseActivity.ActivityResultListener {
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        }
    }

    @Before
    public void setUp() throws Exception {
        mBaseActivity=new BaseActivity();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testOnAttachFragment() throws Exception {

    }

    @Test
    public void testRegisterActivityResultListener() throws Exception {
//        BaseActivity BaseActivity= Mockito.spy(mBaseActivity);
        TestActivityResultListener activityResultListener=new TestActivityResultListener();
        mBaseActivity.registerActivityResultListener(1,activityResultListener);
        Field field = BaseActivity.class.getDeclaredField("activityResultListeners");
        field.setAccessible(true);
//        field.set(mBaseActivity, new SparseArray<BaseActivity.ActivityResultListener>());
        SparseArray<TestActivityResultListener> activityResultListeners =(SparseArray<TestActivityResultListener>)field.get(mBaseActivity);
        assertEquals(activityResultListener,activityResultListeners.get(1));

    }

    @Test
    public void testOnActivityResult() throws Exception {

    }

    @Test
    public void testOnWindowFocusChanged() throws Exception {
        Field field = BaseActivity.class.getDeclaredField("isHasFocus");
        field.setAccessible(true);
        boolean isHasFocus=(boolean)field.get(mBaseActivity);

//        Field field1 = BaseActivity.class.getDeclaredField("viewModelManager");
//        field1.setAccessible(true);
//        ViewModelManager vm=(ViewModelManager)field1.get(mBaseActivity);
//        field1.set(mBaseActivity,vm);
        System.out.println(isHasFocus);
        assertEquals(isHasFocus,false);
        ReflectUtil.invoke(mBaseActivity,"init",new Class []{Bundle.class},new Object[]{null});
        mBaseActivity.onWindowFocusChanged(true);
        System.out.println(isHasFocus);
        assertEquals(true,isHasFocus);
    }

    @Test
    public void testOnCreate() throws Exception {

    }

    @Test
    public void testInit() throws Exception {
        ReflectUtil.invoke(mBaseActivity,"init",new Class []{Bundle.class},new Object[]{null});
        Field field = BaseActivity.class.getDeclaredField("runState");
        field.setAccessible(true);
        RunState runState=(RunState)field.get(mBaseActivity);

        assertEquals(RunState.Created,runState);
    }

    @Test
    public void testOnStart() throws Exception {

    }

    @Test
    public void testOnStop() throws Exception {

    }

    @Test
    public void testOnSaveInstanceState() throws Exception {

    }

    @Test
    public void testOnRestoreInstanceState() throws Exception {

    }

    @Test
    public void testOnDestroy() throws Exception {

    }

    @Test
    public void testOnResume() throws Exception {

    }

    @Test
    public void testOnPause() throws Exception {

    }

    @Test
    public void testGetViewModelManager() throws Exception {

    }

    @Test
    public void testGetUseCaseHolderId() throws Exception {

    }

    @Test
    public void testAddViewModel() throws Exception {
        LifecycleViewModel lifecycleViewModel=new LifecycleViewModel();
        mBaseActivity.addViewModel(lifecycleViewModel);
//        addViewModel
    }

    @Test
    public void testRemoveViewModel() throws Exception {

    }

    @Test
    public void testShowLoadingDialog() throws Exception {

    }

    @Test
    public void testShowLoadingDialog1() throws Exception {

    }

    @Test
    public void testDismissLoadingDialog() throws Exception {

    }
}