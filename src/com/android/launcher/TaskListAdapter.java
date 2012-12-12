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

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskListAdapter extends BaseAdapter {
	List<TaskInfo> mTaskInfos;
	Context mContext;
	LayoutInflater inflater ;

	public TaskListAdapter(List<TaskInfo> taskinfos, Context context) {
		mTaskInfos = taskinfos;
		mContext = context;
		inflater = LayoutInflater.from(context);
	}
	
	public void setData(List<TaskInfo> taskinfos){
		mTaskInfos = taskinfos;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return mTaskInfos.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mTaskInfos.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view ;
		TaskInfo item = mTaskInfos.get(position);
		if(convertView==null){
			view = inflater.inflate(R.layout.task_item, null);
		}else{
			view = convertView;
		}
		ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_task_item_icon);
		TextView tv_appname= (TextView) view.findViewById(R.id.tv_task_item_appname);
		TextView tv_appmem= (TextView) view.findViewById(R.id.tv_task_item_appmem);
		CheckBox cb = (CheckBox) view.findViewById(R.id.cb_task_item_isselect);
		
		cb.setVisibility(View.VISIBLE);
		iv_icon.setImageDrawable(item.getIcon());
		
		tv_appmem.setText(item.getMemsize());
		tv_appname.setText(item.getAppname());
		
		// Check if item checked.
		if (item.isIschecked()){
			cb.setChecked(true);
			item.setSmp(true);
		} else {
			cb.setChecked(false);
			item.setSmp(true);
		}
		
		if("cn.itcast.mobilesafe".equals(item.getPackname()))
		   cb.setVisibility(View.INVISIBLE);

		return view;
	}
}
