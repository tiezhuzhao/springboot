package com.fast.boot.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fast.boot.sso.SSOException;
import com.fast.boot.utils.lang.StringUtils;
import com.fast.boot.utils.tools.encrypt.MD5;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * 单点登录工具类
 */
public class CommonUtils {

	private static final Logger logger = Logger.getLogger("CommonUtils");

	/* 浏览器关闭时自动删除 */
	public final static int CLEAR_BROWSER_IS_CLOSED = -1;
	/* 立即删除 */
	public final static int CLEAR_IMMEDIATELY_REMOVE = 0;
	
	private static ShardedJedis jedis;
	
	/**
	 * 获取缓存类
	 * 
	 * @return {@link RedisClient}
	 */
	@SuppressWarnings("resource")
	public static ShardedJedis getConfigCache(String cacheHost, Integer cachePort, String passWord) {
		if (jedis != null) {
			return jedis;
		}
		try {
			if(StringUtils.isNotEmpty(cacheHost)&&cachePort!=null) {
				// 连接的 Redis 服务
				JedisPoolConfig config = new JedisPoolConfig();
		        config.setMaxTotal(100);
		        config.setMaxIdle(50);
		        config.setMaxWaitMillis(30000);
		        config.setTestOnBorrow(true);
		        config.setTestOnReturn(true);
		        // 集群
		        JedisShardInfo jedisShardInfo1 = new JedisShardInfo(cacheHost, cachePort);
		        jedisShardInfo1.setPassword(passWord);
		        List<JedisShardInfo> list = new LinkedList<JedisShardInfo>();
		        list.add(jedisShardInfo1);   
		        ShardedJedisPool pool = new ShardedJedisPool(config, list);
		        jedis = pool.getResource();
		       
			}
		} catch (Exception e) {
			throw new SSOException("sso Config 【 sso.cache-host】 【 sso.cache-port】 not find", e);
		}
		return jedis;
	}

	/**
	 * <p>
	 * 获取客户端的IP地址的方法是：request.getRemoteAddr()，这种方法在大部分情况下都是有效的。
	 * 但是在通过了Apache,Squid等反向代理软件就不能获取到客户端的真实IP地址了，如果通过了多级反向代理的话，
	 * X-Forwarded-For的值并不止一个，而是一串IP值， 究竟哪个才是真正的用户端的真实IP呢？
	 * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
	 * 例如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
	 * 192.168.1.100 用户真实IP为： 192.168.1.110
	 * </p>
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			if (ip.equals("127.0.0.1")) {
				/** 根据网卡取本机配置的IP */
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
					ip = inet.getHostAddress();
				} catch (UnknownHostException e) {
					logger.severe("IpHelper error." + e.toString());
				}
			}
		}
		/**
		 * 对于通过多个代理的情况， 第一个IP为客户端真实IP,多个IP按照','分割 "***.***.***.***".length() =
		 * 15
		 */
		if (ip != null && ip.length() > 15) {
			if (ip.indexOf(",") > 0) {
				ip = ip.substring(0, ip.indexOf(","));
			}
		}
		return ip;
	}

	/**
	 * 混淆浏览器版本信息
	 * 
	 * @Description 获取浏览器客户端信息签名值
	 * @param request
	 * @return
	 */
	public static String getUserAgent(HttpServletRequest request, String value) {
		StringBuffer sf = new StringBuffer();
		sf.append(value);
		sf.append("-");
		sf.append(request.getHeader("user-agent"));
		/**
		 * MD5 浏览器版本信息
		 */
		logger.fine("Browser info:" + sf.toString());
		return MD5.getMD5Code(sf.toString());
	}

	/**
	 * <p>
	 * 请求浏览器是否合法 (只校验客户端信息不校验domain)
	 * </p>
	 * 
	 * @param request
	 * @param userAgent
	 *            浏览器客户端信息
	 * @return
	 */
	public static boolean isLegalUserAgent(HttpServletRequest request, String value, String userAgent) {
		String rlt = getUserAgent(request, value);
		if (rlt.equalsIgnoreCase(userAgent)) {
			logger.fine("Browser isLegalUserAgent is legal. Browser getUserAgent:" + rlt);
			return true;
		}
		logger.fine("Browser isLegalUserAgent is illegal. Browser getUserAgent:" + rlt);
		return false;
	}

	/**
	 * 
	 * <p>
	 * 防止伪造SESSIONID攻击. 用户登录校验成功销毁当前JSESSIONID. 创建可信的JSESSIONID
	 * </p>
	 * 
	 * @param request
	 *            当前HTTP请求
	 * @param value
	 *            用户ID等唯一信息
	 */
	public static void authJSESSIONID(HttpServletRequest request, String value) {
		request.getSession().invalidate();
		request.getSession().setAttribute("BJHJ-" + value, true);
	}

	/**
	 * <p>
	 * 根据cookieName获取Cookie
	 * </p>
	 * 
	 * @param request
	 * @param cookieName
	 *            Cookie name
	 * @return Cookie
	 */
	public static Cookie findCookieByName(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null)
			return null;
		for (int i = 0; i < cookies.length; i++) {
			if (cookies[i].getName().equals(cookieName)) {
				return cookies[i];
			}
		}
		return null;
	}

	/**
	 * 
	 * <p>
	 * 根据 cookieName 清空 Cookie【默认域下】
	 * </p>
	 * 
	 * @param response
	 * @param cookieName
	 */
	public static void clearCookieByName(HttpServletResponse response, String cookieName) {
		Cookie cookie = new Cookie(cookieName, "");
		cookie.setMaxAge(CLEAR_IMMEDIATELY_REMOVE);
		response.addCookie(cookie);
	}

	/**
	 * 
	 * <p>
	 * 清除指定doamin的所有Cookie
	 * </p>
	 * 
	 * @param request
	 * @param response
	 * @param cookieName
	 *            cookie name
	 * @param domain
	 *            Cookie所在的域
	 * @param path
	 *            Cookie 路径
	 * @return
	 */
	public static void clearAllCookie(HttpServletRequest request, HttpServletResponse response, String domain,
			String path) {
		Cookie[] cookies = request.getCookies();
		for (int i = 0; i < cookies.length; i++) {
			clearCookie(response, cookies[i].getName(), domain, path);
		}
		logger.info("clearAllCookie in  domain " + domain);
	}

	/**
	 * 
	 * <p>
	 * 根据cookieName清除指定Cookie
	 * </p>
	 * 
	 * @param request
	 * @param response
	 * @param cookieName
	 *            cookie name
	 * @param domain
	 *            Cookie所在的域
	 * @param path
	 *            Cookie 路径
	 * @return boolean
	 */
	public static boolean clearCookieByName(HttpServletRequest request, HttpServletResponse response, String cookieName,
			String domain, String path) {
		boolean result = false;
		Cookie ck = findCookieByName(request, cookieName);
		if (ck != null) {
			result = clearCookie(response, cookieName, domain, path);
		}
		return result;
	}

	/**
	 * <p>
	 * 清除指定Cookie 等同于 clearCookieByName(...)
	 * </p>
	 * 
	 * <p>
	 * 该方法不判断Cookie是否存在,因此不对外暴露防止Cookie不存在异常.
	 * </p>
	 * 
	 * @param response
	 * @param cookieName
	 *            cookie name
	 * @param domain
	 *            Cookie所在的域
	 * @param path
	 *            Cookie 路径
	 * @return boolean
	 */
	private static boolean clearCookie(HttpServletResponse response, String cookieName, String domain, String path) {
		boolean result = false;
		try {
			Cookie cookie = new Cookie(cookieName, "");
			cookie.setMaxAge(CLEAR_IMMEDIATELY_REMOVE);
			cookie.setDomain(domain);
			cookie.setPath(path);
			response.addCookie(cookie);
			logger.fine("clear cookie " + cookieName);
			result = true;
		} catch (Exception e) {
			logger.severe("clear cookie " + cookieName + " is exception!\n" + e.toString());
		}
		return result;
	}

	/**
	 * 
	 * <p>
	 * 添加 Cookie
	 * </p>
	 * 
	 * @param response
	 * @param domain
	 *            所在域
	 * @param path
	 *            域名路径
	 * @param name
	 *            名称
	 * @param value
	 *            内容
	 * @param maxAge
	 *            生命周期参数
	 * @param httpOnly
	 *            只读
	 * @param secured
	 *            Https协议下安全传输
	 */
	public static void addCookie(HttpServletResponse response, String domain, String path, String name, String value,
			int maxAge, boolean httpOnly, boolean secured) {
		Cookie cookie = new Cookie(name, value);
		/**
		 * 不设置该参数默认 当前所在域
		 */
		if (domain != null && !"".equals(domain)) {
			cookie.setDomain(domain);
		}
		cookie.setPath(path);
		cookie.setMaxAge(maxAge);
		/** Cookie 只在Https协议下传输设置 */
		if (secured) {
			cookie.setSecure(secured);
		}
		/** Cookie 只读设置 */
		if (httpOnly) {
			addHttpOnlyCookie(response, cookie);
		} else {
			response.addCookie(cookie);
		}
	}

	/**
	 * 
	 * <p>
	 * 解决 servlet 3.0 以下版本不支持 HttpOnly
	 * </p>
	 * 
	 * @param response
	 *            HttpServletResponse类型的响应
	 * @param cookie
	 *            要设置httpOnly的cookie对象
	 */
	public static void addHttpOnlyCookie(HttpServletResponse response, Cookie cookie) {
		if (cookie == null) {
			return;
		}
		/**
		 * 依次取得cookie中的名称、值、 最大生存时间、路径、域和是否为安全协议信息
		 */
		String cookieName = cookie.getName();
		String cookieValue = cookie.getValue();
		int maxAge = cookie.getMaxAge();
		String path = cookie.getPath();
		String domain = cookie.getDomain();
		boolean isSecure = cookie.getSecure();
		StringBuffer sf = new StringBuffer();
		sf.append(cookieName + "=" + cookieValue + ";");
		if (maxAge >= 0) {
			sf.append("Max-Age=" + cookie.getMaxAge() + ";");
		}
		if (domain != null) {
			sf.append("domain=" + domain + ";");
		}
		if (path != null) {
			sf.append("path=" + path + ";");
		}
		if (isSecure) {
			sf.append("secure;HTTPOnly;");
		} else {
			sf.append("HTTPOnly;");
		}
		response.addHeader("Set-Cookie", sf.toString());
	}

}
