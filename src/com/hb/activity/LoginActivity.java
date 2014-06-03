package com.hb.activity;

import java.util.HashMap;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hb.R;
import com.hb.client.ApplicationEnvironment;
import com.hb.client.Constants;
import com.hb.client.HttpRequestType;
import com.hb.enums.LoginCode;
import com.hb.network.LKAsyncHttpResponseHandler;
import com.hb.network.LKHttpRequest;
import com.hb.network.LKHttpRequestQueue;
import com.hb.network.LKHttpRequestQueueDone;
import com.hb.util.PatternUtil;
import com.hb.view.EditTextWithClearView;

public class LoginActivity extends BaseActivity implements OnClickListener {
	
	private Button completedButton = null;
	private Button registrationButton = null;
	private EditTextWithClearView nameView = null;
	private EditTextWithClearView passwordView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_login);

		this.init();
	}

	private void init(){
		registrationButton = (Button) this.findViewById(R.id.registrationButton);
		registrationButton.setOnClickListener(this);
		
		completedButton = (Button) this.findViewById(R.id.completedButton);
		completedButton.setOnClickListener(this);
		
		nameView = (EditTextWithClearView) this.findViewById(R.id.nameText);
//		nameView.setText("20131014001@qq.com");
		passwordView = (EditTextWithClearView) this.findViewById(R.id.passwordText);
//		passwordView.setText("123");
		
		nameView.setText("");
		passwordView.setText("");
		nameView.setText(ApplicationEnvironment.getInstance().getPreferences().getString(Constants.kUSERNAME, ""));
		passwordView.setText("");
	}
	
	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.registrationButton:
			Intent intent = new Intent(this, RegistrationActivity.class);
			this.startActivity(intent);
			
			break;
			
		case R.id.completedButton:
			this.doLogin();
			
			break;
		}
	}

	private void doLogin(){
		if(!this.checkValue()) return;
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("name", nameView.getText());
		paramMap.put("password", passwordView.getText());
		
		Editor editor = ApplicationEnvironment.getInstance().getPreferences().edit();
		editor.putString(Constants.kUSERNAME, nameView.getText());
		editor.commit();
		
		LKHttpRequest req1 = new LKHttpRequest( HttpRequestType.HTTP_LOGIN, paramMap, getLoginHandler());
		
		new LKHttpRequestQueue().addHttpRequest(req1)
		.executeQueue("正在登录请稍候...", new LKHttpRequestQueueDone(){

			@Override
			public void onComplete() {
				super.onComplete();
			}
		});	
	}
	
	private LKAsyncHttpResponseHandler getLoginHandler(){
		 return new LKAsyncHttpResponseHandler(){
			@Override
			public void successAction(Object obj) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> respMap = (HashMap<String, String>) obj;
				
				int returnCode = Integer.parseInt(respMap.get("rc"));
				if (returnCode == LoginCode.SUCCESS){
					Constants.SESSION_ID = respMap.get("sid");
					
					int statusCode = Integer.parseInt(respMap.get("status"));
					if (statusCode == 1){ // 账号状态正常
						Intent intent = new Intent(LoginActivity.this, MainActivityGroup.class);
						LoginActivity.this.startActivity(intent);
						
					} else if (statusCode == -1) { // 尚未填写基本信息
						Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
						LoginActivity.this.startActivity(intent);
						
					} else if (statusCode == -2) { // 等待审核
						LoginActivity.this.showDialog(LoginActivity.MODAL_DIALOG, "该账号正在审核");
					}

				} else if (returnCode == LoginCode.WRONG_PASSWORD){
					LoginActivity.this.showDialog(BaseActivity.MODAL_DIALOG, "密码错误");
				} else if (returnCode == LoginCode.USER_NOT_EXIST) {
					LoginActivity.this.showDialog(BaseActivity.MODAL_DIALOG, "用户名不存在");
				} else if (returnCode == LoginCode.UNKNOWN_FAILURE){
					LoginActivity.this.showDialog(BaseActivity.MODAL_DIALOG, "登录失败，未知原因");
				}
			}
			 
		 };
	}
	
	private boolean checkValue(){
		if (nameView.getText().trim().equals("")){
			this.showToast("请输入邮箱");
			return false;
		} else if (!PatternUtil.isValidEmail(nameView.getText().trim())) {
			// TODO 先暂时只能为邮箱
			this.showToast("用户名不是合法的邮箱格式");
			return false;
			
		} else if (passwordView.getText().trim().equals("")) {
			this.showToast("请输入密码");
			return false;
		}
		
		return true;
	}

}
