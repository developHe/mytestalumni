package com.hb.util;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.widget.ImageView;

public class ImageUtil {
	
	private static String imageURL[] = new String[]{
		"http://www.cnu.edu.cn/download.jsp?attachSeq=15815&filename=20131209144643404.jpg",
		"http://www.cnu.edu.cn/download.jsp?attachSeq=15820&filename=20131209145251129.jpg",
		"http://www.cnu.edu.cn/download.jsp?attachSeq=15823&filename=20131210130505295.jpg",
		"http://www.cnu.edu.cn/download.jsp?attachSeq=15362&filename=20131031155715804.jpg",
		"http://www.cnu.edu.cn/download.jsp?attachSeq=15368&filename=20131101090654481.jpg",
		"http://www.cnu.edu.cn/download.jsp?attachSeq=15257&filename=20131021165222134.jpg"
		};
	
	public static String getTestImageURL1(){
		int random = (int)(Math.random() * imageURL.length);
		return imageURL[random];
	}
	
	// 取测试数据
	public static String[] getTestImageURL2(){
		int random1 = (int)(Math.random() * imageURL.length);
		int random2 = (int)(Math.random() * imageURL.length);
		return new String[]{imageURL[random1], imageURL[random2]};
	}
	
	public static String[] getTopMediaImageList(){
		return new String[]{"http://www.cnu.edu.cn/download.jsp?attachSeq=15820&filename=20131209145251129.jpg",
				"http://www.cnu.edu.cn/download.jsp?attachSeq=15781&filename=20131205161653892.jpg", 
				"http://www.cnu.edu.cn/download.jsp?attachSeq=15780&filename=20131205161646324.jpg"};
	}
	
	// http://my.oschina.net/u/143926/blog/137637
	public static void loadImage(int resid, String url, ImageView imageView){
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageOnLoading(resid)
		.showImageForEmptyUri(resid)
		.showImageOnFail(resid)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.bitmapConfig(Bitmap.Config.ARGB_8888)
		.build();
		
		ImageLoader.getInstance().displayImage(url, imageView, options);
	}
	
	/**
	 * 对指定的图片进行圆角处理
	 * 
	 * @param bitmap
	 *            要圆角处理的图片的bitmap信息
	 * @return 圆角之后的图片的bitmap信息
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		try {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()));
			final float roundPx = 4;
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(Color.BLACK);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			final Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			canvas.drawBitmap(bitmap, src, rect, paint);
			return output;
		} catch (Exception e) {
			return bitmap;
		}
	}

	/**
	 * 对指定的图片进行旋转处理
	 * 
	 * @param bitmap
	 *            要旋转的图片的bitmap信息
	 * @return 旋转之后的图片的bitmap信息
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix mtx = new Matrix();
		mtx.postRotate(90);// Setting post rotate to 90
		return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
	}

}
