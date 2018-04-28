package no.aml.android.blinky.profile;

import no.nordicsemi.android.ble.BleManagerCallbacks;

/**
 * Created by zhenhua.he on 2018/1/16.
 */


public interface BlinkyManagerCallbacks extends BleManagerCallbacks {


	/**
	 * Called when the data has been sent to the connected device.
	 *
	 * @param state true when wif was connect, false when disabled
	 */
	void onDataSent(final boolean state);
}
