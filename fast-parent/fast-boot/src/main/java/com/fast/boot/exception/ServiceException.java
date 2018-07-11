package com.fast.boot.exception;

public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = -8634700792767837033L;
	
	public ServiceException(String message){
	    super(message);
	}
}
