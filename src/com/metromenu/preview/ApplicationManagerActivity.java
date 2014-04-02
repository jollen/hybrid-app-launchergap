/*
 * Copyright (C) 2011 Moko365 Inc.
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

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.metromenu.preview.adapter.AppDetailAdapter;
import org.metromenu.preview.domain.MetroApplicationInfo;
import org.metromenu.preview.engine.ApplicationListProvider;
import org.metromenu.preview.ui.MyToast;
import org.metromenu.preview.util.Logger;
import org.metromenu.preview.database.MetroMenuDatabase;
 
public class ApplicationManagerActivity extends Activity implements OnClickListener {

	ListView lv;
	ApplicationListProvider provider; 
	
	List<MetroApplicationInfo> appinfos; 
	List<MetroApplicationInfo> userappinfos;
	
	ProgressDialog progressDialog;
	AppDetailAdapter adapter;  
	PopupWindow popupWindow;
	LayoutInflater inflater;
	
	boolean flag = false;
	private static final int GET_APPINFO_FINISH = 20;
	private static final String TAG = "ApplicationManagerActivity";
	
	private MetroMenuDatabase mDatabase;	
	private String mModuleName;
	
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case GET_APPINFO_FINISH:
				progressDialog.dismiss();
				
				if (adapter == null) {
					adapter = new AppDetailAdapter(ApplicationManagerActivity.this,
							appinfos);
					lv.setAdapter(adapter);
				}else{
					adapter.setAppInfos(appinfos);
					adapter.notifyDataSetChanged();
				}
				

				break;
			}
		}

	};
	
	private Thread mGetAppThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.installed_applications);
		
		progressDialog = new ProgressDialog(this);
		inflater = LayoutInflater.from(this);
		lv = (ListView) this.findViewById(R.id.lv_appmanage);

		provider = new ApplicationListProvider(this);
 
		progressDialog.setTitle("Searching");
		progressDialog.show();
		
		mGetAppThread = new Thread(new GetAppsTask());
		lv.setOnItemClickListener(new OnAppItemClickListener());
		lv.setOnScrollListener(new AppListScrollLinstener());
		
		mDatabase = new MetroMenuDatabase(this);		
		
		mModuleName = null;
		
		// Get Bundle from SpecialTileActivity
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			this.setModuleName(bundle.getString("module"));
		}
		
		mGetAppThread.start();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Get Bundle from SpecialTileActivity
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			this.setModuleName(bundle.getString("module"));
		}	
		mDatabase.open();
	}
	
	private void setModuleName(String string) {
		mModuleName = string;
		Log.i(TAG, "Set Module: " + mModuleName);
	}

	private void dismissPopupWindow() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}

	private class GetAppsTask implements Runnable {

		public void run() {
			appinfos = provider.getApplicationInfos();
			//appinfos = provider.getPackageInfos();
			
			Message msg = new Message();
			msg.what = GET_APPINFO_FINISH;
			handler.sendMessage(msg);
		}
	}

	private class OnAppItemClickListener implements OnItemClickListener {

		private static final String TAG = "OnAppItemClickListener";

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			dismissPopupWindow();
			
			// Check if we are in special tile mode.
			if (mModuleName != null) {
				if (flag) {
					metroMenuAddApplication(userappinfos.get(position).getPackname(),
							userappinfos.get(position).getAppName(),
							userappinfos.get(position).getLuncheractivity());
				} else {
					metroMenuAddApplication(appinfos.get(position).getPackname(),
							appinfos.get(position).getAppName(),
							appinfos.get(position).getLuncheractivity());
				}		
				return;
			} 
			
			View popupView = inflater.inflate(R.layout.popup_item, null);
			LinearLayout ll = (LinearLayout) popupView
					.findViewById(R.id.ll_popup_item);
			Animation animation = AnimationUtils.loadAnimation(
					ApplicationManagerActivity.this, R.anim.popup_enter);
			ImageView iv_delete = (ImageView) popupView
					.findViewById(R.id.iv_delete);
			ImageView iv_start = (ImageView) popupView
					.findViewById(R.id.iv_start);
			//ImageView iv_share = (ImageView) popupView
			//		.findViewById(R.id.iv_share);
			iv_delete.setOnClickListener(ApplicationManagerActivity.this);
			iv_delete.setTag(position);
			iv_start.setOnClickListener(ApplicationManagerActivity.this);
			iv_start.setTag(position);
			//iv_share.setOnClickListener(ApplicationManagerActivity.this);
			//iv_share.setTag(position);
			ll.setAnimation(animation);
			ll.startAnimation(animation);

			// Compute location of PopupWindow
			popupWindow = new PopupWindow(popupView,
					LinearLayout.LayoutParams.WRAP_CONTENT,
					view.getLayoutParams().height);			
			Drawable background = getResources().getDrawable(
					R.drawable.local_popup_bg);
			popupWindow.setBackgroundDrawable(background);
			int[] location = new int[2];
			view.getLocationInWindow(location);
			
			ImageView app_icon = (ImageView) view.findViewById(R.id.app_icon);
			int marginLeft = app_icon.getWidth() + 5;
			int marginTop = app_icon.getHeight();

			Log.i(TAG, "getLocationInWindow: " + location[0] + ", " + location[1] + ". marginLeft = " + marginLeft + ". marginTop = " + marginTop);
			popupWindow.showAtLocation(view, 51, 
								location[0] + marginLeft, 
								location[1] - 20);
		}

	}

	private class AppListScrollLinstener implements OnScrollListener {

		public void onScrollStateChanged(AbsListView view, int scrollState) {
			dismissPopupWindow();

		}

		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			dismissPopupWindow();
		}

	}

	public void onClick(View v) {
		appPopupWindow(v);
	}
	
	private void appPopupWindow(View v) {
		int position = (Integer) v.getTag();
		
		switch (v.getId()) {
		case R.id.iv_delete:
			Logger.i(TAG, "Deleted " + position + "name = "
					+ appinfos.get(position).getAppName());
			if(flag){
				deleteApp(userappinfos.get(position).getPackname());
			}else{
				deleteApp(appinfos.get(position).getPackname());
			}
			break;
		//case R.id.iv_share:
		//	if(flag){
		//		shareApp(userappinfos.get(position).getAppname());
		//	}else{
		//		shareApp(appinfos.get(position).getAppname());
		//	}
		//	break;
		case R.id.iv_start:
			if (flag) {
				metroMenuAddApplication(userappinfos.get(position).getPackname(),
						userappinfos.get(position).getAppName(),
						userappinfos.get(position).getLuncheractivity());
			} else {
				metroMenuAddApplication(appinfos.get(position).getPackname(),
						appinfos.get(position).getAppName(),
						appinfos.get(position).getLuncheractivity());
			}
			break;
		}

	}

	private void shareApp(String appname) {
		Intent intent = new Intent("android.intent.action.SEND");
		intent.setType("text/plain");
		intent.putExtra("android.intent.extra.SUBJECT", "[TEXT 2]");
		StringBuilder sb = new StringBuilder("[TEXT 1]");
		sb.append(appname);
		intent.putExtra("android.intent.extra.TEXT", sb.toString());
		intent = Intent.createChooser(intent, "[TEXT 3]");
		startActivity(intent);
		dismissPopupWindow();
	}

	private void metroMenuAddApplication(String packname, String appname, String activityname) {
		if (mDatabase.checkItemNoExist(packname)) {
			
			if(mModuleName == null) {
				mDatabase.saveItem(packname, appname, activityname);
			} else {
				// Add a custom tile
				mDatabase.updateItemByModuleName(mModuleName, packname, appname, activityname);
			}
		
			Toast.makeText(this, R.string.new_tile_added, Toast.LENGTH_SHORT).show(); 

			// Update MainActivity
	        sendBroadcast(new Intent("metromenu.intent.action.MENU_UPDATE"));
		} else {
			// Nothing to do
			Log.i(TAG, "Not saved: this item exists.");
		}
		
		finish();
	}

	private void deleteApp(String packname) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_DELETE);
		intent.setData(Uri.parse("package:" + packname));
		// startActivity(intent);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		progressDialog.show();
		new Thread(new GetAppsTask()).start();
	}

	public void change(View view){
		TextView tv =  (TextView) view;
	}
	
	@Override
	protected void onStop() {
		mDatabase.close();
		dismissPopupWindow();
		super.onStop();
	}
}
