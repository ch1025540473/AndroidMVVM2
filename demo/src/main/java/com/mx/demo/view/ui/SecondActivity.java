package com.mx.demo.view.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mx.demo.R;

public class SecondActivity extends AppCompatActivity {
    public static final int RESULT_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setResult(RESULT_CODE);
        System.out.println("Set result_code=" + RESULT_CODE);
    }
}
