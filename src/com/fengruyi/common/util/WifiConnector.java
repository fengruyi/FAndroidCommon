package com.fengruyi.common.util;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * wifi链接帮助类
 * @author fengruyi
 *
 */
public class WifiConnector {
	/*
	 * manifest中需要的权限
	 * 
	 * <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"></uses-permission>
	   <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"></uses-permission>
       <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>
	 * 
	 * */
	
	private static final int WIFI_CONNECT_TIMEOUT = 20;//连接wifi超时时间
	
	private Context mContext;
	private WifiManager mWifiManager;
	private Lock mLock;
	private Condition mCondition;
	private WiFiConnecctReceiver mWiFiConnecctReceiver;
	private WifiConnectListener mWifiConnectListener;
	private boolean mIsConnected = false;
	private int mNetworkID = -1;
	
	//网络加密模式
	public enum SecurityMode{
		OPEN,WEP,WPA,WPA2
	}
	//连接结果监听器
	public interface WifiConnectListener{
		
		public void onWifiConnectCompleted(boolean isConnect);
	}
	/**
	 * 构造方法
	 * @param context
	 * @param listener
	 */
	public WifiConnector(Context context,WifiConnectListener listener){
		
		mContext = context;
		mLock = new ReentrantLock();
		mCondition = mLock.newCondition();
		mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		mWiFiConnecctReceiver = new WiFiConnecctReceiver();
		mWifiConnectListener = listener;
	}
	
	/**
	 * 连接指定wifi
	 * @param ssid wifi名称
	 * @param pass wifi密码
	 * @param mode 加密模式
	 */
	public void connect(final String ssid,final String pass,final SecurityMode mode){
		//WIFI的连接是一个耗时的过程，所以需要放到线程中执行
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				//如果WIFI没有打开，则打开WIFI
				if( !mWifiManager.isWifiEnabled() ) {
					mWifiManager.setWifiEnabled(true);
				}
				//注册连接结果监听对象
				mContext.registerReceiver(mWiFiConnecctReceiver, new IntentFilter(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION));
				//连接指定SSID
				if(!onConnect(ssid, pass, mode)){
					mWifiConnectListener.onWifiConnectCompleted(false);
				}else{
					mWifiConnectListener.onWifiConnectCompleted(true);
				}
				//删除注册的监听类对象
				mContext.unregisterReceiver(mWiFiConnecctReceiver);
			}
		}).start();
	}
	/**
	 * 具体连接wifi代码
	 * @param ssid
	 * @param pass
	 * @param mode
	 * @return
	 */
	protected boolean onConnect(String ssid,String pass,SecurityMode mode){
		//添加新的网络配置对象
		WifiConfiguration wcfg = new WifiConfiguration();
		wcfg.SSID = "\"" + ssid +"\"";
		if(pass!=null&&!"".equals(pass)){//密码不为空
			//这里比较关键，如果是WEP加密方式的网络，密码需要放到cfg.wepKeys[0]里面
			if(mode == SecurityMode.WEP){
				wcfg.wepKeys[0] = "\"" +pass+"\"";
				wcfg.wepTxKeyIndex = 0;
			}else{
				wcfg.preSharedKey = "\""+pass+"\"";
			}
		}
		wcfg.status = WifiConfiguration.Status.ENABLED;
		//添加网络配置
		mNetworkID = mWifiManager.addNetwork(wcfg);
		
		mLock.lock();
		mIsConnected = false;
		
		if(!mWifiManager.enableNetwork(mNetworkID, true)){
			mLock.unlock();
			return false;
		}
		try {
			//等待连接结果
			mCondition.await(WIFI_CONNECT_TIMEOUT,TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mLock.unlock();
		return mIsConnected;
	}
	//监听系统wifi连接消息广播
	protected class WiFiConnecctReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {	
			if(!WifiManager.SUPPLICANT_STATE_CHANGED_ACTION.equals(intent.getAction())){//如果不是wifi的广播，则不做任何处理
				return;
			}
			
			mLock.lock();
			
			WifiInfo info = mWifiManager.getConnectionInfo();
			if ( info.getNetworkId()==mNetworkID && info.getSupplicantState() == SupplicantState.COMPLETED ) {
				mIsConnected = true;
				mCondition.signalAll();   
				
			}
			mLock.unlock();
		}	
	}
}
