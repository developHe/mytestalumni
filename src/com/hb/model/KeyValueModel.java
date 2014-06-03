package com.hb.model;

import java.io.Serializable;
import java.util.HashMap;


public class KeyValueModel implements Serializable{

	private static final long serialVersionUID = -3811194226649140134L;

	private String key;
	private String value;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
