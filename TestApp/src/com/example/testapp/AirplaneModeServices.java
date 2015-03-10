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
import android.app.AlarmManager;
import android.os.SystemClock;
import android.content.Context;
import android.content.ContentResolver;
import android.content.Intent;
import android.telephony.TelephonyManager;
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
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.view.MotionEvent;
import android.view.KeyEvent;
import android.view.InputDevice;
import android.hardware.input.InputManager;

import java.lang.reflect.Method;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.ITelephony;

public class AirplaneModeServices extends Service {

	private ConnectivityManager cm;
	private WifiManager wm;
	private PowerManager pm;
	private Phone phone;
	private BluetoothAdapter bt;
	private OutputStream outstream;
	protected BluetoothSocket btSocket;
	boolean isDataon;
	boolean isAirplaneon;
	
private void ConnecttoBTServer(){
bt = BluetoothAdapter.getDefaultAdapter();
while(!bt.isEnabled()){ bt.enable(); }
SystemClock.sleep(5000);
String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
final UUID uuid = UUID.fromString(SPP_UUID);

Set<BluetoothDevice> pairedDevices =  BluetoothAdapter.getDefaultAdapter().getBondedDevices();
BluetoothDevice btDev = null;

for (BluetoothDevice device : pairedDevices) {
       	String name = device.getName();
        Log.d("BT",device.getName());
        if (name.equals("WODEN_LEE-PC")) {
		btDev = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(device.getAddress());
                break;
                			 }
            				     }				
try {
    BluetoothSocket btSocket = btDev.createRfcommSocketToServiceRecord(uuid);
    btSocket.connect();
    outstream = btSocket.getOutputStream();
    } catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
    }

}

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

/*private void Reset(Context context){
if(bt.isEnabled()){ bt.disable(); }
if(isAirplaneon){cm.setAirplaneMode(false); isAirplaneon=false;}
if(isWifion){wm.setWifiEnabled(false);isWifion=false;}
if(isDataon){tm.setDataEnabled(false);isDataon=false;}
}*/


	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate();
		isAirplaneon=false;
        	isDataon=false;
	}

	
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	Context context = getBaseContext();
	ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
	TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
	WifiManager wm = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
	PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);	
	AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
	PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "bright");
	PowerManager.WakeLock w2 = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "bright");
	bt = BluetoothAdapter.getDefaultAdapter();	
	NetworkInfo mWifi = cm.getNetworkInfo(cm.TYPE_WIFI);
	NetworkInfo mMobile = cm.getNetworkInfo(cm.TYPE_MOBILE);
	Bundle extras = intent.getExtras();
        int settings = extras.getInt("settings");

	if(settings==0){
	/////////////////////////////////////////////////////////////////////
	while(isAirplaneon){cm.setAirplaneMode(false); isAirplaneon=false;}
	while(wm.isWifiEnabled()){wm.setWifiEnabled(false);}
	while(isDataon){tm.setDataEnabled(false);isDataon=false;}
	////////////////////////////////////////////////////////////////////
	ConnecttoBTServer();
	String message = "airplane-mode";
        byte[] msgBuffer = message.getBytes();
                	
        try {
            outstream.write(msgBuffer);
	    SystemClock.sleep(3000);
	    outstream.close();
	    outstream=null;
            } catch (IOException e) {
            // TODO ..... catch ..
            e.printStackTrace();
            }
	while(bt.isEnabled()){ bt.disable(); }
	while(!isAirplaneon){cm.setAirplaneMode(true); isAirplaneon=true;}
	}

	if(settings==1){
	Log.i("woden","3G data enable"+0);
	while(!pm.isScreenOn()){ sendkeyevent(KeyEvent.KEYCODE_POWER);}
	wl.acquire();
	//////////////////////////////////////////////////////////////////////
        while(isAirplaneon){cm.setAirplaneMode(false); isAirplaneon=false;}
        while(wm.isWifiEnabled()){wm.setWifiEnabled(false);}
        while(isDataon){tm.setDataEnabled(false);isDataon=false;}
	//////////////////////////////////////////////////////////////////////
	ConnecttoBTServer();
	String message = "3G-data-enable";
        byte[] msgBuffer = message.getBytes();
        try {
            outstream.write(msgBuffer);
	    SystemClock.sleep(3000);
	    outstream.close();
	    outstream=null;
            } catch (IOException e) {
           // TODO ..... catch ..
            e.printStackTrace();
            }
	while(bt.isEnabled()){ bt.disable(); }
        int networkType = Phone.NT_MODE_WCDMA_PREF;
        phone = PhoneFactory.getDefaultPhone();
        android.provider.Settings.Global.putInt(phone.getContext().getContentResolver(),android.provider.Settings.Global.PREFERRED_NETWORK_MODE,networkType);
        phone.setPreferredNetworkType(networkType, null);
		//cm.setMobileDataEnabled(true);
	tm.setDataEnabled(true); isDataon=true;
	wl.release();
        }	

	if(settings==2){
	Log.i("woden","wifi on"+1);
	while(!pm.isScreenOn()){ sendkeyevent(KeyEvent.KEYCODE_POWER);}
        wl.acquire();
	 //////////////////////////////////////////////////////////////////////
        while(wm.isWifiEnabled()){wm.setWifiEnabled(false);}
        while(isDataon){tm.setDataEnabled(false);isDataon=false;}
        //////////////////////////////////////////////////////////////////////
	ConnecttoBTServer();
	String message = "wifi-on";
        byte[] msgBuffer = message.getBytes();
        try {
            outstream.write(msgBuffer);
	    SystemClock.sleep(3000);
	    outstream.close();
	    outstream=null;	    
            } catch (IOException e) {
           // TODO ..... catch ..
            e.printStackTrace();
            }
	while(bt.isEnabled()){ bt.disable(); }
	while(!isAirplaneon){cm.setAirplaneMode(true); isAirplaneon=true;}
	SystemClock.sleep(3000);
	while(!wm.isWifiEnabled()){wm.setWifiEnabled(true);}
        wl.release();
        }
	
	if(settings==3){
	Log.i("woden","wifi-connected"+2);
	while(!pm.isScreenOn()){ sendkeyevent(KeyEvent.KEYCODE_POWER);}
	wl.acquire();
	 //////////////////////////////////////////////////////////////////////
        while(wm.isWifiEnabled()){wm.setWifiEnabled(false);}
        while(isDataon){tm.setDataEnabled(false);isDataon=false;}
        //////////////////////////////////////////////////////////////////////
	ConnecttoBTServer();
	String message = "wifi-connected";
        byte[] msgBuffer = message.getBytes();
        try {
            outstream.write(msgBuffer);
	    SystemClock.sleep(3000);
	    outstream.close();
	    outstream=null;
            } catch (IOException e) {
           // TODO ..... catch ..
            e.printStackTrace();
            }
	while(bt.isEnabled()){ bt.disable(); }
	while(!isAirplaneon){cm.setAirplaneMode(true); isAirplaneon=true;}
	SystemClock.sleep(3000);
	while(!wm.isWifiEnabled()){wm.setWifiEnabled(true);}
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

	if(settings==4){
	Log.i("woden","BT on"+4);
	while(!pm.isScreenOn()){ sendkeyevent(KeyEvent.KEYCODE_POWER);}
	wl.acquire();
         //////////////////////////////////////////////////////////////////////
        while(wm.isWifiEnabled()){wm.setWifiEnabled(false);}
        while(isDataon){tm.setDataEnabled(false);isDataon=false;}
        //////////////////////////////////////////////////////////////////////
	ConnecttoBTServer();
	String message = "BT-on";
        byte[] msgBuffer = message.getBytes();
        try {
            outstream.write(msgBuffer);
	    SystemClock.sleep(3000);
	    outstream.close();
	    outstream=null;
            } catch (IOException e) {
           // TODO ..... catch ..
            e.printStackTrace();
            }
	while(!isAirplaneon){cm.setAirplaneMode(true); isAirplaneon=true;}
        while(!bt.isEnabled()){ bt.enable(); }
        wl.release();
	}

	if(settings==5){
	Log.i("woden","BT connected"+5);
	while(!pm.isScreenOn()){ sendkeyevent(KeyEvent.KEYCODE_POWER);}
	wl.acquire();
         //////////////////////////////////////////////////////////////////////
        while(wm.isWifiEnabled()){wm.setWifiEnabled(false);}
        while(isDataon){tm.setDataEnabled(false);isDataon=false;}
        //////////////////////////////////////////////////////////////////////
        ConnecttoBTServer();
	String message = "BT-connected";
        byte[] msgBuffer = message.getBytes();
        try {
            outstream.write(msgBuffer);
	    SystemClock.sleep(3000);
	    outstream.close();
	    outstream=null;
            } catch (IOException e) {
           // TODO ..... catch ..
            e.printStackTrace();
            }
	while(!bt.isEnabled()){ bt.enable(); }
	while(!isAirplaneon){cm.setAirplaneMode(true); isAirplaneon=true;}
      	Intent intent_IntentCall = new Intent();
        intent_IntentCall.setClassName("com.android.settings","com.android.settings.bluetooth.BluetoothSettings");
        intent_IntentCall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent_IntentCall);
        SystemClock.sleep(3000);
        sendkeyevent(KeyEvent.KEYCODE_DPAD_DOWN);
        sendkeyevent(KeyEvent.KEYCODE_DPAD_DOWN);
	SystemClock.sleep(3000);
        sendkeyevent(KeyEvent.KEYCODE_DPAD_CENTER);
        wl.release();
	}
	
	if(settings==6){
	Log.i("woden","screen on white color"+6);
	while(!pm.isScreenOn()){ sendkeyevent(KeyEvent.KEYCODE_POWER);}
	wl.acquire();
	w2.acquire(3000);
	 //////////////////////////////////////////////////////////////////////
        while(wm.isWifiEnabled()){wm.setWifiEnabled(false);}
	while(isDataon){tm.setDataEnabled(false);isDataon=false;}
        //////////////////////////////////////////////////////////////////////
	ConnecttoBTServer();
	String message = "Screen-on-white";
        byte[] msgBuffer = message.getBytes();
        try {
            outstream.write(msgBuffer);
	    SystemClock.sleep(3000);
	    outstream.close();
	    outstream=null;
            } catch (IOException e) {
           // TODO ..... catch ..
            e.printStackTrace();
            }
	while(bt.isEnabled()){ bt.disable(); }
	while(!isAirplaneon){cm.setAirplaneMode(true); isAirplaneon=true;}
	Intent intent_IntentCall = new Intent();
        intent_IntentCall.setClassName("com.asus.gallery","com.asus.gallery.app.EPhotoActivity");
        intent_IntentCall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent_IntentCall);
        SystemClock.sleep(5000);
        sendxy(500,200);
        SystemClock.sleep(5000);
        sendxy(150,400);
	wl.release();
	}

	if(settings==7){
	Log.i("woden","screen off idle"+8);
	while(!pm.isScreenOn()){ sendkeyevent(KeyEvent.KEYCODE_POWER);}
	wl.acquire();
	 //////////////////////////////////////////////////////////////////////
        while(wm.isWifiEnabled()){wm.setWifiEnabled(false);}
        while(isDataon){tm.setDataEnabled(false);isDataon=false;}
        //////////////////////////////////////////////////////////////////////
        ConnecttoBTServer();
	String message = "Screen-off-idle";
        byte[] msgBuffer = message.getBytes();
        try {
            outstream.write(msgBuffer);
	    SystemClock.sleep(3000);
	    outstream.close();
	    outstream=null;
            } catch (IOException e) {
           // TODO ..... catch ..
            e.printStackTrace();
            }
	while(bt.isEnabled()){ bt.disable(); }
	while(!isAirplaneon){cm.setAirplaneMode(true); isAirplaneon=true;}
        sendkeyevent(KeyEvent.KEYCODE_HOME);
        PowerManager.WakeLock wakelock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "cpu on");
        wakelock.acquire(3000);
        wl.release();	
	}

	if(settings==8){
	Log.i("woden","audio playback HS"+9);
	while(!pm.isScreenOn()){ sendkeyevent(KeyEvent.KEYCODE_POWER);}
	wl.acquire();
	 //////////////////////////////////////////////////////////////////////
        while(wm.isWifiEnabled()){wm.setWifiEnabled(false);}
        while(isDataon){tm.setDataEnabled(false);isDataon=false;}
        //////////////////////////////////////////////////////////////////////
        ConnecttoBTServer();
	String message = "Audio-playback-HS";
        byte[] msgBuffer = message.getBytes();
        try {
            outstream.write(msgBuffer);
	    SystemClock.sleep(3000);
	    outstream.close();
	    outstream=null;
            } catch (IOException e) {
           // TODO ..... catch ..
            e.printStackTrace();
            }
	while(bt.isEnabled()){ bt.disable(); }
	while(!isAirplaneon){cm.setAirplaneMode(true); isAirplaneon=true;}
        Intent intent_IntentCall = new Intent();
        intent_IntentCall.setClassName("com.asus.music","com.asus.music.MusicMainActivity");
        intent_IntentCall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent_IntentCall);
        SystemClock.sleep(10000);
        sendkeyevent(KeyEvent.KEYCODE_MEDIA_PLAY);
	wl.release();
	}

	if(settings==9){
	Log.i("woden","video 720p"+10);
	while(!pm.isScreenOn()){ sendkeyevent(KeyEvent.KEYCODE_POWER);}
	wl.acquire();
	sendkeyevent(KeyEvent.KEYCODE_MEDIA_PAUSE);
	 //////////////////////////////////////////////////////////////////////
        while(wm.isWifiEnabled()){wm.setWifiEnabled(false);}
        while(isDataon){tm.setDataEnabled(false);isDataon=false;}
        //////////////////////////////////////////////////////////////////////
        ConnecttoBTServer();
        String message = "Video-720P";
        byte[] msgBuffer = message.getBytes();
        try {
            outstream.write(msgBuffer);
	    SystemClock.sleep(3000);
	    outstream.close();
	    outstream=null;
            } catch (IOException e) {
           // TODO ..... catch ..
            e.printStackTrace();
            }
	while(bt.isEnabled()){ bt.disable(); }
	while(!isAirplaneon){cm.setAirplaneMode(true); isAirplaneon=true;}
	SystemClock.sleep(5000);
        sendkeyevent(KeyEvent.KEYCODE_MEDIA_PAUSE);
        SystemClock.sleep(5000);
        Intent intent_IntentCall = new Intent();
        intent_IntentCall.setClassName("org.iii.romulus.meridian","org.iii.romulus.meridian.MainActivity");
        intent_IntentCall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent_IntentCall);
        SystemClock.sleep(7000);
        sendkeyevent(KeyEvent.KEYCODE_DPAD_DOWN);
        sendkeyevent(KeyEvent.KEYCODE_DPAD_DOWN);
	SystemClock.sleep(3000);
        sendkeyevent(KeyEvent.KEYCODE_DPAD_CENTER);
	wl.release();
	}

	
	this.onDestroy();
	
}

	
	public void onDestroy() {
		super.onDestroy();
	}

	
}
