package com.fast.boot.sso;

import java.util.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.fast.boot.bean.LoginUser;
import com.fast.boot.utils.CommonUtils;
import com.fast.boot.utils.FastJsonUtils;
import com.fast.boot.utils.lang.StringUtils;
import com.fast.boot.utils.mapper.JsonMapper;
import com.fast.boot.utils.tools.encrypt.AES;
import com.fast.boot.utils.tools.encrypt.MD5;

import redis.clients.jedis.ShardedJedis;

/**
 * 单点登录工具类
 */
public class SSOHelper {

	private static final Logger logger = Logger.getLogger("SSOHelper");
	
	private final static JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();
	
	/**
	 * 获取登录用户
	 * @param request
	 * @param properties
	 * @return
	 */
	public static LoginUser getLoginUser(HttpServletRequest request, SSOProperties properties, boolean fromSession) {
		// 从cookie中获取登录信息
		LoginUser user = getUserFromCookie(request, properties);
		if(user!=null) {
			if(fromSession) {
				// 从session中获取登录信息
				LoginUser sessionUser = getUserFromSession(request);
				if(sessionUser!=null) {
					if(user.getId().equals(sessionUser.getId())&&user.getLoginTime()==sessionUser.getLoginTime()) {
						return sessionUser;
					} else {
						removeUserToSession(request);
					}
				}
			}
			// 缓存中恢复登录信息
			ShardedJedis cache = properties.getCache();
			if (cache != null) {
				try {
					String cacheUser = cache.get(user.toCacheKey());
					LoginUser cacheuser = jsonMapper.fromJson(cacheUser, LoginUser.class);
					if(cacheuser != null) {
						/*
						 * 验证 cookie 与 cache 中 token 登录时间是否<br>
						 * 不一致返回 null
						 */
						if(user.getLoginTime() == cacheuser.getLoginTime()){
							setUserToSession(request, cacheuser);
							return cacheuser;
						} else {
							logger.severe("Login time is not consistent or kicked out.");
							request.setAttribute(SSOConst.SSO_KICK_FLAG, SSOConst.SSO_KICK_USER);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/**
	 * 获取登录用户
	 * @param request
	 * @param properties
	 * @return
	 */
	public static LoginUser getLoginUser(HttpServletRequest request, SSOProperties properties) {
		return getLoginUser(request, properties, true);
	}
	
	/**
	 * 获取登录信息
	 * @param request
	 * @return
	 */
	public static LoginUser getUserFromSession(HttpServletRequest request) {
		Object user = request.getSession().getAttribute(SSOConst.SSOLOGINUSER);
		if(user!=null) {
			return (LoginUser)user;
		}
		return null;
	}

	/**
	 * 设置登录信息
	 * @param request
	 * @param cacheUser
	 */
	private static void setUserToSession(HttpServletRequest request, LoginUser loginUser) {
		request.getSession().setAttribute(SSOConst.SSOLOGINUSER, loginUser);
	}
	
	/**
	 * 设置登录信息
	 * @param request
	 * @param cacheUser
	 */
	private static void removeUserToSession(HttpServletRequest request) {
		request.getSession().removeAttribute(SSOConst.SSOLOGINUSER);
	}

	/**
	 * 设置登录用户
	 * 
	 * <p>
	 * 最后一个参数 true 销毁当前JSESSIONID. 创建可信的 JSESSIONID 防止伪造 SESSIONID 攻击
	 * </p>
	 * <p>
	 * 最后一个参数 false 只设置 cookie
	 * </p>
	 * 
	 * <p>
	 * request.setAttribute(SSOConst.SSO_COOKIE_MAXAGE, maxAge);<br>
	 * 可以动态设置 Cookie maxAge 超时时间 ，优先于配置文件的设置，无该参数 - 默认读取配置文件数据 。<br>
	 * maxAge 定义：-1 浏览器关闭时  自动删除 0    立即删除 120 表示Cookie有效期2分钟(以秒为单位)
	 * </p>
	 * 
	 * 
	 * @param request
	 * @param response
	 * @param properties
	 * @param user 票据
	 * @param invalidate 销毁当前 JSESSIONID
	 * @return
	 */
	public static boolean setLoginUser(HttpServletRequest request, HttpServletResponse response, SSOProperties properties, LoginUser user, boolean invalidate) {
		user.setLoginIp(CommonUtils.getIpAddress(request));
		user.setLoginTime(System.currentTimeMillis());
		ShardedJedis cache = properties.getCache();
		try {
			//String cacheUser = cache.get(user.toCacheKey());
			//LoginUser cacheuser = jsonMapper.fromJson(cacheUser, LoginUser.class);
			String objectJson = JSON.toJSONString(user);
			String result = cache.set(user.toCacheKey(), objectJson);
			if (result.equals("OK")) {
				setUserToSession(request, user);
				setUserToCookie(request, response, properties, user, invalidate);
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * 
	 * 清理当前登录状态
	 */
	public static boolean clearLoginUser(HttpServletRequest request, HttpServletResponse response, SSOProperties properties) {
		ShardedJedis cache = properties.getCache();
		/**
		 * 如果开启了缓存，删除缓存记录
		 */
		if (cache != null && !SSOConst.SSO_KICK_USER.equals(request.getAttribute(SSOConst.SSO_KICK_FLAG)) ) {
			LoginUser user = getUserFromCookie(request, properties);
			if (user != null ) {
				Long rlt = 0L;
				try {
					rlt = cache.del(user.toCacheKey());
					if (rlt.equals(1)) {
						cache.del(user.toCacheKey());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return CommonUtils.clearCookieByName(request, response, properties.getCookieName(), properties.getCookieDomain(), properties.getCookiePath());
	}

	/**
	 * 
	 * cookie中获取当前请求用户信息
	 */
	private static LoginUser getUserFromCookie(HttpServletRequest request, SSOProperties properties) {
		LoginUser user = null;
		String jsonToken = getJsonToken(request, properties, properties.getCookieName());
		if (StringUtils.isNotEmpty(jsonToken)) {
			user = FastJsonUtils.parseUser(jsonToken);
		}
		// 验证ip是否正确
		if (properties.isCookieCheckip()) {
			String ip = CommonUtils.getIpAddress(request);
			if (user != null && ip != null && !ip.equals(user.getLoginIp())) {
				return null;
			}
		}
		return user;
	}

	/**
	 * 
	 * 设置加密 Cookie（登录验证成功）
	 */
	private static void setUserToCookie(HttpServletRequest request, HttpServletResponse response,
			SSOProperties properties, LoginUser user, boolean invalidate) {
		if (invalidate) {
			CommonUtils.authJSESSIONID(request, MD5.getMD5Code(StringUtils.getRandomString(8)));
		}
		try {
			/**
			 * 设置加密 Cookie
			 */
			Cookie ck = generateCookie(request, properties, user);
			response.addCookie(ck);
		} catch (Exception e) {
			logger.severe("set HTTPOnly cookie createAUID is exception! " + e.toString());
		}
	}

	/**
	 * <p>
	 * 获取当前请求 JsonToken
	 * </p>
	 * 
	 * @param request
	 * @param encrypt
	 *            对称加密算法类
	 * @param cookieName
	 *            Cookie名称
	 * @return String 当前Token的json格式值
	 */
	private static String getJsonToken(HttpServletRequest request, SSOProperties properties, String cookieName) {
		Cookie uid = CommonUtils.findCookieByName(request, cookieName);
		if (uid != null) {
			String jsonToken = uid.getValue();
			String[] tokenAttr = new String[2];
			try {
				jsonToken = AES.RevertAESCode(jsonToken, properties.getSecretKey());
				tokenAttr = jsonToken.split(SSOConst.CUT_SYMBOL);
			} catch (Exception e) {
				logger.severe("jsonToken decrypt error, may be fake login. IP = " + CommonUtils.getIpAddress(request));
			}
			/**
			 * 判断是否认证浏览器 混淆信息
			 */
			if (properties.isCookieBrowser()) {
				if (CommonUtils.isLegalUserAgent(request, tokenAttr[0], tokenAttr[1])) {
					return tokenAttr[0];
				} else {
					/**
					 * 签名验证码失败
					 */
					logger.severe("SSOHelper getToken, find Browser is illegal. tokenAttr[0]:" + tokenAttr[0]
							+ ",tokenAttr[1]:" + tokenAttr[1]);
				}
			} else {
				/**
				 * 不需要认证浏览器信息混淆 返回JsonToken
				 */
				return tokenAttr[0];
			}
		}
		return null;
	}

	/**
	 * <p>
	 * 根据Token生成登录信息Cookie
	 * </p>
	 * 
	 * @param request
	 * @param token
	 *            SSO 登录信息票据
	 * @param encrypt
	 *            对称加密算法类
	 * @return Cookie 登录信息Cookie {@link Cookie}
	 */
	private static Cookie generateCookie(HttpServletRequest request, SSOProperties properties, LoginUser user) {
		try {
			if (user == null) {
				throw new SSOException("LoginUser not for null.");
			}
			/**
			 * token加密混淆
			 */
			String jt = FastJsonUtils.toJsonUser(user);
			StringBuffer sf = new StringBuffer();
			sf.append(jt);
			sf.append(SSOConst.CUT_SYMBOL);
			/**
			 * 判断是否认证浏览器信息 否取8位随机数混淆
			 */
			if (properties.isCookieBrowser()) {
				sf.append(CommonUtils.getUserAgent(request, jt));
			} else {
				sf.append(MD5.getMD5Code(StringUtils.getRandomString(8)));
			}
			String cookieString = AES.GetAESCode(sf.toString(), properties.getSecretKey());
			Cookie cookie = new Cookie(properties.getCookieName(), cookieString);
			cookie.setPath(properties.getCookiePath());
			/**
			 * domain 提示
			 * <p>
			 * 有些浏览器 localhost 无法设置 cookie
			 * </p>
			 */
			String domain = properties.getCookieDomain();
			if(StringUtils.isNotEmpty(domain)&&!domain.contains("localhost")) {
				cookie.setDomain(domain);
			} else {
				logger.warning("if you can't login, please enter normal domain. instead:" + domain);
			}
			/**
			 * 设置Cookie超时时间
			 */
			int maxAge = properties.getCookieMaxage();
			Integer attrMaxAge = (Integer) request.getAttribute(SSOConst.SSO_COOKIE_MAXAGE);
			if (attrMaxAge != null) {
				maxAge = attrMaxAge;
			}
			if (maxAge >= 0) {
				cookie.setMaxAge(maxAge);
			}
			return cookie;
		} catch (Exception e) {
			logger.severe("generateCookie is exception!" + e.toString());
			return null;
		}
	}

}