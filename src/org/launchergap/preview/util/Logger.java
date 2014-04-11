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
package org.launchergap.preview.util;

import android.util.Log;

public class Logger {
	public static int LOGLEVEL = 6;
	public static int LOGLVIRBOS = 5;
	public static int LOGLDEBUG = 4;
	public static int LOGLINFO = 3;
	public static int LOGLWARN = 2;
	public static int LOGLERROR = 1;

	public static int w(String tag, String msg) {
		if (LOGLEVEL > LOGLWARN)
			return Log.w(tag, msg);
		else
			return 0;
	}

	public static int e(String tag, String msg) {
		if (LOGLEVEL > LOGLERROR)
			return Log.e(tag, msg);
		else
			return 0;
	}

	public static int i(String tag, String msg) {
		if (LOGLEVEL > LOGLINFO)
			return Log.i(tag, msg);
		else
			return 0;
	}

	public static int v(String tag, String msg) {
		if (LOGLEVEL > LOGLVIRBOS)
			return Log.v(tag, msg);
		else
			return 0;
	}

	public static int d(String tag, String msg) {
		if (LOGLEVEL > LOGLDEBUG)

			return Log.d(tag, msg);
		else {
			return 0;
		}
	}
}
