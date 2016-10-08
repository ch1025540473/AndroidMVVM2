package com.mx.demo.view.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mx.demo.DemoModule;
import com.mx.demo.R;
import com.mx.demo.databinding.FragmentMainBinding;
import com.mx.demo.viewmodel.MainViewModel;
import com.mx.framework2.view.DataBindingFactory;
import com.mx.framework2.view.ui.BaseFragment;


public class MainFragment extends BaseFragment {

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        FragmentMainBinding mainBinding = DataBindingFactory.inflate(getContext(), R.layout.fragment_main);
        MainViewModel viewModel = DemoModule.get().getViewModelFactory().createViewModel("main_view_model", MainViewModel.class, this);

        mainBinding.setModel(viewModel);
        mainBinding.demoMylist.getRefreshableView().getItemAnimator().setAddDuration(2000);

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
}
