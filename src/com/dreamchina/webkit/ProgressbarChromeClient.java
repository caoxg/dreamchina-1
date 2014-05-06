package com.dreamchina.webkit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class ProgressbarChromeClient extends WebChromeClient {

	private ProgressBar progressBar;
	private Activity activity;

	public ProgressbarChromeClient(ProgressBar progressBar, Activity activity) {
		this.progressBar = progressBar;
		this.activity = activity;
	}

	@Override
	public boolean onJsPrompt(WebView view, String url, String message,
			String defaultValue, JsPromptResult result) {
		return super.onJsPrompt(view, url, message, defaultValue, result);
	}

	@Override
	public void onGeolocationPermissionsShowPrompt(String origin,
			Callback callback) {
		callback.invoke(origin, true, false);
	}

	@Override
	public boolean onJsAlert(WebView view, String url, String message,
			JsResult result) {
		if (!TextUtils.isEmpty(message)) {
			result.cancel();
			// android.view.WindowManager$BadTokenException: Unable to add
			// window
			if (!activity.isFinishing()) {
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setMessage(message).setPositiveButton("确定", null)
						.show();
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean onJsConfirm(WebView view, String url, String message,
			final JsResult result) {
		if (activity.isFinishing())
			return false;
		Builder builder = new Builder(activity);
		builder.setTitle("提示").setIcon(
				activity.getResources().getDrawable(
						android.R.drawable.ic_dialog_info));
		builder.setMessage(message);
		builder.setPositiveButton(android.R.string.ok,
				new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						result.confirm();
					}

				});
		builder.setNeutralButton(android.R.string.cancel,
				new AlertDialog.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						result.cancel();
					}

				});
		builder.setCancelable(false);
		builder.create();
		builder.show();
		return true;
	}

	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		progressBar.setProgress(newProgress);
		if (newProgress > 0)
			progressBar.setVisibility(View.VISIBLE);
		if (newProgress == 100)
			progressBar.setVisibility(View.GONE);
		super.onProgressChanged(view, newProgress);
	}
}
