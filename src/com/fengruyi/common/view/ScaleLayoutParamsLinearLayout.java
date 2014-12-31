package com.fengruyi.common.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.fengruyi.common.util.LayoutParamsManager;
import com.fengruyi.fandroidcommon.R;


/**
 * LinearLayout自动缩放控件
 * 
 * @author fengruyi
 *
 */
public class ScaleLayoutParamsLinearLayout extends LinearLayout {
    private static final int SCALE_STYLE_NORMAL = 0;
    private static final int SCALE_STYLE_BY_WIDTH = 1;
    private static final int SCALE_STYLE_BY_HEIGHT = 2;

    private int mScaleStyle = SCALE_STYLE_NORMAL;//默认按宽高缩放

    public ScaleLayoutParamsLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        readScaleConfig(context, attrs);
    }

    public ScaleLayoutParamsLinearLayout(Context context) {
        super(context);
    }

    private void readScaleConfig(Context context, AttributeSet attrs) {
        final TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.LayoutParamsConfig);
        mScaleStyle = ta.getInt(R.styleable.LayoutParamsConfig_scale_style,
                SCALE_STYLE_NORMAL);
        ta.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        scaleLayoutParamsForChildView();
    }

    private void scaleLayoutParamsForChildView() {
        if (mScaleStyle == SCALE_STYLE_BY_WIDTH) {
            LayoutParamsManager.scaleViewGroupEnlargeByWidthPixels(this);
        } else if (mScaleStyle == SCALE_STYLE_BY_HEIGHT) {
            LayoutParamsManager.scaleViewGroupEnlargeByHeightPixels(this);
        } else {
            LayoutParamsManager.scaleViewGroupByRatio(this);
        }
    }
}
