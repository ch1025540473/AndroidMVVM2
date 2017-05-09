package com.mx.demo.viewmodel;

import com.mx.demo.BuildConfig;
import com.mx.demo.model.DemoUseCase;
import com.mx.demo.model.bean.ApiBean;
import com.mx.engine.utils.SubscriberResult;
import com.mx.framework2.viewmodel.command.OnLoadMoreCommand;
import com.mx.framework2.viewmodel.proxy.PTRRecyclerViewProxy;
import com.mx.gunit.BaseTest;
import com.mx.gunit.InvocationListener;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Method;
import java.util.List;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, manifest = "src/main/AndroidManifest.xml")
public class MainViewModelTest extends BaseTest {
    private MainViewModel vm;

    //    @Mock
//    private PTRRecyclerViewProxy proxy;
    @Mock
    private DemoUseCase useCase;
    @Captor
    private ArgumentCaptor<OnLoadMoreCommand> loadMoreCommandArgumentCaptor;
    @Captor
    private ArgumentCaptor<SubscriberResult<List<ApiBean>>> subscriberResultArgumentCaptor;

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testConstructor() throws Exception {

        mockMethod(MainViewModel.class, "obtainUseCase").thenReturn(useCase).withInvocationListener(new InvocationListener() {
            @Override
            public void onInvoke(Object obj, Method method, Object[] args) {
                System.out.println(args[0].getClass().getName());
            }
        }).done();

        mockMethod(PTRRecyclerViewProxy.class, "setOnLoadMoreCommand").withInvocationListener(new InvocationListener() {
            @Override
            public void onInvoke(Object obj, Method method, Object[] args) {
                OnLoadMoreCommand cmd = OnLoadMoreCommand.class.cast(args[0]);
                cmd.onLoadMore();
            }
        }).done();

        vm = PowerMockito.spy(new MainViewModel());
    }
}