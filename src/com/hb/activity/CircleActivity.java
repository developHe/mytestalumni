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
import com.hb.client.ApplicationEnvironment;
import com.hb.client.Constants;
import com.hb.client.HttpRequestType;
import com.hb.model.GroupModel;
import com.hb.network.LKAsyncHttpResponseHandler;
import com.hb.network.LKHttpRequest;
import com.hb.network.LKHttpRequestQueue;
import com.hb.network.LKHttpRequestQueueDone;
import com.hb.util.ActivityUtil;

public class CircleActivity extends AbsSubActivity implements OnClickListener {

	private ListView listView = null;
	private GroupAdapter adapter = null;
	private ArrayList<GroupModel> array = null;
	private ArrayList<GroupModel> array_0 = null;
	private ArrayList<GroupModel> array_1 = null;
	private ArrayList<GroupModel> array_2 = null;
	private GroupModel selectModel = null;

	private int totalPage;
	private int currentPage = 0;
	public Boolean isAll = true;
	public int currentGroup = 0; // 0：我创建的圈子  1：我加入的圈子  2：系统推荐

	private Button btn_1 = null;
	private Button btn_2 = null;
	private Button btn_3 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_circle);

		btn_1 = (Button) this.findViewById(R.id.btn_1);
		btn_2 = (Button) this.findViewById(R.id.btn_2);
		btn_3 = (Button) this.findViewById(R.id.btn_3);
		btn_1.setOnClickListener(this);
		btn_2.setOnClickListener(this);
		btn_3.setOnClickListener(this);
		
		Button btn_create = (Button)this.findViewById(R.id.btn_create);
		btn_create.setOnClickListener(this);
		
		Button btn_back = (Button) this.findViewById(R.id.profileButton);
		btn_back.setOnClickListener(this);
		listView = (ListView) this.findViewById(R.id.listView);
		array = new ArrayList<GroupModel>();
		array_0 = new ArrayList<GroupModel>();
		array_1 = new ArrayList<GroupModel>();
		array_2 = new ArrayList<GroupModel>();
		
		adapter = new GroupAdapter(this);
		listView.setAdapter(adapter);
		// 设置空页面
		ActivityUtil.setEmptyView(listView);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				 Intent intent = new Intent(CircleActivity.this,
				 GroupDetailActivity.class);
				 intent.putExtra("model", (GroupModel)(array.get(arg2)));
				 intent.putExtra("tag", currentGroup);
				 CircleActivity.this.startActivityForResult(intent, 0);
			}

		});
		adapter.notifyDataSetChanged();

		totalPage = 0;
		currentPage = 0;

		refresh();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0:
			array.clear();
			currentPage = 0;
			totalPage = 0;
			refresh();
			break;

		default:
			break;
		}
	}

	public void refresh() {
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		if (currentGroup == 0) {
			queue.addHttpRequest(getMyGroupRequest());
		} else if(currentGroup == 1) {
			queue.addHttpRequest(getMyJoinGroupRequest());
		} else if(currentGroup == 2){
			queue.addHttpRequest(getAllGroupRequest());
		}

		queue.executeQueue("正在请求数据...", new LKHttpRequestQueueDone());
	}

	// 查看所有圈子列表
	private LKHttpRequest getAllGroupRequest() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("page", ++currentPage + "");
		paramMap.put("num", Constants.PAGESIZE);
		LKHttpRequest request = new LKHttpRequest(HttpRequestType.HTTP_GROUP_LIST, paramMap, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				if (null != obj) {
					ArrayList<GroupModel> list = (ArrayList<GroupModel>) (((HashMap<String, Object>) obj).get("list"));
					for (int i = 0; i < list.size(); i++) {
						array_2.add(list.get(i));
					}
					int count = Integer.valueOf((String) ((HashMap<String, Object>) obj).get("total"));
					array = array_2;
					totalPage = (count + Constants.PAGESIZE - 1) / Constants.PAGESIZE;
					adapter.notifyDataSetChanged();
				} else {
					array.clear();
					adapter.notifyDataSetChanged();
				}

			}
		}, null);

		return request;
	}

	// 查看我创建的圈子列表
	private LKHttpRequest getMyGroupRequest() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("page", ++currentPage + "");
		paramMap.put("num", Constants.PAGESIZE);
		LKHttpRequest request = new LKHttpRequest(HttpRequestType.HTTP_GROUP_MY_LIST, paramMap, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				if (null != obj) {
					ArrayList<GroupModel> list = (ArrayList<GroupModel>) (((HashMap<String, Object>) obj).get("list"));
					for (int i = 0; i < list.size(); i++) {
						array_0.add(list.get(i));
					}
					int count = Integer.valueOf((String) ((HashMap<String, Object>) obj).get("total"));
					array = array_0;
					totalPage = (count + Constants.PAGESIZE - 1) / Constants.PAGESIZE;
					adapter.notifyDataSetChanged();
				} else {
					array.clear();
					adapter.notifyDataSetChanged();
				}

			}
		}, null);

		return request;
	}
	
	// 查看我加入的圈子列表
	private LKHttpRequest getMyJoinGroupRequest() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("page", ++currentPage + "");
		paramMap.put("num", Constants.PAGESIZE);
		LKHttpRequest request = new LKHttpRequest(HttpRequestType.HTTP_GROUP_ME_LIST, paramMap, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				if (null != obj) {
					ArrayList<GroupModel> list = (ArrayList<GroupModel>) (((HashMap<String, Object>) obj).get("list"));
					for (int i = 0; i < list.size(); i++) {
						array_1.add(list.get(i));
					}
					int count = Integer.valueOf((String) ((HashMap<String, Object>) obj).get("total"));
					array = array_1;
					totalPage = (count + Constants.PAGESIZE - 1) / Constants.PAGESIZE;
					adapter.notifyDataSetChanged();
				} else {
					array.clear();
					adapter.notifyDataSetChanged();
				}

			}
		}, null);

		return request;
	}

	public final class GroupViewHolder {
		public RelativeLayout contentLayout;
		public RelativeLayout moreLayout;

		public TextView tv_name;
		public TextView tv_desc;
		public TextView tv_creatTime;

		public Button moreButton;
	}

	public class GroupAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public GroupAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			if (currentPage < totalPage) {
				return array.size() + 1;
			} else {
				return array.size();
			}
		}

		public Object getItem(int arg0) {
			return arg0;
		}

		public long getItemId(int arg0) {
			return arg0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			GroupViewHolder holder = null;
			if (null == convertView) {
				holder = new GroupViewHolder();

				convertView = mInflater.inflate(R.layout.listview_item_circle, null);

				holder.contentLayout = (RelativeLayout) convertView.findViewById(R.id.contentLayout);
				holder.moreLayout = (RelativeLayout) convertView.findViewById(R.id.moreLayout);

				holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				holder.tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);
				holder.tv_creatTime = (TextView) convertView.findViewById(R.id.tv_createTime);
				holder.moreButton = (Button) convertView.findViewById(R.id.moreButton);
				holder.moreButton.setOnClickListener(CircleActivity.this);
				convertView.setTag(holder);
			} else {
				holder = (GroupViewHolder) convertView.getTag();
			}

			if (currentPage < totalPage) {
				if (position == array.size()) {
					holder.contentLayout.setVisibility(View.GONE);
					holder.moreLayout.setVisibility(View.VISIBLE);
				} else {
					if (array != null) {
						holder.contentLayout.setVisibility(View.VISIBLE);
						holder.moreLayout.setVisibility(View.GONE);
						holder.tv_name.setText(array.get(position).getName());
						holder.tv_desc.setText(array.get(position).getDesc());
						holder.tv_creatTime.setText(array.get(position).getCreateTime());
					}

				}
			} else {
				if (array != null) {
					holder.contentLayout.setVisibility(View.VISIBLE);
					holder.moreLayout.setVisibility(View.GONE);
					holder.tv_name.setText(array.get(position).getName());
					holder.tv_desc.setText(array.get(position).getDesc());
					holder.tv_creatTime.setText(array.get(position).getCreateTime());
				}

			}

			return convertView;
		}
	}

	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.profileButton:
			Intent it = new Intent();
			setResult(5, it);
			finish();
			break;
		case R.id.moreButton:
			refresh();
			break;
		case R.id.btn_1:
			currentGroup = 0;
			currentPage = 0;
			totalPage = 0;
			array.clear();
			btn_1.setBackgroundResource(R.drawable.btn_group_s);
			btn_2.setBackgroundResource(R.drawable.btn_group_n);
			btn_3.setBackgroundResource(R.drawable.btn_group_n);
			adapter.notifyDataSetChanged();
			refresh();
			break;
		case R.id.btn_2:
			currentGroup = 1;
			currentPage = 0;
			totalPage = 0;
			array.clear();
			btn_2.setBackgroundResource(R.drawable.btn_group_s);
			btn_1.setBackgroundResource(R.drawable.btn_group_n);
			btn_3.setBackgroundResource(R.drawable.btn_group_n);
			adapter.notifyDataSetChanged();
			refresh();
			break;
		case R.id.btn_3:
			currentGroup = 2;
			currentPage = 0;
			totalPage = 0;
			array.clear();
			btn_3.setBackgroundResource(R.drawable.btn_group_s);
			btn_2.setBackgroundResource(R.drawable.btn_group_n);
			btn_1.setBackgroundResource(R.drawable.btn_group_n);
			adapter.notifyDataSetChanged();
			refresh();
			break;
		case R.id.btn_create:
			Intent intent_create = new Intent(CircleActivity.this, CreateCircleActivity.class);
			CircleActivity.this.startActivityForResult(intent_create, 0);
			break;
		default:
			break;
		}

	}
	
	public void backAction() {
		ApplicationEnvironment.getInstance().exitApp();
	}

}
