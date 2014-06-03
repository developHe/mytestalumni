package com.hb.activity.component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hb.R;
import com.hb.activity.MyAttentionsActivity;
import com.hb.activity.SuggestPeopleActivity;
import com.hb.client.Constants;
import com.hb.client.HttpRequestType;
import com.hb.model.ProfileModel;
import com.hb.network.LKAsyncHttpResponseHandler;
import com.hb.network.LKHttpRequest;
import com.hb.network.LKHttpRequestQueue;
import com.hb.network.LKHttpRequestQueueDone;

public class SwitchableAttentionScrollViewer extends ScrollView {

	private TextView tvTitle = null;
	
	private ArrayList<ProfileModel> entries = null;
	private Context mContext = null;
	private boolean isList = true;
//	private ImageButton btnList = null;
	private Button mBtn_more = null;
	private String mTitle = null;
	private String mPage = "1";
	private String mNum = Constants.PAGESIZE + "";
	private int totalPage = 0;
	private int currentPage = 1;
	private String currentId = null;
	
	private ArrayList<ProfileModel> list = new ArrayList<ProfileModel>();
	
	public SwitchableAttentionScrollViewer(Context context){
		super(context);
		this.mContext = context;
	}
	
	public SwitchableAttentionScrollViewer(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		
		this.init();
	}
	
	public SwitchableAttentionScrollViewer(Context context, ArrayList<ProfileModel> e, String title, String idTmp, int total) {
		super(context);

		this.mContext = context;
		this.entries = e;
		this.mTitle = title;
		this.currentId = idTmp;
		this.totalPage = (total + Integer.parseInt(mNum) - 1) / Integer.parseInt(mNum);
		this.init();
	}
	
	private void init(){
		LayoutInflater.from(this.mContext).inflate(R.layout.layout_switchable_attention_scrollview, this, true);
				
		mBtn_more = (Button)this.findViewById(R.id.btn_more);
		mBtn_more.setOnClickListener(onSwitchView);
		
//		this.btnList = (ImageButton)this.findViewById(R.id.btn_layout_switch);
		
//		btnList.setOnClickListener(this.onSwitchView);
		if(this.entries == null){
//			if(this.isList){
//				this.btnList.setBackgroundResource(R.drawable.img_card_list_two);
//			}else{
//				this.btnList.setBackgroundResource(R.drawable.img_card_list_one);
//			}
//			
		}else{
			
			this.refreshContent();
		}
	}
	
	private void refreshContent(){
		if(this.mTitle != null && tvTitle != null){
			tvTitle.setText(this.mTitle);
		}
		
		if(this.entries == null) return;
		
		if(currentPage < totalPage){
			mBtn_more.setVisibility(View.VISIBLE);
		}else{
			mBtn_more.setVisibility(View.GONE);
		}
		ScrollView svContent = (ScrollView)this.findViewById(R.id.sv_container);
		
		svContent.removeAllViews();
		
		if(this.isList){
			NameCardListLinearLayout glNameCards = new NameCardListLinearLayout(this.mContext, this.entries);
			svContent.addView(glNameCards);
//			this.btnList.setBackgroundResource(R.drawable.img_card_list_two);

		}else{
			NameCardListTableLayout glNameCards = new NameCardListTableLayout(this.mContext, this.entries);
			svContent.addView(glNameCards);
//			this.btnList.setBackgroundResource(R.drawable.img_card_list_one);
		}
	}
	
	public void setTitle(String title){
		this.mTitle = title;
	}
	
	private OnClickListener onSwitchView = new OnClickListener(){

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
//			case R.id.btn_layout_switch:
//				isList = !isList;
//				refreshContent();
//				break;
			case R.id.btn_more:
				if(mTitle.equals("个人关注")){
					getMoreMyAttentionData();
				}else if(mTitle.equals("我关注的人")){
					getMoreFansData();
				}else if(mTitle.equals("相关推荐")){
					getSuggestPeopleList();
				}else if(mTitle.equals("圈子成员")){
					getMoreGroupParticipantList();
				}
				break;
			default:
				break;
			}
			
		}
	};
	
	private void getMoreGroupParticipantList(){
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getGroupParticipantsRequest());
		
		queue.executeQueue("正在获取更多...", new LKHttpRequestQueueDone());
	}
	
	private void getMoreMyAttentionData(){
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getMyAttentionsRequest());
		
		queue.executeQueue("正在获取更多...", new LKHttpRequestQueueDone());
		
	}
	
	private void getMoreFansData(){
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getFansRequest());
		
		queue.executeQueue("正在获取更多...", new LKHttpRequestQueueDone());
		
	}
	
	// 查看圈子成员
	private LKHttpRequest getGroupParticipantsRequest(){
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("page", ++currentPage+"");
		paramMap.put("num", mNum);
		LKHttpRequest request = new LKHttpRequest( HttpRequestType.HTTP_GROUP_PARTICIPANT_LIST, paramMap, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				if(obj == null){
					mBtn_more.setVisibility(View.GONE);
					return;
				}
				ArrayList<ProfileModel> tmpList = (ArrayList<ProfileModel>)(((HashMap<String, Object>)obj).get("list"));
				for(ProfileModel model:tmpList){
					entries.add(model);
				}
				
				int count = Integer.valueOf((String)(((HashMap<String, Object>)obj).get("total")));
				totalPage = (count + Integer.parseInt(mNum) - 1) / Integer.parseInt(mNum);
				 
				if(entries == null || entries.size() == 0){
					
				}else{
					refreshContent();
					
				}
				
			}
		}, currentId);
		
		return request;
	}
		
	// 查看我关注的人
	private LKHttpRequest getMyAttentionsRequest(){
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("page", ++currentPage+"");
		paramMap.put("num", mNum);
		LKHttpRequest request = new LKHttpRequest( HttpRequestType.HTTP_FRIENDS_LIST, paramMap, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				if(obj == null){
					mBtn_more.setVisibility(View.GONE);
					return;
				}
				ArrayList<ProfileModel> tmpList = (ArrayList<ProfileModel>)(((HashMap<String, Object>)obj).get("list"));
				for(ProfileModel model:tmpList){
					entries.add(model);
				}
				
				int count = Integer.valueOf((String)(((HashMap<String, Object>)obj).get("total")));
				totalPage = (count + Integer.parseInt(mNum) - 1) / Integer.parseInt(mNum);
				 
				if(entries == null || entries.size() == 0){
					
				}else{
					refreshContent();
					
				}
				
			}
		});
		
		return request;
	}
	
	// 查看关注我的人
	private LKHttpRequest getFansRequest(){
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("page", ++currentPage+"");
		paramMap.put("num", mNum);
		LKHttpRequest request = new LKHttpRequest( HttpRequestType.HTTP_FRIENDS_FUNS_LIST, paramMap, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				if(obj == null){
					mBtn_more.setVisibility(View.GONE);
					return;
				}
				ArrayList<ProfileModel> tmpList = (ArrayList<ProfileModel>)(((HashMap<String, Object>)obj).get("list"));
				for(ProfileModel model:tmpList){
					entries.add(model);
				}
				
				int count = Integer.valueOf((String)(((HashMap<String, Object>)obj).get("total")));
				totalPage = (count + Integer.parseInt(mNum) - 1) / Integer.parseInt(mNum);
				 
				if(entries == null || entries.size() == 0){
					
				}else{
					refreshContent();
					
				}
			}
		});
		
		return request;
	}
		
	private void getSuggestPeopleList(){
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getSuggestPeopleRequest());
		queue.executeQueue("正在查询推荐好友...", new LKHttpRequestQueueDone());
		
	}
	
	// 查看推荐好友
	private LKHttpRequest getSuggestPeopleRequest(){
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("page", ++currentPage+"");
		paramMap.put("num", Constants.PAGESIZE+"");
		
		LKHttpRequest request = new LKHttpRequest( HttpRequestType.HTTP_TIMELINE_NODE_NEWFRIENDS_LIST, paramMap, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				if(obj == null){
					mBtn_more.setVisibility(View.GONE);
					return;
				}
				ArrayList<ProfileModel> tmpList = (ArrayList<ProfileModel>)(((HashMap<String, Object>)obj).get("list"));
				for(ProfileModel model:tmpList){
					entries.add(model);
				}
				
				int count = Integer.valueOf((String)(((HashMap<String, Object>)obj).get("total")));
				totalPage = (count + Integer.parseInt(mNum) - 1) / Integer.parseInt(mNum);
				 
				if(entries == null || entries.size() == 0){
					
				}else{
					refreshContent();
					
				}
			}
		}, currentId);
		
		return request;
	}
	public void refresh(ArrayList<ProfileModel> entries) {
		this.setVisibility(View.VISIBLE);
		
		this.entries = entries;
		
		this.refreshContent();
	}
	
}
