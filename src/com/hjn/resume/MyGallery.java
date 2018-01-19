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
	private GestureDetector mGestureDetector;//GestureDetectorΪ����ʶ����
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
			float baseValue;//ԭʼ��С
			float originalScale;//ԭ����
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
							float value = (float) Math.sqrt(x * x + y * y);// ��������ľ���
							
							if (baseValue == 0) {
								baseValue = value;
							} else {
								float scale = value / baseValue;// ��ǰ�����ľ��������ָ����ʱ�����ľ��������Ҫ���ŵı�����
								
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
	 * �û����´����������϶�����1��MotionEvent ACTION_DOWN, ���ACTION_MOVE����
	 * �������ͣ�
		  e1����1��ACTION_DOWN MotionEvent   
	      e2�����һ��ACTION_MOVE MotionEvent   
	      velocityX��X���ϵ��ƶ��ٶȣ�����/��   
	      velocityY��Y���ϵ��ƶ��ٶȣ�����/��     
	 */
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		View view = MyGallery.this.getSelectedView();
		if (view instanceof MyImageViewTouchBase) {
			mMyImageView = (MyImageViewTouchBase) view;

			float v[] = new float[9];
			Matrix m = mMyImageView.getImageMatrix();
			m.getValues(v);
			// ͼƬʵʱ��������������
			float left, right;
			// ͼƬ��ʵʱ����
			float width, height;
			width = mMyImageView.getScale() * mMyImageView.getImageWidth();
			height = mMyImageView.getScale() * mMyImageView.getImageHeight();
			// �����߼�Ϊ�ƶ�ͼƬ�ͻ���gallery�������߼������û����������˽�ķǳ��������Ķ����µĴ���ǰ����˼������������
			if ((int) width <= PictureActivity.screenWidth && (int) height <= PictureActivity.screenHeight)// ���ͼƬ��ǰ��С<��Ļ��С��ֱ�Ӵ������¼�
			{
				super.onScroll(e1, e2, distanceX, distanceY);
			} else {
				left = v[Matrix.MTRANS_X];
				right = left + width;
				Rect r = new Rect();
				mMyImageView.getGlobalVisibleRect(r);

				if (distanceX > 0)// ���󻬶�
				{
					if (r.left > 0) {// �жϵ�ǰImageView�Ƿ���ʾ��ȫ
						super.onScroll(e1, e2, distanceX, distanceY);
					} else if (right < PictureActivity.screenWidth) {
						super.onScroll(e1, e2, distanceX, distanceY);
					} else {
						mMyImageView.postTranslate(-distanceX, -distanceY);
					}
				} else if (distanceX < 0)// ���һ���
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
	 * �û����´������������ƶ����ɿ�����1��MotionEvent ACTION_DOWN, ���ACTION_MOV
	 * �������ͣ�
		  e1����1��ACTION_DOWN MotionEvent   
	      e2�����һ��ACTION_MOVE MotionEvent   
	      velocityX��X���ϵ��ƶ��ٶȣ�����/��   
	      velocityY��Y���ϵ��ƶ��ٶȣ�����/��  
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
     * ��������Ļ�����¼���ʱ�����ȳ����˷�������ͨ���˷������������������ 
     * ��onTouch()�����У����ǵ���GestureDetector��onTouchEvent()������
     * ����׽����MotionEvent����GestureDetector �������Ƿ��к��ʵ�callback�����������û������� 
     */ 
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mGestureDetector.onTouchEvent(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			// �ж����±߽��Ƿ�Խ��
			View view = MyGallery.this.getSelectedView();
			if (view instanceof MyImageViewTouchBase) {
				mMyImageView = (MyImageViewTouchBase) view;
				float width = mMyImageView.getScale() * mMyImageView.getImageWidth();
				float height = mMyImageView.getScale() * mMyImageView.getImageHeight();
				if ((int) width <= PictureActivity.screenWidth && (int) height <= PictureActivity.screenHeight)// ���ͼƬ��ǰ��С<��Ļ��С���жϱ߽�
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
		// �����µĵڶ���Touch downʱ����
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
