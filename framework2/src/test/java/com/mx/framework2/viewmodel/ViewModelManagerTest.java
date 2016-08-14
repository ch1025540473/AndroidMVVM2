package com.mx.framework2.viewmodel;

import android.os.Bundle;

import com.mx.framework2.view.BaseActivity;
import com.mx.framework2.view.RunState;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Created by liuyuxuan on 16/5/25.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = com.mx.engine.BuildConfig.class)
public class ViewModelManagerTest {

    public static class TestViewModel extends ViewModel {


        @Override
        protected void onLoadData() {

        }
    }
    @Mock
    BaseActivity baseActivity;

    TestViewModel testViewModel;

    ViewModelManager viewModelManager;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
        viewModelManager = new ViewModelManager(null);
        Mockito.when(baseActivity.getRunState()).thenReturn(RunState.Created);
        testViewModel =new TestViewModel();
        testViewModel.setContext(baseActivity);
        testViewModel.setViewModelManager(viewModelManager);
        testViewModel.setActivity(baseActivity);
        viewModelManager.addViewModel(testViewModel);
    }


    @Test
    public void testGetViewModel() throws Exception {
        viewModelManager.addViewModel(testViewModel);
        assertEquals(testViewModel, viewModelManager.getViewModel(TestViewModel.class));
    }

    @Test
    public void testAddViewModel() throws Exception {
        viewModelManager.addViewModel(testViewModel);
    }

    @Test
    public void testOnWindowFocusChanged_false() throws Exception {
        TestViewModel testViewModel = Mockito.mock(TestViewModel.class);
        Mockito.when(testViewModel.getRunState()).thenReturn(RunState.Created);
        viewModelManager.addViewModel(testViewModel);
        ArgumentCaptor<Boolean> argument = ArgumentCaptor.forClass(Boolean.class);
        viewModelManager.onWindowFocusChanged(false);
        Mockito.verify(testViewModel).onWindowFocusChanged(argument.capture());
        assertEquals(false, argument.getValue());
    }

    @Test
    public void testOnWindowFocusChanged_true() throws Exception {
        TestViewModel testViewModel = Mockito.mock(TestViewModel.class);
        Mockito.when(testViewModel.getRunState()).thenReturn(RunState.Created);
        viewModelManager.addViewModel(testViewModel);
        ArgumentCaptor<Boolean> argument = ArgumentCaptor.forClass(Boolean.class);
        viewModelManager.onWindowFocusChanged(true);
        Mockito.verify(testViewModel).onWindowFocusChanged(argument.capture());
        assertEquals(true, argument.getValue());
    }

    @Test
    public void testStart() throws Exception {
        viewModelManager.start();
        RunState runState = testViewModel.getRunState();
        assertEquals(RunState.Started, runState);
    }

    @Test
    public void testResume() throws Exception {
        viewModelManager.resume();
        RunState runState = testViewModel.getRunState();
        assertEquals(RunState.Resumed, runState);
    }

    @Test
    public void testPause() throws Exception {
        viewModelManager.pause();
        RunState runState = testViewModel.getRunState();
        assertEquals(RunState.Paused, runState);
    }

    @Test
    public void testStop() throws Exception {
        viewModelManager.stop();
        RunState runState = testViewModel.getRunState();
        assertEquals(RunState.Stoped, runState);
    }
    @Test
    public void testSaveInstanceState() throws Exception {
        Bundle bundle= new Bundle();
       TestViewModel testViewModelSpy=Mockito.spy(testViewModel) ;
        viewModelManager.addViewModel(testViewModelSpy);
        viewModelManager.saveInstanceState(bundle);
        Mockito.verify(testViewModelSpy).onSaveInstanceState(bundle);
    }

    @Test
    public void testGetAllModel() throws Exception {
      Collection<ViewModel> vms= viewModelManager.getAllModel();
        assertEquals(vms.iterator().next(),testViewModel);
    }
    @Test
    public void testRestoreInstanceState() throws Exception {
        Bundle bundle= new Bundle();
        TestViewModel testViewModelSpy=Mockito.spy(testViewModel) ;
        viewModelManager.addViewModel(testViewModelSpy);
        viewModelManager.restoreInstanceState(bundle);
        Mockito.verify(testViewModelSpy).onRestoreInstanceState(bundle);

    }
}