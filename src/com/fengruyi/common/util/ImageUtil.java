package com.fengruyi.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;

import com.fengruyi.common.log.Logger;
import com.nostra13.universalimageloader.utils.IoUtils;

public class ImageUtil {
	public static final String TAG = ImageUtil.class.getSimpleName();
	/**
     * 压缩图片尺寸
     * 
     * @param filepath
     * @param outpath
     * @param rw
     *            请求的宽
     * @param rh
     *            请求的高
     * @return  压缩后的图片路径
     * @throws CompressException 
     */
    public static boolean compress(String filepath, String outpath, int rw, int rh) {
        if(TextUtil.isEmpty(filepath)||!FileUtil.isFileExist(filepath)){
        	return false;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        
        // 获取宽，高,不做真实解码
        options.inJustDecodeBounds = true;

        // 解码，注：此时返回的bitmap为空，在options中有图片的真实宽，高
        Bitmap bitmap = BitmapFactory.decodeFile(filepath, options);
        // 设置图片文件为原大小的1/N
        //options.inSampleSize = computeSampleSize(picWidth, picHeight, rw, rh);  // 品质 高
        //options.inSampleSize = ImageUtil.getSampleSize(options, rw, rh);  // 品质 低
        options.inSampleSize = computeSampleSize(options, 800, rw * rh); // 品质 中
        //options.inSampleSize = computeSampleSize2(picWidth, picHeight);   // 品质 中
        // 设置做真实解码
        options.inJustDecodeBounds = false;

        // 设置灰度
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        // 设置，解码不占用系统核心内存，随时可以释放
        options.inInputShareable = true;
        options.inPurgeable = true;

        // 真实解码
        // Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(filepath, options);
        } catch (Exception e) {
        	Logger.v(TAG, "Error to load image from decodeFile", e);
        	return false;
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        // jpg 编码
        if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)) {
        	Logger.v(TAG, "Can not commpress image to JPEG");
        	return false;
        }
        
        byte[] imageData = stream.toByteArray();

        // copy 数据
        byte[] result = imageData.clone();

        // 关闭stream
        try {
            stream.close();
        } catch (Exception e) {
        	Logger.e(TAG, "Can not close bitmap stream", e);
        }
        // 储存图片至指定路径
        FileUtil.writeDataToFile(result, outpath);
        imageData = null;
        result = null;
        IOUtil.recycleBitmap(bitmap);
        return true;
    }

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
	  /**
     * 压缩图片直到容量小于指定值(kb)并返回ByteArrayInputStream类型
     * @param image
     * @param capacity
     * @return
     */
    public static ByteArrayInputStream compressImage(Bitmap image, int capacity) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > capacity) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            if(options < 10){
                break;
            }
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        baos.reset();
        return bais;
    }
    /**
     *  压缩图片直到容量小于指定值(kb)，并保存到sdcard
     * @param file
     * @param bm
     * @param capacity
     * @return
     */
    public static int saveBitmap2SDWithCapacity(File file, Bitmap bm, int capacity){
  
        FileOutputStream out = null;
        ByteArrayInputStream bais = null;
        try {
        	FileUtil.creatSDDir(file.getParent());
            out = new FileOutputStream(file);
            bais = compressImage(bm, capacity);
            byte[] buffer = new byte[1024];
            int len = 0;
            while((len = bais.read(buffer)) != -1){
                out.write(buffer, 0, len);
            }
            out.flush();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }finally {
            IOUtil.closeIO(out, bais);
        }

    }
    
    /**
     * 先质量压缩到指定百分比（0% ~ 90%），再把bitmap保存到sd卡上
     * @param relativePath 文件所在目录路径
     * @param fileName 文件名
     * @param bm
     * @param quality
     * @return
     */
    public static int saveBitmap2SD(File file, Bitmap bm, int quality){
        FileOutputStream out = null;
        try {
        	FileUtil.creatSDDir(file.getParent());
            out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, quality, out);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }finally{
            IOUtil.closeIO(out);
        }
    }

    /**
     * 先质量压缩到指定百分比（0% ~ 90%），再把bitmap保存到sd卡上
     * @param filePath 文件路径
     * @param bm
     * @param quality
     * @return
     */
    public static int saveBitmap2SDAbsolute(String filePath, Bitmap bm, int quality){
        File file = null;
        FileOutputStream out = null;
        try {
            file = new File(filePath);
            if(!file.exists()){
                file.createNewFile();
            }
            out = new FileOutputStream(file.getPath());
            bm.compress(Bitmap.CompressFormat.JPEG, quality, out);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }finally{
            IOUtil.closeIO(out);
        }
    }
	/**
	 * 按指定质量（0-100）把bitmap转化成byte
	 * @param bitmap
	 * @return
	 */
	public static byte[] bitmapToByte(Bitmap bitmap,int quality){
		if(bitmap == null){
			Logger.v(TAG, "bitmap is null");
			return null;
		} 
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, quality, out);
		byte[] result = out.toByteArray();
		IOUtil.closeIO(out);
		return result;
	}
	
	/**
	 * byte 转化成bitmap
	 * @param b
	 * @return
	 */
	public static Bitmap byteToBitmap(byte[] b){
		return (b ==null || b.length == 0)?null:BitmapFactory.decodeByteArray(b, 0, b.length);
	}
	
	/**
	 * Drawable 转化成  Bitmap
	 * @param d
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable d){
		return d == null ? null : ((BitmapDrawable)d).getBitmap();
	}
	
	/**
	 * Bitmap 转化成 Drawable
	 * @param b
	 * @return
	 */
	public static Drawable bitmapToDrawable(Bitmap b){
		return b == null ? null : new BitmapDrawable(b);
	}
	
	/**
	 * 按指定质量（0-100）把Drawable转化成byte
	 * @param d
	 * @param quality
	 * @return
	 */
	public static byte[] drawableToByte(Drawable d,int quality){
		return bitmapToByte(drawableToBitmap(d), quality);
	}
	
	/**
	 * 把byte转化成Drawable
	 * @param b
	 * @return
	 */
	public static Drawable byteToDrawable(byte[] b){
		return bitmapToDrawable(byteToBitmap(b));
	}
	
	/**
	 * 请求网络图片并把结果转化成输入流inputStream
	 * @param imageUrl 网络图片地址
	 * @param readTimeoutMillis 请求超时时间，毫秒数
	 * @param requestProperty 请求参数
	 * @return
	 */
	public static InputStream getInputStreamFromUrl(String imageUrl, int readTimeoutMillis,Map<String,String> requestProperty){
		InputStream input = null ;
		try {
			URL url = new URL(imageUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			
			if(readTimeoutMillis > 0 ){
				con .setReadTimeout(readTimeoutMillis);
			}
			
			input = con.getInputStream();
		} catch (MalformedURLException  e) {
			Logger.e(TAG, "MalformedURLException occurred ",e);
		} catch (IOException  e) {	
			Logger.e(TAG, "IOException occurred ",e);
		}finally{
			IOUtil.closeIO(input);
		}
		return input;
	}
	
	/**
	 * 请求网络图片并把结果转化成Drawable
	 * @param imageUrl 网络图片地址
	 * @param readTimeoutMillis 请求超时时间，毫秒数
	 * @param requestProperty 请求参数
	 * @return
	 */
	public static Drawable getDrawableFromUrl(String imageUrl,int readTimeoutMillis,Map<String,String> requestProperty){
		InputStream input = getInputStreamFromUrl(imageUrl, readTimeoutMillis, requestProperty);
		Drawable d = Drawable.createFromStream(input, "src");
		IOUtil.closeIO(input);
		return d;
	}
	
	/**
	 * 请求网络图片并把结果转化成Bitmap
	 * @param imageUrl 网络图片地址
	 * @param readTimeoutMillis 请求超时时间，毫秒数
	 * @param requestProperty 请求参数
	 * @return
	 */
	 public static Bitmap getBitmapFromUrl(String imageUrl, int readTimeoutMillis, Map<String, String> requestProperties) {
	        InputStream stream = getInputStreamFromUrl(imageUrl, readTimeoutMillis, requestProperties);
	        Bitmap b = BitmapFactory.decodeStream(stream);
	        IOUtil.closeIO(stream);
	        return b;
	    }
	 
	 /**
	  * 指定图片宽高绽放图片
	  * @param org
	  * @param newWidth
	  * @param newHeight
	  * @return
	  */
	  public static Bitmap scaleImageTo(Bitmap org, int newWidth, int newHeight) {
	        return scaleImage(org, (float)newWidth / org.getWidth(), (float)newHeight / org.getHeight());
	    }
	  
	  /**
	   * 指定图片宽高比例绽放图片
	   * @param org
	   * @param scaleWidth (0.0-1.0)绽放比例
	   * @param scaleHeight (0.0-1.0)绽放比例
	   * @return
	   */
	  public static Bitmap scaleImage(Bitmap org, float scaleWidth, float scaleHeight) {
	        if (org == null) {
	            return null;
	        }
	        Matrix matrix = new Matrix();
	        matrix.postScale(scaleWidth, scaleHeight);
	        return Bitmap.createBitmap(org, 0, 0, org.getWidth(), org.getHeight(), matrix, true);
	    }
	  
	  public static Bitmap getCompressedImage(String srcPath, float ww, float hh, int size) {
	        BitmapFactory.Options newOpts = new BitmapFactory.Options();
	        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
	        newOpts.inJustDecodeBounds = true;
	        BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

	        int w = newOpts.outWidth;
	        int h = newOpts.outHeight;
	        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
//	        float hh = 800f;//这里设置高度为800f
//	        float ww = 480f;//这里设置宽度为480f
	        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
	        int be = 1;//be=1表示不缩放
	        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
	            be = (int) (newOpts.outWidth / ww);
	        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
	            be = (int) (newOpts.outHeight / hh);
	        }
	        if (be <= 0)
	            be = 1;
	        newOpts.inSampleSize = be;//设置缩放比例
	        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
	        newOpts.inJustDecodeBounds = false;
	        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
	        return BitmapFactory.decodeStream(
	                compressImage(bitmap, size), // 缩小到指定容量
	                null, null);//把ByteArrayInputStream数据生成图片
	    }
	  
	  /**
	     * 读取图片属性：旋转的角度
	     * @param path 图片绝对路径
	     * @return degree旋转的角度
	     */
	    public static int readPictureDegreeFromExif(String path) {
	        int degree  = 0;
	        try {
	            ExifInterface exifInterface = new ExifInterface(path);
	            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
	            switch (orientation) {
	                case ExifInterface.ORIENTATION_ROTATE_90:
	                    degree = 90;
	                    break;
	                case ExifInterface.ORIENTATION_ROTATE_180:
	                    degree = 180;
	                    break;
	                case ExifInterface.ORIENTATION_ROTATE_270:
	                    degree = 270;
	                    break;
	            }
	        } catch (IOException e) {
	            Logger.e(TAG, e);
	        }
	        return degree;
	    }
	    
	    /*
	     * 旋转图片
	     * @param angle
	     * @param bitmap
	     * @return Bitmap
	     */
	    public static Bitmap rotaingImage(int angle , Bitmap bitmap){
	        //旋转图片 动作
	        Matrix matrix = new Matrix();;
	        matrix.postRotate(angle);
	        // 创建新的图片
	        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	        IOUtil.recycleBitmap(bitmap);
	        return resizedBitmap;
	    }

	    /**
	     * 处理相机照片旋转角度
	     * @param path 用于获取原图的信息
	     * @return 原图的bitmap（可以是被压缩过的）
	     */
	    public static Bitmap formatCameraPictureOriginal(String path, Bitmap bitmap){
	        /**
	         * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
	         */
	        int degree = readPictureDegreeFromExif(path);
	        if(0 == degree){
	            return bitmap;
	        }
	        /**
	         * 把图片旋转为正的方向
	         */
	        Bitmap newbitmap = rotaingImage(degree, bitmap);
	        IOUtil.recycleBitmap(bitmap);
	        return newbitmap;
	    }
}
