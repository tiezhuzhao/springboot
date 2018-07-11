package com.fast.boot.open;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 静态资源文件配置
 */
@Configuration
@ConfigurationProperties(prefix = OpenAppProperties.STATIC_PREFIX)
public class OpenAppProperties {
	
	 public final static String STATIC_PREFIX = "openapp";
	 
	private Long clientKey;// 应用编号
	private String secretKey;// 应用秘钥
	private String serverUrl;// 开放平台地址
	private String platformUrl;// 平台首页
	private String profile;// 环境

	public Long getClientKey() {
		return clientKey;
	}
	public void setClientKey(Long clientKey) {
		this.clientKey = clientKey;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public String getServerUrl() {
		return serverUrl;
	}
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}
	public String getPlatformUrl() {
		return platformUrl;
	}
	public void setPlatformUrl(String platformUrl) {
		this.platformUrl = platformUrl;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}

}