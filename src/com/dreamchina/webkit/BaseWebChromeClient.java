package com.dreamchina.webkit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.text.TextUtils;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class BaseWebChromeClient extends WebChromeClient {
	private Dialog progressDialog;
	private Activity activity;

	public BaseWebChromeClient(Activity activity, Dialog progressDialog) {
		this.progressDialog = progressDialog;
		this.activity = activity;
	}
	
	@Override
	public boolean onJsPrompt(WebView view, String url, String message,
			String defaultValue, JsPromptResult result) {
		return super.onJsPrompt(view, url, message, defaultValue,
				result);
	}

	@Override
	public void onGeolocationPermissionsShowPrompt(String origin,
			Callback callback) {
		callback.invoke(origin, true, false);
	}

	@Override
	public boolean onJsAlert(WebView view, String url, String message,
			JsResult result) {
//		result.confirm();
//		return super.onJsAlert(view, url, message, result);
		if (!TextUtils.isEmpty(message)) {
			result.cancel();
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setMessage(message).setPositiveButton("确定", null).show();
			return true;
		}
		return false;
	}

	@Override
	public void onReceivedTitle(WebView view, String title) {
		super.onReceivedTitle(view, title);
		if(progressDialog != null)
			progressDialog.hide();
	}

	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		super.onProgressChanged(view, newProgress);
	}	
}
