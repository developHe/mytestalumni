package com.hb.activity.component;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hb.R;
import com.hb.activity.AnnouncementListActivity;
import com.hb.activity.BaseActivity;
import com.hb.activity.LoginActivity;
import com.hb.activity.PersonInfoModifyActivity;
import com.hb.activity.SubmitProfileActivity;
import com.hb.model.AnnouncementModel;
import com.hb.model.ProfileModel;
import com.hb.util.ImageUtil;

public class NameCardMainRelativeLayout extends RelativeLayout {

	private Context mContext = null;
	
	private ProfileModel data = null;
	private RelativeLayout topLayout = null;
	public String mIdentity = "he";// 个人还是他人
	
	public NameCardMainRelativeLayout(Context context){
		super(context);
		
		this.mContext = context;
		this.init();
	}
	
	public NameCardMainRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.mContext = context;
		this.init();
	}
	
	public NameCardMainRelativeLayout(Context context, ProfileModel d) {
		super(context);
		
		this.mContext = context;
		this.data = d;
		
        this.init();
	}

	private void init(){
		LayoutInflater.from(this.mContext).inflate(R.layout.layout_name_card_main, this, true);
		
		this.refresh(this.data);
	}
	
	public void refresh(ProfileModel profileModel){
		this.data = profileModel;
		
		if(this.data == null) 
			return;
		
		this.setVisibility(View.VISIBLE);
		
		topLayout = (RelativeLayout)this.findViewById(R.id.topLayout);
		topLayout.setOnClickListener(listener);
		TextView tvName = (TextView)this.findViewById(R.id.tv_name_card_main_name);
		TextView tvGender = (TextView)this.findViewById(R.id.tv_name_card_main_gender);
		TextView tvBrief1 = (TextView)this.findViewById(R.id.tv_name_card_main_brief1);
		TextView tvBrief2 = (TextView)this.findViewById(R.id.tv_name_card_main_brief2);
		ImageView photoImageView = (ImageView) this.findViewById(R.id.iv_name_card_main_photo);
		
		tvName.setText(this.data.getName());
		tvGender.setText(this.data.getGender() == 1? "男":"女");
		
//		if(this.data.isSchool()){
//			tvBrief1.setText(this.data.getSchool());
//			tvBrief2.setText(this.data.getMajor());
//		}else{
//			tvBrief1.setText(this.data.getCompany());
//			tvBrief2.setText(this.data.getPosition());
//		}
		
		tvBrief1.setText(this.data.getOrg());
		tvBrief2.setText(this.data.getTitle());
		
		ImageUtil.loadImage(R.drawable.img_card_head_portrait, this.data.getPic(), photoImageView);
	}

	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			if(arg0.getId() == R.id.topLayout){
				if(mIdentity.equals("me")){
					Intent intent = new Intent(BaseActivity.getTopActivity(), PersonInfoModifyActivity.class);
					intent.putExtra("MODEL", data);
					((BaseActivity) NameCardMainRelativeLayout.this.mContext).startActivityForResult(intent,5);
				}
				
			}
			
		}
	};
}
