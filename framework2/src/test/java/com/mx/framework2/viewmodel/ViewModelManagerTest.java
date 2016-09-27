package com.mx.framework2.viewmodel;

import android.os.Bundle;

import com.mx.framework2.BuildConfig;
import com.mx.framework2.MyRunner;
import com.mx.framework2.ReflectUtil;
import com.mx.framework2.view.ui.BaseActivity;

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

/**
 * Created by wwish on 16/8/29.
 */
@RunWith(MyRunner.class)
@Config(constants = BuildConfig.class)
public class ViewModelManagerTest {

    public static class TestLifecycleViewModel extends LifecycleViewModel {

    }

    TestLifecycleViewModel testLifecycleViewModel;

    BaseActivity baseActivity = Mockito.mock(BaseActivity.class);

    ViewModelManager viewModelManager;
    Bundle bundle=new Bundle();
    @Before
    public void setUp() throws Exception {
        testLifecycleViewModel = new TestLifecycleViewModel();
        testLifecycleViewModel.setContext(baseActivity);

        viewModelManager = new ViewModelManager(bundle);
    }

    @After
    public void tearDown() throws Exception {


    }

    @Test
    public void testAddViewModel() throws Exception {
        Field field = ViewModelManager.class.getDeclaredField("lifecycleViewModelList");
        field.setAccessible(true);
        List<LifecycleViewModel> list = (List<LifecycleViewModel>) field.get(viewModelManager);
        assertNotNull(list);

        list = Mockito.spy(list);
        field.set(viewModelManager, list);

        TestLifecycleViewModel testLifecycleViewModelSpy = new TestLifecycleViewModel();

        viewModelManager.addViewModel(testLifecycleViewModelSpy);
        Mockito.verify(list).add(testLifecycleViewModelSpy);
    }

    @Test
    public void testRemoveViewModel() throws Exception {
        Field field = ViewModelManager.class.getDeclaredField("lifecycleViewModelList");
        field.setAccessible(true);
        List<LifecycleViewModel> list = (List<LifecycleViewModel>) field.get(viewModelManager);
        assertNotNull(list);

        list = Mockito.spy(list);
        field.set(viewModelManager, list);

        TestLifecycleViewModel testLifecycleViewModelSpy = new TestLifecycleViewModel();

        viewModelManager.addViewModel(testLifecycleViewModelSpy);
        viewModelManager.removeViewModel(testLifecycleViewModelSpy);
        Mockito.verify(list).remove(testLifecycleViewModelSpy);
    }

    @Test
    public void testSetUserVisibleHint() throws Exception {
        TestLifecycleViewModel testLifecycleViewModelSpy = Mockito.spy(testLifecycleViewModel);
        ReflectUtil.invoke(testLifecycleViewModelSpy, "init");
        viewModelManager.addViewModel(testLifecycleViewModelSpy);
        ArgumentCaptor<Boolean> argument = ArgumentCaptor.forClass(Boolean.class);
        viewModelManager.setUserVisibleHint(false);
        Mockito.verify(testLifecycleViewModelSpy).setUserVisibleHint(argument.capture());
        assertEquals(false, argument.getValue());
    }

    @Test
    public void testOnWindowFocusChanged() throws Exception {
        TestLifecycleViewModel testLifecycleViewModelSpy = Mockito.spy(testLifecycleViewModel);
        ReflectUtil.invoke(testLifecycleViewModelSpy, "init");
        viewModelManager.addViewModel(testLifecycleViewModelSpy);
        ArgumentCaptor<Boolean> argument = ArgumentCaptor.forClass(Boolean.class);
        viewModelManager.onWindowFocusChanged(false);
        Mockito.verify(testLifecycleViewModelSpy).onWindowFocusChanged(argument.capture());
        assertEquals(false, argument.getValue());
    }

    @Test
    public void testCreate() throws Exception {
        TestLifecycleViewModel testLifecycleViewModelSpy = Mockito.spy(testLifecycleViewModel);
        ReflectUtil.invoke(testLifecycleViewModelSpy, "init");
        viewModelManager.addViewModel(testLifecycleViewModelSpy);
        viewModelManager.create();
        Mockito.verify(testLifecycleViewModelSpy).create(bundle);
    }

    @Test
    public void testStart() throws Exception {
        TestLifecycleViewModel testLifecycleViewModelSpy = Mockito.spy(testLifecycleViewModel);
        ReflectUtil.invoke(testLifecycleViewModelSpy, "init");
        viewModelManager.addViewModel(testLifecycleViewModelSpy);
        viewModelManager.start();
        Mockito.verify(testLifecycleViewModelSpy).start();
    }

    @Test
    public void testResume() throws Exception {
        TestLifecycleViewModel testLifecycleViewModelSpy = Mockito.spy(testLifecycleViewModel);
        ReflectUtil.invoke(testLifecycleViewModelSpy, "init");
        viewModelManager.addViewModel(testLifecycleViewModelSpy);
        viewModelManager.resume();
        Mockito.verify(testLifecycleViewModelSpy).resume();

    }

    @Test
    public void testPause() throws Exception {
        TestLifecycleViewModel testLifecycleViewModelSpy = Mockito.spy(testLifecycleViewModel);
        ReflectUtil.invoke(testLifecycleViewModelSpy, "init");
        viewModelManager.addViewModel(testLifecycleViewModelSpy);
        viewModelManager.pause();
        Mockito.verify(testLifecycleViewModelSpy).pause();

    }

    @Test
    public void testStop() throws Exception {
        TestLifecycleViewModel testLifecycleViewModelSpy = Mockito.spy(testLifecycleViewModel);
        ReflectUtil.invoke(testLifecycleViewModelSpy, "init");
        viewModelManager.addViewModel(testLifecycleViewModelSpy);
        viewModelManager.stop();
        Mockito.verify(testLifecycleViewModelSpy).stop();

    }


    @Test
    public void testSaveInstanceState() throws Exception {
        Bundle bundle = new Bundle();
        TestLifecycleViewModel testLifecycleViewModelSpy = Mockito.spy(testLifecycleViewModel);
        viewModelManager.addViewModel(testLifecycleViewModelSpy);
        viewModelManager.saveInstanceState(bundle);
        Mockito.verify(testLifecycleViewModelSpy).onSaveInstanceState(bundle);
    }

    @Test
    public void testRestoreInstanceState() throws Exception {

        Bundle bundle = new Bundle();
        TestLifecycleViewModel testLifecycleViewModelSpy = Mockito.spy(testLifecycleViewModel);
        viewModelManager.addViewModel(testLifecycleViewModelSpy);
        viewModelManager.restoreInstanceState(bundle);
        Mockito.verify(testLifecycleViewModelSpy).onRestoreInstanceState(bundle);
    }
}
