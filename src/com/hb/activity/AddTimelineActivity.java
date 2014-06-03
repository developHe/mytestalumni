package com.hb.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;

import com.hb.R;
import com.hb.activity.ImproveRegistrationActivity.DeptAdapter;
import com.hb.client.Constants;
import com.hb.client.HttpRequestType;
import com.hb.enums.RegistrationCode;
import com.hb.model.DeptModel;
import com.hb.model.IndustryModel;
import com.hb.model.ProfileModel;
import com.hb.model.TimelineModel;
import com.hb.network.LKAsyncHttpResponseHandler;
import com.hb.network.LKHttpRequest;
import com.hb.network.LKHttpRequestQueue;
import com.hb.network.LKHttpRequestQueueDone;
import com.hb.util.DateUtil;
import com.hb.util.ImageUtil;
import com.hb.util.PatternUtil;
import com.hb.view.LKAlertDialog;

public class AddTimelineActivity extends AbsSubActivity {
	// test 11/19
	private String afterId = null;
	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0; // 这里的IMAGE_CODE是自己任意定义的
	private Bitmap bm = null;

	private Button btn_back = null;
	private Button btn_confirm = null;

	// 基本信息
	private ImageView image_head = null;
	private Button btn_pick = null;
	private EditText et_title = null;
	private EditText et_org = null;
	private EditText et_province = null;
	private EditText et_city = null;
	private TextView tv_stime = null;
	private TextView tv_etime = null;
	private Spinner industrySpinner = null;
	
	private ArrayList<IndustryModel> industryList = null;
	private String current_industry = null;

	private TimelineModel data = null;
	private Boolean isModify = false;

	public static final int DATE = 0;
	public static final int TIME = 1;

	private Boolean isTopDataPicker = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_addtimeline);

		this.init();
	}

	private void init() {
		Intent intent = this.getIntent();
		data = (TimelineModel) intent.getSerializableExtra("DATA");
		isModify = intent.getBooleanExtra("ISMODIFY", false);
		afterId = (String) intent.getStringExtra("AFTERID");
		TextView tv_title = (TextView) this.findViewById(R.id.titleView);
		String title = intent.getStringExtra("TITLE");
		tv_title.setText(title != null ? title : "新增个人履历");

		btn_back = (Button) this.findViewById(R.id.btn_back);
		btn_back.setOnClickListener(listener);
		btn_confirm = (Button) this.findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(listener);

		image_head = (ImageView) this.findViewById(R.id.image_head);
		btn_pick = (Button) this.findViewById(R.id.btn_pick);
		btn_pick.setOnClickListener(listener);
		et_title = (EditText) this.findViewById(R.id.et_title);
		industrySpinner = (Spinner) this.findViewById(R.id.industrySpinner);
		et_org = (EditText) this.findViewById(R.id.et_org);
		et_province = (EditText) this.findViewById(R.id.et_province);
		et_city = (EditText) this.findViewById(R.id.et_city);
		tv_stime = (TextView) this.findViewById(R.id.tv_stime);
		tv_stime.setText(DateUtil.getCurrentYearMonthDay());
		tv_stime.clearFocus();
		tv_stime.setOnClickListener(listener);
		tv_etime = (TextView) this.findViewById(R.id.tv_etime);
		tv_etime.setText(DateUtil.getCurrentYearMonthDay());
		tv_etime.setOnClickListener(listener);

		
		if (isModify) {
			if (data.getImgUrl() != null) {
				ImageUtil.loadImage(R.drawable.img_card_head_portrait, data.getImgUrl(), image_head);
			}
			et_title.setText(data.getTitle());
			et_org.setText(data.getOrg());
			et_province.setText(data.getProvince());
			et_city.setText(data.getCity());
			tv_stime.setText(data.getStartTime() + "-09");
			tv_etime.setText(data.getEndTime() + "-06");
			current_industry = data.getIndustry();
		}
		getIndustryList();
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
			case R.id.tv_stime:
				isTopDataPicker = true;
				String tmpStr1 = tv_stime.getText().toString();
				int year1 = Integer.valueOf(tmpStr1.substring(0, 4));
				int month1 = Integer.valueOf(tmpStr1.substring(5, 7));
				int day1 = Integer.valueOf(tmpStr1.substring(8, 10));
				buildDateOrTimeDialog(AddTimelineActivity.this, year1, month1, day1);
				break;
			case R.id.tv_etime:
				isTopDataPicker = false;
				String tmpStr2 = tv_etime.getText().toString();
				int year2 = Integer.valueOf(tmpStr2.substring(0, 4));
				int month2 = Integer.valueOf(tmpStr2.substring(5, 7));
				int day2 = Integer.valueOf(tmpStr2.substring(8, 10));
				buildDateOrTimeDialog(AddTimelineActivity.this, year2, month2, day2);
				break;
			case R.id.btn_back:
				AddTimelineActivity.this.goback();
				break;
			case R.id.btn_confirm:
				if (checkValue()) {
					if (isModify) {
						doTimeLineModify();
					} else {
						doTimeLineAdd();
					}

				}
				;

				break;
			default:
				break;
			}

		}
	};

	private void buildDateOrTimeDialog(Context context, int year, int month, int day) {
		// df = new SimpleDateFormat("HH:mm:SS");
		switch (444) {
		case DATE:
			date: new DatePickerDialog(context, listener1, year, month - 1, day).show();
			break;

		default:
			new DatePickerDialog(context, listener1, year, month - 1, day).show();

		}

	}

	private DatePickerDialog.OnDateSetListener listener1 = new DatePickerDialog.OnDateSetListener() { //

		@Override
		public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
			if (isTopDataPicker) {
				tv_stime.setText(arg1 + "-" + (arg2 + 1) + "-" + arg3);
			} else {
				tv_etime.setText(arg1 + "-" + (arg2 + 1) + "-" + arg3);
			}

		}

	};

	// 修改时间轴
	private void doTimeLineModify() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("title", et_title.getText().toString());
		paramMap.put("industryId", current_industry);
		paramMap.put("org", et_org.getText().toString());
		paramMap.put("province", et_province.getText().toString());
		paramMap.put("city", et_city.getText().toString());
		paramMap.put("stime", tv_stime.getText().toString().substring(0, 7));
		paramMap.put("etime", tv_etime.getText().toString().substring(0, 7));
		paramMap.put("id", data.getid());
		if (bm != null) {
			paramMap.put("pic", this.bitmaptoString(bm));
		}

		LKHttpRequest req1 = new LKHttpRequest(HttpRequestType.HTTP_TIMELINE_NODE_UPDATE, paramMap, getModifyHandler());
		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在提交数据，请稍候...", new LKHttpRequestQueueDone() {

			@Override
			public void onComplete() {
				super.onComplete();
			}
		});
	}

	// 获取行业列表
	private void getIndustryList() {
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getIndustryListRequest());
		queue.executeQueue("正在获取行业信息，请稍候...", new LKHttpRequestQueueDone());

	}

	private LKHttpRequest getIndustryListRequest() {

		LKHttpRequest req = new LKHttpRequest(HttpRequestType.HTTP_CONFIG_INDUSTRY_LIST, null, new LKAsyncHttpResponseHandler(){
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				HashMap<String, Object> map = (HashMap<String, Object>) obj;
				industryList = (ArrayList<IndustryModel>)(map.get("list"));
				
				ArrayAdapter<IndustryModel> adapter = new ArrayAdapter<IndustryModel>(AddTimelineActivity.this, R.layout.simple_spinner_item, android.R.id.text1, industryList);
				industrySpinner.setAdapter(adapter);
				if(isModify){
					for (int i = 0; i < industryList.size(); i++) {
						IndustryModel model = industryList.get(i);
						if(model.getId().equals(data.getIndustry())){
							industrySpinner.setSelection(i);
						}
					}
					
				}
				industrySpinner.setOnItemSelectedListener(new Adapter());
			}
		}
		, null);

		return req;
	}

	class Adapter implements OnItemSelectedListener {

		/**
		 * (non-Javadoc)
		 * 
		 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView,
		 *      android.view.View, int, long)
		 */
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			onChange(position);
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
	
	public void onChange(int position) {
		current_industry = industryList.get(position).getId(); 
	}
	// 添加时间轴结点
	private void doTimeLineAdd() {
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getTimeLineAdd());
		queue.executeQueue("正在提交数据，请稍候...", new LKHttpRequestQueueDone());

	}

	private LKHttpRequest getTimeLineAdd() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("title", et_title.getText().toString());
		paramMap.put("industryId", current_industry);
		paramMap.put("org", et_org.getText().toString());
		paramMap.put("province", et_province.getText().toString());
		paramMap.put("city", et_city.getText().toString());
		paramMap.put("stime", tv_stime.getText().toString().substring(0, 7));
		paramMap.put("etime", tv_etime.getText().toString().substring(0, 7));
		paramMap.put("afterID", afterId);
		if (bm != null) {
			paramMap.put("pic", this.bitmaptoString(bm));
		}

		LKHttpRequest req = new LKHttpRequest(HttpRequestType.HTTP_TIMELINE_NODE_CREATE, paramMap, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				if ((Integer) obj == 1) {
					new AlertDialog.Builder(AddTimelineActivity.this).setTitle("提示").setMessage("新增履历成功！").setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							Intent it = new Intent();
							AddTimelineActivity.this.gobackWithResult(5, it);

						}
					}).show();
				}
			}
		}, null);

		return req;
	}

	private LKAsyncHttpResponseHandler getModifyHandler() {
		return new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				if ((Integer) obj == 1) {
					new AlertDialog.Builder(AddTimelineActivity.this).setTitle("提示").setMessage("修改履历成功！").setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							Intent it = new Intent();
							AddTimelineActivity.this.gobackWithResult(5, it);

						}
					}).show();
				}

			}

		};
	}

	private boolean checkValue() {
		if (et_title.getText().toString().trim().equals("")) {
			this.showToast("身份不能为空！");
			return false;
		} else if (et_org.getText().toString().trim().equals("")) {
			this.showToast("组织不能为空！");
			return false;
		} else if (et_province.getText().toString().trim().equals("")) {
			this.showToast("省份不能为空！");
			return false;
		}

		return true;
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
}
