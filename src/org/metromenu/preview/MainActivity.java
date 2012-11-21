/*
 * Copyright (C) 2012 Moko365 Inc.
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
package org.metromenu.preview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.apache.cordova.*;
import org.metromenu.preview.database.MetroMenuDatabase;
  
public class MainActivity extends Activity {
	
	private String TAG = "MetroMenu";
	private MetroMenuDatabase mDatabase;
	private MetroWebView mWebView;
	private static String sJsonCode;
	
	protected LinearLayout root;
	private MetroMenuHandler mHandler;
	private BroadcastReceiver mMenuUpdateReceiver;
	
	public static final int MSG_START_ACTIVITY = 0;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, 
        		WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        
        // Use LinearLayout
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.BLACK);
        root.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
        		ViewGroup.LayoutParams.FILL_PARENT, 0.0F));
        
        mHandler = new MetroMenuHandler(this);
        mDatabase = new MetroMenuDatabase(this);
        
        // Broadcast receiver
        IntentFilter filter = new IntentFilter("metromenu.intent.action.MENU_UPDATE");
        mMenuUpdateReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				updateMenu();
			}
        };
        registerReceiver(mMenuUpdateReceiver, filter);
        
        // Load Url 
		this.init();
		this.loadUrl("file:///android_asset/metromenu/index.html");
	}
	
	protected void loadUrl(String url) {
		mWebView.loadUrl(url);
	}

	private void init() {
        // Initializing WebView
        mWebView = new MetroWebView(this);
        
        mWebView.setVisibility(View.INVISIBLE);
        root.addView(mWebView);
        setContentView(root);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }    
    
    @Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		Intent i = new Intent();
		
		i.setAction("metromenu.intent.action.SETTINGS");
    		startActivity(i);
    		
		return super.onMenuItemSelected(featureId, item);
	}    
    
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}	
 
	public void menuLoader() {
        sJsonCode = mDatabase.getJSON(); 
        
        String url = "javascript: createMetroMenu(" + sJsonCode + ")";		
        mWebView.loadUrl(url);	
        Log.i(TAG , "url: " + url);
	} 
	
	public void updateMenu() {
        sJsonCode = mDatabase.getJSON(); 
        
        String url = "javascript: updateMetroMenu(" + sJsonCode + ")";		
        mWebView.loadUrl(url);	
        Log.i(TAG , "url: " + url);
	}
	
	// Used by MetroJavaScriptInterface
	private class MetroMenuHandler extends Handler {
		
		private Context ctx;

		MetroMenuHandler(Context context) {
			ctx = context;
		}
		
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_START_ACTIVITY:
				Bundle data = msg.getData();
				
				String packageName = data.getString("packageName");
				String activityName = data.getString("activityName");
								
				Log.i(TAG, "startActivity: [" + packageName + "], [" + activityName + "]");

				if (activityName != null) {
					Intent intent = new Intent(Intent.ACTION_MAIN);
					intent.addCategory(Intent.CATEGORY_LAUNCHER);
					intent.setClassName(packageName, activityName);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					
					ctx.startActivity(intent);
				} else {
					Toast.makeText(ctx, "by Package", Toast.LENGTH_SHORT).show();
					
					Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
					ctx.startActivity(intent);
				}
			}
			super.handleMessage(msg);
		}
	}
	
	public MetroMenuHandler getHandler() {
		return mHandler;
	}
}
