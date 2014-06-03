package com.hb.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 圈子
 * 
 * @author liaojia
 * 
 */
public class GroupModel implements Serializable{

	private static final long serialVersionUID = -8443539917708958943L;
	
	private String id;
	private String name;
	private String desc;
	private String createTime;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = checkNull(name);
	}
	
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = checkNull(desc);
	}
	
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = checkNull(createTime.substring(0, 10));
	}

	private String checkNull(String s)
	{
		if(s != null && s.length() != 0 && !s.contains("null")){
			return s;
		}else{
			return "未知";
		}
	}
}
