package com.mx.demo.view.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mx.demo.R;
import com.mx.framework2.view.ui.BaseDialogFragment;
import com.mx.framework2.view.ui.BaseFragment;

/**
 * Created by zhulianggang on 16/9/21.
 */

public class TestDialogFragment extends BaseFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.button_lay, null);
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseDialogFragment fragment = getMyDialogFragment();
            }
        });
        return view;
    }


    public BaseDialogFragment getMyDialogFragment() {
        GomeDialogFragment fragment = new GomeDialogFragment();
        fragment.setOnBtnClickListener(new GomeDialogFragment.OnBtnClickListener() {
            @Override
            public void onOkClick() {
            }

            @Override
            public void onCancelClick() {
            }
        });
        return fragment;
    }
}
