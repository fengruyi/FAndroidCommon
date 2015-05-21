package com.fengruyi.common.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * fragment懒加载
 * @author fengruyi
 *
 */
public abstract class LazyFragment extends Fragment {
	 protected boolean isLazyLoadCompleted; //fragment可见后是否已经执行过懒加载 
	 protected boolean isVisible;//fragment是否可见
	 protected View mContentView; //fragment装载的顶级view
	/**
	 * 可见返回true
	 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		  if(getUserVisibleHint()) { 
			    isVisible = true;
	            onVisible();  
	        } else {   
	        	isVisible = false;
	            onInvisible();  
	        }  
	    }  
	 @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    	Bundle savedInstanceState) {
    	mContentView = inflater.inflate(getContentViewId(), null); 
    	initViewAndData();
    	onVisible();
    	return mContentView;
    }
	 
	 public abstract int getContentViewId();
	    
	 public abstract void initViewAndData();
	    
	 public <T extends View> T obtainView(View convertView, int resId){
         View v = convertView.findViewById(resId);          
         return (T)v;
	 }
		
	 public <T extends View> T obtainView(int resId){
         View v = mContentView.findViewById(resId);          
         return (T)v;
	 }
	 
     protected void onVisible(){  
    	if(mContentView==null||!isVisible||isLazyLoadCompleted){	
    		return;
    	}
        lazyLoad();  	
        isLazyLoadCompleted = true;
     }  
	  
     /**
      * fragment可见执行此方法，而且只执行一次
      */
     protected abstract void lazyLoad();  
     
     /**
      * frament不可见时执行此方法 
      */
     protected void onInvisible(){}  

}
