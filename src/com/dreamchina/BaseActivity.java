package com.dreamchina;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.dreamchina.util.DialogUtils;
import com.flurry.android.FlurryAgent;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends Activity {
	
	private boolean isResumed;
	protected Dialog progressDialog;
	private String loadingMessage = "加载中";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		progressDialog = DialogUtils.initDialog(this, loadingMessage);
	}
	
	public Dialog getDialog() {
		return progressDialog;
	}
	
	public void setDialogMessage(String message) {
		if(progressDialog instanceof ProgressDialog) {
			ProgressDialog dialog = (ProgressDialog) progressDialog;
			dialog.setMessage(message);
		}
	}
	
	public void setDefaultDialogMessage() {
		setDialogMessage(loadingMessage);
	}

	@Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, getFlurryKey(this));
	}
	
	private static String flurryKey;

	public static String getFlurryKey(Context context) {
		if (!TextUtils.isEmpty(flurryKey)) {
			return flurryKey;
		}
		String ret = null;
		if (TextUtils.isEmpty(ret)) {
			ret = getMetaData(context, "FLURRY_APPKEY");
		}
		flurryKey = ret;
		return ret;
	}	

	public static String getMetaData(Context context, String name) {
		try {
			ApplicationInfo applicationInfo = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			Bundle bundle = applicationInfo.metaData;
			String value = bundle.getString(name);
			return value;
		} catch (NameNotFoundException e) {
		}
		return "";
	}	
	
	@Override
	protected void onResume() {
		super.onResume();
		if(!isResumed) {
			isResumed = true;
			firstResume();
		} else {
			notFirstResume();
		}
		MobclickAgent.onResume(this);
	}
	
	protected void firstResume() {
		
	}
	
	/*
	 * 第二次以后进入onResume执行
	 */
	protected void notFirstResume() {
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}
	
	protected Activity getActivity() {
		return this;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		DialogUtils.closeDialog(progressDialog);
	}
	
	public void goBack(View v){
		onBackPressed();
	}
	
	public void goClose(View v){
		finish();
	} 
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onBackPressed() {
		if (getParent() != null) {
			getParent().onBackPressed();
		} else {
			super.onBackPressed();
		}
	}	

}
