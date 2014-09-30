package com.fengruyi.common.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.fengruyi.common.log.Logger;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;

/**
 * 文件处理类
 * @author fengruyi
 *
 */
public class FileUtil {
	
	public static final String TAG = FileUtil.class.getSimpleName();
	public static final String SD_CARD_PATH = Environment.getExternalStorageDirectory().toString();
	
	/**
	 * 
	 * @return 返回sd卡路径:/mnt/sdcard
	 */
	public static String getSDPATH() {
		return SD_CARD_PATH;
	}
    
	/**
	 * 创建指定路径目录,可以是多级目录生成新
	 * @param 文件夹路径
	 * @return 
	 */
    public static boolean createDirF(String path){
        File file = new File(path);
        if(!file.exists()){
            return file.mkdirs();
        }
        return true;
    }
    /**
	 * 创建指定路径目录,可以是多级目录生成新
	 * @param 文件夹路径
	 * @return 
	 */
    public static File obtainDirF(String dir){
    	File file = new File(SD_CARD_PATH+File.separator+dir);
    	if(!file.exists()){
    		file.mkdirs();
    	}
    	return file;
    }
    /**
	 * 在SD卡上创建文件
	 * 
	 * @throws java.io.IOException
	 */
	public static boolean creatSDFile(String filePath){
		File file = new File(SD_CARD_PATH + filePath);
		try {
			return file.createNewFile();
		} catch (IOException e) {
			Logger.e(TAG, "IOException", e);
			return false;
		}
	}
	
	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dirRelativePath
	 */
	public static boolean creatSDDir(String filePath) {
		return createDirF(SD_CARD_PATH + filePath);
	}
	
	/**
	 * 判断文件是否存在
	 */
	public static boolean isFileExist(String fileRelativePath){
		File file = new File(fileRelativePath);
		return file.exists();
	}
	
	/**
	 * 获取文件名
	 * @param filePath 文件路径
	 * @return
	 */
	public static String getFileName(String filePath) {
        if (TextUtil.isEmpty(filePath)) {
            return filePath;
        }
        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
    }
	/**
	 * 递归计算某个文件或目录所有文件总大小 单件kb
	 * @param file 文件夹
	 * @return
	 */
	public static double getFolderSize(File file){
		double size = 0;
		if(file!=null&&file.exists()){
			if(file.isDirectory()){
				File[] children = file.listFiles();
				for(File f:children){
					size+= getFolderSize(f);
					
				}
				return size;
			}else{
			    size = file.length()/1024.0f;//整数则计算不准确
				return size;
			}
		}else{
			Logger.v(TAG, "文件不存在");
			return 0;
		}
	}
	/**
	 * 清空指定文件夹
	 * @author com.tiantian
	 * @param relativePath
	 */
	public static void deleteFiles(File file){
		if(file == null){
			Logger.v(TAG, "文件不存在");
			return;
		}
		if(file.isFile() || file.list().length == 0){
			file.delete();
        }else{
        	 File[] files = file.listFiles();
	         for(File f : files){
	        	 deleteFiles(f);//递归删除每一个文件
	        	 f.delete();//删除该文件夹
	         }
         }
	}
    
    /**
     * 输出数据到文件
     * @param data
     * @param outpath
     * @throws CompressException
     */
	public static void writeDataToFile(byte[] data, String outpath){
        if (TextUtil.isEmpty(outpath)){
        	Logger.v(TAG, "The file path to write is blank or null. " + outpath);
        	return;
        }
        try {
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outpath));
            try {
                out.write(data);
                out.flush();
            } catch (IOException e) {
            	Logger.e(TAG, "Write file to " + outpath + " error",e);
           }finally{
             IOUtil.closeIO(out);
           }
        } catch (FileNotFoundException e) {
        	Logger.e(TAG, "Error to save data to file " + outpath, e);
        }
    }
	/**
	 * 系统打开文件
	 * @param context
	 * @param path
	 */
	public static void openFile(Context context, String path){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = getMimeType(path);
        //参数二type见下面
        intent.setDataAndType(Uri.fromFile(new File(path)), type);
        context.startActivity(intent);
    }
	
	/**
	 * 获取文件类型
	 * @param uri
	 * @return
	 */
    public static String getMimeType(String uri)
    {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(uri);
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }
    
    /**
     * 把输入流写入文件
     * @param file 要求包含全路径的文件名
     * @param stream
     * @param append
     * @return
     */
    public static boolean writeFile(File file, InputStream stream, boolean append) {
        OutputStream o = null;
        try {
        	createDirF(file.getParent());
            o = new FileOutputStream(file, append);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        } catch (FileNotFoundException e) {
        	Logger.e(TAG, "FileNotFoundException occurred", e);
        } catch (IOException e) {
        	Logger.e(TAG, "IOException occurred", e);
        } finally {
          IOUtil.closeIO(o);
          IOUtil.closeIO(stream);
        }
        return true;
    }
    
    /**
     * 拷贝文件
     * @param sourceFilePath
     * @param destFilePath
     * @return
     */
    public static boolean copyFile(String sourceFilePath, String destFilePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(sourceFilePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        }
        return writeFile(new File(destFilePath), inputStream,false);
    }
}
