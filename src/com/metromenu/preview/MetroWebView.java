package com.metromenu.preview;

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
