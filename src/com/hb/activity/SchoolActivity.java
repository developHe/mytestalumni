package com.hb.activity;

import java.util.ArrayList;
import java.util.HashMap;

import weibo4j.Timeline;
import weibo4j.model.Paging;
import weibo4j.model.StatusWapper;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.hb.R;
import com.hb.activity.component.SchoolCardRelativeLayout;
import com.hb.activity.component.SchoolEventRelativeLayout;
import com.hb.activity.component.SchoolInfoCardRelativeLayout;
import com.hb.activity.component.SchoolNoticeRelativeLayout;
import com.hb.activity.component.SchoolWeiboRelativeLayout;
import com.hb.client.ApplicationEnvironment;
import com.hb.client.Constants;
import com.hb.client.HttpRequestType;
import com.hb.model.ActiveModel;
import com.hb.model.AnnouncementModel;
import com.hb.model.SchoolModel;
import com.hb.network.LKAsyncHttpResponseHandler;
import com.hb.network.LKHttpRequest;
import com.hb.network.LKHttpRequestQueue;
import com.hb.network.LKHttpRequestQueueDone;
import com.hb.util.WeiboUtil;

public class SchoolActivity extends AbsSubActivity {

	private SchoolInfoCardRelativeLayout rlSchoolInfo = null; // 学校信息
	private SchoolNoticeRelativeLayout rlSchoolNotice = null; // 官方公告
	private SchoolEventRelativeLayout rlSchoolEvent = null; // 官方活动
	private SchoolCardRelativeLayout rlSchoolCard = null; // 校友龙卡
	private SchoolWeiboRelativeLayout rlSchoolWeibo = null; // 官方微博

	private SchoolModel schoolModel = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_school);

		this.init();
	}

	private void init() {
		//RelativeLayout rlMain = (RelativeLayout) this.findViewById(R.id.rl_main);
		LinearLayout llSchoolContainer = (LinearLayout) this.findViewById(R.id.ll_main_school_container);

		// 学校信息
		rlSchoolInfo = new SchoolInfoCardRelativeLayout(this);
		llSchoolContainer.addView(rlSchoolInfo);

		// 官方公告
		rlSchoolNotice = new SchoolNoticeRelativeLayout(this);
		llSchoolContainer.addView(rlSchoolNotice);
		rlSchoolNotice.setVisibility(View.GONE);

		// 官方活动
		rlSchoolEvent = new SchoolEventRelativeLayout(this);
		llSchoolContainer.addView(rlSchoolEvent);
		rlSchoolEvent.setVisibility(View.GONE);

		// 校友龙卡
		rlSchoolCard = new SchoolCardRelativeLayout(this);
		llSchoolContainer.addView(rlSchoolCard);

		// 官方微博
		rlSchoolWeibo = new SchoolWeiboRelativeLayout(this);
		llSchoolContainer.addView(rlSchoolWeibo);
		rlSchoolWeibo.setVisibility(WeiboUtil.hasAuth() ? View.GONE : View.VISIBLE);

		refreshData();
	}

	// 刷新数据
	private void refreshData() {
		schoolModel = new SchoolModel();

		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getCollegeInfo());
		queue.addHttpRequest(getLastestAnnouncement());
		queue.addHttpRequest(getActiveTypeList());
		queue.addHttpRequest(getActiveList());
		queue.executeQueue("正在刷新数据...", new LKHttpRequestQueueDone());

		// 获取学校微博
		this.getSchoolWeibo();
	}

	// 获取母校信息
	private LKHttpRequest getCollegeInfo() {
		LKHttpRequest request = new LKHttpRequest(HttpRequestType.HTTP_COLLEGE_INTRODUCT, null, new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				schoolModel = (SchoolModel) obj;

				rlSchoolInfo.refresh(schoolModel);
			}
		});

		return request;
	}

	// 获取最新一条公告
	private LKHttpRequest getLastestAnnouncement() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("page", "1");
		paramMap.put("num", "1");
		paramMap.put("previewLen", "200"); // 预览长度，即取正文内容前几个字符，范围[0,200]，0为关闭预览

		LKHttpRequest request = new LKHttpRequest(HttpRequestType.HTTP_COLLEGE_BROADCAST_LIST, paramMap, new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> map = (HashMap<String, Object>) obj;
				int total = (Integer) map.get("total");
				if (total == 0) {
					rlSchoolNotice.setVisibility(View.GONE);
				} else {
					// 只取一条数据
					@SuppressWarnings("unchecked")
					ArrayList<AnnouncementModel> list = (ArrayList<AnnouncementModel>) map.get("list");
					AnnouncementModel model = list.get(0);
					rlSchoolNotice.refresh(model);
				}
			}
		});

		return request;
	}

	// 取得活动类型列表
	private LKHttpRequest getActiveTypeList() {
		LKHttpRequest request = new LKHttpRequest(HttpRequestType.HTTP_COLLEGE_EVENT_TYPE_LIST, null, new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> map = (HashMap<String, String>) obj;
				ActiveModel.setActiveTypeMap(map);
			}
		});

		return request;
	}

	// 取得活动列表
	private LKHttpRequest getActiveList() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("page", "1");
		paramMap.put("num", "2");
		paramMap.put("typeID", "0"); // 类型ID，用于返回特定类型的活动，0表示不限类型
		paramMap.put("previewLen", "200"); // 预览长度，即取正文内容前几个字符，范围[0,200]，0为关闭预览

		LKHttpRequest request = new LKHttpRequest(HttpRequestType.HTTP_COLLEGE_EVENT_LIST, paramMap, new LKAsyncHttpResponseHandler() {

			@Override
			public void successAction(Object obj) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> map = (HashMap<String, Object>) obj;
				int total = (Integer) map.get("total");
				if (total > 0) {
					rlSchoolEvent.setVisibility(View.VISIBLE);
					rlSchoolEvent.refresh((ArrayList<ActiveModel>) map.get("list"));

				} else {
					rlSchoolEvent.setVisibility(View.GONE);
				}
			}
		});

		return request;
	}

	// 取得微博信息
	private void getSchoolWeibo() {
		// 如果用户已经登录了新浪微博，则直接取得数据
		if (WeiboUtil.hasAuth()) {
			new GetSchoolTask().execute();
		}
	}

	// 异步取得微博数据
	class GetSchoolTask extends AsyncTask<Object, Object, Object> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Object doInBackground(Object... arg0) {
			try {
				Timeline timeline = new Timeline();
				Paging paging = new Paging();
				paging.count(3); // 在主页中只显示三条微博数据

				timeline.client.setToken(WeiboUtil.getToken());
				StatusWapper statusWapper = timeline.getUserTimelineByName(Constants.WEIBO_TIMELINE_SCREENNAME, paging, 0, 0);
				return statusWapper;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			try {
				rlSchoolWeibo.refresh(((StatusWapper) result).getStatuses());
			} catch (Exception e) {
				e.printStackTrace();

				rlSchoolWeibo.setVisibility(View.GONE);
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 认证微博成功
		if (requestCode == 100 && resultCode == RESULT_OK) {
			getSchoolWeibo();
		}
	}
	
	public void backAction() {
		ApplicationEnvironment.getInstance().exitApp();
	}


}
