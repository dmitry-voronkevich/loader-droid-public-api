package org.zloy.android.downloader;

import org.zloy.android.compat.AlertDialogBuilderCompat;
import org.zloy.android.downloader.api.LoaderDroidPublicAPI;
import org.zloy.android.downloader.api.R;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class InstallLoaderDroidDialogFragment extends DialogFragment {
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		if (LoaderDroidPublicAPI.isLoaderDroidRequireUpdate(getActivity())) {
			return createUpdateDialog();
		} else {
			return createInstallDialog();
		}
	}

	private Dialog createInstallDialog() {
		AlertDialogBuilderCompat builder = AlertDialogBuilderCompat.newInstance(getActivity());
		builder.setTitle(R.string.ldapi_dialog_install_title);
		builder.setView(R.layout.ldapi_install_dialog);
		builder.setPositiveButton(R.string.ldapi_go_google_play, createGoGooglePlayListener());
		builder.setNegativeButton(android.R.string.cancel, createCancelListener());
		return builder.create();
	}

	private Dialog createUpdateDialog() {
		AlertDialogBuilderCompat builder = AlertDialogBuilderCompat.newInstance(getActivity());
		builder.setTitle(R.string.ldapi_dialog_update_title);
		builder.setView(R.layout.ldapi_update_dialog);
		builder.setPositiveButton(R.string.ldapi_go_google_play, createGoGooglePlayListener());
		builder.setNegativeButton(android.R.string.cancel, createCancelListener());
		return builder.create();
	}
	
	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		FragmentActivity activity = getActivity();
		if (activity == null)
			return;
		activity.finish();
	}

	private OnClickListener createCancelListener() {
		return new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				FragmentActivity activity = getActivity();
				if (activity == null)
					return;
				activity.finish();
			}
		};
	}
	
	private OnClickListener createGoGooglePlayListener() {
		return new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(LoaderDroidPublicAPI.LOADER_DROID_MARKET_URI);
				try {
					startActivity(intent);
				} catch (ActivityNotFoundException e) {
					Toast.makeText(getActivity(), getString(R.string.ldapi_no_google_play_toast), Toast.LENGTH_SHORT).show();
				}
			}
		};
	}
}
