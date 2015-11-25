/**  
 * All rights Reserved, Designed By Android_Fengry   
 * @Title:       APIHelper.java   
 * @Package      com.example.volleyutil   
 * @Description: (volley框架的封装)   
 * @author:      Android_Fengry    
 * @date:        2015-11-24 下午2:53:59   
 * @version      V1.0     
 */   
package com.fengruyi.common.util;

import java.util.Map;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;

public class APIHelper
{	
	private volatile static RequestQueue  requestQueue;
	
	public static RequestQueue getQueue(Context context) {
		if (requestQueue == null) {
			synchronized (APIHelper.class) {
				if (requestQueue == null) {
					requestQueue = Volley.newRequestQueue(context.getApplicationContext());
				}
			}
		}
		return requestQueue;
	}
	public static <T> void sendRequest(Context context,String url,Map<String,String> paramsMap,final APICallback<T> callbak,final TypeReference typeReference){
		StringRequest stringRequest = new StringRequest(url,paramsMap ,new Listener<String>()
		{
			public void onResponse(String arg0)
			{	
				try
				{	
					@SuppressWarnings("unchecked")
					T t = (T) JSON.parseObject(arg0, typeReference);
					if(callbak!=null)
					{
						callbak.onSuccess(t);
					}
				} catch (Exception e)
				{
					callbak.onFailure(e.toString());
				}
				
			}
		}, new ErrorListener()
		{
			public void onErrorResponse(VolleyError arg0)
			{
				if(callbak!=null)
				{
					callbak.onFailure(arg0.getMessage());
				}
			}
		});
		stringRequest.setTag(context);
		getQueue(context).add(stringRequest);
	}
	
	public static <T> void sendRequest(Context context,String url,final APICallback<T> callbak,final TypeReference typeReference){
		sendRequest(context, url, null, callbak, typeReference);
	}
	public static <T, K> void sendRequest(Context context,String url,Map<String,String> paramsMap,final APICallback<T> callbak,final Class<K> clazz){
		StringRequest stringRequest = new StringRequest(url,paramsMap ,new Listener<String>()
		{
			public void onResponse(String arg0)
			{	
				try
				{	
					@SuppressWarnings("unchecked")
					T t = (T) JSON.parseObject(arg0, clazz);
					if(callbak!=null)
					{
						callbak.onSuccess(t);
					}
				} catch (Exception e)
				{
					callbak.onFailure(e.toString());
				}
				
			}
		}, new ErrorListener()
		{
			public void onErrorResponse(VolleyError arg0)
			{
				if(callbak!=null)
				{
					callbak.onFailure(arg0.getMessage());
				}
			}
		});
		stringRequest.setTag(context);
		getQueue(context).add(stringRequest);
	}
	
	public static <T, K> void sendRequest(Context context,String url,final APICallback<T> callbak,final Class<K> clazz){
		sendRequest(context, url, null, callbak, clazz);
	}
	
}
