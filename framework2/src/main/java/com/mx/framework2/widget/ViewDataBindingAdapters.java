package com.mx.framework2.widget;

import android.databinding.BindingAdapter;
import android.util.Log;
import android.view.View;

/**
 * Created by liuyuxuan on 16/8/23.
 */
public class ViewDataBindingAdapters {

    @BindingAdapter({"clickCommand"})
    public static void clickCommandAdapter(View view, final ClickCommand clickCommand) {
        Log.d("PTR", "clickCommandAdapter"+ clickCommand.getClass());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickCommand != null) {
                    clickCommand.execute(v.getId());
                }
            }
        });
    }


}