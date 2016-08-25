package com.mx.framework.model;



import com.mx.framework.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class UseCaseManagerTest {

    public static class TestUseCase extends UseCase {

        @Override
        protected void onOpen() {

        }

        @Override
        protected void onClose() {

        }
    }


    UseCaseManager useCaseManager;

    @Before
    public void setUp() throws Exception {
        useCaseManager = new UseCaseManager(RuntimeEnvironment.application);

    }

    @Test
    public void testRegister() throws Exception {

        useCaseManager.register(TestUseCase.class);

        try{
            useCaseManager.register(TestUseCase.class);
        }catch (IllegalArgumentException e){

        }

    }

    @Test
    public void testObtainUseCase() throws Exception {
        useCaseManager.register(TestUseCase.class);
        TestUseCase useCaseTest = useCaseManager.obtainUseCase(TestUseCase.class);
        assertNotNull(useCaseTest);

    }

    @Test
    public void testCloseUseCase() throws Exception {
        useCaseManager.register(TestUseCase.class);
        TestUseCase useCaseTest = useCaseManager.obtainUseCase(TestUseCase.class);
        useCaseTest.close();


    }
}