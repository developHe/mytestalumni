package com.hb.enums;

public class ErrorCode{
	
	// ec 响应状态码
	
	public static final int SUCCESS		 				= 1; // 正常
	
	public static final int UNKNOWN 					= 0; // 未知错误
	public static final int SYSTEM_MAINTENANCE  		= 101; // 系统维护中
	public static final int SESSIONID_ERROR				= -1; // 会话失效
	public static final int CLIENTID_ERROR				= -2; // 无效的cid
	public static final int VERSION_ERROR				= -3; // 无效的协议版本
	public static final int PARAM_ERROR					= -4; // 参数错误
	public static final int PERMISSION_ERROR			= -5; // 权限不足
	public static final int CACHE_ERROR					= -101; // 缓存错误
	public static final int DATABASE_ERROR				= -201; //数据库错误
	
}
