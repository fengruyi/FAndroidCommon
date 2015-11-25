/**  
 * All rights Reserved, Designed By Android_Fengry   
 * @Title:       I_SkipActivity.java   
 * @Package      com.fengruyi.common.base   
 * @Description: (规范Activity跳转的接口协议)   
 * @author:      Android_Fengry    
 * @date:        2015-11-24 上午10:48:34   
 * @version      V1.0     
 */   
package com.fengruyi.common.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public interface I_SkipActivity
{
	 /**
     * skip to @param(cls)，and call @param(aty's) finish() method
     */
    public void skipActivity(Activity aty, Class<?> cls);

    /**
     * skip to @param(cls)，and call @param(aty's) finish() method
     */
    public void skipActivity(Activity aty, Intent it);

    /**
     * skip to @param(cls)，and call @param(aty's) finish() method
     */
    public void skipActivity(Activity aty, Class<?> cls, Bundle extras);

    /**
     * show a @param(cls)，but can't finish activity
     */
    public void showActivity(Activity aty, Class<?> cls);

    /**
     * show a @param(cls)，but can't finish activity
     */
    public void showActivity(Activity aty, Intent it);

    /**
     * show a @param(cls)，but can't finish activity
     */
    public void showActivity(Activity aty, Class<?> cls, Bundle extras);
}
