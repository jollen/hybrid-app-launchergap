package com.android.launcher;

import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MetroWebViewClient extends WebViewClient {

	private MetroWebView mWebView;
	private MainActivity mContext;

	public MetroWebViewClient(MetroWebView webview) {
		mWebView = webview;
		mContext = (MainActivity) mWebView.getContext();
	}

	private String TAG = "MetroWebViewClient";

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		view.loadUrl(url);
		Log.i(TAG , "Override Url: " + url);
		return super.shouldOverrideUrlLoading(view, url);
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
		Log.i(TAG , "onPageFinished: " + url);
		
		// Try to load Metro Menu
		if (mWebView.getVisibility() == View.INVISIBLE) {
	        mContext.menuLoader();
			mWebView.setVisibility(View.VISIBLE);
		}
	}
}
