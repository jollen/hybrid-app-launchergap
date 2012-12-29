/*
 * Copyright (C) 2011 Moko365 Inc.
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
package org.metromenu.preview.database;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MetroMenuDatabase extends SQLiteOpenHelper {

	private static final String TAG = "MetroMenuDatabase";

	private static final String DATABASE_NAME = "metromenu_preview.db";
	private static final String TABLE_NAME = "items";
	private static final int DATABASE_VERSION = 70; 
    private static final String TABLE_CREATE =
        "CREATE TABLE " + TABLE_NAME + "("
    	     + "_ID INTEGER PRIMARY KEY,"
    	     + "package_name TEXT,"
    	     + "app_name TEXT,"
    	     + "activity_name TEXT,"
    	     + "module TEXT,"
    	     + "image TEXT,"
    	     + "size TEXT,"
    	     + "theme TEXT,"
    	     + "ordering INTEGER"
        + ");";
    
	private SQLiteDatabase db;
	private Cursor mCursor;
	private MetroMenuDatabase mMokoWebDatabase;
	
	// Not Singleton at this project
	public MetroMenuDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		open();
	}
	
	public synchronized void open() {
		db = this.getWritableDatabase();
		Log.i(TAG, "on open");
	}
	
	@Override
	public void close() {
		synchronized (db) {
			db.close();
			mMokoWebDatabase = null;			
		}
		Log.i(TAG, "on close");
	}	

	@Override
	public void onCreate(SQLiteDatabase db) {
		synchronized (db) {
			db.execSQL(TABLE_CREATE);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop old
		db.execSQL("DROP TABLE IF EXISTS items");
		// Create new one
		onCreate(db);
	}
	
	public void saveItem(String package_name, 
			String app_name, String activity_name) {
		put(package_name, app_name, activity_name);
	}
	
	public void saveItem(String package_name, 
			String app_name, String activity_name, String module_name, String image, String module) {
		put(package_name, app_name, activity_name, module_name, image, module, "1x1");		
	}

	private synchronized Cursor get(String package_name) throws SQLException {
		Cursor cursor = db.query(true,
			"items",
			new String[] {"_ID", 
							"package_name", 
							"app_name",
							"activity_name",
				    	     "module",	
							"image",
							"size",
							"theme",
							"ordering"},
			"package_name=\"" + package_name + "\"",	// WHERE
			null, 						// Parameters to WHERE
			null, 						// GROUP BY
			null, 						// HAVING
			null, 						// ORDOR BY
			null  						// Max num of return rows
		);
 
		// Must check
		if (0 != cursor.getCount()) {
			cursor.moveToFirst(); // Move to first row
		} else {
			return null;
		}
		return cursor;
	}
	
	private synchronized long put(String package_name, 
			String app_name, String activity_name) {
		ContentValues args = new ContentValues();
		args.put("package_name", package_name);
		args.put("app_name", app_name);		
		args.put("activity_name", activity_name);
		args.put("image", "");
		args.put("module", "");
		args.put("size", "1x1");				// The default tile size
		args.put("theme", "metro-green");		// No image, and the default theme is metro-green
		args.put("ordering", 999);
		
		long id = db.insert("items", null, args);
		Log.i(TAG, "Insert to: " + id + ", " + activity_name);

		return id;
    }

	private synchronized long put(String package_name, 
			String app_name, String activity_name, String module_name, String image, String theme, String size) {
		ContentValues args = new ContentValues();
		args.put("package_name", package_name);
		args.put("app_name", app_name);		
		args.put("activity_name", activity_name);
		args.put("image", image);
		args.put("module", module_name);
		args.put("size", size);		
		args.put("theme", theme);		
		args.put("ordering", 999);
		
		long id = db.insert("items", null, args);
		Log.i(TAG, "Insert to: " + id + ", " + activity_name + " (Module: " + module_name + ")");

		return id;
    }

	public long putDefaultTile(String module_name, String image, String theme, String size) {
		return put("", module_name, "", module_name, image, theme, size);
    }
	
	/**
	 * checkItemExist
	 * @param url
	 * @return true: exist in DB
	 */
	public boolean checkItemNoExist(String package_name) {
		if (db.isOpen())Log.i(TAG, "db is open");
		else Log.i(TAG,"db is close");
		return get(package_name) == null ? true : false;
	}
	
	/**
	 * Get all tiles and order by 'order'.
	 * 
	 * @return
	 */
	private synchronized Cursor getAll() {
		Cursor cursor = db.query(true,
				"items",
				new String[] {"_ID", 
								"package_name", 
								"app_name",
								"activity_name",
								"module",
								"image",
								"size",
								"theme",
								"ordering"},
								null,	// WHERE
				null, 						// Parameters to WHERE
				null, 						// GROUP BY
				null, 						// HAVING
				"ordering", 						// ORDER BY
				null  						// Max num of return rows
			);
	 
			// Must check
			if (0 != cursor.getCount()) {
				cursor.moveToFirst(); // Move to first row
			}
			return cursor;	
	}
	
	public String getJSON() {
		Cursor items = getAll();
		String json_code;
		int id, order;
		String packageName, appName, activityName, moduleName, image, size, theme;
		
		if (items.getCount() == 0) {
			return null;
		}
		
		json_code = "[";
		
		for (int i = 0; i < items.getCount(); i++) {
			id = items.getInt(0);
			packageName = items.getString(1);
			appName = items.getString(2);
			activityName = items.getString(3);
			moduleName = items.getString(4);
			image = items.getString(5);
			size = items.getString(6);
			theme = items.getString(7);
			order = items.getInt(8);
			
			json_code += "{";
			
			json_code += "id: \"" + id + "\","; 					// _ID (primary key)
			json_code += "package: \"" + packageName + "\","; 		// package name
			json_code += "app: \"" + appName + "\",";				// application name
			json_code += "activity: \"" + activityName + "\",";		// activity name
			json_code += "module: \"" + moduleName + "\",";			// module name
			json_code += "image: \"" + image + "\",";				// image URL
			json_code += "size: \"" + size + "\",";					// tile size
			json_code += "theme: \"" + theme + "\"";				// tile size			
			
			json_code += "}";
			if (i < (items.getCount() - 1))
				json_code += ",";
			items.moveToNext();
		}
		
		json_code += "]";
		
		return json_code;
	}

	public synchronized String getPackageByModule(String moduleName) {
		Cursor cursor = db.query(true,
				"items",
				new String[] {"_ID", 
								"package_name", 
								"app_name",
								"activity_name",
								"module",
								"image",
								"size",
								"theme",
								"ordering"},
				"module=\"" + moduleName + "\"",	// WHERE
				null, 						// Parameters to WHERE
				null, 						// GROUP BY
				null, 						// HAVING
				null, 						// ORDOR BY
				null  						// Max num of return rows
			);
	 
			// Must check
			if (0 != cursor.getCount()) {
				cursor.moveToFirst(); 		// Move to first row
				return cursor.getString(1); // package name
			}
			return null;
	}

	public synchronized String getActivityByModule(String moduleName) {
		Cursor cursor = db.query(true,
				"items",
				new String[] {"_ID", 
								"package_name", 
								"app_name",
								"activity_name",
								"module",
								"image",
								"size",
								"theme",
								"ordering"},
								"module=\"" + moduleName + "\"",	// WHERE
				null, 						// Parameters to WHERE
				null, 						// GROUP BY
				null, 						// HAVING
				null, 						// ORDOR BY
				null  						// Max num of return rows
			);
	 
			// Must check
			if (0 != cursor.getCount()) {
				cursor.moveToFirst(); 		// Move to first row
				return cursor.getString(3); // activity name
			}
			return null;
	}

	public void reset() {
		this.removeAll();
	}

	private void removeAll() {
		int rows = db.delete(TABLE_NAME, null, null);
		Log.i(TAG, "Delete " + rows + " rows.");
	}

	/**
	 * @deprecated
	 */
	public void setTileSize(String packageName, String activityName, String size) {
        ContentValues cv = new ContentValues();
        cv.put("size", size);

		int result = db.update(TABLE_NAME,
				cv,
				"package_name=\"" + packageName + "\"",
				null
			);		
		
		Log.i(TAG, packageName + ": setTileSize = " + size + ", result: " + result);
	}

	public synchronized void setTileSize(int tileID, String size) {
        ContentValues cv = new ContentValues();
        cv.put("size", size);

		int result = db.update(TABLE_NAME,
				cv,
				"_ID=" + tileID,
				null
			);	
		
		Log.i(TAG, "Update " + tileID + ", result: " + result + ", size: " + size);
	}
	
	public boolean isEmpty() {
		Cursor items = getAll();
		
		if (items.getCount() == 0) {
			return true;
		}
		return false;
	}

	public void updateItemByModuleName(String module_name, String package_name,
			String app_name, String activity_name) {
		
		ContentValues args = new ContentValues();
		args.put("package_name", package_name);
		args.put("app_name", app_name);		
		args.put("activity_name", activity_name);
		
		int result = db.update(TABLE_NAME,
				args,
				"module=\"" + module_name + "\"",
				null
			);	
		Log.i(TAG, "updateItemByModuleName: " + module_name);
	}

	public void updateOrderByID(int id, int order) {
		
		ContentValues args = new ContentValues();
		args.put("ordering", order);
		
		int result = db.update(TABLE_NAME,
				args,
				"_ID=\"" + id + "\"",
				null
			);	
		//Log.i(TAG, "updateOrderByID. ID = " + id + ", Order = " + order);
	}
}
