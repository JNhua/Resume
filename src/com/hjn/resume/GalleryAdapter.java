package com.hjn.resume;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;


public class GalleryAdapter extends BaseAdapter {

	private Context context;
	
	public GalleryAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return PictureConstant.images.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
				PictureConstant.images[position]);
		MyImageViewTouchBase view = new MyImageViewTouchBase(context, bmp.getWidth(),
				bmp.getHeight());
		view.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		view.setImageBitmap(bmp);
		return view;
		
	}

}
