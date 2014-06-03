package com.hb.activity;

import java.util.ArrayList;
import java.util.List;

import weibo4j.Timeline;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hb.R;
import com.hb.client.ApplicationEnvironment;
import com.hb.client.Constants;
import com.hb.client.HttpsUtil;
import com.hb.model.WeiboItemModel;
import com.hb.util.ListViewUtil;
import com.hb.util.WeiboUtil;
import com.hb.view.TimelineAdapter;

public class WeiboListActivity extends AbsSubActivity implements OnItemClickListener, OnClickListener {

	private static final int BASEAPP = 0;
	private static final int FEATURE = 0;
	private static final int pageSize = 10;

	private long totalCount = 0L;
	private long totalPage = 0L;
	private int currentPage = 1;

	private Button backButton = null;
	private Button moreButton = null;

	private ScrollView weiboScrollView = null;
	private WebView oauthWebview = null;
	private TextView titleView = null;

	private ListView timelineListView = null;
	private List<Status> timelineList = null;
	private TimelineAdapter timelineAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_weibo_list);

		this.init();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void init() {
		backButton = (Button) this.findViewById(R.id.backButton);
		backButton.setOnClickListener(this);
		
		moreButton = (Button) this.findViewById(R.id.moreButton);
		moreButton.setOnClickListener(this);

		weiboScrollView = (ScrollView) this.findViewById(R.id.weiboLayout);
		oauthWebview = (WebView) this.findViewById(R.id.oauth_webview);
		titleView = (TextView) this.findViewById(R.id.tv_title);

		timelineListView = (ListView) this.findViewById(R.id.lv_timeline);
		timelineListView.setOnItemClickListener(this);

		timelineList = new ArrayList<Status>();
		timelineAdapter = new TimelineAdapter(timelineList);
		timelineListView.setAdapter(timelineAdapter);

		if (WeiboUtil.hasAuth()) {
			oauthWebview.setVisibility(View.GONE);
			weiboScrollView.setVisibility(View.VISIBLE);
			titleView.setText("官方微博");

			new LoadTimelineTask().execute();

		} else {
			oauthWebview.setVisibility(View.VISIBLE);
			weiboScrollView.setVisibility(View.GONE);
			titleView.setText("用户登录");

			oauthWebview.getSettings().setJavaScriptEnabled(true);
			oauthWebview.setFocusable(true);
			oauthWebview.loadUrl(Constants.SINA_OAUTH);

			oauthWebview.setWebViewClient(new WebViewClient() {

				@Override
				public void onPageStarted(WebView view, String url, Bitmap favicon) {
					WeiboListActivity.this.showDialog(BaseActivity.PROGRESS_HUD, "正在加载...");

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

							if (accessToken == null || accessToken.trim().equals("")) {
								// WeiboListActivity.this.setResult(RESULT_CANCELED);

							} else {
								WeiboListActivity.this.showToast("微博认证成功");

								// 保存Access Token的值
								WeiboUtil.setToken(accessToken);

								// WeiboListActivity.this.setResult(RESULT_OK);
							}

							// gobackWithResult(RESULT_OK, getIntent());
							Intent intent = new Intent(WeiboListActivity.this, WeiboListActivity.class);
							WeiboListActivity.this.startActivityForResult(intent, 100);
						}
					}

					super.onPageStarted(view, url, favicon);
				}

				@Override
				public void onPageFinished(WebView view, String url) {
					super.onPageFinished(view, url);

					WeiboListActivity.this.hideDialog(BaseActivity.PROGRESS_HUD);
				}

				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					oauthWebview.loadUrl(url);

					return super.shouldOverrideUrlLoading(view, url);
				}

			});
		}
	}

	class LoadTimelineTask extends AsyncTask<Object, Object, Object> {

		@Override
		protected void onPreExecute() {
			WeiboListActivity.this.showDialog(BaseActivity.PROGRESS_HUD, "正在加载微博...");

			super.onPreExecute();
		}

		@Override
		protected Object doInBackground(Object... arg0) {
			doLoadTimeline();

			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			if (totalPage > 1) {
				moreButton.setVisibility(View.VISIBLE);
			}

			timelineAdapter.notifyDataSetChanged();

			ListViewUtil.setListViewHeightBasedOnChildren(timelineListView);

			WeiboListActivity.this.hideDialog(BaseActivity.PROGRESS_HUD);
		}

	}

	private void doLoadTimeline() {
		try {
			Timeline timeline = new Timeline();
			Paging paging = new Paging();
			paging.count(pageSize).setPage(currentPage++);

			timeline.client.setToken(WeiboUtil.getToken());

			StatusWapper statusWapper = timeline.getUserTimelineByName(Constants.WEIBO_TIMELINE_SCREENNAME, paging, BASEAPP, FEATURE);
			totalCount = statusWapper.getTotalNumber();
			totalPage = (totalCount + pageSize - 1) / pageSize;

			timelineList.addAll(statusWapper.getStatuses());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Status status = this.timelineList.get(arg2);

		Intent intent = new Intent(this, WeiboDetailActivity.class);
		intent.putExtra("status", new WeiboItemModel(status));
		this.startActivityForResult(intent, 1);

	}

	@Override
	public void onBackPressed() {
		this.goback();
		super.onBackPressed();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.backButton:
			WeiboListActivity.this.goback();
			break;

		case R.id.moreButton:
			if (currentPage < totalPage) {
				moreButton.setVisibility(View.VISIBLE);
			} else {
				moreButton.setVisibility(View.GONE);
			}

			new LoadTimelineTask().execute();

			break;
		}

	}

}
