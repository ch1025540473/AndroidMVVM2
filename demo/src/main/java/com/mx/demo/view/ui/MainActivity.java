package com.mx.demo.view.ui;

import android.content.Intent;
import android.os.Bundle;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.memory.PooledByteBuffer;
import com.mx.demo.R;
import com.mx.framework2.view.DataBindingFactory;
import com.mx.framework2.view.ui.BaseActivity;
import com.orhanobut.logger.Logger;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logger.t("result").d(getClass().getName() + "mode=" + getUseCaseHolderId());
        DataSource<CloseableReference<PooledByteBuffer>> dataSource =null;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.t("result").d(getClass().getName() + "resultCode=" + resultCode);
    }

}
