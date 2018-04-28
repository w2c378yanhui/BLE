package no.aml.android.blinky.viewmodels;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import no.aml.android.blinky.adapter.ExtendedBluetoothDevice;
import no.nordicsemi.android.support.v18.scanner.ScanResult;

/**
 * Created by zhenhua.he on 2018/1/16.
 */

public class ScannerLiveData extends LiveData<ScannerLiveData> {
	private final List<ExtendedBluetoothDevice> mDevices = new ArrayList<>();
	private Integer mUpdatedDeviceIndex;
	private boolean mScanningStarted;
	private boolean mBluetoothEnabled;
	private boolean mLocationEnabled;

	/* package */ ScannerLiveData(final boolean bluetoothEnabled, final boolean locationEnabled) {
		mScanningStarted = false;
		mBluetoothEnabled = bluetoothEnabled;
		mLocationEnabled = locationEnabled;
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

	/* package */ void bluetoothEnabled() {
		mBluetoothEnabled = true;
		postValue(this);
	}

	/* package */ void bluetoothDisabled() {
		mBluetoothEnabled = false;
		mUpdatedDeviceIndex = null;
		mDevices.clear();
		postValue(this);
	}

	/* package */ void setLocationEnabled(final boolean enabled) {
		mLocationEnabled = enabled;
		postValue(this);
	}

	/* package */ void deviceDiscovered(final ScanResult result) {
		ExtendedBluetoothDevice device;

		final int index = indexOf(result);
		if (index == -1) {
			device = new ExtendedBluetoothDevice(result);
			mDevices.add(device);
			mUpdatedDeviceIndex = null;
		} else {
			device = mDevices.get(index);
			mUpdatedDeviceIndex = index;
		}
		// Update RSSI and name
		device.setRssi(result.getRssi());
		device.setName(result.getScanRecord().getDeviceName());

		postValue(this);

	}

	/**
	 * Returns the list of devices.
	 * @return current list of devices discovered
	 */
	@NonNull
	public List<ExtendedBluetoothDevice> getDevices() {
		return mDevices;
	}

	/**
	 * Returns null if a new device was added, or an index of the updated device.
	 */
	@Nullable
	public Integer getUpdatedDeviceIndex() {
		final Integer i = mUpdatedDeviceIndex;
		mUpdatedDeviceIndex = null;
		return i;
	}

	/**
	 * Returns whether the list is empty.
	 */
	public boolean isEmpty() {
		return mDevices.isEmpty();
	}

	/**
	 * Returns whether scanning is in progress.
	 */
	public boolean isScanning() {
		return mScanningStarted;
	}

	/**
	 * Returns whether Bluetooth adapter is enabled.
	 */
	public boolean isBluetoothEnabled() {
		return mBluetoothEnabled;
	}

	/**
	 * Returns whether Location is enabled.
	 */
	public boolean isLocationEnabled() {
		return mLocationEnabled;
	}

	/**
	 * Finds the index of existing devices on the scan results list.
	 *
	 * @param result scan result
	 * @return index of -1 if not found
	 */
	private int indexOf(final ScanResult result) {
		int i = 0;
		for (final ExtendedBluetoothDevice device : mDevices) {
			if (device.matches(result))
				return i;
			i++;
		}
		return -1;
	}
}
