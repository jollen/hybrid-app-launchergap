/*
 * Copyright (C) 2012 Moko365 Inc.
 * Copyright (C) 2014 Launcher Gap
 * 
 * Author: jollen <jollen@jollen.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.launchergap.preview;

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
			mWebView.setVisibility(View.VISIBLE);
		}
        mContext.updateMenu();	
	}
}
