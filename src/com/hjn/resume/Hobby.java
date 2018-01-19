package com.hjn.resume;

import com.hjn.resume.R;

import android.app.Activity;
import android.os.Bundle;


public class Hobby extends Activity {
    private GalleryFlow mGalleryFlow;

    private int[] imageIds = new int[] { R.drawable.h1, R.drawable.h2,
            R.drawable.h3, R.drawable.h4, R.drawable.h5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hobby);
       
        findViews();
        initViews();
    }

    private void findViews() {
        mGalleryFlow = (GalleryFlow) findViewById(R.id.gf);
    }

    private void initViews() {
        ImageAdapter adapter = new ImageAdapter(this, imageIds);
        // 生成带有倒影效果的图片
        adapter.createReflectedImages();
        mGalleryFlow.setAdapter(adapter);
    }

}
