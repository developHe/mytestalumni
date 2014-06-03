package com.hb.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.hb.R;
import com.hb.client.HttpRequestType;
import com.hb.network.LKAsyncHttpResponseHandler;
import com.hb.network.LKHttpRequest;
import com.hb.network.LKHttpRequestQueue;
import com.hb.network.LKHttpRequestQueueDone;
import com.hb.util.DateUtil;
import com.hb.util.PatternUtil;
import com.hb.view.EditTextWithClearView;
import com.hb.view.LKAlertDialog;

public class SubmitProfileActivity extends BaseActivity implements OnClickListener, OnItemSelectedListener {
	
	private Button backButton = null;
	private Button submitButton = null;
	
	private Spinner idTypeSpinner = null;
	private EditTextWithClearView idTextView = null;
	private EditTextWithClearView nameView = null;
	private EditTextWithClearView majorView = null;
	private EditTextWithClearView classView = null;
	private Spinner genderSpinner = null;
	private Spinner adYearSpinner = null;
	private Spinner gradYearSpinner = null;
	
	private ArrayList<String> idTypeList = new ArrayList<String>();
	
	private ArrayList<String> genderKeyList = new ArrayList<String>();
	private ArrayList<String> genderValueList = new ArrayList<String>();
	
	private ArrayList<String> adYearKeyList = new ArrayList<String>();
	private ArrayList<String> adYearValueList = new ArrayList<String>();
	
	private ArrayList<String> gradYearKeyList = new ArrayList<String>();
	private ArrayList<String> gradYearValueList = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_submit_profile);
		
		backButton = (Button) this.findViewById(R.id.backButton);
		backButton.setOnClickListener(this);
		
		submitButton = (Button) this.findViewById(R.id.submitButton);
		submitButton.setOnClickListener(this);
		
		idTypeSpinner = (Spinner) this.findViewById(R.id.idTypeSpinner);
		idTypeSpinner.setOnItemSelectedListener(this);
		
		idTextView = (EditTextWithClearView) this.findViewById(R.id.idText);
		nameView = (EditTextWithClearView) this.findViewById(R.id.nameText);
		majorView = (EditTextWithClearView) this.findViewById(R.id.majorText);
		classView = (EditTextWithClearView) this.findViewById(R.id.classText);
		genderSpinner = (Spinner) this.findViewById(R.id.genderSpinner);
		adYearSpinner = (Spinner) this.findViewById(R.id.adYearSpinner);
		gradYearSpinner = (Spinner) this.findViewById(R.id.gradYearSpinner);
		
		this.preparData();
		
		ArrayAdapter<String> idTypeAdapter = new ArrayAdapter<String>(this, R.layout.myspinner,  idTypeList);
		idTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		idTypeSpinner.setAdapter(idTypeAdapter);
		idTypeSpinner.setPrompt("请选择");
		
		ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(this, R.layout.myspinner,  genderValueList);
		genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		genderSpinner.setAdapter(genderAdapter);
		genderSpinner.setPrompt("性别");
		
		ArrayAdapter<String> adYearAdapter = new ArrayAdapter<String>(this, R.layout.myspinner,  adYearValueList);
		adYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adYearSpinner.setAdapter(adYearAdapter);
		adYearSpinner.setPrompt("入学年份");
		adYearSpinner.setSelection(adYearValueList.size() - 10);
		
		ArrayAdapter<String> gradYearAdapter = new ArrayAdapter<String>(this, R.layout.myspinner,  gradYearValueList);
		gradYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		gradYearSpinner.setAdapter(gradYearAdapter);
		gradYearSpinner.setPrompt("毕业年份");
		gradYearSpinner.setSelection(gradYearValueList.size() - 9);
	}
	
	private void preparData(){
		idTypeList.add("学号");
		idTypeList.add("身份证号");
		
		genderValueList.add("男");
		genderValueList.add("女");
		
		genderKeyList.add("1");
		genderKeyList.add("0");
		
		for (int i=1950; i<Integer.parseInt(DateUtil.getCurrentYear())+1; i++){
			adYearKeyList.add(i+"");
			adYearValueList.add(i+" 年");
			
			gradYearKeyList.add(i+"");
			gradYearValueList.add(i+" 年");
		}
		
		gradYearKeyList.add(gradYearKeyList.size(), "null");
		gradYearValueList.add(gradYearValueList.size(), "尚未毕业");
	}
	
	private void submitAction(){
		if (!checkValue()) return;
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		if (idTypeSpinner.getSelectedItemPosition() == 0){
			paramMap.put("stuNo", idTextView.getText().trim());
		} else {
			paramMap.put("idCardNo", idTextView.getText().trim());
		}
		
		paramMap.put("name", nameView.getText().trim());
		paramMap.put("major", majorView.getText().trim());
		paramMap.put("class", classView.getText().trim());
		paramMap.put("gender", genderKeyList.get(genderSpinner.getSelectedItemPosition()));
		paramMap.put("adYear", adYearKeyList.get(adYearSpinner.getSelectedItemPosition()));
		paramMap.put("gradYear", gradYearKeyList.get(gradYearSpinner.getSelectedItemPosition()));
		
		LKHttpRequest req = new LKHttpRequest( HttpRequestType.HTTP_PROFILE_MATCH, paramMap, matchProfileHandler());
		
		new LKHttpRequestQueue().addHttpRequest(req)
		.executeQueue("正在处理请稍候...", new LKHttpRequestQueueDone(){

			@Override
			public void onComplete() {
				super.onComplete();
			}
		});
	}
	
	private LKAsyncHttpResponseHandler matchProfileHandler(){
		return new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				int code = (Integer) obj;
				
				if (code == 1){ // 匹配成功 
					Intent intent = new Intent(SubmitProfileActivity.this, SchoolExActivity.class);
					SubmitProfileActivity.this.startActivity(intent);
					SubmitProfileActivity.this.finish();
					
				} else if (code == 2){
					showError("正在审核，请耐心等待");
					
				} else if (code == 0) {
					showError("失败，未知原因");
					
				} else if (code == -2) {
					showError("您已提交个人信息");
				}
			}
		};
	}
	
	private void showError(String msg){
		LKAlertDialog dialog = new LKAlertDialog(this);
		dialog.setTitle("提示");
		dialog.setMessage(msg);
		dialog.setCancelable(false);
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
				
				SubmitProfileActivity.this.finish();
			}
		});
		dialog.create().show();
	}
	
	private boolean checkValue(){
		if (idTextView.getText().trim().equals("")){
			this.showToast(idTypeSpinner.getSelectedItemPosition()==0?"请输入学号":"请输入身份证号");
			return false;
			
		} else if (idTypeSpinner.getSelectedItemPosition() == 1 && !PatternUtil.isValidIDNum(idTextView.getText().trim())) {
			this.showToast("身份证号不合法");
			return false;
			
		} else if (nameView.getText().trim().equals("")){
			this.showToast("请输入姓名");
			return false;
			
		} else if (majorView.getText().trim().equals("")){
			this.showToast("请输入专业名称");
			return false;
			
		} else if (classView.getText().trim().equals("")) {
			this.showToast("请输入班级");
			return false;
		}
		
		return true;
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.backButton:
			this.finish();
			break;
			
		case R.id.submitButton:
			this.submitAction();
			break;
		}
	}
	
	public void backAction(){
		this.finish();
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
		if (arg0.getId() == R.id.idTypeSpinner){
			if (idTypeSpinner.getSelectedItemPosition() == 0){
				idTextView.setHint("请输入学号");
			} else {
				idTextView.setHint("请输入身份证号");
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	

}
