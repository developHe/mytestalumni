package com.hb.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hb.R;
import com.hb.client.Constants;
import com.hb.client.HttpRequestType;
import com.hb.model.AnnouncementModel;
import com.hb.network.LKAsyncHttpResponseHandler;
import com.hb.network.LKHttpRequest;
import com.hb.network.LKHttpRequestQueue;
import com.hb.network.LKHttpRequestQueueDone;
import com.hb.util.ActivityUtil;

public class AnnouncementListActivity extends AbsSubActivity implements OnClickListener, OnItemClickListener {
	
	private Button backButton = null;
	private ListView listView = null;
	private AnnouncementAdapter adapter = null;
	
	private ArrayList<AnnouncementModel> modelList = new ArrayList<AnnouncementModel>();
	private AnnouncementModel selectedFile = null;
	
	
	private int totalCount = 0;
	private long totalPage = 0L;
	private int currentPage = 1;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.activity_announcement_list);
		
		Intent intent = this.getIntent();
		modelList = (ArrayList<AnnouncementModel>) intent.getSerializableExtra("LIST");
		totalCount = intent.getIntExtra("TOTAL", 0);
		totalPage = (totalCount + Constants.PAGESIZE - 1) / Constants.PAGESIZE;
		
		backButton = (Button) this.findViewById(R.id.backButton);
		backButton.setOnClickListener(this);
		
		listView = (ListView) this.findViewById(R.id.transList);
        adapter = new AnnouncementAdapter(this);
       
        listView.setAdapter(adapter);
        // 设置空页面
     	ActivityUtil.setEmptyView(listView);
        listView.setOnItemClickListener(this);
	}
	

	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.backButton:
			this.goback();
			break;
			
		case R.id.moreButton:
			this.loadMoreData();
			break;
		}
	}
	
	public void onBackPressed(){
		this.goback();
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		try{
			selectedFile = modelList.get(position);
			queryDetail();
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	private void queryDetail(){
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(this.getAnnouncementDetailRequest());
		queue.executeQueue("正在查询公告详情", new LKHttpRequestQueueDone());
	}
	
	private LKHttpRequest getAnnouncementListRequest(){
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("page", ++currentPage + "");
		paramMap.put("num", Constants.PAGESIZE + "");
		paramMap.put("previewLen", "200"); //预览长度，即取正文内容前几个字符，范围[0,200]，0为关闭预览
		
		LKHttpRequest request = new LKHttpRequest( HttpRequestType.HTTP_COLLEGE_BROADCAST_LIST, paramMap, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				HashMap<String, Object> map = (HashMap<String, Object>)obj;
				modelList.addAll((ArrayList<AnnouncementModel>)map.get("list"));
				
				adapter.notifyDataSetChanged();
			}
		});
		
		return request;
	}
	
	private LKHttpRequest getAnnouncementDetailRequest(){
		LKHttpRequest request = new LKHttpRequest( HttpRequestType.HTTP_COLLEGE_BROADCAST_DETAIL, null, new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				AnnouncementModel model = (AnnouncementModel) obj;
				
				Intent intent = new Intent(AnnouncementListActivity.this, AnnouncementDetailActivity.class);
				intent.putExtra("MODEL", model);
				AnnouncementListActivity.this.startActivity(intent);
				
			}
		}, selectedFile.getId());
		
		return request;
	}
	
	private void loadMoreData(){
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(this.getAnnouncementListRequest());
		queue.executeQueue("正在加载公告", new LKHttpRequestQueueDone());
	}
	
	public final class AnnouncementViewHolder{
		public RelativeLayout contentLayout;
		public RelativeLayout moreLayout;
		
		public TextView titleView;
		public TextView previewView;
		public TextView timeView;
		
		public Button moreButton;
	}
	
	public class AnnouncementAdapter extends BaseAdapter{
		
		private LayoutInflater mInflater;
		public AnnouncementAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			if (currentPage+1 < totalPage){
				return modelList.size() + 1;
			} else {
				return modelList.size();
			}
		}

		@Override
		public Object getItem(int arg0) {
			return modelList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			AnnouncementViewHolder holder = null;
			
			if (null == convertView){
				holder = new AnnouncementViewHolder();
				
				convertView = mInflater.inflate(R.layout.listview_item_announcement, null);
				
				holder.contentLayout = (RelativeLayout) convertView.findViewById(R.id.contentLayout);
				holder.moreLayout = (RelativeLayout) convertView.findViewById(R.id.moreLayout);
				
				holder.titleView = (TextView) convertView.findViewById(R.id.annTitle);
				holder.previewView = (TextView) convertView.findViewById(R.id.annContent);
				holder.timeView = (TextView) convertView.findViewById(R.id.annDate);
				
				holder.moreButton = (Button) convertView.findViewById(R.id.moreButton);
				holder.moreButton.setOnClickListener(AnnouncementListActivity.this);
				
				convertView.setTag(holder);
			} else {
				holder = (AnnouncementViewHolder) convertView.getTag(); 
			}
			
			if (currentPage < totalPage) {
				if (position == modelList.size()){
					holder.contentLayout.setVisibility(View.GONE);
					holder.moreLayout.setVisibility(View.VISIBLE);
				} else {
					holder.contentLayout.setVisibility(View.VISIBLE);
					holder.moreLayout.setVisibility(View.GONE);
					
					holder.titleView.setText(modelList.get(position).getTitle());
					holder.previewView.setText(modelList.get(position).getPreview());
					holder.timeView.setText(modelList.get(position).getTime());
				}
			} else {
				holder.contentLayout.setVisibility(View.VISIBLE);
				holder.moreLayout.setVisibility(View.GONE);
				
				holder.titleView.setText(modelList.get(position).getTitle());
				holder.previewView.setText(modelList.get(position).getPreview());
				holder.timeView.setText(modelList.get(position).getTime());
			}
			
			
			return convertView;
		}
		
	}
	
}
