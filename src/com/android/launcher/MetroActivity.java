package com.android.launcher;

import java.util.Set;

import org.metromenu.preview.database.MetroMenuDatabase;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CallLog;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MetroActivity extends Activity {

	@Override
	protected void onResume() {
		registerReceiver(mMenuUpdateReceiver, mFilter);
		mDatabase.open();
		updateMenu();
		super.onResume();
	}

	private String TAG = "MetroActivity";		

	protected LinearLayout root;
	private MetroMenuHandler mHandler;
	private MetroMenuDatabase mDatabase;
	private static MetroWebView mWebView;
	private BroadcastReceiver mMenuUpdateReceiver;

	private IntentFilter mFilter;
	private static String sJsonCode;	

	public static final int MSG_START_ACTIVITY = 0;
	public static final int MSG_START_MODULE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
		mFilter = new IntentFilter("metromenu.intent.action.MENU_UPDATE");
		mMenuUpdateReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				updateMenu();
			}
		};
		registerReceiver(mMenuUpdateReceiver, mFilter);

		// Initializing WebView
		mWebView = new MetroWebView(this);
		
		mWebView.setVisibility(View.INVISIBLE);
		root.addView(mWebView);
		setContentView(root);

		this.init(savedInstanceState);
		loadUrl("file:///android_asset/metromenu/index.html");		
	}

	@Override
	protected void onStop() {
		unregisterReceiver(mMenuUpdateReceiver);
		mDatabase.close();
		super.onStop();
	}

	protected void loadUrl(String url) {
		mWebView.loadUrl(url);
	}

	// Used by MetroJavaScriptInterface
	private class MetroMenuHandler extends Handler {

		private Context ctx;

		Bundle data;
		String packageName;
		String activityName;
		String moduleName;

		MetroMenuHandler(Context context) {
			ctx = context;
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_START_MODULE:
				data = msg.getData();

				moduleName = data.getString("moduleName");
				Log.i(TAG, "Launching module: " + moduleName);

				packageName = mDatabase.getPackageByModule(moduleName);
				activityName = mDatabase.getActivityByModule(moduleName);

				Log.i(TAG, "startActivity: [" + packageName + "], [" + activityName + "], " + "module: " + moduleName);

				if (activityName != null) {
					Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
					
					// LaunchIntent is null. It's because that we don't have that permissions.
					// New intent by ourselves instead.
					// Fix issues: Issue #4
					if (intent == null) {
						Intent intent2 = new Intent(Intent.ACTION_MAIN);
						intent2.addCategory(Intent.CATEGORY_LAUNCHER);
						intent2.setClassName(packageName, activityName);
						intent2.setPackage(packageName);
						intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);	
						
						Log.i(TAG, "by ClassName");
						
						ctx.startActivity(intent2);
					} else {
						Log.i(TAG, "by LaunchIntent: " + intent.getAction());

						// Fix issue: Issue #6
						ComponentName componentName = intent.getComponent();
						String className = componentName.getClassName();
						
						// Fix issue: Issue #6.
						// Check if activityName (the class name) equals the class name in that package.
						//if (className.equals(activityName) == false) {
						//	Log.i(TAG, "setClassName:" + activityName);
						//	intent.setClassName(packageName, activityName);
						//}
						
						ctx.startActivity(intent);
					}
				} else if (packageName != null) {
					//Toast.makeText(ctx, "by Package", Toast.LENGTH_SHORT).show();
					Log.i(TAG, "by packageName");

					Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
					ctx.startActivity(intent);
				} else {
					// None found...
					// TODO: let user pick up app through PackageManager
					//Toast.makeText(ctx, "Please pick up " + moduleName, Toast.LENGTH_SHORT).show();
					showHitSetModule(moduleName);
					//startApplicationManagerWithModuleName(moduleName);
				}
				break;
			case MSG_START_ACTIVITY:
				data = msg.getData();

				packageName = data.getString("packageName");
				activityName = data.getString("activityName");

				Log.i(TAG, "startActivity: [" + packageName + "], [" + activityName + "]");

				//if (packageName.matches("com.android.contacts")) {
				//	Intent intent = new Intent(Intent.ACTION_VIEW, CallLog.Calls.CONTENT_URI);
				//	ctx.startActivity(intent);
				//	break;
				//}
				
				if (activityName != null) {
					Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
					
					// LaunchIntent is null. It's because that we don't have that permissions.
					// New intent by ourselves instead.
					// Fix issues: Issue #4
					if (intent == null) {
						Intent intent2 = new Intent(Intent.ACTION_MAIN);
						intent2.addCategory(Intent.CATEGORY_LAUNCHER);
						intent2.setClassName(packageName, activityName);
						intent2.setPackage(packageName);
						intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);	
						
						Log.i(TAG, "by ClassName");
						
						ctx.startActivity(intent2);
					} else {
						Log.i(TAG, "by LaunchIntent: " + intent.getAction());
	
						ctx.startActivity(intent);
					}
				} else {
					//Toast.makeText(ctx, "by Package", Toast.LENGTH_SHORT).show();
					Log.i(TAG, "by packageName");

					Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
					ctx.startActivity(intent);
				}
				break;
			}
			super.handleMessage(msg);
		}
	}

	private void init(Bundle savedInstanceState) {
		mWebView.restoreState(savedInstanceState); // handling rotation
	}

	public void showHitSetModule(String module) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final String moduleName = module;
		
		String message = this.getString(R.string.set_tile_module_dialog_message) + " " + moduleName;
		String title = this.getString(R.string.set_tile_module_dialog_title);
		
		builder.setMessage(message).setTitle(title);
		builder.setPositiveButton(R.string.set_title_module_dialog_ok, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				startApplicationManagerWithModuleName(moduleName);				
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	/**
	 * Handling BACK KEY. Do it in UI thread.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:				
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * Handling rotation.
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		mWebView.saveState(outState);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// use 'android:configChanges' in manifest to handle rotation
		super.onConfigurationChanged(newConfig);
	}	
	
	public MetroMenuHandler getHandler() {
		return mHandler;
	}

	public void menuLoader() {
		sJsonCode = mDatabase.getJSON(); 

		String url = "javascript: createMetroMenu(" + sJsonCode + ")";		
		mWebView.loadUrl(url);	
		//Log.i(TAG , "url: " + url);
	} 

	public void updateMenu() {
		sJsonCode = mDatabase.getJSON(); 

		String url = "javascript: updateMetroMenu(" + sJsonCode + ")";		
		mWebView.loadUrl(url);	
		//Log.i(TAG , "url: " + url);
	}
	
	private void startApplicationManagerWithModuleName(String module) {
		Bundle bundle = new Bundle();
		bundle.putString("module", module);
		
		Intent intent = new Intent();
		intent.setAction("metromenu.intent.action.SETTINGS");
		intent.putExtras(bundle);
		
		startActivity(intent);			
	}
	
	public MetroMenuDatabase getDatabase() {
		return mDatabase;
	}
}
