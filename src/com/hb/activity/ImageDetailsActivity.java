package com.hb.activity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hb.R;
import com.hb.util.FileUtil;
import com.hb.view.ZoomImageView;

/**
 * 查看大图的Activity界面。
 * 
 * @author guolin
 */
public class ImageDetailsActivity extends Activity implements OnClickListener {
	/**
	 * 自定义的ImageView控制，可对图片进行多点触控缩放和拖动
	 */
	private ZoomImageView zoomImageView;

	/**
	 * 待展示的图片
	 */
	private static final String params = "http://upload.wikimedia.org/wikipedia/commons/thumb/e/ea/Hukou_Waterfall.jpg/800px-Hukou_Waterfall.jpg";
	private ProgressBar progress;
	private ProgressDialog dialog = null;
	private Bitmap bitmap = null;
	private ImageLoader imageLoader;
	private String imagePath;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_image_details);
		zoomImageView = (ZoomImageView) findViewById(R.id.zoom_image_view);
		// 取出图片路径，并解析成Bitmap对象，然后在ZoomImageView中显示
		zoomImageView.content = this;
		imagePath = getIntent().getStringExtra("pic");
		
		downloadImage(imagePath);
		
		Button btn_back = (Button) this.findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);

		Button btn_download = (Button) this.findViewById(R.id.btn_download);
		btn_download.setOnClickListener(this);
		btn_download.setVisibility(View.GONE);

		progress = (ProgressBar) this.findViewById(R.id.progress);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 记得将Bitmap对象回收掉
		if (bitmap != null) {
			bitmap.recycle();
		}
	}
	
	public void backAction() {
		this.finish();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.btn_download:
			dialog = ProgressDialog.show(this, "", "下载数据，请稍候...", true, true);
			// 启动一个后台线程
			handler.post(new Runnable() {
				@Override
				public void run() {
					// 这里下载数据
					try {
						URL url = new URL(params);
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setDoInput(true);
						conn.connect();
						InputStream inputStream = conn.getInputStream();
						bitmap = BitmapFactory.decodeStream(inputStream);
						Message msg = new Message();
						msg.what = 1;
						handler.sendMessage(msg);

					} catch (MalformedURLException e1) {
						e1.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			break;

		default:
			break;
		}

	}

	/** 这里重写handleMessage方法，接受到子线程数据后更新UI **/
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				// 关闭
				try {
					ImageDetailsActivity.this.saveMyBitmap("bitmap");
				} catch (IOException e) {
					e.printStackTrace();
				}
				dialog.dismiss();
				break;
			}
		}
	};

	// 附件及所有文件都存放在本目录下
	public static String getDownloadPath() {
		// 其它程序无法访问
		// String path =
		// ApplicationEnvironment.getInstance().getApplication().getFilesDir().getPath()+"/download/";
		String path = Environment.getExternalStorageDirectory() + "/LinkedApp/Images/";
		File file = new File(path);
		if (!file.exists()) {
			// file.mkdir();
			// creating missing parent directories if necessary
			file.mkdirs();
		}

		return path;
	}

	public void saveMyBitmap(String bitName) throws IOException {

		try {

			ContentResolver cr = this.getContentResolver();

			String url = MediaStore.Images.Media.insertImage(cr, bitName, "filnale", "");

			Toast.makeText(this, "保存成功!", Toast.LENGTH_SHORT).show();

		} catch (Exception e) {

			e.printStackTrace();

		}


	}
	
	/**
	 * 将图片下载到SD卡缓存起来。
	 * 
	 * @param imageUrl
	 *            图片的URL地址。
	 */
	private void downloadImage(String imageUrl) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			Log.d("TAG", "monted sdcard");
		} else {
			Log.d("TAG", "has no sdcard");
		}
		HttpURLConnection con = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		File imageFile = null;
		try {
			URL url = new URL(imageUrl);
			con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(5 * 1000);
			con.setReadTimeout(15 * 1000);
			con.setDoInput(true);
			con.setDoOutput(true);
			bis = new BufferedInputStream(con.getInputStream());
			imageFile = new File(getImagePath(imageUrl));
			fos = new FileOutputStream(imageFile);
			bos = new BufferedOutputStream(fos);
			byte[] b = new byte[1024];
			int length;
			while ((length = bis.read(b)) != -1) {
				bos.write(b, 0, length);
				bos.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}
				if (bos != null) {
					bos.close();
				}
				if (con != null) {
					con.disconnect();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (imageFile != null) {
				Bitmap imageBitmap = null;
				if (imageBitmap == null) {
					imageBitmap = loadImage(imagePath);
				}
				zoomImageView.setImageBitmap(imageBitmap);
		}
	}

	/**
	 * 根据传入的URL，对图片进行加载。如果这张图片已经存在于SD卡中，则直接从SD卡里读取，否则就从网络上下载。
	 * 
	 * @param imageUrl
	 *            图片的URL地址
	 * @return 加载到内存的图片。
	 */
	private Bitmap loadImage(String imageUrl) {
		File imageFile = new File(getImagePath(imageUrl));
		if (!imageFile.exists()) {
			downloadImage(imageUrl);
		}
		if (imageUrl != null) {
			Bitmap bitmap = ImageLoader.decodeSampledBitmapFromResource(imageFile.getPath(), 400);
			if (bitmap != null) {
				return bitmap;
			}
		}
		return null;
	}
	/**
	 * 获取图片的本地存储路径。
	 * 
	 * @param imageUrl
	 *            图片的URL地址。
	 * @return 图片的本地存储路径。
	 */
	private String getImagePath(String imageUrl) {
		int lastSlashIndex = imageUrl.lastIndexOf("/");
		String imageName = imageUrl.substring(lastSlashIndex + 1)+".jpg";
		String imageDir = FileUtil.getDownloadPath();
		
		String imagePath = imageDir + imageName;
		return imagePath;
	}

}
