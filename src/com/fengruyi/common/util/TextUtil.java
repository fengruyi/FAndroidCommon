package com.fengruyi.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.fengruyi.common.log.Logger;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;


public class TextUtil {
	 public static final String TAG = TextUtil.class.getSimpleName();
	    /**
	     * 获得字体的缩放密度
	     *
	     * @param context
	     * @return
	     */
	    public static float getScaledDensity(Context context) {
	        DisplayMetrics dm = context.getResources().getDisplayMetrics();
	        return dm.scaledDensity;
	    }

	    /**
	     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	     */
	    public static int dip2px(Context context, float dpValue) {
	        final float scale = context.getResources().getDisplayMetrics().density;
	        return (int) (dpValue * scale + 0.5f);
	    }

	    /**
	     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	     */
	    public static int px2dip(Context context, float pxValue) {
	        final float scale = context.getResources().getDisplayMetrics().density;
	        return (int) (pxValue / scale + 0.5f);
	    }


	    /**
	     * 将px值转换为sp值，保证文字大小不变
	     *
	     * @param pxValue
	     * @return
	     */
	    public static int px2sp(Context context, float pxValue) {
	        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
	        return (int) (pxValue / fontScale + 0.5f);
	    }

	    /**
	     * 将sp值转换为px值，保证文字大小不变
	     *
	     * @param spValue
	     * @return
	     */
	    public static int sp2px(Context context, float spValue) {
	        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
	        return (int) (spValue * fontScale + 0.5f);
	    }


	    /**
	     * 一些是否为空判断
	     */

	    public static boolean isEmpty(Collection collection) {
	        return null == collection || collection.isEmpty();
	    }

	    public static boolean isEmpty(Map map) {
	        return null == map || map.isEmpty();
	    }

	    public static boolean isEmpty(Object[] objs) {
	        return null == objs || objs.length <= 0;
	    }

	    public static boolean isEmpty(CharSequence charSequence) {
	        return null == charSequence || charSequence.length() <= 0;
	    }
	    
	    /**
	     * 判断两个对象是否相等
	     * @param actual
	     * @param expected
	     * @return
	     */
	    public static boolean isEquals(Object actual, Object expected) {
	        return actual == expected || (actual == null ? expected == null : actual.equals(expected));
	    }
       

	    /**
	     * 替换文本为图片
	     *
	     * @param charSequence
	     * @param regPattern
	     * @param drawable
	     * @return
	     */
	    public static SpannableString replaceImageSpan(CharSequence charSequence, String regPattern, Drawable drawable) {
	        SpannableString ss = charSequence instanceof SpannableString ? (SpannableString) charSequence : new SpannableString(charSequence);
	        try {
	            ImageSpan is = new ImageSpan(drawable);
	            Pattern pattern = Pattern.compile(regPattern);
	            Matcher matcher = pattern.matcher(ss);
	            while (matcher.find()) {
	                String key = matcher.group();
	                ss.setSpan(is, matcher.start(), matcher.start() + key.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
	            }
	        } catch (Exception ex) {
	            Logger.e(TAG, ex);
	        }

	        return ss;
	    }


	    /**
	     * 压缩字符串到Zip
	     *
	     * @param str
	     * @return 压缩后字符串
	     * @throws IOException
	     */
	    public static String compress(String str) throws IOException {
	        if (str == null || str.length() == 0) {
	            return str;
	        }
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        GZIPOutputStream gzip = new GZIPOutputStream(out);
	        gzip.write(str.getBytes());
	        gzip.close();
	        return out.toString("ISO-8859-1");
	    }

	    /**
	     * 解压Zip字符串
	     *
	     * @param str
	     * @return 解压后字符串
	     * @throws IOException
	     */
	    public static String uncompress(String str) throws IOException {
	        if (str == null || str.length() == 0) {
	            return str;
	        }
	        ByteArrayInputStream in = new ByteArrayInputStream(str
	                .getBytes("UTF-8"));
	        return uncompress(in);
	    }

	    /**
	     * 解压Zip字符串
	     *
	     * @param inputStream
	     * @return 解压后字符串
	     * @throws IOException
	     */
	    public static String uncompress(InputStream inputStream) throws IOException {
	        if (inputStream == null) {
	            return null;
	        }
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        GZIPInputStream gunzip = new GZIPInputStream(inputStream);
	        byte[] buffer = new byte[256];
	        int n;
	        while ((n = gunzip.read(buffer)) >= 0) {
	            out.write(buffer, 0, n);
	        }
	        return out.toString();
	    }
	    
	    /**
	     * 字符编码
	     * @param str
	     * @return
	     */
	    public static String utf8Encode(String str) {
	        if (!isEmpty(str) && str.getBytes().length != str.length()) {
	            try {
	                return URLEncoder.encode(str, "UTF-8");
	            } catch (UnsupportedEncodingException e) {
	                throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
	            }
	        }
	        return str;
	    }
		/**
		 * Used to build output as Hex
		 */
		private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
		        									'e', 'f' };
	    /**
	     * encode By MD5
	     * 
	     * @param str
	     * @return String
	     */
	    public static String md5(String str) {
	        if (str == null) {
	            return null;
	        }
	        try {
	            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
	            messageDigest.update(str.getBytes());
	            return new String(encodeHex(messageDigest.digest()));
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }
	    protected static char[] encodeHex(final byte[] data) {
	        final int l = data.length;
	        final char[] out = new char[l << 1];
	        // two characters form the hex value.
	        for (int i = 0, j = 0; i < l; i++) {
	            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
	            out[j++] = DIGITS_LOWER[0x0F & data[i]];
	        }
	        return out;
	    }
}
