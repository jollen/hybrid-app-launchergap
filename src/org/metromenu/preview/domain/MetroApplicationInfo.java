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
package org.metromenu.preview.domain;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

public class MetroApplicationInfo {
	private String mPackageName;
	private Drawable mIcon;
	private String mApplicationName;
	private String mLuncherActivity;
	private Intent mIntent;
	
	private boolean isThirdParty;
	private String mPermission;
	
	public boolean isThirdParty() {
		return isThirdParty;
	}
	public void setThirdParty(boolean isThirdParty) {
		this.isThirdParty = isThirdParty;
	}
	public String getLuncheractivity() {
		return mLuncherActivity;
	}
	public void setLuncherActivity(String luncheractivity) {
		this.mLuncherActivity = luncheractivity;
	}
	public String getPackname() {
		return mPackageName;
	}
	public void setPackageName(String packname) {
		this.mPackageName = packname;
	}
	public Drawable getIcon() {
		return mIcon;
	}
	public void setIcon(Drawable icon) {
		this.mIcon = icon;
	}
	public String getAppName() {
		return mApplicationName;
	}
	public void setApplicationName(String appname) {
		this.mApplicationName = appname;
	}
	
	public void setIntent(Intent intent) {
		this.mIntent = intent;
	}
	public void setPermission(String permission) {
		this.mPermission = permission;
	}
	
	public String getPermission() {
		return mPermission;
	}
	
}
