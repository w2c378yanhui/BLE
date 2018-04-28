package no.aml.android.blinky;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import no.aml.android.blinky.adapter.WifiAdapter;


/**
 * Created by zhenhua.he on 2018/1/16.
 */

public class WifiFragment extends Fragment {

    private WifiManager wifiManager;
    private List<ScanResult> wifiList;
    private String WIFI_SSID;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
	super.onViewCreated(view, savedInstanceState);
	initWifiDiscover();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        /**
         * init Wifi Fragment
         */
	Log.e("TAG", "Creat the wifi fragment");
	View view= inflater.inflate(R.layout.activity_wifi, container, false);
	Log.e("TAG", "set view");

	Log.e("TAG", "start discover");
	return view;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initWifiDiscover() {
        /**
         * init wifi discover manager
         */
	wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
	startWifiDiscovery();
	wifiList = wifiManager.getScanResults();
	ListView wifiListView = getActivity().findViewById(R.id.wifi_list);

	if (wifiList == null) {
		Toast.makeText(getActivity(), "noting to find , please check your wifi is open",Toast.LENGTH_LONG).show();
        }else {
		wifiListView.setAdapter(new WifiAdapter(getActivity(),wifiList));
        }
        wifiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

		AlertDialog.Builder pwdAlert = new AlertDialog.Builder(getActivity());

		WIFI_SSID = wifiList.get(i).SSID;

		pwdAlert.setTitle(WIFI_SSID);

		pwdAlert.setMessage("please enter password ");

		final EditText etPwd = new EditText(getActivity());

		final SharedPreferences preferences = getActivity().getSharedPreferences("WIFI_PWD", Context.MODE_PRIVATE);

		etPwd.setText(preferences.getString(WIFI_SSID, ""));

		pwdAlert.setView(etPwd);

		pwdAlert.setPositiveButton("Connect", new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialogInterface, int i) {
			Toast.makeText(getActivity(), WIFI_SSID + ',' + etPwd.getText(), Toast.LENGTH_LONG).show();

			BlinkyActivity blinkyActivity = (BlinkyActivity)getActivity();
			blinkyActivity.setWifi(WIFI_SSID, etPwd.getText().toString());
		}
		});

		pwdAlert.create();
		pwdAlert.show();
		}
	});

	}

    private void startWifiDiscovery(){
        /**
         * start discovery
         */
	if (!wifiManager.isWifiEnabled()) {
	wifiManager.setWifiEnabled(true);
        }
    }

}
