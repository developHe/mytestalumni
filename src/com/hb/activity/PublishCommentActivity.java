package com.hb.activity;

import java.util.HashMap;

import android.content.Intent;
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

public class PublishCommentActivity extends AbsSubActivity implements OnClickListener {
	private EditText et_circle_content = null;
	private String group_id = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_publish_comment);

		Intent intent = this.getIntent();
		group_id = intent.getStringExtra("GROUP_ID");
		Button btn_back = (Button) this.findViewById(R.id.backButton);
		btn_back.setOnClickListener(this);
		
		Button btn_create = (Button)this.findViewById(R.id.btn_create);
		btn_create.setOnClickListener(this);
		et_circle_content = (EditText)this.findViewById(R.id.et_circle_content);
	}

	
	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.backButton:
			this.goback();
			break;
			
		case R.id.btn_create:
			if(checkValue()){
				createCircle();				
			}
			break;
		}
	}


	private void createCircle(){
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getPublishCommentRequest());
		queue.executeQueue("正在请求数据...", new LKHttpRequestQueueDone());
	}
	
	private Boolean checkValue(){
		if(et_circle_content.length() == 0){
			this.showToast("评论内容不能为空！");
			return false;
		}
		return true;
	}
	
	// 发布评论
	private LKHttpRequest getPublishCommentRequest() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("content", et_circle_content.getText().toString());
		LKHttpRequest request = new LKHttpRequest(HttpRequestType.HTTP_PUBLISH_COMMENT, paramMap, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				if (null != obj) {
					if(((Integer)obj) == 1){
						PublishCommentActivity.this.showToast("评论成功！");
						PublishCommentActivity.this.gobackWithResult(RESULT_OK, PublishCommentActivity.this.getIntent());
					}else{
						PublishCommentActivity.this.showToast("评论失败！");
					}
					
				} else {
					
				}

			}
		}, group_id);

		return request;
	}
}
