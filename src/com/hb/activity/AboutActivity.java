package com.hb.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hb.R;

public class AboutActivity extends AbsSubActivity implements OnClickListener {

	private Button backButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_about);

		backButton = (Button) this.findViewById(R.id.backButton);
		backButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.backButton:
			this.goback();
			break;
		}
	}

}
