package com.hb.activity.component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.hb.R;
import com.hb.activity.BaseActivity;
import com.hb.activity.ImageDetailsActivity;
import com.hb.activity.PhotoWallActivity;
import com.hb.client.ApplicationEnvironment;
import com.hb.client.HttpRequestType;
import com.hb.enums.LoginCode;
import com.hb.model.ImageModel;
import com.hb.network.LKAsyncHttpResponseHandler;
import com.hb.network.LKHttpRequest;
import com.hb.network.LKHttpRequestQueue;
import com.hb.network.LKHttpRequestQueueDone;
import com.hb.util.ImageUtil;

public class SchoolPhotoWallRelativeLayout extends RelativeLayout {

	private Button applyButton;
	private BaseActivity context;

	private Gallery mGallery = null;

	private ArrayList<ImageModel> imageModelList = null;

	public SchoolPhotoWallRelativeLayout(Context context) {
		super(context);
		this.context = (BaseActivity) context;

		LayoutInflater.from(context).inflate(R.layout.layout_school_photowall, this, true);

		applyButton = (Button) this.findViewById(R.id.btn_schoolcard_apply);
		applyButton.setOnClickListener(new ClickListener());
		
		
		// 开始的时候不显示该控件
		this.setVisibility(View.GONE);

		refreshImage();
	}

	private class GalleryAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (null == convertView) {
				convertView = new MyImageView(SchoolPhotoWallRelativeLayout.this.context);
				int width = ApplicationEnvironment.getInstance().getPixels().widthPixels;
				convertView.setLayoutParams(new Gallery.LayoutParams(width / 3, width / 2));
			}

			ImageView imageView = (ImageView) convertView;
			imageView.setScaleType(ScaleType.FIT_XY);
			String strUrl = imageModelList.get(position % imageModelList.size()).getThumbnail();
//			URL url = null;
//			try {
//				url = new URL(imageModelList.get(position % imageModelList.size()).getThumbnail());
//				Bitmap bmp = null;
//				try {
//					bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//
//				imageView.setImageBitmap(bmp);
//			} catch (MalformedURLException e) {
//				e.printStackTrace();
//			}
			ImageUtil.loadImage(R.drawable.img_weibo_item_pic_loading, strUrl, imageView);
			return imageView;
		}
	}

	private class MyImageView extends ImageView {
		public MyImageView(Context context) {
			this(context, null);
		}

		public MyImageView(Context context, AttributeSet attrs) {
			super(context, attrs, 0);
		}

		public MyImageView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
		}

		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
		}
	}

	class ClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.btn_schoolcard_apply) {
				Intent intent = new Intent(SchoolPhotoWallRelativeLayout.this.context, PhotoWallActivity.class);
				SchoolPhotoWallRelativeLayout.this.context.startActivityForResult(intent, 100);
			}

		}

	}

	private void refreshImage() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("page", "1");
		paramMap.put("num", "6");

		LKHttpRequest req1 = new LKHttpRequest(HttpRequestType.HTTP_GALARY_LIST, paramMap, getImagesHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在获取图片请稍候...", new LKHttpRequestQueueDone() {

			@Override
			public void onComplete() {
				super.onComplete();
			}
		});
	}

	private LKAsyncHttpResponseHandler getImagesHandler() {
		return new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> respMap = (HashMap<String, Object>) obj;

				int returnCode = (Integer) respMap.get("rc");
				if (returnCode == LoginCode.SUCCESS) {
					imageModelList = (ArrayList<ImageModel>) (respMap.get("list"));

					mGallery = (Gallery) context.findViewById(R.id.gallery);
					mGallery.setBackgroundColor(Color.GRAY);
					mGallery.setSpacing(10);
					mGallery.setFadingEdgeLength(0);
					mGallery.setGravity(Gravity.CENTER_VERTICAL);
					mGallery.setAdapter(new GalleryAdapter());
					mGallery.setSelection(Integer.MAX_VALUE / 2);
					mGallery.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
							getImageDetail(imageModelList.get(arg2%imageModelList.size()).getId());
						}
					});
					
				// 得到数据后再显示该控件。
				setVisibility(View.VISIBLE);
				}

			};
		};
	}

	private void getImageDetail(String id) {
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getImageDetailRequest(id));
		queue.executeQueue("正在加载...", new LKHttpRequestQueueDone());
	}

	private LKHttpRequest getImageDetailRequest(String id) {
		return new LKHttpRequest(HttpRequestType.HTTP_GALARY_DETAIL, null, new LKAsyncHttpResponseHandler() {

			@Override
			public void successAction(Object obj) {
				@SuppressWarnings("unchecked")
				String pic = ((HashMap<String, String>) obj).get("pic");

				Intent intent = new Intent(getContext(), ImageDetailsActivity.class);
				intent.putExtra("pic", pic);
				getContext().startActivity(intent);

			}
		}, id) {

		};
	}

}
