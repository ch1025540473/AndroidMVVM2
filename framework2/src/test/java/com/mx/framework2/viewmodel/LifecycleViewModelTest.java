package com.mx.framework2.viewmodel;

import android.content.Intent;
import android.util.SparseArray;

import com.mx.engine.event.EventProxy;
import com.mx.framework2.BuildConfig;
import com.mx.framework2.MyRunner;
import com.mx.framework2.ReflectUtil;
import com.mx.framework2.view.ui.BaseActivity;
import com.mx.framework2.view.ui.RunState;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;

/**
 * Created by wwish on 16/8/30.
 */
@RunWith(MyRunner.class)
@Config(constants = BuildConfig.class)
public class LifecycleViewModelTest {

    public static class TestModel extends LifecycleViewModel{

    }
    BaseActivity baseActivity;

    @Before
    public void setUp() throws Exception {
        baseActivity= Mockito.mock(BaseActivity.class);


    }

    @After
    public void tearDown() throws Exception {
        baseActivity=null;

    }

    @Test
    public void testStartActivityForResult() throws Exception {
        Mockito.when(baseActivity.getRunState()).thenReturn(RunState.Created);
        TestModel testModel = new TestModel();
        testModel.setViewModelScope(baseActivity);
        Field field = BaseActivity.class.getDeclaredField("activityResultListeners");
        field.setAccessible(true);
        field.set(baseActivity, new SparseArray<BaseActivity.ActivityResultListener>());
        testModel.startActivityForResult(Mockito.mock(Intent.class), 22);
        Mockito.verify(baseActivity).startActivityForResult(ArgumentCaptor.forClass(Intent.class).capture(),
                ArgumentCaptor.forClass(Integer.class).capture());


    }

    @Test
    public void testGetActivity() throws Exception {
        Mockito.when(baseActivity.getRunState()).thenReturn(RunState.Created);
        TestModel testModel = new TestModel();
        testModel.setViewModelScope(baseActivity);

        BaseActivity baseActivity1=(BaseActivity) ReflectUtil.invoke(testModel,"getActivity");

        assertEquals(baseActivity1,baseActivity);

    }

    @Test
    public void testGetViewModelScope() throws Exception {
        Mockito.when(baseActivity.getRunState()).thenReturn(RunState.Created);
        TestModel testModel = new TestModel();
        testModel.setViewModelScope(baseActivity);

        ViewModelScope viewModelScope=(ViewModelScope) ReflectUtil.invoke(testModel,"getActivity");

        assertEquals(viewModelScope,baseActivity);

    }

    @Test
    public void testInit() throws Exception {
        Mockito.when(baseActivity.getRunState()).thenReturn(RunState.Created);
        TestModel testModel = new TestModel();
        testModel.setViewModelScope(baseActivity);

        ReflectUtil.invoke(testModel,"init");
        Field eventProxyField = LifecycleViewModel.class.getDeclaredField("eventProxy");
        eventProxyField.setAccessible(true);
        EventProxy eventProxy = (EventProxy) eventProxyField.get(testModel);
        assertNotNull(eventProxy);



    }

    @Test
    public void testRecycle() throws Exception {
        Mockito.when(baseActivity.getRunState()).thenReturn(RunState.Created);
        TestModel testModel = new TestModel();
        testModel.setViewModelScope(baseActivity);

        ReflectUtil.invoke(testModel,"init");
        ReflectUtil.invoke(testModel,"recycle");
        Field eventProxyField = LifecycleViewModel.class.getDeclaredField("eventProxy");
        eventProxyField.setAccessible(true);
        EventProxy eventProxy = (EventProxy) eventProxyField.get(testModel);
        assertNotNull(eventProxy);

    }

    @Test
    public void testAttachedToView() throws Exception {
        Mockito.when(baseActivity.getRunState()).thenReturn(RunState.Created);
        TestModel testModel = new TestModel();
        testModel.setViewModelScope(baseActivity);
        testModel = Mockito.spy(testModel);
        testModel.attachedToView();
//        Mockito.verify(testModel).attachedToView();


    }

    @Test
    public void testDetachedFromView() throws Exception {
        Mockito.when(baseActivity.getRunState()).thenReturn(RunState.Created);
        TestModel testModel = new TestModel();
        testModel.setViewModelScope(baseActivity);
        testModel = Mockito.spy(testModel);
        testModel.detachedFromView();
//        Mockito.verify(testModel).detachedFromView();

    }

    @Test
    public void testCreate() throws Exception {
        Mockito.when(baseActivity.getRunState()).thenReturn(RunState.Created);
        TestModel testModel = new TestModel();
        testModel.setViewModelScope(baseActivity);
        testModel = Mockito.spy(testModel);
        testModel.create(null);
        assertEquals(LifecycleState.Created, testModel.getLifecycleState());
        Mockito.verify(testModel).create(null);

    }

    @Test
    public void testStart() throws Exception {
//        Module module = Mockito.mock(Module.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(baseActivity.getRunState()).thenReturn(RunState.Created);
        TestModel testModel = new TestModel();
        testModel.setViewModelScope(baseActivity);
//        testModel.setModule(module);
        testModel = Mockito.spy(testModel);
        testModel.start();
        assertEquals(LifecycleState.Started, testModel.getLifecycleState());
        Mockito.verify(testModel).start();

    }

    @Test
    public void testResume() throws Exception {

        TestModel testModel = new TestModel();
        testModel.setViewModelScope(baseActivity);
        testModel = Mockito.spy(testModel);
        testModel.resume();
        assertEquals(LifecycleState.Resumed, testModel.getLifecycleState());
        Mockito.verify(testModel).resume();

    }

    @Test
    public void testPause() throws Exception {
        TestModel testModel = new TestModel();
        testModel.setViewModelScope(baseActivity);
        testModel = Mockito.spy(testModel);
        testModel.pause();
        assertEquals(LifecycleState.Paused, testModel.getLifecycleState());
        Mockito.verify(testModel).pause();

    }

    @Test
    public void testStop() throws Exception {
        TestModel testModel = new TestModel();
        testModel.setViewModelScope(baseActivity);
        testModel = Mockito.spy(testModel);
        ReflectUtil.invoke(testModel, "init");
        testModel.stop();
        assertEquals(LifecycleState.Stopped, testModel.getLifecycleState());
        Mockito.verify(testModel).stop();

    }
}
