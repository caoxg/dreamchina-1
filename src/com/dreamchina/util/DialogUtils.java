package com.dreamchina.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

public class DialogUtils {
	public static ProgressDialog initDialog(Context context) {
		return initDialog(context, "加载中...");
	}
	public static ProgressDialog initDialog(Context context, String message) {
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(message);
		progressDialog.setCancelable(true);
		return progressDialog;
	}

	public static void closeDialog(Dialog dialog) {
		if(dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}
}
