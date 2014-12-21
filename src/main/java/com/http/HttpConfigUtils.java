/*
 * Copyright (c)
 */
package com.http;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP 配置文件解析工具类。
 * 
 * @author huagang.li
 * @since 1.0
 */
public class HttpConfigUtils {

	private static final Logger logger = LoggerFactory
			.getLogger(HttpConfigUtils.class);

	private static final String HTTP_CONFIG_FILE = "http.config.properties";

	private static Properties httpConfigs;

	static {
		httpConfigs = new Properties();
		try {
			httpConfigs.load(HttpConfigUtils.class.getClassLoader()
					.getResourceAsStream(HTTP_CONFIG_FILE));
		} catch (IOException ioe) {
			ioe.printStackTrace();
			logger.error("Http config file not found: " + HTTP_CONFIG_FILE, ioe);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Http config: {}", httpConfigs);
		}
	}

	private static final String TAOBAO_LOGIN_HOST = "taobao.login.host";

	/**
	 * 获取淘宝登陆页面域名。
	 * 
	 * @return
	 */
	public static String getTaobaoLoginHost() {
		return httpConfigs.getProperty(TAOBAO_LOGIN_HOST, "");
	}

	private static final String TAOBAO_SSL_CERT_FILE_NAME = "taobao.ssl.cert.file";

	/**
	 * 获取淘宝HTTPs请求证书文件名称。
	 * 
	 * @return
	 */
	public static String getTaobaoSslCertFileName() {
		return httpConfigs.getProperty(TAOBAO_SSL_CERT_FILE_NAME, "");
	}

	private static final String TAOBAO_SSL_CERT_FILE_PASSWORD = "taobao.ssl.cert.file.password";

	/**
	 * 获取淘宝HTTPs请求证书文件密码。
	 * 
	 * @return
	 */
	public static String getTaobaoSslCertFilePassword() {
		return httpConfigs.getProperty(TAOBAO_SSL_CERT_FILE_PASSWORD, "");
	}
	
	private static final String TAOBAO_LOGIN_URL = "taobao.login.url";

	/**
	 * 获取淘宝登陆页面URL。
	 * 
	 * @return 当在配置文件中已配置{@code taobao.login.url}时，返回淘宝登陆页面URL；否则，返回空字符串("")。
	 */
	public static String getTaobaoLoginUrl() {
		return httpConfigs.getProperty(TAOBAO_LOGIN_URL, "");
	}

	private static final String WEIBO_HOST = "weibo.host";

	/**
	 * 获取微博登陆页面域名。
	 * 
	 * @return
	 */
	public static String getWeiboHost() {
		return httpConfigs.getProperty(WEIBO_HOST, "");
	}

	/**
	 * 获取给定key的配置值。
	 * <p>
	 * 使用示例：
	 * 
	 * <pre>
	 * {@code HttpConfigUtils.getProperty("key", "")}
	 * </pre>
	 * 
	 * @param key
	 *            键
	 * @param defaultValue
	 *            如果未配置过给定key，则返回这个值
	 * @return 当未配置过给定key，返回{@code defaultValue}值。
	 */
	public static String getProperty(String key, String defaultValue) {
		return httpConfigs.getProperty(key, defaultValue);
	}

}
