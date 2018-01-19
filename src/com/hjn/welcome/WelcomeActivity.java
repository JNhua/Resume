package com.hjn.welcome;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import at.markushi.ui.CircleButton;

import com.hjn.resume.MainActivity;
import com.hjn.resume.R;
import com.hjn.welcome.MyViewPagerAdapter;

public class WelcomeActivity extends Activity {

	
	private List<View> mLists=new ArrayList<View>();
	private ViewPager mViewPager;
	private MyViewPagerAdapter adapter;
	private CircleButton mButton;
	
	private LayoutInflater mLayoutInflater;
	private LinearLayout mLinearLayout;
	private int current=0;
	
	private ImageView[] points=new ImageView[4];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logo);
		mLayoutInflater=LayoutInflater.from(this);
		mLinearLayout=(LinearLayout)this.findViewById(R.id.liear);
		mViewPager=(ViewPager)this.findViewById(R.id.viewpager);		
		View view1=mLayoutInflater.inflate(R.layout.welcome01, null);
		View view2=mLayoutInflater.inflate(R.layout.welcome02, null);
		View view3=mLayoutInflater.inflate(R.layout.welcome03, null);
		View view4=mLayoutInflater.inflate(R.layout.welcome04, null);
		mButton=(CircleButton)view4.findViewById(R.id.btn_start);
		mButton.setOnClickListener(new MySetOnClickListener());
		mLists.add(view1);
		mLists.add(view2);
		mLists.add(view3);
		mLists.add(view4);
		adapter=new MyViewPagerAdapter(mLists);
		mViewPager.setAdapter(adapter);
		mViewPager.setOnPageChangeListener(new MyOnPagerChangerListener());
		initPoints();
	}	
	 //初始化下面的小圆点
    private void initPoints()
    {
  	  for(int i=0;i<4;i++)
  	  {
  		  points[i]=(ImageView)mLinearLayout.getChildAt(i);
  		  points[i].setImageResource(R.drawable.sysclear_ic_storage_left);  		  
  	  }
  	  current=0;//默认在第一页
  	  points[current].setImageResource(R.drawable.sysclear_ic_storage_used);//此刻处于第一页，把第一页的小圆圈设置为unenabled
    }	
	class MyOnPagerChangerListener implements OnPageChangeListener
	{
		@Override
		public void onPageScrollStateChanged(int arg0) {			
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
					
		}
		@Override
		public void onPageSelected(int arg0) {
			points[arg0].setImageResource(R.drawable.sysclear_ic_storage_used);
			points[current].setImageResource(R.drawable.sysclear_ic_storage_left);
			current=arg0;
		}
		
	}
	
	
	class  MySetOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			new Handler().postDelayed(new Runnable(){   
			    public void run() {   
			       Intent intent=new Intent();
			       intent.setClass(WelcomeActivity.this, MainActivity.class);
			       startActivity(intent);
			       WelcomeActivity.this.finish();
			    }   
			 }, 400);
		}
		
	}
}
