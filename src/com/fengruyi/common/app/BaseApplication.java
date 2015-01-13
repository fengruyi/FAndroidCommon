package com.fengruyi.common.app;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.fengruyi.common.log.Logger;
import com.fengruyi.common.util.AppUtil;
import com.fengruyi.common.util.FileUtil;
import com.fengruyi.common.util.PrefsUtil;
import com.nostra13.universalimageloader.cache.disc.DiscCacheAware;
import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiscCache;
import com.nostra13.universalimageloader.cache.memory.MemoryCacheAware;
import com.nostra13.universalimageloader.cache.memory.impl.LimitedAgeMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class BaseApplication extends Application{
	
	private static BaseApplication instance;
	public static float sHeightPixels = -1f;//屏幕的高，像素值
	public static float sWidthPixels = -1f;//屏幕的宽，像素值
	public static float sDensity = 1f;//屏幕密度
	public static BaseApplication getInstance(){
		return instance;
	}
	
	/**
	 *应用初始工作
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		//initAppFileRoot();
		instance = this;
		readParams();
		initImageLoader();
		initPrefs();
		
	}
	/**
	 * 获取屏幕参数
	 */
	protected void readParams(){
		DisplayMetrics displayMetrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(displayMetrics);
		sWidthPixels = displayMetrics.widthPixels;
		sHeightPixels = displayMetrics.heightPixels;
		sDensity = displayMetrics.density;
	}
	/**
	 * 图片缓存初始化,图片缓存大小 目录等设置
	 */
	protected void initImageLoader(){
		File cacheDir = FileUtil.obtainDirF("feng"); // or any other folder
		MemoryCacheAware<String, Bitmap> memoryCacheCore 
		          = new LruMemoryCache(4 * 1024 * 1024); // or any other implementation 4M缓存

		MemoryCacheAware<String, Bitmap> memoryCache 
		          = new LimitedAgeMemoryCache<String, Bitmap>(memoryCacheCore, 15 * 60);//15分钟
		DiscCacheAware discCache = new LimitedAgeDiscCache(cacheDir, 15 * 60);//15分钟
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
		        .memoryCache(memoryCache)
		        .discCache(discCache)
		        .build();
		ImageLoader.getInstance().init(config);//全局初始化此配置  
	}
	/**
	 * 创建应用根目录，用于保存应用数据到sd卡
	 */
	protected void initAppFileRoot(){
		if(AppUtil.haveSDCard()){
			FileUtil.obtainDirF("feng");
		}else{
			Logger.e("Baseapplication", "sd卡不存在！");
		}
	}
	
	/**
     * 初始化SharedPreference
     */
	protected void initPrefs(){
		PrefsUtil.init(getInstance(), getPackageName()+"_preference", Context.MODE_MULTI_PROCESS);
	}
}
