package com.example.testapp;

import java.io.IOException;
import android.net.ConnectivityManager;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.provider.Settings.Global;
import android.provider.Settings;
import android.content.Context;
import android.content.ContentResolver;
import android.telephony.TelephonyManager;
import android.os.CountDownTimer;
import android.net.wifi.WifiManager;
import android.view.WindowManager;
import android.bluetooth.BluetoothAdapter;
import android.view.Window;
import android.net.wifi.WifiInfo;
import android.app.AlarmManager;
import android.content.Intent;
import android.app.PendingIntent;
import android.os.SystemClock;

import android.view.KeyEvent;
import java.io.IOException;
import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;
import android.util.Log;

import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.RIL;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.CommandsInterface;

import static android.provider.Settings.Global.PREFERRED_NETWORK_MODE;

public class MainActivity extends Activity {
    private ConnectivityManager mCM;
    private WifiManager mWM;
    private TelephonyManager mTM;
    private BluetoothAdapter mBT;
    private PowerManager mPM;
    private WifiInfo mWifiInfo;
    private AlarmManager AM;  
    private Intent intent;
    //public CommandsInterface mCi;
    //private Phone mPhone;	
    //private RIL mRil;
    private Phone mPhone;
    int i;
    //private static Phone mPhone;
    //private static MyHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCM = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
	mWM = (WifiManager)getSystemService(WIFI_SERVICE);
	mBT = BluetoothAdapter.getDefaultAdapter();
	mPM = (PowerManager)getSystemService(POWER_SERVICE);
	AM = (AlarmManager) getSystemService(ALARM_SERVICE);
	//mTM = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
	//RIL mRil = new RIL(this);	
	//mPhone = PhoneGlobals.getPhone();
	//mPhone = PhoneGlobals.getPhone();
 	//mHandler = new MyHandler();
        addButtonClickListener();
    }
    
    public void addButtonClickListener(){
    	Button start = (Button) findViewById(R.id.button1);
    	start.setOnClickListener(new OnClickListener(){
    		public void onClick(View arg){

	mCM.setAirplaneMode(true);


	for(i=0;i<=1;i++){
        Intent intent = new Intent(MainActivity.this, BrowsingReciever.class);
	Bundle bundle = new Bundle();
	bundle.putInt("id", i);
	intent.putExtras(bundle);
        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, i, intent, 0);
        long start_time = SystemClock.elapsedRealtime();
	AM.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, start_time +(i+1)*60000, pi);
	}


//                int networkType = Phone.NT_MODE_GSM_ONLY;
 //               mPhone = PhoneFactory.getDefaultPhone();		
//	        android.provider.Settings.Global.putInt(mPhone.getContext().getContentResolver(),android.provider.Settings.Global.PREFERRED_NETWORK_MODE,networkType);
//		mPhone.setPreferredNetworkType(networkType, null);
/*
//1st Timer to do Flight Mode Suspend~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
 new CountDownTimer(60000,1000){
		@Override
		pinActivityublic void onFinish() {
                PowerManager.WakeLock wl = mPM.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "bright");
                wl.acquire();
		mCM.setAirplaneMode(false);
	        int networkType = Phone.NT_MODE_GSM_ONLY;                
		mPhone = PhoneFactory.getDefaultPhone();
                android.provider.Settings.Global.putInt(mPhone.getContext().getContentResolver(),android.provider.Settings.Global.PREFERRED_NETWORK_MODE,networkType);
                mPhone.setPreferredNetworkType(networkType, null);
		wl.release();

//2nd timer to do 2G data disable+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 new CountDownTimer(60000,1000){
                @Override
                public void onFinish() {
		PowerManager.WakeLock wl = mPM.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "bright");
                wl.acquire();
		mCM.setMobileDataEnabled(true);
		wl.release();

//3rd timer to do 2G data enable///////////////////////////////////////////////////////////////////////////////////////////////                                        
 new CountDownTimer(60000,1000){
                @Override
                public void onFinish() {
		PowerManager.WakeLock wl = mPM.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "bright");
                wl.acquire();
                int networkType = Phone.NT_MODE_WCDMA_PREF;                
		mPhone = PhoneFactory.getDefaultPhone();
                android.provider.Settings.Global.putInt(mPhone.getContext().getContentResolver(),android.provider.Settings.Global.PREFERRED_NETWORK_MODE,networkType);
                mPhone.setPreferredNetworkType(networkType, null);
                wl.release();
//4rd timer to do 3G data enable~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~					
 new CountDownTimer(60000,1000){
                @Override
                public void onFinish() {
	        PowerManager.WakeLock wl = mPM.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "bright");
                wl.acquire();
                mCM.setMobileDataEnabled(false);
                wl.release();
//5rd timer @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
 new CountDownTimer(60000,1000){
                @Override
                public void onFinish() {
		PowerManager.WakeLock wl = mPM.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "bright");
                wl.acquire();
                mCM.setAirplaneMode(true);
	        mWM.setWifiEnabled(true);
                wl.release();

//6rd timer ##########################################################################################################################
 new CountDownTimer(60000,1000){
                @Override
                public void onFinish() {
                PowerManager.WakeLock wl = mPM.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "bright");
                wl.acquire();
                mCM.setAirplaneMode(false);
		mCM.setMobileDataEnabled(true);
                wl.release();
//7rd timer!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 new CountDownTimer(60000,1000){
                @Override
                public void onFinish() {
                PowerManager.WakeLock wl = mPM.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "bright");
                wl.acquire();
                mCM.setAirplaneMode(true);
		mCM.setMobileDataEnabled(false);
		mWM.setWifiEnabled(true);
		mWifiInfo = mWM.getConnectionInfo();
                int NETWORKID = mWifiInfo.getNetworkId();
                mWM.disableNetwork(NETWORKID);
                wl.release();
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
 new CountDownTimer(60000,1000){
                @Override
                public void onFinish() {
                PowerManager.WakeLock wl = mPM.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "bright");
                wl.acquire();
                mWM.setWifiEnabled(false);
                mBT.enable();
                wl.release();
//|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
 new CountDownTimer(60000,1000){
                @Override
                public void onFinish() {
                PowerManager.WakeLock wl = mPM.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "bright");
                wl.acquire();
                mBT.disable();
		Settings.System.putInt(getContentResolver(),Settings.System.SCREEN_BRIGHTNESS,255);
                wl.release();
//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^		
 new CountDownTimer(60000,1000){
                @Override
                public void onFinish() {
                PowerManager.WakeLock wl = mPM.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "bright");
                wl.acquire();
                Settings.System.putInt(getContentResolver(),Settings.System.SCREEN_BRIGHTNESS,0);
                wl.release();
//___________________________________________________________________________________________________________________________________________
 new CountDownTimer(60000,1000){
                @Override
                public void onFinish() {
                PowerManager.WakeLock wl = mPM.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "bright");
                wl.acquire();
                Settings.System.putInt(getContentResolver(),Settings.System.SCREEN_BRIGHTNESS,69);
                wl.release();

                                        }
                public void onTick(long arg0) {
                //PowerManager.WakeLock w2 = mPM.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "wakelock");
                //w2.acquire();

                                              }
		
                                }.start();
//___________________________________________________________________________________________________________________________________________

					}
                public void onTick(long arg0) {

                                              }

                                }.start();
//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^


                                       }
                public void onTick(long arg0) {

                                              }

                                }.start();
//|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
                                       }
                public void onTick(long arg0) {

                                              }

                                }.start();
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                                       }
                public void onTick(long arg0) {

                                              }

                                }.start();
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                                       }
                public void onTick(long arg0) {

                                              }

                                }.start();
//######################################################################################################################################
                                       }
                public void onTick(long arg0) {

                                              }

                                }.start();
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
				       }
                public void onTick(long arg0) {
                    
				              }

                                }.start();
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		@Override
                public void onTick(long arg0) {
                
					}
}.start();
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                       }
 
                @Override
                public void onTick(long arg0) {
                
                                              }
                               }.start();
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
				//Settings.System.putInt(getContentResolver(),Settings.System.SCREEN_BRIGHTNESS,100);
				      }

				@Override
		public void onTick(long arg0) {
		mCM.setAirplaneMode(true);			    
				              }
	            	       }.start();
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
*/
		 //android.provider.Settings.Global.putInt(getContentResolver(),android.provider.Settings.Global.PREFERRED_NETWORK_MODE, networkType);
                 //phone.setPreferredNetworkType(networkType,null);
		//mCM.setAirplaneMode(true);
 		//mCM.setMobileDataEnabled(true);
		  
	//	  int networkType = -1; 
	  //        Settings.Global.putInt(getContentResolver(),Settings.Global.NETWORK_PREFERENCE,networkType);  	
 	
	   //loadSetting(stmt,Settings.Global.PREFERRED_NETWORK_MODE,RILConstants.NETWORK_MODE_GSM_ONLY);  

		}

/*	 private synchronized int shellCmd(String cmd) {
	 try {
                 ShellCommand sc = new ShellCommand(cmd, true);
                 Log.e("TAG", "Success");
                 return sc.getRetval();
             } catch (IOException e) {
                 Log.e("TAG", "Problem launching '" + cmd + "': " + e);
                 e.printStackTrace();
                 return -1;
             }
         }
*/
    	});
    }

} 
