package com.fast.boot.sso;

/**
 * 单点登录异常信息
 *
 */
public class SSOException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SSOException(String message) {
		super(message);
	}
	public SSOException(Throwable throwable) {
		super(throwable);
	}
	public SSOException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
}
