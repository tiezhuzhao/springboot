package com.fast.boot.sso;

/**
 * 单点登录常量
 */
public class SSOConst {
	
	/**
	 * SSO 动态设置 Cookie 参数
	 * <p>
	 * -1 浏览器关闭时自动删除 0 立即删除 120 表示Cookie有效期2分钟(以秒为单位)
	 * </p>
	 */
	public static final String SSO_COOKIE_MAXAGE = "sso_cookie_maxage";
	
	/**
	 * 单点登录编码方式
	 */
	public static final String SSO_ENCODING = "UTF-8";
	
	/**
	 * cookie加密分隔符
	 */
	public static final String CUT_SYMBOL = "#";
	
	/**
	 * SSO 跳转参数命名
	 */
	public static final String REDIRECT_URL = "redirectUrl";

	/**
	 * SSO 缓存前缀
	 */
	public static final String SSOUSERKEY_PRE = "SSOUSERKEY_";
	
	/**
	 * SSO session登录用户前缀
	 */
	public static final String SSOLOGINUSER = "SSOLOGINUSER";
	
	/**
	 * 踢出用户逻辑标记
	 */
	public static final String SSO_KICK_FLAG = "SSOKickFlag";
	public static final String SSO_KICK_USER = "SSOKickUser";

}
