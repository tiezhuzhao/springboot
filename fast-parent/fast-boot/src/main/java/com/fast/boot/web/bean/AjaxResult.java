package com.fast.boot.web.bean;

import java.io.Serializable;

/**
 * 
 * @author zhaoz
 *
 */
public class AjaxResult implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final int UN_AUTH = -3;// 没有权限
	public static final int UN_LOGIN = -2;// 未登录
	public static final int ERROR = -1;// 错误
	public static final int FAILURE = 0;// 失败
	public static final int SUCCESS = 1;// 成功
	
	private int code;// 返回参数
	private String message;// 返回提示信息
	private Object result;// 返回对象

	public AjaxResult() {}
	
	public AjaxResult(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
