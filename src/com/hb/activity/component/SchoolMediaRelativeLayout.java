package com.hb.activity.component;

import java.util.ArrayList;

import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hb.R;
import com.hb.activity.BaseActivity;
import com.hb.activity.SchoolMediaDetailActivity;
import com.hb.client.HttpRequestType;
import com.hb.model.MediaModel;
import com.hb.network.LKAsyncHttpResponseHandler;
import com.hb.network.LKHttpRequest;
import com.hb.network.LKHttpRequestQueue;
import com.hb.network.LKHttpRequestQueueDone;
import com.hb.util.ImageUtil;

public class SchoolMediaRelativeLayout extends RelativeLayout {

	private TextView titleView;
	private Button moreButton;

	private LinearLayout bodyLayout;

	private BaseActivity context;
	private String title;
	private OnClickListener moreListener;

	public SchoolMediaRelativeLayout(BaseActivity context, String title, OnClickListener moreListener) {
		super(context);
		this.context = context;
		this.title = title;
		this.moreListener = moreListener;

		this.init();
	}

	private void init() {
		LayoutInflater.from(context).inflate(R.layout.layout_school_media, this, true);
		bodyLayout = (LinearLayout) this.findViewById(R.id.bodyLayout);

		titleView = (TextView) this.findViewById(R.id.titleView);
		titleView.setText(title);

		moreButton = (Button) this.findViewById(R.id.moreButton);
		moreButton.setOnClickListener(moreListener);
	}

	public void refresh(ArrayList<MediaModel> mediaList) {
		this.setVisibility(View.VISIBLE);

		for (int i = 0; i < mediaList.size(); i++) {

			MediaLayout layout = new MediaLayout(this.context, mediaList.get(i));

			layout.setPadding(0, 0, 0, 0);

			bodyLayout.addView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

		}
	}
	
	class MediaLayout extends LinearLayout implements View.OnClickListener {

		public BaseActivity context;

		private LinearLayout rootLayout;
		private ImageView imageView;
		private TextView titleView;
		private TextView contentView;

		private MediaModel media;

		public MediaLayout(BaseActivity context) {
			super(context);
			this.context = context;

			this.init();
		}

		public MediaLayout(BaseActivity context, AttributeSet attrs) {
			super(context, attrs);
			this.context = context;

			this.init();
		}

		public MediaLayout(BaseActivity context, MediaModel media) {
			super(context);
			this.context = context;
			this.media = media;

			this.init();
		}

		private void init() {
			LayoutInflater.from(this.context).inflate(R.layout.layout_media_card, this, true);

			rootLayout = (LinearLayout) this.findViewById(R.id.rootLayout);
			rootLayout.setOnClickListener(this);

			imageView = (ImageView) this.findViewById(R.id.imageView);
			ImageUtil.loadImage(R.drawable.img_weibo_item_pic_loading, media.getPic(), imageView);

			titleView = (TextView) this.findViewById(R.id.titleView);
			titleView.setText(media.getTitle());

			contentView = (TextView) this.findViewById(R.id.contentView);
			contentView.setText(media.getContent());

		}

		@Override
		public void onClick(View v) {
			LKHttpRequestQueue queue = new LKHttpRequestQueue();
			queue.addHttpRequest(getMediaDetailRequest());
			queue.executeQueue("正在查询请稍候...", new LKHttpRequestQueueDone());
		}

		private LKHttpRequest getMediaDetailRequest() {
			return new LKHttpRequest(HttpRequestType.HTTP_MEDIA_DETAIL, null, new LKAsyncHttpResponseHandler() {

				@Override
				public void successAction(Object obj) {
					Intent intent = new Intent(MediaLayout.this.context, SchoolMediaDetailActivity.class);
					intent.putExtra("MODEL", (MediaModel) obj);
					intent.putExtra("TITLE", title);
					MediaLayout.this.context.startActivityForResult(intent, 100);
				}
			}, media.getId()) {

			};
		}
	}

}
