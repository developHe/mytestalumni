package com.hb.model;

import java.io.Serializable;

public class SchoolModel implements Serializable{

	private static final long serialVersionUID = 4742626869001520846L;
	
	private String mId 					= null;
	private String mName 				= null;
	private String mLogoUrl 			= null;
	private String mDesc 				= null;

	// 要取校官方微博需要至少提供其中一项
	private String mWeiboScreenName 	= null;
	private String mWeiboUid 			= null;

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getmLogoUrl() {
		return mLogoUrl;
	}

	public void setmLogoUrl(String mLogoUrl) {
		this.mLogoUrl = mLogoUrl;
	}

	public String getmDesc() {
		return mDesc;
	}

	public void setmDesc(String mDesc) {
		this.mDesc = mDesc;
	}

	public String getmWeiboScreenName() {
		return mWeiboScreenName;
	}

	public void setmWeiboScreenName(String mWeiboScreenName) {
		this.mWeiboScreenName = mWeiboScreenName;
	}

	public String getmWeiboUid() {
		return mWeiboUid;
	}

	public void setmWeiboUid(String mWeiboUid) {
		this.mWeiboUid = mWeiboUid;
	}

	
	public String toString(){
		return this.mName;
	}
}
