package com.hb.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.hb.R;

@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
public class ShowWebViewActivity extends AbsSubActivity implements OnClickListener {
	
	private WebView mWebView;
	private Button backButton;
	private TextView titleView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_webcontent);
		
		Intent intent = this.getIntent();
		String title = intent.getStringExtra("TITLE");
		String URL = intent.getStringExtra("URL");
		
		backButton = (Button) this.findViewById(R.id.btn_back);
		backButton.setOnClickListener(this);
		
		titleView = (TextView) this.findViewById(R.id.tv_title);
		titleView.setText(title == null ? this.getResources().getString(R.string.app_name) : title);
		
		mWebView = (WebView)this.findViewById(R.id.webview);
		mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
//        this.setFitZoom();
        // http://blog.csdn.net/to_cm/article/details/7801918
        
        if (null != URL){
        	
            mWebView.setWebViewClient(new WebViewClient(){
            	@Override
    			public void onPageStarted(WebView view, String url, Bitmap favicon) {
    				super.onPageStarted(view, url, favicon);
    				ShowWebViewActivity.this.showDialog(BaseActivity.PROGRESS_HUD, "正在加载，请稍候...");
    			}

    			@Override
    			public void onPageFinished(WebView view, String url) {
    				super.onPageFinished(view, url);
    				ShowWebViewActivity.this.hideDialog(BaseActivity.PROGRESS_HUD);
    			}

    			// 重写此方法返回true表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边。
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					Log.e("url",url);
					if (url.endsWith("returnPageIndex")){
						ShowWebViewActivity.this.goback();
						return true;
					}
					
					mWebView.loadUrl(url);
					return true;
				}

            });
            
            
            mWebView.loadUrl(URL);
        }
        
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.btn_back:
			this.goback();
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		this.goback();
		super.onBackPressed();
	}

	private void setFitZoom(){
		//Enum for specifying the WebView's desired density. FAR makes 100% looking like 
        //in 240dpi MEDIUM makes 100% looking like in 160dpi CLOSE makes 100% looking like in 120dpi
		int screenDensity = getResources().getDisplayMetrics().densityDpi;
		WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM;
		switch(screenDensity){
		case DisplayMetrics.DENSITY_LOW:
		    zoomDensity = WebSettings.ZoomDensity.CLOSE;
		    break;
		case DisplayMetrics.DENSITY_MEDIUM:
		    zoomDensity = WebSettings.ZoomDensity.MEDIUM;
		    break;
		case DisplayMetrics.DENSITY_HIGH:
		    zoomDensity = WebSettings.ZoomDensity.FAR;
		    break;
		}
		
		mWebView.getSettings().setDefaultZoom(zoomDensity);
	}

}
