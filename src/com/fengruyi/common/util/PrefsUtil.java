package com.fengruyi.common.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;

/**
 * android preference util
 * @author fengruyi
 *
 */
public class PrefsUtil {
	
    private static PrefsUtil prefsUtil;
    public Context context;
    public SharedPreferences prefs;
    public SharedPreferences.Editor editor;

    public synchronized static PrefsUtil getInstance(){
        return prefsUtil;
    }

    public static void init(Context context, String prefsname, int mode) {
        prefsUtil = new PrefsUtil();
        prefsUtil.context = context;
        prefsUtil.prefs = prefsUtil.context.getSharedPreferences(prefsname, mode);
        prefsUtil.editor = prefsUtil.prefs.edit();
    }

    private PrefsUtil() {
    }

    public boolean getBoolean(String key, boolean defaultVal){
        return this.prefs.getBoolean(key, defaultVal);
    }
    public boolean getBoolean(String key){
        return this.prefs.getBoolean(key, false);
    }


    public String getString(String key, String defaultVal){
        return this.prefs.getString(key, defaultVal);
    }
    public String getString(String key){
        return this.prefs.getString(key, null);
    }

    public int getInt(String key, int defaultVal){
        return this.prefs.getInt(key, defaultVal);
    }
    public int getInt(String key){
        return this.prefs.getInt(key, 0);
    }


    public float getFloat(String key, float defaultVal){
        return this.prefs.getFloat(key, defaultVal);
    }
    public float getFloat(String key){
        return this.prefs.getFloat(key, 0f);
    }

    public long getLong(String key, long defaultVal){
        return this.prefs.getLong(key, defaultVal);
    }
    public long getLong(String key){
        return this.prefs.getLong(key, 0l);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Set<String> getStringSet(String key, Set<String> defaultVal){
        return this.prefs.getStringSet(key, defaultVal);
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Set<String> getStringSet(String key){
        return this.prefs.getStringSet(key, null);
    }

    public Map<String, ?> getAll(){
        return this.prefs.getAll();
    }

    public PrefsUtil putString(String key, String value){
        editor.putString(key, value);
//        editor.commit();
        return this;
    }

    public PrefsUtil putInt(String key, int value){
        editor.putInt(key, value);
//        editor.commit();
        return this;
    }

    public PrefsUtil putFloat(String key, float value){
        editor.putFloat(key, value);
//        editor.commit();
        return this;
    }

    public PrefsUtil putLong(String key, long value){
        editor.putLong(key, value);
//        editor.commit();
        return this;
    }

    public PrefsUtil putBoolean(String key, boolean value){
        editor.putBoolean(key, value);
//        editor.commit();
        return this;
    }
    
    public PrefsUtil putObject(String key, Object object){
    	if(object == null){
			throw new IllegalArgumentException("object is null");
		}
		
		if(key.equals("") || key == null){
			throw new IllegalArgumentException("key is empty or null");
		}
		editor.putString(key, JSON.toJSONString(object));
//        editor.commit();
        return this;
    }
    public <T> T getObject(String key, Class<T> a) {
    	
		String json = prefs.getString(key, null);
		if (json == null) {
			return null;
		} else {
			try{
				return JSON.parseObject(json, a);
			} catch (Exception e) {
				throw new IllegalArgumentException("Object storaged with key " + key + " is instanceof other class");				
			}
		}
	}
    public void commit(){
        editor.commit();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public PrefsUtil putStringSet(String key, Set<String> value){
        editor.putStringSet(key, value);
        editor.commit();
        return this;
    }

}
