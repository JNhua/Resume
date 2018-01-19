package com.hjn.resume;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import at.markushi.ui.CircleButton;
import com.hjn.circlemenu.CircleImageView;
import com.hjn.circlemenu.CircleLayout;
import com.hjn.circlemenu.CircleLayout.OnItemClickListener;
import com.hjn.circlemenu.CircleLayout.OnItemSelectedListener;
import com.hjn.resume.R;
@SuppressWarnings("deprecation")

public class MainActivity extends Activity implements OnItemSelectedListener, OnItemClickListener{
	
	TextView selectedTextView;
	MusicService service;
	Intent intent;
	CircleButton cb1;
	CircleLayout circleMenu;
	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		
		//启动背景音乐
		intent=new Intent(MainActivity.this,MusicService.class);
		startService(intent);
		
		        
		cb1=(CircleButton)findViewById(R.id.circleButton1);
		circleMenu = (CircleLayout)findViewById(R.id.main_circle_layout);
		circleMenu.setOnItemSelectedListener(this);
		circleMenu.setOnItemClickListener(this);
		selectedTextView = (TextView)findViewById(R.id.main_selected_textView);
		selectedTextView.setText(((CircleImageView)circleMenu.getSelectedItem()).getName());
		cb1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Handler().postDelayed(new Runnable(){   
				    public void run() {   
				       OpenOutDialog(); 
				    }   
				 }, 400);   
			}
			});
		
	}
	
	
	@Override
	public void onItemSelected(View view, int position, long id, String name) {
		// TODO Auto-generated method stub
		selectedTextView.setText(name);
	}
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
        //当用户在主类上按下back键，则调用退出对话框
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {   
            OpenOutDialog();  //这里是以上的退出对话框
            return true;  
        }
        return super.onKeyDown(keyCode, event);  
    }
    
    private void OpenOutDialog(){
        new AlertDialog.Builder(MainActivity.this)
            .setTitle("请看：")
            .setMessage("狠心退出?")
            .setPositiveButton("嗯,拜拜",
                new DialogInterface.OnClickListener(){
                public void onClick(
                    DialogInterface dialoginterface, int i){
                	stopService(intent);
                        dialoginterface.dismiss();
                        finish();
                }
            })
            .setNeutralButton("再看看",
                new DialogInterface.OnClickListener(){
                public void onClick(	
                    DialogInterface dialoginterface, int i){
                        dialoginterface.dismiss();
                }
            })
            .show();
    }
	
	 
	
	 
	
	public void onItemClick(View view, int position, long id, String name) {
		Intent intent1;
		if(position==1){
			intent1 = new Intent();
			intent1.setClass(MainActivity.this, PictureActivity.class);
			MainActivity.this.startActivity(intent1);
		}
		if(position==2){
			stopService(intent);
			intent1 = new Intent();
			intent1.setClass(MainActivity.this, VideoActivity.class);
			MainActivity.this.startActivity(intent1);
		}
		if(position==5){
			intent1 = new Intent();
			intent1.setClass(MainActivity.this, Web_microblog.class);
			MainActivity.this.startActivity(intent1);
		}
		if(position==0){
			intent1 = new Intent();
			intent1.setClass(MainActivity.this, WordActivity.class);
			MainActivity.this.startActivity(intent1);
		}
		if(position==3){
			intent1 = new Intent();
			intent1.setClass(MainActivity.this, Shake.class);
			MainActivity.this.startActivity(intent1);
		}
		if(position==4){
			intent1 = new Intent();
			intent1.setClass(MainActivity.this, Hobby.class);
			MainActivity.this.startActivity(intent1);
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onRestart();
		startService(intent);
	}

}

