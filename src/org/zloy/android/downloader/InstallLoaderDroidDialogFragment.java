package org.zloy.android.downloader;

import org.zloy.android.downloader.api.LoaderDroidPublicAPI;
import org.zloy.android.downloader.api.R;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

public class InstallLoaderDroidDialogFragment extends DialogFragment {
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog result = super.onCreateDialog(savedInstanceState);
		
		if (LoaderDroidPublicAPI.isLoaderDroidRequireUpdate(getActivity())) {
			result.setTitle(R.string.ldapi_dialog_update_title);
		} else {
			result.setTitle(R.string.ldapi_dialog_install_title);
		}
		return result;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (LoaderDroidPublicAPI.isLoaderDroidRequireUpdate(getActivity()))
			return inflater.inflate(R.layout.ldapi_update_dialog, container, false);
		return inflater.inflate(R.layout.ldapi_install_dialog, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		view.findViewById(R.id.btn_goto_google_play).setOnClickListener(createGoGooglePlayListener());
		view.findViewById(R.id.btn_goto_slideme).setOnClickListener(createGoSlideMeListener());
		view.findViewById(R.id.btn_cancel).setOnClickListener(createCancelListener());
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
			public void onClick(View view) {
				FragmentActivity activity = getActivity();
				if (activity == null)
					return;
				activity.finish();
			}
		};
	}
	
	private OnClickListener createGoSlideMeListener() {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(LoaderDroidPublicAPI.LOADER_DROID_SLIDEME_URI);
				try {
					startActivity(intent);
				} catch (ActivityNotFoundException e) {
					Toast.makeText(getActivity(), getString(R.string.ldapi_no_google_play_toast), Toast.LENGTH_SHORT).show();
				}
			}
		};
	}
	
	private OnClickListener createGoGooglePlayListener() {
		return new OnClickListener() {
			
			@Override
			public void onClick(View view) {
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
