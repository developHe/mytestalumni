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
import android.widget.TextView;

import com.hb.R;
import com.hb.activity.BaseActivity;
import com.hb.activity.MyAttentionsActivity;
import com.hb.activity.ProfileActivity;
import com.hb.client.Constants;
import com.hb.client.HttpRequestType;
import com.hb.model.GroupModel;
import com.hb.model.ProfileModel;
import com.hb.network.LKAsyncHttpResponseHandler;
import com.hb.network.LKHttpRequest;
import com.hb.network.LKHttpRequestQueue;
import com.hb.network.LKHttpRequestQueueDone;

public class NameCardGroupRelativeLayout extends RelativeLayout {

	private GroupModel data = null;
	private Button	 btnAttention = null;
	private Context context;
	private ProfileModel detailModel = null;
	
	public NameCardGroupRelativeLayout(Context context, GroupModel d) {
		super(context);
		this.context = context;
		this.data = d;
		
        LayoutInflater.from(context).inflate(R.layout.listview_item_my_group, this, true); 
        
        this.init();
	}

	private void init(){
		if(this.data == null) return;
		
		TextView tv_name = (TextView)this.findViewById(R.id.tv_name);
		TextView tv_desc = (TextView)this.findViewById(R.id.tv_desc);
		TextView tv_creatTime = (TextView)this.findViewById(R.id.tv_createTime);
		LinearLayout layout_content = (LinearLayout)this.findViewById(R.id.contentLayout);
		layout_content.setOnClickListener(listener);
		Button btn_quite = (Button)this.findViewById(R.id.btn_quite);
		btn_quite.setOnClickListener(listener);
		
		tv_name.setText(this.data.getName());
		tv_desc.setText(this.data.getDesc());
		tv_creatTime.setText(this.data.getCreateTime());
		
	}
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.contentLayout:
				groupParticipantAction();
				break;
			case R.id.btn_quite:
				quiteAction();
				break;
			default:
				break;
			}
			
		}
	};
	
	private void quiteAction(){
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(quiteResquest());
		queue.executeQueue("正在退出圈子...", new LKHttpRequestQueueDone());
	}
	private LKHttpRequest quiteResquest(){
		LKHttpRequest request = new LKHttpRequest( HttpRequestType.HTTP_GROUP_QUIT, null, new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				if((Integer)obj == 1){
					BaseActivity.getTopActivity().showToast("成功退出圈子！");
					((ProfileActivity)BaseActivity.getTopActivity()).refreshData();
					
				}else if((Integer)obj == -2){
					BaseActivity.getTopActivity().showToast("没有加入过该圈子！");
				}else{
					BaseActivity.getTopActivity().showToast("退出失败！");
				}
			}
		}, data.getId());
		
		return request;
	}
	
	private void groupParticipantAction(){
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(groupParticipantRequest());
		queue.executeQueue("正在查询成员信息...", new LKHttpRequestQueueDone());
	}
	private LKHttpRequest groupParticipantRequest(){
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("page", "1");
		paramMap.put("num", Constants.PAGESIZE+"");
		LKHttpRequest request = new LKHttpRequest( HttpRequestType.HTTP_GROUP_PARTICIPANT_LIST, paramMap, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				ArrayList<ProfileModel> tmpList = (ArrayList<ProfileModel>)(((HashMap<String, Object>)obj).get("list"));
				
				if(tmpList == null || tmpList.size() == 0){
					BaseActivity.getTopActivity().showToast("没有相关成员");
				}else{
					Integer total = Integer.valueOf((String)(((HashMap<String, Object>)obj).get("total")));
					Intent intent = new Intent(BaseActivity.getTopActivity(), MyAttentionsActivity.class);  
					intent.putExtra("PROFILEMODELLIST", tmpList);
					intent.putExtra("TITLE", "圈子成员");
					intent.putExtra("TOTAL", total);
					BaseActivity.getTopActivity().startActivity(intent);
					
				}
			}
		}, data.getId());
		
		return request;
	}
	
	
}
