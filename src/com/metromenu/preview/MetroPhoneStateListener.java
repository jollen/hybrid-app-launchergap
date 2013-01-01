package com.metromenu.preview;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class MetroPhoneStateListener extends PhoneStateListener{
	private static final String TAG = "PhoneStateChanged";
	Context context;

	/**
	 * Detect missing calls.
	 * Please refer to the following URLs.
	 * 	1) http://stackoverflow.com/questions/9684866/how-to-detect-when-phone-is-answered-or-rejected
	 *  2) http://stackoverflow.com/questions/12153070/handling-telephonymanager-handling-incoming-calls-dialed-missed-calls-is-t
	 */
	public static boolean wasRinging;

	public MetroPhoneStateListener(Context context) {
		super();
		this.context = context;
	}
	@Override
	public void onCallStateChanged(int state, String incomingNumber){
		//super.onCallStateChanged(state, incomingNumber);
		switch (state) {
		case TelephonyManager.CALL_STATE_IDLE:
			//Toast.makeText(context, "Phone State Idle. Miss", Toast.LENGTH_LONG).show();
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:
			//Toast.makeText(context, "Phone State Off hook", Toast.LENGTH_LONG).show();
			break;
		case TelephonyManager.CALL_STATE_RINGING:
			//Toast.makeText(context, "Phone State Ringing", Toast.LENGTH_LONG).show();
			break;
		default:
			break;
		}
	}
}
