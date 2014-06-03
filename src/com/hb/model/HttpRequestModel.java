package com.hb.model;

public class HttpRequestModel {

	private String requestId;
	private String url;
	private String method;
	private String contentType;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
	public String getContentType(){
		return (null == contentType||"".equals(contentType))?"application/json":contentType;
	}
	
	public void setContentType(String contentType){
		this.contentType = contentType;
	}

	@Override
	public String toString() {
		return this.requestId;
	}

}
