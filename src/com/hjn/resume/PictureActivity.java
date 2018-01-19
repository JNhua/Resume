package com.hjn.resume;

import com.hjn.resume.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;


/**
 * ��㴥�صķŴ���С
 * 
 */
@SuppressWarnings("deprecation")
public class PictureActivity extends Activity implements OnTouchListener {
	// ��Ļ���
	public static int screenWidth;
	// ��Ļ�߶�
	public static int screenHeight;
	private MyGallery gallery;

	private float beforeLenght = 0.0f; // ���������
	private float afterLenght = 0.0f; // ���������
	private boolean isScale = false;// �Ƿ�����
	private float currentScale = 1.0f;// ��ǰͼƬ�����ű���

	private int _position = 0;// ��ǰΪ��һ��
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				 WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.picture_introduce);
		
		
        gallery = (MyGallery) findViewById(R.id.gallery);
		gallery.setVerticalFadingEdgeEnabled(false);// ȡ����ֱ����߿�
		gallery.setHorizontalFadingEdgeEnabled(false);// ȡ��ˮƽ����߿�
		gallery.setAdapter(new GalleryAdapter(this));
		gallery.setSelection(_position);// ��ʼѡ�е��ǵ�һ��

		screenWidth = getWindow().getWindowManager().getDefaultDisplay()
				.getWidth();
		screenHeight = getWindow().getWindowManager().getDefaultDisplay()
				.getHeight();
	}
  

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_POINTER_DOWN:// �������
			beforeLenght = spacing(event);
			if (beforeLenght > 5f) {
				isScale = true;
			}
			break;
		case MotionEvent.ACTION_MOVE:// �����ƶ�
			if (isScale) {
				afterLenght = spacing(event);
				if (afterLenght < 5f)
					break;
				float gapLenght = afterLenght - beforeLenght;
				if (gapLenght == 0) {
					break;
				} else if (Math.abs(gapLenght) > 5f) {
					float scaleRate = gapLenght / 854;// ���ű���
					Animation myAnimation_Scale = new ScaleAnimation(
							currentScale, currentScale + scaleRate,
							currentScale, currentScale + scaleRate,
							Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f);
					myAnimation_Scale.setDuration(100);
					myAnimation_Scale.setFillAfter(true);
					myAnimation_Scale.setFillEnabled(true);
					currentScale = currentScale + scaleRate;
					gallery.getSelectedView().setLayoutParams(
							new Gallery.LayoutParams(
									(int) (480 * (currentScale)),
									(int) (854 * (currentScale))));
					beforeLenght = afterLenght;
				}
				return true;
			}
			break;
		case MotionEvent.ACTION_POINTER_UP:
			isScale = false;
			break;
		}

		return false;
	}

	/**
	 * ���������ľ���
	 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float) Math.sqrt(x * x + y * y);
	}
	
}