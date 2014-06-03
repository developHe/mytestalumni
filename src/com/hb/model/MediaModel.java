package com.hb.model;

import java.io.Serializable;
import java.util.ArrayList;

public class MediaModel implements Serializable {

	private static final long serialVersionUID = 614234352745638089L;

	public String id = "";
	public int type = 1; // 类型：1校友动态；2通知公告；3母校动态；4回馈母校
	public String title = "";
	public String time = "";
	public String preview = "";
	public String content = "";
	public String pic = "";
	public ArrayList<String> pics = new ArrayList<String>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPreview() {
		return preview;
	}

	public void setPreview(String preview) {
		this.preview = preview;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPic() {
		return (null==pic||pic.equals("")||pic.equals("null"))?"http://www.cnu.edu.cn/download.jsp?attachSeq=970&filename=20081005173959422.jpg":pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public ArrayList<String> getPics() {
		return pics;
	}

	public void setPics(ArrayList<String> pics) {
		this.pics = pics;
	}

	public void setPics(String[] pic) {
		for (int i = 0; i < pic.length; i++) {
			this.pics.add(pic[i]);
		}
	}

	public String toString() {
		return this.title;
	}

}
