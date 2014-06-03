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
import android.widget.ImageView;
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
import com.hb.util.ImageUtil;

public class MeberOfGroupActivity extends AbsSubActivity implements
		OnClickListener {

	private ListView listView = null;
	private MemberAdapter adapter = null;
	private ArrayList<ProfileModel> array = null;
	private GroupModel groupModel = null;

	private int totalPage;
	private int currentPage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_all_group_list);

		Intent intent = this.getIntent();
		groupModel = (GroupModel) intent.getSerializableExtra("model");
		TextView tv_title = (TextView) this.findViewById(R.id.titleView);
		tv_title.setText("圈子成员");
		Button btn_back = (Button) this.findViewById(R.id.profileButton);
		btn_back.setOnClickListener(this);
		listView = (ListView) this.findViewById(R.id.listView);
		array = new ArrayList<ProfileModel>();
		adapter = new MemberAdapter(this);
		listView.setAdapter(adapter);
		// 设置空页面
		ActivityUtil.setEmptyView(listView);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.i("tag", "----");
			}

		});
		adapter.notifyDataSetChanged();

		totalPage = 0;
		currentPage = 0;

		refresh();
	}

	public void refresh() {
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(groupParticipantRequest());
		queue.executeQueue("正在查询成员信息...", new LKHttpRequestQueueDone());
	}

	private LKHttpRequest groupParticipantRequest() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("page", ++currentPage + "");
		paramMap.put("num", Constants.PAGESIZE + "");
		LKHttpRequest request = new LKHttpRequest(
				HttpRequestType.HTTP_GROUP_PARTICIPANT_LIST, paramMap,
				new LKAsyncHttpResponseHandler() {
					@SuppressWarnings("unchecked")
					@Override
					public void successAction(Object obj) {
						if (obj == null) {
							MeberOfGroupActivity.this.showToast("没有相关成员");
							return;
						}
						ArrayList<ProfileModel> tmpList = (ArrayList<ProfileModel>) (((HashMap<String, Object>) obj)
								.get("list"));

						if (tmpList == null || tmpList.size() == 0) {
							MeberOfGroupActivity.this.showToast("没有相关成员");
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
				}, groupModel.getId());

		return request;
	}

	public final class MemberViewHolder {
		public RelativeLayout contentLayout;
		public RelativeLayout moreLayout;

		public ImageView iv_head;
		public TextView tv_name;
		public TextView tv_gender;
		public TextView tv_title;
		public TextView tv_location;
		public Button btn_delete;

		public Button moreButton;
	}

	public class MemberAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public MemberAdapter(Context context) {
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
			MemberViewHolder holder = null;
			if (null == convertView) {
				holder = new MemberViewHolder();

				convertView = mInflater.inflate(
						R.layout.listview_item_group_member, null);

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
				holder.btn_delete = (Button) convertView
						.findViewById(R.id.btn_delete);
				holder.btn_delete.setOnClickListener(MeberOfGroupActivity.this);
				holder.btn_delete.setTag(position+100);
				holder.moreButton = (Button) convertView
						.findViewById(R.id.moreButton);
				holder.moreButton.setOnClickListener(MeberOfGroupActivity.this);
				convertView.setTag(holder);
			} else {
				holder = (MemberViewHolder) convertView.getTag();
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
					ImageUtil.loadImage(R.drawable.img_card_head_portrait,
							MeberOfGroupActivity.this.array.get(position)
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
				ImageUtil.loadImage(R.drawable.img_card_head_portrait,
						MeberOfGroupActivity.this.array.get(position).getPic(),
						holder.iv_head);
			}

			return convertView;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.profileButton:
			goback();
			break;
		case R.id.moreButton:
			refresh();
			break;
		case R.id.btn_delete:
			int tag = (Integer) ((Button)v).getTag()-100;
			deleteAction(((ProfileModel)array.get(tag)).getmId());
			break;
		default:
			break;
		}

	}

	public void deleteAction(String persionId) {
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getDeleteMemberRequest(persionId));
		queue.executeQueue("正在删除成员", new LKHttpRequestQueueDone());
	}

	// 删除成员
	private LKHttpRequest getDeleteMemberRequest(String persionId) {
		LKHttpRequest request = new LKHttpRequest(
				HttpRequestType.HTTP_GOURPKICK, null,
				new LKAsyncHttpResponseHandler() {
					@Override
					public void successAction(Object obj) {
						if (obj.equals("1")) {
							MeberOfGroupActivity.this.showToast("成功删除该成员！");
							currentPage = 0;
							array.clear();
							refresh();
						} else if (obj.equals("-1")) {
							MeberOfGroupActivity.this.showToast("ID不存在！");
						} else if (obj.equals("-2")) {
							MeberOfGroupActivity.this.showToast("personId不存在！");
						} else if (obj.equals("-3")) {
							MeberOfGroupActivity.this.showToast("不是圈子所有者 ！");
						}

					}
				}, persionId);
		return request;
	}
}
