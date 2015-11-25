/**  
 * All rights Reserved, Designed By Android_Fengry   
 * @Title:       APICallback.java   
 * @Package      com.example.volleyutil   
 * @Description: (用一句话描述该文件做什么)   
 * @author:      Android_Fengry    
 * @date:        2015-11-24 下午2:55:54   
 * @version      V1.0     
 */   
package com.fengruyi.common.util;

public interface APICallback<T>
{
	public void onSuccess(T t);
	
	public void onFailure(String errMsg);
}
