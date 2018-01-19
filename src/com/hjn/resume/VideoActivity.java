package com.hjn.resume;

import com.hjn.resume.R;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;


public class VideoActivity extends Activity{
	private Button mBtnPlay, mBtnPause, mBtnLoad;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_activity);
		mBtnPlay= (Button)findViewById(R.id.video_play);
		mBtnPause= (Button)findViewById(R.id.video_pause);
		mBtnLoad= (Button)findViewById(R.id.LoadButton);
		final VideoView videoView = (VideoView) findViewById(R.id.VideoView01);
		mBtnLoad.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0)
			{	
				//play resume.mp4 
				//videoView.setVideoPath("android.resource://com.hjn.resume/"+R.raw.resume);
				videoView.setMediaController(new MediaController(VideoActivity.this));
				videoView.requestFocus();
			}
		});

		mBtnPlay.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0)
			{
				videoView.start();
			}
		});

	   mBtnPause.setOnClickListener(new OnClickListener() {
		public void onClick(View arg0)
		{
			videoView.pause();
		}
	});
    }
	
}

