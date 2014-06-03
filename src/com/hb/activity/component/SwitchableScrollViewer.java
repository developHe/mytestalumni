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
import com.hb.activity.AllGroupActivity;
import com.hb.activity.BaseActivity;
import com.hb.activity.MyAttentionsActivity;
import com.hb.activity.SuggestPeopleActivity;
import com.hb.client.Constants;
import com.hb.client.HttpRequestType;
import com.hb.model.ProfileModel;
import com.hb.network.LKAsyncHttpResponseHandler;
import com.hb.network.LKHttpRequest;
import com.hb.network.LKHttpRequestQueue;
import com.hb.network.LKHttpRequestQueueDone;

public class SwitchableScrollViewer extends ScrollView {

	private TextView tvTitle = null;
	
	private ArrayList<ProfileModel> entries = null;
	private Context mContext = null;
	private boolean isList = true;
//	private ImageButton btnList = null;
	private Button btnMore = null;
	private Button btnAllGroup = null;
	private String mTitle = null;
	private Boolean mHasMoreButton = true;
	private Boolean mHasSwitchButton = true;
	private String mPage = "1";
	private String mNum = Constants.PAGESIZE + "";
	private int totalPage = 0;
	private int currentPage = 0;
	private ArrayList<ProfileModel> list = new ArrayList<ProfileModel>();
	
	public SwitchableScrollViewer(Context context){
		super(context);
		this.mContext = context;
	}
	
	public void hiddenMoreButton(){
		if(this.btnMore != null){
			this.mHasMoreButton = false;
			this.btnMore.setVisibility(View.GONE);
		}
	}
	
	public void showAllGroupButton(){
		this.btnAllGroup.setVisibility(View.VISIBLE);
	}
	
	public SwitchableScrollViewer(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		
		this.init();
	}
	
	public SwitchableScrollViewer(Context context, ArrayList<ProfileModel> e, String title) {
		super(context);

		this.mContext = context;
		this.entries = e;
		this.mTitle = title;
		
		this.init();
	}
	
	public SwitchableScrollViewer(Context context, ArrayList<ProfileModel> e, String title, Boolean hasMore, Boolean hasSwitch) {
		super(context);

		this.mContext = context;
		this.entries = e;
		this.mTitle = title;
		this.mHasMoreButton = hasMore;
		this.mHasSwitchButton = hasSwitch;
		this.init();
	}

	private void init(){
		LayoutInflater.from(this.mContext).inflate(R.layout.layout_switchable_scrollview, this, true);
		
		tvTitle = (TextView)this.findViewById(R.id.tv_friend_title);
		if(this.mTitle != null){
			tvTitle.setText(this.mTitle);
		}
		
		btnMore = (Button)this.findViewById(R.id.btn_layout_more);
		btnMore.setOnClickListener(this.onSwitchView);
		btnMore.setVisibility(mHasMoreButton ? View.VISIBLE:View.GONE);
		
		btnAllGroup = (Button)this.findViewById(R.id.btn_layout_all_group);
		btnAllGroup.setOnClickListener(this.onSwitchView);
		
//		this.btnList = (ImageButton)this.findViewById(R.id.btn_layout_switch);
//		
//		btnList.setOnClickListener(this.onSwitchView);
//		btnList.setVisibility(View.GONE);
//		btnList.setVisibility(mHasSwitchButton ? View.VISIBLE:View.GONE);
		if(this.entries == null){
//			if(this.isList){
//				this.btnList.setBackgroundResource(R.drawable.img_card_list_two);
//			}else{
//				this.btnList.setBackgroundResource(R.drawable.img_card_list_one);
//			}
			
		}else{
			
			this.refreshContent();
		}
	}
	
	private void refreshContent(){
		if(this.mTitle != null){
			tvTitle.setText(this.mTitle);
		}
		
		if(this.entries == null) return;
		
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
		tvTitle.setText(title);
	}
	
	private OnClickListener onSwitchView = new OnClickListener(){

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
//			case R.id.btn_layout_switch:
//				isList = !isList;
//				refreshContent();
//				break;
			case R.id.btn_layout_more:
				if(mTitle.equals("个人关注")){
					getMoreMyAttentionData();
				}else{
					getMoreFansData();
				}
				break;
			case R.id.btn_layout_all_group:
				Intent intent = new Intent(SwitchableScrollViewer.this.mContext, AllGroupActivity.class);
				((BaseActivity) SwitchableScrollViewer.this.mContext).startActivityForResult(intent,5);
				break;
			default:
				break;
			}
			
		}
	};
	
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
	// 查看我关注的人
	private LKHttpRequest getMyAttentionsRequest(){
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("page", mPage);
		paramMap.put("num", mNum);
		LKHttpRequest request = new LKHttpRequest( HttpRequestType.HTTP_FRIENDS_LIST, paramMap, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				ArrayList<ProfileModel> tmpList = (ArrayList<ProfileModel>)(((HashMap<String, Object>)obj).get("list"));
				list = tmpList;
				
				if(list == null || list.size() == 0){
					
				}else{
					Integer total = Integer.valueOf((String)(((HashMap<String, Object>)obj).get("total")));
					Intent intent = new Intent(SwitchableScrollViewer.this.mContext, MyAttentionsActivity.class);  
					intent.putExtra("PROFILEMODELLIST", list);
					intent.putExtra("TITLE", "个人关注");
					intent.putExtra("TOTAL", total);
					SwitchableScrollViewer.this.mContext.startActivity(intent);
					
				}
				
			}
		});
		
		return request;
	}
	
	// 查看关注我的人
	private LKHttpRequest getFansRequest(){
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("page", mPage);
		paramMap.put("num", mNum);
		LKHttpRequest request = new LKHttpRequest( HttpRequestType.HTTP_FRIENDS_FUNS_LIST, paramMap, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				ArrayList<ProfileModel> list = (ArrayList<ProfileModel>)(((HashMap<String, Object>)obj).get("list"));
				Integer total = Integer.valueOf((String)(((HashMap<String, Object>)obj).get("total")));
				if(list == null || list.size() == 0){
					
				}else{
					Intent intent = new Intent(SwitchableScrollViewer.this.mContext, MyAttentionsActivity.class);  
					intent.putExtra("PROFILEMODELLIST", list);
					intent.putExtra("TITLE", "关注我的人");
					intent.putExtra("TOTAL", total);
					SwitchableScrollViewer.this.mContext.startActivity(intent);
				}
				
			}
		});
		
		return request;
	}
		
	public void refresh(ArrayList<ProfileModel> entries) {
		this.setVisibility(View.VISIBLE);
		
		this.entries = entries;
		
		this.refreshContent();
	}
	
}
