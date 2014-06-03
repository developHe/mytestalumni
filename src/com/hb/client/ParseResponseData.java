package com.hb.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hb.enums.ErrorCode;
import com.hb.exception.ServiceErrorException;
import com.hb.model.ActiveModel;
import com.hb.model.AnnouncementModel;
import com.hb.model.CommentModel;
import com.hb.model.DeptModel;
import com.hb.model.GroupModel;
import com.hb.model.ImageModel;
import com.hb.model.IndustryModel;
import com.hb.model.MajorModel;
import com.hb.model.MediaModel;
import com.hb.model.OrgOneModel;
import com.hb.model.OrgTwoModel;
import com.hb.model.ProfileModel;
import com.hb.model.SchoolModel;
import com.hb.model.TimelineModel;
import com.hb.util.ImageUtil;

public class ParseResponseData {

	public static Object parse(String type, JSONObject jsonObject) throws ServiceErrorException {
		int errorCode = jsonObject.optInt("ec", ErrorCode.UNKNOWN);

		if (errorCode != ErrorCode.SUCCESS) {
			throw new ServiceErrorException(errorCode, getErrorMsg(errorCode) + "[ERROR_CODE:" + errorCode + "]");
		}

		// Parse
		if (type.equalsIgnoreCase(HttpRequestType.HTTP_LOGIN)) {
			return login(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_LOGOUT)) {
			return logout(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_UPDATEPWD)) {
			return updatePwd(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_REGISTER)) {
			return register(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_PROFILE_DETAIL)) {
			return getProfileDetail(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_PROFILE_MATCH)) {
			return profileMatch(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_PROFILE_UPDATE)) {
			return profileUpdate(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_COLLEGE_BROADCAST_LIST)) {
			return getBroadcastList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_COLLEGE_BROADCAST_DETAIL)) {
			return getBroadcastDetail(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_COLLEGE_EVENT_TYPE_LIST)) {
			return getEventTypeList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_COLLEGE_EVENT_LIST)) {
			return getEventList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_COLLEGE_EVENT_DETAIL)) {
			return getEventDetail(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_COLLEGE_EVENT_PARTICIPANT_LIST)) {
			return getEventParticipantList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_COLLEGE_CARD_APPLY)) {
			return collegeCardApply(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_COLLEGE_CARD_STATUS)) {
			return collegeCardStatus(jsonObject);
			
		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_COLLEGE_FEEDBACK_APPLY)) {
			return collegeFeedbackApply(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_TIMELINE_LIST)) {
			return getTimelineList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_TIMELINE_NODE_CREATE)) {
			return timelineNodeCreate(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_TIMELINE_NODE_UPDATE)) {
			return timelineNodeUpdate(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_TIMELINE_NODE_DELETE)) {
			return timelineNodeDelete(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_COLLEGE_INTRODUCT)) {
			return getCollegeInfo(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_TIMELINE_NODE_NEWFRIENDS_LIST)) {
			return getSuggestPeopleList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_FRIENDS_LIST)) {
			return getMyAttentionsList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_FRIENDS_FUNS_LIST)) {
			return getFansList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_FRIENDS_FOLLOW)) {
			return getAddAttention(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_FRIENDS_UNFOLLOW)) {
			return getCancelAttention(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_GROUP_ME_LIST)) {
			return getGroupMeList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_GROUP_MY_LIST)) {
			return getGroupMyList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_GROUP_PARTICIPANT_LIST)) {
			return getParticipantList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_FRIENDS_SEARCH)) {
			return getSearchList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_GROUP_LIST)) {
			return getAllGroupList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_GROUP_JOIN)) {
			return getGroupJoin(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_GROUP_DELETE)) {
			return getGroupDelete(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_GROUP_QUIT)) {
			return getGroupQuit(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_GROUP_CREATE)) {
			return getCreateCircle(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_GROUP_COMMENT_LIST)) {
			return getCommentList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_PUBLISH_COMMENT)) {
			return getPublishComment(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_MEDIA_TOPLIST)) {
			return getMediaTopList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_MEDIA_LIST)) {
			return getMediaList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_MEDIA_DETAIL)) {
			return getMediaDetail(jsonObject);
		} else if(type.equalsIgnoreCase(HttpRequestType.HTTP_MEDIA_STREAM)){
			return getMediaDetail(jsonObject);
		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_CONFIG_DEPT_LIST)) {
			return getConfigDeptList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_CONFIG_ORG_LIST)) {
			return getConfigOrgList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_PROFILE_ME_CREATE)) {
			return getProfileMeCreateList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_CONFIG_INDUSTRY_LIST)) {
			return getConfigIndustryList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_GALARY_LIST)) {
			return getGalaryList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_GALARY_DETAIL)) {
			return getGalaryDetail(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_PROFILE_ME_CONFIG)) {
			return getProfileMeConfig(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_PROFILE_ME_UPDATE)) {
			return getProfileMeUpdate(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_GOURPKICK)) {
			return getDeleteMember(jsonObject);

		}

		return null;
	}

	/*
	 * 登录
	 */
	private static Object login(JSONObject jsonObject) {

		return JSONObject2Map(jsonObject);
	}

	/*
	 * 登出
	 */
	private static Object logout(JSONObject jsonObject) {

		return JSONObject2Map(jsonObject);
	}

	/*
	 * 修改密码
	 */
	private static Object updatePwd(JSONObject jsonObject) {
		int errorCode = jsonObject.optInt("rc", ErrorCode.UNKNOWN);

		return errorCode;
	}

	/*
	 * 注册
	 */
	private static Object register(JSONObject jsonObject) {

		return JSONObject2Map(jsonObject);
	}

	/*
	 * 查看个人基本信息
	 * 
	 * {"idCardNo":null,"adYear":2009,"pic":"http://115.47.56.228:8080/alumni/pic/391/983/67952e3bda626f2f37a766b8d8fedcd7","type":1,"rc":1,"ec":1,"majorId":null, "email":null,"name":"王之稚","gender":0,"deptId":null,"qq":null,"mobile":null}
	 */
	private static Object getProfileDetail(JSONObject jsonObject) {
		try {
			if (null != jsonObject) {
				ProfileModel model = new ProfileModel();
				model.setIdCardNo(jsonObject.optString("idCardNo", ""));
				model.setName(jsonObject.optString("name", ""));
				model.setGender(jsonObject.optInt("gender"));
				model.setMajorId(jsonObject.optString("majorId", ""));
				model.setAdYear(jsonObject.optString("adYear", ""));
				model.setPic(jsonObject.optString("pic", ""));
				model.setType(jsonObject.optInt("type", 1));
				model.setClassName(jsonObject.optString("clazz", ""));
				model.setDeptId(jsonObject.optString("deptId", ""));
				model.setMajorId(jsonObject.optString("majorId", ""));

				model.setOrg1Id(jsonObject.optString("org2Id"));
				model.setOrg2Id(jsonObject.optString("org2Id", ""));
				model.setEmpNo(jsonObject.optString("empNo", ""));
				model.setEmail(jsonObject.optString("email", ""));
				model.setDeptId(jsonObject.optString("deptId", ""));
				model.setQq(jsonObject.optString("qq", ""));
				model.setMobile(jsonObject.optString("mobile", ""));

				return model;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private static Object profileMatch(JSONObject jsonObject) {
		int errorCode = jsonObject.optInt("rc", ErrorCode.UNKNOWN);

		return errorCode;
	}

	// 更新个人信息
	private static Object profileUpdate(JSONObject jsonObject) {

		return JSONObject2Map(jsonObject);
	}

	// 取得公告列表
	private static Object getBroadcastList(JSONObject jsonObject) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		ArrayList<AnnouncementModel> modelList = new ArrayList<AnnouncementModel>();

		int total = jsonObject.optInt("total", 0);
		map.put("total", total);

		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = (JSONObject) jsonArray.opt(i);

				AnnouncementModel model = new AnnouncementModel();
				model.setId(obj.optString("id", ""));
				model.setTitle(obj.optString("title", ""));
				model.setTime(obj.optString("time", ""));
				model.setPreview(obj.optString("preview", ""));

				modelList.add(model);
			}
		}

		map.put("list", modelList);

		return map;
	}

	public static Object getBroadcastDetail(JSONObject jsonObject) {
		AnnouncementModel model = new AnnouncementModel();
		model.setTitle(jsonObject.optString("title", ""));
		model.setContent(jsonObject.optString("content", ""));
		model.setTime(jsonObject.optString("time", ""));

		return model;
	}

	// 获取活动类型列表
	public static Object getEventTypeList(JSONObject jsonObject) {
		HashMap<String, String> map = new HashMap<String, String>();

		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (null != jsonArray) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = (JSONObject) jsonArray.opt(i);
				map.put(obj.optString("id"), obj.optString("display"));
			}
		}

		return map;
	}

	// 获取活动列表
	public static Object getEventList(JSONObject jsonObject) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		int total = jsonObject.optInt("total", 0); // 活动总数
		map.put("total", total);

		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (null != jsonArray) {
			ArrayList<ActiveModel> list = new ArrayList<ActiveModel>();

			for (int i = 0; i < jsonArray.length(); i++) {
				ActiveModel model = new ActiveModel();

				JSONObject obj = (JSONObject) jsonArray.opt(i);
				model.setId(obj.optString("id", ""));
				model.setStime(obj.optString("stime", ""));
				model.setEtime(obj.optString("etime", ""));
				model.setAddress(obj.optString("address", ""));
				model.setTitle(obj.optString("title", ""));
				model.setTypeID(obj.optString("typeID", ""));
				model.setCharge(obj.optInt("charge", 0));
				model.setSponsor(obj.optString("sponsor", ""));
				model.setPreview(obj.optString("preview", ""));
				model.setPosterImage(obj.optString("pic", ""));

				list.add(model);
			}

			map.put("list", list);
		}

		return map;
	}

	// 获取活动详情
	public static Object getEventDetail(JSONObject jsonObject) {
		ActiveModel model = new ActiveModel();

		model.setId(jsonObject.optString("id", ""));
		model.setStime(jsonObject.optString("stime", ""));
		model.setEtime(jsonObject.optString("etime", ""));
		model.setAddress(jsonObject.optString("address", ""));
		model.setTitle(jsonObject.optString("title", ""));
		model.setTypeID(jsonObject.optString("typeID", ""));
		model.setCharge(jsonObject.optInt("charge", 0));
		model.setSponsor(jsonObject.optString("sponsor", ""));
		model.setPreview(jsonObject.optString("preview", ""));
		model.setContent(jsonObject.optString("content", ""));
		model.setPosterImage(jsonObject.optString("pic", ""));

		return model;
	}

	public static Object getEventParticipantList(JSONObject jsonObject) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		ArrayList<ProfileModel> modelList = new ArrayList<ProfileModel>();

		int total = jsonObject.optInt("total", 0);
		map.put("total", total);

		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = (JSONObject) jsonArray.opt(i);

				ProfileModel model = new ProfileModel();
				model.setmId(obj.optString("id", ""));
				model.setName(obj.optString("name", ""));
				model.setGender(obj.optInt("gender", 0));
				model.setProvince(obj.optString("province", ""));
				model.setCity(obj.optString("city", ""));
				model.setOrg(obj.optString("org", ""));
				model.setTitle(obj.optString("title", ""));
				model.setPic(obj.optString("pic", ""));

				modelList.add(model);
			}
		}

		map.put("list", modelList);

		return map;
	}

	private static Object collegeCardApply(JSONObject jsonObject) {
		int errorCode = jsonObject.optInt("rc", 0);
		return errorCode;
	}
	
	private static Object collegeCardStatus(JSONObject jsonObject) {
		int status = jsonObject.optInt("status", -1);
		return status;
	}

	private static Object collegeFeedbackApply(JSONObject jsonObject) {
		int errorCode = jsonObject.optInt("rc", 0);
		return errorCode;
	}

	// 根据选中履历推荐好友列表
	private static Object getSuggestPeopleList(JSONObject jsonObject) {
		ArrayList<ProfileModel> modelList = new ArrayList<ProfileModel>();

		HashMap<String, Object> map = new HashMap<String, Object>();
		String total = jsonObject.optString("total", "0");
		map.put("total", total);
		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				ProfileModel model = new ProfileModel();

				JSONObject obj = (JSONObject) jsonArray.opt(i);
				model.setmFlag("2");
				model.setmId(obj.optString("id", ""));
				model.setCity(obj.optString("city"));
				model.setProvince(obj.optString("province"));
				model.setName(obj.optString("name", ""));
				model.setGender(obj.optInt("gender", 1));
				model.setOrg(obj.optString("org", ""));
				model.setTitle(obj.optString("title", ""));
				model.setPic(obj.optString("pic", ""));

				modelList.add(model);
			}
			map.put("list", modelList);
			return map;

		}

		return null;
	}

	// 我关注的好友列表
	private static Object getMyAttentionsList(JSONObject jsonObject) {
		ArrayList<ProfileModel> modelList = new ArrayList<ProfileModel>();

		HashMap<String, Object> map = new HashMap<String, Object>();
		String total = jsonObject.optString("total", "0");
		map.put("total", total);
		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				ProfileModel model = new ProfileModel();

				JSONObject obj = (JSONObject) jsonArray.opt(i);
				model.setmFlag("1");
				model.setmId(obj.optString("id", ""));
				model.setCity(obj.optString("city"));
				model.setProvince(obj.optString("province"));
				model.setName(obj.optString("name", ""));
				model.setGender(obj.optInt("gender", 1));
				model.setOrg(obj.optString("org", ""));
				model.setTitle(obj.optString("title", ""));
				model.setPic(obj.optString("pic", ""));

				modelList.add(model);
			}
			map.put("list", modelList);
			return map;

		}

		return null;
	}

	// 关注我的人列表
	private static Object getFansList(JSONObject jsonObject) {
		ArrayList<ProfileModel> modelList = new ArrayList<ProfileModel>();

		HashMap<String, Object> map = new HashMap<String, Object>();
		String total = jsonObject.optString("total", "0");
		map.put("total", total);
		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				ProfileModel model = new ProfileModel();

				JSONObject obj = (JSONObject) jsonArray.opt(i);
				model.setmFlag("0");
				model.setmId(obj.optString("id", ""));
				model.setCity(obj.optString("city"));
				model.setProvince(obj.optString("province"));
				model.setName(obj.optString("name", ""));
				model.setGender(obj.optInt("gender", 1));
				model.setOrg(obj.optString("org", ""));
				model.setTitle(obj.optString("title", ""));
				model.setPic(obj.optString("pic", ""));

				modelList.add(model);
			}
			map.put("list", modelList);
			return map;

		}

		return null;
	}

	// 加关注
	private static int getAddAttention(JSONObject jsonObject) {
		int errorCode = jsonObject.optInt("rc", ErrorCode.UNKNOWN);

		return errorCode;
	}

	// 取消关注
	private static int getCancelAttention(JSONObject jsonObject) {
		int errorCode = jsonObject.optInt("rc", ErrorCode.UNKNOWN);

		return errorCode;
	}

	// 查看我加入的圈子列表
	private static Object getGroupMeList(JSONObject jsonObject) {
		ArrayList<GroupModel> modelList = new ArrayList<GroupModel>();

		HashMap<String, Object> map = new HashMap<String, Object>();
		String total = jsonObject.optString("total", "0");
		map.put("total", total);
		map.put("rc", jsonObject.optInt("rc", ErrorCode.UNKNOWN));
		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				GroupModel model = new GroupModel();

				JSONObject obj = (JSONObject) jsonArray.opt(i);
				model.setId(obj.optString("id", ""));
				model.setName(obj.optString("name", ""));
				model.setDesc(obj.optString("desc", ""));
				model.setCreateTime(obj.optString("createTime", ""));

				modelList.add(model);
			}
			map.put("list", modelList);
			return map;

		}

		return null;
	}

	// 查看我创建的圈子列表
	private static Object getGroupMyList(JSONObject jsonObject) {
		ArrayList<GroupModel> modelList = new ArrayList<GroupModel>();

		HashMap<String, Object> map = new HashMap<String, Object>();
		String total = jsonObject.optString("total", "0");
		map.put("total", total);
		map.put("rc", jsonObject.optInt("rc", ErrorCode.UNKNOWN));
		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				GroupModel model = new GroupModel();

				JSONObject obj = (JSONObject) jsonArray.opt(i);
				model.setId(obj.optString("id", ""));
				model.setName(obj.optString("name", ""));
				model.setDesc(obj.optString("desc", ""));
				model.setCreateTime(obj.optString("createTime", ""));

				modelList.add(model);
			}
			map.put("list", modelList);
			return map;

		}

		return null;
	}

	// 所有圈子列表
	private static Object getAllGroupList(JSONObject jsonObject) {
		ArrayList<GroupModel> modelList = new ArrayList<GroupModel>();

		HashMap<String, Object> map = new HashMap<String, Object>();
		String total = jsonObject.optString("total", "0");
		map.put("total", total);
		map.put("rc", jsonObject.optInt("rc", ErrorCode.UNKNOWN));
		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				GroupModel model = new GroupModel();

				JSONObject obj = (JSONObject) jsonArray.opt(i);
				model.setId(obj.optString("id", ""));
				model.setName(obj.optString("name", ""));
				model.setDesc(obj.optString("desc", ""));
				model.setCreateTime(obj.optString("createTime", ""));

				modelList.add(model);
			}
			map.put("list", modelList);
			return map;

		}

		return null;
	}

	// 获取圈子发言列表
	private static Object getCommentList(JSONObject jsonObject) {
		ArrayList<CommentModel> modelList = new ArrayList<CommentModel>();

		HashMap<String, Object> map = new HashMap<String, Object>();
		String total = jsonObject.optString("total", "0");
		map.put("total", total);
		map.put("rc", jsonObject.optInt("rc", ErrorCode.UNKNOWN));
		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				CommentModel model = new CommentModel();

				JSONObject obj = (JSONObject) jsonArray.opt(i);
				model.setId(obj.optString("id", ""));
				model.setContent(obj.optString("content", ""));
				model.setAuthorId(obj.optString("authorId", ""));
				model.setAuthorPic(obj.optString("authorPic", ""));
				model.setAuthorName(obj.optString("authorName", ""));
				model.setTime(obj.optString("time", ""));

				modelList.add(model);
			}
			map.put("list", modelList);
			return map;

		}

		return null;
	}

	// 找同学
	private static Object getSearchList(JSONObject jsonObject) {
		ArrayList<ProfileModel> modelList = new ArrayList<ProfileModel>();

		HashMap<String, Object> map = new HashMap<String, Object>();
		String total = jsonObject.optString("total", "0");
		map.put("total", total);
		map.put("rc", jsonObject.optInt("rc", ErrorCode.UNKNOWN));
		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				ProfileModel model = new ProfileModel();

				JSONObject obj = (JSONObject) jsonArray.opt(i);
				model.setmFlag("2");
				model.setmId(obj.optString("id", ""));
				model.setName(obj.optString("name", ""));
				model.setGender(obj.optInt("gender", 1));
				model.setProvince(obj.optString("province"));
				model.setCity(obj.optString("city"));
				model.setOrg(obj.optString("org", ""));
				model.setTitle(obj.optString("title", ""));
				model.setPic(obj.optString("pic", ""));
				modelList.add(model);
			}
			map.put("list", modelList);
			return map;

		}

		return null;
	}

	// 查看圈子成员
	private static Object getParticipantList(JSONObject jsonObject) {
		ArrayList<ProfileModel> modelList = new ArrayList<ProfileModel>();

		HashMap<String, Object> map = new HashMap<String, Object>();
		String total = jsonObject.optString("total", "0");
		map.put("total", total);
		map.put("rc", jsonObject.optInt("rc", ErrorCode.UNKNOWN));
		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				ProfileModel model = new ProfileModel();

				JSONObject obj = (JSONObject) jsonArray.opt(i);
				model.setmFlag("2");
				model.setmId(obj.optString("id", ""));
				model.setName(obj.optString("name", ""));
				model.setGender(obj.optInt("gender", 1));
				model.setProvince(obj.optString("province"));
				model.setCity(obj.optString("city"));
				model.setOrg(obj.optString("org", ""));
				model.setTitle(obj.optString("title", ""));
				model.setPic(obj.optString("pic", ""));
				modelList.add(model);
			}
			map.put("list", modelList);
			return map;

		}

		return null;
	}

	// 加入圈子
	private static int getGroupJoin(JSONObject jsonObject) {
		int errorCode = jsonObject.optInt("rc", ErrorCode.UNKNOWN);

		return errorCode;
	}

	// 删除圈子
	private static int getGroupDelete(JSONObject jsonObject) {
		int errorCode = jsonObject.optInt("rc", ErrorCode.UNKNOWN);

		return errorCode;
	}

	// 退出圈子
	private static int getGroupQuit(JSONObject jsonObject) {
		int errorCode = jsonObject.optInt("rc", ErrorCode.UNKNOWN);

		return errorCode;
	}

	// 退出圈子
	private static int getCreateCircle(JSONObject jsonObject) {
		int errorCode = jsonObject.optInt("rc", ErrorCode.UNKNOWN);

		return errorCode;
	}

	// 退出圈子
	private static int getPublishComment(JSONObject jsonObject) {
		int errorCode = jsonObject.optInt("rc", ErrorCode.UNKNOWN);

		return errorCode;
	}

	private static Object getTimelineList(JSONObject jsonObject) {
		ArrayList<TimelineModel> modelList = new ArrayList<TimelineModel>();

		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				TimelineModel timeline = new TimelineModel();

				JSONObject tiemlineObj = (JSONObject) jsonArray.opt(i);
				timeline.setId(tiemlineObj.optString("id", ""));
				timeline.setTitle(tiemlineObj.optString("title", ""));
				timeline.setDescription(tiemlineObj.optString("desc", ""));
				timeline.setIndustry(tiemlineObj.optString("industryId", ""));
				timeline.setStartTime(tiemlineObj.optString("stime", ""));
				timeline.setEndTime(tiemlineObj.optString("etime", ""));
				timeline.setProvince(tiemlineObj.optString("province", ""));
				timeline.setCity(tiemlineObj.optString("city", ""));
				timeline.setDistrict(tiemlineObj.optString("district", ""));
				timeline.setOrg(tiemlineObj.optString("org", ""));
				timeline.setImgUrl(tiemlineObj.optString("pic", ""));

				modelList.add(timeline);
			}

			return modelList;

		}

		return null;
	}

	// 添加时间轴结点
	private static Object timelineNodeCreate(JSONObject jsonObject) {
		int errorCode = jsonObject.optInt("rc", ErrorCode.UNKNOWN);
		return errorCode;
	}

	private static Object timelineNodeUpdate(JSONObject jsonObject) {
		int errorCode = jsonObject.optInt("rc", ErrorCode.UNKNOWN);
		return errorCode;
	}

	private static Object timelineNodeDelete(JSONObject jsonObject) {
		int errorCode = jsonObject.optInt("rc", ErrorCode.UNKNOWN);
		return errorCode;
	}

	// 获取母校信息
	private static Object getCollegeInfo(JSONObject jsonObject) {
		SchoolModel school = new SchoolModel();
		school.setmId(jsonObject.optString("id", ""));
		school.setmLogoUrl(jsonObject.optString("logoURL", ""));
		school.setmName(jsonObject.optString("name", ""));
		school.setmDesc(jsonObject.optString("introduct", ""));
		school.setmWeiboScreenName(jsonObject.optString("sinaWeibo", ""));

		return school;
	}

	private static Object getMediaTopList(JSONObject jsonObject) {
		ArrayList<MediaModel> mediaList = new ArrayList<MediaModel>();

		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				MediaModel media = new MediaModel();

				JSONObject mediaObj = (JSONObject) jsonArray.opt(i);
				media.setId(mediaObj.optString("id", ""));
				media.setTitle(mediaObj.optString("title", ""));
				media.setTime(mediaObj.optString("time", ""));
				// media.setPic(ImageUtil.getTestImageURL1());
				media.setPic(mediaObj.optString("pic", ""));

				mediaList.add(media);
			}

		}

		return mediaList;
	}

	private static Object getMediaList(JSONObject jsonObject) {
		ArrayList<MediaModel> mediaList = new ArrayList<MediaModel>();

		HashMap<String, Object> map = new HashMap<String, Object>();
		String total = jsonObject.optString("total", "0");
		map.put("total", total);
		map.put("rc", jsonObject.optInt("rc", ErrorCode.UNKNOWN));

		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				MediaModel media = new MediaModel();

				JSONObject mediaObj = (JSONObject) jsonArray.opt(i);
				media.setId(mediaObj.optString("id", ""));
				media.setTitle(mediaObj.optString("title", ""));
				media.setTime(mediaObj.optString("time", ""));
				media.setPreview(mediaObj.optString("preview", ""));
				// media.setPic(ImageUtil.getTestImageURL1());
				media.setPic(mediaObj.optString("pic", ""));

				// 如果没有图片，则忽略掉该消息。
				mediaList.add(media);

			}

		}

		map.put("list", mediaList);

		return map;
	}

	private static Object getGalaryDetail(JSONObject jsonObject) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("time", jsonObject.optString("time"));
		map.put("pic", jsonObject.optString("pic"));
		map.put("thumbnail", jsonObject.optString("thumbnail"));

		return map;
	}

	private static Object getProfileMeConfig(JSONObject jsonObject) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("email", jsonObject.optString("email"));
		map.put("qq", jsonObject.optString("qq"));
		map.put("mobile", jsonObject.optString("mobile"));

		return map;
	}

	private static Object getProfileMeUpdate(JSONObject jsonObject) {

		return jsonObject.optString("rc");
	}

	private static Object getDeleteMember(JSONObject jsonObject) {

		return jsonObject.optString("rc");
	}

	private static Object getMediaDetail(JSONObject jsonObject) {
		MediaModel media = new MediaModel();
		media.setType(jsonObject.optInt("type", 1));
		media.setTitle(jsonObject.optString("title", ""));
		media.setTime(jsonObject.optString("time", ""));
		media.setContent(jsonObject.optString("content", ""));

		ArrayList<String> picList = new ArrayList<String>();
		JSONArray jsonArray = jsonObject.optJSONArray("pics");
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject picsObj = (JSONObject) jsonArray.opt(i);
				picList.add(picsObj.optString("url", ""));
			}
			media.setPics(picList);
		}

		// media.setPics(ImageUtil.getTestImageURL2());

		return media;
	}

	private static Object getConfigOrgList(JSONObject jsonObject) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("rc", jsonObject.optInt("rc", ErrorCode.UNKNOWN));

		JSONArray jsonArray = jsonObject.optJSONArray("list");
		ArrayList<OrgOneModel> orgOneList = new ArrayList<OrgOneModel>();
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				OrgOneModel orgOneModel = new OrgOneModel();

				JSONObject deptObj = (JSONObject) jsonArray.opt(i);
				orgOneModel.setCode(Integer.valueOf(deptObj.optString("id", "")));
				orgOneModel.setName(deptObj.optString("display", ""));

				JSONArray majorArray = deptObj.optJSONArray("list");
				ArrayList<OrgTwoModel> majorList = new ArrayList<OrgTwoModel>();
				for (int j = 0; j < majorArray.length(); j++) {
					OrgTwoModel majorModel = new OrgTwoModel();
					JSONObject majorObj = (JSONObject) majorArray.opt(j);
					majorModel.setCode(Integer.valueOf(majorObj.optString("id", "")));
					majorModel.setName(majorObj.optString("display", ""));
					majorList.add(majorModel);
				}
				orgOneModel.setOrgTwoModels(majorList);
				orgOneList.add(orgOneModel);

			}

		}

		map.put("list", orgOneList);

		return map;
	}

	private static int getProfileMeCreateList(JSONObject jsonObject) {
		return jsonObject.optInt("rc", ErrorCode.UNKNOWN);
	}

	private static Object getConfigIndustryList(JSONObject jsonObject) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("rc", jsonObject.optInt("rc", ErrorCode.UNKNOWN));

		JSONArray jsonArray = jsonObject.optJSONArray("list");
		ArrayList<IndustryModel> list = new ArrayList<IndustryModel>();
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				IndustryModel model = new IndustryModel();

				JSONObject deptObj = (JSONObject) jsonArray.opt(i);
				model.setId(deptObj.optString("id", ""));
				model.setDisplay(deptObj.optString("display", ""));

				list.add(model);

			}

		}

		map.put("list", list);

		return map;
	}

	private static Object getGalaryList(JSONObject jsonObject) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("rc", jsonObject.optInt("rc", ErrorCode.UNKNOWN));
		map.put("total", jsonObject.optString("total", "0"));

		JSONArray jsonArray = jsonObject.optJSONArray("list");
		ArrayList<ImageModel> list = new ArrayList<ImageModel>();
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				ImageModel model = new ImageModel();

				JSONObject deptObj = (JSONObject) jsonArray.opt(i);
				model.setId(deptObj.optString("id", ""));
				model.setTitle(deptObj.optString("title", ""));
				model.setTime(deptObj.optString("time", ""));
				model.setAuthor(deptObj.optString("author", ""));
				model.setThumbnail(deptObj.optString("thumbnail", ""));

				list.add(model);

			}

		}

		map.put("list", list);

		return map;
	}

	private static Object getConfigDeptList(JSONObject jsonObject) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("rc", jsonObject.optInt("rc", ErrorCode.UNKNOWN));

		JSONArray jsonArray = jsonObject.optJSONArray("list");
		ArrayList<DeptModel> deptList = new ArrayList<DeptModel>();
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				DeptModel deptModel = new DeptModel();

				JSONObject deptObj = (JSONObject) jsonArray.opt(i);
				deptModel.setCode(Integer.valueOf(deptObj.optString("id", "")));
				deptModel.setName(deptObj.optString("display", ""));

				JSONArray majorArray = deptObj.optJSONArray("list");
				ArrayList<MajorModel> majorList = new ArrayList<MajorModel>();
				for (int j = 0; j < majorArray.length(); j++) {
					MajorModel majorModel = new MajorModel();
					JSONObject majorObj = (JSONObject) majorArray.opt(j);
					majorModel.setCode(Integer.valueOf(majorObj.optString("id", "")));
					majorModel.setName(majorObj.optString("display", ""));
					majorList.add(majorModel);
				}
				deptModel.setMajors(majorList);
				deptList.add(deptModel);

			}

		}

		map.put("list", deptList);

		return map;
	}

	// ////////////////////////////////////////////////////////////////////////////

	private static HashMap<String, String> JSONObject2Map(JSONObject jsonObject) {
		HashMap<String, String> responseMap = new HashMap<String, String>();

		try {
			@SuppressWarnings("unchecked")
			Iterator<String> keys = jsonObject.keys();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				responseMap.put(key, jsonObject.getString(key));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseMap;
	}

	private static String getErrorMsg(int errorCode) {
		if (errorCode == ErrorCode.SYSTEM_MAINTENANCE) {
			return "服务器维护中，请稍候再试。";
		} else if (errorCode == ErrorCode.PARAM_ERROR) {
			return "参数异常，请重试。";
		} else if (errorCode == ErrorCode.PERMISSION_ERROR) {
			return "权限不足，请与管理员联系。";
		} else if (errorCode == ErrorCode.SESSIONID_ERROR) {
			return "会话超时，请重新登录。";
		} else if (errorCode == ErrorCode.CLIENTID_ERROR) {
			return "无效的终端类型。";
		} else if (errorCode == ErrorCode.VERSION_ERROR) {
			return "协议版本异常，请重试。";
		} else if (errorCode == ErrorCode.CACHE_ERROR) {
			return "服务器缓存异常，请重试。";
		} else if (errorCode == ErrorCode.DATABASE_ERROR) {
			return "服务器数据库异常，请重试。";
		} else if (errorCode == ErrorCode.UNKNOWN) {
			return "未知错误。";
		} else {
			return "未定义错误";
		}
	}

}
