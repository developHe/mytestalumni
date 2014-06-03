package com.hb.activity.component;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hb.R;
import com.hb.activity.BaseActivity;
import com.hb.activity.SchoolCardApplyActivity;
import com.hb.activity.SchoolCardIntroductionActivity;

public class SchoolCardRelativeLayout extends RelativeLayout {
	
	private Button applyButton;
	private TextView statusTextView;
	private BaseActivity context;
	private LinearLayout imageLayout;

	public SchoolCardRelativeLayout(Context context) {
		super(context);
		this.context = (BaseActivity) context;

		LayoutInflater.from(context).inflate(R.layout.layout_school_card, this, true); 
		
		imageLayout = (LinearLayout) this.findViewById(R.id.rl_school_card_content);
		imageLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SchoolCardRelativeLayout.this.context, SchoolCardIntroductionActivity.class);
				SchoolCardRelativeLayout.this.context.startActivityForResult(intent, 100);
			}
		});
        
		statusTextView = (TextView) this.findViewById(R.id.tv_school_card_status);
		statusTextView.setVisibility(View.GONE);
		
		applyButton = (Button) this.findViewById(R.id.btn_schoolcard_apply);
		applyButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(SchoolCardRelativeLayout.this.context, SchoolCardApplyActivity.class);
				SchoolCardRelativeLayout.this.context.startActivityForResult(intent, 100);
			}
		});
		
	}
	
	public void setCardStatus(String text){
		statusTextView.setVisibility(View.VISIBLE);
		applyButton.setVisibility(View.GONE);
		
		statusTextView.setText(text);
	}
	
}
