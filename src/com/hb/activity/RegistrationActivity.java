package com.hb.activity;

import java.util.HashMap;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hb.R;
import com.hb.client.Constants;
import com.hb.client.HttpRequestType;
import com.hb.enums.RegistrationCode;
import com.hb.network.LKAsyncHttpResponseHandler;
import com.hb.network.LKHttpRequest;
import com.hb.network.LKHttpRequestQueue;
import com.hb.network.LKHttpRequestQueueDone;
import com.hb.util.PatternUtil;
import com.hb.view.EditTextWithClearView;
import com.hb.view.LKAlertDialog;

public class RegistrationActivity extends BaseActivity implements OnClickListener {
	
	private Button backButton = null;
	private Button completedButton = null;
	private EditTextWithClearView nameView = null;
	private EditTextWithClearView passwordView = null;
	private EditTextWithClearView passwordConfirmView = null;
	private EditTextWithClearView et_idcard = null;
	private EditTextWithClearView et_stu_num = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_registration);

		this.init();
	}

	private void init(){
		backButton = (Button) this.findViewById(R.id.backButton);
		backButton.setOnClickListener(this);
		
		completedButton = (Button) this.findViewById(R.id.completedButton);
		completedButton.setOnClickListener(this);
		
		nameView = (EditTextWithClearView) this.findViewById(R.id.nameText);
		passwordView = (EditTextWithClearView) this.findViewById(R.id.passwordText);
		passwordConfirmView = (EditTextWithClearView) this.findViewById(R.id.passwordConfirmText);
		et_idcard = (EditTextWithClearView) this.findViewById(R.id.et_idcard); 
		et_stu_num = (EditTextWithClearView) this.findViewById(R.id.et_stu_num);
	}
	
	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.backButton:
			this.finish();
			
			break;
			
		case R.id.completedButton:
			this.doRegistration();

//			Intent intent = new Intent (RegistrationActivity.this, ImproveRegistrationActivity.class);
//			RegistrationActivity.this.startActivity(intent);
			break;
		}
	}
	
	public void backAction(){
		this.finish();
	}

	private void doRegistration(){
		if(!this.checkValue()) return;
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("name", nameView.getText());
		paramMap.put("password", passwordView.getText());
		paramMap.put("idCardNo", et_idcard.getText());
		if(!et_stu_num.getText().equals("")){
			paramMap.put("stuNo", et_stu_num.getText());
		}
		
		LKHttpRequest req1 = new LKHttpRequest( HttpRequestType.HTTP_REGISTER, paramMap, getRegisterHandler());
		
		new LKHttpRequestQueue().addHttpRequest(req1)
		.executeQueue("正在注册请稍候...", new LKHttpRequestQueueDone(){

			@Override
			public void onComplete() {
				super.onComplete();
			}
		});	
		
	}
	
	private LKAsyncHttpResponseHandler getRegisterHandler(){
		 return new LKAsyncHttpResponseHandler(){
			@Override
			public void successAction(Object obj) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> respMap = (HashMap<String, String>) obj;
				int returnCode = Integer.parseInt(respMap.get("rc"));
				if (returnCode == RegistrationCode.SUCCESS){
					
					Constants.SESSION_ID = respMap.get("sid");
					RegistrationActivity.this.showDialog(BaseActivity.MODAL_DIALOG, "注册成功");
					
//					LKAlertDialog dialog = new LKAlertDialog(RegistrationActivity.this);
//					dialog.setTitle("提示");
//					dialog.setMessage("注册成功");
//					dialog.setCancelable(false);
//					dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface arg0, int arg1) {
//							arg0.dismiss();
//							
//							Intent intent = new Intent(RegistrationActivity.this, SubmitProfileActivity.class);
//							RegistrationActivity.this.startActivity(intent);
//							
//							RegistrationActivity.this.finish();
//						}
//					});
//					
//					dialog.create().show();

				} else if (returnCode == 0) {
					RegistrationActivity.this.showDialog(BaseActivity.MODAL_DIALOG, "失败，原因未知");
					
				} else if (returnCode == -2) {
					RegistrationActivity.this.showDialog(BaseActivity.MODAL_DIALOG, "该邮箱已被注册");
					
				} else if(returnCode == 2){
					Constants.SESSION_ID = respMap.get("sid");
					Intent intent = new Intent (RegistrationActivity.this, ImproveRegistrationActivity.class);
					RegistrationActivity.this.startActivity(intent);
				}
			}
			 
		 };
	}
	private boolean checkValue(){
		if (nameView.getText().trim().equals("")){
			this.showToast("请输入邮箱");
			return false;
		} else if (!PatternUtil.isValidEmail(nameView.getText().trim())) {
			this.showToast("邮箱格式不合法");
			return false;
		} else if (passwordView.getText().trim().equals("")) {
			this.showToast("请输入密码");
			return false;
		} else if (passwordConfirmView.getText().trim().equals("")) {
			this.showToast("请输入确认密码");
			return false;
		} else if (!passwordView.getText().trim().equals(passwordConfirmView.getText().trim())) {
			this.showToast("两次密码输入不一致");
			return false;
		} else if (et_idcard.getText().trim().equals("")) {
			this.showToast("请输入身份证号码");
			return false;
		} else if (!PatternUtil.isValidIDNum(et_idcard.getText())) {
			this.showToast("身份证号码不合法");
			return false;
		}
		
		return true;
	}
	
}
