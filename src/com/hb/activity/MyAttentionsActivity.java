package com.hb.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.R;
import com.hb.activity.component.SwitchableAttentionScrollViewer;
import com.hb.activity.component.SwitchableScrollViewer;
import com.hb.client.Constants;
import com.hb.model.ProfileModel;

public class MyAttentionsActivity extends AbsSubActivity implements OnClickListener{
	
	private ArrayList<ProfileModel> entries = null;
	private String mTitle = null;
	private Button backButton = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myattention_people);

		backButton = (Button) this.findViewById(R.id.backButton);
		backButton.setOnClickListener(this);
		this.init();
		
	}

	@SuppressWarnings("unchecked")
	private void init(){
		
		Intent intent = this.getIntent();
		entries = (ArrayList<ProfileModel>) intent.getSerializableExtra("PROFILEMODELLIST");
		mTitle = (String) intent.getStringExtra("TITLE");
		TextView tv_title = (TextView)this.findViewById(R.id.titleView);
		tv_title.setText(mTitle != null ? mTitle:"相关推荐人");
		String idExtra = (String) intent.getStringExtra("ID");
		int count = intent.getIntExtra("TOTAL", 0);
		SwitchableAttentionScrollViewer svSuggestList = new SwitchableAttentionScrollViewer(this, this.entries, mTitle!=null ? mTitle:"相关推荐", idExtra, count);
		LinearLayout llListContainer = (LinearLayout)this.findViewById(R.id.ll_suggest_people);
		llListContainer.removeAllViews();
		
		llListContainer.addView(svSuggestList);
	}

	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.backButton:
			this.goback();
			break;
		}
		
	}
	
}
