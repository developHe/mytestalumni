package com.hb.model;

import java.io.Serializable;

public class CommentModel implements Serializable {

	private static final long serialVersionUID = -2501441005917099605L;

	private String id;
	private String content;
	private String authorId;
	private String authorPic;
	private String authorName;
	private String time; // yyyy-MM-dd HH:mm

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public String getAuthorPic() {
		return authorPic;
	}

	public void setAuthorPic(String authorPic) {
		this.authorPic = authorPic;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String toString() {
		return this.content;
	}

}
