package com.example.testapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.os.SystemClock;
import android.content.Context;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiConfiguration;
import android.provider.Settings.Global;
import android.provider.Settings;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.bluetooth.BluetoothAdapter;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.util.Log;

import android.view.MotionEvent;
import android.view.KeyEvent;
import android.view.InputDevice;
import android.hardware.input.InputManager;

import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.PhoneFactory;

public class AirplaneModeServices extends Service {

	private ConnectivityManager cm;
	private WifiManager wm;
	private PowerManager pm;
	private Phone phone;
	private BluetoothAdapter bt;

public void sendKeySync(KeyEvent event) {

        long downTime = event.getDownTime();
        long eventTime = event.getEventTime();
        int action = event.getAction();
        int code = event.getKeyCode();
        int repeatCount = event.getRepeatCount();
        int metaState = event.getMetaState();
        int deviceId = event.getDeviceId();
        int scancode = event.getScanCode();
        int source = event.getSource();
        int flags = event.getFlags();
        if (source == InputDevice.SOURCE_UNKNOWN) {
            source = InputDevice.SOURCE_KEYBOARD;
        }
        if (eventTime == 0) {
            eventTime = SystemClock.uptimeMillis();
        }
        if (downTime == 0) {
            downTime = eventTime;
        }
        KeyEvent newEvent = new KeyEvent(downTime, eventTime, action, code, repeatCount, metaState,
                deviceId, scancode, flags | KeyEvent.FLAG_FROM_SYSTEM, source);
        InputManager.getInstance().injectInputEvent(newEvent,
                InputManager.INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH);
    }

public void sendkeyevent(int keycode){
        sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, keycode));
        sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, keycode));
    }

public void sendxy(int x,int y) {

        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        MotionEvent eventDown = MotionEvent.obtain(downTime, eventTime,MotionEvent.ACTION_DOWN, x, y, 0);

        if ((eventDown.getSource() & InputDevice.SOURCE_CLASS_POINTER) == 0) {
            eventDown.setSource(InputDevice.SOURCE_TOUCHSCREEN);
        }
        InputManager.getInstance().injectInputEvent(eventDown,
                InputManager.INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH);

        MotionEvent eventUp = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x, y, 0);

        if ((eventUp.getSource() & InputDevice.SOURCE_CLASS_POINTER) == 0) {
            eventUp.setSource(InputDevice.SOURCE_TOUCHSCREEN);
        }
        InputManager.getInstance().injectInputEvent(eventUp,
                InputManager.INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH);

    }

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate();
	}

	
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

	ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
	WifiManager wm = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
	PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);	
	PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "bright");
	PowerManager.WakeLock w2 = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "bright");
	bt = BluetoothAdapter.getDefaultAdapter();	

	Bundle extras = intent.getExtras();
        int settings = extras.getInt("settings");


	if(settings==0){
	Log.i("woden","2G data disable"+0);
	while(!pm.isScreenOn()){ sendkeyevent(KeyEvent.KEYCODE_POWER);}
	wl.acquire();
        cm.setAirplaneMode(false);
        int networkType = Phone.NT_MODE_GSM_ONLY;
        phone = PhoneFactory.getDefaultPhone();
        android.provider.Settings.Global.putInt(phone.getContext().getContentResolver(),android.provider.Settings.Global.PREFERRED_NETWORK_MODE,networkType);
        phone.setPreferredNetworkType(networkType, null);
	wl.release();
        }	

	if(settings==1){
	Log.i("woden","2G data enable"+1);
	while(!pm.isScreenOn()){ sendkeyevent(KeyEvent.KEYCODE_POWER);}
        wl.acquire();
        cm.setMobileDataEnabled(true);
        wl.release();
        }
	
	if(settings==2){
	Log.i("woden","3G data enable"+2);
	while(!pm.isScreenOn()){ sendkeyevent(KeyEvent.KEYCODE_POWER);}
	wl.acquire();
        int networkType = Phone.NT_MODE_WCDMA_PREF;
        phone = PhoneFactory.getDefaultPhone();
        android.provider.Settings.Global.putInt(phone.getContext().getContentResolver(),android.provider.Settings.Global.PREFERRED_NETWORK_MODE,networkType);
        phone.setPreferredNetworkType(networkType, null);
        wl.release();
	
	//cm.setAirplaneMode(true);
        //wm.setWifiEnabled(true);
	}

	if(settings==3){
        //Log.i("woden","chrome browsing 3G"+3);
        //wl.acquire();
        //sendkeyevent(KeyEvent.KEYCODE_HOME);
        //SystemClock.sleep(5000);
        //sendkeyevent(KeyEvent.KEYCODE_DPAD_UP);
        //sendkeyevent(KeyEvent.KEYCODE_DPAD_CENTER);
        //SystemClock.sleep(5000);
        //sendkeyevent(KeyEvent.KEYCODE_DPAD_CENTER);
        //SystemClock.sleep(10000);
        //sendxy(290,290);
        //wl.release();
	}

	if(settings==4){
	Log.i("woden","3G data disable"+4);
	while(!pm.isScreenOn()){ sendkeyevent(KeyEvent.KEYCODE_POWER);}
	wl.acquire();
	sendxy(320,65);
        cm.setMobileDataEnabled(false);
	SystemClock.sleep(5000);
	sendxy(320,65);
        wl.release();
	}

	if(settings==5){
	Log.i("woden","wifi on"+5);
	while(!pm.isScreenOn()){ sendkeyevent(KeyEvent.KEYCODE_POWER);}
	wl.acquire();
        cm.setAirplaneMode(true);
	while(!wm.isWifiEnabled()){ wm.setWifiEnabled(true); }
        wl.release();
	}
	
	if(settings==6){
	Log.i("woden","wifi connect to AP"+6);
	while(!pm.isScreenOn()){ sendkeyevent(KeyEvent.KEYCODE_POWER);}
	wl.acquire();
        String networkSSID = "sw3rd2";
        String networkPass = "qwertyui";
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = String.format("\"%s\"", networkSSID);
        conf.preSharedKey = String.format("\"%s\"", networkPass);;
        int netId = wm.addNetwork(conf);
        wm.disconnect();
        wm.enableNetwork(netId, true);
        wm.reconnect();
        wl.release();
	}

	if(settings==7){
        //Log.i("woden","chrome browsing wifi"+7);
        //wl.acquire();
        //sendxy(80,60);
        //SystemClock.sleep(5000);
        //sendxy(300,580);
        //SystemClock.sleep(10000);
        //sendxy(290,290);
        //wl.release();
        }

	if(settings==8){
	Log.i("woden","wifi 3G associated"+8);
	while(!pm.isScreenOn()){ sendkeyevent(KeyEvent.KEYCODE_POWER);}
	wl.acquire();
	SystemClock.sleep(5000);
	sendxy(320,65);
        cm.setAirplaneMode(false);
        cm.setMobileDataEnabled(true);
        wl.release();	
	}

	if(settings==9){
	Log.i("woden","bt on"+9);
	while(!pm.isScreenOn()){ sendkeyevent(KeyEvent.KEYCODE_POWER);}
	wl.acquire();
        wm.setWifiEnabled(false);
	cm.setMobileDataEnabled(false);
        cm.setAirplaneMode(true);
	while(!bt.isEnabled()){ bt.enable(); } 
        wl.release();
	}

	if(settings==10){
	Log.i("woden","bt connect to HS"+10);
	while(!pm.isScreenOn()){ sendkeyevent(KeyEvent.KEYCODE_POWER);}
	wl.acquire();
        //sendkeyevent(KeyEvent.KEYCODE_SETTINGS);
        //SystemClock.sleep(10000);
        //sendkeyevent(KeyEvent.KEYCODE_DPAD_DOWN);
        //sendkeyevent(KeyEvent.KEYCODE_DPAD_DOWN);
	//sendkeyevent(KeyEvent.KEYCODE_DPAD_CENTER);
	Intent intent_IntentCall = new Intent();
	intent_IntentCall.setClassName("com.android.settings","com.android.settings.bluetooth.BluetoothSettings");
	intent_IntentCall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	this.startActivity(intent_IntentCall);
        SystemClock.sleep(5000);
        sendkeyevent(KeyEvent.KEYCODE_DPAD_DOWN);
        sendkeyevent(KeyEvent.KEYCODE_DPAD_DOWN);
        sendkeyevent(KeyEvent.KEYCODE_DPAD_CENTER);
        wl.release();
	}

	if(settings==11){
	Log.i("woden","brightness 255"+11);
	while(!pm.isScreenOn()){ sendkeyevent(KeyEvent.KEYCODE_POWER);}
	wl.acquire();
	w2.acquire(50000);
        bt.disable();
        Settings.System.putInt(getContentResolver(),Settings.System.SCREEN_BRIGHTNESS,255);
        wl.release();
	}

	if(settings==12){
	Log.i("woden","brightness 0"+12);
	while(!pm.isScreenOn()){ sendkeyevent(KeyEvent.KEYCODE_POWER);}
	wl.acquire();
	w2.acquire(50000);
        Settings.System.putInt(getContentResolver(),Settings.System.SCREEN_BRIGHTNESS,0);
        wl.release();
	}

	if(settings==13){
	Log.i("woden","brightness 100 nits"+13);
	while(!pm.isScreenOn()){ sendkeyevent(KeyEvent.KEYCODE_POWER);}
	wl.acquire();
	w2.acquire(50000);
        Settings.System.putInt(getContentResolver(),Settings.System.SCREEN_BRIGHTNESS,69);
        wl.release();
	}

	if(settings==14){
	Log.i("woden","music play"+14);
	while(!pm.isScreenOn()){ sendkeyevent(KeyEvent.KEYCODE_POWER);}
	wl.acquire();
        //sendkeyevent(KeyEvent.KEYCODE_HOME);
        //SystemClock.sleep(5000);
        //sendkeyevent(KeyEvent.KEYCODE_DPAD_UP);
        //sendkeyevent(KeyEvent.KEYCODE_DPAD_CENTER);
        //SystemClock.sleep(5000);
        //sendkeyevent(KeyEvent.KEYCODE_DPAD_RIGHT);
        //sendkeyevent(KeyEvent.KEYCODE_DPAD_CENTER);
	Intent intent_IntentCall = new Intent();
	intent_IntentCall.setClassName("com.asus.music","com.asus.music.MusicMainActivity");
	intent_IntentCall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	this.startActivity(intent_IntentCall);
        SystemClock.sleep(10000);
        sendkeyevent(KeyEvent.KEYCODE_MEDIA_PLAY);
        wl.release();
	}

	if(settings==15){
	Log.i("woden","video play 1"+15);
	while(!pm.isScreenOn()){ sendkeyevent(KeyEvent.KEYCODE_POWER);}
	wl.acquire();
	SystemClock.sleep(5000);
        sendkeyevent(KeyEvent.KEYCODE_MEDIA_PAUSE);
        //sendkeyevent(KeyEvent.KEYCODE_HOME);
        //SystemClock.sleep(5000);
        //sendkeyevent(KeyEvent.KEYCODE_DPAD_UP);
        //sendkeyevent(KeyEvent.KEYCODE_DPAD_CENTER);
        //SystemClock.sleep(5000);
        //sendkeyevent(KeyEvent.KEYCODE_DPAD_DOWN);
        //sendkeyevent(KeyEvent.KEYCODE_DPAD_CENTER);
	SystemClock.sleep(5000);
	Intent intent_IntentCall = new Intent();
	intent_IntentCall.setClassName("org.iii.romulus.meridian","org.iii.romulus.meridian.MainActivity");
	intent_IntentCall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	this.startActivity(intent_IntentCall);
        SystemClock.sleep(7000);
        sendkeyevent(KeyEvent.KEYCODE_DPAD_DOWN);
        sendkeyevent(KeyEvent.KEYCODE_DPAD_DOWN);
        sendkeyevent(KeyEvent.KEYCODE_DPAD_CENTER);
        wl.release();
	}	

	if(settings==16){
	Log.i("woden","video play 2"+16);
	while(!pm.isScreenOn()){ sendkeyevent(KeyEvent.KEYCODE_POWER);}
	wl.acquire();
        sendkeyevent(KeyEvent.KEYCODE_BACK);
        SystemClock.sleep(5000);
        sendkeyevent(KeyEvent.KEYCODE_DPAD_UP);
        sendkeyevent(KeyEvent.KEYCODE_DPAD_CENTER);
        wl.release();	
	}

	if(settings==17){
	Log.i("woden","recording"+17);
	while(!pm.isScreenOn()){ sendkeyevent(KeyEvent.KEYCODE_POWER);}
	wl.acquire();
        cm.setAirplaneMode(true);
        //sendkeyevent(KeyEvent.KEYCODE_HOME);
        //SystemClock.sleep(5000);
        sendkeyevent(KeyEvent.KEYCODE_LAUNCH_CAMERA);
        SystemClock.sleep(10000);
        sendxy(1200,500);
        wl.release();
	}

	if(settings==18){
	Log.i("woden","snowboarding"+18);
	while(!pm.isScreenOn()){ sendkeyevent(KeyEvent.KEYCODE_POWER);}
	wl.acquire();
        //sendkeyevent(KeyEvent.KEYCODE_HOME);
        //SystemClock.sleep(5000);
        //sendkeyevent(KeyEvent.KEYCODE_DPAD_UP);
        //sendkeyevent(KeyEvent.KEYCODE_DPAD_CENTER);
        //SystemClock.sleep(5000);
        //sendkeyevent(KeyEvent.KEYCODE_DPAD_RIGHT);
        //sendkeyevent(KeyEvent.KEYCODE_DPAD_RIGHT);
        //sendkeyevent(KeyEvent.KEYCODE_DPAD_CENTER);
	Intent intent_IntentCall = new Intent();
	intent_IntentCall.setClassName("com.ezone.Snowboard","com.unity3d.player.UnityPlayerActivity");
	intent_IntentCall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	this.startActivity(intent_IntentCall);
        SystemClock.sleep(30000);
	sendxy(250,70);
	SystemClock.sleep(7000);
        sendxy(250,370);
        SystemClock.sleep(7000);
        sendxy(250,370);
        SystemClock.sleep(7000);
        sendxy(250,230);
        SystemClock.sleep(10000);
        sendxy(270,650);
        SystemClock.sleep(7000);
        sendxy(270,650);
        SystemClock.sleep(7000);
        sendxy(270,650);
        SystemClock.sleep(7000);
        sendxy(270,650);
        SystemClock.sleep(7000);
        sendxy(270,650);
        SystemClock.sleep(7000);
        sendxy(270,650);
        wl.release();
	}

	
	this.onDestroy();

//mWifiInfo = mWM.getConnectionInfo();
//int NETWORKID = mWifiInfo.getNetworkId();
//mWM.disableNetwork(NETWORKID);
	
}

	
	public void onDestroy() {
		super.onDestroy();
	}



	
	
}
