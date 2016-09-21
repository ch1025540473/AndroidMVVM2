package com.mx.framework2.view.adapter;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.view.View;

import com.mx.framework2.viewmodel.command.OnClickCommand;

/**
 * Created by liuyuxuan on 16/8/23.
 */
public class ViewDataBindingAdapters {

    //    @BindingAdapter({"onClickCommand"})
//    public static void clickCommandAdapter(View view, final OnClickCommand onClickCommand) {
//        Log.d("PTR", "clickCommandAdapter" + onClickCommand.getClass());
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (onClickCommand != null) {
//                    onClickCommand.execute(v.getUseCaseHolderId());
//                }
//            }
//        });
//    }
    @BindingConversion
    public static View.OnClickListener click(final OnClickCommand onClickCommand) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickCommand != null) {
                    onClickCommand.execute(view.getId());
                }
            }
        };
    }

    @BindingConversion
    public static String classConversion(Class className) {
        return className.getName();
    }


    @BindingAdapter("showOrHide")
    public static void showOrHide(View view, boolean showOrHide) {
        view.setVisibility(showOrHide ? View.VISIBLE : View.GONE);
    }
}
