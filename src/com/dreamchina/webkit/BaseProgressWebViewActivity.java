package com.dreamchina.webkit;

import org.apache.http.protocol.HTTP;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dreamchina.BaseActivity;
import com.dreamchina.R;
import com.dreamchina.util.AzkHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;

public class BaseProgressWebViewActivity extends BaseActivity implements
		OnRefreshListener<WebView> {

	public WebView webView;
	private ProgressBar progressBar;
	public PullToRefreshWebView pullRefreshWebView;
	public String startUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView();
		progressBar = (ProgressBar) findViewById(R.id.progress);
		pullRefreshWebView = (PullToRefreshWebView) findViewById(R.id.webview);
		pullRefreshWebView.setOnRefreshListener(this);
		initWebView();
		initStartUrl();
		initTitle();
		if (TextUtils.isEmpty(startUrl)) {
			AzkHelper.showToast(this, "url 为空");
			finish();
		}
		webView.loadUrl(startUrl);
		onCreated();
	}

	protected void onCreated() {
		View actionbarView = findViewById(R.id.action_bar);
		if (actionbarView != null) {
			actionbarView.setClickable(true);
			actionbarView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (webView != null) {
						webView.scrollTo(0, 0);
					}
				}
			});
		}
	}

	private String title;
	private void initTitle() {
		title = getIntent().getStringExtra("title");
		TextView titleTextView = (TextView) findViewById(R.id.title);
		if (!TextUtils.isEmpty(title) && titleTextView != null) {
			titleTextView.setText(title);
		}
	}

	protected void setContentView() {
		setContentView(R.layout.webview_detail);
	}

	protected void initStartUrl() {
		String url = getIntent().getStringExtra("url");
		startUrl = url;
	}

	protected ActivityWebViewClient createWebViewClient(
			PullToRefreshWebView pullRefreshWebView) {
		return new BaseActivityWebViewClient(pullRefreshWebView, this);
	}

	protected class BaseActivityWebViewClient extends ActivityWebViewClient {
		public BaseActivityWebViewClient(
				PullToRefreshWebView pullRefreshWebView, Activity activity) {
			super(pullRefreshWebView, activity);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (intercept(view, url, getActivity())) {
				return true;
			}
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			AzkHelper.showLog("onPageStarted: " + url);
			if (intercept(view, url, getActivity())) {
				return;
			}
			super.onPageStarted(view, url, favicon);
			if(TextUtils.isEmpty(title)) {//如果标题为空，则读取title参数
				TextView titleTextView = (TextView) findViewById(R.id.title);
				AzkHelper.showLog("view.getTitle(): " + view.getTitle());
				if (titleTextView != null && view.getTitle() != null) {
					titleTextView.setText(view.getTitle() );
				}					
			}			
			pageStarted();
		}

	}
	
	protected void pageStarted() {
		
	}

	public static final boolean intercept(WebView view, String url,
			Context context) {
		return false;
	}

	@SuppressLint("SetJavaScriptEnabled")
	protected void initWebView() {
		webView = (WebView) pullRefreshWebView.getRefreshableView();
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setGeolocationEnabled(true);
		// settings.setBuiltInZoomControls(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setUserAgentString(AzkHelper.sAzkUserAgent);
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		settings.setDefaultTextEncodingName(HTTP.UTF_8);
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		// hide webview scrollbar
		webView.setVerticalScrollBarEnabled(false);
		webView.setHorizontalScrollBarEnabled(false);

		webView.setDownloadListener(downloadListener());
		webView.setWebChromeClient(new ProgressbarChromeClient(progressBar,
				this));
		webView.setWebViewClient(createWebViewClient(pullRefreshWebView));
	}

	private DownloadListener downloadListener() {
		return new DownloadListener() {
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype,
					long contentLength) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(url));
				startActivity(intent);
			}
		};
	}

	public void goBack(View v) {
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		if (webView != null && webView.canGoBack()) {
			webView.goBack();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onRefresh(PullToRefreshBase<WebView> refreshView) {
		webView.reload();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
