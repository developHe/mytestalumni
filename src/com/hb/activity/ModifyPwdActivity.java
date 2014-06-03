package com.hb.activity;

import java.util.HashMap;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.hb.R;
import com.hb.client.HttpRequestType;
import com.hb.network.LKAsyncHttpResponseHandler;
import com.hb.network.LKHttpRequest;
import com.hb.network.LKHttpRequestQueue;
import com.hb.network.LKHttpRequestQueueDone;
import com.hb.view.EditTextWithClearView;

public class ModifyPwdActivity extends AbsSubActivity implements OnClickListener {
	
	private Button backButton;
	private Button okButton;
	
	private EditTextWithClearView oldPwdText = null;
	private EditTextWithClearView newPwdText1 = null;
	private EditTextWithClearView newPwdText2 = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_modify_pwd);
		
		backButton = (Button) this.findViewById(R.id.backButton);
		backButton.setOnClickListener(this);
		
		okButton = (Button) this.findViewById(R.id.okButton);
		okButton.setOnClickListener(this);
		
		oldPwdText = (EditTextWithClearView) this.findViewById(R.id.oldpwd);
		newPwdText1 = (EditTextWithClearView) this.findViewById(R.id.newpwd1);
		newPwdText2 = (EditTextWithClearView) this.findViewById(R.id.newpwd2);
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.backButton:
			this.goback();
			break;
			
		case R.id.okButton:
			this.submit();
			break;
		}
	}
	
	private boolean checkValue(){
		if (this.oldPwdText.getText().trim().equals("")){
			Toast.makeText(this, "请输入原密码", Toast.LENGTH_SHORT).show();
			return false;
		} else if (this.newPwdText1.getText().trim().equals("")){
			Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
			return false;
		} else if (this.newPwdText2.getText().trim().equals("")){
			Toast.makeText(this, "请再次输入新密码", Toast.LENGTH_SHORT).show();
			return false;
		} else if (!this.newPwdText1.getText().trim().equals(this.newPwdText2.getText().trim())){
			Toast.makeText(this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		return true;
	}
	
	private void submit(){
		if (this.checkValue()){
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("old", this.oldPwdText.getText().trim());
			paramMap.put("new", this.newPwdText1.getText().trim());
			
			LKHttpRequest req1 = new LKHttpRequest( HttpRequestType.HTTP_UPDATEPWD, paramMap, updatePwdHandler(), null);
			
			new LKHttpRequestQueue().addHttpRequest(req1)
			.executeQueue("正在处理请稍候...", new LKHttpRequestQueueDone(){

				@Override
				public void onComplete() {
					super.onComplete();
				}
			});	
		}
	}
	
	private LKAsyncHttpResponseHandler updatePwdHandler(){
		 return new LKAsyncHttpResponseHandler(){
			@Override
			public void successAction(Object obj) {
				clear();
				
				Integer returnCode = (Integer) obj;
				if (returnCode == 1){
					BaseActivity.getTopActivity().showDialog(BaseActivity.MODAL_DIALOG, "密码修改成功，请牢记新密码");
				} else if (returnCode == -1){
					BaseActivity.getTopActivity().showDialog(BaseActivity.MODAL_DIALOG, "原密码错误");
				} else {
					BaseActivity.getTopActivity().showDialog(BaseActivity.MODAL_DIALOG, "密码修改失败，未知错误");
				}
			}
		 };
	}
	
	public void clear(){
		this.oldPwdText.setText("");
		this.newPwdText1.setText("");
		this.newPwdText2.setText("");
	}

	@Override
	public void onBackPressed() {
		this.goback();
	}
	
}
