package com.hb.activity.component;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hb.R;
import com.hb.activity.AnnouncementListActivity;
import com.hb.activity.SchoolEventListActivity;
import com.hb.client.HttpRequestType;
import com.hb.model.ActiveModel;
import com.hb.model.AnnouncementModel;
import com.hb.network.LKAsyncHttpResponseHandler;
import com.hb.network.LKHttpRequest;
import com.hb.network.LKHttpRequestQueue;
import com.hb.network.LKHttpRequestQueueDone;

public class SchoolEventRelativeLayout extends RelativeLayout {

	private LinearLayout bodyLayout;
	private Button eventMoreButton;
	private Context context;

	public SchoolEventRelativeLayout(Context context) {
		super(context);
		this.context = context;

		this.init();
	}

	private void init() {
		LayoutInflater.from(context).inflate(R.layout.layout_school_event, this, true);
		bodyLayout = (LinearLayout) this.findViewById(R.id.bodyLayout);
		eventMoreButton = (Button) this.findViewById(R.id.btn_school_event_more);
		eventMoreButton.setOnClickListener(new EventMoreOnClickListener());
	}

	public void refresh(ArrayList<ActiveModel> list) {
		this.setVisibility(View.VISIBLE);

		for (int i = 0; i < list.size(); i++) {

			ActiveItemLinearLayout layout = new ActiveItemLinearLayout(this.context, list.get(i));

			// 最后一项隐藏分隔线
			if (i == list.size() - 1) {
				layout.hideDivider();
			}

			layout.setPadding(0, 0, 0, 0);

			bodyLayout.addView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

		}
	}

	class EventMoreOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			getEventList();
		}
	}

	public void getEventList() {
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getEventListRequest());
		queue.executeQueue("正在查询活动...", new LKHttpRequestQueueDone());
	}

	// 取得活动列表
	private LKHttpRequest getEventListRequest() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("page", "1");
		paramMap.put("num", "10");
		paramMap.put("typeID", "0"); // 类型ID，用于返回特定类型的活动，0表示不限类型
		paramMap.put("previewLen", "200"); // 预览长度，即取正文内容前几个字符，范围[0,200]，0为关闭预览

		LKHttpRequest request = new LKHttpRequest(HttpRequestType.HTTP_COLLEGE_EVENT_LIST, paramMap, new LKAsyncHttpResponseHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				HashMap<String, Object> map = (HashMap<String, Object>) obj;

				Intent intent = new Intent(SchoolEventRelativeLayout.this.getContext(), SchoolEventListActivity.class);
				intent.putExtra("TOTAL", (Integer) map.get("total"));
				intent.putExtra("LIST", (ArrayList<AnnouncementModel>) map.get("list"));
				SchoolEventRelativeLayout.this.getContext().startActivity(intent);
			}
		});

		return request;
	}
}
