package no.aml.android.blinky.viewmodels;

import android.arch.lifecycle.LiveData;
import android.net.wifi.ScanResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhenhua.he on 2018/1/16.
 */

public class WifiLiveData extends LiveData<WifiLiveData>{
    private final List<ScanResult> wifilist = new ArrayList<ScanResult>();
    private Integer mUpdatedWifiIndex;
    private boolean mScanningStarted;
    private boolean mWifiEnabled;
    private boolean mLocationEnabled;

    /*package*/ WifiLiveData(final boolean WifiEnabled, final boolean LocationEnabled) {
        mScanningStarted = false;
        mWifiEnabled = WifiEnabled;
        mLocationEnabled = LocationEnabled;
        postValue(this);
    }

    /* package */ void refresh() {
        postValue(this);
    }

    /* package */ void scanningStarted() {
        mScanningStarted = true;
        postValue(this);
    }

    /* package */ void scanningStopped() {
        mScanningStarted = false;
        postValue(this);
    }

    /* package */ void wifiEnabled() {
        mWifiEnabled = true;
        postValue(this);
    }

    /* package */ void wifiDisabled() {
        mWifiEnabled = false;
        postValue(this);
    }

    /* package */ void setLocationEnabled(final boolean enabled) {
        mLocationEnabled = enabled;
        postValue(this);
    }

    /* package */ void wifiDiscovered(final ScanResult result) {
        final int index = indexOf(result);
        if (index == -1) {
            wifilist.add(result);
            mUpdatedWifiIndex = null;
        }
        else {
            mUpdatedWifiIndex = index;
        }

        postValue(this);
    }


    /**
     * Returns the list of devices.
     * @return current list of devices discovered
     */
    @NonNull
    public List<ScanResult> getDevices() {
        return wifilist;
    }

    /**
     * Returns null if a new wifi was added, or an index of the updated device.
     */
    @Nullable
    public Integer getUpdatedWifiIndex() {
        final Integer i = mUpdatedWifiIndex;
        mUpdatedWifiIndex = null;
        return i;
    }

    /**
     * Returns whether the list is empty.
     */
    public boolean isEmpty() {
        return wifilist.isEmpty();
    }

    /**
     * Returns whether scanning is in progress.
     */
    public boolean isScanning() {
        return mScanningStarted;
    }

    /**
     * Returns whether wifi adapter is enabled.
     */
    public boolean ismWifiEnabled() {
        return mWifiEnabled;
    }

    /**
     * Returns whether Location is enabled.
     */
    public boolean isLocationEnabled() {
        return mLocationEnabled;
    }



    /**
     * Finds the index of existing wifi on the scan results list.
     *
     * @param result scan result
     * @return index of -1 if not found
     */
    private int indexOf(final ScanResult result) {
        int i = 0;
        for (final ScanResult wifi_ret : wifilist) {
            if (wifi_ret.SSID.equals(result.SSID))
                return i;
            i++;
        }
        return -1;
    }



}
