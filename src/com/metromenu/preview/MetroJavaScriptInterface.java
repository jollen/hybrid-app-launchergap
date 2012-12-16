package com.metromenu.preview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
  
public class MetroJavaScriptInterface {
	
	private MainActivity mContext;
	private String TAG = "MetroJavaScriptInterface";
	
	public MetroJavaScriptInterface(Context context) {
		mContext = (MainActivity) context;
	}

	public void startActivityByMessage(Message msg) {
		Handler handler = mContext.getHandler();
		
		// If we are in edit mode, rewrite the message
		if (mContext.getConfiguration().getEditMode() == true) {
			if (msg.what == MainActivity.MSG_START_MODULE) {
				mContext.showHitCannotResize();
				msg.what = MainActivity.MSG_END_EDIT_DIALOG;
			} else {
				msg.what = MainActivity.MSG_START_EDIT_DIALOG;
			}
		}
		handler.sendMessage(msg);	
	}
	
	/**
	 * Start an Activity
	 * 
	 * @param packageName
	 * @param activityName
	 */
	public void startActivity(String packageName, String activityName) {
		//Log.i(TAG, "startActivity: [" + packageName + "], [" + activityName + "]");

		Message msg = Message.obtain();
		Bundle data = new Bundle();
		
		data.putString("packageName", packageName);
		data.putString("activityName", activityName);
		msg.setData(data);
		
		msg.what = MainActivity.MSG_START_ACTIVITY;
		
		startActivityByMessage(msg);
	}
	
	public void startActivity(String moduleName) 
	{
		Log.i(TAG, "Start module: " + moduleName);
		
		Message msg = Message.obtain();
		Bundle data = new Bundle();
		
		data.putString("moduleName", moduleName);
		msg.setData(data);
		
		msg.what = MainActivity.MSG_START_MODULE;
		
		startActivityByMessage(msg);
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