package org.zloy.android.compat;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;

public class AlertDialgBuilderHoneycomb extends AlertDialogBuilderCompat {

	private Builder mBuilder;
	private Context mContext;

	public AlertDialgBuilderHoneycomb(Context ctx) {
		mBuilder = new AlertDialog.Builder(ctx);
		mContext = ctx;
	}
	
	@Override
	public void setTitle(int resId) {
		mBuilder.setTitle(resId);
	}

	@Override
	public void setView(View view) {
		mBuilder.setView(view);
	}
	
	@Override
	public void setView(int resourceId) {
		mBuilder.setView(LayoutInflater.from(mContext).inflate(resourceId, null));
	}

	@Override
	public void setPositiveButton(int textId, OnClickListener clickListener) {
		mBuilder.setPositiveButton(textId, clickListener);
	}
	
	@Override
	public void setNegativeButton(int textId, OnClickListener clickListener) {
		mBuilder.setNegativeButton(textId, clickListener);
	}

	@Override
	public Dialog create() {
		return mBuilder.create();
	}

}
