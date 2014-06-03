package com.hb.activity.component;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.hb.R;
import com.hb.model.GroupModel;
import com.hb.model.ProfileModel;

public class NameCardListGroupLinearLayout extends LinearLayout {

	private ArrayList<GroupModel> entries = null;
	public NameCardListGroupLinearLayout(Context context, ArrayList<GroupModel> e) {
		super(context);
		this.entries = e;
		
        LayoutInflater.from(context).inflate(R.layout.layout_name_card_list, this, true); 
		
		this.init();
	}

	private void init(){
		LinearLayout llNameCardList = (LinearLayout)this.findViewById(R.id.ll_name_card_list);
		
		llNameCardList.removeAllViews();
		
		for(int i = 0; i < this.entries.size(); i ++){

			NameCardGroupRelativeLayout profile = new NameCardGroupRelativeLayout(this.getContext(), this.entries.get(i));
			
			profile.setPadding(0, 0, 0, 0);
			
			llNameCardList.addView(profile,
					new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT, 
							LinearLayout.LayoutParams.WRAP_CONTENT));	

		}
		llNameCardList.invalidate();
	}
}
