package com.fengruyi.common.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import com.fengruyi.common.log.Logger;

/**
 * 当前程序是否后台运行
 * 当前手机是否处于睡眠
 * 当前网络是否已连接
 * 当前网络是否wifi状态
 * 安装apk
 * 判断是否为手机
 * 获取屏幕宽度
 * 获取屏幕高度
 * 获取设备的IMEI
 * 获取设备的mac地址
 * 获取当前应用的版本号
 * 收集设备信息并以Properties返回
 * 收集设备信息并以String返回
 * Author:fengruyi
 */
public class AppUtil {
    private static final String TAG = AppUtil.class.getSimpleName();

    /**
     *判断当前应用程序是否后台运行
     * @param context
     * @return
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    Logger.d(TAG, "后台程序: " + appProcess.processName);
                    return true;
                }else{
                    Logger.d(TAG, "前台程序: " + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 判断手机是否处理睡眠
     * @param context
     * @return
     */
    public static boolean isSleeping(Context context){
        KeyguardManager kgMgr = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        boolean isSleeping = kgMgr.inKeyguardRestrictedInputMode();
        Logger.d(TAG, isSleeping ? "手机睡眠中.." : "手机未睡眠...");
        return isSleeping;
    }

    /**
     * 检查网络是否已连接
     * @author com.tiantian
     * @param context
     * @return
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前是否是wifi状态
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(wifiNetworkInfo.isConnected())
        {
            return true ;
        }
        return false ;
    }
    /**
     * 指定wifi 是否连接 
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context,String ssid)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        WifiManager wifiManager =   (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if(wifiNetworkInfo!=null&&wifiNetworkInfo.isConnected())
        {
        	WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        	if(wifiInfo!=null&&wifiInfo.getSSID().equals("\"" + ssid + "\"")){
        		return true ;
        	}
            
        }
        return false ;
    }
    /**
     * 安装apk
     * @param context
     * @param file
     */
    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("application/vnd.android.package-archive");
        intent.setData(Uri.fromFile(file));
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

     

    /** 使用Properties来保存设备的信息和错误堆栈信息 */
    private static final String VERSION_NAME = "versionName";
    private static final String VERSION_CODE = "versionCode";
    private static final String STACK_TRACE = "STACK_TRACE";

    /**
     * 判断是否为手机
     * @author fengruyi
     * @param context
     * @return
     */
    public static boolean isPhone(Context context) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int type = telephony.getPhoneType();
        if (type == TelephonyManager.PHONE_TYPE_NONE) {
            Logger.i(TAG, "Current device is Tablet!");
            return false;
        } else {
            Logger.i(TAG, "Current device is phone!");
            return true;
        }
    }

    /**
     * 获得设备的屏幕宽度
     * @param context
     * @return
     */
    public static int getDeviceWidth(Context context){
    	DisplayMetrics dm = new DisplayMetrics();
    	WindowManager manager = (WindowManager) context
                 .getSystemService(Context.WINDOW_SERVICE);
    	manager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获得设备的屏幕高度
     * @param context
     * @return
     */
    public static int getDeviceHeight(Context context){
    	DisplayMetrics dm = new DisplayMetrics();
    	WindowManager manager = (WindowManager) context
                 .getSystemService(Context.WINDOW_SERVICE);
    	manager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }
    
    /**
     * 获取设备id（IMEI）
     * @author fengruyi
     * @param context
     * @return
     */
    public static String getDeviceIMEI(Context context) {
        String deviceId;
        if (isPhone(context)) {
            TelephonyManager telephony = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = telephony.getDeviceId();
        } else {
            deviceId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);

        }
        Logger.d(TAG, "当前设备IMEI码: " + deviceId);
        return deviceId;
    }
    /**
     * 获取设备mac地址
     * @author fengruyi
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        String macAddress;
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        macAddress = info.getMacAddress();
        Logger.d(TAG, "当前mac地址: " + (null == macAddress ? "null" : macAddress));
        if(null == macAddress){
            return "";
        }
        macAddress = macAddress.replace(":", "");
        return macAddress;
    }

    /**
     * 获取当前应用程序的版本号
     * @author fengruyi
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context) {
        String version = "1.0";
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Logger.e(TAG, "getAppVersion", e);
        }
        Logger.d(TAG, "该应用的版本号: " + version);
        return version;
    }
    /**
	 * 获取应用程序名称
	 * 
	 * @param context
	 * @return 应用程序名称
	 */
	public String getAppName(Context context) {
		String appName = "";
		try {
			PackageManager pm = context.getPackageManager();
			ApplicationInfo applicationInfo = pm.getApplicationInfo(
					context.getPackageName(), 0);
			appName = pm.getApplicationLabel(applicationInfo).toString();
		} catch (Exception e) {

		}
		return appName;
	}
	
    /**
     * 收集设备信息
     *
     * @param context
     */
    public static Properties collectDeviceInfo(Context context) {
        Properties mDeviceCrashInfo = new Properties();
        try {
           
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                mDeviceCrashInfo.put(VERSION_NAME, pi.versionName == null ? "not set" : pi.versionName);
                mDeviceCrashInfo.put(VERSION_CODE, pi.versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e(TAG, "Error while collect package info", e);
        }
        // 使用反射来收集设备信息.在Build类中包含各种设备信息,
        // 例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
        // 返回 Field 对象的一个数组，这些对象反映此 Class 对象所表示的类或接口所声明的所有字段
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                // setAccessible(boolean flag)
                // 将此对象的 accessible 标志设置为指示的布尔值。
                // 通过设置Accessible属性为true,才能对私有变量进行访问，不然会得到一个IllegalAccessException的异常
                field.setAccessible(true);
                mDeviceCrashInfo.put(field.getName(), field.get(null));
            } catch (Exception e) {
                Logger.e(TAG, "Error while collect crash info", e);
            }
        }

        return mDeviceCrashInfo;
    }

    /**
     * 收集设备信息
     * @param context
     * @return
     */
    public static String collectDeviceInfoStr(Context context){
        Properties prop = collectDeviceInfo(context);
        Set deviceInfos = prop.keySet();
        StringBuilder deviceInfoStr = new StringBuilder("{\n");
        for(Iterator iter = deviceInfos.iterator(); iter.hasNext();){
            Object item = iter.next();
            deviceInfoStr.append("\t\t\t" + item + ":" + prop.get(item) + ", \n");
        }
        deviceInfoStr.append("}");
        return deviceInfoStr.toString();
    }

    /**
     * 是否有SDCard
     * @return
     */
    public static boolean haveSDCard(){
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 强制隐藏软键盘
     */
    public static void hideSoftInput(Context context) {
        View view = ((Activity)context).getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    /**
     * 隐藏软键盘
     */
    public static void hideSoftInput(Context context, EditText edit) {
        edit.clearFocus();
        InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(edit.getWindowToken(), 0);
    }
    /**
     * 显示软键盘
     */
    public static void showSoftInput(Context context, EditText edit) {
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        InputMethodManager inputManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(edit, 0);
    }

    /**
     * 回到home，后台运行
     * @param context
     */
    public static void goHome(Context context){
        Logger.d(TAG, "返回键回到HOME，程序后台运行...");
        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);

        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        context.startActivity(mHomeIntent);
    }

    /**
     * 获取状态栏高度
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity){
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }



}
