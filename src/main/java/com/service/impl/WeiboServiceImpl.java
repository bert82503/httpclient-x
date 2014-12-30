/*
 * Copyright (c)
 */
package com.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.http.HttpConfigUtils;
import com.http.HttpWorkerPool;
import com.hz.util.JsUtil;
import com.hz.util.NumberUtil;
import com.hz.util.StringUtils;
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

	private static final ConcurrentMap<String, String> javaScriptMap;

	static {
		javaScriptMap = new ConcurrentHashMap<String, String>();
		String remoteJsVersion = HttpConfigUtils
				.readWeiboLoginRemoteJsVersion();
		if (StringUtils.isNotEmpty(remoteJsVersion)) {
			String weiboSSOEncoderJs = HttpConfigUtils
					.readWeiboLoginRemoteJsContentMiddle();
			String remoteJs = appendRemoteJs(weiboSSOEncoderJs);
			javaScriptMap.put(remoteJsVersion, remoteJs);
		}
	}

	private final HttpWorkerPool httpWorker;

	private final Map<String, String> loginWeiboParamsMap;

	public WeiboServiceImpl() {
		httpWorker = HttpWorkerPool.getInstance();

		loginWeiboParamsMap = getLoginWeiboParamsMap();
	}

	/**
	 * 拼装"remote.js"内容，并返回拼装后的内容。
	 * 
	 * @param remoteJsContentMiddle
	 * @return
	 */
	private static String appendRemoteJs(String remoteJsContentMiddle) {
		String remoteJsContentPrefix = HttpConfigUtils
				.readWeiboLoginRemoteJsContentPrefix();
		String remoteJsContentSuffix = HttpConfigUtils
				.readWeiboLoginRemoteJsContentSuffix();

		int capacity = remoteJsContentPrefix.length()
				+ remoteJsContentMiddle.length()
				+ remoteJsContentSuffix.length();
		StringBuilder buffer = new StringBuilder(capacity);
		buffer.append(remoteJsContentPrefix).append(remoteJsContentMiddle)
				.append(remoteJsContentSuffix);
		return buffer.toString();
	}

	/**
	 * function description.
	 * 
	 * 
	 * @return
	 */
	private Map<String, String> getLoginWeiboParamsMap() {
		Map<String, String> loginWeiboParamsMap = new HashMap<String, String>();
		loginWeiboParamsMap.put("door", "");
		loginWeiboParamsMap.put("encoding", "UTF-8");
		loginWeiboParamsMap.put("entry", "weibo");
		loginWeiboParamsMap.put("from", "");
		loginWeiboParamsMap.put("gateway", "1");
		loginWeiboParamsMap.put("nonce", "");
		loginWeiboParamsMap.put("pagerefer", "");
		loginWeiboParamsMap.put("pcid", "");
		loginWeiboParamsMap.put("prelt", "269");
		loginWeiboParamsMap.put("pwencode", "rsa2");
		loginWeiboParamsMap.put("returntype", "META");
		loginWeiboParamsMap.put("rsakv", "");
		loginWeiboParamsMap.put("savestate", "0");
		loginWeiboParamsMap.put("servertime", "");
		loginWeiboParamsMap.put("service", "miniblog");
		loginWeiboParamsMap.put("sp", "");
		loginWeiboParamsMap.put("sr", "1280*800");
		loginWeiboParamsMap.put("su", "");
		loginWeiboParamsMap.put("useticket", "1");
		loginWeiboParamsMap.put("vsnf", "1");
		return loginWeiboParamsMap;
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
	 * 获取 pcid。 <br>
	 * 请求验证码时需要用到三个参数：r，s，p（pcid）
	 */
	private String getPcid(String username) {
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
			JSONObject jsonObj = JSON.parseObject(retJson);
			loginWeiboParamsMap.put("servertime",
					jsonObj.getString("servertime"));
			loginWeiboParamsMap.put("nonce", jsonObj.getString("nonce"));
			loginWeiboParamsMap.put("pubkey", jsonObj.getString("pubkey"));
			loginWeiboParamsMap.put("rsakv", jsonObj.getString("rsakv"));
			loginWeiboParamsMap.put("pcid", jsonObj.getString("pcid"));
			return jsonObj.getString("pcid");
		}

		return "";
	}

	/**
	 * 从微博登录页面获取'remote.js' URL。
	 * 
	 * @param weiboLoginUrl
	 * @return
	 */
	private String getRemoteJSUrl(String weiboLoginUrl) {
		String weiboLoginHtml = httpWorker.getString(weiboLoginUrl);
		Document doc = Jsoup.parse(weiboLoginHtml);
		Element remoteJs = doc.body()
				.getElementsByAttributeValueContaining("src", "remote.js")
				.first();
		return remoteJs.attr("src");
	}

	private static final String remoteJSKeySeparator = "/";

	/**
	 * 生成'remote.js'脚本文件的Key。
	 * 
	 * @param remoteJSUrl
	 * @return
	 */
	private static String generateRemoteJSKey(String remoteJSUrl) {
		return remoteJSUrl.substring(remoteJSUrl
				.lastIndexOf(remoteJSKeySeparator)
				+ remoteJSKeySeparator.length());
	}

	private static final String sinaSSOEncoderPrefix = "var sinaSSOEncoder";
	private static final String sinaSSOEncoderSuffix = "call(sinaSSOEncoder);";

	/**
	 * 获取微博登录SSO编码器的JavaScript代码。
	 * 
	 * @param remoteJSUrl
	 * @return
	 */
	private String getWeiboSSOEncoderJS(String remoteJSUrl) {
		String remoteJSKey = generateRemoteJSKey(remoteJSUrl);
		String weiboSSOEncoderJs = javaScriptMap.get(remoteJSKey);
		if (weiboSSOEncoderJs != null) {
			return weiboSSOEncoderJs;
		}

		// 首次请求或有新版本JS发布
		String remoteJS = httpWorker.getString(remoteJSUrl);
		if (StringUtils.isNotEmpty(remoteJS)) {
			BufferedReader reader = new BufferedReader(new StringReader(
					remoteJS));
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					int beginIndex = line.indexOf(sinaSSOEncoderPrefix);
					if (beginIndex >= 0) {
						int endIndex = line.lastIndexOf(sinaSSOEncoderSuffix)
								+ sinaSSOEncoderSuffix.length();
						weiboSSOEncoderJs = line
								.substring(beginIndex, endIndex);
						HttpConfigUtils
								.writeWeiboLoginRemoteJsVersion(remoteJSKey);
						HttpConfigUtils
								.writeWeiboLoginRemoteJsContentMiddle(weiboSSOEncoderJs);
						String remoteJs = appendRemoteJs(weiboSSOEncoderJs);
						javaScriptMap.put(remoteJSKey, remoteJs);
						return remoteJs;
					}
				}
			} catch (IOException ioe) {
				logger.error("'remote.js' read failed", ioe);
			}
		}
		return null;
	}

	@Override
	public String login(String username, String password) {
		String weiboLoginUrl = this.getWeiboLoginUrl();
		logger.debug("Weibo login url: {}", weiboLoginUrl);
		String pcid = getPcid(username);
		logger.debug("Weibo login url pcid: {}", pcid);

		String remoteJSUrl = this.getRemoteJSUrl(weiboLoginUrl);
		logger.debug("Weibo 'remote.js' url: {}", remoteJSUrl);
		String remoteJs = this.getWeiboSSOEncoderJS(remoteJSUrl);
		logger.debug("Weibo 'remote.js' content: {}", remoteJs);

		// String imgUrl = getImgUrlOfWeiboVerifyCode(pcid);
		// logger.debug("Weibo login url imgUrl: {}", imgUrl);
		// String verifyCode = OCRParser.parseOCR(imgUrl);//
		// loginWeiboParamsMap.put("su", encodeUsername(username));
		// loginWeiboParamsMap.put("sp", encodePassword(password));
		// loginWeiboParamsMap.put("door", verifyCode);
		// loginWeiboParamsMap.put("pcid", pcid);
		// return doLogin(weiboLoginUrl);
		return null;
	}

	/**
	 * 从淘宝入口登录微博.
	 * 
	 * @param username
	 *            ：用户名
	 * @param password
	 *            ：密码
	 * @param verifyCode
	 *            ：验证码
	 */
	private String doLogin(String weiboLoginUrl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : loginWeiboParamsMap.entrySet()) {
			params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		return httpWorker.post(weiboLoginUrl, params);
	}

	/**
	 * 调用Java的Js执行引擎根据对密码等字段进行加密.
	 * 
	 * @param pwd
	 * @return 明文密码内容
	 */
	private String encodePassword(String pwd) {
		return JsUtil.getSpParamOfWeiboLogin(pwd,
				loginWeiboParamsMap.get("servicetime"),
				loginWeiboParamsMap.get("nonce"),
				loginWeiboParamsMap.get("pubkey"));
	}

	/**
	 * function description.
	 * 
	 * 
	 * @return
	 */
	private String getTaobaoRegisterUrl() {
		// TODO
		return null;
	}

	/**
	 * 获取微博验证码url.
	 * 
	 * @param p
	 *            ：pcid
	 * @return
	 */
	private String getImgUrlOfWeiboVerifyCode(String p) {
		return getImgUrlOfWeiboVerifyCode(NumberUtil.getWeiboMathRandomNum(), p);
	}

	/**
	 * 获取微博验证码url.
	 * 
	 * @param r
	 *            ：随机数
	 * @param p
	 *            ：pcid值
	 * @return
	 */
	private String getImgUrlOfWeiboVerifyCode(String r, String p) {
		return getImgUrlOfWeiboVerifyCode(r, "0", p);
	}

	/**
	 * 获取微博验证码url.<br>
	 * http://login.sina.com.cn/cgi/pin.php?r=20703705&s=0&p=gz-1
	 * c1d08a7f7b5a9f4f1387a6feaab940c7556
	 * 
	 * @param r
	 *            ：随机数
	 * @param s
	 *            ：一般为0
	 * @param p
	 *            ：pcid
	 * @return
	 */
	private String getImgUrlOfWeiboVerifyCode(String r, String s, String p) {
		StringBuilder sb = new StringBuilder(
				HttpConfigUtils.getWeiboLoginVerifyCodeUrl());
		sb.append("?r=").append(r).append("&s=").append("&p=").append(p);
		return sb.toString();
	}

}
