/*
 * Copyright (c)
 */
package com.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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

	/** 属性配置文件路径 */
	public static final String PROPERTY_FILE_PATH = "properties";

	/** HTTP配置文件 */
	private static final String HTTP_CONFIG_FILE = PROPERTY_FILE_PATH
			+ File.separator + "http.config.properties";

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

	private static final String TAOBAO_REGISTER_VERIFYCODE_URL = "taobao.register.verifycode.url";

	/**
	 * 获取淘宝注册页面的验证码URL。
	 * 
	 * @return
	 */
	public static String getTaobaoRegisterVerifycodeUrl() {
		return httpConfigs.getProperty(TAOBAO_REGISTER_VERIFYCODE_URL, "");
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

	// JavaScript
	/** JavaScript文件存放目录 */
	public static final String JAVASCRIPT_FILE_PATH = "javascript";

	/**
	 * 定位JavaScript文件存放的目录({@link #JAVASCRIPT_FILE_PATH})。
	 * 
	 * @param jsFileName
	 * @return
	 */
	private static String getJsFilePath(String jsFileName) {
		return JAVASCRIPT_FILE_PATH + File.separator + jsFileName;
	}

	/**
	 * 读取文件的所有内容。
	 * 
	 * @param file
	 * @return
	 */
	public static String readFileToString(File file) {
		String content = "";
		if (file.exists()) {
			try {
				content = FileUtils.readFileToString(file, Charsets.UTF_8);
			} catch (IOException ioe) {
				logger.error("File not found: " + file.getPath(), ioe);
			}
		}
		return content;
	}

	/**
	 * 写入字符串数据到文件中，会覆盖原先的文件内容。
	 * 
	 * @param file
	 * @param data
	 */
	public static void writeStringToFile(File file, String data) {
		try {
			FileUtils.writeStringToFile(file, data, Charsets.UTF_8, false);
		} catch (IOException ioe) {
			logger.error("File write failed: " + file.getPath(), ioe);
		}
	}

	private static final String WEIBO_LOGIN_REMOTE_JS_VERSION = "weibo.login.remote.js.version";

	/**
	 * 读取微博登录页面的"remote.js"文件的当前版本号信息。
	 * 
	 * @return
	 */
	public static String readWeiboLoginRemoteJsVersion() {
		String remoteJsVersion = httpConfigs.getProperty(
				WEIBO_LOGIN_REMOTE_JS_VERSION, "");
		File jsFile = new File(getJsFilePath(remoteJsVersion));
		return readFileToString(jsFile);
	}

	/**
	 * 写入微博登录页面的"remote.js"文件的最新版本号信息。
	 * 
	 * @param remoteJsVersionContent
	 */
	public static void writeWeiboLoginRemoteJsVersion(
			String remoteJsVersionContent) {
		String remoteJsVersion = httpConfigs.getProperty(
				WEIBO_LOGIN_REMOTE_JS_VERSION, "");
		File jsFile = new File(getJsFilePath(remoteJsVersion));
		writeStringToFile(jsFile, remoteJsVersionContent);
	}

	private static final String WEIBO_LOGIN_REMOTE_JS_CONTENT_MIDDLE = "weibo.login.remote.js.content.middle";

	/**
	 * 读取微博登录页面的"remote.js"文件的中间内容。
	 * 
	 * @return
	 */
	public static String readWeiboLoginRemoteJsContentMiddle() {
		String remoteJsMiddle = httpConfigs.getProperty(
				WEIBO_LOGIN_REMOTE_JS_CONTENT_MIDDLE, "");
		File jsFile = new File(getJsFilePath(remoteJsMiddle));
		return readFileToString(jsFile);
	}

	/**
	 * 写入微博登录页面的"remote.js"文件的中间内容到文件中。
	 * 
	 * @param remoteJsMiddleContent
	 */
	public static void writeWeiboLoginRemoteJsContentMiddle(
			String remoteJsMiddleContent) {
		String remoteJsMiddle = httpConfigs.getProperty(
				WEIBO_LOGIN_REMOTE_JS_CONTENT_MIDDLE, "");
		File jsFile = new File(getJsFilePath(remoteJsMiddle));
		writeStringToFile(jsFile, remoteJsMiddleContent);
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

	/**
	 * 读取类路径下(class path)的文件的所有内容，并返回。
	 * 
	 * @param fileName
	 * @return
	 */
	private static String readFileToStringInClasspath(String fileName) {
		try {
			return IOUtils.toString(getResourceAsStream(fileName),
					Charsets.UTF_8);
		} catch (IOException ioe) {
			logger.error("File not found: " + fileName, ioe);
		}
		return "";
	}

	private static final String WEIBO_LOGIN_REMOTE_JS_CONTENT_PREFIX = "weibo.login.remote.js.content.prefix";

	/**
	 * 读取微博登录页面的"remote.js"文件的前缀内容。
	 * 
	 * @return
	 */
	public static String readWeiboLoginRemoteJsContentPrefix() {
		String remoteJsPrefix = httpConfigs.getProperty(
				WEIBO_LOGIN_REMOTE_JS_CONTENT_PREFIX, "");
		return readFileToStringInClasspath(getJsFilePath(remoteJsPrefix));
	}

	private static final String WEIBO_LOGIN_REMOTE_JS_CONTENT_SUFFIX = "weibo.login.remote.js.content.suffix";

	/**
	 * 读取微博登录页面的"remote.js"文件的后缀内容。
	 * 
	 * @return
	 */
	public static String readWeiboLoginRemoteJsContentSuffix() {
		String remoteJsSuffix = httpConfigs.getProperty(
				WEIBO_LOGIN_REMOTE_JS_CONTENT_SUFFIX, "");
		return readFileToStringInClasspath(getJsFilePath(remoteJsSuffix));
	}

	private static final String WEIBO_LOGIN_VERIFY_CODE_URL = "weibo.login.verify.code.url";

	/**
	 * 获取微博登录页面的校验码URL。
	 * 
	 * @return
	 */
	public static String getWeiboLoginVerifyCodeUrl() {
		return httpConfigs.getProperty(WEIBO_LOGIN_VERIFY_CODE_URL, "");
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

}
