package com.mx.framework2.viewmodel;

import android.content.Intent;
import android.util.SparseArray;

import com.mx.engine.event.BroadcastEvent;
import com.mx.engine.event.EventProxy;
import com.mx.engine.event.NetworkBroadcastEvent;
import com.mx.framework2.DataSourceChangeAware;
import com.mx.framework2.Module;
import com.mx.framework2.model.UseCase;
import com.mx.framework2.view.BaseActivity;
import com.mx.framework2.view.RunState;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by liuyuxuan on 16/5/26.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = com.mx.engine.BuildConfig.class)
public class ViewModelTest {

    public static class TestModel extends ViewModel {


        @Override
        protected void onLoadData() {

        }
    }

    public static class TestUseCase extends UseCase {

        public TestUseCase() {
        }

        @Override
        protected void onOpen() {

        }

        @Override
        protected void onClose() {

        }
    }


    TestUseCase testUseCase;

    BaseActivity baseActivity;

    @Before
    public void setUp() throws Exception {
        baseActivity = Mockito.mock(BaseActivity.class);
        testUseCase = new TestUseCase();

    }

    @Test
    public void testObtainUseCase() throws Exception {
        Module module = Mockito.mock(Module.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(module.getUserCaseManager().obtainUseCase(TestUseCase.class)).thenReturn(testUseCase);

        TestModel testModel = new TestModel();
        testModel.setModule(module);
        testModel.setActivity(baseActivity);
        TestUseCase tmp = testModel.obtainUseCase(TestUseCase.class);
        Mockito.verify(module.getUserCaseManager()).obtainUseCase(TestUseCase.class);
        assertEquals(tmp, testUseCase);
        Mockito.reset(module);

        tmp = testModel.obtainUseCase(TestUseCase.class);
        assertEquals(tmp, testUseCase);

        Field field = ViewModel.class.getDeclaredField("useCases");
        field.setAccessible(true);

        Map<Class, UseCase> useCases = (Map<Class, UseCase>) field.get(testModel);
        assertNotNull(useCases);
        assertEquals(tmp, useCases.get(TestUseCase.class));

        Mockito.when(baseActivity.getRunState()).thenReturn(RunState.Created);
        testModel = new TestModel();
        testModel.setModule(module);
        testModel.setActivity(baseActivity);
        testModel.setViewModelManager(Mockito.mock(ViewModelManager.class));

        try {
            testModel.obtainUseCase(TestUseCase.class);
        } catch (Exception e) {
            System.out.print(e.getClass() + ">>>" + e.getMessage());
            assertEquals(e.getClass(), NullPointerException.class);
        }
        useCases = (Map<Class, UseCase>) field.get(testModel);
        assertEquals(null, useCases);
    }


    @Test
    public void testGetActivity() throws Exception {
        Mockito.when(baseActivity.getRunState()).thenReturn(RunState.Created);
        TestModel testModel = new TestModel();
        testModel.setActivity(baseActivity);
        testModel.setViewModelManager(Mockito.mock(ViewModelManager.class));

        BaseActivity activity = testModel.getActivity();
        assertEquals(activity, baseActivity);
    }


    @Test
    public void testStartActivityForResult() throws Exception {

        Mockito.when(baseActivity.getRunState()).thenReturn(RunState.Created);
        TestModel testModel = new TestModel();
        testModel.setActivity(baseActivity);
        testModel.setViewModelManager(Mockito.mock(ViewModelManager.class));
        Field field = BaseActivity.class.getDeclaredField("activityResultListeners");
        field.setAccessible(true);
        field.set(baseActivity, new SparseArray<BaseActivity.ActivityResultListener>());
        testModel.startActivityForResult(Mockito.mock(Intent.class), 22);
        Mockito.verify(baseActivity).startActivityForResult(ArgumentCaptor.forClass(Intent.class).capture(),
                ArgumentCaptor.forClass(Integer.class).capture());
    }


    @Test
    public void testGetViewModel() throws Exception {
        Mockito.reset(baseActivity);
        ViewModelManager viewModelManager = new ViewModelManager(null);
        Mockito.when(baseActivity.getRunState()).thenReturn(RunState.Created);
        TestModel testModel = new TestModel();
        testModel.setActivity(baseActivity);
        testModel.setViewModelManager(viewModelManager);
        viewModelManager.addViewModel(testModel);
        TestModel tmp = testModel.getViewModel(TestModel.class);
        assertEquals(testModel, tmp);

    }

    @Test
    public void testPostEvent() throws Exception {

        ViewModelManager viewModelManager = new ViewModelManager(null);
        Mockito.when(baseActivity.getRunState()).thenReturn(RunState.Created);
        TestModel testModel = new TestModel();
        testModel.setActivity(baseActivity);
        testModel.setViewModelManager(viewModelManager);

        Field eventProxyField = ViewModel.class.getDeclaredField("eventProxy");
        eventProxyField.setAccessible(true);
        EventProxy eventProxy = (EventProxy) eventProxyField.get(testModel);
        assertNotNull(eventProxy);

        eventProxy = Mockito.spy(eventProxy);
        eventProxyField.set(testModel, eventProxy);

        testModel.postEvent(Mockito.mock(BroadcastEvent.class));

        Mockito.verify(eventProxy).post(ArgumentCaptor.forClass(NetworkBroadcastEvent.class).capture());
    }


    @Test
    public void testRequestDataReloading() throws Exception {
        baseActivity = new BaseActivity();
        ViewModelManager viewModelManager = new ViewModelManager(null);
        TestModel testModel = new TestModel();
        testModel.setActivity(baseActivity);
        testModel.setViewModelManager(viewModelManager);
        testModel = Mockito.spy(testModel);
        Field field = BaseActivity.class.getDeclaredField("viewModelManager");
        field.setAccessible(true);
        field.set(baseActivity, viewModelManager);
        viewModelManager.addViewModel(testModel);
        testModel.requestDataReloading();
        Mockito.verify(testModel).onReloadData(ArgumentCaptor.forClass(DataSourceChangeAware.class).capture());

    }

    @Test
    public void testOnSaveInstanceState() throws Exception {
        ViewModelManager viewModelManager = new ViewModelManager(null);
        Mockito.when(baseActivity.getRunState()).thenReturn(RunState.Created);

        TestModel testModel = new TestModel();
        testModel.setActivity(baseActivity);
        testModel.setViewModelManager(viewModelManager);
        testModel = Mockito.spy(testModel);
        viewModelManager.addViewModel(testModel);
        viewModelManager.saveInstanceState(null);
        Mockito.verify(testModel).onSaveInstanceState(null);

    }

    @Test
    public void testOnRestoreInstanceState() throws Exception {
        ViewModelManager viewModelManager = new ViewModelManager(null);
        Mockito.when(baseActivity.getRunState()).thenReturn(RunState.Created);
        TestModel testModel = new TestModel();
        testModel.setActivity(baseActivity);
        testModel.setViewModelManager(viewModelManager);
        testModel = Mockito.spy(testModel);
        viewModelManager.addViewModel(testModel);
        viewModelManager.restoreInstanceState(null);
        Mockito.verify(testModel).onRestoreInstanceState(null);

    }

    @Test
    public void testStart() throws Exception {

        Module module = Mockito.mock(Module.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(baseActivity.getRunState()).thenReturn(RunState.Created);
        TestModel testModel = new TestModel();
        testModel.setActivity(baseActivity);
        testModel.setModule(module);
        testModel = Mockito.spy(testModel);
        testModel.start();
        assertEquals(RunState.Started, testModel.getRunState());
        Mockito.verify(testModel).start();


    }


    @Test
    public void testResume() throws Exception {

        Module module = Mockito.mock(Module.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(baseActivity.getRunState()).thenReturn(RunState.Created);
        TestModel testModel = new TestModel();
        testModel.setActivity(baseActivity);
        testModel.setModule(module);
        testModel = Mockito.spy(testModel);
        testModel.resume();
        assertEquals(RunState.Resumed, testModel.getRunState());
        Mockito.verify(testModel, Mockito.atLeastOnce()).resume();

    }


    @Test
    public void testPause() throws Exception {
        Module module = Mockito.mock(Module.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(baseActivity.getRunState()).thenReturn(RunState.Created);
        TestModel testModel = new TestModel();
        testModel.setActivity(baseActivity);
        testModel.setModule(module);
        testModel = Mockito.spy(testModel);
        testModel.pause();
        assertEquals(RunState.Paused, testModel.getRunState());
        Mockito.verify(testModel).onPause();
    }


    @Test
    public void testStop() throws Exception {
        Mockito.reset(baseActivity);
        Module module = Mockito.mock(Module.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(baseActivity.getRunState()).thenReturn(RunState.Created);
        TestModel testModel = new TestModel();
        testModel.setActivity(baseActivity);
        testModel.setModule(module);
        testModel = Mockito.spy(testModel);
        testModel.stop();
        assertEquals(RunState.Stoped, testModel.getRunState());
        Mockito.verify(testModel).onStop();
    }

}