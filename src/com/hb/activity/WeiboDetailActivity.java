package com.hb.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.R;
import com.hb.model.WeiboItemModel;
import com.hb.util.ImageUtil;

public class WeiboDetailActivity extends AbsSubActivity {

	/** 用户的头像 */
	private ImageView iv_shakeweiboitem_profile;

	/** 用户的性别 */
	private ImageView iv_shakeweiboitem_gender;

	/** 微博的图片 */
	private ImageView iv_shakeweiboitem_statusImage;

	/** 原微博的图片 */
	private ImageView iv_shakeweiboitem_sourceImage;

	/** 原微博用户的昵称 */
	private TextView tv_shakeweiboitem_screenname;

	/** 用户的所在地 */
	private TextView tv_shakeweiboitem_location;

	/** 微博发布的时间 */
	private TextView tv_shakeweiboitem_time;

	/** 微博的内容 */
	private TextView tv_shakeweiboitem_content;

	/** 微博的来源 */
	private TextView tv_shakeweiboitem_from;

	/** 原微博的内容 */
	private TextView tv_shakeweiboitem_sourceContent;

	/** 原微博的用户的昵称 */
	private TextView tv_shakeweiboitem_sourceName;

	/** 原微博组件 */
	private LinearLayout ll_shakeweiboitem_source;

	/** 微博对象 */
	private WeiboItemModel weiboItem;

	/*
	 * (non-Javadoc)创建界面的回调方法
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weibo_detail);
		
		weiboItem = getIntent().getParcelableExtra("status");
		
		iv_shakeweiboitem_gender = (ImageView) findViewById(R.id.iv_shakeweiboitem_gender);
		iv_shakeweiboitem_profile = (ImageView) findViewById(R.id.iv_shakeweiboitem_profile);
		iv_shakeweiboitem_statusImage = (ImageView) findViewById(R.id.iv_shakeweiboitem_statusImage);
		iv_shakeweiboitem_sourceImage = (ImageView) findViewById(R.id.iv_shakeweiboitem_sourceImage);

		tv_shakeweiboitem_screenname = (TextView) findViewById(R.id.tv_shakeweiboitem_screenname);
		tv_shakeweiboitem_location = (TextView) findViewById(R.id.tv_shakeweiboitem_location);
		tv_shakeweiboitem_time = (TextView) findViewById(R.id.tv_shakeweiboitem_time);
		tv_shakeweiboitem_content = (TextView) findViewById(R.id.tv_shakeweiboitem_content);
		tv_shakeweiboitem_from = (TextView) findViewById(R.id.tv_shakeweiboitem_from);
		tv_shakeweiboitem_sourceName = (TextView) findViewById(R.id.tv_shakeweiboitem_sourceName);
		tv_shakeweiboitem_sourceContent = (TextView) findViewById(R.id.tv_shakeweiboitem_sourceContent);

		ll_shakeweiboitem_source = (LinearLayout) findViewById(R.id.ll_shakeweiboitem_source);

		tv_shakeweiboitem_screenname.setText(weiboItem.getUsername());
		tv_shakeweiboitem_from.setText(weiboItem.getFrom());
		tv_shakeweiboitem_content.setText(weiboItem.getContent());
		tv_shakeweiboitem_location.setText(weiboItem.getLocation());
		tv_shakeweiboitem_time.setText(weiboItem.getTime());
		tv_shakeweiboitem_sourceContent.setText(weiboItem.getSourceContent());
		tv_shakeweiboitem_sourceName.setText("@" + weiboItem.getSourceName());// @ name

		if (weiboItem.getGender().equalsIgnoreCase("f")) {// female
			iv_shakeweiboitem_gender.setVisibility(View.VISIBLE);
			iv_shakeweiboitem_gender.setImageResource(R.drawable.img_weibo_userinfo_female_1);
		} else if (weiboItem.getGender().equalsIgnoreCase("m")) {
			iv_shakeweiboitem_gender.setVisibility(View.VISIBLE);
			iv_shakeweiboitem_gender.setImageResource(R.drawable.img_weibo_userinfo_male_1);
		} else {// not know
			iv_shakeweiboitem_gender.setVisibility(View.GONE);
		}

		if (weiboItem.getProfileImageUrl() != null && !weiboItem.getProfileImageUrl().equalsIgnoreCase("")) {
			ImageUtil.loadImage(R.drawable.img_weibo_userinfo_female_2, weiboItem.getProfileImageUrl(), iv_shakeweiboitem_profile);
		}

		if (weiboItem.getSourceName() == null || "".equalsIgnoreCase(weiboItem.getSourceName())) {// no source
			ll_shakeweiboitem_source.setVisibility(View.GONE);
			if (weiboItem.getStatusImageUrl() != null && !weiboItem.getStatusImageUrl().equalsIgnoreCase("")) {// status
				iv_shakeweiboitem_statusImage.setVisibility(View.VISIBLE);
				ImageUtil.loadImage(R.drawable.img_weibo_item_pic_loading, weiboItem.getStatusImageUrl(), iv_shakeweiboitem_statusImage);
			} else {
				iv_shakeweiboitem_statusImage.setVisibility(View.GONE);
			}
		} else {// have source
			iv_shakeweiboitem_statusImage.setVisibility(View.GONE);
			ll_shakeweiboitem_source.setVisibility(View.VISIBLE);
			if (weiboItem.getSourceImageUrl() != null && !weiboItem.getSourceImageUrl().equalsIgnoreCase("")) {// source
				ImageUtil.loadImage(R.drawable.img_weibo_item_pic_loading, weiboItem.getSourceImageUrl(), iv_shakeweiboitem_sourceImage);
			} else {
				iv_shakeweiboitem_sourceImage.setVisibility(View.GONE);
			}
		}

	}

	public void btn_back(View v) {
		goback();
	}
}