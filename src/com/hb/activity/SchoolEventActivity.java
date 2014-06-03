package com.hb.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.R;
import com.hb.activity.component.NameCardListTableLayout;
import com.hb.model.ActiveModel;
import com.hb.model.ProfileModel;
import com.hb.util.ImageUtil;

public class SchoolEventActivity extends AbsSubActivity implements OnClickListener {
	
	private ImageView posterImage;
	private TextView titleView;
	private TextView previewView;
	private TextView timeView;
	private TextView addressView;
	private TextView typeView;
	private TextView chargeView;
	private TextView sponsorView;
	private TextView contentView;
	
	private LinearLayout profileLayout;
	
	private Button backButton;
	
	private ActiveModel active = null;
	private ArrayList<ProfileModel> profileList = null;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_school_event);
		
		Intent intent = this.getIntent();
		
		active = (ActiveModel) intent.getSerializableExtra("MODEL");
		profileList = (ArrayList<ProfileModel>) intent.getSerializableExtra("LIST");
		
		this.init();
	}
	
	private void init(){
		backButton = (Button) this.findViewById(R.id.backButton);
		backButton.setOnClickListener(this);
		
		posterImage = (ImageView) this.findViewById(R.id.iv_event_poster);
		ImageUtil.loadImage(R.drawable.img_weibo_item_pic_loading, this.active.getPosterImage(), posterImage);
		
		titleView = (TextView) this.findViewById(R.id.tv_event_title);
		titleView.setText(this.active.getTitle());
		
		previewView = (TextView) this.findViewById(R.id.tv_event_preview);
		previewView.setText(this.active.getPreview());
		
		timeView = (TextView) this.findViewById(R.id.tv_event_time);
		timeView.setText(this.active.getStime());
		
		addressView = (TextView) this.findViewById(R.id.tv_event_address);
		addressView.setText(this.active.getAddress());
		
		typeView = (TextView) this.findViewById(R.id.tv_event_type);
		typeView.setText(this.active.getTypeName());
		
		chargeView = (TextView) this.findViewById(R.id.tv_event_charge);
		chargeView.setText(this.active.getCharge());
		
		sponsorView = (TextView) this.findViewById(R.id.tv_event_sponsor);
		sponsorView.setText(this.active.getSponsor());
		
		contentView = (TextView) this.findViewById(R.id.tv_event_content);
		contentView.setText(this.active.getContent());
		
		profileLayout = (LinearLayout) this.findViewById(R.id.profileLayout);
		
		NameCardListTableLayout glNameCards = new NameCardListTableLayout(this, this.profileList);
		profileLayout.addView(glNameCards);
		
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.backButton:
			this.goback();
			break;
		}
	}
	
	public void onBackPressed(){
		this.goback();
	}


}
