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
package org.metromenu.preview;

import android.graphics.drawable.Drawable;

public class TaskInfo{
	private Drawable icon;
	private String appname;
	private String packname;
	private int pid;
	private String memsize;
	private int mem;
	private boolean ischecked;
	private boolean mIsSmp;
	
	
	public int getMem() {
		return mem;
	}
	public void setMem(int mem) {
		this.mem = mem;
	}
	public String getMemsize() {
		return memsize;
	}
	public void setMemsize(String memsize) {
		this.memsize = memsize;
	}

	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appicon) {
		this.appname = appicon;
	}
	public String getPackname() {
		return packname;
	}
	public void setPackname(String packname) {
		this.packname = packname;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public boolean isIschecked() {
		return ischecked;
	}
	public void setIschecked(boolean ischecked) {
		this.ischecked = ischecked;
	}
	public void setSmp(boolean isSmp) {
		mIsSmp = isSmp;
	}
	public boolean getIsSmp() {
		return mIsSmp;
	}
}
