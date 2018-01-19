package com.hjn.resume;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.widget.Toast;

public class Shake extends Activity {
	 private SensorManager sensorManager; 
	    private Vibrator vibrator; 
	    private int pic_num=0;
	    private static final int SENSOR_SHAKE = 10; 
	 
	    /** Called when the activity is first created. */ 
	    @Override 
	    public void onCreate(Bundle savedInstanceState) { 
	        super.onCreate(savedInstanceState); 
	        setContentView(R.layout.shake); 
	        
	        Resources res = getResources();
	    	  Drawable drawable = res.getDrawable(R.drawable.shake);
	    	  Shake.this.getWindow().setBackgroundDrawable(drawable);
	    	  Toast.makeText(Shake.this, "上下摇一摇换图片~！", Toast.LENGTH_SHORT).show();
	    	  
	        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE); 
	        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE); 
	    } 
	 
	    @Override 
	    protected void onResume() { 
	        super.onResume(); 
	        if (sensorManager != null) {// 注册监听器 
	            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME); 
	            // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率 
	        } 
	    } 
	 
	    @Override 
	    protected void onStop() { 
	        super.onStop(); 
	        if (sensorManager != null) {// 取消监听器 
	            sensorManager.unregisterListener(sensorEventListener); 
	        } 
	    } 
	 
	    /**
	     * 重力感应监听
	     */ 
	    private SensorEventListener sensorEventListener = new SensorEventListener() { 
	 
	        @Override 
	        public void onSensorChanged(SensorEvent event) { 
	            // 传感器信息改变时执行该方法 
	            float[] values = event.values; 
	            float x = values[0]; // x轴方向的重力加速度，向右为正 
	            float y = values[1]; // y轴方向的重力加速度，向前为正 
	            float z = values[2]; // z轴方向的重力加速度，向上为正 
	           
	            
	            int medumValue = 18;
	            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) { 
	                vibrator.vibrate(200); 
	                Message msg = new Message(); 
	                msg.what = SENSOR_SHAKE; 
	                handler.sendMessage(msg); 
	            } 
	        } 
	 
	        @Override 
	        public void onAccuracyChanged(Sensor sensor, int accuracy) { 
	 
	        } 
	    }; 
	 
	    /**
	     * 动作执行
	     */ 
	    Handler handler = new Handler() { 
	 
	        @Override 
	        public void handleMessage(Message msg) { 
	            super.handleMessage(msg); 
	            switch (msg.what) { 
	            case SENSOR_SHAKE:
	            	if (pic_num%3==0){
	            		Resources res2 = getResources();
		            	  Drawable drawable = res2.getDrawable(R.drawable.shake1);
		            	  Shake.this.getWindow().setBackgroundDrawable(drawable);
		            	 
		            	  pic_num=pic_num+1;
	            	}
	            	else if (pic_num%3==1){
	            		Resources res2 = getResources();
		            	  Drawable drawable = res2.getDrawable(R.drawable.shake2);
		            	  Shake.this.getWindow().setBackgroundDrawable(drawable);
		            	  
		            	  pic_num=pic_num+1;
	            	}
	            	else if (pic_num%3==2){
	            		Resources res2 = getResources();
		            	  Drawable drawable = res2.getDrawable(R.drawable.shake3);
		            	  Shake.this.getWindow().setBackgroundDrawable(drawable);
		            	 
		            	  pic_num=pic_num+1;
	            	}
	            	
	                break; 
	            } 
	        } 
	 
	    }; 

}
