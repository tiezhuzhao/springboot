package com.fast.boot.bean;

import java.io.Serializable;

import com.fast.boot.sso.SSOConst;


/**
 * 基本账号信息
 */
public class LoginUser implements Serializable {
	
private static final long serialVersionUID = 1L;

	/* 正常 */
	public final static int FLAG_NORMAL = 0;
	/* 缓存宕机 */
	public final static int FLAG_CACHE_SHUT = 1;
	
	private Long id;// 用户编号
	private String name;// 用户名称
	private String username;// 用户账号
	private Short userType;// 用户类型
	private String loginIp;//IP地址
	private String userFace;// 用户头像
	private long loginTime;// 登录时间
	private String accessToken;// 访问令牌
	
	private int flag = FLAG_NORMAL;
	
	// 环信账号
	private String easemobAccount;
	private String easemobPassword;
	
	/**
	 * 生成USER缓存主键
	 */
	public String toCacheKey() {
		return SSOConst.SSOUSERKEY_PRE+this.getId();
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Short getUserType() {
		return userType;
	}
	public void setUserType(Short userType) {
		this.userType = userType;
	}
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	public long getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getUserFace() {
		return userFace;
	}
	public void setUserFace(String userFace) {
		this.userFace = userFace;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getEasemobAccount() {
		return easemobAccount;
	}
	public void setEasemobAccount(String easemobAccount) {
		this.easemobAccount = easemobAccount;
	}
	public String getEasemobPassword() {
		return easemobPassword;
	}
	public void setEasemobPassword(String easemobPassword) {
		this.easemobPassword = easemobPassword;
	}
}