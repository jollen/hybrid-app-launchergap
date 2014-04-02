/*
 * Copyright (C) 2012 Moko365 Inc.
 * Copyright (C) 2014 Jollen Chen
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
package com.metromenu.preview;

import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CallLog;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.speech.RecognizerIntent;
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
import org.metromenu.preview.helper.ConfigurationHelperImpl;
  
public class MainActivity extends MetroActivity {

	private String TAG = "MetroMenu";		
	
	// Speech to Text
	protected static final int RESULT_SPEECH = 1;
	private String mSTT;

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
				showAbout();
			} break;

			case R.id.menu_reset: {
				showHitResetDatabase();
			} break;
			
			case R.id.menu_resize: {
				Toast.makeText(this, "Please choose one tile to resize.", Toast.LENGTH_LONG).show();
				getConfiguration().setResizableMode(true);
			} break;

			case R.id.menu_voice_launch: {
				 Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				 intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
				 try {
					 startActivityForResult(intent, RESULT_SPEECH);
				} catch (ActivityNotFoundException a) {
					Toast.makeText(getApplicationContext(),
							"Your device doesn't support Speech to Text",
								Toast.LENGTH_SHORT).show();
				}
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
				createDefaultTile();
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
		startActivityForResult(i, 0);
	}
	
	public void showAbout() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		String message = this.getString(R.string.about_dialog_message);
		
		builder.setMessage(message);
		builder.setPositiveButton(R.string.set_title_module_dialog_ok, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	public void updateOrderByID(int id, int order) {
		getDatabase().updateOrderByID(id, order);
	}

	public void deleteTileDialog(final int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		String message = this.getString(R.string.delete_tile_dialog_message);
		String title = this.getString(R.string.delete_tile_dialog_title);
		
		builder.setMessage(message).setTitle(title);
		builder.setPositiveButton(R.string.set_title_module_dialog_ok, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				getDatabase().deleteTileByID(id);
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

	/**
	 * Speech to Text
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {		
		switch (requestCode) {
			case RESULT_SPEECH: {
				if (resultCode == RESULT_OK && null != data) {
					ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
					mSTT = text.get(0);
					Toast.makeText(this, "You Say: " + mSTT, Toast.LENGTH_SHORT).show();
					getDatabase().getPackageByKeyword(mSTT);
				}
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
