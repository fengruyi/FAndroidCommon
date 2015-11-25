package com.fengruyi.common.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 自定义toast
 * @author fengruyi
 *
 */
public class ToastUtil {
	
	/**
	 * 居中显示的toast，无图片
	 * 
	 * @param context
	 *            上下文对象
	 * @param contentRes
	 *            提示语
	 * @param showtimes
	 *            显示时间
	 */
	public static void centerToast(Context context, int contentRes,
			int duration) {
		Toast toast = Toast.makeText(context, contentRes, duration);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	/**
	 * 居中显示的toast，无图片
	 * 
	 * @param context
	 *            上下文对象
	 * @param contentRes
	 *            提示语
	 * @param showtimes
	 *            显示时间
	 */
	public static void centerToast(Context context, String msg,
			int duration) {
		Toast toast = Toast.makeText(context, msg, duration);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	/**
	 * 居中显示的toast ，带图片
	 * 
	 * @param context
	 *            上下文对象
	 * @param content
	 *            提示语
	 * @param showtimes
	 *            显示时间长度
	 * @param drawableRes
	 *            显示的图片资源
	 */
	public static void centerToastWithPic(Context context, String content,
			int duration, int drawableRes) {
		Toast toast = Toast.makeText(context, content, duration);
		toast.setGravity(Gravity.CENTER, 0, 0);
		LinearLayout toastView = (LinearLayout) toast.getView();
		ImageView imageCodeProject = new ImageView(context);
		imageCodeProject.setImageResource(drawableRes);
		toastView.addView(imageCodeProject, 0);
		toast.show();
	}
	

	/**
	 * 居中显示的toast ，带图片
	 * 
	 * @param context
	 *            上下文对象
	 * @param contentRes
	 *            提示语
	 * @param showtimes
	 *            显示时间长度
	 * @param drawableRes
	 *            显示的图片资源
	 */
	public static void centerToastWithPic(Context context, int contentRes,
			int duration, int drawableRes) {
		Toast toast = Toast.makeText(context, contentRes, duration);
		toast.setGravity(Gravity.CENTER, 0, 0);
		LinearLayout toastView = (LinearLayout) toast.getView();
		ImageView imageCodeProject = new ImageView(context);
		imageCodeProject.setImageResource(drawableRes);
		toastView.addView(imageCodeProject, 0);
		toast.show();
	}
}
