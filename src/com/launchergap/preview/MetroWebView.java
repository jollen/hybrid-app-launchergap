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

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MetroWebView extends WebView {

	public MetroWebView(Context context) {
		super(context);
		initWebView(context);
	}

	private MainActivity mContext;

	public MetroWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWebView(context);
	}

	public MetroWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWebView(context);
	}

	private void initWebView(Context context) {
		mContext = (MainActivity) context;
		
		this.setWebViewClient(new MetroWebViewClient(this));
		this.setWebChromeClient(new MetroWebChromeClient(this));
		
		this.setInitialScale(100);
		this.setVerticalScrollBarEnabled(false);
		this.requestFocusFromTouch();
		
		// Enable JavaScript and add JavaScriptInterface
		WebSettings settings = this.getSettings();
        settings.setJavaScriptEnabled(true);
        this.addJavascriptInterface(new MetroJavaScriptInterface(mContext), "MetroMenu");		
	
        // Set ID
        this.setId(88);
	}
}
