package com.hb.client;

public class Constants {
	
//	public static final String HOST 							= "http://xy.cnu.edu.cn";
	public static final String HOST 							= "http://115.47.56.228/alumni";
	
	public static final String HOSTNAME 						= HOST + "/service";
	public static final String DOWNLOADURL						= HOST + "/apk/version.xml";
	
	public static final int VERSION 							= 3;
	
	// 公共参数
	public static final	String VERSION_ID 						= "1";
	public static final String CLIENT_ID						= "2"; //1、网站 2、Android 3、IOS 
	public static String SESSION_ID								= "-1"; // sessionId
	
	public static int GROUP_MY 									= 0;//我的圈子
	public static int GROUP_SYS 								= 1;//系统圈子
	public static int GROUP_SEARCH								= 2;//搜索圈子
	
	
	// 测试数据
	public static final String WEIBO_TIMELINE_SCREENNAME		= "首都师范大学校友会-教育基金会";
	public static final String URL_CNU_EDU 						= "http://www.cnu.edu.cn/pages/info_details.jsp?seq=2379&classcode=70101&boardid=70101";
	
	public static final int PAGESIZE							= 20;
	
	public static final String kUSERNAME 						= "username";
	
	
	// Sina Weibo
//	public static final String SINA_WEIBO_APP_KEY 				= "1166774910";
//	public static final String SINA_WEIBO_SECRET_KEY 			= "a6b0066e12c9d425726b4e8edd115e19";
//	public static final String SINA_WEIBO_CALLBACK_URL 			= "http://www.dhcc.com";

//	public static final String SINA_WEIBO_APP_KEY 				= "569583472";
//	public static final String SINA_WEIBO_SECRET_KEY 			= "d980e8e404147e899cd9f67a2c5aa9df";
//	public static final String SINA_WEIBO_CALLBACK_URL 			= "http://www.baidu.com";
	
	public static final String SINA_WEIBO_APP_KEY 				= "925901794";
	public static final String SINA_WEIBO_SECRET_KEY 			= "9a14857d4a744f9049927c88b21df13f";
	public static final String SINA_WEIBO_CALLBACK_URL 			= "https://api.weibo.com/oauth2/default.html";//"http://www.baidu.com/p/develop_He";
	
//	public static final String SINA_WEIBO_APP_KEY 				= "3601604349";
//	public static final String SINA_WEIBO_SECRET_KEY 			= "7894dfdd1fc2ce7cc6e9e9ca620082fb";
//	public static final String SINA_WEIBO_CALLBACK_URL 			= "http://hi.baidu.com/jt_one";
	
	public static final String kACCESS_TOKEN					= "access_token";
	
	public static String SINA_OAUTH = "https://api.weibo.com/oauth2/authorize?client_id="
			+ SINA_WEIBO_APP_KEY
			+ "&response_type=code&redirect_uri="
			+ SINA_WEIBO_CALLBACK_URL
			+ "&display=mobile";

	public static String SINA_ACCESS_TOKEN = "https://api.weibo.com/oauth2/access_token?client_id="
			+ SINA_WEIBO_APP_KEY
			+ "&client_secret="
			+ SINA_WEIBO_SECRET_KEY
			+ "&grant_type=authorization_code&redirect_uri="
			+ SINA_WEIBO_CALLBACK_URL+"&code=";
	
	
	
	
}
