package no.aml.android.blinky.profile;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;

import java.util.Deque;
import java.util.LinkedList;
import java.util.UUID;

import no.nordicsemi.android.ble.BleManager;

/**
 * Created by zhenhua.he on 2018/1/16.
 */


public class BlinkyManager extends BleManager<BlinkyManagerCallbacks> {

    /**
     * Amlogic ble Service UUID
     */
    public final static UUID BLE_UUID_SERVICE = UUID.fromString("0000180A-0000-1000-8000-00805F9B34FB");

    /**
     * Amlogic ble Wifi set service CHAR
     */
    public final static UUID BLE_UUID_WIFI_CHAR = UUID.fromString("00009999-0000-1000-8000-00805F9B34FB");

    private BluetoothGattCharacteristic mWifiCharacteristic;

    public BlinkyManager(final Context context) {
        super(context);
    }

    @Override
    protected BleManagerGattCallback getGattCallback() {
        return mGattCallback;
    }

    @Override
    protected boolean shouldAutoConnect() {
        //If you want to connect to the device using autoConnect flag = true
        return super.shouldAutoConnect();
    }

    private final BleManagerGattCallback mGattCallback = new BleManagerGattCallback() {

        @Override
        protected Deque<Request> initGatt(BluetoothGatt gatt) {
            final LinkedList<Request> requests = new LinkedList<>();
            requests.push(Request.newReadRequest(mWifiCharacteristic));
            Log.e("huihui", "try get request for CHAR");
            return requests;
        }


        @Override
        protected boolean isRequiredServiceSupported(BluetoothGatt gatt) {
            final BluetoothGattService service = gatt.getService(BLE_UUID_SERVICE);

            if (service != null) {
                mWifiCharacteristic = service.getCharacteristic(BLE_UUID_WIFI_CHAR);
            }

            boolean writeRequest = true;

            if (mWifiCharacteristic != null) {
                final int rxProperties = mWifiCharacteristic.getProperties();
                writeRequest = (rxProperties & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0;
            }
            return mWifiCharacteristic != null && writeRequest;
        }


        @Override
        protected void onDeviceDisconnected() {
            mWifiCharacteristic = null;
        }

        @Override
        protected void onCharacteristicRead(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            //do something with read
            Log.e("huihui", "Read data");
        }

        @Override
        public void onCharacteristicWrite(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            Log.e("huihui", "SEND SUCCESS");
            //i may can do something with the return;

        }

        @Override
        public void onCharacteristicNotified(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            //i can do something with notify
        }
    };

    public void send(String wifiSSID, String wifiPWD) {

        StringBuffer sb = null;

        //make sure our service is online
        if (mWifiCharacteristic == null)
            return;
        byte[] ch = new byte[85];
        ch[0] = 0x01;
        char[] str = "amlogicblewifisetup".toCharArray();
        for (int i = 0; i < 19; i++) {
            ch[i + 1] = (byte) str[i];
        }

        Log.e("SSID && PWD ", wifiSSID + "," + wifiPWD);
        for (int i = 0; i < 32; i++) {
            if (i < wifiSSID.length()) {
                ch[i + 1 + 19] = (byte) wifiSSID.charAt(i);

            } else
                ch[i + 1 + 19] = 0;
        }
        for (int i = 0; i < 32; i++) {


            if (i < wifiPWD.length()) {
                ch[i + 1 + 19 + 32] = (byte) wifiPWD.charAt(i);
            } else
                ch[i + 1 + 19 + 32] = 0;
        }
        ch[19 + 32 + 32 + 1] = 0x04;
        mWifiCharacteristic.setValue(ch);
//		mWifiCharacteristic.setValue(bb.array());
        writeCharacteristic(mWifiCharacteristic);
    }
}
