/*
 * Copyright (c)
 */
package com.http;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test for {@link HttpUtils}.
 * 
 * @author huagang.li
 * @since 1.0
 */
public class HttpUtilsTest {

	@Test(dataProvider = "getHostname")
	public void getHostname(String uri, String expected) {
		assertEquals(HttpUtils.getHostname(uri), expected);
	}

	@DataProvider(name = "getHostname")
	protected static final Object[][] getHostnameTestData() {
		Object[][] testData = new Object[][] {
				{
						"https://login.taobao.com/member/login.jhtml?goto=http%3A%2F%2Fwww.taobao.com%2Flogin.taobao.com",
						"login.taobao.com" },
				{
						"https://login.taobao.com?goto=http%3A%2F%2Fwww.taobao.com%2Flogin.taobao.com",
						"login.taobao.com" }, };
		return testData;
	}

}
