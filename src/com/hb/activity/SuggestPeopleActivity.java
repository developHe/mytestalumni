package com.hb.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hb.R;
import com.hb.activity.component.SwitchableScrollViewer;
import com.hb.model.ProfileModel;

public class SuggestPeopleActivity extends AbsSubActivity implements OnClickListener{
	
	private ArrayList<ProfileModel> entries = null;
	
	private Button backButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_suggest_people);
		
		backButton = (Button) this.findViewById(R.id.backButton);
		backButton.setOnClickListener(this);

		this.init();
		
	}

	@SuppressWarnings("unchecked")
	private void init(){
		
		Intent intent = this.getIntent();
		entries = (ArrayList<ProfileModel>) intent.getSerializableExtra("PROFILEMODELLIST");
		this.prepareSampleData();
		
		SwitchableScrollViewer svSuggestList = new SwitchableScrollViewer(this, this.entries, "相关推荐", false, true);
		
		LinearLayout llListContainer = (LinearLayout)this.findViewById(R.id.ll_suggest_people);
		llListContainer.removeAllViews();
		
		llListContainer.addView(svSuggestList);
	}
	
	private void prepareSampleData(){

	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.backButton:
			this.goback();
			break;
		}
		
	}
}
