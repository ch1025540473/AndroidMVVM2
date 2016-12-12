package com.mx.demo.view.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mx.demo.R;
import com.mx.framework2.view.ui.BaseActivity;
import com.mx.router.Callback;
import com.mx.router.Route;
import com.mx.router.Router;
import com.orhanobut.logger.Logger;

public class SecondActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        findViewById(R.id.start_three_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Router.getDefault().newRoute()
                        .from(SecondActivity.this)
                        .uri("demo/thirdActivity")
                        .callback(new Callback<Bundle>() {
                            @Override
                            public void onRouteSuccess(Route route, Bundle data) {
                                System.out.println("<RA> result data=" + data.getString("data"));
                            }

                            @Override
                            public void onRouteFailure(Route route) {
                            }
                        })
                        .buildAndRoute();
//                setResult(RESULT_CANCELED);
//                Intent intent = new Intent(getContext(), ThirdActivity.class);
//                startActivityForResult(intent, 100);
            }
        });

        findViewById(R.id.result_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.t("result").d(getClass().getName() + "resultCode=" + resultCode + "  resultCode="
                + resultCode + " " + (resultCode == Activity.RESULT_CANCELED));
    }

    public  void finish(){
        super.finish();
        Logger.t("result").d(getClass().getName() + "finish");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.t("result").d(getClass().getName()+"destory="+isFinishing());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
