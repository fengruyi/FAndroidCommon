package com.fengruyi.common.util;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class ViewUtil {
	
	public static void setOnClickListener(OnClickListener Listener,View... views){
		for(View view : views){
			view.setOnClickListener(Listener);
		}
	}
	
	 public static void setViewHeight(View view, int height) {
	        if (view == null) {
	            return;
	        }
	        ViewGroup.LayoutParams params = view.getLayoutParams();
	        params.height = height;
	    }
	 
	 public static void setViewWidth(View view, int width) {
	        if (view == null) {
	            return;
	        }
	        ViewGroup.LayoutParams params = view.getLayoutParams();
	        params.width = width;
	    }
}
