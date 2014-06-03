package com.hb.activity.component;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hb.R;
import com.hb.activity.AnnouncementDetailActivity;
import com.hb.activity.AnnouncementListActivity;
import com.hb.activity.BaseActivity;
import com.hb.client.Constants;
import com.hb.client.HttpRequestType;
import com.hb.model.AnnouncementModel;
import com.hb.model.TimelineModel;
import com.hb.network.LKAsyncHttpResponseHandler;
import com.hb.network.LKHttpRequest;
import com.hb.network.LKHttpRequestQueue;
import com.hb.network.LKHttpRequestQueueDone;

public class SchoolNoticeRelativeLayout extends RelativeLayout {
	
	private Button noticeMoreButton = null;
	
	private LinearLayout contentLayout = null;
	private TextView tvNoticeTitle = null;
	private TextView tvNoticePreview = null;
	private TextView tvNoticeTime = null;
	
	private AnnouncementModel model = null;

	public SchoolNoticeRelativeLayout(Context context) {
		super(context);

		LayoutInflater.from(context).inflate(R.layout.layout_school_notice, this, true); 
        
        this.init();
	}

	private void init(){
		contentLayout = (LinearLayout) this.findViewById(R.id.rl_school_notice_content);
		contentLayout.setOnClickListener(new QueryAnnouncementDetailListener());
		
		tvNoticeTitle = (TextView)this.findViewById(R.id.tv_notice_title);
		tvNoticePreview = (TextView) this.findViewById(R.id.tv_notice_preview);
		tvNoticeTime = (TextView) this.findViewById(R.id.tv_notice_time);
		
		noticeMoreButton = (Button) this.findViewById(R.id.btn_school_notice_more);
		noticeMoreButton.setOnClickListener(new NoticeMoreListener());
	}
	
	public void refresh(AnnouncementModel model){
		this.model = model;
		
		this.setVisibility(View.VISIBLE);
		
		tvNoticeTitle.setText("    "+model.getTitle());
		tvNoticePreview.setText(model.getPreview());
		tvNoticeTime.setText(model.getTime());
	}
	
	class NoticeMoreListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			getAnnouncementList();
		}
	}
	
	class QueryAnnouncementDetailListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			/*
			Intent intent = new Intent(SchoolNoticeRelativeLayout.this.getContext(), AnnouncementDetailActivity.class);
			intent.putExtra("MODEL", model);
			SchoolNoticeRelativeLayout.this.getContext().startActivity(intent);
			**/
			
			getAnnouncementList();
		}
		
	}
	
	private void getAnnouncementList(){
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getNoticeListRequest());
		queue.executeQueue("正在查询公告...", new LKHttpRequestQueueDone());
		
	}
	
	// 查询公告列表
	private LKHttpRequest getNoticeListRequest(){
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("page", "1");
		paramMap.put("num", Constants.PAGESIZE + "");
		paramMap.put("previewLen", "200"); //预览长度，即取正文内容前几个字符，范围[0,200]，0为关闭预览
		
		LKHttpRequest request = new LKHttpRequest( HttpRequestType.HTTP_COLLEGE_BROADCAST_LIST, paramMap, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				HashMap<String, Object> map = (HashMap<String, Object>)obj;
				
				Intent intent = new Intent(SchoolNoticeRelativeLayout.this.getContext(), AnnouncementListActivity.class);
				intent.putExtra("TOTAL", (Integer)map.get("total"));
				intent.putExtra("LIST", (ArrayList<AnnouncementModel>)map.get("list"));
				SchoolNoticeRelativeLayout.this.getContext().startActivity(intent);
			}
		});
		
		return request;
	}

}
