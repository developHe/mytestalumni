package com.hb.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hb.R;
import com.hb.client.ApplicationEnvironment;
import com.hb.client.Constants;
import com.hb.client.DownloadFileRequest;
import com.hb.view.LKAlertDialog;

public class SystemSettingActivity extends AbsSubActivity {
	private ListView listView;
	private BusinessAdapter adapter;

	private String downloadAPKURL = null;

	private Integer[] icons = { R.drawable.img_sys_weibo, R.drawable.img_sys_modify_pwd, R.drawable.icon_authority, R.drawable.img_sys_update, R.drawable.img_sys_about, R.drawable.img_sys_exit };
	private String[] title = { "官方微博", "修改密码", "设置权限", "检查更新", "关于", "退出" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_system_setting);

		listView = (ListView) this.findViewById(R.id.listview);

		adapter = new BusinessAdapter(this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				switch (arg2) {
				case 0: // 官方微博
				{
					Intent intent = new Intent(SystemSettingActivity.this, WeiboListActivity.class);
					SystemSettingActivity.this.startActivityForResult(intent, 0);
				}
					break;

				case 1:// 修改密码
				{
					Intent intent = new Intent(SystemSettingActivity.this, ModifyPwdActivity.class);
					SystemSettingActivity.this.startActivityForResult(intent, 0);
				}
					break;

				case 2: // 权限设置
				{
					Intent intent = new Intent(SystemSettingActivity.this, AuthoritySettingActivity.class);
					SystemSettingActivity.this.startActivityForResult(intent, 0);
				}
					break;
					
				case 3: // 检查更新
				{
					checkUpdate();
				}
					break;
					
				case 4:// 关于
				{
					Intent intent = new Intent(SystemSettingActivity.this, AboutActivity.class);
					SystemSettingActivity.this.startActivityForResult(intent, 0);
					
				}
					break;

				case 5:// 退出
					logout();
					break;

				default:
					break;
				}
			}

		});

	}

	private void checkUpdate() {
		try {
			//this.showDialog(BaseActivity.PROGRESS_HUD, "正在检查更新");
			
			URL myURL = new URL(Constants.DOWNLOADURL);
			URLConnection ucon = myURL.openConnection();
			InputStream is = ucon.getInputStream();

			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(is, "UTF-8");

			int event = parser.getEventType();// 产生第一个事件
			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
				case XmlPullParser.START_TAG:
					if ("version".equals(parser.getName())) {
						this.hideDialog(BaseActivity.PROGRESS_HUD);
						
						int serviceVersion = Integer.parseInt(parser.nextText());
						if (serviceVersion > Constants.VERSION) {
							showUpdateDialog();
						} else {
							showNoUpdateDialog();
						}

					} else if ("apkurl".equals(parser.getName())) {
						downloadAPKURL = parser.nextText();
						// downloadAPKURL =
						// "http://cdn.market.hiapk.com/data/upload/2014/01_26/10/com.yek.lafaso_104638.apk";
					}

					break;
				}

				event = parser.next();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}

	public void logout() {
		LKAlertDialog dialog = new LKAlertDialog(this);
		dialog.setTitle("提示");
		dialog.setMessage("您确定要注销登录吗？");
		dialog.setCancelable(false);
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();

				try {
					while (!(BaseActivity.getTopActivity() instanceof LoginActivity)) {
						BaseActivity.getTopActivity().finish();
					}
				} catch (Exception e) {
					ActivityManager activityMgr = (ActivityManager) SystemSettingActivity.this.getSystemService(ACTIVITY_SERVICE);
					activityMgr.restartPackage(getPackageName());
				}

			}
		});
		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.create().show();
	}

	public final class BusinessHolder {
		private ImageView iv_icon_left;
		private TextView tv_title;
	}

	public class BusinessAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public BusinessAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return title.length;
		}

		public Object getItem(int arg0) {
			return arg0;
		}

		public long getItemId(int arg0) {
			return arg0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			BusinessHolder holder = null;
			if (null == convertView) {
				holder = new BusinessHolder();

				convertView = mInflater.inflate(R.layout.listview_item_sys_setting, null);
				convertView.setTag(holder);
			} else {
				holder = (BusinessHolder) convertView.getTag();
			}
			holder.tv_title = (TextView) convertView.findViewById(R.id.titleView);
			holder.tv_title.setText(title[position]);
			holder.iv_icon_left = (ImageView) convertView.findViewById(R.id.imageView);
			holder.iv_icon_left.setImageResource(icons[position]);
			return convertView;
		}
	}

	private void showUpdateDialog() {
		LKAlertDialog dialog = new LKAlertDialog(this);
		dialog.setTitle("提示");
		dialog.setMessage("有新版本，是否下载更新？");
		dialog.setCancelable(false);
		dialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
				Update();
			}
		});
		dialog.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		dialog.create().show();
	}

	private void showNoUpdateDialog() {
		LKAlertDialog dialog = new LKAlertDialog(this);
		dialog.setTitle("提示");
		dialog.setMessage("当前版本已是最新版本");
		dialog.setCancelable(false);
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
		});
		dialog.create().show();
	}

	private void Update() {
		DownloadFileRequest.sharedInstance().downloadAndOpen(this, downloadAPKURL, "校友会.apk");
	}

	public void backAction() {
		ApplicationEnvironment.getInstance().exitApp();
	}
}
