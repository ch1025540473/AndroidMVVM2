package com.mx.gunit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by chenbaocheng on 17/5/3.
 */
//@Config(constants = BuildConfig.class) 指定当前Module的BuildConfig类
//@PrepareForTest(ClassForTest.class) 指定被测试的类
@RunWith(RobolectricTestRunner.class)
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

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {

    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void dummyTest() {
    }
}
