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
package org.launchergap.preview.tasks;

import android.graphics.drawable.Drawable;

public class TaskInfo {
	private Drawable mIcon;
	private String mAppName;
	private String mPackname;
	private String memSize;

	private boolean isChecked;
	private boolean mIsSmp;

	private int mem;
	private int pid;

	public int getMem() {
		return mem;
	}
	
	public void setMem(int mem) {
		this.mem = mem;
	}
	
	public String getMemsize() {
		return memSize;
	}
	
	public void setMemsize(String memsize) {
		this.memSize = memsize;
	}

	public Drawable getIcon() {
		return mIcon;
	}
	
	public void setIcon(Drawable icon) {
		this.mIcon = icon;
	}
	
	public String getAppname() {
		return mAppName;
	}
	
	public void setAppname(String appicon) {
		this.mAppName = appicon;
	}
	
	public String getPackname() {
		return mPackname;
	}
	
	public void setPackname(String packname) {
		this.mPackname = packname;
	}
	
	public int getPid() {
		return pid;
	}
	
	public void setPid(int pid) {
		this.pid = pid;
	}
	
	public boolean isIschecked() {
		return isChecked;
	}
	
	public void setIschecked(boolean ischecked) {
		this.isChecked = ischecked;
	}
	
	public void setSmp(boolean isSmp) {
		mIsSmp = isSmp;
	}
	
	public boolean getIsSmp() {
		return mIsSmp;
	}
}
