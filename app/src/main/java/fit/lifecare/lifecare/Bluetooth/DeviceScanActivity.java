package fit.lifecare.lifecare.Bluetooth;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import java.util.List;
import java.util.UUID;

public class DeviceScanActivity extends ListActivity {

    private Activity mainAppActivity;
    
    private String readed_value;

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;
    
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;
    
    public final static String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";
    
    public Boolean mScanning;
    private static final int SCAN_PERIOD = 10000;
    private boolean start_clicked = false;
    private boolean isConnected = false;
    
    private static final String TAG = "BluTutScan";
    
    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            
            Log.d(TAG, "haciii");
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Log.d(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.d(TAG, "Attempting to start service discovery:" +
                        mBluetoothGatt.discoverServices());
                
                
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.d(TAG, "Disconnected from GATT server.");
                broadcastUpdate(intentAction);
            }
        }
        
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
    
            if (status == BluetoothGatt.GATT_SUCCESS) {
                
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
    
                for (BluetoothGattService gattService : gatt.getServices()) {
                    Log.i(TAG, "onServicesDiscovered: ---------------------");
                    Log.i(TAG, "onServicesDiscovered: service=" + gattService.getUuid());
                    for (BluetoothGattCharacteristic characteristic : gattService.getCharacteristics()) {
                        Log.i(TAG, "onServicesDiscovered: characteristic=" + characteristic.getUuid());
            
                        if (characteristic.getUuid().toString().equals("0972ef8c-7613-4075-ad52-756f33d4da91")) {
                
                            Log.w(TAG, "onServicesDiscovered: found Lifecare");
                            gatt.setCharacteristicNotification(characteristic,true);

                            BluetoothGattDescriptor descriptor = characteristic.getDescriptor( UUID.fromString("00002902-0000-1000-8000-00805f9b34fb") );
                            //BluetoothGattDescriptor descriptor = characteristic.getDescriptor( UUID.fromString("00002902-0000-1000-8000-00805f9b34fb") );
                            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            mBluetoothGatt.writeDescriptor(descriptor);
                            
                        }
                        
                    }
                }
            
            } else {
                Log.d(TAG, "onServicesDiscovered received: " + status);
            }
        }
    
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
    
            Log.d(TAG, " read" + characteristic.getValue().toString() );
        }
    
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.d(TAG, " write" );
        }
    
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            readed_value = new String(characteristic.getValue());
            Log.d(TAG, " changed " + readed_value);
            if (start_clicked) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, readed_value);
            }
        }
    };
    
    private ScanCallback mLeScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
    
            if(!isConnected) {

                if (result.getDevice().getAddress().equals("80:7D:3A:F2:31:96")) {
                    
                    isConnected = true;
                    mBluetoothGatt = result.getDevice().connectGatt( mainAppActivity,false, mGattCallback);
                }
            }
            
            super.onScanResult(callbackType, result);
         }
        
        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            Log.d(TAG ,"bu ne:" + results.get(0).toString());
            super.onBatchScanResults(results);
        }
        
        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };
    
    public void disconnectDevice() {
    
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        isConnected = false;
        if (mBluetoothGatt != null)
            mBluetoothGatt.disconnect();
    }

    public DeviceScanActivity(Activity mainActivity) {
        
        mainAppActivity = mainActivity;
        BluetoothManager bluetoothManager = (BluetoothManager) mainActivity.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        
    }
    
    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        
        mainAppActivity.sendBroadcast(intent);
    }
    
    private void broadcastUpdate(final String action, final  String readed_value) {
        
        final Intent intent = new Intent(action);
        
        intent.putExtra(EXTRA_DATA, readed_value);
        mainAppActivity.sendBroadcast(intent);
    }
    
    
    public void scanLeDevice(final boolean enable) {
        
        final BluetoothLeScanner bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        
        if (enable) {
            // Stops scanning after a pre-defined scan period.
    
            Handler mHandler = new Handler();
            
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    
                    bluetoothLeScanner.stopScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);
            
            mScanning = true;
            bluetoothLeScanner.startScan(mLeScanCallback);
        } else {
            mScanning = false;
            bluetoothLeScanner.stopScan(mLeScanCallback);
        }
    }
    
    public void setStartClicked(Boolean isClicked) {
        
        this.start_clicked = isClicked;
    }

    public void writeToDevice(String str) {
    
        UUID uid_service = UUID.fromString("ab0828b1-198e-4351-b779-901fa0e0371e");
        UUID uid_characteristic = UUID.fromString("4ac8a682-9736-4e5d-932b-e9b31405049c");
    
        if(mBluetoothGatt != null) {
    
            Log.d("sorun", "burada");
            BluetoothGattCharacteristic characteristic = mBluetoothGatt.getService(uid_service).getCharacteristic(uid_characteristic);
    
            byte[] b = str.getBytes();
    
            characteristic.setValue(b); // call this BEFORE(!) you 'write' any stuff to the server
            mBluetoothGatt.writeCharacteristic(characteristic);
    
        }
    }
}
