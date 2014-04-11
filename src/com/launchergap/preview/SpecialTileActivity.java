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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class SpecialTileActivity extends Activity {

	private String TAG = "SpecialTileActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.tile_module_radio_group);
	}

	public void onRadioButtonClicked(View view) {	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.module_camera:
	        	Log.i(TAG, "Add a special Camera module.");
	        	setupCameraTile();
	            break;
	        case R.id.module_browser:
	        	Log.i(TAG, "Add a special Browser module.");	
	        	setupBrowserTile();
	            break;
	        case R.id.module_phone:
	        	Log.i(TAG, "Add a special Phone module.");
	        	setupPhoneTile();
	            break;
	    }
	}

	private void setupPhoneTile() {		
		startApplicationManagerWithModuleName("Phone");
	}

	private void setupBrowserTile() {	
		startApplicationManagerWithModuleName("Browser");
	}

	private void setupCameraTile() {	
		startApplicationManagerWithModuleName("Camera");
	}
	
	private void startApplicationManagerWithModuleName(String module) {
		Bundle bundle = new Bundle();
		bundle.putString("module", module);
		
		Intent intent = new Intent();
		intent.setAction("metromenu.intent.action.SETTINGS");
		intent.putExtras(bundle);
		
		startActivity(intent);			
	}
}
