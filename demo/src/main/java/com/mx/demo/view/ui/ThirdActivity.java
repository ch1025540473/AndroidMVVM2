package com.mx.demo.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mx.demo.R;
import com.mx.framework2.view.ui.BaseActivity;
import com.orhanobut.logger.Logger;

/**
 * Created by liuyuxuan on 2016/10/31.
 */

public class ThirdActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);
        findViewById(R.id.result_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("data", "received");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        findViewById(R.id.start_main_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.t("result").d(getClass().getName()+"destory="+isFinishing());
    }
}
