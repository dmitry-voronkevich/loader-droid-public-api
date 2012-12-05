package org.zloy.android.downloader;

import org.zloy.android.downloader.api.LoaderDroidPublicAPI;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

public class AddLoadingActivity extends FragmentActivity {
	private static final String TAG_DIALOG = "dialog";
	private static final int REQUEST_CODE_ADD_LOADING = 100;
	
	private BroadcastReceiver mInstallationReceiver;
	private IntentFilter mInstallationFilter;
	private boolean mIsInstallationReceiverRegistered = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mInstallationReceiver = null;
		mInstallationFilter = null;
	}

	@Override
	protected void onResume() {
		super.onResume();
		// This is done in on resume because when user did not have LD installed and was asked to install it
		// He might already return back to this activity
		if (LoaderDroidPublicAPI.isLoaderDroidInstalled(this)) {
			delegateLoadingToLoaderDroid();
		} else {
			askUserToInstallLoaderDroid();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mIsInstallationReceiverRegistered) {
			unregisterReceiver(mInstallationReceiver);
			mIsInstallationReceiverRegistered = false;
		}
	}
	
	private void delegateLoadingToLoaderDroid() {
		Intent intent = new Intent(LoaderDroidPublicAPI.ACTION_ADD_LOADING);
		intent.setData(getIntent().getData());
		intent.putExtras(getIntent().getExtras());
		try {
			startActivityForResult(intent, REQUEST_CODE_ADD_LOADING);
		} catch (ActivityNotFoundException e) {
			askUserToInstallLoaderDroid();
		}
	}
	
	private void askUserToInstallLoaderDroid() {
		if (!mIsInstallationReceiverRegistered) {
			if (mInstallationReceiver == null) {
				mInstallationReceiver = createInstallationReceiver();
				mInstallationFilter = createInstallationFilter();
			}
			registerReceiver(mInstallationReceiver, mInstallationFilter);
			mIsInstallationReceiverRegistered = true;
		}
		new InstallLoaderDroidDialogFragment().show(getSupportFragmentManager(), TAG_DIALOG);
	}
	
	private BroadcastReceiver createInstallationReceiver() {
		return new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (LoaderDroidPublicAPI.isLoaderDroidInstalled(AddLoadingActivity.this)) {
					hideDialog();
					delegateLoadingToLoaderDroid();
				}
			}
		};
	}
	void hideDialog() {
		DialogFragment fragment = (DialogFragment) getSupportFragmentManager().findFragmentByTag(TAG_DIALOG);
		if (fragment != null) {
			fragment.dismiss();
		}
	}
	
	@SuppressWarnings("deprecation")
	private IntentFilter createInstallationFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
		filter.addAction(Intent.ACTION_PACKAGE_INSTALL);
		filter.addDataScheme("package");
		return filter;
	}

	@Override
	protected void onActivityResult(int requestCode, int result, Intent intent) {
		super.onActivityResult(requestCode, result, intent);
		if (requestCode == REQUEST_CODE_ADD_LOADING)
			finish();
	}

}
