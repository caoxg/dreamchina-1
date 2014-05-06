package com.dreamchina.webkit;

import android.app.Activity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dreamchina.util.AzkHelper;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;

public class ActivityWebViewClient extends WebViewClient {
	
	private PullToRefreshWebView pullRefreshWebView;
	private Activity activity;
	public ActivityWebViewClient(PullToRefreshWebView pullRefreshWebView, Activity activity) {
		this.pullRefreshWebView = pullRefreshWebView;
		this.activity = activity;
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		AzkHelper.showLog("ActivityWebViewClient::onPageFinished: " + url);
		if(pullRefreshWebView != null)
			pullRefreshWebView.onRefreshComplete();
		super.onPageFinished(view, url);
	}

	@Override
	public void onReceivedError(WebView view, int errorCode,
			String description, String failingUrl) {
		AzkHelper.showErrorLog("onReceivedError: " + failingUrl);
		if (WebViewClient.ERROR_HOST_LOOKUP == errorCode
				|| WebViewClient.ERROR_TIMEOUT == errorCode
				|| WebViewClient.ERROR_CONNECT == errorCode) {
			if (WebViewClient.ERROR_TIMEOUT == errorCode)
				AzkHelper.showToast(activity, "网络连接超时");
			if (WebViewClient.ERROR_HOST_LOOKUP == errorCode)
				AzkHelper.showToast(activity, "域名解析出错了");
			if (WebViewClient.ERROR_CONNECT == errorCode)
				AzkHelper.showToast(activity, "连接服务器失败了");
			// AzkHelper.networkNotAvailable(view, failingUrl);
		} else {
			super.onReceivedError(view, errorCode, description,
					failingUrl);
		}
	}	
}
