package com.hjn.resume;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.widget.Gallery;

@SuppressWarnings("deprecation")
public class MyGallery  extends Gallery {
	private GestureDetector mGestureDetector;//GestureDetector为手势识别类
	private MyImageViewTouchBase mMyImageView;

	public MyGallery(Context context) {
		super(context);

	}

	public MyGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(new MySimpleGesture());
		
		this.setOnTouchListener(new OnTouchListener() {
			float baseValue;//原始大小
			float originalScale;//原比例
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				View view = MyGallery.this.getSelectedView();
				if (view instanceof MyImageViewTouchBase) {
					mMyImageView = (MyImageViewTouchBase) view;
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						baseValue = 0;
						originalScale = mMyImageView.getScale();
					}
					if (event.getAction() == MotionEvent.ACTION_MOVE) {
						if (event.getPointerCount() == 2) {
							float x = event.getX(0) - event.getX(1);
							float y = event.getY(0) - event.getY(1);
							float value = (float) Math.sqrt(x * x + y * y);// 计算两点的距离
							
							if (baseValue == 0) {
								baseValue = value;
							} else {
								float scale = value / baseValue;// 当前两点间的距离除以手指落下时两点间的距离就是需要缩放的比例。
								
								mMyImageView.zoomTo(originalScale * scale, x + event.getX(1), y + event.getY(1));

							}
						}
					}
				}
				return false;
			}

		});
	}
	
	
	
	
	/**
	 * 用户按下触摸屏，并拖动，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE触发
	 * 参数解释：
		  e1：第1个ACTION_DOWN MotionEvent   
	      e2：最后一个ACTION_MOVE MotionEvent   
	      velocityX：X轴上的移动速度，像素/秒   
	      velocityY：Y轴上的移动速度，像素/秒     
	 */
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		View view = MyGallery.this.getSelectedView();
		if (view instanceof MyImageViewTouchBase) {
			mMyImageView = (MyImageViewTouchBase) view;

			float v[] = new float[9];
			Matrix m = mMyImageView.getImageMatrix();
			m.getValues(v);
			// 图片实时的上下左右坐标
			float left, right;
			// 图片的实时宽，高
			float width, height;
			width = mMyImageView.getScale() * mMyImageView.getImageWidth();
			height = mMyImageView.getScale() * mMyImageView.getImageHeight();
			// 以下逻辑为移动图片和滑动gallery换屏的逻辑。如果没对整个框架了解的非常清晰，改动以下的代码前请三思！！！！！！
			if ((int) width <= PictureActivity.screenWidth && (int) height <= PictureActivity.screenHeight)// 如果图片当前大小<屏幕大小，直接处理滑屏事件
			{
				super.onScroll(e1, e2, distanceX, distanceY);
			} else {
				left = v[Matrix.MTRANS_X];
				right = left + width;
				Rect r = new Rect();
				mMyImageView.getGlobalVisibleRect(r);

				if (distanceX > 0)// 向左滑动
				{
					if (r.left > 0) {// 判断当前ImageView是否显示完全
						super.onScroll(e1, e2, distanceX, distanceY);
					} else if (right < PictureActivity.screenWidth) {
						super.onScroll(e1, e2, distanceX, distanceY);
					} else {
						mMyImageView.postTranslate(-distanceX, -distanceY);
					}
				} else if (distanceX < 0)// 向右滑动
				{
					if (r.right < PictureActivity.screenWidth) {
						super.onScroll(e1, e2, distanceX, distanceY);
					} else if (left > 0) {
						super.onScroll(e1, e2, distanceX, distanceY);
					} else {
						mMyImageView.postTranslate(-distanceX, -distanceY);
					}
				}

			}

		} else {
			super.onScroll(e1, e2, distanceX, distanceY);
		}
		return true;
	}
	/**
	 * 用户按下触摸屏、快速移动后松开，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOV
	 * 参数解释：
		  e1：第1个ACTION_DOWN MotionEvent   
	      e2：最后一个ACTION_MOVE MotionEvent   
	      velocityX：X轴上的移动速度，像素/秒   
	      velocityY：Y轴上的移动速度，像素/秒  
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		 // TODO Auto-generated method stub

	     int kEvent;

	           if(isScrollingLeft(e1, e2)){ //Check if scrolling left

	             kEvent = KeyEvent.KEYCODE_DPAD_LEFT;

	           }

	           else{ //Otherwise scrolling right

	             kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;

	           }

	           onKeyDown(kEvent, null);

	           return true;
	    

	    }

	    private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2){

	           return e2.getX() > e1.getX();

	    }

	/** 
     * 当发现屏幕触控事件的时候，首先出发此方法，再通过此方法，监听具体的整件 
     * 在onTouch()方法中，我们调用GestureDetector的onTouchEvent()方法，
     * 将捕捉到的MotionEvent交给GestureDetector 来分析是否有合适的callback函数来处理用户的手势 
     */ 
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mGestureDetector.onTouchEvent(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			// 判断上下边界是否越界
			View view = MyGallery.this.getSelectedView();
			if (view instanceof MyImageViewTouchBase) {
				mMyImageView = (MyImageViewTouchBase) view;
				float width = mMyImageView.getScale() * mMyImageView.getImageWidth();
				float height = mMyImageView.getScale() * mMyImageView.getImageHeight();
				if ((int) width <= PictureActivity.screenWidth && (int) height <= PictureActivity.screenHeight)// 如果图片当前大小<屏幕大小，判断边界
				{
					break;
				}
				float v[] = new float[9];
				Matrix m = mMyImageView.getImageMatrix();
				m.getValues(v);
				float top = v[Matrix.MTRANS_Y];
				float bottom = top + height;
				if (top > 0) {
					mMyImageView.postTranslateDur(-top, 200f);
				}
				Log.i("manga", "bottom:" + bottom);
				if (bottom < PictureActivity.screenHeight) {
					mMyImageView.postTranslateDur(PictureActivity.screenHeight - bottom, 200f);
				}

			}
			break;
		}
		return super.onTouchEvent(event);
	}

	private class MySimpleGesture extends SimpleOnGestureListener {
		// 按两下的第二下Touch down时触发
		public boolean onDoubleTap(MotionEvent e) {
			View view = MyGallery.this.getSelectedView();
			if (view instanceof MyImageViewTouchBase) {
				mMyImageView = (MyImageViewTouchBase) view;
				if (mMyImageView.getScale() > mMyImageView.getScaleRate()) {
					mMyImageView.zoomTo(mMyImageView.getScaleRate(), PictureActivity.screenWidth / 2, PictureActivity.screenHeight / 2, 200f);
					// imageView.layoutToCenter();
				} else {
					mMyImageView.zoomTo(1.0f, PictureActivity.screenWidth / 2, PictureActivity.screenHeight / 2, 200f);
				}

			} else {

			}
			return true;
		}
	}
}
