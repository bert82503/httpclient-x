/*
 * Copyright (c)
 */
package com.service;

/**
 * Class description goes here. (A brief description of the purpose.)
 * 
 * @author Daqiang.Yan
 * @since 1.0
 */
public interface TaobaoService {
	/**
	 * 微博登录成功后跳转到淘宝注册页面，注册淘宝账户
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            登录密码
	 * @param country
	 *            国家
	 * @return
	 */
	String register(String username, String password, String country);

}
