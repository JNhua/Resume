package com.hjn.resume;

import com.hjn.resume.R;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

public class WordActivity extends Activity{
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.word_activity);
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) { 
			Log.i("info", "landscape"); 
			} 
			else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) { 
			Log.i("info", "portrait");

			}
		
	}
}
