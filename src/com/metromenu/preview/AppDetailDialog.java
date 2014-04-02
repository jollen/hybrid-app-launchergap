/*
 * Copyright (C) 2011 Moko365 Inc.
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

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
 
public class AppDetailDialog extends Activity {
	PackageManager pm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_app_detail);
		pm = getPackageManager();
		
		TextView tv_name = (TextView) this.findViewById(R.id.tv_show_app_detail_name);
		TextView tv_pack_name = (TextView) this.findViewById(R.id.tv_show_app_detail_packname);
		TextView tv_app_version = (TextView) this.findViewById(R.id.tv_show_app_detail_version);
		TextView tv_app_permission = (TextView) this.findViewById(R.id.tv_show_app_detail_permission);

		TaskInfoUtils app =	 (TaskInfoUtils) getApplication();
	    TaskInfo info = app.taskInfo;
	    tv_name.setText(info.getAppname());
	    tv_pack_name.setText(info.getPackname());
	    
	    try {
	    	PackageInfo  packinfo = pm.getPackageInfo(info.getPackname(), PackageManager.GET_PERMISSIONS );
	    	tv_app_version.setText(packinfo.versionName);
	    	String[] permissions = packinfo.requestedPermissions;
	    	StringBuffer sb = new StringBuffer();
	    	for(String permission : permissions){
	    		sb.append(permission);
	    		sb.append("\n");
	    	}
	    	String permission = sb.toString();
	    	tv_app_permission.setText(permission);
	    	
	    } catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		
	}
}
