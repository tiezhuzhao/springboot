package com.fast.boot.sso;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.fast.boot.utils.CommonUtils;

import redis.clients.jedis.ShardedJedis;

@Configuration
@ConfigurationProperties(prefix = SSOProperties.STATIC_PREFIX)
public class SSOProperties {

	public final static String STATIC_PREFIX = "sso";

	private String appKey = "";// 客户端标识
	private String secretKey = "";// 客户端秘钥
	private String cookieName = "";// Cookie 名称
	private String cookieDomain = "";// Cookie 所在域
	private String cookiePath = "/";// Cookie 域路径
	private int cookieMaxage = -1;// Cookie 超时时间 -1 浏览器关闭时自动删除 0 立即删除 120  表示Cookie有效期2分钟(以秒为单位)
	private boolean cookieBrowser = false;// Cookie 开启浏览器版本校验
	private boolean cookieCheckip = false;// Cookie 开启IP校验
	private String loginUrl = "";// SSO 登录地址
	private String logoutUrl = "";// SSO 退出地址
	private String cacheHost;
    private Integer cachePort;
    private String password;
	
	/**
	 * 自定义 SSOCache Class
	 */
	public ShardedJedis getCache() {
		return CommonUtils.getConfigCache(cacheHost, cachePort, password);
	}
	
	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getCookieName() {
		return cookieName;
	}

	public void setCookieName(String cookieName) {
		this.cookieName = cookieName;
	}

	public String getCookieDomain() {
		return cookieDomain;
	}

	public void setCookieDomain(String cookieDomain) {
		this.cookieDomain = cookieDomain;
	}

	public String getCookiePath() {
		return cookiePath;
	}

	public void setCookiePath(String cookiePath) {
		this.cookiePath = cookiePath;
	}

	public int getCookieMaxage() {
		return cookieMaxage;
	}

	public void setCookieMaxage(int cookieMaxage) {
		this.cookieMaxage = cookieMaxage;
	}

	public boolean isCookieBrowser() {
		return cookieBrowser;
	}

	public void setCookieBrowser(boolean cookieBrowser) {
		this.cookieBrowser = cookieBrowser;
	}

	public boolean isCookieCheckip() {
		return cookieCheckip;
	}

	public void setCookieCheckip(boolean cookieCheckip) {
		this.cookieCheckip = cookieCheckip;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getLogoutUrl() {
		return logoutUrl;
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	public String getCacheHost() {
		return cacheHost;
	}

	public void setCacheHost(String cacheHost) {
		this.cacheHost = cacheHost;
	}

	public Integer getCachePort() {
		return cachePort;
	}

	public void setCachePort(Integer cachePort) {
		this.cachePort = cachePort;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
