package com.mx.gunit;


import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.mockito.MockitoAnnotations;
import org.powermock.core.MockRepository;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RuntimeEnvironment;

/**
 * Created by chenbaocheng on 17/5/3.
 */
//@Config(constants = BuildConfig.class) 指定当前Module的BuildConfig类
//@PrepareForTest(ClassForTest.class) 指定被测试的类
@PrepareForTest(fullyQualifiedNames = {"com.mx.*"})
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*", "javax.net.ssl.*"})
public class BaseTest extends Asserts {
    public BaseTest() {
        super();
    }

    /**
     * 该字段用于集成PowerMock和Robolectric
     * 参见文档：https://github.com/robolectric/robolectric/wiki/Using-PowerMock
     */
    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
        MethodMockManager.getInstance().clear();
        MockRepository.clear();
    }

    public MethodMockable mockMethod(Object owner, String methodName) {
        MethodMock.MethodMockBuilder builder = new MethodMock.MethodMockBuilder(methodName);
        builder.setOwner(owner);
        return builder;
    }

    public MethodMockable mockMethod(Class<?> type, String methodName) {
        MethodMock.MethodMockBuilder builder = new MethodMock.MethodMockBuilder(methodName);
        builder.setClass(type);
        return builder;
    }

    public Context getApplicationContext() {
        return RuntimeEnvironment.application.getApplicationContext();
    }
}
