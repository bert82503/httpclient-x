/*
 * Copyright (c)
 */
package com.http;

import java.io.IOException;
import java.io.InputStream;
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

	/** HTTP配置文件 */
	private static final String HTTP_CONFIG_FILE = "properties/http.config.properties";

	/** 所有HTTP配置信息 */
	private static Properties httpConfigs;

	static {
		httpConfigs = new Properties();
		try {
			httpConfigs.load(HttpConfigUtils.class.getClassLoader()
					.getResourceAsStream(HTTP_CONFIG_FILE));
		} catch (IOException ioe) {
			logger.error("Http config file not found: " + HTTP_CONFIG_FILE, ioe);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Http config: {}", httpConfigs);
		}
	}

	private static final String HTTP_MAX_PER_ROUTE = "http.max.per.route";

	/**
	 * 获取"HTTP请求的每个域名的最大并发连接数"。
	 * 
	 * @return 当未配置该属性时，返回2。
	 */
	public static int getHttpMaxPerRoute() {
		return Integer.parseInt(httpConfigs
				.getProperty(HTTP_MAX_PER_ROUTE, "2"));
	}

	private static final String TAOBAO_SSL_CERT_FILE_NAME = "taobao.ssl.cert.file";

	/**
	 * 获取淘宝HTTPs请求证书的文件名称。
	 * 
	 * @return
	 */
	public static String getTaobaoSslCertFileName() {
		return httpConfigs.getProperty(TAOBAO_SSL_CERT_FILE_NAME, "");
	}

	private static final String TAOBAO_SSL_CERT_FILE_PASSWORD = "taobao.ssl.cert.file.pass";

	/**
	 * 获取淘宝HTTPs请求证书的文件密码。
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

	private static final String TAOBAO_WEIBO_LOGIN_URL_LINK_CLASS = "taobao.weibo.login.url.link.class";

	/**
	 * 从淘宝登录页面获取微博登录URL链接的class属性标识。
	 * 
	 * @return
	 */
	public static String getWeiboLoginUrlLinkClassFromTaobaoLoginPage() {
		return httpConfigs.getProperty(TAOBAO_WEIBO_LOGIN_URL_LINK_CLASS, "");
	}

	private static final String WEIBO_PRELOGIN_URL = "weibo.prelogin.url";

	/**
	 * 获取微博预登录URL。
	 * 
	 * @return
	 */
	public static String getWeiboPreloginUrl() {
		return httpConfigs.getProperty(WEIBO_PRELOGIN_URL, "");
	}

	private static final String WEIBO_PRELOGIN_URL_DEFAULT_PARAMS = "weibo.prelogin.url.default.params";

	/**
	 * 获取微博预登录URL的默认参数。
	 * 
	 * @return
	 */
	public static String getWeiboPreloginUrlDefaultParams() {
		return httpConfigs.getProperty(WEIBO_PRELOGIN_URL_DEFAULT_PARAMS, "");
	}

	private static final String LIANZHONG_USER = "lianzhong.user";

	/**
	 * 获取联众的用户名。
	 * 
	 * @return 返回联众用户名。
	 */
	public static String getLianZhongUser() {
		return httpConfigs.getProperty(LIANZHONG_USER, "");
	}

	private static final String LIANZHONG_PASS = "lianzhong.pass";

	/**
	 * 获取联众的用户密码。
	 * 
	 * @return 返回联众用户密码。
	 */
	public static String getLianZhongPass() {
		return httpConfigs.getProperty(LIANZHONG_PASS, "");
	}

	private static final String WEIBO_VERIFYCODE_URL = "weibo.verifycode.url";
	public static String getWeiboVerifycodeUrl() {
		return httpConfigs.getProperty(WEIBO_VERIFYCODE_URL,
				"http://login.sina.com.cn/cgi/pin.php");
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

	/**
	 * 获取资源文件的输入流。
	 * 
	 * @param fileName
	 * @return
	 */
	public static InputStream getResourceAsStream(String fileName) {
		return HttpConfigUtils.class.getClassLoader().getResourceAsStream(
				fileName);
	}

}
