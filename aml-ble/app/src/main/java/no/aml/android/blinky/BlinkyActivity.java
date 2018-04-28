package no.aml.android.blinky;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import no.aml.android.blinky.adapter.ExtendedBluetoothDevice;
import no.aml.android.blinky.viewmodels.BlinkyViewModel;

/**
 * Created by zhenhua.he on 2018/1/16.
 */

public class BlinkyActivity extends AppCompatActivity {
    public static final String EXTRA_DEVICE = "EXTRA_DEVICE";
    private BlinkyViewModel viewModel;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blinky);

        final Intent intent = getIntent();
        final ExtendedBluetoothDevice device = intent.getParcelableExtra(EXTRA_DEVICE);
        final String deviceName = device.getName();
        final String deviceAddress = device.getAddress();
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(deviceName);
        getSupportActionBar().setSubtitle(deviceAddress);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Configure the view model
        viewModel = ViewModelProviders.of(this).get(BlinkyViewModel.class);
        viewModel.connect(device);

        final LinearLayout progressContainer = findViewById(R.id.progress_container);
        final TextView connectionState = findViewById(R.id.connection_state);
        final View content = findViewById(R.id.device_container);

        //set up views
        final TextView wifiState = findViewById(R.id.wifi_state);
        final CardView wifiCard = findViewById(R.id.wifi_card);

        wifiCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setView();
            }
        });

        viewModel.isDeviceReady().observe(this, deviceReady -> {
            progressContainer.setVisibility(View.GONE);
            content.setVisibility(View.VISIBLE);
        });
        viewModel.getConnectionState().observe(this, connectionState::setText);
        viewModel.isConnected().observe(this, connected -> {
            Log.e("huihui", "connected " +connected);
            if (!connected) {
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    private void setView() {

        /**
         * set Activity view to wifi list
         */
        Log.e("huihui", "try set view");
        FragmentManager mfragmentManager = getFragmentManager();
        FragmentTransaction mfragmentTransaction = mfragmentManager.beginTransaction();

        WifiFragment wifiFragment = new WifiFragment();

        mfragmentTransaction.add(R.id.blink_id, wifiFragment);
        mfragmentTransaction.commit();
        Log.e("huihui", "setview success");
    }

    public void setWifi(String wifiSsid, String wifiPwd) {
        viewModel.setWifi(wifiSsid, wifiPwd);
    }
}
