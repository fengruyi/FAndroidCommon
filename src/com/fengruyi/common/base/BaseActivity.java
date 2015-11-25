package com.fengruyi.common.base;
import com.fengruyi.common.util.AppManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


/**
 * activity基类，所有的activity需要继承它
 * @author fengruyi
 * 
 */
public abstract class BaseActivity extends Activity implements I_SkipActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppManager.getAppManager().addActivity(this);
		setContentView(getContentViewId());
		initViewAndData();
	}
	/**
	 * 
	 * @return 返回xml id
	 */
	public abstract int getContentViewId();
	/**
	 * 实例化组件和数据
	 */
	public abstract void initViewAndData();
	
	/**
	 * 简化findviewbyid转型
	 * @param resId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T obtainView(int resId){
        View v = findViewById(resId);          
        return (T)v;
	}
	
	/**
     * skip to @param(cls)，and call @param(aty's) finish() method
     */
    @Override
    public void skipActivity(Activity aty, Class<?> cls) {
        showActivity(aty, cls);
        aty.finish();
    }

    /**
     * skip to @param(cls)，and call @param(aty's) finish() method
     */
    @Override
    public void skipActivity(Activity aty, Intent it) {
        showActivity(aty, it);
        aty.finish();
    }

    /**
     * skip to @param(cls)，and call @param(aty's) finish() method
     */
    @Override
    public void skipActivity(Activity aty, Class<?> cls, Bundle extras) {
        showActivity(aty, cls, extras);
        aty.finish();
    }

    /**
     * show to @param(cls)，but can't finish activity
     */
    @Override
    public void showActivity(Activity aty, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(aty, cls);
        aty.startActivity(intent);
    }

    /**
     * show to @param(cls)，but can't finish activity
     */
    @Override
    public void showActivity(Activity aty, Intent it) {
        aty.startActivity(it);
    }

    /**
     * show to @param(cls)，but can't finish activity
     */
    @Override
    public void showActivity(Activity aty, Class<?> cls, Bundle extras) {
        Intent intent = new Intent();
        intent.putExtras(extras);
        intent.setClass(aty, cls);
        aty.startActivity(intent);
    }
}
