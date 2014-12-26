/*
 * Copyright (c)
 */
package com.http;

import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Test for {@link HttpWorkerPool}.
 * 
 * @author huagang.li
 * @since 1.0
 */
public class HttpWorkerPoolTest {

	private static final Logger logger = LoggerFactory
			.getLogger(HttpWorkerPoolTest.class);

	private HttpWorkerPool httpWorker;

	@BeforeClass
	public void init() throws GeneralSecurityException {
		httpWorker = HttpWorkerPool.getInstance();
	}

	@Test(description = "验证从淘宝登录页面获取微博登录URL")
	public void getString() {
		String taobaoLoginUrl = HttpConfigUtils.getTaobaoLoginUrl();
		String taobaoLoginHtml = httpWorker.getString(taobaoLoginUrl);
		Document doc = Jsoup.parse(taobaoLoginHtml);
		Element weiboLoginUrlLink = doc.select(
				HttpConfigUtils.getWeiboLoginUrlLinkClassFromTaobaoLoginPage())
				.first();
		String weiboLoginUrl = weiboLoginUrlLink.attr("href");
		logger.debug("Weibo login url: {}", weiboLoginUrl);
		assertTrue(weiboLoginUrl.contains("http://weibo.com/login.php"));
	}

	@AfterClass
	public void destroy() throws IOException {
		httpWorker.close();
	}

}
