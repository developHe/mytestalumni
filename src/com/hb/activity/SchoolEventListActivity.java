package com.hb.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hb.R;
import com.hb.activity.component.ActiveItemLinearLayout;
import com.hb.model.ActiveModel;

public class SchoolEventListActivity extends AbsSubActivity implements OnClickListener {

	private LinearLayout bodyLayout = null;
	
	private Button backButton = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.activity_event_list);
		
		bodyLayout = (LinearLayout) this.findViewById(R.id.bodyLayout);
		backButton = (Button) this.findViewById(R.id.backButton);
		backButton.setOnClickListener(this);
		
		@SuppressWarnings("unchecked")
		ArrayList<ActiveModel> list = (ArrayList<ActiveModel>) this.getIntent().getSerializableExtra("LIST");
		
		for (int i = 0; i < list.size(); i++) {

			ActiveItemLinearLayout layout = new ActiveItemLinearLayout(this, list.get(i));
			layout.hideDivider();
			layout.setPadding(0, 0, 0, 5);
			layout.setBackgroundResource(R.drawable.img_weibo_con_border);

			LinearLayout.LayoutParams params =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			params.setMargins(0, 0, 0, 10);
			bodyLayout.addView(layout, params);
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.backButton:
			goback();
			break;
		}
	}
}
