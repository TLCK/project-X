package com.example.testapp;

import android.app.Activity;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
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

import java.lang.reflect.Method;
import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.RIL;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.ITelephony;

import static android.provider.Settings.Global.PREFERRED_NETWORK_MODE;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

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
    private ITelephony itelephony;
    private OutputStream outstream;
    protected BluetoothSocket btSocket;    

private void answerPhoneHeadsethook(Context context) {
		// Simulate a press of the headset button to pick up the call
		Intent buttonDown = new Intent(Intent.ACTION_MEDIA_BUTTON);		
		buttonDown.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK));
		context.sendOrderedBroadcast(buttonDown, "android.permission.CALL_PRIVILEGED");

		// froyo and beyond trigger on buttonUp instead of buttonDown
		Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);		
		buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
		context.sendOrderedBroadcast(buttonUp, "android.permission.CALL_PRIVILEGED");
}

private void answerPhoneAidl(Context context) throws Exception {
		// Set up communication with the telephony service (thanks to Tedd's Droid Tools!)
		TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
		Class c = Class.forName(tm.getClass().getName());
		Method m = c.getDeclaredMethod("getITelephony");
		m.setAccessible(true);
		ITelephony telephonyService;
		telephonyService = (ITelephony)m.invoke(tm);

		// Silence the ringer and answer the call!
		telephonyService.silenceRinger();
		telephonyService.answerRingingCall();
	}

private void endCall(Context context) throws Exception {
                // Set up communication with the telephony service (thanks to Tedd's Droid Tools!)
                TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
                Class c = Class.forName(tm.getClass().getName());
                Method m = c.getDeclaredMethod("getITelephony");
                m.setAccessible(true);
                ITelephony telephonyService;
                telephonyService = (ITelephony)m.invoke(tm);

                // Silence the ringer and answer the call!
                //telephonyService.silenceRinger();
                telephonyService.endCall();
        }
	
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
	
	
	Bundle extras = intent.getExtras();
    	int id = extras.getInt("id");        

	Intent arg0 = new Intent(context, AirplaneModeServices.class);
        Bundle bundle = new Bundle();

	PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "bright");
	PowerManager.WakeLock w2 = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "bright");
	
	
/*String phone_state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
if (phone_state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
	TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		try {
			answerPhoneAidl(context);
		}
		catch (Exception e) {
			e.printStackTrace();
			Log.d("AutoAnswer","Error trying to answer using telephony service.  Falling back to headset.");
			answerPhoneHeadsethook(context);
		}
			
}*/


/*calling
String phone_number="0937427947";
Intent IntentCall = new Intent("android.intent.action.CALL",Uri.parse("tel:" + phone_number));
IntentCall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
context.startActivity(IntentCall);

SystemClock.sleep(15000);
try{
endCall(context);
}catch(Exception e){}
*/

//chromption e///////////////////////////////////////
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
bundle.putInt("settings", 3);
arg0.putExtras(bundle);
context.startService(arg0);
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
bundle.putInt("settings", 7);
arg0.putExtras(bundle);
context.startService(arg0);
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


}
}
