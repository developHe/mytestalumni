package com.hb.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hb.R;
import com.hb.client.Constants;
import com.hb.client.HttpRequestType;
import com.hb.model.MediaModel;
import com.hb.network.LKAsyncHttpResponseHandler;
import com.hb.network.LKHttpRequest;
import com.hb.network.LKHttpRequestQueue;
import com.hb.network.LKHttpRequestQueueDone;
import com.hb.util.ImageUtil;
import com.hb.util.ListViewUtil;
import com.hb.view.SlideImageLayout;

public class SchoolMediaListActivity extends AbsSubActivity implements OnClickListener {
	// 滑动图片的集合
	private ArrayList<View> mImagePageViewList = new ArrayList<View>();
	private ViewGroup mMainView = null;
	private ViewPager mViewPager = null;

	// 包含圆点图片的View
	private ViewGroup mImageCircleView = null;
	private ImageView[] mImageCircleViews = null;
	// 滑动标题
	private TextView mSlideTitle = null;

	// 布局设置类
	private SlideImageLayout mSlideLayout = null;
	private LinearLayout linearlayout_images_slide = null;
	private LinearLayout layout_middle = null;

	private ListView mediaListView = null;
	private MediaAdapter mediaAdapter = null;

	private TextView titleView = null;
	private Button backButton = null;

	private int totalPage = 1;
	private int currentPage = 1;

	private int totalCount = 0;
	private ArrayList<MediaModel> topMediaList = new ArrayList<MediaModel>();
	private ArrayList<MediaModel> mediaList = new ArrayList<MediaModel>();

	private ArrayList<String> slideImages = new ArrayList<String>();
	private ArrayList<String> slideTitles = new ArrayList<String>();

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = this.getIntent();
		totalCount = intent.getIntExtra("TOTAL", 0);
		totalPage = (totalCount + Constants.PAGESIZE - 1) / Constants.PAGESIZE;

		topMediaList = (ArrayList<MediaModel>) intent.getSerializableExtra("TOPLIST");
		mediaList = (ArrayList<MediaModel>) intent.getSerializableExtra("LIST");

		for (MediaModel model : this.topMediaList) {
			slideImages.add(model.getPic());
			slideTitles.add(model.getTitle());
		}

		initeViews();
	}

	private void initeViews() {
		// 滑动图片区域
		LayoutInflater inflater = getLayoutInflater();
		mMainView = (ViewGroup) inflater.inflate(R.layout.activity_topic_news, null);
		mViewPager = (ViewPager) mMainView.findViewById(R.id.image_slide_page);

		linearlayout_images_slide = (LinearLayout)mMainView.findViewById(R.id.linearlayout_images_slide);
		layout_middle = (LinearLayout)mMainView.findViewById(R.id.layout_middle);
		titleView = (TextView) mMainView.findViewById(R.id.titleView);
		titleView.setText("信息列表");

		backButton = (Button) mMainView.findViewById(R.id.backButton);
		backButton.setOnClickListener(this);

		// 圆点图片区域
		mImageCircleViews = new ImageView[this.slideImages.size()];
		mImageCircleView = (ViewGroup) mMainView.findViewById(R.id.layout_circle_images);
		mSlideLayout = new SlideImageLayout(SchoolMediaListActivity.this);
		mSlideLayout.setCircleImageLayout(this.slideImages.size());

		for (int i = 0; i < this.slideImages.size(); i++) {
			mImagePageViewList.add(mSlideLayout.getSlideImageLayout(this.topMediaList.get(i), this.topMediaList, i));
			mImageCircleViews[i] = mSlideLayout.getCircleImageLayout(i);
			mImageCircleView.addView(mSlideLayout.getLinearLayout(mImageCircleViews[i], 10, 10));
		}

		// 设置默认的滑动标题
		mSlideTitle = (TextView) mMainView.findViewById(R.id.tvSlideTitle);
		if (this.slideTitles.size() != 0) {
			mSlideTitle.setText(this.slideTitles.get(0));	
		}
		mSlideTitle.setSingleLine(true);

		if (topMediaList.size() == 0) {
			layout_middle.setVisibility(View.GONE);
			linearlayout_images_slide.setVisibility(View.GONE);
		}
		setContentView(mMainView);

		// 设置ViewPager
		mViewPager.setAdapter(new SlideImageAdapter());
		mViewPager.setOnPageChangeListener(new ImagePageChangeListener());

		mediaListView = (ListView) this.findViewById(R.id.lv_news);
		mediaAdapter = new MediaAdapter(this);
		mediaListView.setAdapter(mediaAdapter);
		ListViewUtil.setListViewHeightBasedOnChildren(mediaListView);
		mediaListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				getMediaDetail(mediaList.get(arg2));
			}

		});
	}

	
	
	// 滑动图片数据适配器
	private class SlideImageAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return mImagePageViewList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View view, int arg1, Object arg2) {
			((ViewPager) view).removeView(mImagePageViewList.get(arg1));
		}

		@Override
		public Object instantiateItem(View view, int position) {
			((ViewPager) view).addView(mImagePageViewList.get(position));

			return mImagePageViewList.get(position);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

		@Override
		public void finishUpdate(View arg0) {
		}
	}

	// 滑动页面更改事件监听器
	private class ImagePageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int index) {
			mSlideTitle.setText(SchoolMediaListActivity.this.slideTitles.get(index));

			for (int i = 0; i < mImageCircleViews.length; i++) {
				mImageCircleViews[index].setBackgroundResource(R.drawable.dot_selected);

				if (index != i) {
					mImageCircleViews[i].setBackgroundResource(R.drawable.dot_none);
				}

			}
		}
	}

	// 普通新闻列表Adapter
	public final class MediaViewHolder {
		public RelativeLayout contentLayout;
		public RelativeLayout moreLayout;

		public TextView titleView;
		public TextView contentView;
		public TextView tv_time;
		public ImageView imageView;
		public Button moreButton;
	}

	public class MediaAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public MediaAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			if (currentPage < totalPage) {
				return mediaList.size() + 1;
			} else {
				return mediaList.size();
			}
		}

		public Object getItem(int arg0) {
			return mediaList.get(arg0);
		}

		public long getItemId(int arg0) {
			return arg0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			MediaViewHolder holder = null;
			if (null == convertView) {
				holder = new MediaViewHolder();

				convertView = mInflater.inflate(R.layout.listview_item_media, null);

				holder.contentLayout = (RelativeLayout) convertView.findViewById(R.id.contentLayout);
				holder.moreLayout = (RelativeLayout) convertView.findViewById(R.id.moreLayout);

				holder.titleView = (TextView) convertView.findViewById(R.id.titleView);
				holder.contentView = (TextView) convertView.findViewById(R.id.contentView);
				holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
				holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
				holder.moreButton = (Button) convertView.findViewById(R.id.moreButton);
				holder.moreButton.setOnClickListener(SchoolMediaListActivity.this);

				convertView.setTag(holder);
			} else {
				holder = (MediaViewHolder) convertView.getTag();
			}

			if (currentPage < totalPage) {
				if (position == mediaList.size()) {
					holder.contentLayout.setVisibility(View.GONE);
					holder.moreLayout.setVisibility(View.VISIBLE);
				} else {
					holder.contentLayout.setVisibility(View.VISIBLE);
					holder.moreLayout.setVisibility(View.GONE);

					holder.titleView.setText(mediaList.get(position).getTitle());
					holder.contentView.setText(mediaList.get(position).getContent());
					holder.tv_time.setText(mediaList.get(position).getTime());
					ImageUtil.loadImage(R.drawable.img_weibo_item_pic_loading, mediaList.get(position).getPic(), holder.imageView);
				}
			} else {
				holder.contentLayout.setVisibility(View.VISIBLE);
				holder.moreLayout.setVisibility(View.GONE);

				holder.titleView.setText(mediaList.get(position).getTitle());
				holder.contentView.setText(mediaList.get(position).getContent());
				holder.tv_time.setText(mediaList.get(position).getTime());
				ImageUtil.loadImage(R.drawable.img_weibo_item_pic_loading, mediaList.get(position).getPic(), holder.imageView);
			}
			
			// TODO 不分页
			holder.moreLayout.setVisibility(View.GONE);

			return convertView;
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.backButton:
			this.goback();
			break;

		case R.id.moreButton:
			Toast.makeText(this, "----", Toast.LENGTH_SHORT).show();
			break;
		}
	}

	private void getMediaDetail(MediaModel media) {
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getMediaDetailRequest(media));
		queue.executeQueue("正在查询请稍候...", new LKHttpRequestQueueDone());
	}

	private LKHttpRequest getMediaDetailRequest(MediaModel media) {
		return new LKHttpRequest(HttpRequestType.HTTP_MEDIA_DETAIL, null, new LKAsyncHttpResponseHandler() {

			@Override
			public void successAction(Object obj) {
				Intent intent = new Intent(SchoolMediaListActivity.this, SchoolMediaDetailActivity.class);
				intent.putExtra("MODEL", (MediaModel) obj);
				SchoolMediaListActivity.this.startActivityForResult(intent, 100);
			}
		}, media.getId()) {

		};
	}

}
