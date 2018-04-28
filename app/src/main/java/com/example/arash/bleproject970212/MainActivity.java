package com.example.arash.bleproject970212;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG1="MyDebug";
    private boolean mScanning;
    BluetoothDevice bluetoothDevice;
    private BluetoothLeScanner bluetoothLeScanner;
    private BluetoothAdapter mBluetoothAdapter;
    //private Handler mHandler;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 10000;      // Stops scanning after 10 seconds.


    /*
     **
     **
     **
     */
    private ScanCallback mLeScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            bluetoothDevice = result.getDevice();
            Log.v(TAG1,"Result:"+ bluetoothDevice);
            Log.v(TAG1,"adr:"+bluetoothDevice.getAddress());
            Log.v(TAG1, "Name:"+bluetoothDevice.getName());
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.v(TAG1, "Scanning Failed " + errorCode);
        }
    };/** end of private ScanCallback mLeScanCallback = new ScanCallback()*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(this.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        Log.v(TAG1, "Bonded Devices"+mBluetoothAdapter.getBondedDevices());
        Log.v(TAG1, "NameBLE:"+mBluetoothAdapter.getName());
        Log.v(TAG1, "get BLE adaprtor info");
        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        // set on click scan for button
        Button ib= (Button) findViewById(R.id.scanbutton);
        ib.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ScanBLEDevices(true);
            }
        });


    }/*end of  protected void onCreate(Bundle...*/

    /*
     **
     **
     **
     */
    private void ScanBLEDevices(final boolean enable) {
        bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        Handler mHandler= new Handler();
        if (enable) {
            // Stops scanning after a pre-defined scan period.
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
    }/*end of private void ScanBLEDevices(final boole...*/


}
