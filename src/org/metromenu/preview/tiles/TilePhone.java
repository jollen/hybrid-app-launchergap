package org.metromenu.preview.tiles;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;

public class TilePhone {
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
}
