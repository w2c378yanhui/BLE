package no.aml.android.blinky;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by zhenhua.he on 2018/1/16.
 */


public class SplashScreenActivity extends Activity {
	private static final int DURATION = 1000;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);

		new Handler().postDelayed(() -> {
			final Intent intent = new Intent(SplashScreenActivity.this, ScannerActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			finish();
		}, DURATION);
	}

	@Override
	public void onBackPressed() {
		// We don't want the splash screen to be interrupted
	}
}
