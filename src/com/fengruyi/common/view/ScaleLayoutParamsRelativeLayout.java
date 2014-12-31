package com.fengruyi.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.fengruyi.common.util.LayoutParamsManager;
import com.fengruyi.fandroidcommon.R;



/**
 * RelativeLayout自动缩放控件
 * 
 * @author fengruyi
 *
 */
public class ScaleLayoutParamsRelativeLayout extends RelativeLayout {
    private static final int SCALE_STYLE_NORMAL = 0;
    private static final int SCALE_STYLE_BY_WIDTH = 1;
    private static final int SCALE_STYLE_BY_HEIGHT = 2;

    private int mScaleStyle = SCALE_STYLE_NORMAL;//默认按宽高缩放

    public ScaleLayoutParamsRelativeLayout(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle); 
    }

    public ScaleLayoutParamsRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScaleLayoutParamsRelativeLayout(Context context) {
        super(context);
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
