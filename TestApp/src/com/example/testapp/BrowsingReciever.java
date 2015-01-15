package com.example.testapp;

import android.app.Activity;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothProfile.ServiceListener;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiConfiguration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.content.BroadcastReceiver;
import android.provider.Settings.Global;
import android.provider.Settings;
import android.content.Context;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.content.ComponentName;

import android.view.WindowManager;
import android.view.KeyEvent;
import android.view.InputDevice;
import android.hardware.input.InputManager;
import android.view.MotionEvent;
import android.os.SystemClock;
import android.preference.CheckBoxPreference;

import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.RIL;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.CallStateException;

import static android.provider.Settings.Global.PREFERRED_NETWORK_MODE;

public class BrowsingReciever extends BroadcastReceiver{
    private PowerManager pm;
    private Phone phone;
    private ConnectivityManager cm;
    private NetworkInfo mNetworkInfo;
    private WifiManager wm;
    private BluetoothAdapter bt;
    private WifiInfo wifiinfo;
    private BluetoothA2dp mService;   
    private BluetoothDevice btDev;
    private Intent arg0;
    private Intent arg1;	
    private Intent intent_IntentCall;	
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

private BluetoothProfile.ServiceListener A2dpServiceListener = new BluetoothProfile.ServiceListener() {
        public void onServiceConnected(int profile, BluetoothProfile proxy)
        {
            mService = (BluetoothA2dp) proxy;
            Log.e("ACE", " onServiceConnected");

        }

        public void onServiceDisconnected(int profile)
        {
            Log.e("ACE", " onServiceDisconnected");
            mService = null;
        }
    };


	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
	cm = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
	pm = (PowerManager)context.getSystemService(context.POWER_SERVICE);
	wm = (WifiManager)context.getSystemService(context.WIFI_SERVICE);
        bt = BluetoothAdapter.getDefaultAdapter();
	mNetworkInfo = cm.getActiveNetworkInfo();
	Log.i("woden", "onReceive");
	
	Bundle extras = intent.getExtras();
    	int id = extras.getInt("id");        

	Intent arg0 = new Intent(context, AirplaneModeServices.class);
        Bundle bundle = new Bundle();

	PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "bright");
	PowerManager.WakeLock w2 = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "bright");
	
/*String phone_state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
if (phone_state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
			  try
        {
            phone.acceptCall();
        }
        catch(CallStateException e){
            //Log.e(tag,e.toString() );
        }
		}		
*/



//chrome browsing///////////////////////////////////////
//Intent intent_IntentCall = new Intent("android.intent.action.VIEW",Uri.parse("http://218.211.38.216/"));
//intent_IntentCall.setClassName("com.android.chrome","com.google.android.apps.chrome.Main");
//intent_IntentCall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//context.startActivity(intent_IntentCall);

//play music//////////////////////////////////////////
//Intent intent_IntentCall = new Intent();
//intent_IntentCall.setClassName("com.asus.music","com.asus.music.MusicMainActivity");
//intent_IntentCall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//context.startActivity(intent_IntentCall);

//play video//////////////////////////////////////////
//Intent intent_IntentCall = new Intent();
//intent_IntentCall.setClassName("org.iii.romulus.meridian","org.iii.romulus.meridian.MainActivity");
//intent_IntentCall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//context.startActivity(intent_IntentCall);

//snowboarding
//Intent intent_IntentCall = new Intent();
//intent_IntentCall.setClassName("com.ezone.Snowboard","com.unity3d.player.UnityPlayerActivity");
//intent_IntentCall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//context.startActivity(intent_IntentCall);

//Intent intent_IntentCall = new Intent();
//intent_IntentCall.setAction("android.media.action.VIDEO_CAMERA");
//intent_IntentCall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//context.startActivity(intent_IntentCall);

//Intent intent_IntentCall = new Intent();
//intent_IntentCall.setClassName("com.android.settings","com.android.settings.bluetooth.BluetoothSettings");
//intent_IntentCall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//context.startActivity(intent_IntentCall);




if(id==0){
bundle.putInt("settings", 0);
arg0.putExtras(bundle);
context.startService(arg0);
}

if(id==1){
bundle.putInt("settings", 1);
arg0.putExtras(bundle);
context.startService(arg0);
}

if(id==2){
bundle.putInt("settings", 2);
arg0.putExtras(bundle);
context.startService(arg0);
}

if(id==3){
//bundle.putInt("settings", 3);
//arg0.putExtras(bundle);
//context.startService(arg0);
Log.i("woden","chrome 3G"+14);
while(!pm.isScreenOn()){ sendkeyevent(KeyEvent.KEYCODE_POWER);}
wl.acquire();
w2.acquire(50000); 
//sendkeyevent(KeyEvent.KEYCODE_HOME);
//SystemClock.sleep(5000);
//sendkeyevent(KeyEvent.KEYCODE_DPAD_UP);
//sendkeyevent(KeyEvent.KEYCODE_DPAD_CENTER);
SystemClock.sleep(5000);
//sendkeyevent(KeyEvent.KEYCODE_DPAD_CENTER);
//SystemClock.sleep(5000);
Intent intent_IntentCall = new Intent("android.intent.action.VIEW",Uri.parse("http://218.211.38.216/"));
intent_IntentCall.setClassName("com.android.chrome","com.google.android.apps.chrome.Main");
intent_IntentCall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
context.startActivity(intent_IntentCall);
SystemClock.sleep(10000);
sendxy(290,290);
wl.release();
}

if(id==4){
bundle.putInt("settings", 4);
arg0.putExtras(bundle);
context.startService(arg0);
}

if(id==5){
bundle.putInt("settings", 5);
arg0.putExtras(bundle);
context.startService(arg0);
}

if(id==6){
bundle.putInt("settings", 6);
arg0.putExtras(bundle);
context.startService(arg0);
}

if(id==7){
//bundle.putInt("settings", 7);
//arg0.putExtras(bundle);
//context.startService(arg0);
Log.i("woden","chrome wifi"+7);
while(!pm.isScreenOn()){ sendkeyevent(KeyEvent.KEYCODE_POWER);}
wl.acquire();
w2.acquire(50000); 
SystemClock.sleep(5000);
Intent intent_IntentCall = new Intent("android.intent.action.VIEW",Uri.parse("http://218.211.38.216/"));
intent_IntentCall.setClassName("com.android.chrome","com.google.android.apps.chrome.Main");
intent_IntentCall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
context.startActivity(intent_IntentCall);
//sendxy(80,60);
//SystemClock.sleep(5000);
//sendxy(300,300);
SystemClock.sleep(10000);
sendxy(290,290);
wl.release();
}

if(id==8){
bundle.putInt("settings", 8);
arg0.putExtras(bundle);
context.startService(arg0);
}

if(id==9){
bundle.putInt("settings", 9);
arg0.putExtras(bundle);
context.startService(arg0);
}

if(id==10){
bundle.putInt("settings", 10);
arg0.putExtras(bundle);
context.startService(arg0);
}

if(id==11){
bundle.putInt("settings", 11);
arg0.putExtras(bundle);
context.startService(arg0);
}

if(id==12){
bundle.putInt("settings", 12);
arg0.putExtras(bundle);
context.startService(arg0);
}

if(id==13){
bundle.putInt("settings", 13);
arg0.putExtras(bundle);
context.startService(arg0);
}

if(id==14){
bundle.putInt("settings", 14);
arg0.putExtras(bundle);
context.startService(arg0);
}

if(id==15){
bundle.putInt("settings", 15);
arg0.putExtras(bundle);
context.startService(arg0);
}

if(id==16){
bundle.putInt("settings", 16);
arg0.putExtras(bundle);
context.startService(arg0);
}

if(id==17){
bundle.putInt("settings", 17);
arg0.putExtras(bundle);
context.startService(arg0);
}

if(id==18){
bundle.putInt("settings", 18);
arg0.putExtras(bundle);
context.startService(arg0);
}
/* 
if(id==0){	
	wl.acquire();
	Log.i("woden","2G data disable"+0);
        cm.setAirplaneMode(false);
	//bundle.putInt("settings", 0);
        //arg0.putExtras(bundle);
        //context.startService(arg0);
        int networkType = Phone.NT_MODE_GSM_ONLY;
        phone = PhoneFactory.getDefaultPhone();
        android.provider.Settings.Global.putInt(phone.getContext().getContentResolver(),android.provider.Settings.Global.PREFERRED_NETWORK_MODE,networkType);
        phone.setPreferredNetworkType(networkType, null);
        wl.release();
}

if(id==1){
	Log.i("woden","2G data enable"+1);
        wl.acquire();
        cm.setMobileDataEnabled(true);
        wl.release();
}


if(id==2){
	Log.i("woden","3G data enable"+2);
        wl.acquire();
	//bundle.putInt("settings", 2);
        //arg0.putExtras(bundle);
        //context.startService(arg0);
        int networkType = Phone.NT_MODE_WCDMA_PREF;
        phone = PhoneFactory.getDefaultPhone();
        android.provider.Settings.Global.putInt(phone.getContext().getContentResolver(),android.provider.Settings.Global.PREFERRED_NETWORK_MODE,networkType);
        phone.setPreferredNetworkType(networkType, null);
        wl.release();
}

if(id==3){
        Log.i("woden","chrome 3G"+14);
        wl.acquire();
        sendkeyevent(KeyEvent.KEYCODE_HOME);
        SystemClock.sleep(5000);
        sendkeyevent(KeyEvent.KEYCODE_DPAD_UP);
        sendkeyevent(KeyEvent.KEYCODE_DPAD_CENTER);
        SystemClock.sleep(5000);
        sendkeyevent(KeyEvent.KEYCODE_DPAD_CENTER);
        SystemClock.sleep(10000);
        sendxy(290,290);
        wl.release();
}


if(id==4){
	Log.i("woden","3G data disable"+3);
	wl.acquire();
	sendxy(320,65);
        cm.setMobileDataEnabled(false);
        wl.release();
}
	
if(id==5){
	Log.i("woden","wifi on"+4);
	wl.acquire();
        cm.setAirplaneMode(true);
        wm.setWifiEnabled(true);
        wl.release();
}
	
if(id==6){
	Log.i("woden","wifi connet to AP"+5);
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

if(id==7){
        Log.i("woden","chrome wifi"+15);
        wl.acquire();
        SystemClock.sleep(5000);
        sendxy(80,60);
        SystemClock.sleep(5000);
        sendxy(300,300);
        SystemClock.sleep(5000);
        sendxy(290,290);
        wl.release();
}

if(id==8){
	Log.i("woden","wifi 3G associated"+6);
	wl.acquire();
	sendxy(320,65);
        cm.setAirplaneMode(false);
        cm.setMobileDataEnabled(true);
        wl.release();
}

if(id==9){
	Log.i("woden","bt On"+7);
	wl.acquire();
        wm.setWifiEnabled(false);
	cm.setMobileDataEnabled(false);
	cm.setAirplaneMode(true);
        bt.enable();
        wl.release();
}

if(id==10){
        Log.i("woden","bt connect to HS"+8);
        wl.acquire();
	sendkeyevent(KeyEvent.KEYCODE_SETTINGS);
	SystemClock.sleep(5000);
	sendkeyevent(KeyEvent.KEYCODE_DPAD_DOWN);
	sendkeyevent(KeyEvent.KEYCODE_DPAD_DOWN);
        sendkeyevent(KeyEvent.KEYCODE_DPAD_CENTER);
        SystemClock.sleep(5000);
	sendkeyevent(KeyEvent.KEYCODE_DPAD_DOWN);
        sendkeyevent(KeyEvent.KEYCODE_DPAD_DOWN);
        sendkeyevent(KeyEvent.KEYCODE_DPAD_CENTER);
        wl.release();
}
	
if(id==11){
	Log.i("woden","brightness MAX"+9);
	wl.acquire();
        bt.disable();
        Settings.System.putInt(context.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS,255);
        wl.release();
}

if(id==12){
	Log.i("woden","brightness MIN"+10);
	wl.acquire();
        Settings.System.putInt(context.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS,0);
        wl.release();
}

if(id==13){
	Log.i("woden","brightness 100nits"+11);
        wl.acquire();
        Settings.System.putInt(context.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS,69);
        wl.release();
}

if(id==14){
        Log.i("woden","recording"+12);
        wl.acquire();
	sendkeyevent(KeyEvent.KEYCODE_HOME);
        SystemClock.sleep(5000);
	sendkeyevent(KeyEvent.KEYCODE_LAUNCH_CAMERA);
	SystemClock.sleep(10000);
        sendxy(1200,500);
        wl.release();
}

if(id==15){
	Log.i("woden","music"+13);
        wl.acquire();
	sendkeyevent(KeyEvent.KEYCODE_HOME);
	SystemClock.sleep(5000);
	sendkeyevent(KeyEvent.KEYCODE_DPAD_UP);
	sendkeyevent(KeyEvent.KEYCODE_DPAD_UP);
	sendkeyevent(KeyEvent.KEYCODE_DPAD_CENTER);
	SystemClock.sleep(5000);
	sendkeyevent(KeyEvent.KEYCODE_DPAD_RIGHT);
	sendkeyevent(KeyEvent.KEYCODE_DPAD_CENTER);
	SystemClock.sleep(5000);
	sendkeyevent(KeyEvent.KEYCODE_MEDIA_PLAY);
	wl.release();
}
	
if(id==16){
	Log.i("woden","video 720p"+16);
        wl.acquire();
	sendkeyevent(KeyEvent.KEYCODE_MEDIA_PAUSE);
	sendkeyevent(KeyEvent.KEYCODE_HOME);
        SystemClock.sleep(5000);
	sendkeyevent(KeyEvent.KEYCODE_DPAD_UP);
        sendkeyevent(KeyEvent.KEYCODE_DPAD_CENTER);
        SystemClock.sleep(5000);
	sendkeyevent(KeyEvent.KEYCODE_DPAD_DOWN);
        sendkeyevent(KeyEvent.KEYCODE_DPAD_CENTER);
        SystemClock.sleep(5000);
	sendkeyevent(KeyEvent.KEYCODE_DPAD_DOWN);
	sendkeyevent(KeyEvent.KEYCODE_DPAD_DOWN);
        sendkeyevent(KeyEvent.KEYCODE_DPAD_CENTER);
	wl.release();
}

if(id==17){
	Log.i("woden","video 1080p"+17);
        wl.acquire();
	sendkeyevent(KeyEvent.KEYCODE_BACK);
        SystemClock.sleep(5000);
        sendkeyevent(KeyEvent.KEYCODE_DPAD_DOWN);
        sendkeyevent(KeyEvent.KEYCODE_DPAD_CENTER);
	wl.release();
}

if(id==18){
        Log.i("woden","snowboard"+18);
        wl.acquire();
        sendkeyevent(KeyEvent.KEYCODE_HOME);
        SystemClock.sleep(5000);
        sendkeyevent(KeyEvent.KEYCODE_DPAD_UP);
        sendkeyevent(KeyEvent.KEYCODE_DPAD_CENTER);
        SystemClock.sleep(5000);
        sendkeyevent(KeyEvent.KEYCODE_DPAD_RIGHT);
        sendkeyevent(KeyEvent.KEYCODE_DPAD_RIGHT);
        sendkeyevent(KeyEvent.KEYCODE_DPAD_CENTER);
        SystemClock.sleep(30000);
        sendxy(250,370);
        SystemClock.sleep(10000);
        sendxy(250,370);
        SystemClock.sleep(10000);
        sendxy(250,230);
        SystemClock.sleep(10000);
        sendxy(270,650);
        SystemClock.sleep(10000);
        sendxy(270,650);
        SystemClock.sleep(10000);
        sendxy(270,650);
        SystemClock.sleep(10000);
        sendxy(270,650);
        SystemClock.sleep(10000);
        sendxy(270,650);
        SystemClock.sleep(10000);
        sendxy(270,650);
        wl.release();
}
*/

}
}
