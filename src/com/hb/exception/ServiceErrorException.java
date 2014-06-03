package com.hb.exception;

public class ServiceErrorException extends Exception {

	private static final long serialVersionUID = -7359568581865095930L;
	
	private int errorCode;
	
	public ServiceErrorException(int errorCode, String message){
		super(message);
		
		this.errorCode = errorCode;
	}
	
	public int getErrorCode(){
		return this.errorCode;
	}

}
