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
import com.hb.activity.CircleActivity;
import com.hb.activity.MyAttentionsActivity;
import com.hb.activity.SuggestPeopleActivity;
import com.hb.client.Constants;
import com.hb.client.HttpRequestType;
import com.hb.model.GroupModel;
import com.hb.model.ProfileModel;
import com.hb.network.LKAsyncHttpResponseHandler;
import com.hb.network.LKHttpRequest;
import com.hb.network.LKHttpRequestQueue;
import com.hb.network.LKHttpRequestQueueDone;

public class SwitchableGroupScrollViewer extends ScrollView {

	private TextView tvTitle = null;
	
	private ArrayList<GroupModel> entries = null;
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
	private ArrayList<GroupModel> list = new ArrayList<GroupModel>();
	
	public SwitchableGroupScrollViewer(Context context){
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
	
	public SwitchableGroupScrollViewer(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		
		this.init();
	}
	
	public SwitchableGroupScrollViewer(Context context, ArrayList<GroupModel> e, String title) {
		super(context);

		this.mContext = context;
		this.entries = e;
		this.mTitle = title;
		
		this.init();
	}
	
	public SwitchableGroupScrollViewer(Context context, ArrayList<GroupModel> e, String title, Boolean hasMore, Boolean hasSwitch) {
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
//		btnMore.setVisibility(mHasMoreButton ? View.VISIBLE:View.GONE);
		
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
			NameCardListGroupLinearLayout glNameCards = new NameCardListGroupLinearLayout(this.mContext, this.entries);
			svContent.addView(glNameCards);
//			this.btnList.setBackgroundResource(R.drawable.img_card_list_two);

		}else{
//			NameCardListTableLayout glNameCards = new NameCardListGroupLinearLayout(this.mContext, this.entries);
//			svContent.addView(glNameCards);
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
				
				getMoreMyGroupData();
				break;
			case R.id.btn_layout_all_group:
				Intent intent = new Intent(SwitchableGroupScrollViewer.this.mContext, CircleActivity.class);
				((BaseActivity) SwitchableGroupScrollViewer.this.mContext).startActivityForResult(intent,5);
				
				break;
			default:
				break;
			}
			
		}
	};
	
	private void getMoreMyGroupData(){
		Intent intent = new Intent(SwitchableGroupScrollViewer.this.mContext, AllGroupActivity.class);
		intent.putExtra("ISALL", false);
		((BaseActivity) SwitchableGroupScrollViewer.this.mContext).startActivityForResult(intent,5);
		
	}
		
	public void refresh(ArrayList<GroupModel> entries) {
		this.setVisibility(View.VISIBLE);
		
		this.entries = entries;
		
		this.refreshContent();
	}
	
}
