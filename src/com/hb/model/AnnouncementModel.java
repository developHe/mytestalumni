package com.hb.model;

import java.io.Serializable;

public class AnnouncementModel implements Serializable {

	private static final long serialVersionUID = -8307674336517627131L;

	private String mId = null;
	private String mTitle = null;
	private String mTime = null;
	private String mPreview = null;
	private String mContent = null;

	public AnnouncementModel() {

	}

	public String getId() {
		return mId;
	}

	public void setId(String id) {
		this.mId = id;
	}

	public String getTitle() {
		return this.mTitle;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}

	public String getTime() {
		return this.mTime;
	}

	public void setTime(String time) {
		this.mTime = time;
	}
	
	public String getPreview(){
		return this.mPreview;
	}
	
	public void setPreview(String preview){
		this.mPreview = preview.replace("\r", "\n");
	}

	public String getContent() {
		return this.mContent;
	}

	public void setContent(String content) {
		this.mContent = content.replace("\r", "\n");;
	}

	
	public String toString(){
		return this.mTitle;
	}
}
