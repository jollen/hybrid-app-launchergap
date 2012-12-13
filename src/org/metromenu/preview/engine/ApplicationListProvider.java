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
package org.metromenu.preview.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.util.Log;

import org.metromenu.preview.domain.MetroApplicationInfo;

public class ApplicationListProvider {
	private static final String TAG = "ApplicationListProvider";
	
	private Context 						context;
	private PackageManager 				packageManager;
	private List<ApplicationInfo> 		applicationInfoList;

	public ApplicationListProvider(Context context) {
		this.context = context;
		packageManager = context.getPackageManager();
	}
	
	/**
	 *  Get the list of activities/applications installed on Android
	 *  
	 * @return
	 */
	public List<ResolveInfo> getInstalledAppDetails() {
		final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		return context.getPackageManager().queryIntentActivities(mainIntent, 0);
	}

	/*
	 * TODO
	 * 1. applicationLoader thread
	 * 2. set thread priority
	 * See: LauncherModel.java::run()
	 */
	public List<MetroApplicationInfo> getApplicationInfos() {
		List<MetroApplicationInfo> applicationInfos = new ArrayList<MetroApplicationInfo>();
		applicationInfoList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
						
		for (ApplicationInfo appInfo : applicationInfoList) {	
			
			MetroApplicationInfo applicationInfoItem = new MetroApplicationInfo();
				
			applicationInfoItem.setApplicationName(appInfo.loadLabel(packageManager).toString());
			applicationInfoItem.setIcon(appInfo.loadIcon(packageManager));
			applicationInfoItem.setPackageName(appInfo.packageName);
			applicationInfoItem.setPermission(appInfo.permission);
						
				//PackageInfo packinfo = packageManager.getPackageInfo(appInfo.packageName,
				//		PackageManager.GET_ACTIVITIES);
				
				//ActivityInfo[] activityinfos = packinfo.activities;
				
				Intent intent = packageManager.getLaunchIntentForPackage(appInfo.packageName);
				
				if (intent != null) {
					String name = intent.getComponent().getClassName();
				
					applicationInfoItem.setLuncherActivity(name);
				

					if(filterApp(appInfo)){
						applicationInfoItem.setThirdParty(true);
					}else {
						applicationInfoItem.setThirdParty(false);
					}
		
					applicationInfos.add(applicationInfoItem);
				}
			
			applicationInfoItem = null;
		}
		return applicationInfos;
	}

	public List<MetroApplicationInfo> getPackageInfos() {
		List<MetroApplicationInfo> applicationInfos = new ArrayList<MetroApplicationInfo>();
				
		List<PackageInfo> packageList = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES);
		
		for (PackageInfo pkgInfo : packageList) {	
			
			MetroApplicationInfo applicationInfoItem = new MetroApplicationInfo();
				
			ApplicationInfo appInfo = pkgInfo.applicationInfo;
			
			applicationInfoItem.setApplicationName(appInfo.loadLabel(packageManager).toString());
			applicationInfoItem.setIcon(appInfo.loadIcon(packageManager));
			applicationInfoItem.setPackageName(appInfo.packageName);
			applicationInfoItem.setPermission(appInfo.permission);
						

				
				ActivityInfo[] activityinfos = pkgInfo.activities;
				
				if (activityinfos!=null && activityinfos.length > 0) {
					ActivityInfo activtiyInfo = activityinfos[0];
					String name = activtiyInfo.name;
					applicationInfoItem.setLuncherActivity(name);
				}

			if(filterApp(appInfo)){
				applicationInfoItem.setThirdParty(true);
			}else {
				applicationInfoItem.setThirdParty(false);
			}

			applicationInfos.add(applicationInfoItem);
			
			applicationInfoItem = null;
		}
		return applicationInfos;
	}
	
	public boolean filterApp(ApplicationInfo info) {
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return true;
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            return true;
        } 
        return false;
    }
}
