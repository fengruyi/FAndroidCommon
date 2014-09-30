package com.fengruyi.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.fengruyi.common.log.Logger;



/**
 * 时间处理工具
 * @author fengruyi
 *
 */
public class TimeUtil {
	
		public static Calendar CALENDAR = Calendar.getInstance();
		public static final String TAG = TimeUtil.class.getSimpleName();
	
		/**
		 * 把一个毫秒数转化成时间字符串。格式为小时/分/秒/毫秒（如：24903600 --> 06小时55分03秒600毫秒）。
		 * @author fengruyi
		 * @param millis 要转化的毫秒数。
		 * @param isWhole 是否强制全部显示小时/分/秒/毫秒。
		 * @param isFormat 时间数字是否要格式化，如果true：少位数前面补全；如果false：少位数前面不补全。
		 * @return 返回时间字符串：小时/分/秒/毫秒的格式（如：24903600 --> 06小时55分03秒600毫秒）。
		 */
		public static String millisToString(long millis, boolean isWhole, boolean isFormat){
			String h = "";
			String m = "";
			String s = "";
			String mi = "";
			if(isWhole){
				h = isFormat ? "00小时" : "0小时";
				m = isFormat ? "00分" : "0分";
				s = isFormat ? "00秒" : "0秒";
				mi = isFormat ? "00毫秒" : "0毫秒";
			}
			
			long temp = millis;
			
			long hper = 60 * 60 * 1000;
			long mper = 60 * 1000;
			long sper = 1000;
			
			if(temp / hper > 0){
				if(isFormat){
					h = temp / hper < 10 ? "0" + temp / hper : temp / hper + "";
				}else{
					h = temp / hper + "";
				}
				h += "小时";
			}
			temp = temp % hper;
			
			if(temp / mper > 0){
				if(isFormat){
					m = temp / mper < 10 ? "0" + temp / mper : temp / mper + "";
				}else{
					m = temp / mper + "";
				}
				m += "分";
			}
			temp = temp % mper;
			
			if(temp / sper > 0){
				if(isFormat){
					s = temp / sper < 10 ? "0" + temp / sper : temp / sper + "";
				}else{
					s = temp / sper + "";
				}
				s += "秒";
			}
			temp = temp % sper;
			mi = temp + "";
			
			if(isFormat){
				if(temp < 100 && temp >= 10){
					mi = "0" + temp;
				}
				if(temp < 10){
					mi = "00" + temp;
				}
			}
			
			mi += "毫秒";
			return h + m + s + mi;
		}
		
	    /**
	     * 把一个毫秒数转化成时间字符串。格式为小时/分/秒/毫秒（如：24903600 --> 06小时55分钟）。
	     * @author fengruyi
	     * @param millis 要转化的毫秒数。
	     * @param isWhole 是否强制全部显示小时/分。
	     * @param isFormat 时间数字是否要格式化，如果true：少位数前面补全；如果false：少位数前面不补全。
	     * @return 返回时间字符串：小时/分/秒/毫秒的格式（如：24903600 --> 06小时55分钟）。
	     */
	    public static String millisToStringShort(long millis, boolean isWhole, boolean isFormat){
	        String h = "";
	        String m = "";
	        if(isWhole){
	            h = isFormat ? "00小时" : "0小时";
	            m = isFormat ? "00分钟" : "0分钟";
	        }

	        long temp = millis;

	        long hper = 60 * 60 * 1000;
	        long mper = 60 * 1000;
	        long sper = 1000;

	        if(temp / hper > 0){
	            if(isFormat){
	                h = temp / hper < 10 ? "0" + temp / hper : temp / hper + "";
	            }else{
	                h = temp / hper + "";
	            }
	            h += "小时";
	        }
	        temp = temp % hper;

	        if(temp / mper > 0){
	            if(isFormat){
	                m = temp / mper < 10 ? "0" + temp / mper : temp / mper + "";
	            }else{
	                m = temp / mper + "";
	            }
	            m += "分钟";
	        }

	        return h + m;
	    }

		/**
		 * 把日期毫秒转化为字符串。默认格式：yyyy-MM-dd HH:mm:ss。
		 * @author fengruyi
		 * @param millis 要转化的日期毫秒数。
		 * @return 返回日期字符串（如："2013-02-19 11:48:31"）。
		 */
		public static String millisToStringDate(long millis){
			return millisToStringDate(millis, "yyyy-MM-dd HH:mm:ss");
		}
		
		/**
		 * 把日期毫秒转化为字符串。
		 * @author fengruyi
		 * @param millis 要转化的日期毫秒数。
		 * @param pattern 要转化为的字符串格式（如：yyyy-MM-dd HH:mm:ss）。
		 * @return 返回日期字符串。
		 */
		public static String millisToStringDate(long millis, String pattern){
			SimpleDateFormat format =  new SimpleDateFormat(pattern);
			return format.format(new Date(millis));
			
		}

	    /**
	     * 把日期毫秒转化为字符串（文件名）。
	     * @author fengruyi
	     * @param millis 要转化的日期毫秒数。
	     * @param pattern 要转化为的字符串格式（如：yyyy-MM-dd HH:mm:ss）。
	     * @return 返回日期字符串（yyyy_MM_dd_HH_mm_ss）。
	     */
	    public static String millisToStringFilename(long millis, String pattern){
	        String dateStr = millisToStringDate(millis, pattern);
	        return dateStr.replaceAll("[- :]", "_");
	    }

	    /**
	     * 把日期毫秒转化为字符串（文件名）。
	     * @author fengruyi
	     * @param millis 要转化的日期毫秒数。
	     * @return 返回日期字符串（yyyy_MM_dd_HH_mm_ss）。
	     */
	    public static String millisToStringFilename(long millis){
	        String dateStr = millisToStringDate(millis, "yyyy-MM-dd HH:mm:ss");
	        return dateStr.replaceAll("[- :]", "_");
	    }



	    public static long oneHourMillis = 60 * 60 * 1000; // 一小时的毫秒数
	    public static long oneDayMillis = 24 * oneHourMillis; // 一天的毫秒数
	    public static long oneYearMillis = 365 * oneDayMillis; // 一年的毫秒数

	    /**
	     * 时间格式：
	     * 1小时内用，多少分钟前；
	     * 超过1小时，显示时间而无日期；
	     * 如果是昨天，则显示昨天
	     * 超过昨天再显示日期；
	     * 超过1年再显示年。
	     * @param millis
	     * @return
	     */
	    public static String millisToLifeString(long millis){
	        long now = System.currentTimeMillis();
	        long todayStart = string2Millis(millisToStringDate(now, "yyyy-MM-dd"), "yyyy-MM-dd");

	        if(now - millis <= oneHourMillis && now - millis > 0l){ // 一小时内
	            String m = millisToStringShort(now - millis, false, false);
	            return "".equals(m) ? "1分钟内" : m + "前";
	        }

	        if(millis >= todayStart && millis <= oneDayMillis + todayStart){ // 大于今天开始开始值，小于今天开始值加一天（即今天结束值）
	            return "今天 " + millisToStringDate(millis, "HH:mm");
	        }

	        if(millis > todayStart - oneDayMillis){ // 大于（今天开始值减一天，即昨天开始值）
	            return "昨天 " + millisToStringDate(millis, "HH:mm");
	        }

	        long thisYearStart = string2Millis(millisToStringDate(now, "yyyy"), "yyyy");
	        if(millis > thisYearStart){ // 大于今天小于今年
	            return millisToStringDate(millis, "MM月dd日 HH:mm");
	        }

	        return millisToStringDate(millis, "yyyy年MM月dd日 HH:mm");
	    }

	    /**
	     * 时间格式：
	     * 今天，显示时间而无日期；
	     * 如果是昨天，则显示昨天
	     * 超过昨天再显示日期；
	     * 超过1年再显示年。
	     * @param millis
	     * @return
	     */
	    public static String millisToLifeString2(long millis){
	        long now = System.currentTimeMillis();
	        long todayStart = string2Millis(millisToStringDate(now, "yyyy-MM-dd"), "yyyy-MM-dd");

	        if(millis > todayStart + oneDayMillis && millis < todayStart + 2 * oneDayMillis){ // 明天
	            return "明天" + millisToStringDate(millis, "HH:mm");
	        }
	        if(millis > todayStart + 2 * oneDayMillis && millis < todayStart + 3 * oneDayMillis){ // 后天
	            return "后天" + millisToStringDate(millis, "HH:mm");
	        }

	        if(millis >= todayStart && millis <= oneDayMillis + todayStart){ // 大于今天开始开始值，小于今天开始值加一天（即今天结束值）
	            return "今天 " + millisToStringDate(millis, "HH:mm");
	        }

	        if(millis > todayStart - oneDayMillis && millis < todayStart){ // 大于（今天开始值减一天，即昨天开始值）
	            return "昨天 " + millisToStringDate(millis, "HH:mm");
	        }

	        long thisYearStart = string2Millis(millisToStringDate(now, "yyyy"), "yyyy");
	        if(millis > thisYearStart){ // 大于今天小于今年
	            return millisToStringDate(millis, "MM月dd日 HH:mm");
	        }

	        return millisToStringDate(millis, "yyyy年MM月dd日 HH:mm");
	    }

	    /**
	     * 时间格式：
	     * 今天，显示时间而无日期；
	     * 如果是昨天，则显示昨天
	     * 超过昨天再显示日期；
	     * 超过1年再显示年。
	     * @param millis
	     * @return
	     */
	    public static String millisToLifeString3(long millis){
	        long now = System.currentTimeMillis();
	        long todayStart = string2Millis(millisToStringDate(now, "yyyy-MM-dd"), "yyyy-MM-dd");

	        if(millis > todayStart + oneDayMillis && millis < todayStart + 2 * oneDayMillis){ // 明天
	            return "明天";
	        }
	        if(millis > todayStart + 2 * oneDayMillis && millis < todayStart + 3 * oneDayMillis){ // 后天
	            return "后天";
	        }

	        if(millis >= todayStart && millis <= oneDayMillis + todayStart){ // 大于今天开始开始值，小于今天开始值加一天（即今天结束值）
	            return millisToStringDate(millis, "HH:mm");
	        }

	        if(millis > todayStart - oneDayMillis && millis < todayStart){ // 大于（今天开始值减一天，即昨天开始值）
	            return "昨天 ";
	        }

	        return millisToStringDate(millis, "MM月dd日");
	    }

	    /**
	     * 字符串解析成毫秒数
	     * @param str
	     * @param pattern
	     * @return
	     */
	    public static long string2Millis(String str, String pattern){
	        SimpleDateFormat format = new SimpleDateFormat(pattern);
	        long millis = 0;
	        try {
	            millis = format.parse(str).getTime();
	        } catch (ParseException e) {
	            Logger.e(TAG, e);
	        }
	        return millis;
	    }

	    /**
	     * 获得今天开始的毫秒值
	     * @return
	     */
	    public static long getTodayStartMillis(){
	        return getOneDayStartMillis(System.currentTimeMillis());
	    }

	    public static long getOneDayStartMillis(long millis){
	        String dateStr = millisToStringDate(millis, "yyyy-MM-dd");
	        return string2Millis(dateStr, "yyyy-MM-dd");
	    }

	/**
	 * 获得星期几
	 * 
	 * @return 星期几
	 */
	public static String getWeek() {
		String str = "";
		switch (CALENDAR.get(Calendar.DAY_OF_WEEK)) {
		case 1:
			str = "星期日 ";
			break;
		case 2:
			str = "星期一 ";
			break;
		case 3:
			str = "星期二 ";
			break;
		case 4:
			str = "星期三 ";
			break;
		case 5:
			str = "星期四 ";
			break;
		case 6:
			str = "星期五 ";
			break;
		default:
			str = "星期六 ";
			break;
		}
		return str;
	}
	
	/**
	 * 根据给定时间获得星期几
	 * 
	 * @param dateTime
	 * @return
	 * @author OLH
	 * @date 2013-9-10 下午3:45:50
	 */
	public static String getWeek(long dateTime) {
		String str = "";
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(dateTime);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		switch (dayOfWeek) {
		case 1:
			str = "星期日 ";
			break;
		case 2:
			str = "星期一 ";
			break;
		case 3:
			str = "星期二 ";
			break;
		case 4:
			str = "星期三 ";
			break;
		case 5:
			str = "星期四 ";
			break;
		case 6:
			str = "星期五 ";
			break;
		default:
			str = "星期六 ";
			break;
		}
		return str;
	}

	/**
	 * 获取当前小时数
	 * 
	 * @return int
	 */
	public static int getHour() {
		return CALENDAR.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取分钟数
	 * 
	 * @return int
	 */
	public static int getMin() {
		return CALENDAR.get(Calendar.MINUTE);
	}
	
	/**
	 * 当前时间毫秒数
	 * @return
	 */
	public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }
}
