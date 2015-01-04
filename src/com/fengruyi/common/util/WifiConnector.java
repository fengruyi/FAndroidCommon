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
 * wifi���Ӱ�����
 * @author fengruyi
 *
 */
public class WifiConnector {
	/*
	 * manifest����Ҫ��Ȩ��
	 * 
	 * <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"></uses-permission>
	   <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"></uses-permission>
       <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>
	 * 
	 * */
	
	private static final int WIFI_CONNECT_TIMEOUT = 20;//����wifi��ʱʱ��
	
	private Context mContext;
	private WifiManager mWifiManager;
	private Lock mLock;
	private Condition mCondition;
	private WiFiConnecctReceiver mWiFiConnecctReceiver;
	private WifiConnectListener mWifiConnectListener;
	private boolean mIsConnected = false;
	private int mNetworkID = -1;
	
	//�������ģʽ
	public enum SecurityMode{
		OPEN,WEP,WPA,WPA2
	}
	//���ӽ�������
	public interface WifiConnectListener{
		
		public void onWifiConnectCompleted(boolean isConnect);
	}
	/**
	 * ���췽��
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
	 * ����ָ��wifi
	 * @param ssid wifi���
	 * @param pass wifi����
	 * @param mode ����ģʽ
	 */
	public void connect(final String ssid,final String pass,final SecurityMode mode){
		//WIFI��������һ����ʱ�Ĺ�̣�������Ҫ�ŵ��߳���ִ��
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				//���WIFIû�д򿪣����WIFI
				if( !mWifiManager.isWifiEnabled() ) {
					mWifiManager.setWifiEnabled(true);
				}
				//ע�����ӽ��������
				mContext.registerReceiver(mWiFiConnecctReceiver, new IntentFilter(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION));
				//����ָ��SSID
				if(!onConnect(ssid, pass, mode)){
					mWifiConnectListener.onWifiConnectCompleted(false);
				}else{
					mWifiConnectListener.onWifiConnectCompleted(true);
				}
				//ɾ��ע��ļ��������
				mContext.unregisterReceiver(mWiFiConnecctReceiver);
			}
		}).start();
	}
	/**
	 * ��������wifi����
	 * @param ssid
	 * @param pass
	 * @param mode
	 * @return
	 */
	protected boolean onConnect(String ssid,String pass,SecurityMode mode){
		//����µ��������ö���
		WifiConfiguration wcfg = new WifiConfiguration();
		wcfg.SSID = "\"" + ssid +"\"";
		if(pass!=null&&!"".equals(pass)){//���벻Ϊ��
			//����ȽϹؼ������WEP���ܷ�ʽ�����磬������Ҫ�ŵ�cfg.wepKeys[0]����
			if(mode == SecurityMode.WEP){
				wcfg.wepKeys[0] = "\"" +pass+"\"";
				wcfg.wepTxKeyIndex = 0;
			}else{
				wcfg.preSharedKey = "\""+pass+"\"";
			}
		}
		wcfg.status = WifiConfiguration.Status.ENABLED;
		//�����������
		mNetworkID = mWifiManager.addNetwork(wcfg);
		
		mLock.lock();
		mIsConnected = false;
		
		if(!mWifiManager.enableNetwork(mNetworkID, true)){
			mLock.unlock();
			return false;
		}
		try {
			//�ȴ����ӽ��
			mCondition.await(WIFI_CONNECT_TIMEOUT,TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mLock.unlock();
		return mIsConnected;
	}
	//����ϵͳwifi������Ϣ�㲥
	protected class WiFiConnecctReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {	
			if(!WifiManager.SUPPLICANT_STATE_CHANGED_ACTION.equals(intent.getAction())){//�����wifi�Ĺ㲥�������κδ���
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
