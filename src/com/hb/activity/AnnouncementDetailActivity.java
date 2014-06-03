package com.hb.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.hb.R;
import com.hb.model.AnnouncementModel;

public class AnnouncementDetailActivity extends AbsSubActivity implements OnClickListener {
	
	private Button backButton = null;
	
	private TextView titleView = null;
	private TextView timeView = null;
	private TextView contentView = null;
	
	private AnnouncementModel model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.activity_announcement_detail);
		
		backButton = (Button) this.findViewById(R.id.backButton);
		backButton.setOnClickListener(this);
		
		
		titleView = (TextView) this.findViewById(R.id.title);
		timeView = (TextView) this.findViewById(R.id.time);
		contentView = (TextView) this.findViewById(R.id.content);
		//contentView.setMovementMethod(ScrollingMovementMethod.getInstance()); // 实现文本可滚动
		
		model = (AnnouncementModel) this.getIntent().getSerializableExtra("MODEL");
		titleView.setText(model.getTitle());
		timeView.setText(model.getTime());
		contentView.setText(model.getContent());
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.backButton:
			this.goback();
			break;
		}
	}

}
