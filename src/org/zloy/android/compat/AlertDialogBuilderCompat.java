package org.zloy.android.compat;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.View;

public abstract class AlertDialogBuilderCompat {
	public abstract void setTitle(int resId);
	public abstract void setView(View view);
	public abstract void setView(int resourceId);
	public abstract void setPositiveButton(int textId, DialogInterface.OnClickListener clickListener);
	public abstract void setNegativeButton(int textId, DialogInterface.OnClickListener clickListener);
	public abstract Dialog create();
	
	public static AlertDialogBuilderCompat newInstance(Context ctx) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			return new AlertDialgBuilderHoneycomb(ctx);
		} else {
			return new AlertDialogBuiderEmulation(ctx);
		}
	}
	
	AlertDialogBuilderCompat() {}; // eclipse always tries to create new class if mistakenly typed new AlertDialogBuilderCompat
}
