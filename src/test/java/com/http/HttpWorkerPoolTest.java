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
		httpWorker = new HttpWorkerPool();
		httpWorker.addHttpHost(HttpConfigUtils.getWeiboHost());
	}

	@Test
	public void get() {
		String taobaoLoginUrl = HttpConfigUtils.getTaobaoLoginUrl();
		String html = httpWorker.getString(taobaoLoginUrl);
		Document doc = Jsoup.parse(html);
		Element weiboLoginLink = doc.select("a.weibo-login").first();
		String link = weiboLoginLink.attr("href");
		logger.debug("Weibo login url: {}", link);
		assertTrue(link.contains("http://weibo.com/login.php"));
	}

	@AfterClass
	public void destroy() throws IOException {
		httpWorker.close();
	}

}
