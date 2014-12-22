package com.example.testapp;

import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.net.wifi.WifiManager;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.content.BroadcastReceiver;
import android.provider.Settings.Global;
import android.provider.Settings;
import android.content.Context;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.preference.CheckBoxPreference;

import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.RIL;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.PhoneFactory;

import static android.provider.Settings.Global.PREFERRED_NETWORK_MODE;

public class BrowsingReciever extends BroadcastReceiver{
    private PowerManager pm;
    private Phone phone;
    private ConnectivityManager cm;
    
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
	cm = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
	pm = (PowerManager)context.getSystemService(context.POWER_SERVICE);
	Log.i("woden", "onReceive");

	Bundle extras = intent.getExtras();
    	int id = extras.getInt("id");        
 
	if(id==0){	
	PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "bright");
        wl.acquire();
        cm.setAirplaneMode(false);
        int networkType = Phone.NT_MODE_GSM_ONLY;
        phone = PhoneFactory.getDefaultPhone();
        android.provider.Settings.Global.putInt(phone.getContext().getContentResolver(),android.provider.Settings.Global.PREFERRED_NETWORK_MODE,networkType);
        phone.setPreferredNetworkType(networkType, null);
        wl.release();
		}

	if(id==1){
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "bright");
        wl.acquire();
        cm.setMobileDataEnabled(true);
        wl.release();
                }

/*
        if(id==2){
        PowerManager.WakeLock wl = mPM.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "bright");
        wl.acquire();
        int networkType = Phone.NT_MODE_WCDMA_PREF;
        mPhone = PhoneFactory.getDefaultPhone();
        android.provider.Settings.Global.putInt(mPhone.getContext().getContentResolver(),android.provider.Settings.Global.PREFERRED_NETWORK_MODE,networkType);
        mPhone.setPreferredNetworkType(networkType, null);
        wl.release();
          	}

	if(id==3){
	PowerManager.WakeLock wl = mPM.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "bright");
        wl.acquire();
        mCM.setMobileDataEnabled(false);
        wl.release();
		 }
	
        if(id==4){
	PowerManager.WakeLock wl = mPM.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "bright");
        wl.acquire();
        mCM.setAirplaneMode(true);
        mWM.setWifiEnabled(true);
        wl.release();
		 }
*/

	}

}
