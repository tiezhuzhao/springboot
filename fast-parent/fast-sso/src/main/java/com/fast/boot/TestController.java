package com.fast.boot;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.fast.boot.bean.LoginUser;
import com.fast.boot.sso.SSOHelper;
import com.fast.boot.sso.SSOProperties;
import com.fast.boot.utils.CommonUtils;
import com.fast.boot.utils.mapper.JsonMapper;

import redis.clients.jedis.ShardedJedis;

public class TestController {

	private final static JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();
	
	@Autowired
	private static SSOProperties properties;
	
	public static void main(String[] args) {
		LoginUser user = new LoginUser();
        user.setId(2L);
        user.setFlag(0);
        user.setName("hhy");
        user.setUsername("hhy");
        user.setUserType((short) 1);
        //String objectJson = JSON.toJSONString(user);
		//String result = jedis.set("1", objectJson);
		SSOHelper.setLoginUser(null, null, properties, user, true);
		ShardedJedis jedis = CommonUtils.getConfigCache("106.14.159.196", 6379, "redis106141591966379");
		System.err.println(jedis.get("1"));
        LoginUser cacheuser = jsonMapper.fromJson(jedis.get("1"), LoginUser.class);
        System.err.println(JSON.toJSONString(cacheuser));
	}
}
