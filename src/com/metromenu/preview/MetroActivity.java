package com.metromenu.preview;

import java.util.List;
import java.util.Set;

import org.metromenu.preview.database.MetroMenuDatabase;
import org.metromenu.preview.helper.ConfigurationHelperImpl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
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

	private String TAG = "MetroActivity";		

	protected LinearLayout root;
	private MetroMenuHandler mHandler;
	private MetroMenuDatabase mDatabase;
	private static MetroWebView mWebView;
	private BroadcastReceiver mMenuUpdateReceiver;

	private IntentFilter mFilter;

	private MetroActivity mContext;

	private static ConfigurationHelperImpl mConfiguration;
	private static String sJsonCode;	

	public static final int MSG_START_ACTIVITY = 0;
	public static final int MSG_START_MODULE = 1;
	public static final int MSG_START_EDIT_DIALOG = 2;
	public static final int MSG_END_EDIT_DIALOG = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mContext = this;
        
        mConfiguration = new ConfigurationHelperImpl();        
        mConfiguration.setResizableMode(false);
        mConfiguration.setResortableMode(false);
		
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

		if (mDatabase.isEmpty() == true)
			createDefaultTile();

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
		//loadUrl("file:///android_asset/metromenu/sandbox/resizable.html");	
		loadUrl("file:///android_asset/metromenu/index.html");				
	}

	public synchronized void createDefaultTile() {
		mDatabase.putDefaultTile("Browser", "tiles/IE_64.png", "metro-green", "1x1");
		mDatabase.putDefaultTile("Phone", "tiles/Cell_64.png", "metro-green", "1x1");
		//mDatabase.putDefaultTile("Camera", "tiles/Digital_Camera.png", "metro-black", "1x2");
		mDatabase.putDefaultTile("Youtube", "tiles/YouTube_64.png", "metro-green", "1x1");
		mDatabase.putDefaultTile("Massanger", "tiles/ConversionMassanger_64.png", "metro-green", "1x1"); 
	}
	
	@Override
	protected void onResume() {
		registerReceiver(mMenuUpdateReceiver, mFilter);
		mDatabase.open();
		updateMenu();
		super.onResume();
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
		int	tileID;

		MetroMenuHandler(Context context) {
			ctx = context;
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_START_MODULE:
				data = msg.getData();

				/* TODO: need to check if the package was uninstalled */
				
				moduleName = data.getString("moduleName");
				Log.i(TAG, "Launching module: " + moduleName);

				packageName = mDatabase.getPackageByModule(moduleName);
				activityName = mDatabase.getActivityByModule(moduleName);

				Log.i(TAG, "startActivity: [" + packageName + "], [" + activityName + "], " + "module: " + moduleName);

				if (packageName != null &&
						packageName.matches("com.android.contacts")) {
					Intent intent = new Intent(Intent.ACTION_VIEW, CallLog.Calls.CONTENT_URI);
					ctx.startActivity(intent);
					break;
				}
				
				if (activityName.equals("") == false) {
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
				} else if (packageName.equals("") == false) {
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

				tileID = data.getInt("_ID");
				packageName = data.getString("packageName");
				activityName = data.getString("activityName");

				Log.i(TAG, "startActivity: [" + packageName + "], [" + activityName + "]");

				if (packageName.matches("com.android.contacts")) {
					Intent intent = new Intent(Intent.ACTION_VIEW, CallLog.Calls.CONTENT_URI);
					ctx.startActivity(intent);
					break;
				}
				
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
			
			case MSG_START_EDIT_DIALOG:
				data = msg.getData();

				packageName = data.getString("packageName");
				activityName = data.getString("activityName");
				tileID = data.getInt("_ID");
				
				//showEditDialog(packageName, activityName);
				showEditDialog(tileID);
				break;
				
			case MSG_END_EDIT_DIALOG:
				mConfiguration.setResizableMode(false);
				updateMenu();
				break;			
				
			default:
				super.handleMessage(msg);
			}
		}
	}

	private void init(Bundle savedInstanceState) {
		mWebView.restoreState(savedInstanceState); // handling rotation
	}

	public void showHitCannotResize() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		String message = "You can't resize a built-in tile.";
		
		builder.setMessage(message);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.show();
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

	public void showEditDialog(final int tileID) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
				
		builder.setTitle("Tile Size").setSingleChoiceItems(R.array.tile_size_array_name, 1, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {	
				Log.i(TAG, "which: " + which);
				
				if (which < 0) {
					which = 1;
				}
				
				/* which: start from 0 */
				String[] size = {"1x1", "1x2", "2x2"};
				//mDatabase.setTileSize(packageName, activityName, size[which]);
				mDatabase.setTileSize(tileID, size[which]);
				
				Message msg = Message.obtain();
				msg.what = MSG_END_EDIT_DIALOG;
				mHandler.sendMessage(msg);
			}
		});
		
		builder.setPositiveButton(R.string.set_title_module_dialog_ok, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// Nothing to do.
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	/** @deprecated
	 */
	public void showEditDialog(final String packageName, final String activityName) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
				
		builder.setTitle("Tile Size").setSingleChoiceItems(R.array.tile_size_array_name, 1, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {	
				Log.i(TAG, "which: " + which);
				
				/* which: start from 0 */
				String[] size = {"1x1", "1x2", "2x2"};
				mDatabase.setTileSize(packageName, activityName, size[which]);
				
				Message msg = Message.obtain();
				msg.what = MSG_END_EDIT_DIALOG;
				mHandler.sendMessage(msg);
			}
		});
		
		builder.setPositiveButton(R.string.set_title_module_dialog_ok, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				if (which == -1) {
					// Default checked. which = 1
					mDatabase.setTileSize(packageName, activityName, "1x2");
					
					Message msg = Message.obtain();
					msg.what = MSG_END_EDIT_DIALOG;
					mHandler.sendMessage(msg);
				}
				updateMenu();
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
	
	public MetroMenuHandler getHandler() {
		return mHandler;
	}

	public void updateMenu() {
		sJsonCode = mDatabase.getJSON(); 
		
		// Try to fix bug: resort and resize don't work after a while
		//loadUrl("file:///android_asset/metromenu/index.html");

		String url = "javascript: updateMetroMenu(" + sJsonCode + ")";		
		mWebView.loadUrl(url);	
		Log.i(TAG , "updateMenu: ");
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
	
	public ConfigurationHelperImpl getConfiguration() {
		return mConfiguration;
	}
	
	public MetroWebView getWebView() {
		return mWebView;
	}
}
