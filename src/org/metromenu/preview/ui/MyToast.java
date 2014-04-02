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
package org.metromenu.preview.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.metromenu.preview.R;

public class MyToast {

	public static void show(Context context, int icon, int text){
		Toast toast = new Toast(context);
		LayoutInflater inflater = LayoutInflater.from(context);
	    View view = inflater.inflate(R.layout.mytoast, null);
		TextView tv = (TextView) view.findViewById(R.id.tv_my_toast);
		ImageView iv = (ImageView) view.findViewById(R.id.iv_my_toast);
		tv.setText(text);
		iv.setImageResource(icon);
		toast.setView(view);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.show();	
	}
	public static void show(Context context, int icon, String text){
		Toast toast = new Toast(context);
		LayoutInflater inflater = LayoutInflater.from(context);
	    View view = inflater.inflate(R.layout.mytoast, null);
		TextView tv = (TextView) view.findViewById(R.id.tv_my_toast);
		ImageView iv = (ImageView) view.findViewById(R.id.iv_my_toast);
		tv.setText(text);
		iv.setImageResource(icon);
		toast.setView(view);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.show();	
	}
}
