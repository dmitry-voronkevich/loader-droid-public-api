package org.zloy.android.compat;

import org.zloy.android.downloader.api.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class AlertDialogBuiderEmulation extends AlertDialogBuilderCompat {

	private Dialog mDialog;
	private FrameLayout mDialogViewContent;

	public AlertDialogBuiderEmulation(Context ctx) {
		TypedValue outValue = new TypedValue();
		ctx.getTheme().resolveAttribute(R.attr.ldapi_dialogCompatStyle, outValue, false);
		mDialog = new Dialog(ctx, outValue.data) {
			// Workaround for http://code.google.com/p/android/issues/detail?id=4936
			@Override
			public void dismiss() {
				super.dismiss();
				mDialogViewContent.removeAllViews();
			}
		};
		mDialog.setContentView(R.layout.ldapi_alert_dialog_content);
		mDialogViewContent = (FrameLayout) mDialog.findViewById(R.id.dialog_content);
	}
	
	@Override
	public void setTitle(int resId) {
    	mDialog.setTitle(resId);
	}

	@Override
	public void setView(View view) {
		mDialogViewContent.addView(view);
	}
	
	@Override
	public void setView(int resourceId) {
		View view = LayoutInflater.from(mDialogViewContent.getContext()).inflate(resourceId, null);
		setView(view);
	}

	@Override
	public void setPositiveButton(final int textId, final OnClickListener clickListener) {
		initButton(R.id.button1, textId, clickListener);
	}
	
	@Override
	public void setNegativeButton(int textId, OnClickListener clickListener) {
		initButton(R.id.button3, textId, clickListener);
	}

	private void initButton(int buttonId, final int textId, final OnClickListener clickListener) {
		Button button1 = (Button) mDialog.findViewById(buttonId);
		button1.setVisibility(View.VISIBLE);
		button1.setText(textId);
		button1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (clickListener != null)
					clickListener.onClick(mDialog, AlertDialog.BUTTON_POSITIVE);
				mDialog.dismiss();
			}
		});
	}
	
	@Override
	public Dialog create() {
		return mDialog;
	}

}
