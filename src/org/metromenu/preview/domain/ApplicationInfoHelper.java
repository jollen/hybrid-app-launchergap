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
package org.metromenu.preview.domain;

import android.content.Intent;
import android.graphics.drawable.Drawable;

public interface ApplicationInfoHelper {
	public boolean isThirdParty();
	
	public void setThirdParty(boolean isThirdParty);
	
	public String getLuncheractivity();
	
	public void setLuncherActivity(String luncheractivity);
	
	public String getPackname();
	
	public void setPackageName(String packname);
	
	public Drawable getIcon();
	
	public void setIcon(Drawable icon);
	
	public String getAppName();
	
	public void setApplicationName(String appname);
	
	public void setIntent(Intent intent);
	
	public void setPermission(String permission);
	
	public String getPermission();
}
