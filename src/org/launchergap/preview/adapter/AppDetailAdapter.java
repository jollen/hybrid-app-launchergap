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
package org.launchergap.preview.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.launchergap.preview.domain.MetroApplicationInfoImpl;

import com.launchergap.preview.R;

public class AppDetailAdapter extends BaseAdapter {
	private List<MetroApplicationInfoImpl> appInfos;
	private static LayoutInflater sInflater;

	/**
	 * 
	 * @param appInfos
	 */
	public void setAppInfos(List<MetroApplicationInfoImpl> appInfos){
		this.appInfos = appInfos;
	}

	/**
	 * 
	 * @param context
	 * @param appInfo
	 */
	public AppDetailAdapter(Context context,
			List<MetroApplicationInfoImpl> appInfo) {
		this.appInfos = appInfo;
		
		sInflater = LayoutInflater.from(context);
	}

	/**
	 * 
	 */
	public int getCount() {
		return appInfos.size();
	}

	/**
	 * 
	 */
	public Object getItem(int position) {
		return appInfos.get(position);
	}

	/**
	 * 
	 */
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		
		if (convertView == null) {
			view = sInflater.inflate(R.layout.manage_applications_item, null);
		} else {
			view = convertView;
		}
		
		ImageView iv = (ImageView) view.findViewById(R.id.app_icon);
		Drawable icon = appInfos.get(position).getIcon();
		
		if (icon != null) {
			iv.setImageDrawable(icon);
		} else {
			iv.setImageResource(R.drawable.ic_launcher);
		}
		
		TextView tv = (TextView) view.findViewById(R.id.app_name);
		String text = appInfos.get(position).getAppName();
		
		if (text != null){
			tv.setText(text);
		} else {
			tv.setText(R.string.app_name_not_available);
		}

		return view;
	}
}
