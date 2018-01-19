package com.hjn.resume;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;

@SuppressWarnings("deprecation")
public class GalleryFlow extends Gallery {

    // ����Camera����
    private Camera mCamera = new Camera();
    // �����ת�Ƕ�
    private int mMaxRotateAngle = 120;
    // �������ֵ
    private int mMaxZoom = -420;
    private int mCoverflowCenter;

    public GalleryFlow(Context context) {
        super(context);
        setStaticTransformationsEnabled(true);
    }

    public GalleryFlow(Context context, AttributeSet attrs) {
        super(context, attrs);
        setStaticTransformationsEnabled(true);
    }

    public GalleryFlow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setStaticTransformationsEnabled(true);
    }

    // ���galleryչʾͼƬ�����ĵ�
    public int getCenterOfCoverflow() {
        return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2
                + getPaddingLeft();
    }

    // ���ͼƬ���ĵ�
    public int getCenterOfView(View view) {
        return view.getLeft() + view.getWidth() / 2;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mCoverflowCenter = getCenterOfCoverflow();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    // ����gallery��ÿ��ͼƬ����ת
    @Override
    protected boolean getChildStaticTransformation(View child, Transformation t) {
        // �õ�ͼƬ�����ĵ�
        final int childCenter = getCenterOfView(child);
        final int width = child.getLayoutParams().width;
        // ��ת�Ƕ�
        int rotationAngle = 0;
        //����ת��״̬
        t.clear();
         //����ת������
        t.setTransformationType(Transformation.TYPE_MATRIX);
        //���ͼƬλ������λ�ò���Ҫ������ת
        if (childCenter == mCoverflowCenter) {
            transformImageBitmap((ImageView) child, t, 0);
        } else {
            //����ͼƬ��gallery�е�λ��������ͼƬ����ת�Ƕ�
            rotationAngle = (int) ((float) (mCoverflowCenter - childCenter)
                    / width * mMaxRotateAngle);
            //�����ת�ǶȾ���ֵ���������ת�Ƕȷ��أ�-mMaxRotationAngle��mMaxRotationAngle;��
            if (Math.abs(rotationAngle) > mMaxRotateAngle) {
                rotationAngle = rotationAngle < 0 ? -mMaxRotateAngle
                        : mMaxRotateAngle;
            }
            transformImageBitmap((ImageView) child, t, rotationAngle);

        }
        return true;
    }

    // ͼƬ����
    private void transformImageBitmap(ImageView child, Transformation t,
            int rotateAngle) {
        // ����ͼ��任��Ч��
        mCamera.save();

        final Matrix matrix = t.getMatrix();
        // ͼƬ�߶�
        final int imageHeight = child.getLayoutParams().height;
        // ͼƬ���
        final int imageWidth = child.getLayoutParams().width;
        // ������ת�Ƕȵľ���ֵ
        final int rotation = Math.abs(rotateAngle);

        // ��Z���������ƶ�camera���ӽǣ�ʵ��Ч��Ϊ�Ŵ�ͼƬ��
        // �����Y�����ƶ�����ͼƬ�����ƶ���X���϶�ӦͼƬ�����ƶ���
        mCamera.translate(0.0f, 0.0f, 20.0f);

        if (rotation < mMaxRotateAngle) {
            float zoom = (float) ((rotation * 1.5) + mMaxZoom);
            mCamera.translate(0.0f, 0.0f, zoom);
            child.setAlpha((int) (255 - rotation * 2.5));
        }

        /// ��Y������ת����ӦͼƬ�������﷭ת��
        // �����X������ת�����ӦͼƬ�������﷭ת��
        mCamera.rotateY(rotateAngle);
        mCamera.getMatrix(matrix);
        matrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));
        matrix.postTranslate((imageWidth / 2), (imageHeight / 2));

        // ��ԭ
        mCamera.restore();

    }

}
