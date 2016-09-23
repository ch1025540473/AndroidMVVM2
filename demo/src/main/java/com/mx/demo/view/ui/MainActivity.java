package com.mx.demo.view.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.mx.demo.DemoModule;
import com.mx.demo.R;
import com.mx.demo.databinding.ActivityMainBinding;
import com.mx.demo.viewmodel.MainViewModel;
import com.mx.framework2.view.ui.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        MainViewModel viewModel = DemoModule.get().getViewModelFactory().createViewModel(MainViewModel.class, this);
        mainBinding.setModel(viewModel);
        mainBinding.demoMylist.getRefreshableView().getItemAnimator().setAddDuration(2000);

   /*     setContentView(R.layout.fragment_content_dialog);
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_content,new TestDialogFragment());
        ft.commit();*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy");
    }
}
