package com.hb.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.hb.R;
import com.hb.activity.CircleActivity.GroupAdapter;
import com.hb.activity.component.NameCardRelativeLayout;
import com.hb.client.Constants;
import com.hb.client.HttpRequestType;
import com.hb.model.GroupModel;
import com.hb.model.ProfileModel;
import com.hb.network.LKAsyncHttpResponseHandler;
import com.hb.network.LKHttpRequest;
import com.hb.network.LKHttpRequestQueue;
import com.hb.network.LKHttpRequestQueueDone;
import com.hb.util.ActivityUtil;
import com.hb.util.ImageUtil;
import com.hb.view.EditTextWithClearView;

public class SearchPeopleActivity extends AbsSubActivity implements
		OnClickListener {

	private Button backButton = null;
	private Button searchButton = null;
	private ListView listView = null;
	private PeopleAdapter adapter = null;
	private ArrayList<ProfileModel> array = null;
	private GroupModel groupModel = null;
	private int totalPage;
	private int currentPage = 0;
	private EditTextWithClearView keyEditView = null;
	private ProfileModel selectProfileModel = null;
	private Button btn_attention;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_search_people);

		backButton = (Button) this.findViewById(R.id.backButton);
		backButton.setOnClickListener(this);

		searchButton = (Button) this.findViewById(R.id.btn_search);
		searchButton.setOnClickListener(this);

		keyEditView = (EditTextWithClearView) this.findViewById(R.id.keyText);

		listView = (ListView) this.findViewById(R.id.listView);

		array = new ArrayList<ProfileModel>();

		adapter = new PeopleAdapter(this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				selectProfileModel = array.get(arg2);
				LKHttpRequestQueue queue = new LKHttpRequestQueue();
				queue.addHttpRequest(getProfileRequest());
				queue.executeQueue("正在请求数据...", new LKHttpRequestQueueDone());
			}

		});
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backButton:
			goback();
			break;

		case R.id.btn_search:
			if (checkValue()) {
				currentPage = 0;
				totalPage = 0;
				array.clear();
				refresh();
			}
			break;
		case R.id.moreButton:
			refresh();
			break;
		case R.id.btn_attention:
			btn_attention = (Button)v;
			int index = (Integer) v.getTag() - 1000;
			ProfileModel model = array.get(index);
			if(btn_attention.getText().equals("关注")){
				refreshAddAttentionData(model.getmId());
				
			}else if(btn_attention.getText().equals("取消关注")){
				refreshCancelAttentionData(model.getmId());
			}
			break;
		}
	}

	private boolean checkValue() {
		if (keyEditView.getText().trim().equals("")) {
			Toast.makeText(this, "取输入关键字", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	// 查看个人基本信息
	private LKHttpRequest getProfileRequest() {
		LKHttpRequest request = new LKHttpRequest(
				HttpRequestType.HTTP_PROFILE_DETAIL, null,
				new LKAsyncHttpResponseHandler() {
					@Override
					public void successAction(Object obj) {
						ProfileModel tmpModel = (ProfileModel) obj;
						tmpModel.setmId(selectProfileModel.getmId());

						Intent intent = new Intent(SearchPeopleActivity.this,
								ProfileOtherActivity.class);
						intent.putExtra("PROFILE", tmpModel);
						intent.putExtra("IDENTITY", "he");
						SearchPeopleActivity.this.startActivityForResult(
								intent, 100);
					}
				}, selectProfileModel.getmId());

		return request;
	}

	// 找同学
	private LKHttpRequest getSerchRequest() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("page", ++currentPage + "");
		paramMap.put("num", Constants.PAGESIZE);
		paramMap.put("keywords", keyEditView.getText());
		LKHttpRequest request = new LKHttpRequest(
				HttpRequestType.HTTP_FRIENDS_SEARCH, paramMap,
				new LKAsyncHttpResponseHandler() {
					@SuppressWarnings("unchecked")
					@Override
					public void successAction(Object obj) {
						if (obj == null) {
							SearchPeopleActivity.this.showToast("未搜到相关人员！");
							return;
						}
						ArrayList<ProfileModel> tmpList = (ArrayList<ProfileModel>) (((HashMap<String, Object>) obj)
								.get("list"));

						if (tmpList == null || tmpList.size() == 0) {
							SearchPeopleActivity.this.showToast("未搜到相关人员！");
						} else {
							Integer total = Integer
									.valueOf((String) (((HashMap<String, Object>) obj)
											.get("total")));
							totalPage = (total + Constants.PAGESIZE - 1)
									/ Constants.PAGESIZE;
							for (int i = 0; i < tmpList.size(); i++) {
								array.add(tmpList.get(i));
							}
							adapter.notifyDataSetChanged();

						}
					}
				});

		return request;
	}

	public final class PeopleViewHolder {
		public RelativeLayout contentLayout;
		public RelativeLayout moreLayout;

		public ImageView iv_head;
		public TextView tv_name;
		public TextView tv_gender;
		public TextView tv_title;
		public TextView tv_location;

		public Button btn_attention;
		public Button moreButton;
	}

	public class PeopleAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public PeopleAdapter(Context context) {
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
			PeopleViewHolder holder = null;
			if (null == convertView) {
				holder = new PeopleViewHolder();

				convertView = mInflater.inflate(
						R.layout.listview_item_search_classmate, null);

				holder.contentLayout = (RelativeLayout) convertView
						.findViewById(R.id.contentLayout);
				holder.moreLayout = (RelativeLayout) convertView
						.findViewById(R.id.moreLayout);

				holder.iv_head = (ImageView) convertView
						.findViewById(R.id.iv_photo);
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_name);
				holder.tv_gender = (TextView) convertView
						.findViewById(R.id.tv_gender);
				holder.tv_title = (TextView) convertView
						.findViewById(R.id.tv_title);
				holder.tv_location = (TextView) convertView
						.findViewById(R.id.tv_location);
				holder.btn_attention = (Button) convertView
						.findViewById(R.id.btn_attention);
				holder.btn_attention.setTag(1000 + position);
				holder.btn_attention
						.setOnClickListener(SearchPeopleActivity.this);
				holder.moreButton = (Button) convertView
						.findViewById(R.id.moreButton);
				holder.moreButton.setOnClickListener(SearchPeopleActivity.this);
				convertView.setTag(holder);
			} else {
				holder = (PeopleViewHolder) convertView.getTag();
			}

			if (currentPage < totalPage) {
				if (position == array.size()) {
					holder.contentLayout.setVisibility(View.GONE);
					holder.moreLayout.setVisibility(View.VISIBLE);
				} else {
					holder.contentLayout.setVisibility(View.VISIBLE);
					holder.moreLayout.setVisibility(View.GONE);
					holder.tv_name.setText(array.get(position).getName());
					holder.tv_gender
							.setText(array.get(position).getGender() == 1 ? "男"
									: "女");
					holder.tv_title.setText(array.get(position).getTitle());
					holder.tv_location.setText(array.get(position)
							.getProvince()
							+ "--"
							+ array.get(position).getCity());
//					if (array.get(position).getmFlag().equals("2")) {
//						holder.btn_attention.setVisibility(View.GONE);
//					} else {
//						holder.btn_attention.setText(array.get(position)
//								.getmFlag().equals("0") ? "关注" : "取消关注");
//					}
					ImageUtil.loadImage(R.drawable.img_card_head_portrait,
							SearchPeopleActivity.this.array.get(position)
									.getPic(), holder.iv_head);

				}
			} else {
				holder.contentLayout.setVisibility(View.VISIBLE);
				holder.moreLayout.setVisibility(View.GONE);
				holder.tv_name.setText(array.get(position).getName());
				holder.tv_gender
						.setText(array.get(position).getGender() == 1 ? "男"
								: "女");
				holder.tv_title.setText(array.get(position).getTitle());
				holder.tv_location.setText(array.get(position).getProvince()
						+ "--" + array.get(position).getCity());
//				if (array.get(position).getmFlag().equals("2")) {
//					holder.btn_attention.setVisibility(View.GONE);
//				} else {
//					holder.btn_attention.setText(array.get(position).getmFlag()
//							.equals("0") ? "关注" : "取消关注");
//				}
				ImageUtil.loadImage(R.drawable.img_card_head_portrait,
						SearchPeopleActivity.this.array.get(position).getPic(),
						holder.iv_head);
			}

			return convertView;
		}
	}

	public void refresh() {
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getSerchRequest());

		queue.executeQueue("正在请求数据...", new LKHttpRequestQueueDone());
	}

	// 加关注
	private void refreshAddAttentionData(String mId) {
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getAddAttentionRequest(mId));
		queue.executeQueue("正在关注...", new LKHttpRequestQueueDone());

	}

	private LKHttpRequest getAddAttentionRequest(String mId) {
		LKHttpRequest request = new LKHttpRequest(
				HttpRequestType.HTTP_FRIENDS_FOLLOW, null,
				new LKAsyncHttpResponseHandler() {
					@SuppressWarnings("unchecked")
					@Override
					public void successAction(Object obj) {
						int obj2 = (Integer) obj;
						if (obj2 == 1) {
//							currentPage = 0;
//							refresh();
							btn_attention.setText("取消关注");
						} else if(obj2 == -2){
							btn_attention.setText("取消关注");
						}

					}
				}, mId);

		return request;
	}

	// 取消关注
	private void refreshCancelAttentionData(String mId) {
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getCancelAttentionRequest(mId));
		queue.executeQueue("正在取消关注...", new LKHttpRequestQueueDone());

	}

	private LKHttpRequest getCancelAttentionRequest(String mId) {
		LKHttpRequest request = new LKHttpRequest(
				HttpRequestType.HTTP_FRIENDS_UNFOLLOW, null,
				new LKAsyncHttpResponseHandler() {
					@SuppressWarnings("unchecked")
					@Override
					public void successAction(Object obj) {
						int obj2 = (Integer) obj;
						if (obj2 == 1) {
//							currentPage = 0;
//							refresh();
							btn_attention.setText("关注");
						} else if(obj2 == -2){
							btn_attention.setText("关注");
						}

					}
				}, mId);

		return request;
	}
}
