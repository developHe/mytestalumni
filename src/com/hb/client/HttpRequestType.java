package com.hb.client;

public interface HttpRequestType {
	
	// HTTP REQUEST TYPE
	public static final String HTTP_LOGIN 								= "LOGIN";
	public static final String HTTP_REGISTER							= "REGISTER";
	public static final String HTTP_LOGOUT								= "LOGOUT";
	public static final String HTTP_UPDATEPWD							= "UPDATEPWD";
	
	public static final String HTTP_PROFILE_DETAIL						= "PROFILE_DETAIL";
	public static final String HTTP_PROFILE_MATCH						= "PROFILE_MATCH";
	public static final String HTTP_PROFILE_UPDATE						= "PROFILE_UPDATE";
	
	public static final String HTTP_TIMELINE_LIST						= "TIMELINE_LIST";
	public static final String HTTP_TIMELINE_NODE_CREATE				= "TIMELINE_NODE_CREATE";
	public static final String HTTP_TIMELINE_NODE_UPDATE				= "TIMELINE_NODE_UPDATE";
	public static final String HTTP_TIMELINE_NODE_DELETE				= "TIMELINE_NODE_DELETE";
	public static final String HTTP_TIMELINE_NODE_NEWFRIENDS_LIST		= "TIMELINE_NODE_NEWFRIENDS_LIST";
	
	public static final String HTTP_COLLEGE_INTRODUCT					= "COLLEGE_INTRODUCT";
	public static final String HTTP_COLLEGE_BROADCAST_LIST				= "COLLEGE_BROADCAST_LIST";
	public static final String HTTP_COLLEGE_BROADCAST_DETAIL			= "COLLEGE_BROADCAST_DETAIL";
	
	public static final String HTTP_COLLEGE_EVENT_TYPE_LIST				= "COLLEGE_EVENT_TYPE_LIST";
	public static final String HTTP_COLLEGE_EVENT_LIST					= "COLLEGE_EVENT_LIST";
	public static final String HTTP_COLLEGE_EVENT_DETAIL				= "COLLEGE_EVENT_DETAIL";
	public static final String HTTP_COLLEGE_EVENT_PARTICIPANT_LIST		= "COLLEGE_EVENT_PARTICIPANT_LIST";
	public static final String HTTP_COLLEGE_EVENT_FOLLOW				= "COLLEGE_EVENT_FOLLOW";
	public static final String HTTP_COLLEGE_EVENT_UNFOLLOW				= "COLLEGE_EVENT_UNFOLLOW";
	public static final String HTTP_COLLEGE_EVENT_JOIN					= "COLLEGE_EVENT_JOIN";
	public static final String HTTP_COLLEGE_EVENT_QUIT					= "COLLEGE_EVENT_QUIT";
	
	public static final String HTTP_COLLEGE_FEEDBACK_LIST				= "COLLEGE_FEEDBACK_LIST";
	public static final String HTTP_COLLEGE_FEEDBACK_CREATE				= "COLLEGE_FEEDBACK_CREATE";
	public static final String HTTP_COLLEGE_CARD_APPLY					= "COLLEGE_CARD_APPLY";
	public static final String HTTP_COLLEGE_CARD_STATUS					= "COLLEGE_CARD_STATUS";
	public static final String HTTP_COLLEGE_FEEDBACK_APPLY				= "COLLEGE_FEEDBACK_APPLY";
	
	
	public static final String HTTP_FRIENDS_LIST						= "FRIENDS_LIST";
	public static final String HTTP_FRIENDS_FUNS_LIST					= "FRIENDS_FUNS_LIST";
	public static final String HTTP_FRIENDS_FOLLOW						= "FRIENDS_FOLLOW";
	public static final String HTTP_FRIENDS_UNFOLLOW					= "FRIENDS_UNFOLLOW";

	public static final String HTTP_GROUP_ME_LIST						= "GROUP_ME_LIST";//我加入的圈子列表
	public static final String HTTP_GROUP_MY_LIST						= "GROUP_MY_LIST";//我创建的圈子列表
	public static final String HTTP_GROUP_PARTICIPANT_LIST				= "PARTICIPANT_LIST";
	public static final String HTTP_GROUP_LIST							= "GROUP_LIST";
	public static final String HTTP_GROUP_JOIN							= "GROUP_JOIN";
	public static final String HTTP_GROUP_QUIT							= "GROUP_QUIT";
	public static final String HTTP_GROUP_COMMENT_LIST					= "GROUP_COMMENT_LIST";
	public static final String HTTP_GROUP_COMMENT_CREATE				= "GROUP_COMMENT_CREATE";
	public static final String HTTP_GROUP_CREATE						= "GROUP_CREATE";
	public static final String HTTP_GROUP_DELETE						= "GROUP_DELETE";
	public static final String HTTP_PUBLISH_COMMENT						= "PUBLISH_COMMENT";
	public static final String HTTP_FRIENDS_SEARCH						= "FRIENDS_SEARCH";
	
	public static final String HTTP_MEDIA_TOPLIST						= "MEDIA_TOPLIST";
	public static final String HTTP_MEDIA_LIST							= "MEDIA_LIST";
	public static final String HTTP_MEDIA_DETAIL						= "MEDIA_DETAIL";
	public static final String HTTP_MEDIA_STREAM						= "MEDIA_STREAM";
	public static final String HTTP_CONFIG_DEPT_LIST					= "CONFIG_DEPT_LIST";
	public static final String HTTP_CONFIG_ORG_LIST						= "CONFIG_ORG_LIST";
	public static final String HTTP_PROFILE_ME_CREATE					= "PROFILE_ME_CREATE";
	public static final String HTTP_CONFIG_INDUSTRY_LIST				= "CONFIG_INDUSTRY_LIST";
	public static final String HTTP_GALARY_LIST							= "GALARY_LIST";
	public static final String HTTP_GALARY_DETAIL						= "GALARY_DETAIL";
	public static final String HTTP_PROFILE_ME_CONFIG					= "PROFILE_ME_CONFIG";
	public static final String HTTP_PROFILE_ME_UPDATE					= "PROFILE_ME_UPDATE";
	public static final String HTTP_GOURPKICK							= "GOURPKICK";
	
	
}
