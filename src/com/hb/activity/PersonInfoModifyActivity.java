package com.hb.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;

import com.hb.R;
import com.hb.activity.ImproveRegistrationActivity.AdYearAdapter;
import com.hb.activity.ImproveRegistrationActivity.DeptAdapter;
import com.hb.activity.ImproveRegistrationActivity.MajorAdapter;
import com.hb.activity.ImproveRegistrationActivity.OrgOneAdapter;
import com.hb.activity.ImproveRegistrationActivity.OrgTwoAdapter;
import com.hb.client.HttpRequestType;
import com.hb.enums.LoginCode;
import com.hb.model.DeptModel;
import com.hb.model.MajorModel;
import com.hb.model.OrgOneModel;
import com.hb.model.OrgTwoModel;
import com.hb.model.ProfileModel;
import com.hb.network.LKAsyncHttpResponseHandler;
import com.hb.network.LKHttpRequest;
import com.hb.network.LKHttpRequestQueue;
import com.hb.network.LKHttpRequestQueueDone;
import com.hb.util.DateUtil;
import com.hb.util.ImageUtil;
import com.hb.view.LKAlertDialog;

public class PersonInfoModifyActivity extends AbsSubActivity implements OnClickListener {
	private String type = "1";// 1 学生 2 教工
	private ProfileModel model = null;
	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0; // 这里的IMAGE_CODE是自己任意定义的
	private Bitmap bm = null;

	private Button btn_back = null;
	private Button btn_confirm = null;

	// 基本信息
	private ImageView image_head = null;
	private Button btn_pick = null;
	private EditText et_name = null;
	private RadioGroup radioGroup = null;
	private RadioButton radioMale = null;
	private RadioButton radioFemale = null;
	private Spinner idCardSpinner = null;

	private int gender = 1;// 0: 女 1: 男
	private int idCard_select = 0; // 0:学生 1: 教工
	// 学生
	private LinearLayout layout_stu = null;
	private Spinner deptSpinner = null;
	private Spinner majorSpinner = null;
	private EditText et_class = null;
	private Spinner adYearSpinner = null;

	private List<DeptModel> deptModelList = new ArrayList<DeptModel>();
	private ArrayList<MajorModel> majorModelList = new ArrayList<MajorModel>();
	private DeptModel current_dept = null;
	private MajorModel current_major = null;
	private String current_year = null;

	// 教工
	private LinearLayout layout_teach = null;
	private Spinner org1Spinner = null;
	private Spinner org2Spinner = null;
	private EditText et_empno = null;

	private List<OrgOneModel> orgOneModelList = new ArrayList<OrgOneModel>();
	private ArrayList<OrgTwoModel> orgTwoList = new ArrayList<OrgTwoModel>();
	private OrgOneModel current_org1 = null;
	private OrgTwoModel current_org2 = null;

	// 可选信息
	private EditText et_mobile = null;
	private EditText et_email = null;
	private EditText et_qq = null;

	private ArrayList<String> idCardList = new ArrayList<String>();
	private ArrayList<String> adYearKeyList = new ArrayList<String>();
	private ArrayList<String> adYearValueList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_personinfomodify);

		this.init();
	}

	private void init() {

		// 填充值
		Intent intent = this.getIntent();
		model = (ProfileModel) intent.getSerializableExtra("MODEL");
		type = model.getType()+"";
		btn_back = (Button) this.findViewById(R.id.btn_back);
		btn_back.setOnClickListener(listener);
		btn_confirm = (Button) this.findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(listener);

		image_head = (ImageView) this.findViewById(R.id.image_head);
		btn_pick = (Button) this.findViewById(R.id.btn_pick);
		btn_pick.setOnClickListener(listener);
		et_name = (EditText) this.findViewById(R.id.et_name);

		et_mobile = (EditText) this.findViewById(R.id.et_mobile);
		et_mobile.setInputType(InputType.TYPE_CLASS_NUMBER);
		et_email = (EditText) this.findViewById(R.id.et_email);
		et_email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		et_qq = (EditText) this.findViewById(R.id.et_qq);

		et_mobile.setText(model.getMobile() != null ? model.getMobile():"");
		et_email.setText(model.getEmail() != null ? model.getEmail():"");
		et_qq.setText(model.getQq() != null ? model.getQq():"");
		
		radioGroup = (RadioGroup) this.findViewById(R.id.radioGroup);
		radioMale = (RadioButton) this.findViewById(R.id.radioMale);
		radioFemale = (RadioButton) this.findViewById(R.id.radioFemale);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// 获取变更后的选中项的ID
				int radioButtonId = arg0.getCheckedRadioButtonId();
				// 根据ID获取RadioButton的实例
				RadioButton rb = (RadioButton) PersonInfoModifyActivity.this.findViewById(radioButtonId);
				String text = rb.getText().toString();
				if (text.equals("男")) {
					gender = 1;
				} else {
					gender = 0;
				}
			}
		});

		if (model.getGender() == 0) {
			radioFemale.setChecked(true);
		} else {
			radioMale.setChecked(true);
		}
		ImageUtil.loadImage(R.drawable.img_card_head_portrait_small, model.getPic(), image_head);
		et_name.setText(model.getName());
		if (model.getGender() == 0) {
			radioFemale.setChecked(true);
		} else {
			radioMale.setChecked(true);
		}

		this.preparData();

		idCardSpinner = (Spinner) this.findViewById(R.id.idCardTypeSpinner);
		ArrayAdapter<String> idCardAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, idCardList);
		idCardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		idCardSpinner.setAdapter(idCardAdapter);
		idCardSpinner.setPrompt("身份类型");
		idCardSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				idCard_select = arg2;
				switch (arg2) {
				case 0:
					type = "1";
					layout_stu.setVisibility(View.VISIBLE);
					layout_teach.setVisibility(View.GONE);
					refreshConfigDeptist();
					break;
				case 1:
					type = "2";
					layout_stu.setVisibility(View.GONE);
					layout_teach.setVisibility(View.VISIBLE);
					refreshConfigOrgList();
					break;
				default:
					break;
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

		// 学生
		layout_stu = (LinearLayout) this.findViewById(R.id.layout_stu);
		adYearSpinner = (Spinner) this.findViewById(R.id.adYearSpinner);
		deptSpinner = (Spinner) this.findViewById(R.id.deptSpinner);
		majorSpinner = (Spinner) this.findViewById(R.id.majorSpinner);
		et_class = (EditText) this.findViewById(R.id.et_class);

		ArrayAdapter<String> adYearAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, adYearValueList);
		adYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adYearSpinner.setAdapter(adYearAdapter);
		adYearSpinner.setPrompt("入学年份");
		adYearSpinner.setSelection(adYearValueList.size() - 1);
		if(model.getAdYear() != null){
			for(int i=0; i<adYearKeyList.size(); i++){
				if(model.getAdYear().equals(adYearKeyList.get(i)+"")){
					adYearSpinner.setSelection(i);
				}
			}
			
		}
		adYearSpinner.setOnItemSelectedListener(new AdYearAdapter());

		// 教工
		layout_teach = (LinearLayout) this.findViewById(R.id.layout_teach);
		org1Spinner = (Spinner) this.findViewById(R.id.org1Spinner);
		org2Spinner = (Spinner) this.findViewById(R.id.org2Spinner);
		et_empno = (EditText) this.findViewById(R.id.et_empNo);
		et_empno.setInputType(InputType.TYPE_CLASS_NUMBER);
		
		if(model.getType() == 1){
			et_class.setText(model.getClassName());
			layout_stu.setVisibility(View.VISIBLE);
			layout_teach.setVisibility(View.GONE);
			refreshConfigDeptist();
		}else{
			et_empno.setText(model.getEmpNo());
			layout_stu.setVisibility(View.GONE);
			layout_teach.setVisibility(View.VISIBLE);
			refreshConfigOrgList();
		}

	}

	private void preparData() {
		idCardList.add("学生校友");
		idCardList.add("教工校友");
		for (int i = 1950; i < Integer.parseInt(DateUtil.getCurrentYear()) + 1; i++) {
			adYearKeyList.add(i + "");
			adYearValueList.add(i + " 年");

		}

	}

	// 更新个人信息
	private void doProfileUpdate() {

		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("name", et_name.getText().toString());
		paramMap.put("gender", gender + "");
		paramMap.put("type", type);
		if (type.equals("1")) {// 学生
			paramMap.put("deptId", current_dept.getCode());
			paramMap.put("majorId", current_major.getCode());
			paramMap.put("clazz", et_class.getText());
			paramMap.put("adYear", current_year);
		} else {// 教工
			paramMap.put("org1Id", current_org1.getCode());
			paramMap.put("org2Id", current_org2.getCode());
			paramMap.put("empNo", et_empno.getText());
		}
		paramMap.put("mobile", et_mobile.getText() == null ? "" : et_mobile.getText());
		paramMap.put("email", et_email.getText() == null ? "" : et_email.getText());
		paramMap.put("qq", et_qq.getText() == null ? "" : et_qq.getText());
		paramMap.put("adYear", adYearKeyList.get(adYearSpinner.getSelectedItemPosition()));

		if (!image_head.isDrawingCacheEnabled())
			image_head.setDrawingCacheEnabled(true);
		bm = image_head.getDrawingCache();
		paramMap.put("pic", bm == null ? "null" : this.bitmaptoString(bm));
		image_head.destroyDrawingCache();

		LKHttpRequest req1 = new LKHttpRequest(HttpRequestType.HTTP_PROFILE_UPDATE, paramMap, getUpdateProfileHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在提交数据，请稍候...", new LKHttpRequestQueueDone() {

			@Override
			public void onComplete() {
				super.onComplete();
			}
		});

	}

	private LKAsyncHttpResponseHandler getUpdateProfileHandler() {
		return new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> respMap = (HashMap<String, String>) obj;
				int returnCode = Integer.parseInt(respMap.get("rc"));
				if (returnCode == 1) {
					// 修改成功
					new AlertDialog.Builder(PersonInfoModifyActivity.this).setTitle("提示").setMessage("信息更新成功！").setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							Intent it = new Intent();
							PersonInfoModifyActivity.this.gobackWithResult(5, it);
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
			case R.id.btn_pick:
				Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
				getAlbum.setType(IMAGE_TYPE);
				startActivityForResult(getAlbum, IMAGE_CODE);
				break;
			case R.id.btn_back:
				PersonInfoModifyActivity.this.goback();
				break;
			case R.id.btn_confirm:
				if (checkValue()) {
					doProfileUpdate();
				}

				break;
			default:
				break;
			}

		}
	};

	// 获取院系专业双级列表
	private void refreshConfigDeptist() {
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getConfigDeptListRequest());
		queue.executeQueue("正在刷新数据...", new LKHttpRequestQueueDone());
	}

	// 获取部门组织双级列表
	private void refreshConfigOrgList() {
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getConfigOrgListRequest());
		queue.executeQueue("正在刷新数据...", new LKHttpRequestQueueDone());
	}

	// 获取院系专业双级列表
	private LKHttpRequest getConfigDeptListRequest() {
		LKHttpRequest request = new LKHttpRequest(HttpRequestType.HTTP_CONFIG_DEPT_LIST, null, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> map = (HashMap<String, Object>) obj;
				int returnCode = (Integer) map.get("rc");
				if (returnCode == LoginCode.SUCCESS) {
					deptModelList.addAll((ArrayList<DeptModel>) map.get("list"));
				}
				ArrayAdapter<DeptModel> deptAdapter = new ArrayAdapter<DeptModel>(PersonInfoModifyActivity.this, R.layout.simple_spinner_item, android.R.id.text1, deptModelList);
				deptSpinner.setAdapter(deptAdapter);
				deptSpinner.setOnItemSelectedListener(new DeptAdapter());
				for(int i=0; i<deptModelList.size(); i++){
					if(model.getDeptId().equals(((DeptModel)deptModelList.get(i)).getCode()+"")){
						deptSpinner.setSelection(i);
					}
				}
				
			}
		}, "me");

		return request;
	}

	// 获取部门组织双级列表
	private LKHttpRequest getConfigOrgListRequest() {
		LKHttpRequest request = new LKHttpRequest(HttpRequestType.HTTP_CONFIG_ORG_LIST, null, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> map = (HashMap<String, Object>) obj;
				int returnCode = (Integer) map.get("rc");
				if (returnCode == LoginCode.SUCCESS) {
					orgOneModelList.addAll((ArrayList<OrgOneModel>) map.get("list"));
				}
				ArrayAdapter<OrgOneModel> orgOneAdapter = new ArrayAdapter<OrgOneModel>(PersonInfoModifyActivity.this, R.layout.simple_spinner_item, android.R.id.text1, orgOneModelList);
				org1Spinner.setAdapter(orgOneAdapter);
				org1Spinner.setOnItemSelectedListener(new OrgOneAdapter());
				for(int i=0; i<orgOneModelList.size(); i++){
					if(model.getOrg1Id().equals(((OrgOneModel)orgOneModelList.get(i)).getCode()+"")){
						org1Spinner.setSelection(i);
					}
				}
			}
		}, "me");

		return request;
	}

	class AdYearAdapter implements OnItemSelectedListener {

		/**
		 * (non-Javadoc)
		 * 
		 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView,
		 *      android.view.View, int, long)
		 */
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			onAdYearChange(position);
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
		 */
		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}

	}

	class DeptAdapter implements OnItemSelectedListener {

		/**
		 * (non-Javadoc)
		 * 
		 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView,
		 *      android.view.View, int, long)
		 */
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			onDeptChange(position);
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
		 */
		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}

	}

	class OrgOneAdapter implements OnItemSelectedListener {

		/**
		 * (non-Javadoc)
		 * 
		 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView,
		 *      android.view.View, int, long)
		 */
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			onOrgOneChange(position);
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
		 */
		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}

	}

	public void onAdYearChange(int position) {
		current_year = adYearKeyList.get(position);
	}

	public void onDeptChange(int position) {
		current_dept = deptModelList.get(position);
		ArrayAdapter<MajorModel> majorAdapter = new ArrayAdapter<MajorModel>(this, R.layout.simple_spinner_item, android.R.id.text1, current_dept.getMajors());
		majorSpinner.setAdapter(majorAdapter);
		majorSpinner.setOnItemSelectedListener(new MajorAdapter() {
		});
		for(int i=0; i<current_dept.getMajors().size(); i++){
			if(model.getMajorId().equals(((MajorModel)current_dept.getMajors().get(i)).getCode()+"")){
				majorSpinner.setSelection(i);
			}
		}
		
	}

	public void onOrgOneChange(int position) {
		current_org1 = orgOneModelList.get(position);
		ArrayAdapter<OrgTwoModel> adapter = new ArrayAdapter<OrgTwoModel>(this, R.layout.simple_spinner_item, android.R.id.text1, current_org1.getTwos());
		org2Spinner.setAdapter(adapter);
		org2Spinner.setOnItemSelectedListener(new OrgTwoAdapter() {
		});
		for(int i=0; i<current_org1.getTwos().size(); i++){
			if(model.getOrg2Id().equals(((OrgTwoModel)current_org1.getTwos().get(i)).getCode()+"")){
				org2Spinner.setSelection(i);
			}
		}
	}

	class MajorAdapter implements OnItemSelectedListener {

		/**
		 * (non-Javadoc)
		 * 
		 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView,
		 *      android.view.View, int, long)
		 */
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			onMajorChange(position);
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
		 */
		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}

	}

	class OrgTwoAdapter implements OnItemSelectedListener {

		/**
		 * (non-Javadoc)
		 * 
		 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView,
		 *      android.view.View, int, long)
		 */
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			onOrgTwoChange(position);
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
		 */
		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}

	}

	public void onMajorChange(int position) {
		current_major = current_dept.getMajors().get(position);
	}

	public void onOrgTwoChange(int position) {
		current_org2 = current_org1.getTwos().get(position);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != RESULT_OK) { // 此处的 RESULT_OK 是系统自定义得一个常量

			return;

		}

		bm = null;
		// 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
		ContentResolver resolver = getContentResolver();
		// 此处的用于判断接收的Activity是不是你想要的那个
		if (requestCode == IMAGE_CODE) {
			try {
				Uri originalUri = data.getData(); // 获得图片的uri
				bm = MediaStore.Images.Media.getBitmap(resolver, originalUri); // 显得到bitmap图片这里开始的第二部分，获取图片的路径：
				String[] proj = { MediaStore.Images.Media.DATA };
				// 好像是android多媒体数据库的封装接口，具体的看Android文档
				Cursor cursor = managedQuery(originalUri, proj, null, null, null);
				// 按我个人理解 这个是获得用户选择的图片的索引值
				if (bm != null) {
					image_head.setImageBitmap(bm);
				}

				// int column_index =
				// cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				// //将光标移至开头 ，这个很重要，不小心很容易引起越界
				// cursor.moveToFirst();
				// //最后根据索引值获取图片路径
				// String path = cursor.getString(column_index);
			} catch (IOException e) {

			}

		}

	}

	public Bitmap stringtoBitmap(String string) {
		// 将字符串转换成Bitmap类型
		Bitmap bitmap = null;
		try {
			byte[] bitmapArray;
			bitmapArray = Base64.decode(string, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	public String bitmaptoString(Bitmap bitmap) {

		// 将Bitmap转换成字符串
		String string = null;
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 100, bStream);
		byte[] bytes = bStream.toByteArray();
		string = Base64.encodeToString(bytes, Base64.DEFAULT);
		return string;

	}

	@Override
	public void onClick(View arg0) {

	}

	private Boolean checkValue() {
		if (et_name.getText().length() == 0) {
			this.showToast("姓名不能为空");
			return false;
		}

		if (idCard_select == 0) {// 学生
			if (et_class.getText().length() == 0) {
				this.showToast("班级不能为空");
				return false;
			}
		} else {
			if (et_empno.getText().length() == 0) {
				this.showToast("职工编号不能为空");
				return false;
			}
		}

		return true;
	}
}
