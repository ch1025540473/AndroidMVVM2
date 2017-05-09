package com.mx.demo.viewmodel;

import com.mx.demo.BuildConfig;
import com.mx.demo.model.DemoUseCase;
import com.mx.demo.model.bean.ApiBean;
import com.mx.engine.utils.SubscriberResult;
import com.mx.framework2.viewmodel.command.OnLoadMoreCommand;
import com.mx.framework2.viewmodel.proxy.PTRRecyclerViewProxy;
import com.mx.gunit.BaseTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
@PrepareForTest({MainViewModel.class, DemoUseCase.class})
public class MainViewModelTest extends BaseTest {
    private MainViewModel vm;

    @Mock
    private PTRRecyclerViewProxy proxy;
    @Mock
    private DemoUseCase useCase;
    @Captor
    private ArgumentCaptor<OnLoadMoreCommand> loadMoreCommandArgumentCaptor;
    @Captor
    private ArgumentCaptor<SubscriberResult<List<ApiBean>>> subscriberResultArgumentCaptor;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        PowerMockito.whenNew(PTRRecyclerViewProxy.class).withNoArguments().thenReturn(proxy);
    }

    @Test
    public void testConstructor() throws Exception {
        vm = PowerMockito.spy(new MainViewModel());

        mockMethod(MainViewModel.class, "obtainUseCase").thenReturn(useCase).done();
        Mockito.verify(proxy).setOnLoadMoreCommand(loadMoreCommandArgumentCaptor.capture());
        loadMoreCommandArgumentCaptor.getValue().onLoadMore();
        Mockito.verify(useCase).loadMoreApiBeanFromNetwork(subscriberResultArgumentCaptor.capture());
        subscriberResultArgumentCaptor.getValue().onFailure(null);
    }
}