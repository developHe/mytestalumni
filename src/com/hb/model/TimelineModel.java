package com.hb.model;

import java.io.Serializable;

/*
 * 表示一条履历数据
 */

public class TimelineModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TimelineModel() {
	}

	private String mId = null;
	
	private String mTitle = null;
	
	private String mStartTime = null;
	private String mEndTime = null;

	private String mDescription = null;
	private String mIndustry = null;
	
	private String mProvince = null;
	private String mCity = null;
	private String mDistrict = null;

	private String mSchool = null;
	private String mMajor = null;

	private String mCompany = null;
	private String mPosition = null;

	private String mOrg = null;

	private String mImgUrl = null;
	
	
	public String getIndustry(){
		return mIndustry;
	}
	
	public void setIndustry(String mIndustry){
		this.mIndustry = mIndustry;
	}
	public String getOrg() {
		return mOrg;
	}

	public void setOrg(String mOrg) {
		this.mOrg = mOrg;
	}
	
	public String getid(){
		return this.mId;
	}
	
	public String getTitle(){
		return this.mTitle;
	}

	public String getStartTime() {
		return this.mStartTime;
	}

	public String getEndTime() {
		return this.mEndTime;
	}

	public String getDescription() {
		return this.mDescription;
	}
	
	public String getProvince(){
		return this.mProvince;
	}
	
	public String getCity(){
		return this.mCity;
	}
	
	public String getDistrict(){
		return this.mDistrict;
	}

	public String getSchool() {
		return this.mSchool;
	}

	public String getMajor() {
		return this.mMajor;
	}

	public String getCompany() {
		return this.mCompany;
	}

	public String getPosition() {
		return this.mPosition;
	}

	public String getImgUrl() {
		return this.mImgUrl;
	}
	
	public void setId(String id){
		this.mId = id;
	}

	public void setTitle(String title){
		this.mTitle = title;
	}
	
	public void setStartTime(String time) {
		this.mStartTime = time;
	}

	public void setEndTime(String time) {
		this.mEndTime = time;
	}

	public void setDescription(String s) {
		this.mDescription = s;
	}
	
	public void setProvince(String str){
		this.mProvince = str;
	}
	
	public void setCity(String city){
		this.mCity = city;
	}

	public void setDistrict(String str){
		this.mDistrict = str;
	}
	public void setSchool(String s) {
		this.mSchool = s;
	}

	public void setMajor(String s) {
		this.mMajor = s;
	}

	public void setCompany(String s) {
		this.mCompany = s;
	}

	public void setPosition(String s) {
		this.mPosition = s;
	}

	public void setImgUrl(String s) {
		this.mImgUrl = s;
	}

	public boolean isSchool() {
		return this.mSchool != null;
	}
}
