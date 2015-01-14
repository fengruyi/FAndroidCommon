package com.fengruyi.common.util;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.fengruyi.common.log.Logger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.AuthAlgorithm;
import android.net.wifi.WifiConfiguration.KeyMgmt;
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
	private ConnectivityManager mConnectivityManager;
	private WifiManager mWifiManager;
	private Lock mLock;
	private Condition mCondition;
	private WiFiConnecctReceiver mWiFiConnecctReceiver;
	private WifiConnectListener mWifiConnectListener;
	private boolean mIsConnected = false;
	private int mNetworkID = -1;
	private String mSSID ;
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
		mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
		mSSID = ssid;
		//WIFI的连接是一个耗时的过程，所以需要放到线程中执行
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				//如果WIFI没有打开，则打开WIFI
				if( !mWifiManager.isWifiEnabled() ) {
					mWifiManager.setWifiEnabled(true);
				}
				//注册连接结果监听对象
				mContext.registerReceiver(mWiFiConnecctReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
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
		mIsConnected = false;
		//添加新的网络配置对象
		WifiConfiguration wcfg = new WifiConfiguration();
		wcfg.SSID = "\"" + ssid +"\"";
		if(pass!=null&&!"".equals(pass)){//密码不为空
			//这里比较关键，如果是WEP加密方式的网络，密码需要放到cfg.wepKeys[0]里面
			if(mode == SecurityMode.WEP){
				wcfg.wepKeys[0] = "\"" +pass+"\"";
				wcfg.wepTxKeyIndex = 0;
				wcfg.allowedAuthAlgorithms.set(AuthAlgorithm.OPEN);
				wcfg.allowedAuthAlgorithms.set(AuthAlgorithm.SHARED);
				wcfg.allowedKeyManagement.set(KeyMgmt.NONE);
				wcfg.wepTxKeyIndex = 0;
			}else{
				wcfg.preSharedKey = "\""+pass+"\"";
				wcfg.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
				wcfg.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
				wcfg.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
				wcfg.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
				// 此处需要修改否则不能自动重联
				// config.allowedProtocols.set(WifiConfiguration.Protocol.WPA); 
				wcfg.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
				wcfg.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
			}
		}
		wcfg.status = WifiConfiguration.Status.ENABLED;
		//添加网络配置
		mNetworkID = mWifiManager.addNetwork(wcfg);
		
		mLock.lock();
		//mIsConnected = false;
		
		if(!mWifiManager.enableNetwork(mNetworkID, true)){
		//	mLock.unlock();
			//mIsConnected = false;
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
	// 查看以前是否也配置过这个网络
	private WifiConfiguration isExsits(String SSID) {
		List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
		for (WifiConfiguration existingConfig : existingConfigs) {
		if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
		return existingConfig;
		}
	}
	return null;
	}
	//监听系统wifi连接消息广播
	protected class WiFiConnecctReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {	
			
			mLock.lock();
			
			NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
			if(netInfo!=null&&netInfo.isConnected()&&netInfo.getType()==ConnectivityManager.TYPE_WIFI){
				WifiInfo info = mWifiManager.getConnectionInfo();
				//if ( info.getNetworkId()==mNetworkID && info.getSupplicantState() == SupplicantState.COMPLETED ) {
					Logger.e("", "SSID:-->"+info.getSSID()+"-->参数：SSID-->"+mSSID);
					if(mSSID!=null&&info.getSSID().equals("\"" + mSSID + "\"")){
						Logger.e("", "我已经连上了");
						mIsConnected = true;
						mCondition.signalAll();   	
					}
				
			}
			mLock.unlock();
		}	
	}
}
