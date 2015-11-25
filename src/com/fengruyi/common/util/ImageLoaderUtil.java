package com.fengruyi.common.util;


import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 图片加载工具类，使用Android-Universal-Image-Loader开源库缓存
 * @author fengruyi
 *
 */
public class ImageLoaderUtil {
	
	public static void loadImage(String imageUrl,ImageView imageview,int defaultImgId){
		DisplayImageOptions disoptions = new DisplayImageOptions.Builder()
		.cacheInMemory()
		.showStubImage(defaultImgId)
		.showImageForEmptyUri(defaultImgId)
		.showImageOnFail(defaultImgId)
		.cacheOnDisc().build();
		ImageLoader.getInstance().displayImage(imageUrl, imageview, disoptions);
	}
	
	/**
	 * 清除图片内存缓存
	 */
	public static void cleanImageCache(){
		ImageLoader.getInstance().clearMemoryCache();
	}
}
