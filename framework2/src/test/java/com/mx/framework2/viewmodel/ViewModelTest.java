package com.mx.framework2.viewmodel;

import com.mx.engine.event.BroadcastEvent;
import com.mx.engine.event.EventProxy;
import com.mx.engine.event.NetworkBroadcastEvent;
import com.mx.framework2.BuildConfig;
import com.mx.framework2.MyRunner;
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

import static org.junit.Assert.assertNotNull;

/**
 * Created by wwish on 16/8/29.
 */
@RunWith(MyRunner.class)
@Config(constants = BuildConfig.class)
public class ViewModelTest {

    class TestModel extends ViewModel{

    }
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testPostEvent() throws Exception {


        TestModel testModel = new TestModel();

        testModel.postEvent(Mockito.mock(BroadcastEvent.class));

    }
}
