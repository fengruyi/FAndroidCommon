package com.fengruyi.common.util;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;

import com.fengruyi.common.app.BaseApplication;

/**
 * 控件缩放管理，根据给定像素值实现宽高比例与设计图相适应
 * @author fengruyi
 *
 */
public class LayoutParamsManager {
	/**这里是设计图的宽，不是手机的宽，具体值要根据给的图来测量*/
	public static final float STANDARD_WIDTH = 720f;
	/**这里是设计图的高，不是手机的高，包括通知栏在内，具体值要根据给的图来测量*/
    public static final float STANDARD_HEIGHT = 1280f;
    private LayoutParamsManager() {}
    
    /**
     * 根据缩放率调整容器宽高，分别设置宽高缩放率，宽和高的缩放比例可能不同
     * @param lt 
     */
    private static final void scaleWidthAndHeightByRatio(
            android.view.ViewGroup.LayoutParams lt) {

        if (lt == null)
            return;
        int widthWeight = lt.width;
        int heightWeight = lt.height;
        if (widthWeight <= 0 && heightWeight <= 0) {
            return;
        } else if (widthWeight > 0 && heightWeight <= 0) {
            if (isScaleWidthPixels()) {
                widthWeight = scaleWidthPixels(widthWeight);
            }
        } else if (widthWeight <= 0 && heightWeight > 0) {
            if (isScaleHeightPixels()) {
                heightWeight = scaleHeightPixels(heightWeight);
            }
        } else if (widthWeight > 0 && heightWeight > 0) {
            int width = scaleWidthPixels(widthWeight);
            heightWeight = Math.round(width * heightWeight / widthWeight);
            widthWeight = width;
        }

        lt.width = widthWeight;
        lt.height = heightWeight;
    }

	    private static final void scaleWidthAndHeightEach(
	            android.view.ViewGroup.LayoutParams lt) {

	        if (lt == null)
	            return;
	        int widthWeight = lt.width;
	        int heightWeight = lt.height;
	        if (widthWeight <= 0 && heightWeight <= 0) {
	            return;
	        }

	        if (widthWeight > 0 && isScaleWidthPixels()) {
	            widthWeight = scaleWidthPixels(widthWeight);

	        }

	        if (heightWeight > 0 && isScaleHeightPixels()) {
	            heightWeight = scaleHeightPixels(heightWeight);

	        }

	        lt.width = widthWeight;
	        lt.height = heightWeight;
	    }
	    /**
	     * 根据给定的宽高像素缩放容器宽高，以高为比例设为缩放率
	     * @param lt
	     */
	    private static final void scaleWidthAndHeightByHeightPixels(
	            android.view.ViewGroup.LayoutParams lt) {
	        if (lt == null) {
	            return;
	        }

	        int widthWeight = lt.width;
	        int heightWeight = lt.height;
	        if (widthWeight <= 0 && heightWeight <= 0) {
	            return;
	        }

	        if (widthWeight > 0) {
	            widthWeight = scaleHeightPixels(widthWeight);
	        }

	        if (heightWeight > 0) {
	            heightWeight = scaleHeightPixels(heightWeight);
	        }

	        lt.width = widthWeight;
	        lt.height = heightWeight;
	    }
	    /**
	     * 根据给定的宽高像素缩放容器宽高，以宽为比例设为缩放率
	     * @param lt
	     */
	    private static final void scaleWidthAndHeightByWidthPixels(
	            android.view.ViewGroup.LayoutParams lt) {
	        if (lt == null) {
	            return;
	        }

	        int widthWeight = lt.width;
	        int heightWeight = lt.height;
	        if (widthWeight <= 0 && heightWeight <= 0) {
	            return;
	        }

	        if (widthWeight > 0) {
	            widthWeight = scaleWidthPixels(widthWeight);
	        }

	        if (heightWeight > 0) {
	            heightWeight = scaleWidthPixels(heightWeight);
	        }

	        lt.width = widthWeight;
	        lt.height = heightWeight;
	    }
	    /**
	     * 设置控件的padding缩放值
	     * @param source
	     */
	    public static final void scalePaddingForView(View source) {
	        if (source != null) {
	            int left = source.getPaddingLeft();
	            int top = source.getPaddingTop();
	            int right = source.getPaddingRight();
	            int bottom = source.getPaddingBottom();
	            if (left > 0 && isScaleWidthPixels()) {
	                left = scaleWidthPixels(left);
	            }

	            if (top > 0 && isScaleHeightPixels()) {
	                top = scaleHeightPixels(top);
	            }

	            if (right > 0 && isScaleWidthPixels()) {
	                right = scaleWidthPixels(right);
	            }

	            if (bottom > 0 && isScaleHeightPixels()) {
	                bottom = scaleHeightPixels(bottom);
	            }

	            source.setPadding(left, top, right, bottom);
	        }
	    }
	    /**
	     * 设置控件的margin缩放值
	     * @param source
	     */
	    public static final void scaleMarginForView(View source) {
	        if (source == null)
	            return;

	        android.view.ViewGroup.LayoutParams lt = source.getLayoutParams();

	        if (lt != null && (lt instanceof ViewGroup.MarginLayoutParams)) {
	            scaleMargin((ViewGroup.MarginLayoutParams) lt);
	        }
	    }
	    
	    private static final void scaleMargin(MarginLayoutParams params) {
	        if (params == null)
	            return;
	        if (params.leftMargin != 0 && isScaleWidthPixels()) {
	            params.leftMargin = scaleWidthPixels(params.leftMargin);
	        }

	        if (params.topMargin != 0 && isScaleHeightPixels()) {
	            params.topMargin = scaleHeightPixels(params.topMargin);
	        }

	        if (params.rightMargin != 0 && isScaleWidthPixels()) {
	            params.rightMargin = scaleWidthPixels(params.rightMargin);
	        }

	        if (params.bottomMargin != 0 && isScaleHeightPixels()) {
	            params.bottomMargin = scaleHeightPixels(params.bottomMargin);
	        }
	    }
	    /**
	     * 设置控件的位置和大小 ，包括width,heith,padding,margin
	     * @param source
	     */
	    public static final void scaleViewByRatio(View source) {
	        if (source == null || !isScale())
	            return;
	        if(source instanceof TextView){//如果view 是textview类型的，需要缩放字体大小和Drawable
	        	scaleTextDrawableByWidth((TextView) source);
	        	scaleTextSize(((TextView) source).getTextSize());
	        }
	        android.view.ViewGroup.LayoutParams lt = source.getLayoutParams();
	        if (lt != null) {
	            scaleWidthAndHeightByRatio(lt);

	            if (lt instanceof ViewGroup.MarginLayoutParams) {
	                scaleMargin((MarginLayoutParams) lt);
	            }

	            scalePaddingForView(source);
	        }
	    }

	    public static final void scaleViewByWidthOrHeight(View source) {
	        if (source == null || !isScale())
	            return;
	        android.view.ViewGroup.LayoutParams lt = source.getLayoutParams();

	        if (lt != null) {
	            scaleWidthAndHeightEach(lt);

	            if (lt instanceof ViewGroup.MarginLayoutParams) {
	                scaleMargin((MarginLayoutParams) lt);
	            }

	            scalePaddingForView(source);
	        }
	    }

	    private static final void scaleViewByWidth(View source) {
	        if (source != null && isScaleWidthPixels()) {
	            android.view.ViewGroup.LayoutParams lt = source.getLayoutParams();
	            if (lt != null) {
	                scaleWidthAndHeightByWidthPixels(lt);
	                if (lt instanceof ViewGroup.MarginLayoutParams) {
	                    MarginLayoutParams params = (MarginLayoutParams)lt;
	                    if (params.leftMargin != 0) {
	                        params.leftMargin = scaleWidthPixels(params.leftMargin);
	                    }

	                    if (params.topMargin != 0) {
	                        params.topMargin = scaleWidthPixels(params.topMargin);
	                    }

	                    if (params.rightMargin != 0) {
	                        params.rightMargin = scaleWidthPixels(params.rightMargin);
	                    }

	                    if (params.bottomMargin != 0) {
	                        params.bottomMargin = scaleWidthPixels(params.bottomMargin);
	                    }
	                }
	            }

	            int left = source.getPaddingLeft();
	            int top = source.getPaddingTop();
	            int right = source.getPaddingRight();
	            int bottom = source.getPaddingBottom();
	            if (left > 0) {
	                left = scaleWidthPixels(left);
	            }

	            if (top > 0) {
	                top = scaleWidthPixels(top);
	            }

	            if (right > 0) {
	                right = scaleWidthPixels(right);
	            }

	            if (bottom > 0) {
	                bottom = scaleWidthPixels(bottom);
	            }

	            source.setPadding(left, top, right, bottom);
	        }
	    }

	    private static final void scaleViewByHeight(View source) {
	        if (source != null && isScaleHeightPixels()) {
	            android.view.ViewGroup.LayoutParams lt = source.getLayoutParams();
	            if (lt != null) {
	                scaleWidthAndHeightByHeightPixels(lt);
	                if (lt instanceof ViewGroup.MarginLayoutParams) {
	                    MarginLayoutParams params = (MarginLayoutParams)lt;
	                    if (params.leftMargin != 0) {
	                        params.leftMargin = scaleHeightPixels(params.leftMargin);
	                    }

	                    if (params.topMargin != 0) {
	                        params.topMargin = scaleHeightPixels(params.topMargin);
	                    }

	                    if (params.rightMargin != 0) {
	                        params.rightMargin = scaleHeightPixels(params.rightMargin);
	                    }

	                    if (params.bottomMargin != 0) {
	                        params.bottomMargin = scaleHeightPixels(params.bottomMargin);
	                    }
	                }
	            }

	            int left = source.getPaddingLeft();
	            int top = source.getPaddingTop();
	            int right = source.getPaddingRight();
	            int bottom = source.getPaddingBottom();
	            if (left > 0) {
	                left = scaleHeightPixels(left);
	            }

	            if (top > 0) {
	                top = scaleHeightPixels(top);
	            }

	            if (right > 0) {
	                right = scaleHeightPixels(right);
	            }

	            if (bottom > 0) {
	                bottom = scaleHeightPixels(bottom);
	            }

	            source.setPadding(left, top, right, bottom);
	        }
	    }

	    public static final void scaleViewGroupByRatio(ViewGroup group) {
	        if (group == null || !isScale())
	            return;
	        final int childCount = group.getChildCount();

	        for (int i = 0; i < childCount; i++) {
	            View child = group.getChildAt(i);
	            if(child instanceof ViewGroup){//如果子view也是viewgroup类型的，则递归子view
	            	scaleViewGroupByRatio((ViewGroup) child);
	            }
	            scaleViewByRatio(child);
	        }
	    }

	    public static final void scaleViewGroupByWidthOrHeight(ViewGroup group) {
	        if (group == null || !isScale())
	            return;
	        final int childCount = group.getChildCount();

	        for (int i = 0; i < childCount; i++) {
	            View child = group.getChildAt(i);
	            scaleViewByWidthOrHeight(child);
	        }
	    }

	    public static final void scaleViewGroupEnlargeChildByHeightPixels(
	            ViewGroup group, int childId) {
	        if (group == null || !isScale()) {
	            return;
	        }

	        final int childCount = group.getChildCount();

	        for (int i = 0; i < childCount; i++) {
	            View child = group.getChildAt(i);
	            if (child.getId() == childId) {
	                scaleViewByHeight(child);
	            } else {
	                scaleViewByRatio(child);
	            }
	        }
	    }

	    public static final void scaleViewGroupEnlargeChildByWidthPixels(
	            ViewGroup group, int childId) {
	        if (group == null || !isScale()) {
	            return;
	        }
	        final int childCount = group.getChildCount();

	        for (int i = 0; i < childCount; i++) {
	            View child = group.getChildAt(i);
	            if (child.getId() == childId) {
	                scaleViewByWidth(child);
	            } else {
	                scaleViewByRatio(child);
	            }
	        }
	    }

	    public static final void scaleViewGroupEnlargeByWidthPixels(ViewGroup group) {
	        if (group != null && isScale()) {
	            final int childCount = group.getChildCount();
	            for (int i = 0; i < childCount; i++) {
	                scaleViewByWidth(group.getChildAt(i));
	            }
	        }
	    }

	    public static final void scaleViewGroupEnlargeByHeightPixels(ViewGroup group) {
	        if (group != null && isScale()) {
	            final int childCount = group.getChildCount();
	            for (int i = 0; i < childCount; i++) {
	                scaleViewByHeight(group.getChildAt(i));
	            }
	        }
	    }

	    private static final boolean isScale() {
	        return isScaleHeightPixels() || isScaleWidthPixels();
	    }

	    private static final boolean isScaleWidthPixels() {
	        return (BaseApplication.sWidthPixels/ STANDARD_WIDTH) != 1;
	    }

	    private static final boolean isScaleHeightPixels() {
	        return (BaseApplication.sHeightPixels / STANDARD_HEIGHT) != 1;
	    }

	    public static final int scaleTextSize(float size) {
	        if (size < 0) {
	            return 0;
	        }

	        if (!isScale()) {
	            return (int) size;
	        }

	        if (getWidthRatio() >= getHeightRatio()) {
	            return scaleHeightPixels((int) size);
	        } else {
	            return scaleWidthPixels((int) size);
	        }
	    }

	    public static final int scaleWidthPixels(int px) {
	        int dp = px2dip(px);
	        return Math.round(dp * BaseApplication.sWidthPixels / STANDARD_WIDTH);
	    }

	    public static final int scaleHeightPixels(int px) {
	        int dp = px2dip(px);
	        return Math.round(dp * BaseApplication.sHeightPixels / STANDARD_HEIGHT);
	    }

	    public static final float getWidthRatio() {
	        return BaseApplication.sWidthPixels / STANDARD_WIDTH;
	    }

	    public static final float getHeightRatio() {
	        return BaseApplication.sHeightPixels / STANDARD_HEIGHT;
	    }

	    public static int px2dip(float pxValue) {
	        return (int) ((pxValue / BaseApplication.sDensity + 0.5f) * 0.83);
	    }

	    public static final void scaleTextDrawable(TextView source) {
	        if (source == null) {
	            return;
	        }

	        Drawable[] draws = source.getCompoundDrawables();

	        for (Drawable d : draws) {
	            scaleDrawable(d);
	        }

	        source.setCompoundDrawables(draws[0], draws[1], draws[2], draws[3]);

	        int padding = source.getCompoundDrawablePadding();

	        if (padding > 0) {
	            if (getWidthRatio() >= getHeightRatio()) {
	                padding = scaleHeightPixels(padding);
	            } else {
	                padding = scaleWidthPixels(padding);
	            }

	            source.setCompoundDrawablePadding(padding);
	        }
	    }

	    public static final void scaleDrawable(Drawable d) {
	        if (d != null) {
	            int height = 0;
	            int width = 0;
	            if (getWidthRatio() >= getHeightRatio()) {
	                height = scaleHeightPixels(d.getIntrinsicHeight());
	                width = Math.round(d.getIntrinsicWidth() * height * 1.0f
	                        / d.getIntrinsicHeight());
	            } else {
	                width = scaleWidthPixels(d.getIntrinsicWidth());
	                height = Math.round(d.getIntrinsicHeight() * width * 1.0f
	                        / d.getIntrinsicWidth());
	            }
	            d.setBounds(0, 0, width, height);
	        }
	    }

	    public static final void scaleTextDrawableByWidth(TextView source) {
	        if (source == null) {
	            return;
	        }

	        Drawable[] draws = source.getCompoundDrawables();

	        for (Drawable d : draws) {
	            scaleDrawableByWidth(d);
	        }

	        source.setCompoundDrawables(draws[0], draws[1], draws[2], draws[3]);

	        int padding = source.getCompoundDrawablePadding();

	        if (padding > 0) {
	            source.setCompoundDrawablePadding(scaleWidthPixels(padding));
	        }
	    }

	    public static final void scaleDrawableByWidth(Drawable d) {
	        if (d != null) {
	            int height = 0;
	            int width = 0;
	            width = scaleWidthPixels(d.getIntrinsicWidth());
	            height = Math.round(d.getIntrinsicHeight() * width * 1.0f
	                    / d.getIntrinsicWidth());
	            d.setBounds(0, 0, width, height);
	        }
	    }
	   
	    public static final void scaleTextDrawableByHeight(TextView source) {
	        if (source == null) {
	            return;
	        }

	        Drawable[] draws = source.getCompoundDrawables();

	        for (Drawable d : draws) {
	            scaleDrawableByHeight(d);
	        }

	        source.setCompoundDrawables(draws[0], draws[1], draws[2], draws[3]);

	        int padding = source.getCompoundDrawablePadding();
	        if (padding > 0) {
	            source.setCompoundDrawablePadding(scaleHeightPixels(padding));
	        }
	    }

	    public static final void scaleDrawableByHeight(Drawable d) {
	        if (d != null) {
	            int height = 0;
	            int width = 0;
	            height = scaleHeightPixels(d.getIntrinsicHeight());
	            width = Math.round(d.getIntrinsicWidth() * height * 1.0f
	                    / d.getIntrinsicHeight());
	            d.setBounds(0, 0, width, height);
	        }
	    }

	    public static void scaleViewGroupByHeight(ViewGroup group) {
	        if (group != null && isScale()) {
	            final int childCount = group.getChildCount();
	            for (int i = 0; i < childCount; i++) {
	                scaleViewHeight(group.getChildAt(i));
	            }
	        }
	    }
	    
	    public static void scaleViewHeight(View view) {
	        if (view == null || !isScale())
	            return;
	        android.view.ViewGroup.LayoutParams lt = view.getLayoutParams();

	        if (lt != null) {
	            scaleHeightEach(lt);

	            if (lt instanceof ViewGroup.MarginLayoutParams) {
	                scaleMargin((MarginLayoutParams) lt);
	            }

	            scalePaddingForView(view);
	        }
	    }

	    public static void scaleHeightEach(LayoutParams lt) {
	        if (lt == null)
	            return;
	        int heightWeight = lt.height;
	        if (heightWeight <= 0) {
	            return;
	        }

	        if (heightWeight > 0 && isScaleHeightPixels()) {
	            heightWeight = scaleHeightPixels(heightWeight);
	        }

	        lt.height = heightWeight;
	    }
}
