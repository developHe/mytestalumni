package com.hb.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.hb.R;
import com.hb.client.Constants;
import com.hb.client.HttpRequestType;
import com.hb.model.GroupModel;
import com.hb.model.ProfileModel;
import com.hb.network.LKAsyncHttpResponseHandler;
import com.hb.network.LKHttpRequest;
import com.hb.network.LKHttpRequestQueue;
import com.hb.network.LKHttpRequestQueueDone;
import com.hb.util.ActivityUtil;

public class AllGroupActivity extends AbsSubActivity implements OnClickListener {

	private ListView listView = null;
	private GroupAdapter adapter = null;
	private ArrayList<GroupModel> array = null;
	private GroupModel selectModel = null;
	
	private int totalPage;
	private int currentPage;
	public Boolean isAll = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_all_group_list);
		
		Intent intent = this.getIntent();
		isAll = intent.getBooleanExtra("ISALL", true);
		
		TextView tv_title = (TextView)this.findViewById(R.id.titleView);
		if(!isAll){
			tv_title.setText("我的圈子");
		}
		Button btn_back = (Button)this.findViewById(R.id.profileButton);
		btn_back.setOnClickListener(this);
		listView = (ListView)this.findViewById(R.id.listView);
		array = new ArrayList<GroupModel>();
		adapter = new GroupAdapter(this);
		listView.setAdapter(adapter);
		// 设置空页面
		ActivityUtil.setEmptyView(listView);
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(AllGroupActivity.this, MeberOfGroupActivity.class);
				intent.putExtra("MODEL", array.get(arg2));
				AllGroupActivity.this.startActivity(intent);
			}
			
		});
		adapter.notifyDataSetChanged();
		
		totalPage = 0;
		currentPage = 0;
		
		refresh();
	}
	
	public void refresh(){
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		if(isAll){
			queue.addHttpRequest(getAllGroupRequest());
		}else{
			queue.addHttpRequest(getMyGroupRequest());
		}
		
		queue.executeQueue("正在请求数据...", new LKHttpRequestQueueDone());
	}
	
	// 查看所有圈子列表
	private LKHttpRequest getAllGroupRequest(){
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("page", ++currentPage +"");
		paramMap.put("num", Constants.PAGESIZE);
		LKHttpRequest request = new LKHttpRequest( HttpRequestType.HTTP_GROUP_LIST, paramMap, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				if (null != obj){
					ArrayList<GroupModel> list = (ArrayList<GroupModel>)(((HashMap<String, Object>)obj).get("list"));
					for(int i=0; i<list.size(); i++){
						array.add(list.get(i));
					}
					int count = Integer.valueOf((String)((HashMap<String, Object>)obj).get("total"));
					totalPage = (count + Constants.PAGESIZE - 1) / Constants.PAGESIZE;
					adapter.notifyDataSetChanged();
				}else{
				}
				
			}
		}, null);
		
		return request;
	}
	
	// 查看我的圈子列表
	private LKHttpRequest getMyGroupRequest(){
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("page", ++currentPage +"");
		paramMap.put("num", Constants.PAGESIZE);
		LKHttpRequest request = new LKHttpRequest( HttpRequestType.HTTP_GROUP_ME_LIST, paramMap, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				if (null != obj){
					ArrayList<GroupModel> list = (ArrayList<GroupModel>)(((HashMap<String, Object>)obj).get("list"));
					for(int i=0; i<list.size(); i++){
						array.add(list.get(i));
					}
					int count = Integer.valueOf((String)((HashMap<String, Object>)obj).get("total"));
					totalPage = (count + Constants.PAGESIZE - 1) / Constants.PAGESIZE;
					adapter.notifyDataSetChanged();
				}else{
				}
				
			}
		}, null);
		
		return request;
	}
	
	public final class GroupViewHolder{
		public LinearLayout contentLayout;
		public RelativeLayout moreLayout;
		
		public TextView tv_name;
		public TextView tv_desc;
		public TextView tv_creatTime;
		public Button btn_join;
		
		public Button moreButton;
	}
	public class GroupAdapter extends BaseAdapter{
		private LayoutInflater mInflater;
		public GroupAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}
		
		public int getCount(){
			if (currentPage < totalPage){
				return array.size() + 1;
			} else {
				return array.size();
			}
		}
		
		public Object getItem(int arg0){
			return arg0;
		}
		
		public long getItemId(int arg0){
			return arg0;
		}
		
		public View getView(int position, View convertView, ViewGroup parent){
			GroupViewHolder holder = null;
			if (null == convertView){
				holder = new GroupViewHolder();
				
				convertView = mInflater.inflate(R.layout.listview_item_all_group, null);
				
				holder.contentLayout = (LinearLayout) convertView.findViewById(R.id.contentLayout);
				holder.moreLayout = (RelativeLayout) convertView.findViewById(R.id.moreLayout);
				
				holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				holder.tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);
				holder.tv_creatTime = (TextView) convertView.findViewById(R.id.tv_createTime);
				holder.btn_join = (Button) convertView.findViewById(R.id.btn_join);
				holder.btn_join.setOnClickListener(AllGroupActivity.this);
				if(isAll){
					holder.btn_join.setText("加入");
				}else{
					holder.btn_join.setText("退出");
				}
				holder.btn_join.setTag(1000+position);
				holder.moreButton = (Button) convertView.findViewById(R.id.moreButton);
				holder.moreButton.setOnClickListener(AllGroupActivity.this);
				convertView.setTag(holder);
			} else {
				holder = (GroupViewHolder) convertView.getTag();
			}
			
			if (currentPage < totalPage) {
				if (position == array.size()){
					holder.contentLayout.setVisibility(View.GONE);
					holder.moreLayout.setVisibility(View.VISIBLE);
				} else {
					holder.contentLayout.setVisibility(View.VISIBLE);
					holder.moreLayout.setVisibility(View.GONE);
					holder.tv_name.setText(array.get(position).getName());
					holder.tv_desc.setText(array.get(position).getDesc());
					holder.tv_creatTime.setText(array.get(position).getCreateTime());
					
				}
			} else {
				holder.contentLayout.setVisibility(View.VISIBLE);
				holder.moreLayout.setVisibility(View.GONE);
				holder.tv_name.setText(array.get(position).getName());
				holder.tv_desc.setText(array.get(position).getDesc());
				holder.tv_creatTime.setText(array.get(position).getCreateTime());
				
			}
			
			return convertView;
		}
	}
	
	private void JoinGroup(){
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		if(isAll){
			queue.addHttpRequest(getJoinGroupRequest());
		}else{
			queue.addHttpRequest(getQuiteGroupRequest());
		}
		
		queue.executeQueue(isAll ? "正在加入圈子...":"正在退出圈子...", new LKHttpRequestQueueDone());
	}
	// 加入圈子列表
	private LKHttpRequest getJoinGroupRequest(){
		LKHttpRequest request = new LKHttpRequest( HttpRequestType.HTTP_GROUP_JOIN, null, new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				if((Integer)obj == 1){
					AllGroupActivity.this.showToast("成功加入圈子！");
				}else if((Integer)obj == -2){
					AllGroupActivity.this.showToast("已经加入过该圈子！");
				}
			}
		}, selectModel.getId());
		return request;
	}
	
	// 退出圈子列表
	private LKHttpRequest getQuiteGroupRequest(){
		LKHttpRequest request = new LKHttpRequest( HttpRequestType.HTTP_GROUP_QUIT, null, new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				if((Integer)obj == 1){
					AllGroupActivity.this.showToast("成功退出圈子！");
					array.clear();
					currentPage = 0;
					refresh();
					
				}else if((Integer)obj == -2){
					AllGroupActivity.this.showToast("没有加入过该圈子！");
				}else{
					AllGroupActivity.this.showToast("退出失败！");
				}
			}
		}, selectModel.getId());
		return request;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.profileButton:
			Intent it = new Intent();  
			AllGroupActivity.this.gobackWithResult(5, it);
			break;
		case R.id.moreButton:
			refresh();
			break;
		case R.id.btn_join:
			selectModel = array.get(Integer.valueOf(v.getTag().toString())-1000);
			JoinGroup();
			break;
		default:
			break;
		}
		
	}
	
}
