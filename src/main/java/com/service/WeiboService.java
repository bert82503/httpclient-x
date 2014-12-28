/*
 * Copyright (c)
 */
package com.service;

/**
 * 微博服务定义。
 * 
 * @author huagang.li
 * @since 1.0
 */
public interface WeiboService {
	/**
	 * 登录微博，返回重定向到淘宝注册页面的url。
	 * 
	 * @param username
	 *            微博账号(邮箱/会员帐号/手机号)
	 * @param password
	 *            登录密码
	 * @return
	 */
	String login(String username, String password);

}
