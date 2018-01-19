package com.hjn.resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.hjn.resume.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;



public class Web_microblog extends Activity{
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.webview);  
        byte[]buffer=new byte[1024];
        InputStream is=getResources().openRawResource(R.raw.weibo);
        WebView webView = (WebView) findViewById(R.id.html);
        webView.getSettings().setJavaScriptEnabled(true);
		try {
			int count=is.read(buffer);
			String html = new String(buffer,0,count,"utf-8");
			webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
