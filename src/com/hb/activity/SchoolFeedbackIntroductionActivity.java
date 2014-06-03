package com.hb.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.hb.R;

public class SchoolFeedbackIntroductionActivity extends AbsSubActivity implements OnClickListener {

	private TextView introductionView;

	private Button backButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_school_feedback_introduction);
		
		introductionView = (TextView) this.findViewById(R.id.introductionText);
		introductionView.setText(getInstroduction());

		backButton = (Button) this.findViewById(R.id.backButton);
		backButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.backButton:
			goback();
			break;
		}
	}
	
	private String getInstroduction(){
		return "          首都师范大学教育基金会捐赠途径"
				+ "\n\n          首都师范大学教育基金会欢迎所有热心教育的有识之士以不同方式支持首都师范大学和教育事业，包括设立永久性基金，或以现金、支票、汇票、股票、证券、债券、图书、资料、设备、房产、遗产、财产等形式捐赠。"
				+ "\n\n          捐款可以指定项目和用途，具体捐赠途径如下："
				+ "\n\n(一)银行转账 "
				+ "    人民币捐赠账户 "
				+ "\n    开户行帐号： 0200 0076 0904 7652 806 "
				+ "\n    开户名称： 首都师范大学教育基金会 "
				+ "\n    开户地址： 中国工商银行北京紫竹院支行 "
				+ "\n\n(二)邮局汇款 "
				+ "\n    地 址：北京市海淀区西三环北路105号首都师范大学（学校办公室）"
				+ "\n    邮政编码：100048 "
				+ "\n    收款人：首都师范大学教育基金会 (请在附言中注明捐赠用途) "
				+ "\n    (请您务必在转账和汇款附言中写清您的通讯地址、联系电话等信息，以便于我们和您联系。)"
				+ "\n\n          首都师范大学教育基金会联系方式"
				+ "\n    地址：北京市海淀区西三环北路105号首都师范大学学校办公室-教育基金会"
				+ "\n    邮政编码：100048 "
				+ "\n    联系电话：010-68902418 68902611 "
				+ "\n    传真：86-10-68902418"
				+ "\n    E-mail：cnuxyh@126.com"
				+ "\n    网址：http://xyh.cnu.edu.cn";
	}

}
