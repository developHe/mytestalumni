package com.hb.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 学校活动
 * 
 * @author sth
 * 
 */
public class ActiveModel implements Serializable{

	private static final long serialVersionUID = -3811194226649140134L;

	private static HashMap<String, String> activeTypeMap = new HashMap<String, String>();

	private String id;
	private String title;
	private String address;
	private String stime;
	private String etime;
	private String typeID;
	private String posterImage; // 宣传海报
	private int charge; // 是否收费，0免费；1收费
	private String sponsor; // 主办方，可能为空
	private String preview;
	private String content;

	public static HashMap<String, String> getActiveTypeMap() {
		return activeTypeMap;
	}

	public static void setActiveTypeMap(HashMap<String, String> activeTypeMap) {
		ActiveModel.activeTypeMap = activeTypeMap;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStime() {
		return stime;
	}

	public void setStime(String stime) {
		this.stime = stime;
	}

	public String getEtime() {
		return etime;
	}

	public void setEtime(String etime) {
		this.etime = etime;
	}

	public String getTypeName() {
		if (activeTypeMap.size() == 0)
			return typeID;
		
		return activeTypeMap.get(typeID);
	}

	public void setTypeID(String typeID) {
		this.typeID = typeID;
	}
	
	public String getPosterImage() {
//		String imageURL[] = new String[]{"http://pic15.nipic.com/20110721/7920439_160535163000_2.jpg",
//				"http://res.book.ifeng.com/attachments/2010/09/09/rd_or_3799f9ff07ab2a23ece61167ab4a1065.jpg",
//				"http://pic16.nipic.com/20110903/4708490_085248495000_2.jpg",
//				"http://pic4.nipic.com/20091210/3790843_181035285769_2.jpg"};
//		
//		int random = (int)(Math.random() * imageURL.length);
//		return imageURL[random];
		return posterImage;
	}

	public void setPosterImage(String posterImage) {
		this.posterImage = posterImage;
	}

	public String getCharge() {
		return this.charge==0?"免费":"收费";
	}

	public void setCharge(int charge) {
		this.charge = charge;
	}

	public String getSponsor() {
		return sponsor;
	}

	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}

	public String getPreview() {
		return preview;
	}

	public void setPreview(String preview) {
		this.preview = preview.replace("\r", "\n");
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content.replace("\r", "\n");
	}

}
