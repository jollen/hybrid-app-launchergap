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
package org.metromenu.preview.adapter;

import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.metromenu.preview.domain.MetroApplicationInfo;

import com.android.launcher.R;

public class AppDetailAdapter extends BaseAdapter {
	Context context;
	List<MetroApplicationInfo> appInfos;
	LayoutInflater inflater;


	public void setAppInfos(List<MetroApplicationInfo> appInfos){
		this.appInfos = appInfos;
	}


	public AppDetailAdapter(Context context,
			List<MetroApplicationInfo> appInfo) {
		this.context = context;
		this.appInfos = appInfo;
		
		inflater = LayoutInflater.from(context);
	}

	public int getCount() {

		return appInfos.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return appInfos.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		
		if (convertView == null){
			view = inflater.inflate(R.layout.manage_applications_item, null);
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
			tv.setText("Unavailable");
		}

		return view;
	}

}
