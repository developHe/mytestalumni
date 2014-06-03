package com.hb.activity.component;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hb.R;
import com.hb.activity.AddTimelineActivity;
import com.hb.activity.BaseActivity;
import com.hb.activity.MyAttentionsActivity;
import com.hb.activity.ProfileActivity;
import com.hb.client.Constants;
import com.hb.client.HttpRequestType;
import com.hb.model.ProfileModel;
import com.hb.model.TimelineModel;
import com.hb.network.LKAsyncHttpResponseHandler;
import com.hb.network.LKHttpRequest;
import com.hb.network.LKHttpRequestQueue;
import com.hb.network.LKHttpRequestQueueDone;
import com.hb.util.ImageUtil;


/*
 * 自定义控件 - 卡片式履历
 */

public class ProfileCardRelativeLayout extends RelativeLayout {

	private TimelineModel data = null;
	private Context mContext = null;
	public Boolean isMe = true;
	public ProfileCardRelativeLayout(Context context, TimelineModel e, Boolean iMe) {
		super(context);
		this.mContext = context;
		this.data = e;
		this.isMe = iMe;
		
        LayoutInflater.from(context).inflate(R.layout.layout_profile_card, this, true); 
        
        this.init();
	}
	
	private void init(){
		
		RelativeLayout relayout_whole = (RelativeLayout)this.findViewById(R.id.relayout_whole);
		relayout_whole.setOnClickListener(this.onFindClicked);
		TextView tvStartDate = (TextView)this.findViewById(R.id.tv_profile_start_date);
		TextView tvEndDate = (TextView)this.findViewById(R.id.tv_profile_end_date);
		TextView tvLocalPosition = (TextView)this.findViewById(R.id.tv_profile_localposition);
		
		tvStartDate.setText(this.data.getStartTime().equals("null")?"未知":this.data.getStartTime());
		tvEndDate.setText(this.data.getEndTime().equals("null")?"未知":this.data.getEndTime());
		String localPosition = (this.data.getProvince().equals("null") ? "未知":this.data.getProvince()) +"--"+ (this.data.getCity().equals("null") ? "未知":this.data.getCity());
		tvLocalPosition.setText(localPosition);
		
		TextView tvOrg = (TextView)this.findViewById(R.id.tv_profile_org);
		tvOrg.setText(this.data.getOrg().equals("null") ? "未知":this.data.getOrg());
		
		ImageView photoImageView = (ImageView) this.findViewById(R.id.iv_profile_photo);
		ImageUtil.loadImage(R.drawable.img_card_head_portrait, this.data.getImgUrl(), photoImageView);
		
		ImageButton btnFind = (ImageButton)this.findViewById(R.id.btn_profile_find);
		
		Button btn_modify = (Button)this.findViewById(R.id.btn_modify);
		btn_modify.setOnClickListener(this.onFindClicked);
		
		Button btn_delete = (Button)this.findViewById(R.id.btn_delete);
		btn_delete.setOnClickListener(this.onFindClicked);
		if(!isMe){
			btnFind.setVisibility(View.GONE);
			btn_modify.setVisibility(View.GONE);
			btn_delete.setVisibility(View.GONE);
		}
	}
	
	private OnClickListener onFindClicked=new OnClickListener(){

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.relayout_whole:
				findRelatedProfile();				
				break;
			case R.id.btn_modify:
				modifyTimeLine();
				break;
			case R.id.btn_delete:
				delete();
				break;
			default:
				break;
			}
		}
	};
	
	private void modifyTimeLine()
	{
		Intent intent = new Intent(ProfileCardRelativeLayout.this.mContext, AddTimelineActivity.class);  
		intent.putExtra("DATA", data);
		intent.putExtra("ISMODIFY", true);
		intent.putExtra("TITLE", "修改履历");
		((BaseActivity) ProfileCardRelativeLayout.this.mContext).startActivityForResult(intent,5);

	}
	
	private void delete(){
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(doTimeLineDelete());
		queue.executeQueue("正在删除履历...", new LKHttpRequestQueueDone());
		
	}
	
	// 删除履历
	private LKHttpRequest doTimeLineDelete(){
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", data.getid());
		
		LKHttpRequest request = new LKHttpRequest( HttpRequestType.HTTP_TIMELINE_NODE_DELETE, paramMap, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				if((Integer)obj == 1){
					new AlertDialog.Builder(ProfileCardRelativeLayout.this.mContext)    
	                .setTitle("提示")  
	                .setMessage("履历成功删除！")  
	                .setPositiveButton("确定", new DialogInterface.OnClickListener() {  
                           public void onClick(DialogInterface dialog, int whichButton) {  
                        	   ((ProfileActivity)BaseActivity.getTopActivity()).refreshData();
                           }  
               })  
	                .show();
				}
			}
		});
		
		return request;
	}
	private void findRelatedProfile(){
		getSuggestPeopleList();  
	}
	
	private void getSuggestPeopleList(){
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getSuggestPeopleRequest());
		queue.executeQueue("正在查询推荐好友...", new LKHttpRequestQueueDone());
		
	}
	
	// 查看推荐好友
	private LKHttpRequest getSuggestPeopleRequest(){
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("page", "1");
		paramMap.put("num", Constants.PAGESIZE+"");
		
		LKHttpRequest request = new LKHttpRequest( HttpRequestType.HTTP_TIMELINE_NODE_NEWFRIENDS_LIST, paramMap, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				if(obj == null){
					BaseActivity.getTopActivity().showDialog(BaseActivity.MODAL_DIALOG, "没有相关推荐人！");

					
				}else{
					int count = Integer.valueOf((String)(((HashMap<String, Object>)obj).get("total")));
					ArrayList<ProfileModel> list = (ArrayList<ProfileModel>)(((HashMap<String, Object>)obj).get("list"));
					if(list == null || list.size() == 0){
						
					}else{
						Intent intent = new Intent(ProfileCardRelativeLayout.this.mContext, MyAttentionsActivity.class);  
						intent.putExtra("PROFILEMODELLIST", list);
						intent.putExtra("ID", data.getid());
						intent.putExtra("TITLE", "相关推荐");
						intent.putExtra("TOTAL", count);
						((BaseActivity)ProfileCardRelativeLayout.this.mContext).startActivityForResult(intent, 100);
					}
				}
				
			}
		}, data.getid());
		
		return request;
	}
}