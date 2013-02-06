package com.metromenu.preview;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

public class MetroShareToWeb extends Activity {

	private static final String TAG = "MetroShareToWeb";
	private InputStream httpInputStream;
	private ProgressDialog progressDialog;
	
	private class sendImageThread extends Thread {
		
		private 
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("Photo to TV...");
		
		setContentView(R.layout.share_to_web);
		
        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        
        if (Intent.ACTION_SEND.equals(action) && type != null) {
        	Log.i(TAG, "Share to Tile8");
        	
            if (type.startsWith("image/")) {
        		progressDialog.show();
                handleSendImage(intent); // Handle text being sent
            }
        } else {
            // Handle other intents, such as being started from the home screen
        	
        }
	}

	public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

	void handleSendImage(Intent intent) {
	    Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
	    if (imageUri != null) {
	        // Update UI to reflect image being shared
	    	String realPath = getRealPathFromURI(imageUri);
	    	Log.i(TAG, "Share path: " + realPath);

	    	// 1. base64 encoding
	    	ArrayList<NameValuePair> nameValuePairs = HttpBase64(realPath);
	    	
	    	// 2. upload to server (TODO: use JSON)
			Log.i(TAG, "Try to upload...");
			httpUpload("http://metromenu.me/upload.php", nameValuePairs);
	    }
	}  
	
	private ArrayList<NameValuePair> HttpBase64(String path) {
		Bitmap bitmap = BitmapFactory.decodeFile(path);	
		Bitmap bitmapOrg = Bitmap.createScaledBitmap(bitmap, 816, 612, false);
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		
		bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 60, byteStream);
		
		byte [] ba = byteStream.toByteArray();
		String ba1 = Base64.encodeToString(ba, Base64.DEFAULT);

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("image",ba1));
		
		return nameValuePairs;
	}

	private void httpUpload(String server,
			ArrayList<NameValuePair> nameValuePairs) {

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(server);
		
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			httpInputStream = entity.getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

}
