package com.mx.demo.view.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mx.demo.DemoModule;
import com.mx.demo.R;
import com.mx.demo.databinding.FragmentMainBinding;
import com.mx.demo.view.PullToRefreshHeaderView;
import com.mx.demo.viewmodel.MainViewModel;
import com.mx.framework2.view.DataBindingFactory;
import com.mx.framework2.view.ui.BaseFragment;
import com.mx.framework2.viewmodel.proxy.DialogProxy;
import com.mx.router.Callback;
import com.mx.router.Route;
import com.mx.router.RouteMethod;
import com.mx.router.Router;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainFragment extends BaseFragment implements Callback<MainFragment.Bean> {

    public MainFragment() {
    }

    @Override
    public void onRouteSuccess(Route route, Bean data) {
        System.out.println("<R>" + data.aaa);
    }

    @Override
    public void onRouteFailure(Route route) {

    }

    public static class Bean {
        public String aaa;

    }

    public class SendBean {
        public String aaa;

        public SendBean(String aaa) {
            this.aaa = aaa;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainFragment", "savedInstanceState>>" + savedInstanceState);

        List params = new ArrayList();
        params.add(0,"a");
        params.add(1,"b");
        params.add(2,"c");
        //router可以携带参数和复杂类型的数据
        // method不设置时默认类型为get,支持简单的param携带参数
        //当需要携带复杂对象data时，需要设置method为post方式。
        Map m = (Map) Router.getDefault().newRoute()
                .from(this)
                .uri("demo/test")
                .method(RouteMethod.POST)
                .appendParameter("code", 123)
                .data(new SendBean("testData"))
                .appendParameters("key",params)
                //.callback(this)
                .buildAndRoute();
        System.out.println("<RA> m.aaa=" + m.get("aaa"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
          Log.d("TestPTR","1111111111111");
        FragmentMainBinding mainBinding = DataBindingFactory.inflate(getContext(), R.layout.fragment_main);
        Log.d("TestPTR","444444444");
//
//        mainBinding.demoMylist.setSecondFooterLayout(new PullToRefreshFooterView(getContext()));
        MainViewModel viewModel = DemoModule.get().getViewModelFactory().createViewModel("main_view_model", MainViewModel.class, this);

        //test dialogproxy
        DialogProxy dialogProxy = new DialogProxy(getFragmentManager(), new GomeDialogFragment(), "tag");
        viewModel.setDialogProxy(dialogProxy);
        getViewModelManager().addViewModel(viewModel);
        mainBinding.setModel(viewModel);
        mainBinding.demoMylist.getRefreshableView().getItemAnimator().setAddDuration(2000);
        mainBinding.demoMylist.setHeaderClassName(PullToRefreshHeaderView.className());
        return mainBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("onResume");
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Router.getDefault().saveState(this, outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Router.getDefault().restoreState(this, savedInstanceState);
    }
}
