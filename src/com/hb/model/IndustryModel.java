package com.hb.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 行业
 * 
 * @author liaojia
 * 
 */
public class IndustryModel implements Serializable{

	private static final long serialVersionUID = -8443539917708958943L;
	
	private String id;
	private String display;

	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
    public String toString() {
        return display;
    }
}
