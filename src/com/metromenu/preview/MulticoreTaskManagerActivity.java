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
package com.metromenu.preview;

import java.text.DecimalFormat;
import java.util.List;

import org.metromenu.preview.tasks.TaskInfo;
import org.metromenu.preview.tasks.TaskInfoUtils;
import org.metromenu.preview.tasks.TaskListAdapter;
import org.metromenu.preview.tasks.TaskListProvider;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class MulticoreTaskManagerActivity extends Activity implements OnItemLongClickListener {
	
	protected static final String TAG = "MulticoreTaskManager";

	protected static final int MSG_SUCCESS = 0x0;

	private static ActivityManager mActivityManager;
	private static List<RunningAppProcessInfo> mRunningProcessInfo;
	private static TaskListProvider mTaskProvider;
	
	List<TaskInfo> mTaskInfos;
	ProgressDialog mProgress;
	TaskListAdapter mTaskAdapter;
	
	private MulticoreTaskManagerActivity mContext;
	
	ListView lv;
	TextView tv_task_num;
	TextView tv_avail_memory;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SUCCESS:
				mProgress.dismiss();
				setTitle();

				if (mTaskAdapter == null) {
					mTaskAdapter = new TaskListAdapter(mTaskInfos,
							mContext);
					lv.setAdapter(mTaskAdapter);
				} else {
					mTaskAdapter.setData(mTaskInfos);
					mTaskAdapter.notifyDataSetChanged();
				}
				break;

			}
			super.handleMessage(msg);
		}

	};
	
	private class TaskUpdateThread extends Thread {

		@Override
		public void run() {
			mRunningProcessInfo = mActivityManager.getRunningAppProcesses();
			mTaskInfos = new TaskListProvider(mContext, mRunningProcessInfo).getTaskInfos();
			
			Message msg = Message.obtain();
			msg.what = MSG_SUCCESS;
			
			mHandler.sendMessage(msg);			
		}
	};
	
	private void initTaskData() {
		mProgress.show();
		TaskUpdateThread thread = new TaskUpdateThread();
		thread.start();
	}
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		lv = (ListView) findViewById(R.id.lv_task_manger);
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if ("com.safety.androidguard".equals(mTaskInfos.get(position)
						.getPackname())) {
					return;
				}

				CheckBox cb = (CheckBox) view
						.findViewById(R.id.cb_task_item_isselect);
				if (cb.isChecked()) {
					mTaskInfos.get(position).setIschecked(false);
					cb.setChecked(false);
				} else {
					mTaskInfos.get(position).setIschecked(true);
					cb.setChecked(true);
				}

			}
		});
		
		mActivityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

		lv.setOnItemLongClickListener(this);

		mProgress = new ProgressDialog(this);
		mProgress.setTitle(R.string.please_wait);
		setTitle();

		mTaskProvider = new TaskListProvider(this, mRunningProcessInfo);
	}

	/**
	 * Call by TaskUpdateHandler
	 */
	public void setTitle() {
		tv_task_num = (TextView) this
				.findViewById(R.id.tv_task_manager_tasknum);
		tv_avail_memory = (TextView) this
				.findViewById(R.id.tv_task_manager_availmemory);
		MemoryInfo outInfo = new MemoryInfo();
		mActivityManager.getMemoryInfo(outInfo);

		long availmem = outInfo.availMem;
		tv_avail_memory.setText(getString(R.string.available_mems) + getAvailMemString(availmem));
		mRunningProcessInfo = mActivityManager.getRunningAppProcesses();
		tv_task_num.setText(getString(R.string.running_tasks) + mRunningProcessInfo.size());
	}

	private String getAvailMemString(long availmem) {
		double result = availmem / 1024f / 1024f;
		DecimalFormat df = new DecimalFormat("###.00");

		return df.format(result) + " MB";
	}

	public void configProcess(View view) {
		int total = 0;
		int pid = 0;

		for (TaskInfo info : mTaskInfos) {
			if (info.isIschecked()) {
				total++;
				//am.restartPackage(info.getPackname());
				pid = info.getPid();
			}
		}
		initTaskData();
		Toast.makeText(mContext, "Set " + total + " tasks(s) to multi-core mode", Toast.LENGTH_LONG).show();
	}

	private String getMemoryInfo(int size) {
		if (size < 1024) {
			return size + " KB";
		} else if (size < 1024 * 1024) {
			double sizemb = size / 1024f;
			DecimalFormat df = new DecimalFormat("###.00");
			return df.format(sizemb) + " MB";
		} else
			return "";
	}

	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position,
			long id) {
					
			TaskInfoUtils app =   (TaskInfoUtils) getApplication();
			app.taskInfo = mTaskInfos.get(position);
			
			//Intent intent = new Intent("org.metromenu.preview.AppDetailDialog");
			//startActivity(intent);
			
			return true;
	}
	
	@Override
	protected void onResume() {
		initTaskData();
		super.onResume();
	}
}
