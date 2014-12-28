/*
 * Copyright (c)
 */
package com.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.http.HttpConfigUtils;
import com.http.HttpWorkerPool;
import com.service.WeiboService;

/**
 * 微博登录服务实现。
 * 
 * @author huagang.li
 * @since 1.0
 */
public class WeiboServiceImpl implements WeiboService {

	private static final Logger logger = LoggerFactory
			.getLogger(WeiboServiceImpl.class);

	private final HttpWorkerPool httpWorker;

	public WeiboServiceImpl() {
		httpWorker = HttpWorkerPool.getInstance();
	}

	/**
	 * 获取微博登录URL。
	 * 
	 * @return
	 */
	private String getWeiboLoginUrl() {
		String taobaoLoginUrl = HttpConfigUtils.getTaobaoLoginUrl();
		logger.debug("Taobao login url: {}", taobaoLoginUrl);
		String taobaoLoginHtml = httpWorker.getString(taobaoLoginUrl);
		Document doc = Jsoup.parse(taobaoLoginHtml);
		Element weiboLoginUrlLink = doc.select(
				HttpConfigUtils.getWeiboLoginUrlLinkClassFromTaobaoLoginPage())
				.first();
		return weiboLoginUrlLink.attr("href");
	}

	/**
	 * 对微博账号进行编码。
	 * 
	 * @param username
	 * @return
	 */
	private static String encodeUsername(String username) {
		try {
			return Base64.encodeBase64String(URLEncoder.encode(username,
					CharEncoding.UTF_8).getBytes());
		} catch (UnsupportedEncodingException e) {
			logger.error("Unsupported 'UTF-8' Encoding", e);
		}
		return username;
	}

	/**
	 * 对请求参数进行编码。
	 * 
	 * @param param
	 * @return
	 */
	private static String encodeParam(String param) {
		try {
			return URLEncoder.encode(param, CharEncoding.UTF_8);
		} catch (UnsupportedEncodingException e) {
			logger.error("Unsupported 'UTF-8' Encoding", e);
		}
		return param;
	}

	/**
	 * 获取请求操作的时间(ms)。
	 * 
	 * @return
	 */
	private static long getRequestTime() {
		return System.currentTimeMillis();
	}

	/**
	 * 获取 pcid。
	 */
	private String getPcId(String username) {
		StringBuilder urlBuilder = new StringBuilder(
				HttpConfigUtils.getWeiboPreloginUrl());
		urlBuilder.append('?')
				.append(HttpConfigUtils.getWeiboPreloginUrlDefaultParams())
				.append("&su=").append(encodeParam(encodeUsername(username)))//
				.append("&_=").append(getRequestTime());
		String url = urlBuilder.toString();
		logger.debug("Weibo prelogin url: {}", url);

		String preloginCallBack = httpWorker.getString(url);
		logger.debug("Weibo prelogin response: {}", preloginCallBack);
		if (StringUtils.isNotBlank(preloginCallBack)) {
			String retJson = preloginCallBack.split("[()]")[1];
			return JSON.parseObject(retJson).getString("pcid");
		}

		return "";
	}

	@Override
	public boolean login(String username, String password) {
		// TODO Auto-generated method stub
		String weiboLoginUrl = this.getWeiboLoginUrl();
		logger.debug("Weibo login url: {}", weiboLoginUrl);
		String pcId = this.getPcId(username);
		logger.debug("'pcid' of weibo prelogin response: {}", pcId);
		return false;
	}

}
