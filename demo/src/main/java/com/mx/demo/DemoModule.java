package com.mx.demo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.mx.activitystarter.ActivityResultCallback;
import com.mx.activitystarter.ActivityStarter;
import com.mx.demo.event.GotoPatchEvent;
import com.mx.demo.event.GotoWebEvent;
import com.mx.demo.model.DemoUseCase;
import com.mx.demo.view.ui.HotfixTestActivity;
import com.mx.demo.view.ui.SecondActivity;
import com.mx.demo.view.ui.ThirdActivity;
import com.mx.demo.view.ui.WebActivity;
import com.mx.framework2.Module;
import com.mx.framework2.model.UseCaseManager;
import com.mx.framework2.view.ui.BaseActivity;
import com.mx.router.Pipe;
import com.mx.router.RouteRule;
import com.mx.router.RouteSubscribe;
import com.mx.router.Router;
import com.mx.router.rule.ActivityRouteRule;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenbaocheng on 16/8/14.
 */
public class DemoModule extends Module {
    private static volatile DemoModule instance = null;

    public static DemoModule get() {
        if (instance != null) {
            return instance;
        }

        synchronized (DemoModule.class) {
            if (instance == null) {
                instance = new DemoModule();
                Router.getDefault().registerReceiver(instance);
            }
        }

        return instance;
    }


    public static class BeanReceive {
        public String aaa;
    }

    @Override
    protected void onStart(UseCaseManager userCaseManager) {
        Router.getDefault().registerReceiver(instance);
        userCaseManager.register(DemoUseCase.class);

        Router.getDefault().registerRule("demo/test", new RouteRule() {
            @Override
            public void handleRoute(Pipe pipe) {
                System.out.println("<R> method=" + pipe.getMethod());
                BeanReceive beanReceive =  (BeanReceive) pipe.getData(BeanReceive.class);
                System.out.println("<R>   pipe.getData()=" +  beanReceive.aaa);

                List<String> params=  pipe.getParameters("key");
                System.out.println("<R>   pipe.getParameters=" +  params+",size="+ params.size());
                for(String temp:params){
                    System.out.println("<R>  get param=" + temp);
                }
                Map<String, String> m = new HashMap<>();
                m.put("aaa", "bbb");
                pipe.success(m);
            }
        });

        Router.getDefault().registerRule("demo/routeView", new RouteRule() {
            @Override
            public void handleRoute(Pipe pipe) {
                ImageView v = new ImageView(pipe.getContext());
                v.setImageResource(R.drawable.comm_titlebar_msg_white);
                pipe.success(v);
            }
        });

        Router.getDefault().registerRule("demo/thirdActivity", new ActivityRouteRule(ThirdActivity.class));
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public final void receiveEvent(GotoAnotherEvent gotoSecondActEvent) {
//        ActivityStarter activityStarter = gotoSecondActEvent.getActivityStarter();
//        Intent intent = new Intent(activityStarter.getContext(), SecondActivity.class);
//        activityStarter.startActivityForResult(intent, new ActivityResultCallback() {
//            @Override
//            public void onActivityResult(int resultCode, Intent data) {
//                Log.d("DemoModule", "onActivityResult>>" + resultCode);
//            }
//        });
//    }

    @RouteSubscribe(uri = "demo/anotherActivity")
    public final void receiveEvent(Bundle data) {
        ActivityStarter activityStarter = BaseActivity.getTopActivityStarter();
        Intent intent = new Intent(activityStarter.getContext(), SecondActivity.class);
        activityStarter.startActivityForResult(intent, new ActivityResultCallback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                Log.d("DemoModule", "onActivityResult>>" + resultCode);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public final void receiveEvent(GotoPatchEvent gotoPatchActEvent) {
        ActivityStarter activityStarter = BaseActivity.getTopActivityStarter();
        Intent intent = new Intent(activityStarter.getContext(), HotfixTestActivity.class);
        activityStarter.startActivityForResult(intent, new ActivityResultCallback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                Log.d("DemoModule", "onActivityResult>>" + resultCode);
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public final void receiveEvent(GotoWebEvent gotoWebEvent) {
        ActivityStarter activityStarter = gotoWebEvent.getActivityStarter();
        Intent intent = new Intent(activityStarter.getContext(), WebActivity.class);
        activityStarter.startActivity(intent);
    }
}
