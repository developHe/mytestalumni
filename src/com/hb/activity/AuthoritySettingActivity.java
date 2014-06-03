package com.hb.activity;

import java.util.HashMap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.hb.R;
import com.hb.client.HttpRequestType;
import com.hb.model.ProfileModel;
import com.hb.network.LKAsyncHttpResponseHandler;
import com.hb.network.LKHttpRequest;
import com.hb.network.LKHttpRequestQueue;
import com.hb.network.LKHttpRequestQueueDone;

public class AuthoritySettingActivity extends AbsSubActivity {
	private ProfileModel model = null;

	private Button btn_back = null;
	private Button btn_confirm = null;

	// qq
	private RadioGroup radiogroup_qq = null;
	private RadioButton radio_qq_yes = null;
	private RadioButton radio_qq_no = null;

	// phone
	private RadioGroup radiogroup_mobile = null;
	private RadioButton radio_mobile_yes = null;
	private RadioButton radio_mobile_no = null;

	// email
	private RadioGroup radiogroup_email = null;
	private RadioButton radio_email_yes = null;
	private RadioButton radio_email_no = null;

	private int b_qq = 1;// 0: 否 1: 是
	private int b_mobile = 1;// 0: 否 1: 是
	private int b_email = 1;// 0: 否 1: 是

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_set_authority);

		this.init();
	}

	private void init() {

		btn_back = (Button) this.findViewById(R.id.btn_back);
		btn_back.setOnClickListener(listener);
		btn_confirm = (Button) this.findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(listener);

		radiogroup_qq = (RadioGroup) this.findViewById(R.id.radiogroup_qq);
		radio_qq_yes = (RadioButton) this.findViewById(R.id.radio_qq_yes);
		radio_qq_no = (RadioButton) this.findViewById(R.id.radio_qq_no);

		// phone group
		radiogroup_mobile = (RadioGroup) this.findViewById(R.id.radiogroup_mobile);
		radio_mobile_yes = (RadioButton) this.findViewById(R.id.radio_mobile_yes);
		radio_mobile_no = (RadioButton) this.findViewById(R.id.radio_mobile_no);

		// email group
		radiogroup_email = (RadioGroup) this
				.findViewById(R.id.radiogroup_email);
		radio_email_yes = (RadioButton) this.findViewById(R.id.radio_email_yes);
		radio_email_no = (RadioButton) this.findViewById(R.id.radio_email_no);

		doAuthority();

	}

	// 查看权限
	private void doAuthority() {

		LKHttpRequest req1 = new LKHttpRequest(
				HttpRequestType.HTTP_PROFILE_ME_CONFIG, null,
				getProfileMeConfigHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue(
				"正在获取数据，请稍候...", new LKHttpRequestQueueDone() {

					@Override
					public void onComplete() {
						super.onComplete();
						
						if (b_qq == 0) {
							radio_qq_no.setChecked(true);
						} else {
							radio_qq_yes.setChecked(true);
						}
						
						if (b_mobile == 0) {
							radio_mobile_no.setChecked(true);
							radio_mobile_yes.setChecked(false);
						} else {
							radio_mobile_no.setChecked(false);
							radio_mobile_yes.setChecked(true);
						}
						
						if (b_email == 0) {
							radio_email_no.setChecked(true);
						} else {
							radio_email_yes.setChecked(true);
						}
						
					}
				});

	}

	private LKAsyncHttpResponseHandler getProfileMeConfigHandler() {
		return new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> respMap = (HashMap<String, String>) obj;
				b_qq = Integer.valueOf(respMap.get("qq"));
				b_email = Integer.valueOf(respMap.get("email"));
				b_mobile = Integer.valueOf(respMap.get("mobile"));

				

			}

		};
	}

	// 修改权限
	private void doUpdate() {
		if(radio_qq_no.isChecked()){
			b_qq = 0;
		}else {
			b_qq = 1;
		}
		if(radio_mobile_no.isChecked()){
			b_mobile = 0;
		}else {
			b_mobile = 1;
		}
		if(radio_email_no.isChecked()){
			b_email = 0;
		}else {
			b_email = 1;
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		map.put("qq", b_qq + "");
		map.put("mobile", b_mobile + "");
		map.put("email", b_email + "");

		LKHttpRequest req1 = new LKHttpRequest(
				HttpRequestType.HTTP_PROFILE_ME_UPDATE, map,
				getProfileMeConfigUpdateHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue(
				"正在提交数据，请稍候...", new LKHttpRequestQueueDone() {

					@Override
					public void onComplete() {
						super.onComplete();
					}
				});

	}

	private LKAsyncHttpResponseHandler getProfileMeConfigUpdateHandler() {
		return new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				@SuppressWarnings("unchecked")
				String returnCode = (String) obj;
				if (returnCode.equals("1")) {

					new AlertDialog.Builder(AuthoritySettingActivity.this)
							.setTitle("提示")
							.setMessage("权限设置成功！")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											Intent it = new Intent();
											AuthoritySettingActivity.this
													.gobackWithResult(5, it);
										}
									}).show();
				}

			}

		};
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_back:
				AuthoritySettingActivity.this.goback();
				break;
			case R.id.btn_confirm:
				doUpdate();
				break;
			default:
				break;
			}

		}
	};

}
