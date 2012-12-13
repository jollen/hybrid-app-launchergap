package com.metromenu.preview;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class MetroPhoneStateListener extends PhoneStateListener{
	private static final String TAG = "PhoneStateChanged";
	Context context;
	public MetroPhoneStateListener(Context context) {
		super();
		this.context = context;
	}
	@Override
	public void onCallStateChanged(int state, String incomingNumber){
		super.onCallStateChanged(state, incomingNumber);
		switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				Toast.makeText(context, "Phone State Idle", Toast.LENGTH_LONG).show();
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				Toast.makeText(context, "Phone State Off hook", Toast.LENGTH_LONG).show();
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				Toast.makeText(context, "Phone State Ringing", Toast.LENGTH_LONG).show();
				break;
			default:
				break;
		}
	}

}
