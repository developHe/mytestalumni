package com.hb.activity.component;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hb.R;
import com.hb.activity.BaseActivity;
import com.hb.activity.SchoolCardApplyActivity;
import com.hb.activity.SchoolFeedbackApplyActivity;
import com.hb.activity.SchoolFeedbackIntroductionActivity;

public class SchoolFeedbackRelativeLayout extends RelativeLayout {

	private Button applyButton;
	private LinearLayout imageLayout;
	private BaseActivity context;

	public SchoolFeedbackRelativeLayout(Context context) {
		super(context);
		this.context = (BaseActivity) context;

		LayoutInflater.from(context).inflate(R.layout.layout_school_feedback, this, true);

		imageLayout = (LinearLayout) this.findViewById(R.id.rl_school_card_content);
		imageLayout.setOnClickListener(new Listener());

		applyButton = (Button) this.findViewById(R.id.btn_schoolcard_apply);
		applyButton.setOnClickListener(new Listener());
	}

	class Listener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.rl_school_card_content){
				Intent intent = new Intent(SchoolFeedbackRelativeLayout.this.context, SchoolFeedbackIntroductionActivity.class);
				SchoolFeedbackRelativeLayout.this.context.startActivityForResult(intent, 100);
			} else {
				Intent intent = new Intent(SchoolFeedbackRelativeLayout.this.context, SchoolFeedbackApplyActivity.class);
				SchoolFeedbackRelativeLayout.this.context.startActivityForResult(intent, 100);
			}
			
			
		}

	}

}
