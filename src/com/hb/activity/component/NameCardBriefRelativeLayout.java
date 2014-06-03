package com.hb.activity.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hb.R;
import com.hb.model.ProfileModel;
import com.hb.util.ImageUtil;

public class NameCardBriefRelativeLayout extends RelativeLayout {

	private ProfileModel data = null;

	public NameCardBriefRelativeLayout(final Context context, ProfileModel d) {
		super(context);

		this.data = d;

		LayoutInflater.from(context).inflate(R.layout.layout_name_card_brief, this, true);

		this.init();

		// this.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// Intent intent = new Intent(context, ProfileActivity.class);
		// intent.putExtra("IDENTITY", "he");
		// intent.putExtra("PROFILE", data);
		// context.startActivity(intent);
		// }
		// });
	}

	private void init() {
		if (this.data == null)
			return;

		TextView tvName = (TextView) this.findViewById(R.id.tv_name_card_brief_name);
		tvName.setText(this.data.getName());

		ImageView photoImagView = (ImageView) this.findViewById(R.id.iv_name_card_brief_photo);
		ImageUtil.loadImage(R.drawable.img_card_head_portrait_small, this.data.getPic(), photoImagView);

	}
}
