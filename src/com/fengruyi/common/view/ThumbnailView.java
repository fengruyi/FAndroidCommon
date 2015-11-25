/**  
 * All rights Reserved, Designed By Android_Fengry   
 * @Title:       ThumbnailView.java   
 * @Package      com.shyz.daohang.view   
 * @Description: (点击时显示明暗变化(滤镜效果)的ImageView)   
 * @author:      Android_Fengry     
 * @date:        2015-9-2 下午5:58:45   
 * @version      V1.0     
 */  
package com.fengruyi.common.view;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class ThumbnailView extends ImageView{
	
	public ThumbnailView(Context context){
		 super(context);
	}
	
    public ThumbnailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }
    
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
       switch (event.getActionMasked()) {
       case MotionEvent.ACTION_DOWN:
           //在按下事件中设置滤镜
           setFilter();
           break;
       case MotionEvent.ACTION_UP:
           //由于捕获了Touch事件，需要手动触发Click事件
          // performClick();
       case MotionEvent.ACTION_CANCEL:
           //在CANCEL和UP事件中清除滤镜
           removeFilter();
           break;
       default:
           break;
       }
       return super.onTouchEvent(event);
    }

    /**  
     *   设置滤镜
     */
    private void setFilter() {
        //先获取设置的src图片
        Drawable drawable=getDrawable();
        //当src图片为Null，获取背景图片
        if (drawable==null) {
            drawable=getBackground();
        }
        if(drawable!=null){
            //设置滤镜
            drawable.setColorFilter(0xffdedede,PorterDuff.Mode.MULTIPLY);;
        }
    }
    /**  
     *   清除滤镜
     */
    private void removeFilter() {
        //先获取设置的src图片
        Drawable drawable=getDrawable();
        //当src图片为Null，获取背景图片
        if (drawable==null) {
            drawable=getBackground();
        }
        if(drawable!=null){
            //清除滤镜
            drawable.clearColorFilter();
        }
    }

}
