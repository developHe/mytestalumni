package com.hb.activity.component;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hb.R;
import com.hb.activity.AddTimelineActivity;
import com.hb.activity.BaseActivity;
import com.hb.activity.MyAttentionsActivity;
import com.hb.model.TimelineModel;

public class ProfileTimelineLinearLayout extends LinearLayout {

	private LinearLayout llTimelineLayout = null;
	private Context mContext = null;
	private Button btn_add = null;
	private ArrayList<TimelineModel> modelArray = null;
	public String mIdentity = "me";

	public ProfileTimelineLinearLayout(Context context) {
		super(context);
		this.mContext = context;

		this.init();
	}

	public ProfileTimelineLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;

		this.init();
	}

	private void init() {
		LayoutInflater.from(this.mContext).inflate(R.layout.layout_profile_timeline, this, true);
		llTimelineLayout = (LinearLayout) this.findViewById(R.id.ll_profile_timeline);
		btn_add = (Button) this.findViewById(R.id.btn_add);
		btn_add.setOnClickListener(listener);

	}

	public void refresh(ArrayList<TimelineModel> timelineList) {
		this.setVisibility(View.VISIBLE);
		modelArray = timelineList;

		if (!(mIdentity.equals("me"))) {
			btn_add.setVisibility(View.GONE);
		}
		llTimelineLayout.removeAllViews();
		for (int i = 0; i < timelineList.size(); i++) {

			ProfileCardRelativeLayout profile = new ProfileCardRelativeLayout(this.mContext, timelineList.get(i), mIdentity.equals("me") ? true : false);
			profile.setPadding(0, 0, 0, 0);

			LinearLayout.LayoutParams params =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			params.setMargins(0, 0, 0, 10);
			llTimelineLayout.addView(profile, params);

		}
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.btn_add:
				Intent intent = new Intent(ProfileTimelineLinearLayout.this.mContext, AddTimelineActivity.class);
				String afterId = ((TimelineModel) (modelArray.get(modelArray.size() - 1))).getid();
				intent.putExtra("AFTERID", afterId == null ? "null" : afterId);
				((BaseActivity) ProfileTimelineLinearLayout.this.mContext).startActivityForResult(intent, 5);
				break;

			default:
				break;
			}

		}
	};

}
