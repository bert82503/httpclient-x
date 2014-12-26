/*
 * Copyright (c)
 */
package com.service;

import static org.testng.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.binary.Base64;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.service.impl.WeiboServiceImpl;

/**
 * Test for {@link WeiboService}.
 * 
 * @author huagang.li
 * @since 1.0
 */
public class WeiboServiceTest {

	private WeiboService weiboService;

	@BeforeClass
	public void init() {
		weiboService = new WeiboServiceImpl();
	}

	@Test
	public void login() {
		String username = "hanhuaim98184@163.com";
		String password = "a123456";
		weiboService.login(username, password);
	}

	@Test(dataProvider = "encodeUsername", description = "验证weibo的登录账号编码方式")
	public void encodeUsername(String username, String expected)
			throws UnsupportedEncodingException {
		assertEquals(Base64.encodeBase64String(URLEncoder.encode(username,
						CharEncoding.UTF_8).getBytes()), expected);
	}

	@DataProvider(name = "encodeUsername")
	protected static final Object[][] encodeUsernameTestData() {
		Object[][] testData = new Object[][] {//
				//
				{ "bopengz17305@163.com", "Ym9wZW5nejE3MzA1JTQwMTYzLmNvbQ==" },//
				{ "qiushiv01328@163.com", "cWl1c2hpdjAxMzI4JTQwMTYzLmNvbQ==" },//
				{ "pulann65561@163.com", "cHVsYW5uNjU1NjElNDAxNjMuY29t" },//
				{ "lancib000057@163.com", "bGFuY2liMDAwMDU3JTQwMTYzLmNvbQ==" },//
				{ "renyaon046242@163.com", "cmVueWFvbjA0NjI0MiU0MDE2My5jb20=" },//
				{ "junxunjk968563@163.com", "anVueHVuams5Njg1NjMlNDAxNjMuY29t" },//
				{ "bopengz17305@163.com", "Ym9wZW5nejE3MzA1JTQwMTYzLmNvbQ==" },//
				{ "tiaoyajs289538@163.com", "dGlhb3lhanMyODk1MzglNDAxNjMuY29t" },//
				{ "hanhuaim98184@163.com", "aGFuaHVhaW05ODE4NCU0MDE2My5jb20=" },//
		};
		return testData;
	}

	@Test(dataProvider = "encodeParam")
	public void encodeParam(String param, String expected)
			throws UnsupportedEncodingException {
		assertEquals(URLEncoder.encode(param, CharEncoding.UTF_8),
				expected);
	}

	@DataProvider(name = "encodeParam")
	protected static final Object[][] encodeParamTestData() {
		Object[][] testData = new Object[][] {//
				//
				{ "Ym9wZW5nejE3MzA1JTQwMTYzLmNvbQ==",
						"Ym9wZW5nejE3MzA1JTQwMTYzLmNvbQ%3D%3D" },//
				{ "cWl1c2hpdjAxMzI4JTQwMTYzLmNvbQ==",
						"cWl1c2hpdjAxMzI4JTQwMTYzLmNvbQ%3D%3D" },//
				{ "cHVsYW5uNjU1NjElNDAxNjMuY29t",
						"cHVsYW5uNjU1NjElNDAxNjMuY29t" },//
				{ "bGFuY2liMDAwMDU3JTQwMTYzLmNvbQ==",
						"bGFuY2liMDAwMDU3JTQwMTYzLmNvbQ%3D%3D" },//
				{ "cmVueWFvbjA0NjI0MiU0MDE2My5jb20=",
						"cmVueWFvbjA0NjI0MiU0MDE2My5jb20%3D" },//
				{ "anVueHVuams5Njg1NjMlNDAxNjMuY29t",
						"anVueHVuams5Njg1NjMlNDAxNjMuY29t" },//
				{ "Ym9wZW5nejE3MzA1JTQwMTYzLmNvbQ==",
						"Ym9wZW5nejE3MzA1JTQwMTYzLmNvbQ%3D%3D" },//
				{ "dGlhb3lhanMyODk1MzglNDAxNjMuY29t",
						"dGlhb3lhanMyODk1MzglNDAxNjMuY29t" },//
				{ "aGFuaHVhaW05ODE4NCU0MDE2My5jb20=",
						"aGFuaHVhaW05ODE4NCU0MDE2My5jb20%3D" },//
		};
		return testData;
	}

	@Test
	public void getTime() {
		assertEquals(new Date(1419608681058L).getTime(), 1419608681058L);
		// expected [1419608681058] but found [1419609482860]
		// assertEquals(System.currentTimeMillis(), 1419608681058L);
	}

}
