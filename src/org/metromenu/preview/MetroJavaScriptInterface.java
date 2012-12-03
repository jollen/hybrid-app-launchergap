package org.metromenu.preview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
  
public class MetroJavaScriptInterface {
	
	private MainActivity mContext;
	private String TAG = "MetroJavaScriptInterface";
	
	public MetroJavaScriptInterface(Context context) {
		mContext = (MainActivity) context;
	}

	/**
	 * Start an Activity
	 * 
	 * @param packageName
	 * @param activityName
	 */
	public void startActivity(String packageName, String activityName) {
		Log.i(TAG, "startActivity: [" + packageName + "], [" + activityName + "]");

		Handler handler = mContext.getHandler();
		Message msg = Message.obtain();
		Bundle data = new Bundle();
		
		data.putString("packageName", packageName);
		data.putString("activityName", activityName);
		msg.setData(data);
		
		msg.what = MainActivity.MSG_START_ACTIVITY;
		handler.sendMessage(msg);
	}
	
	public void startActivity(String moduleName) 
	{
		Log.i(TAG, "Start module: " + moduleName);
		
		Handler handler = mContext.getHandler();
		Message msg = Message.obtain();
		Bundle data = new Bundle();
		
		data.putString("moduleName", moduleName);
		msg.setData(data);
		
		msg.what = MainActivity.MSG_START_MODULE;
		handler.sendMessage(msg);
	}
	
	public void lauchPackageManager() {
		Intent i = new Intent();
		
		i.setAction("metromenu.intent.action.SETTINGS");
    		mContext.startActivity(i);
	}
	
	public void debug(String message) {
		Log.i(TAG, message);
	}
}