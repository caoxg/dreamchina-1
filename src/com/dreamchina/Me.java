package com.dreamchina;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;

import com.dreamchina.config.Api;
import com.dreamchina.util.AzkHelper;
import com.dreamchina.util.LoginUtil;
import com.dreamchina.webkit.ActivityWebViewClient;
import com.dreamchina.webkit.BaseProgressWebViewActivity;
import com.dreamchina.webkit.DetailWebView;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;

public class Me extends BaseProgressWebViewActivity {

	
	@Override
	protected void setContentView() {
		setContentView(R.layout.webview_home);
	}
	
	@Override
	protected void initStartUrl() {
		startUrl = Api.ME_RUL;
	}
	
	protected ActivityWebViewClient createWebViewClient(
			PullToRefreshWebView pullRefreshWebView) {
		return new BaseActivityWebViewClient(pullRefreshWebView, this) {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				AzkHelper.showLog("ActivityWebView::shouldOverrideUrlLoading: "
						+ url);
				if (super.shouldOverrideUrlLoading(view, url))
					return true;
				if (LoginUtil.needJump(url, startUrl)) {// 如果不是默认打开的页面
					AzkHelper.showLog("跳转出去: " + url);
					view.stopLoading();
					Intent intent = new Intent(getActivity(),
							DetailWebView.class);
					Bundle bundle = new Bundle();
					bundle.putString("url", url);
					intent.putExtras(bundle);
					getActivity().startActivity(intent);
					return true;
				}
				return false;
			}

			/**
			 * 如果没有触发shouldOverrideUrlLoading，则在此方法拦截
			 */
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				AzkHelper.showLog("ActivityWebView::onPageStarted: " + url);
				if (DetailWebView.intercept(view, url, getActivity())) {
					return;
				}
				AzkHelper.showLog("startUrl: " + startUrl);
				AzkHelper.showLog("url: " + url);
				if (LoginUtil.needJump(url, startUrl)) {// 如果不是默认打开的页面
					AzkHelper.showLog("跳转出去: " + url);
					view.stopLoading();
					Intent intent = new Intent(getActivity(),
							DetailWebView.class);
					Bundle bundle = new Bundle();
					bundle.putString("url", url);
					intent.putExtras(bundle);
					getActivity().startActivity(intent);
					return;
				}
				super.onPageStarted(view, url, favicon);
			}

		};
	}

}
