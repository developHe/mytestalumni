package com.hb.enums;

public class RegistrationCode {
	
	public static final int SUCCESS 					= 1; // 正常
	
	public static final int WAITTING_INPUT_FAILURE 		= -1;  // 尚未填写基本信息 
	public static final int WAITTING_CONFIRM_FAILURE    = -2;  // 尚未确认基本信息 
	public static final int WAITTING_AUDIT_FAILURE 		= -3;  // 等待审核 
	
}

