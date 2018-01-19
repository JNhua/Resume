package com.hjn.welcome;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 自定义的ViewPager适配器
 * @author xujing
 *
 */
public class MyViewPagerAdapter extends PagerAdapter {
	private List<View> mLists;
	public MyViewPagerAdapter(List<View> pLists)
	{
		this.mLists=pLists;
	}	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mLists.size();
	}
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0==(arg1);
	}
	@Override
	public void destroyItem(View container, int position, Object object) {	
		((ViewPager)container).removeView(mLists.get(position));		
	}
	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager)container).addView(mLists.get(position));
		return mLists.get(position);
	}
	@Override
	public void finishUpdate(View arg0) {
	}
	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
		
	}

}