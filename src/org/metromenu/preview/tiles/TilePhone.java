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
package org.metromenu.preview.tiles;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;

public class TilePhone implements Tile {
	private Context context;

	public TilePhone(Context context) {
		this.context = context;
	}
	
	public int getMissedCalls() {
		return queryMissedCalls();
	}
	
	/**
	 * Please refer to http://stackoverflow.com/questions/10771188/get-the-missed-call-list-and-delete-it-from-call-log-in-android
	 * @return
	 */
	private int queryMissedCalls() {
		final String[] projection = null;
		final String selection = null;
		final String[] selectionArgs = null;
		final String sortOrder = CallLog.Calls.DATE + " DESC";
		Cursor cursor = null;	
		
		int missedCalls = 0;
		
		try{
			cursor = context.getContentResolver().query(
					CallLog.Calls.CONTENT_URI,
					projection,
					selection,
					selectionArgs,
					sortOrder);
			
			while (cursor.moveToNext()) { 
				String callLogID = cursor.getString(cursor.getColumnIndex(CallLog.Calls._ID));
				String callNumber = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
				String callDate = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
				String callType = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
				String isCallNew = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NEW));
				
				int dircode = Integer.parseInt( callType );
				switch( dircode ) {
				case CallLog.Calls.OUTGOING_TYPE:
					break;

				case CallLog.Calls.INCOMING_TYPE:
					break;

				case CallLog.Calls.MISSED_TYPE:
					missedCalls = missedCalls + 1;
					break;
				}
			}
		} catch(Exception ex){
		}		
		
		return missedCalls;
	}

	public int request() {
		return getMissedCalls();
	}
}
