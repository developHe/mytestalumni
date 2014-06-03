package com.hb.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.hb.R;

public class SchoolCardIntroductionActivity extends AbsSubActivity implements OnClickListener {

	private TextView introductionView;

	private Button backButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_school_card_introduction);
		
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
		return "          首都师范大学校友龙卡"
				+ "\n\n          首都师范大学校友会、教育基金会联合中国建设银行共同打造的首都师范大学人专属的信用卡——“首都师范大学校友龙卡”正式推出，这是一款为学校教职工、在校生及校友专属的信用卡。有学校标识的信用卡一方面是校友身份的象征，起到学校联系校友的纽带作用，一方面也是教育基金会拓宽筹资渠道的一种新的尝试！卡片为银联标准的人民币信用卡，分为金卡和普通卡。金卡针对教职员工和校友发行，最高信用额度为5000-10万元人民币；普通卡针对在校学生发行，最高信用额度为1000-5000元人民币。"
				+ "\n\n          独有的卡面设计彰显校友身份的权利，持卡人除享有建行的金融服务和相关优惠外，同时享有校友商户优惠计划的优惠。目前，经过校友会的努力，已有19间商户参加计划，相信随着时间推移和发卡量的增加，加入到“首都师范大学校友龙卡”校友商户优惠计划中的商户会越来越丰富！"
				+ "\n\n          办理“首都师范大学校友龙卡”所需材料"
				+ "\n    1.在校本科以上学生："
				+ "\n    （1）身份证复印件（正反面）"
				+ "\n    （2）学生证或校园卡复印件"
				+ "\n    （3）学生家长（父母一方）身份证复印件（正反面）"
				+ "\n\n    2.教职工、校友"
				+ "\n    （1）身份证复印件（正反面）"
				+ "\n    （2）工作证（校园卡）或工卡、门禁卡、工作证明复印件"
				+ "\n\n           “首都师范大学校友龙卡”办理途径和方式说明"
				+ "\n    一、申请人前往中国建设银行各营业网点柜台办理，讲明办理“首都师范大学建行名校卡”。（校友还需携带毕业证书）"
				+ "\n    二、申请人前往首都师范大学校友及发展办公室办理。"
				+ "\n    三、申请人通过网络获取申请表办理"
				+ "\n    （一）学校主页——“校园信息”栏目，点击《关于办理“首都师范大学龙卡”的通知》，下载并打印相应电子版申请表格（下载的电子版申请表请正反面打印在同一张A4纸上）。"
				+ "\n    （二）通过校友会微博、人人网、电子邮箱发送个人姓名、联系方式、邮寄地址后，校友会将发送电子版申请表或寄送纸质申请表。"
				+ "\n    （三）申请人按要求填写好申请表，并将相关材料一并寄送回校友会。"
				+ "\n    四、申请人通过电话联系建行工作人员办理"
				+ "\n    建行联系人：李进，电话：68726965，手机：13810882081"
				+ "\n    申请人致电告知联系地址，建行工作人员将把申请表邮寄给申请人，申请人把其它相关材料和申请表一起邮寄回建行。"
				+ "\n    首都师范大学校友及发展办公室、校友会、教育基金会联系信息"
				+ "\n    办公地点：首都师范大学东校区办公楼B座206"
				+ "\n    邮寄地址：北京市海淀区西三环北路105号 首都师范大学学校办公室校友会"
				+ "\n    邮    编：100048"
				+ "\n    联系电话：68902418、68902611"
				+ "\n    官方网站：http://xyh.cnu.edu.cn"
				+ "\n    官方微博：http://weibo.com/cnuxyh"
				+ "\n    人人网公共主页：http://www.renren.com/xyhcnu"
				+ "\n    公共邮箱：cnuxyh@126.com\n\n";
	}

}
