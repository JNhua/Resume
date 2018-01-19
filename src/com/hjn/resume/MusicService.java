package com.hjn.resume;

import com.hjn.resume.R;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;


public class MusicService extends Service {
	private MediaPlayer mediaPlayer;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		if(mediaPlayer==null){
			mediaPlayer = MediaPlayer.create(MusicService.this,R.raw.music);
			mediaPlayer.setLooping(true);
			mediaPlayer.start();
		}else{
			mediaPlayer.setLooping(true);
			mediaPlayer.start();
		}
	}
	
	
	@Override
	public IBinder onBind(Intent intent) {
	// TODO Auto-generated method stub
		return new MyBinder();
	}
	
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	};
	public class MyBinder extends Binder{
		public MusicService getService(){
			return MusicService.this;
		}
	}

	@Override
	public void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
	mediaPlayer.stop();
	
	}
}

