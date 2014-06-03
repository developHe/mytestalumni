package com.hb.model;

import java.io.InputStream;
import java.io.Serializable;

/**
 * 输入流
 * @author 何斌
 *
 */
public class InputStreamModel implements Serializable{
	private static final long serialVersionUID = 4742626869001520256L;
	private InputStream input = null;
	private String path = null;
	private String url  = null;
	
	public void setURL(String url){
		this.url = url;
	}
	
	public void setPath(String path){
		this.path = path;
	}
	
	public void setStream(InputStream input){
		this.input = input;
	}
	
	public InputStream getStream(){
		return this.input;
	}
	
	public String getPath(){
		return this.path;
	}
	
	public String getUrl(){
		return this.url;
	}
}
