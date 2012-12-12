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
package com.android.launcher;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CallLog;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
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
  
public class MainActivity extends MetroActivity {
	
	private static final String LED_SERVICE  = "led";

	private String TAG = "MetroMenu";		

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }    
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int vid = item.getItemId();
		
		Log.i(TAG, "onMenuItem");
		
		switch (vid) {
			case R.id.menu_all_applications: {
				startApplicationManagerActivity();
			} break;
			
			case R.id.menu_about: {
				showHitResetDatabase();
			} break;

			case R.id.menu_reset: {
				
			} break;
			
			//case R.id.menu_add_special_tile: {	
	    	//	startSpecialTileActivity();
			//} break;
			
			default:
				return false;
		}

    		
		return true;
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
	
	/***** End of Life cycle control *****/

	public void showHitResetDatabase() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		String message = this.getString(R.string.reset_database_dialog_message);
		String title = this.getString(R.string.reset_database_dialog_title);
		
		builder.setMessage(message).setTitle(title);
		builder.setPositiveButton(R.string.set_title_module_dialog_ok, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				getDatabase().reset();
		        sendBroadcast(new Intent("metromenu.intent.action.MENU_UPDATE"));
			}
		});
		
		builder.setNegativeButton(R.string.set_title_module_dialog_cancel, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	private void startSpecialTileActivity() {
		Intent i = new Intent();

		i.setAction("metromenu.intent.action.SPECIAL_TILE");
		startActivity(i);		
	}

	private void startApplicationManagerActivity() {
		Intent i = new Intent();

		i.setAction("metromenu.intent.action.SETTINGS");
		startActivity(i);
	}
}
