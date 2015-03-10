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
import android.widget.CheckBox;
import android.widget.EditText;
import android.provider.Settings.Global;
import android.provider.Settings;
import android.content.Context;
import android.content.ContentResolver;
import android.content.ServiceConnection;
import android.telephony.TelephonyManager;
import android.os.CountDownTimer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration;
import android.view.WindowManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothProfile.ServiceListener;
import android.content.ServiceConnection;
import android.view.Window;
import android.net.wifi.WifiInfo;
import android.app.AlarmManager;
import android.app.ActivityManager;
import android.content.Intent;
import android.app.PendingIntent;
import android.os.SystemClock;
import java.lang.Runtime;
import java.lang.Runnable;
import java.lang.Thread;
import android.hardware.input.InputManager;
import android.app.Instrumentation;
import android.test.InstrumentationTestCase;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.os.SystemClock;
import java.io.InputStream;
import java.io.OutputStream;

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
import java.util.List;
import java.util.UUID;
import java.util.Set;
import android.os.ParcelUuid;

import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.RIL;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.CallStateException;
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
    private Intent inent;
    private UUID uuid;
    private Context mContext;
    private String mactekHartModemName;
    private UUID mactekHartModemUuid;
    private InputStream mmInputStream;
    private OutputStream mmOutputStream;
    private BluetoothDevice mmDevice;
    private BluetoothSocket mmSocket;
    private BluetoothDevice btDev;
    private BluetoothA2dp mService;
    public  OutputStream outstream; 
    //private BluetoothAdapter pairedDevices;
    //public CommandsInterface mCi;
    //private Phone mPhone;	
    //private RIL mRil;
    private Phone phone;
    int x,y;
    private int i;
    String phone_number;
    //private static Phone mPhone;
    //private static MyHandler mHandler;
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

private class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
 
    public ConnectThread(BluetoothDevice device) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        BluetoothSocket tmp = null;
        mmDevice = device;
 
        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device.createRfcommSocketToServiceRecord(mactekHartModemUuid);
        } catch (IOException e) { }
        mmSocket = tmp;
    }
 
    public void run() {
        // Cancel discovery because it will slow down the connection
        mBT.cancelDiscovery();
 
        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            try {
                mmSocket.close();
            } catch (IOException closeException) { }
            return;
        }
 
        // Do work to manage the connection (in a separate thread)
        //manageConnectedSocket(mmSocket);
    }
 

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCM = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
	mWM = (WifiManager)getSystemService(WIFI_SERVICE);
	mBT = BluetoothAdapter.getDefaultAdapter();
	mPM = (PowerManager)getSystemService(POWER_SERVICE);
	AM = (AlarmManager) getSystemService(ALARM_SERVICE);
	mTM = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
	final CheckBox Flight_mode_suspend = (CheckBox)findViewById(R.id.checkBox1);        
        final CheckBox CDMA_data_on = (CheckBox)findViewById(R.id.checkBox2);
        final CheckBox WiFi_on = (CheckBox)findViewById(R.id.checkBox3);
        final CheckBox WiFi_Connected = (CheckBox)findViewById(R.id.checkBox4);
        final CheckBox BT_on = (CheckBox)findViewById(R.id.checkBox5);
        final CheckBox BT_Connected = (CheckBox)findViewById(R.id.checkBox6);
        final CheckBox Screen_on_white_color_photo = (CheckBox)findViewById(R.id.checkBox7);
        final CheckBox Screen_off_idle = (CheckBox)findViewById(R.id.checkBox8);
        final CheckBox Audio_playback_HS = (CheckBox)findViewById(R.id.checkBox9);
        final CheckBox Video_playback720p_HS = (CheckBox)findViewById(R.id.checkBox10);
        i=0;
    	Button start = (Button) findViewById(R.id.button1);
    	start.setOnClickListener(new OnClickListener(){
    		public void onClick(View arg){

	if(Flight_mode_suspend.isChecked()){
	i++;
	Intent intent = new Intent(MainActivity.this, BrowsingReciever.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", 0);
        intent.putExtras(bundle);
        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, i, intent, 0);
        long start_time = SystemClock.elapsedRealtime();
        AM.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, start_time + (i-1)*120000, pi);
	}
	
	if(CDMA_data_on.isChecked()){
	i++;
	Intent intent = new Intent(MainActivity.this, BrowsingReciever.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", 1);
        intent.putExtras(bundle);
        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, i, intent, 0);
        long start_time = SystemClock.elapsedRealtime();
        AM.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, start_time + (i-1)*120000, pi);
        }

	if(WiFi_on.isChecked()){
	i++;
	Intent intent = new Intent(MainActivity.this, BrowsingReciever.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", 2);
        intent.putExtras(bundle);
        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, i, intent, 0);
        long start_time = SystemClock.elapsedRealtime();
        AM.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, start_time + (i-1)*120000, pi);
        }

	if(WiFi_Connected.isChecked()){
	i++;
	Intent intent = new Intent(MainActivity.this, BrowsingReciever.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", 3);
        intent.putExtras(bundle);
        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, i, intent, 0);
        long start_time = SystemClock.elapsedRealtime();
        AM.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, start_time + (i-1)*120000, pi);
        }

	if(BT_on.isChecked()){
	i++;
	Intent intent = new Intent(MainActivity.this, BrowsingReciever.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", 4);
        intent.putExtras(bundle);
        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, i, intent, 0);
        long start_time = SystemClock.elapsedRealtime();
        AM.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, start_time + (i-1)*120000, pi);
        }

	if(BT_Connected.isChecked()){
	i++;
	Intent intent = new Intent(MainActivity.this, BrowsingReciever.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", 5);
        intent.putExtras(bundle);
        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, i, intent, 0);
        long start_time = SystemClock.elapsedRealtime();
        AM.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, start_time + (i-1)*120000, pi);
        }

	if(Screen_on_white_color_photo.isChecked()){
	i++;
	Intent intent = new Intent(MainActivity.this, BrowsingReciever.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", 6);
        intent.putExtras(bundle);
        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, i, intent, 0);
        long start_time = SystemClock.elapsedRealtime();
        AM.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, start_time + (i-1)*120000, pi);
        }

	if(Screen_off_idle.isChecked()){
	i++;
	Intent intent = new Intent(MainActivity.this, BrowsingReciever.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", 7);
        intent.putExtras(bundle);
        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, i, intent, 0);
        long start_time = SystemClock.elapsedRealtime();
        AM.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, start_time + (i-1)*120000, pi);
        }

	if(Audio_playback_HS.isChecked()){
	i++;
	Intent intent = new Intent(MainActivity.this, BrowsingReciever.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", 8);
        intent.putExtras(bundle);
        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, i, intent, 0);
        long start_time = SystemClock.elapsedRealtime();
        AM.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, start_time + (i-1)*120000, pi);
        }

	if(Video_playback720p_HS.isChecked()){
	i++;
	Intent intent = new Intent(MainActivity.this, BrowsingReciever.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", 9);
        intent.putExtras(bundle);
        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, i, intent, 0);
        long start_time = SystemClock.elapsedRealtime();
        AM.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, start_time + (i-1)*120000, pi);
        }


/*String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
                   
BluetoothDevice btDev = mBT.getRemoteDevice("00:1D:82:03:9E:CC");
btDev.createBond();

final UUID uuid = UUID.fromString(SPP_UUID);
 
try {  
            //ParcelUuid[] SPP_UUID = btDev.getUuids();
	    BluetoothSocket btSocket = btDev.createRfcommSocketToServiceRecord(uuid);  
            Log.d("BlueToothTestActivity", ".......");  
            btSocket.connect();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }   



Set<BluetoothDevice> pairedDevices =  BluetoothAdapter.getDefaultAdapter().getBondedDevices();

  for (BluetoothDevice device : pairedDevices) {
        String name = device.getName();
        Log.d("BT",device.getName());
        if (name.equals("Jabra BT3030")) {
            btDev = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(device.getAddress());
	    //mmDevice = device;
            break;
        }
    }


/*
mBT.getProfileProxy(mPhone.getContext(), A2dpServiceListener, BluetoothProfile.A2DP);
 
Set<BluetoothDevice> pairedDevices =  BluetoothAdapter.getDefaultAdapter().getBondedDevices();
for (BluetoothDevice device : pairedDevices) {
    String name = device.getName();
    Log.d("BT",device.getName());
    if (name.contains("Jabra BT3030")) 
	{ 
	  btDev = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(device.getAddress());
	  mService.connect(btDev); 
	}
}
*/

//BluetoothAdapter.getProfileProxy(mPhone.getContext(),A2dpServiceListener, BluetoothProfile.A2DP); 
//mBT.getProfileProxy(MainActivity.this, A2dpServiceListener, BluetoothProfile.A2DP);
//BluetoothDevice btDev = mBT.getRemoteDevice("00:1D:82:03:9E:CC");
//mService.connect(btDev);
//mBT.closeProfileProxy(BluetoothProfile.A2DP, mService);

/*
UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); // Standard SerialPortService ID

    try {
        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
        }catch(IOException io){
            Log.i("BT", "Socket Create : " + io.toString());
        }

Thread connectionThread  = new Thread(new Runnable() {
  public void run() {
   mBT.cancelDiscovery();
    try {
	mmSocket.connect();
	Log.i("BT", "bluetooth socket connected");
    
	}catch (IOException econnect) {
            Log.i("BT", "connect socket failed", econnect);
				      }
    finally{
        try {
             mmSocket.close();
             } catch (IOException e2) {
            Log.i("BT", "close socket failed", e2);
                                      }
        }
}
	
				
			});

connectionThread.start();

*/

/*
String uuidValue;
uuidValue = "00001101-0000-1000-8000-00805F9B34FB";
 
 mactekHartModemUuid = UUID.fromString(uuidValue);
 
    ConnectThread connectBtThread = new ConnectThread(btDev);
    connectBtThread.start();
*/



/*connect specifi WIFI
String networkSSID = "SSID";
String networkPass = "password";

WifiConfiguration conf = new WifiConfiguration();
conf.SSID = String.format("\"%s\"", networkSSID);
conf.preSharedKey = String.format("\"%s\"", networkPass);;
int netId = mWM.addNetwork(conf);
mWM.disconnect();
mWM.enableNetwork(netId, true);
mWM.reconnect();
*/
  


//Thread t1 = new Thread(new Runnable() {
//
// @Override
// public void run() {
//   mCM.setAirplaneMode(false);
//  }
//});
//t1.start();
//	mCM.setAirplaneMode(false);
//	sendkeyevent(KeyEvent.KEYCODE_FLIGHT_MODE_SWITCH);
//	mCM.setMobileDataEnabled(true);
//	SystemClock.sleep(20000);
//        sendkeyevent(KeyEvent.KEYCODE_HOME);
//        SystemClock.sleep(5000);
//	sendkeyevent(KeyEvent.KEYCODE_DPAD_RIGHT);
//        sendkeyevent(KeyEvent.KEYCODE_DPAD_CENTER);
//        SystemClock.sleep(5000);
//        sendkeyevent(KeyEvent.KEYCODE_DPAD_CENTER);
//        SystemClock.sleep(10000);
//        sendxy(290,290);
	
/*
	for(i=0;i<10;i++){
        Intent intent = new Intent(MainActivity.this, BrowsingReciever.class);
	Bundle bundle = new Bundle();
	bundle.putInt("id", i);
	intent.putExtras(bundle);
        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, i, intent, 0);
        long start_time = SystemClock.elapsedRealtime();
	AM.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, start_time + i*120000, pi);
	}
*/

			//String cmd ="dumpsys appops > /data/pnp/test.txt";
			//shellCmd(cmd);



//Intent intent_IntentCall = new Intent("android.intent.action.VIEW",Uri.parse("http://218.211.38.216/"));
//intent_IntentCall.setClassName("com.android.chrome","com.google.android.apps.chrome.Main");
//Intent intent_IntentCall = new Intent();
//intent_IntentCall.setClassName("com.android.settings","com.android.settings.SubSettings");
//startActivity(intent_IntentCall);
	
//String phone_number="0937427947";
//Intent intent_IntentCall = new Intent("android.intent.action.CALL",Uri.parse("tel:" + phone_number));
//startActivity(intent_IntentCall);

//SystemClock.sleep(10000);
//mTM.endCall();


		}

/*
 private synchronized int shellCmd(String cmd) {	        try {
	            ShellCommand sc = new ShellCommand(cmd, false);
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
