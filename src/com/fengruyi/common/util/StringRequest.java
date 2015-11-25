/**  
 * All rights Reserved, Designed By Android_Fengry   
 * @Title:       StringRequest.java   
 * @Package      com.example.volleyutil   
 * @Description: (用一句话描述该文件做什么)   
 * @author:      Android_Fengry    
 * @date:        2015-11-24 下午2:03:59   
 * @version      V1.0     
 */   
package com.fengruyi.common.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

public class StringRequest extends Request<String>
{
	    private Listener<String> mListener;
	    private Map<String,String> mParamsMap;//参数设置
	    public static final int DEFAULT_TIMEOUT_MS = 7000;//超时设置,毫秒
	    public static final int DEFAULT_MAX_RETRIES = 2;
	    public static final float DEFAULT_BACKOFF_MULT = 1f;
	    public StringRequest(int method, String url,Map<String,String> paramsMap, Listener<String> listener,
	            ErrorListener errorListener) {
	        super(method, url, errorListener);
	        setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT));
	        mListener = listener;
	        mParamsMap = paramsMap;
	    }    
	    public StringRequest(String url, Map<String,String> paramsMap ,Listener<String> listener, ErrorListener errorListener) {
	        this(Method.POST, url, paramsMap,listener, errorListener);
	    }
	   
	    protected Map<String, String> getParams() throws AuthFailureError
	    {     
	           return mParamsMap;
	    }
	    @Override
	    protected void deliverResponse(String response) {
	        if (mListener != null) {
	            mListener.onResponse(response);
	        }
	    }
	    @Override
	    protected Response<String> parseNetworkResponse(NetworkResponse response) {
	        String parsed;
	        try {
	            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
	        } catch (UnsupportedEncodingException e) {
	            parsed = new String(response.data);
	        }
	        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
	    }
}
