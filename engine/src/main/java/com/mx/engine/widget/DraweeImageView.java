package com.mx.engine.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by liuyuxuan on 2015/12/18.
 */
public class DraweeImageView extends SimpleDraweeView {


    public DraweeImageView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public DraweeImageView(Context context) {
        super(context);
    }

    public DraweeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DraweeImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DraweeImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }





}
