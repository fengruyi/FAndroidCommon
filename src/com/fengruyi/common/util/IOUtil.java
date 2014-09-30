package com.fengruyi.common.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.fengruyi.common.log.Logger;

import android.content.Context;
import android.graphics.Bitmap;


/**
 * 
 * @author fengruyi
 *
 */
public class IOUtil {
	 public static final String TAG = IOUtil.class.getSimpleName();

	    /**
	     * 关闭流
	     * @param closeables
	     */
	    public static void closeIO(Closeable... closeables){
	        if(null == closeables || closeables.length <= 0){
	            return;
	        }
	        for(Closeable cb : closeables){
	            try {
	                if(null == cb){
	                    continue;
	                }
	                cb.close();
	            } catch (IOException e) {
	                Logger.e(TAG, "close IO ERROR...", e);
	            }
	        }
	    }

	    /**
	     * 回收 bitmaps
	     * @param bitmaps
	     */
	    public static void recycleBitmap(Bitmap... bitmaps){
	        if(TextUtil.isEmpty(bitmaps)){
	            return;
	        }

	        for(Bitmap bm : bitmaps){
	            if(null != bm && !bm.isRecycled()){
	                bm.recycle();
	            }
	            bm = null;
	        }
	        System.gc();

	    }


	    /**
	     * 复制文件
	     * @param from
	     * @param to
	     */
	    public static void copyFile(File from, File to){
	        if(null == from || !from.exists()){
	            Logger.e(TAG, "file(from) is null or is not exists!!");
	            return;
	        }
	        if(null == to){
	            Logger.e(TAG, "file(to) is null!!");
	            return;
	        }

	        InputStream is = null;
	        OutputStream os = null;
	        try {
	            is = new FileInputStream(from);
	            if(!to.exists()){
	                to.createNewFile();
	            }
	            os = new FileOutputStream(to);

	            byte[] buffer = new byte[1024];
	            int len = 0;
	            while(-1 != (len = is.read(buffer))){
	                os.write(buffer, 0, len);
	            }
	            os.flush();
	        } catch (Exception e) {
	            Logger.e(TAG, e);
	        }finally {
	            closeIO(is, os);
	        }


	    }

	    /**
	     * 从文件中读取文本
	     * @param filePath
	     * @return
	     */
	    public static String readFile(String filePath){
	        StringBuilder resultSb = null;
	        InputStream is = null;
	        try {
	            is = new FileInputStream(filePath);
	        } catch (Exception e) {
	            Logger.e(TAG, e);
	        }
	        return inputStream2String(is);
	    }
	    public static String readFile(File file){
	        return readFile(file.getPath());
	    }

	    /**
	     * 从assets中读取文本
	     * @param name
	     * @return
	     */
	    public static String readFileFromAssets(Context context, String name){
	        InputStream is = null;
	        try {
	            is = context.getResources().getAssets().open(name);
	        } catch (Exception e) {
	            Logger.e(TAG, e);
	        }
	        return inputStream2String(is);

	    }
	    
	    /**
	     * 输入流转为字符串
	     * @param is
	     * @return
	     */
	    public static String inputStream2String(InputStream is){
	        if(null == is){
	            return null;
	        }
	        StringBuilder resultSb = null;
	        try{
	            BufferedReader br = new BufferedReader(new InputStreamReader(is));

	            resultSb = new StringBuilder();
	            String len;
	            while(null != (len = br.readLine())){
	                resultSb.append(len);
	            }
	        }catch(Exception ex){
	            Logger.e(TAG, ex);
	        }finally{
	            closeIO(is);
	        }
	        return null == resultSb ? null : resultSb.toString();
	    }


}
