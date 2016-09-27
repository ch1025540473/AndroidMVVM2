package com.mx.demo.view.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mx.demo.R;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setResult(RESULT_OK);
        System.out.println("Set result_code=" + RESULT_OK);
    }
}
