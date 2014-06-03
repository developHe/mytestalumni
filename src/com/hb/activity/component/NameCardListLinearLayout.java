package com.hb.activity.component;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.hb.R;
import com.hb.model.ProfileModel;

public class NameCardListLinearLayout extends LinearLayout {

	private ArrayList<ProfileModel> entries = null;

	public NameCardListLinearLayout(Context context, ArrayList<ProfileModel> e) {
		super(context);
		this.entries = e;

		LayoutInflater.from(context).inflate(R.layout.layout_name_card_list, this, true);

		this.init();
	}

	private void init() {
		LinearLayout llNameCardList = (LinearLayout) this.findViewById(R.id.ll_name_card_list);

		llNameCardList.removeAllViews();

		for (int i = 0; i < this.entries.size(); i++) {

			NameCardRelativeLayout profile = new NameCardRelativeLayout(this.getContext(), this.entries.get(i));

			profile.setPadding(0, 0, 0, 0);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			params.setMargins(0, 0, 0, 10);
			llNameCardList.addView(profile, params);

		}
		llNameCardList.invalidate();
	}
}
