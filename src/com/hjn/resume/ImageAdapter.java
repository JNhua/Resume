package com.hjn.resume;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private int[] mImageIds;
    private ImageView[] mImages;

    public ImageAdapter(Context context, int[] imageIds) {
        this.mContext = context;
        this.mImageIds = imageIds;
        mImages = new ImageView[imageIds.length];
    }

    public int getCount() {
        return mImages.length;
    }

    public Object getItem(int position) {
        return mImages[position];
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return mImages[position];
    }

    // ���ɴ��е�ӰЧ����ͼƬ
    public boolean createReflectedImages() {
        // ԭͼƬ�뵹Ӱ֮��ľ���
        final int reflectionGap = 4;
        int index = 0;
        for (int imageId : mImageIds) {
            // ԭͼƬ
            Bitmap originalImage = BitmapFactory.decodeResource(
                    mContext.getResources(), imageId);

            int width = originalImage.getWidth();
            int height = originalImage.getHeight();

            // �����������
            Matrix matrix = new Matrix();
            matrix.preScale(1, -1);
            // ������Ӧ�õ���ԭͼ֮�У�����һ����Ȳ��䣬�߶�Ϊԭͼ1/2�ĵ�Ӱλͼ
            Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
                    height / 2, width, height / 2, matrix, false);

            // ����һ����Ȳ��䣬�߶�Ϊԭͼ+��Ӱͼ�߶ȵ�λͼ
            Bitmap bitmapWithReflection = Bitmap.createBitmap(width, height
                    + height / 2, Config.ARGB_8888);
            // ��������
            Canvas canvas = new Canvas(bitmapWithReflection);
            // ����ԭͼƬ
            canvas.drawBitmap(originalImage, 0, 0, null);
            // ����ԭͼƬ�뵹Ӱ֮��ľ���
            Paint defaultpaint = new Paint();
            canvas.drawRect(0, height, width, height + reflectionGap,
                    defaultpaint);
            // ���Ƶ�ӰͼƬ
            canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

            Paint paint = new Paint();
            paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
            LinearGradient shader = new LinearGradient(0, height, 0,
                    bitmapWithReflection.getHeight() + reflectionGap,
                    0x70FFFFFF, 0x00FFFFFF, TileMode.MIRROR);
            // ��ɫ�� ����������ɫ ��ɫ��
            paint.setShader(shader);
            canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
                    + reflectionGap, paint);

            // ����һ��ImageView������ʾ�Ѿ����õ�bitmapWithReflection
            ImageView imageView = new ImageView(mContext);
            BitmapDrawable bd = new BitmapDrawable(bitmapWithReflection);
            // ����ͼƬ���Ч��
            bd.setAntiAlias(true);
            imageView.setImageDrawable(bd);
            // ����ͼƬ�Ĵ�С
            imageView.setLayoutParams(new GalleryFlow.LayoutParams(160, 240));
            mImages[index++] = imageView;
        }
        return true;
    }
}
