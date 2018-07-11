package com.fast.boot.utils;

import com.alibaba.fastjson.JSON;
import com.fast.boot.bean.LoginUser;


/**
 * JSON数据解析
 */
public class FastJsonUtils {

	public static String toJsonUser(LoginUser user) {
		return JSON.toJSONString(user);
	}
	public static LoginUser parseUser(String userJson) {
		return JSON.parseObject(userJson, LoginUser.class);
	}

}
