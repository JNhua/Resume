package com.hjn.resume;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.ImageView;
/**
 * ImageViewTouchBase
 * ������һ��������,�̳���imageView,ʵ���˷Ŵ���Сzoom,ƽ��pan�ȷ���.
 * ��Ҫ�õ�Matrix��.
 *
 */
public class MyImageViewTouchBase extends ImageView {
	@SuppressWarnings("unused")
	private static final String TAG = "ImageViewTouchBase";

	protected Matrix mBaseMatrix = new Matrix();//������ʾԭʼ��image�Ļ����任����

	protected Matrix mSuppMatrix = new Matrix();//�洢�û���ͼ�β���������ƽ�Ʊ任����ӵľ���

	private final Matrix mDisplayMatrix = new Matrix();//����������϶���Ľ���������������վ���

	private final float[] mMatrixValues = new float[9];

	protected Bitmap image = null;

	private int mThisWidth = -1, mThisHeight = -1;

	private float mMaxZoom = 2.0f;// ������ű���
	private float mMinZoom;// ��С���ű���

	private int imageWidth;// ͼƬ��ԭʼ���
	private int imageHeight;// ͼƬ��ԭʼ�߶�

	private float scaleRate;// ͼƬ��Ӧ��Ļ�����ű���

	public MyImageViewTouchBase(Context context, int imageWidth, int imageHeight) {
		super(context);
		this.imageHeight = imageHeight;
		this.imageWidth = imageWidth;
		init();
	}

	public MyImageViewTouchBase(Context context, AttributeSet attrs,
			int imageWidth, int imageHeight) {
		super(context, attrs);
		this.imageHeight = imageHeight;
		this.imageWidth = imageWidth;
		init();
	}

	/**
	 * ����ͼƬҪ��Ӧ��Ļ��Ҫ���ŵı���
	 */
	private void arithScaleRate() {
		float scaleWidth = PictureActivity.screenWidth / (float) imageWidth;
		float scaleHeight = PictureActivity.screenHeight / (float) imageHeight;
		scaleRate = Math.min(scaleWidth, scaleHeight);
	}

	public float getScaleRate() {
		return scaleRate;
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			event.startTracking();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking()
				&& !event.isCanceled()) {
			if (getScale() > 1.0f) {
				zoomTo(1.0f);
				return true;
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	protected Handler mHandler = new Handler();

	@Override
	public void setImageBitmap(Bitmap bitmap) {
		super.setImageBitmap(bitmap);
		image = bitmap;
		// ������Ӧ��Ļ�ı���
		arithScaleRate();
		// ���ŵ���Ļ��С
		zoomTo(scaleRate, PictureActivity.screenWidth / 2f,
				PictureActivity.screenHeight / 2f);

		// ����
		layoutToCenter();
	}
	protected void center(boolean horizontal, boolean vertical) {
		if (image == null) {
			return;
		}

		Matrix m = getImageViewMatrix();

		RectF rect = new RectF(0, 0, image.getWidth(), image.getHeight());
		m.mapRect(rect);

		float height = rect.height();
		float width = rect.width();

		float deltaX = 0, deltaY = 0;

		if (vertical) {
			int viewHeight = getHeight();
			if (height < viewHeight) {
				deltaY = (viewHeight - height) / 2 - rect.top;
			} else if (rect.top > 0) {
				deltaY = -rect.top;
			} else if (rect.bottom < viewHeight) {
				deltaY = getHeight() - rect.bottom;
			}
		}

		if (horizontal) {
			int viewWidth = getWidth();
			if (width < viewWidth) {
				deltaX = (viewWidth - width) / 2 - rect.left;
			} else if (rect.left > 0) {
				deltaX = -rect.left;
			} else if (rect.right < viewWidth) {
				deltaX = viewWidth - rect.right;
			}
		}

		postTranslate(deltaX, deltaY);
		setImageMatrix(getImageViewMatrix());
	}

	private void init() {
		setScaleType(ImageView.ScaleType.MATRIX);
	}

	/**
	 * ����ͼƬ������ʾ
	 */
	public void layoutToCenter() {
		// ������ʾ��ͼƬʵ�ʿ��
		float width = imageWidth * getScale();
		float height = imageHeight * getScale();

		// �հ�������
		float fill_width = PictureActivity.screenWidth - width;
		float fill_height = PictureActivity.screenHeight - height;

		// ��Ҫ�ƶ��ľ���
		float tran_width = 0f;
		float tran_height = 0f;

		if (fill_width > 0)
			tran_width = fill_width / 2;
		if (fill_height > 0)
			tran_height = fill_height / 2;

		postTranslate(tran_width, tran_height);
		setImageMatrix(getImageViewMatrix());
	}

	protected float getValue(Matrix matrix, int whichValue) {
		matrix.getValues(mMatrixValues);
		mMinZoom = (PictureActivity.screenWidth / 2f) / imageWidth;

		return mMatrixValues[whichValue];
	}

	// Get the scale factor out of the matrix.
	protected float getScale(Matrix matrix) {
		return getValue(matrix, Matrix.MSCALE_X);
	}

	protected float getScale() {
		return getScale(mSuppMatrix);
	}

	protected Matrix getImageViewMatrix() {
		mDisplayMatrix.set(mBaseMatrix);
		mDisplayMatrix.postConcat(mSuppMatrix);
		return mDisplayMatrix;
	}

	static final float SCALE_RATE = 1.25F;

	
	protected float maxZoom() {
		if (image == null) {
			return 1F;
		}

		float fw = (float) image.getWidth() / (float) mThisWidth;
		float fh = (float) image.getHeight() / (float) mThisHeight;
		float max = Math.max(fw, fh) * 4;
		return max;
	}

	protected void zoomTo(float scale, float centerX, float centerY) {
		if (scale > mMaxZoom) {
			scale = mMaxZoom;
		} else if (scale < mMinZoom) {
			scale = mMinZoom;
		}

		float oldScale = getScale();
		float deltaScale = scale / oldScale;

		mSuppMatrix.postScale(deltaScale, deltaScale, centerX, centerY);
		setImageMatrix(getImageViewMatrix());
		center(true, true);
	}

	protected void zoomTo(final float scale, final float centerX,
			final float centerY, final float durationMs) {
		final float incrementPerMs = (scale - getScale()) / durationMs;
		final float oldScale = getScale();
		final long startTime = System.currentTimeMillis();

		mHandler.post(new Runnable() {
			public void run() {
				long now = System.currentTimeMillis();
				float currentMs = Math.min(durationMs, now - startTime);
				float target = oldScale + (incrementPerMs * currentMs);
				zoomTo(target, centerX, centerY);
				if (currentMs < durationMs) {
					mHandler.post(this);
				}
			}
		});
	}

	protected void zoomTo(float scale) {
		float cx = getWidth() / 2F;
		float cy = getHeight() / 2F;

		zoomTo(scale, cx, cy);
	}

	protected void zoomToPoint(float scale, float pointX, float pointY) {
		float cx = getWidth() / 2F;
		float cy = getHeight() / 2F;

		panBy(cx - pointX, cy - pointY);
		zoomTo(scale, cx, cy);
	}

	protected void zoomIn() {
		zoomIn(SCALE_RATE);
	}

	protected void zoomOut() {
		zoomOut(SCALE_RATE);
	}

	protected void zoomIn(float rate) {
		if (getScale() >= mMaxZoom) {
			return; // Don't let the user zoom into the molecular level.
		} else if (getScale() <= mMinZoom) {
			return;
		}
		if (image == null) {
			return;
		}

		float cx = getWidth() / 2F;
		float cy = getHeight() / 2F;

		mSuppMatrix.postScale(rate, rate, cx, cy);
		setImageMatrix(getImageViewMatrix());
	}

	protected void zoomOut(float rate) {
		if (image == null) {
			return;
		}

		float cx = getWidth() / 2F;
		float cy = getHeight() / 2F;

		// Zoom out to at most 1x.
		Matrix tmp = new Matrix(mSuppMatrix);
		tmp.postScale(1F / rate, 1F / rate, cx, cy);

		if (getScale(tmp) < 1F) {
			mSuppMatrix.setScale(1F, 1F, cx, cy);
		} else {
			mSuppMatrix.postScale(1F / rate, 1F / rate, cx, cy);
		}
		setImageMatrix(getImageViewMatrix());
		center(true, true);
	}

	public void postTranslate(float dx, float dy) {
		mSuppMatrix.postTranslate(dx, dy);
		setImageMatrix(getImageViewMatrix());
	}

	float _dy = 0.0f;

	protected void postTranslateDur(final float dy, final float durationMs) {
		_dy = 0.0f;
		final float incrementPerMs = dy / durationMs;
		final long startTime = System.currentTimeMillis();
		mHandler.post(new Runnable() {
			public void run() {
				long now = System.currentTimeMillis();
				float currentMs = Math.min(durationMs, now - startTime);

				postTranslate(0, incrementPerMs * currentMs - _dy);
				_dy = incrementPerMs * currentMs;

				if (currentMs < durationMs) {
					mHandler.post(this);
				}
			}
		});
	}

	protected void panBy(float dx, float dy) {
		postTranslate(dx, dy);
		setImageMatrix(getImageViewMatrix());
	}
}
