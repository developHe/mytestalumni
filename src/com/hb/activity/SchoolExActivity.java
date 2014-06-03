package com.hb.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.hb.R;
import com.hb.activity.component.SchoolFeedbackRelativeLayout;
import com.hb.activity.component.SchoolInfoCardRelativeLayout;
import com.hb.activity.component.SchoolMediaRelativeLayout;
import com.hb.activity.component.SchoolPhotoWallRelativeLayout;
import com.hb.client.ApplicationEnvironment;
import com.hb.client.Constants;
import com.hb.client.HttpRequestType;
import com.hb.model.MediaModel;
import com.hb.model.SchoolModel;
import com.hb.network.LKAsyncHttpResponseHandler;
import com.hb.network.LKHttpRequest;
import com.hb.network.LKHttpRequestQueue;
import com.hb.network.LKHttpRequestQueueDone;

public class SchoolExActivity extends AbsSubActivity {

	private SchoolMediaRelativeLayout rlSchoolDynamic = null; // 母校动态
	private SchoolFeedbackRelativeLayout rlSchoolFeedback = null; // 校友捐赠
	private SchoolPhotoWallRelativeLayout rlSchoolPhotoWall = null; // 印象首师
	private SchoolInfoCardRelativeLayout rlSchoolInfo = null; // 数据母校

	private SchoolModel schoolModel = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_school_ex);

		this.init();
	}

	private void init() {
		LinearLayout llSchoolContainer = (LinearLayout) this.findViewById(R.id.ll_main_school_container);

		// 母校动态
		rlSchoolDynamic = new SchoolMediaRelativeLayout(this, "母校动态", new MoreMediaListener(3));
		llSchoolContainer.addView(rlSchoolDynamic);
		rlSchoolDynamic.setVisibility(View.GONE);

		// 印象首师
		rlSchoolPhotoWall = new SchoolPhotoWallRelativeLayout(this);
		llSchoolContainer.addView(rlSchoolPhotoWall);
		
		// 校友捐赠
		rlSchoolFeedback = new SchoolFeedbackRelativeLayout(this);
		llSchoolContainer.addView(rlSchoolFeedback);

		// 学校信息
		rlSchoolInfo = new SchoolInfoCardRelativeLayout(this);
		llSchoolContainer.addView(rlSchoolInfo);

		refreshData();
	}

	// 刷新数据
	private void refreshData() {
		schoolModel = new SchoolModel();

		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getCollegeInfoRequest());
		queue.addHttpRequest(getSchoolMediaRequest(3)); // 母校动态
		queue.executeQueue("正在刷新数据...", new LKHttpRequestQueueDone());

	}

	// 获取母校信息
	private LKHttpRequest getCollegeInfoRequest() {
		LKHttpRequest request = new LKHttpRequest(HttpRequestType.HTTP_COLLEGE_INTRODUCT, null, new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				schoolModel = (SchoolModel) obj;

				rlSchoolInfo.refresh(schoolModel);
			}
		});

		return request;
	}

	private LKHttpRequest getSchoolMediaRequest(final int type) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
		map.put("page", "1");
		map.put("num", "3");
		map.put("previewLen", "200");

		LKHttpRequest request = new LKHttpRequest(HttpRequestType.HTTP_MEDIA_LIST, map, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				HashMap<String, Object> map = (HashMap<String, Object>) obj;
				int total = Integer.parseInt((String) map.get("total"));
				if (total == 0) {
					if (type == 3) {
						rlSchoolDynamic.setVisibility(View.GONE);
					}

				} else {
					if (type == 3) {
						rlSchoolDynamic.setVisibility(View.VISIBLE);
						rlSchoolDynamic.refresh((ArrayList<MediaModel>) map.get("list"));
					}
				}

			}
		});

		return request;
	}

	public void backAction() {
		ApplicationEnvironment.getInstance().exitApp();
	}

	class MoreMediaListener implements OnClickListener {

		private int type;

		private ArrayList<MediaModel> topList = null;
		private ArrayList<MediaModel> list = null;
		private int total = 0;

		public MoreMediaListener(int type) {
			this.type = type;
		}

		@Override
		public void onClick(View v) {
			LKHttpRequestQueue queue = new LKHttpRequestQueue();
			queue.addHttpRequest(this.getMediaTopListRequest());
			queue.addHttpRequest(this.getMediaListRequest());
			queue.executeQueue("正在查询请稍候...", new LKHttpRequestQueueDone() {
				public void onComplete() {
					Intent intent = new Intent(SchoolExActivity.this, SchoolMediaListActivity.class);
					intent.putExtra("TOPLIST", topList);
					intent.putExtra("LIST", list);
					intent.putExtra("TOTAL", total);
					SchoolExActivity.this.startActivityForResult(intent, 0);
				}
			});
		}

		private LKHttpRequest getMediaTopListRequest() {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("type", this.type);

			return new LKHttpRequest(HttpRequestType.HTTP_MEDIA_TOPLIST, map, new LKAsyncHttpResponseHandler() {

				@SuppressWarnings("unchecked")
				@Override
				public void successAction(Object obj) {
					topList = (ArrayList<MediaModel>) obj;
				}
			});

		}

		private LKHttpRequest getMediaListRequest() {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("type", this.type);
			map.put("page", "1");
			map.put("num", Constants.PAGESIZE);
			map.put("previewLen", "200");

			return new LKHttpRequest(HttpRequestType.HTTP_MEDIA_LIST, map, new LKAsyncHttpResponseHandler() {

				@SuppressWarnings("unchecked")
				@Override
				public void successAction(Object obj) {
					HashMap<String, Object> map = (HashMap<String, Object>) obj;
					list = (ArrayList<MediaModel>) map.get("list");
					total = Integer.parseInt((String) map.get("total"));
				}
			});
		}
	}

}
