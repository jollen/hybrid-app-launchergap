package org.metromenu.preview;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class SpecialTileActivity extends Activity {

	private String TAG = "SpecialTileActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.tile_module_radio_group);
	}

	public void onRadioButtonClicked(View view) {	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.module_camera:
	        	Log.i(TAG, "Add a special Camera module.");
	            break;
	        case R.id.module_browser:
	        	Log.i(TAG, "Add a special Browser module.");	
	            break;
	        case R.id.module_phone:
	        	Log.i(TAG, "Add a special Phone module.");
	            break;
	    }
	}
}
