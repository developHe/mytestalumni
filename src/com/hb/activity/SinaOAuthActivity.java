package com.hb.activity;

import com.hb.R;
import com.hb.client.ApplicationEnvironment;
import com.hb.client.Constants;
import com.hb.client.HttpsUtil;
import com.hb.util.WeiboUtil;

import android.annotation.SuppressLint;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SinaOAuthActivity extends AbsSubActivity {
	
	public WebView webview;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sina_oauth_webview);

		webview = (WebView) this.findViewById(R.id.oauth_webview);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.setFocusable(true);
		webview.loadUrl(Constants.SINA_OAUTH);

		webview.setWebViewClient(new WebViewClient() {
			
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				SinaOAuthActivity.this.showDialog(BaseActivity.PROGRESS_HUD, "正在加载...");
				
				if (url.startsWith(Constants.SINA_WEIBO_CALLBACK_URL)) {
					// 取消授权后的界面
					view.cancelLongPress();
					view.stopLoading();

					// 获取Code
					Uri uri = Uri.parse(url);
					String code = uri.getQueryParameter("code");

					String result = "";
					if (code != null) {
						result = HttpsUtil.HttpsPost(Constants.SINA_ACCESS_TOKEN + code, "");
					}
					
					if (result.startsWith("{\"access_token\":")) {
						int i = result.indexOf(":");
						int j = result.indexOf(",");
						String accessToken = result.substring(i + 2, j - 1);
						Log.e("ACCESS_TOKEN", accessToken);
						
						if (accessToken == null || accessToken.trim().equals("")){
							SinaOAuthActivity.this.setResult(RESULT_CANCELED);
							
						} else {
							SinaOAuthActivity.this.showToast("微博认证成功");
							
							// 保存Access Token的值
							WeiboUtil.setToken(accessToken);
							
							SinaOAuthActivity.this.setResult(RESULT_OK);
						}
						
						gobackWithResult(RESULT_OK, getIntent());
					}
				}
				
				super.onPageStarted(view, url, favicon);
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				
				SinaOAuthActivity.this.hideDialog(BaseActivity.PROGRESS_HUD);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				webview.loadUrl(url);
				
				return super.shouldOverrideUrlLoading(view, url);
			}

		});
	}
}
