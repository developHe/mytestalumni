package com.hb.activity.component;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.R;
import com.hb.activity.BaseActivity;
import com.hb.activity.SchoolEventActivity;
import com.hb.client.Constants;
import com.hb.client.HttpRequestType;
import com.hb.model.ActiveModel;
import com.hb.model.ProfileModel;
import com.hb.network.LKAsyncHttpResponseHandler;
import com.hb.network.LKHttpRequest;
import com.hb.network.LKHttpRequestQueue;
import com.hb.network.LKHttpRequestQueueDone;
import com.hb.util.ImageUtil;

public class ActiveItemLinearLayout extends LinearLayout implements OnClickListener {
	
	private Context context;
	
	private ActiveModel active;  // 列表使用的model
	
	private ActiveModel active2; // 详情使用的model
	private ArrayList<ProfileModel> profileList;
	
	private LinearLayout rootLayout;
	private ImageView posterImage;
	private TextView titleView;
	private TextView previewView;
	private TextView timeView;
	private TextView addressView;
	private TextView typeView;
	private TextView chargeView;
	private TextView sponsorView;

	public ActiveItemLinearLayout(Context context) {
		super(context);
		this.context = context;
		
		this.init();
	}

	public ActiveItemLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		
		this.init();
	}
	
	public ActiveItemLinearLayout(Context context, ActiveModel active){
		super(context);
		this.context = context;
		this.active = active;
		
		this.init();
	}
	
	private void init(){
		LayoutInflater.from(context).inflate(R.layout.layout_event_card, this, true); 
		
		rootLayout = (LinearLayout) this.findViewById(R.id.rootLayout);
		rootLayout.setOnClickListener(this);
		
		posterImage = (ImageView) this.findViewById(R.id.iv_event_poster);
		ImageUtil.loadImage(R.drawable.img_weibo_item_pic_loading, this.active.getPosterImage(), posterImage);
		
		titleView = (TextView) this.findViewById(R.id.tv_event_title);
		titleView.setText(this.active.getTitle());
		
		previewView = (TextView) this.findViewById(R.id.tv_event_preview);
		previewView.setText(this.active.getPreview());
		
		timeView = (TextView) this.findViewById(R.id.tv_event_time);
		timeView.setText(this.active.getStime());
		
		addressView = (TextView) this.findViewById(R.id.tv_event_address);
		addressView.setText(this.active.getAddress());
		
		typeView = (TextView) this.findViewById(R.id.tv_event_type);
		typeView.setText(this.active.getTypeName());
		
		chargeView = (TextView) this.findViewById(R.id.tv_event_charge);
		chargeView.setText(this.active.getCharge());
		
		sponsorView = (TextView) this.findViewById(R.id.tv_event_sponsor);
		sponsorView.setText(this.active.getSponsor());
	}

	public void hideDivider(){
		this.findViewById(R.id.iv_event_divider).setVisibility(View.GONE);
	}
	
	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.rootLayout:
			this.getActiveContent();
			break;
		}
	}
	
	private void getActiveContent(){
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(this.getActiveContentRequest());
		queue.addHttpRequest(this.getParticipantListRequest());
		
		queue.executeQueue("正在获取活动详情...", new LKHttpRequestQueueDone(){
			public void onComplete(){
				Intent intent = new Intent(ActiveItemLinearLayout.this.context, SchoolEventActivity.class);
				intent.putExtra("MODEL", active2);
				intent.putExtra("LIST", profileList);
				((BaseActivity)ActiveItemLinearLayout.this.context).startActivityForResult(intent, 100);
			}
		});
	}
	
	// 获取活动详情
	private LKHttpRequest getActiveContentRequest(){
		LKHttpRequest request = new LKHttpRequest(HttpRequestType.HTTP_COLLEGE_EVENT_DETAIL, null, new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				active2 = (ActiveModel) obj;
			}
		}, active.getId());
		
		return request;
	}
	
	// 获取活动成员列表
	private LKHttpRequest getParticipantListRequest(){
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("page", "1");
		paramMap.put("num", Constants.PAGESIZE+"");
		
		LKHttpRequest request = new LKHttpRequest(HttpRequestType.HTTP_COLLEGE_EVENT_PARTICIPANT_LIST, paramMap, new LKAsyncHttpResponseHandler() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				// TODO
				profileList = (ArrayList<ProfileModel>) ((HashMap<String, Object>) obj).get("list");
			}
		}, this.active.getId());
		
		return request;
	}

}
