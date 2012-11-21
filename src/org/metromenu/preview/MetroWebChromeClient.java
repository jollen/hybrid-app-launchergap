package org.metromenu.preview;

import android.webkit.WebChromeClient;

public class MetroWebChromeClient extends WebChromeClient {

	private MetroWebView mWebView;

	public MetroWebChromeClient(MetroWebView metroWebView) {
		mWebView = metroWebView;
	}

}
