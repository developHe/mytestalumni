package com.hb.model;

import java.io.Serializable;

/*
 * 个人信息
 */
public class ProfileModel implements Serializable {

	private static final long serialVersionUID = -4122236788754937349L;

	/* 个人基本信息 */

	private String mFlag = "2";// 0,关注我的人 1,我关注的人 2,与关注无关
	private String mId = null; // id

	private String name = null;
	private int gender = 0; // 性别，1男，0女

	private String idCardNo = "";
	private String mobile = "";
	private String email = "";
	private String qq = "";
	private String pic = null;// 头像
	private int type = 1; // 1学生校友， ２教工校友

	// type = 1
	private String deptId;
	private String majorId;
	private String className;
	private String adYear;

	// type = 2
	private String org1Id;
	private String org2Id;
	private String empNo;

	private String province;
	private String city;
	private String org;
	private String title;

	public String getmFlag() {
		return mFlag;
	}

	public void setmFlag(String mFlag) {
		this.mFlag = mFlag;
	}

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getMajorId() {
		return majorId;
	}

	public void setMajorId(String majorId) {
		this.majorId = majorId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getAdYear() {
		return adYear;
	}

	public void setAdYear(String adYear) {
		this.adYear = adYear;
	}

	public String getOrg1Id() {
		return org1Id;
	}

	public void setOrg1Id(String org1Id) {
		this.org1Id = org1Id;
	}

	public String getOrg2Id() {
		return org2Id;
	}

	public void setOrg2Id(String org2Id) {
		this.org2Id = org2Id;
	}

	public String getEmpNo() {
		return empNo;
	}

	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String toString() {
		return this.name;
	}

	private String checkNull(String s) {
		if (s != null && s.length() != 0 && !s.contains("null")) {
			return s;
		} else {
			return "未知";
		}
	}
}
