package com.hb.activity;

import java.util.HashMap;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.hb.R;
import com.hb.client.HttpRequestType;
import com.hb.network.LKAsyncHttpResponseHandler;
import com.hb.network.LKHttpRequest;
import com.hb.network.LKHttpRequestQueue;
import com.hb.network.LKHttpRequestQueueDone;

public class CreateCircleActivity extends AbsSubActivity implements OnClickListener {
	private EditText et_circle_name = null;
	private EditText et_circle_content = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_create_circle);

		Button btn_back = (Button) this.findViewById(R.id.backButton);
		btn_back.setOnClickListener(this);
		
		Button btn_create = (Button)this.findViewById(R.id.btn_create);
		btn_create.setOnClickListener(this);
		et_circle_name = (EditText)this.findViewById(R.id.et_circle_name);
		et_circle_content = (EditText)this.findViewById(R.id.et_circle_content);
	}

	
	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.backButton:
			this.gobackWithResult(1, this.getIntent());
			break;
			
		case R.id.btn_create:
			if(checkValue()){
				createCircle();				
			}
			break;
		}
	}
	
	public void backAction() {
		this.gobackWithResult(1, this.getIntent());
	}

	private void createCircle(){
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getCreateRequest());
		queue.executeQueue("正在请求数据...", new LKHttpRequestQueueDone());
	}
	
	private Boolean checkValue(){
		if(et_circle_name.length() == 0){
			this.showToast("圈子名称不能为空！");
			return false;
		}else if(et_circle_content.length() == 0){
			this.showToast("圈子内容不能为空！");
			return false;
		}
		return true;
	}
	
	// 查看所有圈子列表
	private LKHttpRequest getCreateRequest() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("name", et_circle_name.getText().toString());
		paramMap.put("desc", et_circle_content.getText().toString());
		LKHttpRequest request = new LKHttpRequest(HttpRequestType.HTTP_GROUP_CREATE, paramMap, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				if (null != obj) {
					if(((Integer)obj) == 1){
						CreateCircleActivity.this.showToast("圈子创建成功！");
						CreateCircleActivity.this.gobackWithResult(1, CreateCircleActivity.this.getIntent());
					}else{
						CreateCircleActivity.this.showToast("圈子创建失败！");
					}
					
				} else {
					
				}

			}
		}, null);

		return request;
	}
}
