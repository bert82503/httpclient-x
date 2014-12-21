/*
 * Copyright (c)
 */
package com.http;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

/**
 * Test for {@link HttpConfigUtils}.
 * 
 * @author huagang.li
 * @since 1.0
 */
public class HttpConfigUtilsTest {

	@Test
	public void getTaobaoLoginUrl() {
		assertEquals(
				HttpConfigUtils.getTaobaoLoginUrl(),
				"https://login.taobao.com/member/login.jhtml?goto=http%3A%2F%2Fwww.taobao.com%2F");
	}

}
