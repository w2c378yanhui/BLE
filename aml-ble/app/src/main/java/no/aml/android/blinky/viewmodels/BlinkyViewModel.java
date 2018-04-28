package no.aml.android.blinky.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.util.Log;

import no.aml.android.blinky.adapter.ExtendedBluetoothDevice;
import no.aml.android.blinky.profile.BlinkyManager;
import no.aml.android.blinky.profile.BlinkyManagerCallbacks;
import no.aml.android.blinky.R;
import no.nordicsemi.android.log.LogSession;
import no.nordicsemi.android.log.Logger;

/**
 * Created by zhenhua.he on 2018/1/16.
 */

public class BlinkyViewModel extends AndroidViewModel implements BlinkyManagerCallbacks {

    private final BlinkyManager mBlinkyManager;

    public BlinkyViewModel(@NonNull Application application) {
        super(application);

        //init manager
        mBlinkyManager = new BlinkyManager(getApplication());
        try {
            mBlinkyManager.setGattCallbacks(this);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


    private final MutableLiveData<String> mConnectionState = new MutableLiveData<>();

    // Flag to determine if the device is ready
    private final MutableLiveData<Void> mOnDeviceReady = new MutableLiveData<>();

    private final MutableLiveData<Boolean> mIsConnected = new MutableLiveData<>();

    //Device is ready ?
    public LiveData<Void> isDeviceReady() {
        return mOnDeviceReady;
    }

    //get device connect state
    public LiveData<String> getConnectionState() {
        return mConnectionState;
    }

    //is connect?
    public LiveData<Boolean> isConnected() {
        return mIsConnected;
    }


    /**
     * Connect to peripheral
     */
    public void connect(final ExtendedBluetoothDevice device) {

        final LogSession logSession = Logger.newSession(getApplication(), null, device.getAddress(), device.getName());
        mBlinkyManager.setLogger(logSession);
        mBlinkyManager.connect(device.getDevice());
        Log.e("TAG","try connecting to device " + device.getDevice().getAddress());

    }


    /**
     * Disconnect from peripheral
     */
    private void disconnect() {
        mBlinkyManager.disconnect();
    }

    public void setWifi(final String ssid, final String pwd){
        mBlinkyManager.send(ssid, pwd);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (mBlinkyManager.isConnected()) {
            disconnect();
        }
    }

    @Override
    public void onDataSent(boolean state) {
        Log.e("TAG","send data");
    }

    @Override
    public void onDeviceConnecting(BluetoothDevice device) {
        mConnectionState.postValue(getApplication().getString(R.string.state_connecting));
        Log.e("TAG","Device connecting");
    }

    @Override
    public void onDeviceConnected(BluetoothDevice device) {
        mIsConnected.postValue(true);
        mConnectionState.postValue(getApplication().getString(R.string.state_discovering_services));
        Log.e("TAG","Device connected");
    }

    @Override
    public void onDeviceDisconnecting(BluetoothDevice device) {
        mIsConnected.postValue(false);
        Log.e("TAG","device disconnect");
    }

    @Override
    public void onDeviceDisconnected(BluetoothDevice device) {
        mIsConnected.postValue(false);
        Log.e("TAG","device disconnecting");
    }

    @Override
    public void onLinklossOccur(BluetoothDevice device) {
        mIsConnected.postValue(false);
    }

    @Override
    public void onServicesDiscovered(BluetoothDevice device, boolean optionalServicesFound) {
        mConnectionState.postValue(getApplication().getString(R.string.state_initializing));
        Log.e("TAG","device discover");
    }

    @Override
    public void onDeviceReady(BluetoothDevice device) {
        mConnectionState.postValue(getApplication().getString(R.string.state_discovering_services_completed, device.getName()));
        mOnDeviceReady.postValue(null);
    }

    @Override
    public boolean shouldEnableBatteryLevelNotifications(BluetoothDevice device) {
        return false;
    }

    @Override
    public void onBatteryValueReceived(BluetoothDevice device, int value) {

    }

    @Override
    public void onBondingRequired(BluetoothDevice device) {

    }

    @Override
    public void onBonded(BluetoothDevice device) {

    }

    @Override
    public void onError(BluetoothDevice device, String message, int errorCode) {

    }

    @Override
    public void onDeviceNotSupported(BluetoothDevice device) {

    }
}
