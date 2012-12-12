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

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;

public class TaskListProvider {

	private static PackageManager mPackageManager;
	private static ActivityManager mActivityManager;

	private static Context mContext;

	// Running applicaton process list
	private List<RunningAppProcessInfo> mRunningAppProcessInfos;
	private List<TaskInfo> mTaskInfos;

	public TaskListProvider(Context context,
			List<RunningAppProcessInfo> processinfos) {
		mContext = context;

		mRunningAppProcessInfos = processinfos;

		mPackageManager = mContext.getPackageManager();
		mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	}

	public List<TaskInfo> getTaskInfos() {
		mTaskInfos = new ArrayList<TaskInfo>();

		for (RunningAppProcessInfo processinfo : mRunningAppProcessInfos){

			TaskInfo taskinfo = new TaskInfo();
			taskinfo.setPackname(processinfo.processName);

			try {
				ApplicationInfo appinfo = mPackageManager.getApplicationInfo(processinfo.processName, 0);

				taskinfo.setIcon(appinfo.loadIcon(mPackageManager));
				// Get lable name
				taskinfo.setAppname(appinfo.loadLabel(mPackageManager).toString());
			} catch (NameNotFoundException e) {

				// Use default applicaton icon
				Drawable icon = mContext.getResources().getDrawable(R.drawable.ic_launcher);
				taskinfo.setIcon(icon);

				// No lable name. Use process name instead. This is usually a background process.
				taskinfo.setAppname(processinfo.processName);
			}

			taskinfo.setPid(processinfo.pid);

			// Get process memory information
			MemoryInfo[] memoryinfos =	mActivityManager.getProcessMemoryInfo(new int[]{processinfo.pid});

			// Set memory infomation
			int size = memoryinfos[0].getTotalPrivateDirty();
			taskinfo.setMem(size);
			String memsize = getMemoryInfo(size);
			taskinfo.setMemsize(memsize);

			taskinfo.setIschecked(false);
			mTaskInfos.add(taskinfo);
		}
		return mTaskInfos;
	}


	private String getMemoryInfo(int size){
		if (size < 1024){
			return size +" KB";
		} else if (size < 1024*1024){
			double sizemb = size/1024f;
			DecimalFormat df = new DecimalFormat("###.00");
			return df.format(sizemb)+" MB";
		} else 
			return "";
	}
}
